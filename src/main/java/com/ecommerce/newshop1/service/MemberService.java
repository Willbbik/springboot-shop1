package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.config.CustomUserDetailsService;
import com.ecommerce.newshop1.dto.JoinMemberDto;
import com.ecommerce.newshop1.dto.MemberDto;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.repository.MemberRepository;
import com.ecommerce.newshop1.utils.enums.Role;
import com.ecommerce.newshop1.utils.enums.Sns;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final RedisService redisService;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(MemberService.class);
    ModelMapper mapper = new ModelMapper();

    String idPattern = "^[a-z0-9][a-z0-9_\\-]{4,20}$";
    String pswdPattern = "^(?=.*[a-z0-9])(?=.*[A-Za-z0-9~`!@#$%\\^&*()-]).{8,25}$";
    String phonePattern = "^(010[1-9][0-9]{7})$";
    String authPattern  = "^[0-9]{6}$";


    // 아이디 찾기
    @Transactional(readOnly = true)
    public Optional<Member> findByUserId(String userId){

        return memberRepository.findByuserId(userId);
    }

    // 일반 회원가입
    @Transactional
    public Member joinNormal(MemberDto memberDto){

        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        memberDto.setRole(Role.MEMBER);
        memberDto.setSns(Sns.NONE);
        Member member = mapper.map(memberDto, Member.class);

        memberRepository.save(member);
        return member;
    }

    // oAuth2로 회원가입
    @Transactional
    public void joinOAuth(String id) {

        MemberDto memberDto = MemberDto.builder()
                .userId(id)
                .role(Role.MEMBER)
                .sns(Sns.KAKAO)
                .build();
        Member member = mapper.map(memberDto, Member.class);
        try{
            memberRepository.save(member);
        } catch (Exception e){
            log.info("MemberService 355 line, oAuthJoin exception ");
        }
    }

    // sns 값 찾기
    @Transactional(readOnly = true)
    public Sns findSnsByUserId(String userid){
        return memberRepository.findSnsByUserId(userid);
    }


    // 회원가입 객체 유효성 검사 메소드
    public int joinValidationCheck(JoinMemberDto dto) throws Exception {

        // 아이디 중복검사 && 인증번호 검사
        Optional<Member> memberEntity = memberRepository.findByuserId(dto.getUserId());
        int authNum = redisService.getAuthNum(dto.getPhoneNum());
        int authNumCheckResult = redisService.authNumCheck(dto.getPhoneNum(), dto.getAuthNum());

        // 문제가 있으면
        if(memberEntity.isPresent() || !dto.getPassword().equals(dto.getPswdCheck()) || authNum == 1 || authNumCheckResult != 1) {

            log.info("MemberService : join validation check failed");
            return -1;
        }

        // 정상적이라면
        return 0;
    }

    // 회원가입 에러 메시지 담기
    @Transactional(readOnly = true)
    public Model createJoinDtoErrorMsg(JoinMemberDto dto,  Model model) throws Exception {

        String userid   = dto.getUserId();
        String pswd1    = dto.getPassword();
        String pswd2    = dto.getPswdCheck();
        String phoneNum = dto.getPhoneNum();
        String dtoAuthNum  = dto.getAuthNum();

        // realAuthNum가 1이면 일치, 2면 다름, 3이면 db에 인증번호가 존재하지 않음
        int realAuthNum = redisService.authNumCheck(phoneNum, dtoAuthNum);        // 인증번호 검사
        Optional<Member> memberEntity = memberRepository.findByuserId(userid);

        // 아이디 검사
        if (memberEntity.isPresent()) {	 // 아이디가 존재할때
            model.addAttribute("valid_userId", "이미 사용중이거나 탈퇴한 아이디입니다.");
        }
        // 비밀번호 비교
        if (!pswd1.equals(pswd2)) {		 // 비밀번호가 다를때
            model.addAttribute("valid_pswdCheck","비밀번호가 일치하지 않습니다.");
        }

        // 인증번호 검사
        if (realAuthNum == 3) {          // 인증번호가 시간이 지나서 사라졌거나 db에 인증번호가 없을때
            model.addAttribute("valid_authNum", "인증번호를 다시 확인해주세요.");
        }else if(realAuthNum == 2){      // 인증번호가 다를 때
            model.addAttribute("valid_authNum", "인증을 다시 진행해주세요.");
        }

        return model;
    }


    // view에 띄워줄 회원가입 객체 길이 조정 메소드
    public JoinMemberDto joinDtoLengthEdit(JoinMemberDto memberDto){

        String[] dtoBefore = {memberDto.getUserId(), memberDto.getPassword(), memberDto.getPswdCheck(), memberDto.getPhoneNum(), memberDto.getAuthNum()};
        int[] maxlength = {20, 25, 25, 11, 6};

        String[] dtoAfter = new String[5];

        for(int i = 0; i < 5; i++){
            if(dtoBefore[i].length() > maxlength[i]){
                dtoAfter[i] = dtoBefore[i].substring(0, maxlength[i]);
            }else{
                dtoAfter[i] = dtoBefore[i];
            }
        }

        return JoinMemberDto.builder()
                .userId(dtoAfter[0])
                .password(dtoAfter[1])
                .pswdCheck(dtoAfter[2])
                .phoneNum(dtoAfter[3])
                .authNum(dtoAfter[4])
                .build();
    }

    // 에러메시지 생성
    public Map<String, String> getErrorMsg(Errors errors){
        Map<String, String> map = new HashMap<>();
        for(FieldError error : errors.getFieldErrors()){

            if(map.get("valid_" + error.getField()) != null){
                continue;
            }

            String key = String.format("valid_%s", error.getField());
            map.put(key, error.getDefaultMessage());
        }
        return map;
    }

    // 사용자 아이디 찾기
//    @Transactional
//    public Object findByUserId(String userid){
//        Optional<MemberEntity> optMemberEntity = memberRepository.findByuserid(userid);
//        if(optMemberEntity.isPresent()){
//            MemberEntity memberEntity = optMemberEntity.get();
//
//           return MemberDto.builder()
//                    .id(memberEntity.getId())
//                    .userid(memberEntity.getUserid())
//                    .pswd(memberEntity.getPassword())
//                    .role(memberEntity.getRole())
//                    .sns(memberEntity.getSns())
//                    .phonenum(memberEntity.getPhonenum())
//                    .build();
//        }else{
//            return null;
//        }
//    }

}
