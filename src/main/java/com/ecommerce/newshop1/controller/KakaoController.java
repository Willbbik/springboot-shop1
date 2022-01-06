package com.ecommerce.newshop1.controller;

import com.ecommerce.newshop1.dto.*;
import com.ecommerce.newshop1.entity.*;
import com.ecommerce.newshop1.enums.DeliveryStatus;
import com.ecommerce.newshop1.enums.PayType;
import com.ecommerce.newshop1.enums.Sns;
import com.ecommerce.newshop1.service.CartService;
import com.ecommerce.newshop1.service.KakaoService;
import com.ecommerce.newshop1.service.MemberService;
import com.ecommerce.newshop1.service.OrderService;
import com.ecommerce.newshop1.utils.CommonService;
import com.ecommerce.newshop1.utils.ValidationSequence;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class KakaoController {

    private final MemberService memberService;
    private final CartService cartService;
    private final KakaoService kakaoService;
    private final OrderService orderService;
    private final CommonService commonService;

    ModelMapper mapper = new ModelMapper();

    @PostMapping("/kakaoPay/order")
    @ApiOperation(value = "카카오페이 결제창 띄우기", notes = "카카오쪽으로 상품 정보에 대해서 전송후, 결제창 uri 리턴")
    public @ResponseBody String kakaoPay(@Validated(ValidationSequence.class) AddressDto addressDto, BindingResult errors, String payMethod, HttpSession session){

        PayType payType = PayType.findByPayType(payMethod);
        if(!PayType.KAKAO_PAY.equals(payType)) {
            return "fail";
        } else if(errors.hasErrors()) {
            return commonService.getErrorMessage(errors);
        }

        session.setAttribute("addressDto", addressDto);
        session.setAttribute("payType", payType);

        return kakaoService.kakaoPayReady(session);
    }

    @RequestMapping("/kakaoPay/order/success")
    @ApiOperation(value = "카카오페이 결제 승인", notes = "사용자가 결제 인증 완료후, 결제 완료 처리하는 단계")
    public String kakaoPaySuccess(@RequestParam(name = "pg_token") String pgToken, HttpSession session, Model model){

        String tid = session.getAttribute("tid").toString();
        String orderNum = session.getAttribute("orderNum").toString();

        // 결제 승인 요청
        kakaoService.kakaoPayApprove(pgToken, tid, orderNum);

        // 결제 정보
        OrderPaymentInformation orderPaymentInformation = new OrderPaymentInformation();
        orderPaymentInformation.setPayType(PayType.KAKAO_PAY.getTitle());

        // 배송 정보
        Delivery delivery = new Delivery();
        delivery.setDeliveryStatus(DeliveryStatus.DEPOSIT_SUCCESS);

        // 주문
        orderNum = orderService.doOrder(session, orderPaymentInformation, delivery);

        // 주문 후 결제성공 페이지에 띄워주기 위해서
        Order order = orderService.findByOrderNum(orderNum);
        model.addAttribute(orderService.getModelPayInfo(order, model));

        return "order/order_success";
    }


    @RequestMapping("/kakaoPay/order/cancel")
    @ApiOperation(value = "카카오페이 결제 취소 페이지")
    public String kakaoCencel(){
        return "order/kakao/kakaoPayClose";
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
