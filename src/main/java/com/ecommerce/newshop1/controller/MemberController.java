package com.ecommerce.newshop1.controller;


import com.ecommerce.newshop1.dto.JoinMemberDto;
import com.ecommerce.newshop1.dto.MemberDto;
import com.ecommerce.newshop1.service.KakaoService;
import com.ecommerce.newshop1.service.MemberService;
import com.ecommerce.newshop1.service.MessageService;
import com.ecommerce.newshop1.service.RedisService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;
    private final MessageService messageService;
    private final RedisService redisService;
    private final KakaoService kakaoService;

    private String snsKakao = "kakao";
    private String snsNone = "none";


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
    public String login() {
        return "member/login";
    }


    @ApiOperation(value = "아이디 중복검사", notes = "회원가입시 ajax로 아이디 중복검사 할 때")
    @GetMapping("/member/idConfirm")
    public @ResponseBody
    String idConfirm(@RequestParam(name = "userid", required = true) String userid) {

        Object result = memberService.findByUserId(userid);
        if (result == null) {
            return "Y";
        } else {
            return "N";
        }
    }


    @ApiOperation(value = "비밀번호 검사")
    @GetMapping("/member/pswdCheck")
    public @ResponseBody
    String pswdCheck(@RequestParam(name = "pswd", required = true) String pswd) {

        boolean result = memberService.pswdCheck(pswd);
        if (result) {
            return "Y";
        } else {
            return "N";
        }
    }


    @ApiOperation(value = "인증번호", notes = "인증번호를 전송해주고 reids서버에 저장")
    @GetMapping("/member/sendAuth")
    public @ResponseBody
    String sendAuth(@RequestParam(name = "phoneNum", required = true) String phoneNum) throws Exception {

        boolean result = messageService.phoneValidationCheck(phoneNum);
        int randomNum = messageService.randomNum();

        if (result) {
            redisService.setAuthNo(phoneNum, randomNum);
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

    @ApiOperation(value = "로그인")
    @PostMapping("/member/login")
    public String login(MemberDto memberDto, Model model) {

        int result = memberService.loginValidationCheck(memberDto);

        if (result == 0) {
            memberService.login(memberDto.getUserid());
            return "redirect:/";
        } else {
            MemberDto dto = memberService.loginLength(memberDto);

            model = memberService.loginSetErrorMsg(memberDto, model);
            model.addAttribute("member", dto);
            model.addAttribute(model);
            return "member/login";
        }
    }


    @ApiOperation(value = "일반 회원가입")
    @PostMapping("/join")
    public String Join(JoinMemberDto joinMemberDto, Model model) throws Exception {

        int result = memberService.joinValidationCheck(joinMemberDto);

        if (result == 0) {
            try {
                MemberDto memberDto = memberService.joinDtoToMember(joinMemberDto);
                memberService.joinNormal(memberDto, snsNone);
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
