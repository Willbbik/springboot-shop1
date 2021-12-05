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
    public String checkout(String itemList, String where, Model model, HttpServletRequest request) throws Exception {

        List<ItemDto> items = new ArrayList<>();   // view에 상품 띄워주기 위해서
        String orderName = ""; // 브라우저의 sessionStorage에 저장 ( 가상계좌 결제시 주문 상품명으로 사용하기 위해서 )
        int totalPrice = 0;

        if(where.equals("product")){
            items = orderService.itemToPayment(itemList);// 사용자가 구매하려는 상품
            orderName = items.get(0).getItemName();     // 주문 상품명
            totalPrice += items.get(0).getTotalPrice(); // 최종 결제 금액
        }else if(where.equals("cart")) {
            items = cartService.cartItemToPayment(itemList);                          // 사용자가 구매하려는 상품들
            orderName = items.get(0).getItemName() + "외 " + (items.size() - 1) + "건";// 주문 상품명
            for(ItemDto itemDto : items){
                totalPrice += itemDto.getTotalPrice();                                // 최종 결제 금액
            }
        }else{
            return "redirect:/";
        }

        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String orderId = date + orderService.createOrderId(date, totalPrice);        // 주문번호

        HttpSession session = request.getSession();

        session.setAttribute("test", orderId);

        model.addAttribute("items", items);
        model.addAttribute("orderId", orderId);
        model.addAttribute("orderName", orderName);
        model.addAttribute("totalPrice", totalPrice);

        return "order/order_checkout";
    }




    @PostMapping("/order/paymethod/check")
    public @ResponseBody String payMethodCheck(String payType){
        PayMethod payMethod = PayMethod.findByPayType(payType);
        if(payMethod.getTitle().equals("없음")){
            return "fail";
        }else{
            return "success";
        }
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
    public @ResponseBody String saveAddressDto(AddressDto addressDto, HttpServletRequest request){
        // 주문하기 버튼 클릭시 ajax로 먼저 상품 정보들 가져와서
        // session에 저장해놓고 주문 성공 세션에 있는 값들로 Delivery객체 만들기 위해서
        // 즉 session에 저장해놓는 테스트

        HttpSession session = request.getSession();
        session.setAttribute("addressDto", addressDto);

        System.out.println(addressDto);
        return "good";
    }


    @RequestMapping("/success")
    public String confirmPayment(@RequestParam String paymentKey, @RequestParam String orderId,
                                 @RequestParam int amount, Model model, HttpServletRequest request) throws Exception {

        // 결제 정보 유효성 검사
        // 주문페이지로 이동할 때 redis에 주문번호 : 금액으로 저장해놨던 값과 비교
        // redis에서 주문번호 값으로 확인하고 fail로 보내거나 로직 실행하면됨

        // 결제 승인 요청
        ResponseEntity<JsonNode> responseEntity = orderService.tossPayment(paymentKey, orderId, amount);

        if(responseEntity.getStatusCode() == HttpStatus.OK) {
            // 여기서 Delivery객체 생성해야함

            HttpSession session = request.getSession();
            System.out.println(session.getAttribute("customerName"));

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
