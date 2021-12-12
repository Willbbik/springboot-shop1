package com.ecommerce.newshop1.controller;

import com.ecommerce.newshop1.dto.JoinMemberDto;
import com.ecommerce.newshop1.dto.KakaoDto;
import com.ecommerce.newshop1.dto.MemberDto;
import com.ecommerce.newshop1.dto.OAuthToken;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.entity.Order;
import com.ecommerce.newshop1.exception.MemberNotFoundException;
import com.ecommerce.newshop1.repository.MemberRepository;
import com.ecommerce.newshop1.repository.OrderRepository;
import com.ecommerce.newshop1.service.*;
import com.ecommerce.newshop1.utils.ValidationSequence;
import com.ecommerce.newshop1.utils.enums.Sns;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MessageService messageService;
    private final RedisService redisService;
    private final KakaoService kakaoService;
    private final CartService cartService;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final SecurityService security;

    ModelMapper mapper = new ModelMapper();

    // 기본 페이지
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // 회원가입 페이지
    @GetMapping("/join")
    public String join() {
        return "member/join";
    }

    // 로그인 페이지
    @RequestMapping("/login")
    public String login(HttpServletRequest request) {

        String referer = request.getHeader("Referer");
        request.getSession().setAttribute("prevPage", referer);

        return "member/login";
    }

    @GetMapping("/mypage")
    public String mypage(Model model) {

        String userId = security.getName();
        Member member = memberRepository.findByuserId(userId)
                .orElseThrow(() -> new MemberNotFoundException("MemberController mypage에서 발생, 존재하지 않는 아이디입니다."));

        List<Order> orderList = orderRepository.findAllByMember(member);

        return "member/mypage";
    }

    @GetMapping("/member/idConfirm")
    @ApiOperation(value = "아이디 중복검사", notes = "회원가입시 ajax로 아이디 중복검사 할 때")
    public @ResponseBody String idConfirm(@RequestParam(name = "userId") String userId) {

        Optional<Member> result = memberService.findByUserId(userId);
        if (result.isEmpty()) {
            return "Y";
        } else {
            return "N";
        }
    }

    @GetMapping("/member/sendAuth")
    @ApiOperation(value = "인증번호", notes = "인증번호를 전송해주고 redis에 저장")
    public @ResponseBody String sendAuth(@RequestParam(name = "phoneNum") String phoneNum) throws Exception {

        boolean result = messageService.phoneValidationCheck(phoneNum);
        int randomNum = messageService.randomNum();

        if (result) {
            redisService.setAuthNo(phoneNum, randomNum);
            // 메시지 전송 메소드 사용해야함
            return "Y";
        } else {
            return "N";
        }
    }

    @ApiOperation(value = "회원가입시 인증번호 검사")
    @GetMapping("/member/authNumCheck")
    public @ResponseBody String authNumCheck(@RequestParam(value = "authNum") String authNum,
                        @RequestParam(value = "phoneNum") String phoneNum) {

        int result = redisService.authNumCheck(phoneNum, authNum);

        switch (result) {
            case 1:
                return "Y";
            case 2:
                return "N";
            case 3:
                return "cnt";
            default:
                break;
        }
        return "cnt";
    }


    @ApiOperation(value = "일반 회원가입")
    @PostMapping("/join")
    public String join(@ModelAttribute("member") @Validated(ValidationSequence.class) JoinMemberDto joinMemberDto, BindingResult errors, Model model) throws Exception {

        if(errors.hasErrors()){
            Map<String, String > errorMsgMap = memberService.getErrorMsg(errors);   // 에러 메시지가 담긴 map
            for(String key : errorMsgMap.keySet()){
                model.addAttribute(key, errorMsgMap.get(key));
            }
            return "member/join";
        }

        // 유효성 문제가 없으면 중복검사
        int checkResult = memberService.joinValidationCheck(joinMemberDto);
        if(checkResult == -1){

            model.addAttribute(memberService.createJoinDtoErrorMsg(joinMemberDto, model));            // 에러 메시지
            model.addAttribute("member", memberService.joinDtoLengthEdit(joinMemberDto)); // input 값 유지
            return "member/join";
        }

        MemberDto memberDto = mapper.map(joinMemberDto, MemberDto.class);
        Member member = memberService.joinNormal(memberDto);
        cartService.createCart(member);

        return "redirect:/";
    }


    @GetMapping("/kakao/login")
    @ApiOperation(value = "카카오 로그인 & 회원가입", notes = "여기서 한번에 회원가입과 로그인을 진행한다")
    public String kakaoLogin(String code) throws Exception {

        // code로 AccessToken을 받아오고 그 토큰으로 사용자 정보 가져오기
        OAuthToken oAuthToken = kakaoService.getAccessToken(code);
        KakaoDto kakaoDto = kakaoService.getUserKakaoProfile(oAuthToken.getAccess_token());

        // oauth2 회원가입시 해당 포털을 구분하기 위해서. @k = kakao
        String userId = kakaoDto.getId().toString() + "@k";

        Optional<Member> memberEntity = memberService.findByUserId(userId);
        Member member = new Member();

        // 존재하지 않으면 가입
        if (memberEntity.isEmpty()) {
            member = memberService.joinOAuth(userId, Sns.KAKAO);
            cartService.createCart(member);
        }else{
            member = memberEntity.get();
        }

        // 로그인
        memberService.login(member.getUserId());

        return "redirect:/";
    }


}
