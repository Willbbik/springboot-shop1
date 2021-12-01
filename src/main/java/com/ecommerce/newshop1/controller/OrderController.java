package com.ecommerce.newshop1.controller;

import com.ecommerce.newshop1.dto.CallbackPayload;
import com.ecommerce.newshop1.dto.ItemDto;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.exception.ItemNotFoundException;
import com.ecommerce.newshop1.repository.ItemRepository;
import com.ecommerce.newshop1.service.RedisService;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final ItemRepository itemRepository;
    private final RedisService redisService;

    ModelMapper mapper = new ModelMapper();

    @Value("${tosspayments.secret_key}")
    String SECRET_KEY;


    @GetMapping("/order/checkout")
    public String checkout(@RequestParam Long[] itemId, @RequestParam Long[] quantity, HttpServletRequest request, Model model) throws Exception {

        List<ItemDto> items = new ArrayList<>();      // view에 상품 띄워주기 위해서
        List<Long> orderIdList = new ArrayList<>();       // session에 상품 번호 저장하기 위해서
        List<Long> quantityList = new ArrayList<>(); // session에 상품 개수 저장하기 위해서


        if(itemId.length < 0 || quantity.length < 0 || itemId.length != quantity.length){
            return "error/404";
        }

        int totalPrice = 0;
        for(int i = 0; i < itemId.length; i++){

            Item item = itemRepository.findById(itemId[i])
                    .orElseThrow(() -> new ItemNotFoundException("해당 상품이 존재하지 않습니다."));

            items.add(mapper.map(item, ItemDto.class));

            orderIdList.add(itemId[i]);          // 상품 번호
            quantityList.add(quantity[i]);       // 상품 개수
            totalPrice += item.getPrice() * quantity[i];    // 최종 결제금액 계산
        }

        HttpSession session = request.getSession();
        session.setAttribute("order_id", orderIdList);
        session.setAttribute("order_quantity", quantityList);

        String nowDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String orderId = redisService.createOrderId(nowDate, totalPrice);

        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("items", items);
        model.addAttribute("orderId", orderId);

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
