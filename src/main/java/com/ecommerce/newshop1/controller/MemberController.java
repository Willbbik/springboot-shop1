package com.ecommerce.newshop1.controller;

import com.ecommerce.newshop1.dto.*;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.entity.Order;
import com.ecommerce.newshop1.entity.OrderItem;
import com.ecommerce.newshop1.repository.QnARepository;
import com.ecommerce.newshop1.service.*;
import com.ecommerce.newshop1.utils.CommonService;
import com.ecommerce.newshop1.utils.ValidationSequence;
import com.ecommerce.newshop1.enums.Sns;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
    private final CommonService commonService;
    private final QnARepository qnARepository;
    private final PasswordEncoder passwordEncoder;

    ModelMapper mapper = new ModelMapper();

    @GetMapping("/")
    @ApiOperation(value = "웹 기본 페이지")
    public String index() {

        return "index";
    }


    @GetMapping("/join")
    @ApiOperation(value = "회원가입 페이지")
    public String join() {

        return "member/member_join";
    }


    @RequestMapping("/login")
    @ApiOperation(value = "로그인 페이지")
    public String login(HttpServletRequest request) {

        String referer = request.getHeader("Referer");
        request.getSession().setAttribute("prevPage", referer);

        return "member/member_login";
    }

    @GetMapping("/member/findId")
    @ApiOperation(value = "아이디 찾기 페이지")
    public String findId(){

        return "member/member_findId";
    }

    @GetMapping("/member/withdrawal")
    @ApiOperation(value = "회원탈퇴 페이지")
    public String withdrawal(){

        return "member/member_withdrawal";
    }

    @GetMapping("/member/withdrawal/finalCheck")
    @ApiOperation(value = "회원탈퇴시 비밀번호 검사 페이지")
    public String withdrawalFinalCheck(){

        return "member/member_withdrawalPswd";
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
        return "member/member_mypage";
    }

    @GetMapping("/member/orderDetails/{id}")
    public String memberOrderDetails(@PathVariable(name = "id") Long orderId, Model model){

        Order order = orderService.findById(orderId);

        OrderDto orderInfo = mapper.map(order, OrderDto.class);
        OrderPaymentInfoDto payInfo = mapper.map(order.getPaymentInfo(), OrderPaymentInfoDto.class);
        List<OrderItemDto> orderItems = OrderItem.toDtoList(order.getOrderItems());
        AddressDto address = mapper.map(order.getDelivery().getDeliveryAddress(), AddressDto.class);

        model.addAttribute("method", payInfo.getPayType());
        model.addAttribute("payInfo", payInfo);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("orderItems",  orderItems);
        model.addAttribute("address", address);

        return "member/member_orderDetails";
    }



    @DeleteMapping("/member/withdrawal")
    @ApiOperation(value = "비밀번호 확인 후 회원탈퇴 처리", notes = "회원탈퇴")
    public @ResponseBody String withdrawalPost(HttpSession session, @RequestParam(name = "password") String password){

        Member member = memberService.getCurrentMember();

        boolean result = passwordEncoder.matches(password, member.getPassword());

        if(result){

            memberService.withdrawal(member.getUserId());
            memberService.saveWithdrawalMember(member.getUserId());
            session.invalidate();;
            return "success";
        }else{
            return "fail";
        }
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

        boolean member = memberService.existsByUserId(userId);
        boolean withdrawalMember = memberService.existsWithdrawalByUserId(userId);

        if(!member && !withdrawalMember) return "Y";
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

    @PostMapping("/join")
    @ApiOperation(value = "일반 회원가입")
    public String join(@ModelAttribute("member") @Validated(ValidationSequence.class) JoinMemberDto joinMemberDto, BindingResult errors, Model model) throws Exception {

        if(errors.hasErrors()){
            Map<String, String > errorMsgMap = memberService.getErrorMsg(errors);   // 에러 메시지가 담긴 map
            for(String key : errorMsgMap.keySet()){
                model.addAttribute(key, errorMsgMap.get(key));
            }
            return "member/member_join";
        }

        // 유효성 문제가 없으면 중복검사
        int checkResult = memberService.joinValidationCheck(joinMemberDto);
        if(checkResult == -1){

            model.addAttribute(memberService.createJoinDtoErrorMsg(joinMemberDto, model));
            return "member/member_join";
        }

        Member member = mapper.map(joinMemberDto, Member.class);
        cartService.createCart(member);
        memberService.joinNormal(member);

        return "redirect:/";
    }

    @PostMapping("/member/findId/sendMessage")
    @ApiOperation(value = "아이디 찾기", notes = "인증번호 전송")
    public @ResponseBody String findIdPost(@Validated(ValidationSequence.class) FindIdDto findIdDto, BindingResult errors) throws Exception {

        if(errors.hasErrors()) {
            return commonService.getErrorMessage(errors);
        }

        String phoneNum = findIdDto.getPhoneNum();
        int authNum = commonService.randomAuthNum();

        redisService.setAuthNo(phoneNum, authNum);
        // messageService.sendMessage(phoneNum, authNum);
        return "success";
    }

    @PostMapping("/member/findId/authNum")
    @ApiOperation(value = "아이디 찾기시 인증번호 비교", notes = "아이디 찾기")
    public @ResponseBody String findIdAuth(@Validated(ValidationSequence.class) FindIdDto findIdDto, BindingResult errors, HttpSession session) throws Exception {

        if(errors.hasErrors()){
            return commonService.getErrorMessage(errors);
        }

        boolean result = memberService.checkAuthNum(findIdDto.getPhoneNum(), findIdDto.getAuthNum());

        if(result){ // 인증번호가 일치하다면

            // 아이디 확인 클릭시 인증번호 확인받았는지 검사하기 위해서
            session.setAttribute("phoneNum", findIdDto.getPhoneNum());
            memberService.setAuthCheck(findIdDto.getPhoneNum());

            return "success";
        }else{
            return "fail";
        }
    }

    @PostMapping("/member/findId/findIdResult")
    @ApiOperation(value = "인증했는지 확인 후 아이디 제공", notes = "아이디 찾기")
    public String findId(HttpSession session, Model model){

        String phoneNum = (String)session.getAttribute("phoneNum");
        boolean result = redisService.confirmPhoneCheck(phoneNum);

        if(result){

            List<String> userIdList = memberService.findAllByPhoneNum(phoneNum);
            redisService.deleteKey(phoneNum);
            redisService.deleteKey(phoneNum+"_check");

            model.addAttribute("userIdList", userIdList);
            session.removeAttribute("phoneNum");

            return "member/member_findIdResult";
        }else{
            return null;
        }
    }


}
