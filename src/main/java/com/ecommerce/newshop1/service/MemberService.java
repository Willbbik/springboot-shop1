package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.JoinMemberDto;
import com.ecommerce.newshop1.dto.MemberDto;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.entity.WithdrawalMember;
import com.ecommerce.newshop1.exception.MemberNotFoundException;
import com.ecommerce.newshop1.exception.NotLoginException;
import com.ecommerce.newshop1.repository.MemberRepository;
import com.ecommerce.newshop1.enums.Role;
import com.ecommerce.newshop1.enums.Sns;
import com.ecommerce.newshop1.repository.WithdrawalMemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MemberService {

    private static final Logger log = LoggerFactory.getLogger(MemberService.class);

    private final CustomUserDetailsService customUserDetailsService;
    private final WithdrawalMemberRepository withdrawalMemberRepository;
    private final MemberRepository memberRepository;
    private final RedisService redisService;
    private final SecurityService security;
    private final PasswordEncoder passwordEncoder;

    ModelMapper mapper = new ModelMapper();

    // 해당 상품을 사용자가 구매했는지 확인
    public boolean existsItem(Item item) {

        Member member = getCurrentMember();

        return member.getOrderList().stream()
                .anyMatch(i -> i.getOrderItems().stream()
                        .anyMatch(p -> p.getItem().getId().equals(item.getId()))
                );
    }

    // 인증번호 비교
    public boolean checkAuthNum(String phoneNum, String authNum){

        try{
            int paramAuthNum = Integer.parseInt(authNum);
            int findAuthNum = redisService.getAuthNum(phoneNum);

            return paramAuthNum == findAuthNum;
        }catch (Exception e){
            return false;
        }
    }

    // 인증번호 확인 했다고 설정
    public void setAuthCheck(String phoneNum) throws Exception {
        redisService.setAuthCheck(phoneNum);
    }

    @Transactional
    public void saveWithdrawalMember(String userId){

        WithdrawalMember member = WithdrawalMember.builder()
                .userId(userId)
                .build();

        withdrawalMemberRepository.save(member);
    }

    @Transactional
    public void withdrawal(String userId){

        memberRepository.deleteByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<String> findAllByPhoneNum(String phoneNum){

        List<String> userIdList = new ArrayList<>();
        List<Member> memberList = memberRepository.findAllByPhoneNum(phoneNum);

        for(Member member : memberList){
            userIdList.add(member.getUserId());
        }
        return userIdList;
    }

    @Transactional(readOnly = true)
    public boolean existsByUserId(String userId){
        return memberRepository.existsByUserId(userId);
    }

    @Transactional(readOnly = true)
    public boolean existsWithdrawalByUserId(String userId){
        return withdrawalMemberRepository.existsByUserId(userId);
    }



    // 아이디 찾기
    @Transactional(readOnly = true)
    public Optional<Member> findByUserId(String userId){

        return memberRepository.findByuserId(userId);
    }

    @Transactional(readOnly = true)
    public Member findById(Long memberId){
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원 번호입니다, 회원붠호 : " + memberId));
    }

    @Transactional(readOnly = true)
    public Member getCurrentMember(){

        if(!security.isAuthenticated()){
            throw new NotLoginException(" getCurrentMember,  로그인을 해야합니다.");
        }
        String userId = security.getName();
        return memberRepository.findByuserId(userId)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 아이디입니다."));
    }

    // 로그인 메소드
    public void login(String userId){

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    // 최근회원 3명 정보 가져오기
    @Transactional(readOnly = true)
    public List<MemberDto> findAll(Pageable pageable){

        return memberRepository.findAll(pageable).stream()
                .map(p -> mapper.map(p, MemberDto.class))
                .sorted(Comparator.comparing(MemberDto::getId).reversed())
                .collect(Collectors.toList());
    }

    // 일반 회원가입
    @Transactional
    public Member joinNormal(Member member){

        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setRole(Role.MEMBER);
        member.setSns(Sns.NONE);

        memberRepository.save(member);
        return member;
    }

    // oAuth2로 회원가입
    @Transactional
    public Member joinOAuth(String id, Sns sns) {
        String password = UUID.randomUUID().toString();
        password = passwordEncoder.encode(password);

        MemberDto memberDto = MemberDto.builder()
                .userId(id)
                .password(password)
                .role(Role.MEMBER)
                .sns(sns)
                .build();
        Member member = mapper.map(memberDto, Member.class);
        return memberRepository.save(member);
    }


    // 회원가입 객체 유효성 검사 메소드
    @Transactional(readOnly = true)
    public int joinValidationCheck(JoinMemberDto dto) {

        // 아이디 중복검사
        boolean findMember = memberRepository.existsByUserId(dto.getUserId());
        boolean findWithdrawalMember = withdrawalMemberRepository.existsByUserId(dto.getUserId());

        // 인증번호 검사
        int authNum = redisService.getAuthNum(dto.getPhoneNum());
        int authNumCheckResult = redisService.authNumCheck(dto.getPhoneNum(), dto.getAuthNum());

        if(findMember || findWithdrawalMember || !dto.getPassword().equals(dto.getPswdCheck()) || authNum == 1 || authNumCheckResult != 1) {

            log.info("MemberService : join validation check failed");
            return -1;
        }

        // 정상적이라면
        return 0;
    }

    // 회원가입 에러 메시지 담기
    @Transactional(readOnly = true)
    public Model createJoinDtoErrorMsg(JoinMemberDto dto,  Model model) {

        String pswd1    = dto.getPassword();
        String pswd2    = dto.getPswdCheck();
        String phoneNum = dto.getPhoneNum();
        String dtoAuthNum  = dto.getAuthNum();

        int authNumResult = redisService.authNumCheck(phoneNum, dtoAuthNum);        // 인증번호 검사
        boolean findMember = memberRepository.existsByUserId(dto.getUserId());
        boolean findWithdrawalMember = withdrawalMemberRepository.existsByUserId(dto.getUserId());

        // 아이디 검사
        if (findMember || findWithdrawalMember) {	 // 아이디가 존재할때
            model.addAttribute("valid_userId", "이미 사용중이거나 탈퇴한 아이디입니다.");
        }
        // 비밀번호 비교
        if (!pswd1.equals(pswd2)) {		 // 비밀번호가 다를때
            model.addAttribute("valid_pswdCheck","비밀번호가 일치하지 않습니다.");
        }

        // 인증번호 검사
        if (authNumResult == 3) {          // 인증번호가 시간이 지나서 사라졌거나 db에 인증번호가 없을때
            model.addAttribute("valid_authNum", "인증번호를 다시 확인해주세요.");
        }else if(authNumResult == 2){      // 인증번호가 다를 때
            model.addAttribute("valid_authNum", "인증을 다시 진행해주세요.");
        }

        return model;
    }

    // 회원 가입 실패시 에러메시지 생성
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


}
