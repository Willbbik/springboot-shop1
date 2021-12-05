package com.ecommerce.newshop1.controller;

import com.ecommerce.newshop1.dto.*;
import com.ecommerce.newshop1.repository.ItemRepository;
import com.ecommerce.newshop1.service.CartService;
import com.ecommerce.newshop1.service.ItemService;
import com.ecommerce.newshop1.service.OrderService;
import com.ecommerce.newshop1.utils.enums.PayMethod;
import com.ecommerce.newshop1.utils.enums.TossPayments;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Value("${tosspayments.secret_key}")
    String SECRET_KEY;


    @PostMapping("/order/checkout")
    @ApiOperation(value = "구매할 상품 선택후 주문페이지로 이동")
    public String checkout(String itemList, String where, Model model, HttpServletRequest request) throws Exception {

        List<OrderItemDto> orderItems = new ArrayList<>();   // view에 상품 띄워주기 위해서
        String orderName = ""; // 브라우저의 sessionStorage에 저장 ( 가상계좌 결제시 주문 상품명으로 사용하기 위해서 )
        int totalPrice = 0;

        if(where.equals("product")){        // 상품 상세보기에서 주문시
            orderItems = orderService.itemToPayment(itemList);       // 사용자가 구매하려는 상품
            orderName = orderItems.get(0).getItem().getItemName();   // 주문 상품명
            totalPrice += orderItems.get(0).getTotalPrice();         // 최종 결제 금액
        }
        else if(where.equals("cart")) {     // 장바구니에서 주문시
            orderItems = cartService.cartItemToPayment(itemList);                          // 사용자가 구매하려는 상품들
            orderName = orderItems.get(0).getItem().getItemName() + "외 " + (orderItems.size() - 1) + "건";// 주문 상품명
            for(OrderItemDto itemDto : orderItems){
                totalPrice += itemDto.getTotalPrice();                                // 최종 결제 금액
            }
        }else{
            throw new Exception("주문 페이지 이동시 필수 파라미터 'where'가 존재하지않음");
        }

        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String orderId = date + orderService.createOrderId(date, totalPrice);        // 주문번호

        HttpSession session = request.getSession();
        session.setAttribute("orderId", orderId);
        session.setAttribute("orderItems", orderItems);
        session.setAttribute("totalPrice", totalPrice);

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
        }else if(callbackPayload.getStatus().equals(TossPayments.CANCELED.getValue())){
            // 입금 취소일 때 처리
        }
    }


    @PostMapping("/order/saveAddress")
    @ApiOperation(value = "주문페이지로 이동후 주문버튼 클릭시 배송정보와 주문자정보 세션에 저장")
    public @ResponseBody String saveAddressDto(AddressDto addressDto, HttpServletRequest request){

        // 배송정보 유효성 검사해야함
        HttpSession session = request.getSession();
        session.setAttribute("addressDto", addressDto);

        return "";
    }


    @RequestMapping("/success")
    public String confirmPayment(@RequestParam String paymentKey, @RequestParam String orderId,
                                 @RequestParam int amount, Model model, HttpServletRequest request) throws Exception {

        // 결제 승인 요청
        ResponseEntity<JsonNode> responseEntity = orderService.tossPayment(paymentKey, orderId, amount);

        if(responseEntity.getStatusCode() == HttpStatus.OK) {

            // 주문 메소드 생성해야함

            TossVirtualAccount toss = orderService.getVirtualAccountInfo(responseEntity.getBody());
            model.addAttribute("toss", toss);
            return "order/order_success";
        } else {

            JsonNode failNode = responseEntity.getBody();
            model.addAttribute("message", failNode.get("message").asText());
            model.addAttribute("code", failNode.get("code").asText());
            return "order/order_fail";
        }

    }

}
