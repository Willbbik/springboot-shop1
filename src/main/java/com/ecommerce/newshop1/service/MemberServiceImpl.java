package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.JoinMemberDto;
import com.ecommerce.newshop1.dto.MemberDto;
import com.ecommerce.newshop1.dto.OrderItemDto;
import com.ecommerce.newshop1.entity.*;
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
public class MemberServiceImpl implements MemberService{

    private static final Logger log = LoggerFactory.getLogger(MemberServiceImpl.class);

    private final CustomUserDetailsService customUserDetailsService;
    private final WithdrawalMemberRepository withdrawalMemberRepository;
    private final MemberRepository memberRepository;
    private final RedisService redisService;
    private final OrderItemService orderItemService;
    private final SecurityService security;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;


    // 해당 상품을 사용자가 구매했는지 확인
    @Override
    @Transactional(readOnly = true)
    public boolean existsItem(Item item, String userId) {

        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 아이디입니다."));
        List<OrderItemDto> orderItemList = orderItemService.searchAllByMember(member);

        return orderItemList.stream()
                .anyMatch(p -> p.getItem().getId().equals(item.getId()));
    }

    @Override
    public boolean checkAuthNum(String phoneNum, String authNum){
        // 인증번호 비교
        try{
            int paramAuthNum = Integer.parseInt(authNum);
            int findAuthNum = redisService.getAuthNum(phoneNum);

            return paramAuthNum == findAuthNum;
        }catch (Exception e){
            return false;
        }
    }


    @Override
    public void setAuthCheck(String phoneNum) throws Exception {
        // 인증번호 확인 했다고 설정
        redisService.setAuthCheck(phoneNum);
    }

    @Override
    @Transactional
    public void saveWithdrawalMember(String userId){
        // 회원탈퇴한 아이디 목록에 추가

        WithdrawalMember member = WithdrawalMember.builder()
                .userId(userId)
                .build();

        withdrawalMemberRepository.save(member);
    }

    @Override
    @Transactional
    public void withdrawal(String userId){

        memberRepository.deleteByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> findAllByPhoneNum(String phoneNum){
        // 아이디 찾기시 아이디 반환

        List<String> userIdList = new ArrayList<>();
        List<Member> memberList = memberRepository.findAllByPhoneNum(phoneNum);

        for(Member member : memberList){
            userIdList.add(member.getUserId());
        }
        return userIdList;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUserId(String userId){

        return memberRepository.existsByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsWithdrawalByUserId(String userId){

        return withdrawalMemberRepository.existsByUserId(userId);
    }


    @Override
    @Transactional(readOnly = true)
    public Member findByUserId(String userId){

        return memberRepository.findByUserId(userId)
                .orElseThrow(() -> new MemberNotFoundException("해당 아이디는 존재하지 않습니다." + userId));
    }

    @Override
    @Transactional(readOnly = true)
    public Member findById(Long memberId){
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원 번호입니다, 회원붠호 : " + memberId));
    }

    @Override
    @Transactional(readOnly = true)
    public Member getCurrentMember(){

        if(!security.isAuthenticated()){
            throw new NotLoginException(" getCurrentMember,  로그인을 해야합니다.");
        }
        return memberRepository.findByUserId(security.getName())
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 아이디입니다."));
    }


    @Override
    public void login(String userId){
        // 로그인 메소드

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberDto> findAll(Pageable pageable){
        // 회원가입한 최신 3명 정보 가져오기

        return memberRepository.findAll(pageable).stream()
                .map(p -> mapper.map(p, MemberDto.class))
                .sorted(Comparator.comparing(MemberDto::getId).reversed())
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public Member joinNormal(Member member){
        // 일반 회원가입
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setRole(Role.MEMBER);
        member.setSns(Sns.NONE);

        memberRepository.save(member);
        return member;
    }


    @Override
    @Transactional
    public Member joinOAuth(String id, Sns sns) {
        // oAuth2로 회원가입

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


    @Override
    @Transactional(readOnly = true)
    public int joinValidationCheck(JoinMemberDto dto) {
        // 회원가입시 validator로 검증하지 못하는 부분 유효성 검사

        // 아이디 중복검사
        boolean findMember = memberRepository.existsByUserId(dto.getUserId());
        boolean findWithdrawalMember = withdrawalMemberRepository.existsByUserId(dto.getUserId());

        // 인증번호 검사
        int authNum = redisService.getAuthNum(dto.getPhoneNum());
        int authNumCheckResult = redisService.authNumCheck(dto.getPhoneNum(), dto.getAuthNum());

        if(findMember || findWithdrawalMember || !dto.getPassword().equals(dto.getPswdCheck()) || authNum == 1 || authNumCheckResult != 1) {

            log.warn("MemberService : join validation check failed");
            return -1;
        }

        // 정상적이라면
        return 0;
    }

    @Override
    @Transactional(readOnly = true)
    public Model createJoinDtoErrorMsg(JoinMemberDto dto,  Model model) {
        // 회원가입 에러 메시지 담기

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


    @Override
    public Map<String, String> getErrorMsg(Errors errors){
        // 유효성 에러메시지 생성

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
