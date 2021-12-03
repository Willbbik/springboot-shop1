package com.ecommerce.newshop1.controller;

import com.ecommerce.newshop1.dto.CallbackPayload;
import com.ecommerce.newshop1.dto.ItemDto;
import com.ecommerce.newshop1.dto.OrderItemDto;
import com.ecommerce.newshop1.repository.ItemRepository;
import com.ecommerce.newshop1.service.CartService;
import com.ecommerce.newshop1.service.ItemService;
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

    ModelMapper mapper = new ModelMapper();

    @Value("${tosspayments.secret_key}")
    String SECRET_KEY;


    @PostMapping("/order/checkout")
    public String checkout(String itemList, String where, Model model) throws Exception {

        List<ItemDto> items = new ArrayList<>();   // view에 상품 띄워주기 위해서
        String orderName = ""; // 브라우저의 sessionStorage에 저장 ( 가상계좌 결제시 주문 상품명으로 사용하기 위해서 )
        int totalPrice = 0;

        if(where.equals("product")){
            items = itemService.itemToPayment(itemList);// 사용자가 구매하려는 상품
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
        String orderId = date + itemService.createOrderId(date, totalPrice);        // 주문번호

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


    @RequestMapping("/success")
    public String confirmPayment(@RequestParam String paymentKey, @RequestParam String orderId,
                                 @RequestParam Long amount, Model model) throws Exception {

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(SECRET_KEY, "");
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> payloadMap = new HashMap<>();
        payloadMap.put("orderId", orderId);
        payloadMap.put("amount", String.valueOf(amount));

        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(payloadMap), headers);

        ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
                "https://api.tosspayments.com/v1/payments/" + paymentKey, request, JsonNode.class);
        if(responseEntity.getStatusCode() == HttpStatus.OK) {
            JsonNode successNode = responseEntity.getBody();
            model.addAttribute("orderId", successNode.get("orderId").asText());
            String secret = successNode.get("secret").asText();
            return "order/order_success";
        } else {
            JsonNode failNode = responseEntity.getBody();
            model.addAttribute("message", failNode.get("message").asText());
            model.addAttribute("code", failNode.get("code").asText());
            return "order/order_fail";
        }

    }

}
