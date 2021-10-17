package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.JoinMemberDto;
import com.ecommerce.newshop1.dto.MemberDto;
import com.ecommerce.newshop1.entity.MemberEntity;
import com.ecommerce.newshop1.repository.MemberRepository;
import com.ecommerce.newshop1.utils.CustomUserDetailsService;
import com.ecommerce.newshop1.utils.enums.JoinMsg;
import com.ecommerce.newshop1.utils.enums.LoginMsg;
import com.ecommerce.newshop1.utils.enums.Role;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.Optional;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(MemberService.class);

    String idPattern = "^[a-z0-9][a-z0-9_\\-]{4,20}$";
    String pswdPattern = "^(?=.*[a-z0-9])(?=.*[A-Za-z0-9~`!@#$%\\^&*()-]).{8,25}$";
    String phonePattern = "^(010[1-9][0-9]{7})$";
    String authPattern  = "^[0-9]{6}$";

    // 로그인 메소드
    public void login(String userid){

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userid);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    // 아이디 찾기
    @Transactional(readOnly = true)
    public Optional<MemberEntity> findByUserId(String userid){

        return memberRepository.findByuserid(userid);
    }

    // 일반 회원가입
    @Transactional
    public Long joinNormal(MemberDto memberDto, String snsNone){

        memberDto.setPswd(passwordEncoder.encode(memberDto.getPswd()));
        memberDto.setRole(Role.MEMBER.getValue());
        memberDto.setSns(snsNone);

        return memberRepository.save(memberDto.toEntity()).getId();
    }

    // oAuth2로 회원가입
    @Transactional
    public void joinOAuth(String id, String sns) {

        MemberDto memberDto = MemberDto.builder()
                .userid(id)
                .role(Role.MEMBER.getValue())
                .sns(sns)
                .build();
        try{
            memberRepository.save(memberDto.toEntity());
        } catch (Exception e){
            log.info("MemberService 355 line, oAuthJoin exception ");
        }
    }

    // sns 값 찾기
    @Transactional(readOnly = true)
    public String findSnsByUserId(String userid){
        return memberRepository.findSnsByUserId(userid);
    }

    // 비밀번호 검사
    public boolean pswdCheck(String pswd) {
        return Pattern.matches(pswdPattern, pswd);
    }

    // 공백, null 검사 메소드
    public boolean nullCheck(String obj){

        if(obj == null || obj == "" || obj.isEmpty() || obj.isBlank()){
            return false;
        }else{
            return true;
        }
    }

    // jJoinMemberDto를 MemberDto로 전환
    public MemberDto joinDtoToMember(JoinMemberDto joinMemberDto){

        return MemberDto.builder()
                .userid(joinMemberDto.getUserid())
                .pswd(joinMemberDto.getPswd())
                .phonenum(joinMemberDto.getPhoneNum())
                .build();
    }

    // 객체 확인
    public boolean MemberDtoCheck(JoinMemberDto memberDto){

        if(!nullCheck(memberDto.getUserid()) || !nullCheck(memberDto.getPswd()) ||
           !nullCheck(memberDto.getPswd()) || !nullCheck(memberDto.getPhoneNum()) ||
           !nullCheck(memberDto.getAuthNum())){
            return false;
        }else{
            return true;
        }
    }

    // 로그인 객체 유효성 검사
    public int loginValidationCheck(MemberDto memberDto){

        boolean idNull = nullCheck(memberDto.getUserid());
        boolean pswdNull = nullCheck(memberDto.getPswd());
        boolean idValidation = Pattern.matches(idPattern, memberDto.getUserid());
        boolean pswdValidation = Pattern.matches(pswdPattern, memberDto.getPswd());
        Optional<MemberEntity> memberEntity = memberRepository.findByuserid(memberDto.getUserid());

        if(!idNull || !pswdNull || !idValidation || !pswdValidation || memberEntity.isEmpty()){
            return -1;  // 문제가 있을 경우
        }

        // 비밀번호가 같지 않으면 -1 리턴
        MemberEntity entity = memberEntity.get();
        boolean result = passwordEncoder.matches(memberDto.getPswd(), entity.getPassword());
        if(!result) return -1;

        // 정상일 때
        return 0;
    }

    // 로그인 객체 길이 조절 메소드
    public MemberDto loginLength(MemberDto memberDto){
        // 아이디와 비밀번호를 최대길이까지 자른 다음에 리턴한다.

        String[] beforeDto = {memberDto.getUserid(), memberDto.getPswd()};
        String[] afterDto = new String[2];
        int[] maxLength = {20, 25};

        for(int i = 0; i < 2; i++){

            if(beforeDto[i].length() > maxLength[i]){
                afterDto[i] = beforeDto[i].substring(0, maxLength[i]);
            }else{
                afterDto[i] = beforeDto[i];
            }
        }

        memberDto.setUserid(afterDto[0]);
        memberDto.setPswd(afterDto[1]);

        return memberDto;
    }

    // 로그인 실패시 에러 메시지 전송
    public Model loginSetErrorMsg(MemberDto memberDto, Model model){

        String userid = memberDto.getUserid();
        String pswd = memberDto.getPswd();

        boolean idValidation = Pattern.matches(idPattern, userid);
        boolean pswdValidation = Pattern.matches(pswdPattern, pswd);
        Optional<MemberEntity> memberEntity = memberRepository.findByuserid(userid);

        // 공백이거나 값이 없을때
        if(!nullCheck(userid)) {
            model.addAttribute("idMsg", LoginMsg.USERID_NULL.getValue());
        }
        if(!nullCheck(pswd)) {
            model.addAttribute("pswdMsg", LoginMsg.PASSWORD_NULL.getValue());
        }

        // 유효성 검사에 실패 했을때
        if(nullCheck(userid) && nullCheck(pswd)) {

            if(!idValidation || !pswdValidation) {
                model.addAttribute("errorMsg", LoginMsg.LOGIN_FAILURE.getValue());
            }else if(memberEntity.isEmpty()){
                model.addAttribute("errorMsg", LoginMsg.LOGIN_FAILURE.getValue());
            }
        }

        // 비밀번호가 다르다면
        if(memberEntity.isPresent()) {

            MemberEntity entity = memberEntity.get();
            boolean result = passwordEncoder.matches(memberDto.getPswd(), entity.getPassword());
            if (!result) {
                model.addAttribute("errorMsg", LoginMsg.LOGIN_FAILURE.getValue());
            }
        }
        return model;
    }

    // 회원가입 객체 유효성 검사 메소드
    public int joinValidationCheck(JoinMemberDto dto) throws Exception {
        // 문제 있으면 -1 리턴
        // 없으면 0 리턴

        // dto 기본 공백 검사
        if(!MemberDtoCheck(dto)){
            log.info("MemberServcie join dto validation check exception");
            return -1;
        }

        // 아이디 중복검사 && 인증번호 검사
        Optional<MemberEntity> memberEntity = memberRepository.findByuserid(dto.getUserid());
        int authNum = redisService.getAuthNum(dto.getPhoneNum());

        // 유효성 검사 결과들
        boolean idResult = Pattern.matches(idPattern, dto.getUserid());
        boolean pswd1Result = Pattern.matches(pswdPattern, dto.getPswd());
        boolean phoneNoResult = Pattern.matches(phonePattern, dto.getPhoneNum());
        boolean authResult = Pattern.matches(authPattern, dto.getAuthNum());

        if(!idResult || !pswd1Result || !phoneNoResult ||
           !authResult || !dto.getPswd().equals(dto.getPswdCheck()) || authNum == 1) {

            log.info("MemberService 102 line: validation failed");
            return -1;
        }

        // 인증번호가 다를때
        int dtoAuthNum = Integer.parseInt(dto.getAuthNum());
        if(dtoAuthNum != authNum){
            log.info("MemberService 109 line: different authNum");
            return -1;
        }

        // 유효성 검사도 다 통과하고 인증 번호도 같다면
        return 0;
    }

    // 회원가입 에러 메시지 담기
    public Model joinErrorMsg(JoinMemberDto dto,  Model model) throws Exception {

        String userid   = dto.getUserid();
        String pswd1    = dto.getPswd();
        String pswd2    = dto.getPswdCheck();
        String phoneNum = dto.getPhoneNum();
        String authNum  = dto.getAuthNum();

        int authNumResult = redisService.getAuthNum(phoneNum);
        Optional<MemberEntity> memberEntity = memberRepository.findByuserid(userid);

        boolean idValidationResult = Pattern.matches(idPattern, userid);
        boolean pswdValidationResult = Pattern.matches(pswdPattern, pswd1);
        boolean phoneValidationResult = Pattern.matches(phonePattern, phoneNum);
        boolean authValidationResult = Pattern.matches(authPattern, authNum);

        // 아이디 검사
        if(!nullCheck(userid)) {    		 		 // 공백이거나 null일때
            model.addAttribute("idMsg", JoinMsg.USERID_NULL.getValue());
        } else if (!idValidationResult) {			 // 유효성검사에 실패했을때
            model.addAttribute("idMsg", JoinMsg.USERID_VALIDATION.getValue());
        } else if (memberEntity.isPresent()) {		 		 // 아이디가 존재할때
            model.addAttribute("idMsg", JoinMsg.USERID_EXIST.getValue());
        }

        // 비밀번호 검사
        if(!nullCheck(pswd1)) {						// 공백이거나 null일때
            model.addAttribute("pswd1Msg", JoinMsg.PSWD1_NULL.getValue());
        } else if (!pswdValidationResult) {					// 유효성검사에 실패했을때
            model.addAttribute("pswd1Msg", JoinMsg.PSWD1_VALIDATION.getValue());
        }

        // 비밀번호확인 검사
        if(!nullCheck(pswd2)) {						// 공백이거나 null일때
            model.addAttribute("pswd2Msg", JoinMsg.PSWD2_NULL.getValue());
        } else if (!pswd1.equals(pswd2)) {			// 비밀번호가 다를때
            model.addAttribute("pswd2Msg", JoinMsg.PSWD2_DIFFERENCE.getValue());
        }

        // 전화번호 검사
        if(!nullCheck(phoneNum)) {					// 공백이거나 null일때
            model.addAttribute("phoneNumMsg", JoinMsg.PHONENUM_NULL.getValue());
        } else if (!phoneValidationResult) {		// 전화번호가 정규식에 맞지않을때
            model.addAttribute("phoneNumMsg", JoinMsg.PHONENUM_VALIDATION.getValue());
        }


        // 인증번호 검사
        if(!nullCheck(authNum)) {							// 인증번호를 보내지 않았을때
            model.addAttribute("authNumMsg", JoinMsg.AUTHNUM_NOSEND.getValue());
        } else if (!authValidationResult) {     			// 정규식에 맞지않을때
            model.addAttribute("authNumMsg", JoinMsg.AUTHNUM_DIFFERENCE.getValue());
        }
        else if (authNumResult == 1) {                      // 인증번호가 시간이 지나서 사라졌거나 db에 인증번호가 없을때
            model.addAttribute("authNumMsg", JoinMsg.AUTHNUM_TIMEOUT.getValue());
        }

        // 인증번호가 다를때
        if(authValidationResult && nullCheck(authNum) && authNumResult != 1) {
            int AuthNum = Integer.parseInt(authNum);
            if (AuthNum != authNumResult) {
                model.addAttribute("authNumMsg", JoinMsg.AUTHNUM_DIFFERENCE.getValue());
            }
        }
        return model;
    }

    // 회원가입 객체 길이 조정 메소드
    public JoinMemberDto joinDtoLength(JoinMemberDto memberDto){

        String[] dtoBefore = {memberDto.getUserid(), memberDto.getPswd(), memberDto.getPswdCheck(), memberDto.getPhoneNum(), memberDto.getAuthNum()};
        int[] maxlength = {20, 20, 20, 11, 6};

        String[] dtoAfter = new String[5];

        for(int i = 0; i < 5; i++){
            if(dtoBefore[i].length() > maxlength[i]){
                dtoAfter[i] = dtoBefore[i].substring(0, maxlength[i]);
            }else{
                dtoAfter[i] = dtoBefore[i];
            }
        }

        return JoinMemberDto.builder()
                .userid(dtoAfter[0])
                .pswd(dtoAfter[1])
                .pswdCheck(dtoAfter[2])
                .phoneNum(dtoAfter[3])
                .authNum(dtoAfter[4])
                .build();
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
