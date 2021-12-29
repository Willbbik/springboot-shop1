package com.ecommerce.newshop1.controller;

import com.ecommerce.newshop1.dto.*;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.repository.QnARepository;
import com.ecommerce.newshop1.service.*;
import com.ecommerce.newshop1.utils.ValidationSequence;
import com.ecommerce.newshop1.enums.Sns;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MessageService messageService;
    private final RedisService redisService;
    private final KakaoService kakaoService;
    private final QnAService qnAService;
    private final CartService cartService;
    private final OrderService orderService;
    private final QnARepository qnARepository;

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
    @ApiOperation(value = "mypage 페이지")
    public String mypage(Model model) {

        Member member = memberService.getCurrentMember();
        List<OrderDto> orderList = orderService.searchAllByMember(null, member);

        Long lastOrderId = null;
        if(orderList.size() > 1){
            int lastIndex = orderList.size() - 1;
            lastOrderId = orderList.get(lastIndex).getId();
        }else if(orderList.size() == 1){
            lastOrderId = orderList.get(0).getId();
        }

        model.addAttribute("orderList", orderList);
        model.addAttribute("lastOrderId", lastOrderId);
        return "member/mypage";
    }

    @GetMapping("/mypage/orderList")
    @ApiOperation(value = "mypage에 주문한 상품들이 담긴 html 리턴", notes = "ajax 전용")
    public String orderListMore(@RequestParam(required = false) Long lastOrderId,
                                @RequestParam(required = false) String more, Model model){

        Member member = memberService.getCurrentMember();
        List<OrderDto> orderList = orderService.searchAllByMember(lastOrderId, member);

        lastOrderId = orderService.getLastOrderId(orderList, lastOrderId);

        model.addAttribute("orderList", orderList);
        model.addAttribute("lastOrderId", lastOrderId);

        if(more != null && more.equals("more")){
            return "member/tab/tab1ordermore";
        }
        return "member/tab/tab1orderList";
    }

    @GetMapping("/mypage/qnaList")
    @ApiOperation(value = "mypage에 해당 사용자가 작성한 qna와 답변들이 담긴 html 리턴", notes = "ajax 전용")
    public String qnaList(Model model, @RequestParam(required = false) Long lastQnAId, @RequestParam(required = false) String more){

        Member member = memberService.getCurrentMember();
        boolean result = qnARepository.existsByMember(member);

        // 댓글이 없으면 빈 배열을, 있으면 가져오기
        List<QnADto> qnaList = (!result) ? new ArrayList<>() : qnAService.searchAllByMember(lastQnAId, member);
        List<QnADto> qnaReplyList = qnAService.getQnAReply(qnaList);

        // 값 수정
        qnaList = qnAService.editQna(qnaList);
        qnaReplyList = qnAService.editReply(qnaReplyList);

        // nooffset 페이징을 위해서 마지막 qna번호 가져오기
        lastQnAId = qnAService.getLastQnAId(qnaList, lastQnAId);

        model.addAttribute("qnaList", qnaList);
        model.addAttribute("qnaReplyList", qnaReplyList);
        model.addAttribute("lastQnAId", lastQnAId);

        if(more != null && more.equals("more")){
            return "member/tab/tab2qnamore";
        }
        return "member/tab/tab2qnaList";
    }


    @DeleteMapping("/mypage/qna/delete")
    public @ResponseBody String qnaDelete(@RequestParam List<Long> qnaIdList){

        qnAService.deleteQnaAndReply(qnaIdList);

        return "success";
    }


    @GetMapping("/member/idConfirm")
    @ApiOperation(value = "아이디 중복검사", notes = "회원가입시 ajax로 아이디 중복검사 할 때")
    public @ResponseBody String idConfirm(@RequestParam(name = "userId") String userId) {

        Optional<Member> result = memberService.findByUserId(userId);
        if (result.isEmpty()) return "Y";
        else return "N";

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

        Member member = mapper.map(joinMemberDto, Member.class);
        cartService.createCart(member);
        memberService.joinNormal(member);

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
