package com.ecommerce.newshop1.controller;


import com.ecommerce.newshop1.dto.JoinMemberDto;
import com.ecommerce.newshop1.dto.MemberDto;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.repository.MemberRepository;
import com.ecommerce.newshop1.service.*;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final MessageService messageService;
    private final RedisService redisService;
    private final KakaoService kakaoService;
    private final CartService cartService;
    private final SecurityService security;

    ModelMapper mapper = new ModelMapper();
    private RequestCache requestCache = new HttpSessionRequestCache();

    // 기본 페이지
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // 회원가입 페이지
    @GetMapping("/join")
    public String join(Model model) {
        return "member/join";
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String login(HttpServletRequest request) {

        String referer = request.getHeader("Referer");
        request.getSession().setAttribute("prevPage", referer);

        return "member/login";
    }

    @GetMapping("/mypage")
    public String mypage() {

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


    @GetMapping("/member/pswdCheck")
    @ApiOperation(value = "비밀번호 검사")
    public @ResponseBody String pswdCheck(@RequestParam(name = "pswd") String pswd) {

        boolean result = memberService.pswdCheck(pswd);
        if (result) {
            return "Y";
        } else {
            return "N";
        }
    }


    @GetMapping("/member/sendAuth")
    @ApiOperation(value = "인증번호", notes = "인증번호를 전송해주고 reids서버에 저장")
    public @ResponseBody
    String sendAuth(@RequestParam(name = "phoneNum", required = true) String phoneNum) throws Exception {

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
    public @ResponseBody
    String authNumCheck(@RequestParam(value = "authNum", required = true) String authNum,
                        @RequestParam(value = "phoneNum", required = true) String phoneNum) {

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
    public String Join(JoinMemberDto joinMemberDto, Model model) throws Exception {

        int result = memberService.joinValidationCheck(joinMemberDto);

        if (result == 0) {
            try {

                MemberDto memberDto = mapper.map(joinMemberDto, MemberDto.class);
                Member member = memberService.joinNormal(memberDto);
                cartService.createCart(member);

                return "redirect:/";
            } catch (Exception e) {
                throw new Exception("MemberController 46 line - join failed : " + e.getMessage());
            }
        } else {
            JoinMemberDto dto = memberService.joinDtoLength(joinMemberDto);

            model = memberService.joinErrorMsg(dto, model);
            model.addAttribute("member", dto);
            model.addAttribute(model);
            return "member/join";
        }


    }




    // 일단 카카오 로그인은 다음에
//    @ApiOperation(value = "카카오 로그인 & 회원가입", notes = "여기서 한번에 회원가입과 로그인을 진행한다")
//    @GetMapping("/kakao/login")
//    public String kakaoLogin(String code) throws Exception {
//
//        // code로 AccessToken을 받아오고 그 토큰으로 사용자 정보 가져오기
//        OAuthToken oAuthToken = kakaoService.getAccessToken(code);
//        KakaoDto kakaoDto = kakaoService.getUserKakaoProfile(oAuthToken.getAccess_token());
//
//        // 카카오로 회원가입시 아이디 뒤에 @k를 붙여주기 때문에
//        String userid = kakaoDto.getId().toString() + "@k";
//
//        // 아이디 중복 검사
//        Optional<MemberEntity> memberEntity = memberService.findByUserId(userid);
//
//        // 존재하지 않으면 가입
//        if (memberEntity.isEmpty()) {
//            try{
//                memberService.joinOAuth(userid, snsKakao);
//            }catch (Exception e){
//                throw new Exception("MemberController 176 line : " + e.getCause());
//            }
//        }
//
//        // 로그인
//        Optional<MemberEntity> Entity = memberService.findByUserId(userid);
//        MemberEntity entity = Entity.get();
//        memberService.login(entity.getUserid());
//
//        return "redirect:/";
//    }



//    @GetMapping("/kakao/login")
//    public String kakaoLogin(String code) throws Exception {
//
//        // code로 AccessToken을 받아오고 그 토큰으로 사용자 정보 가져오기
//        OAuthToken oAuthToken = kakaoService.getAccessToken(code);
//        KakaoDto kakaoDto = kakaoService.getUserKakaoProfile(oAuthToken.getAccess_token());
//
//        // 카카오로 회원가입시 아이디 뒤에 @k를 붙여주기 때문에
//        String userid = kakaoDto.getId().toString() + "@k";
//
//        // 아이디 중복 검사
//        Object result = memberService.findByUserId(userid);
//
//        // 존재하지 않으면 가입
//        if(result == null){
//            try{
//                memberService.joinOAuth(userid, snsKakao);
//            }catch (Exception e){
//                throw new Exception("MemberController 169 line : " + e.getCause());
//            }
//        }
//
//        // 해당 아이디의 sns 값 확인
//        // 혹시라도 일반 아이디와 같을 경우를 위해서
//        String sns = memberService.findSnsByUserId(userid);
//        if(sns.equals(snsKakao)){
//            memberService.login(userid);
//            return "redirect:/";
//        }else{
//            throw new Exception();
//        }
//    }



}
