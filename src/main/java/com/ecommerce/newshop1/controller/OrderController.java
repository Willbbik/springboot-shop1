package com.ecommerce.newshop1.controller;

import com.ecommerce.newshop1.dto.*;
import com.ecommerce.newshop1.entity.OrderItem;
import com.ecommerce.newshop1.entity.OrderPaymentInformation;
import com.ecommerce.newshop1.exception.ParameterNotFoundException;
import com.ecommerce.newshop1.service.*;
import com.ecommerce.newshop1.utils.CommonService;
import com.ecommerce.newshop1.utils.ValidationSequence;
import com.ecommerce.newshop1.enums.PayType;
import com.ecommerce.newshop1.enums.TossPayments;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class OrderController {

    private final CartService cartService;
    private final OrderService orderService;
    private final CommonService commonService;
    private final KakaoService kakaoService;

    ModelMapper mapper = new ModelMapper();


    @PostMapping("/order/checkout")
    @ApiOperation(value = "구매할 상품 선택후 주문페이지로 이동")
    public String checkoutPage(String itemList, String where, Model model, HttpServletRequest request) throws Exception {

        List<OrderItemDto> orderItems = new ArrayList<>();
        String orderName = "";
        HttpSession session = request.getSession();
        int totalPrice = 0;

        if(where.equals("product")){    // 상품 바로 구매시
            orderItems = orderService.itemToPayment(itemList);
            orderName = orderItems.get(0).getItem().getItemName(); // 주문 상품명
            totalPrice += orderItems.get(0).getTotalPrice();
        }
        else if(where.equals("cart")) { // 장바구니에서 구매시
            orderItems = cartService.cartItemToPayment(itemList, session);
            orderName = orderItems.get(0).getItem().getItemName() + " 외 " + (orderItems.size() - 1) + "건"; // 주문 상품명
            for(OrderItemDto itemDto : orderItems){
                totalPrice += itemDto.getTotalPrice();
            }
        } else{
            throw new ParameterNotFoundException("주문 페이지 이동시 필수 파라미터 'where'가 정상적인 값이 아님");
        }

        // 주문번호와 상품 번호들 세션에 저장. ( 마지막 최종 주문할 때 사용하기 위해서 )
        session.setAttribute("orderNum", orderService.createOrderNum());  // 주문번호
        session.setAttribute("orderItems", orderItems);
        session.setAttribute("orderName", orderName);

        model.addAttribute("orderItems", orderItems);
        model.addAttribute("orderNum", orderService.createOrderNum());  // 주문번호
        model.addAttribute("orderName", orderName);
        model.addAttribute("totalPrice", totalPrice);

        return "order/order_checkout";
    }


    @RequestMapping("/order/virtual-account/callback")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "가상계좌 입금 완료시 처리", notes = "토스페이먼츠 가상계좌에 입금 완료 콜백 url")
    public void paymentCheck(@RequestBody CallbackPayload callbackPayload){

        if(callbackPayload.getStatus().equals(TossPayments.DONE.getValue())){
            // 입금 완료일 때 처리
            orderService.updateOrderToDepositSuccess(callbackPayload.getOrderId());
            System.out.println("완료처리");
        }else if(callbackPayload.getStatus().equals(TossPayments.CANCELED.getValue())){
            // 입금 취소일 때 처리
            System.out.println("취소처리");
        }
    }


    @PostMapping("/order/virtualAccount")
    @ApiOperation(value = "가상계좌 결제")
    public @ResponseBody String saveAddressDto(@Validated(ValidationSequence.class) AddressDto addressDto, BindingResult errors, String payMethod, HttpSession session){

        PayType payType = PayType.findByPayType(payMethod);

        if(payType.getTitle().equals("없음")){
            return "fail";
        }else if(errors.hasErrors()) {
            return commonService.getErrorMessage(errors);
        }

        session.setAttribute("addressDto", addressDto);
        session.setAttribute("payType", payType);
        return "success";
    }


    @RequestMapping("/success")
    @ApiOperation(value = "토스페이먼츠의 가상계좌 결제", notes = "가상계좌 결제")
    public String confirmPayment(@RequestParam String paymentKey, @RequestParam String orderId,
                                 @RequestParam int amount, Model model, HttpSession session) throws Exception {

        // 결제 승인 요청
        ResponseEntity<JsonNode> responseEntity = orderService.tossPayment(paymentKey, orderId, amount);

        if(responseEntity.getStatusCode() == HttpStatus.OK) {

            // 로그인 검사

            String payType = responseEntity.getBody().get("method").asText();

            if(payType.equals("가상계좌")){

                OrderPaymentInformation paymentInfo = orderService.getVirtualAccountInfo(responseEntity.getBody());
                orderService.doOrder(session, paymentInfo);    // 주문

                OrderPaymentInfoDto payInfo = mapper.map(paymentInfo, OrderPaymentInfoDto.class);
                OrderDto orderInfo = mapper.map(paymentInfo.getOrder(), OrderDto.class);
                List<OrderItemDto> orderItems = OrderItem.toDtoList(paymentInfo.getOrder().getOrderItems());
                AddressDto address = mapper.map(paymentInfo.getOrder().getDelivery().getDeliveryAddress(), AddressDto.class);

                model.addAttribute("method", "virtualAccount");
                model.addAttribute("payInfo", payInfo);
                model.addAttribute("orderInfo", orderInfo);
                model.addAttribute("orderItems",  orderItems);
                model.addAttribute("address", address);

                return "order/order_success";
            }
            return "order/order_fail";
        } else {

            JsonNode failNode = responseEntity.getBody();
            model.addAttribute("message", failNode.get("message").asText());
            model.addAttribute("code", failNode.get("code").asText());
            return "order/order_fail";
        }
    }

}
