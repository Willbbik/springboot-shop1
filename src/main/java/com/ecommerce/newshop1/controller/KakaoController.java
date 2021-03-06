package com.ecommerce.newshop1.controller;

import com.ecommerce.newshop1.dto.*;
import com.ecommerce.newshop1.entity.*;
import com.ecommerce.newshop1.enums.DeliveryStatus;
import com.ecommerce.newshop1.enums.PayType;
import com.ecommerce.newshop1.enums.Sns;
import com.ecommerce.newshop1.service.*;
import com.ecommerce.newshop1.utils.ValidationSequence;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@Controller
@RequiredArgsConstructor
public class KakaoController {

    private final MemberService memberService;
    private final CartService cartService;
    private final KakaoService kakaoService;
    private final OrderService orderService;


    @PostMapping("/kakaoPay/order")
    @ApiOperation(value = "카카오페이 결제창 띄우기", notes = "카카오쪽으로 상품 정보에 대해서 전송후, 결제창 uri 리턴")
    public @ResponseBody String kakaoPay(@Validated(ValidationSequence.class) AddressDto addressDto, BindingResult errors, String payMethod, HttpSession session){

        PayType payType = PayType.findByPayType(payMethod);
        if(!PayType.KAKAO_PAY.equals(payType)) {
            return "fail";
        } else if(errors.hasErrors()) {
            return "validation";
        }

        session.setAttribute("addressDto", addressDto);
        session.setAttribute("payType", payType);

        return kakaoService.kakaoPayReady(session);
    }


    @RequestMapping("/kakaoPay/order/success")
    @ApiOperation(value = "카카오페이 결제 승인", notes = "카카오결제 승인 후 주문성공 페이지 띄워주기")
    public String kakaoPaySuccess(@RequestParam(name = "pg_token") String pgToken, HttpSession session, Model model){

        String tid = session.getAttribute("tid").toString();
        String orderNum = session.getAttribute("orderNum").toString();

        // 결제 승인 요청
        kakaoService.kakaoPayApprove(pgToken, tid, orderNum);

        OrderPaymentInformation orderPaymentInformation = new OrderPaymentInformation();
        orderPaymentInformation.setPayType(PayType.KAKAO_PAY.getTitle());

        Delivery delivery = new Delivery();
        delivery.setDeliveryStatus(DeliveryStatus.DEPOSIT_SUCCESS);

        // 주문
        orderNum = orderService.doOrder(session, orderPaymentInformation, delivery);
        session.removeAttribute("tid");

        // 주문 후 결제성공 페이지에 띄워주기 위해서
        Order order = orderService.findByOrderNum(orderNum);
        model.addAttribute(orderService.getModelPayInfo(order, model));

        return "order/order_success";
    }


    @GetMapping("/kakao/login")
    @ApiOperation(value = "카카오 로그인 & 회원가입", notes = "회원가입과 로그인을 같이 진행")
    public String kakaoLogin(String code) throws Exception {

        OAuthToken oAuthToken = kakaoService.getAccessToken(code);
        KakaoDto kakaoDto = kakaoService.getUserKakaoProfile(oAuthToken.getAccess_token());

        // oauth2 회원가입시 해당 포털을 구분하기 위해서. @k = kakao
        String userId = kakaoDto.getId().toString() + "@k";

        boolean result = memberService.existsByUserId(userId);
        Member member = new Member();

        // 존재하지 않으면 가입
        if (!result) {
            member = memberService.joinOAuth(userId, Sns.KAKAO);

            Cart cart = new Cart();
            cart.createCart(member);
            cartService.save(cart);
        }else{
            member = memberService.findByUserId(userId);
        }

        // 로그인
        memberService.login(member.getUserId());

        return "redirect:/";
    }

}
