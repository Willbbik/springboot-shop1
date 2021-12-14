package com.ecommerce.newshop1.controller;

import com.ecommerce.newshop1.dto.*;
import com.ecommerce.newshop1.entity.OrderPaymentInformation;
import com.ecommerce.newshop1.exception.ParameterNotFoundException;
import com.ecommerce.newshop1.repository.ItemRepository;
import com.ecommerce.newshop1.service.*;
import com.ecommerce.newshop1.utils.ValidationGroups;
import com.ecommerce.newshop1.utils.ValidationSequence;
import com.ecommerce.newshop1.utils.enums.PayType;
import com.ecommerce.newshop1.utils.enums.TossPayments;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private final CartService cartService;
    private final OrderService orderService;

    ModelMapper mapper = new ModelMapper();


    @PostMapping("/order/checkout")
    @ApiOperation(value = "구매할 상품 선택후 주문페이지로 이동")
    public String checkout(String itemList, String where, Model model, HttpServletRequest request) throws Exception {

        List<OrderItemDto> orderItems = new ArrayList<>();
        String orderName = ""; // 주문 상품명 ( 브라우저의 sessionStorage에 저장하기 위해서 )
        HttpSession session = request.getSession();
        int totalPrice = 0;

        if(where.equals("product")){
            orderItems = orderService.itemToPayment(itemList);
            orderName = orderItems.get(0).getItem().getItemName();
            totalPrice += orderItems.get(0).getTotalPrice();
        }
        else if(where.equals("cart")) {
            orderItems = cartService.cartItemToPayment(itemList, session);
            orderName = orderItems.get(0).getItem().getItemName() + " 외 " + (orderItems.size() - 1) + "건"; // 주문 상품명
            for(OrderItemDto itemDto : orderItems){
                totalPrice += itemDto.getTotalPrice();
            }
        } else{
            throw new ParameterNotFoundException("주문 페이지 이동시 필수 파라미터 'where'가 정상적인 값이 아님");
        }

        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String orderId = date + orderService.createOrderId(date, totalPrice);        // 주문번호 생성

        // 주문번호와 상품 번호들 세션에 저장. ( 마지막 최종 주문할 때 사용하기 위해서 )
        session.setAttribute("orderId", orderId);
        session.setAttribute("orderItems", orderItems);

        model.addAttribute("orderItems", orderItems);
        model.addAttribute("orderId", orderId);
        model.addAttribute("orderName", orderName);
        model.addAttribute("totalPrice", totalPrice);

        return "order/order_checkout";
    }


    @RequestMapping("/order/virtual-account/callback")
    @ResponseStatus(HttpStatus.OK)
    public void paymentCheck(@RequestBody CallbackPayload callbackPayload){

        if(callbackPayload.getStatus().equals(TossPayments.DONE.getValue())){
            // 입금 완료일 때 처리
            orderService.updateOrderDepositStatus(callbackPayload.getOrderId());

        }else if(callbackPayload.getStatus().equals(TossPayments.CANCELED.getValue())){
            // 입금 취소일 때 처리
        }
    }


    @PostMapping("/order/order")
    public @ResponseBody String saveAddressDto(@Validated(ValidationSequence.class) AddressDto addressDto, BindingResult errors, String payMethod, HttpServletRequest request){

        HttpSession session = request.getSession();
        PayType payType = PayType.findByPayType(payMethod);

        if(payType.getTitle().equals("없음")){
            return "fail";
        }else if(errors.hasErrors()){
            String message = "";
            for(FieldError error : errors.getFieldErrors()){

                message = error.getDefaultMessage();
                break;
            }
            return message;
        }

        else{
            session.setAttribute("addressDto", addressDto);
            session.setAttribute("payType", payType);
            return "success";
        }
    }


    @RequestMapping("/success")
    public String confirmPayment(@RequestParam String paymentKey, @RequestParam String orderId,
                                 @RequestParam int amount, Model model, HttpServletRequest request) throws Exception {

        // 결제 승인 요청
        ResponseEntity<JsonNode> responseEntity = orderService.tossPayment(paymentKey, orderId, amount);

        if(responseEntity.getStatusCode() == HttpStatus.OK) {

            // 로그인 검사

            String payType = responseEntity.getBody().get("method").asText();

            if(payType.equals("가상계좌")){
                OrderPaymentInformation paymentInfo = orderService.getVirtualAccountInfo(responseEntity.getBody());
                orderService.doOrder(request.getSession(), paymentInfo);
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
