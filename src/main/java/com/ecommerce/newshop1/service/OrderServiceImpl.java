package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.ItemDto;
import com.ecommerce.newshop1.dto.TossVirtualAccount;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.exception.ItemNotFoundException;
import com.ecommerce.newshop1.repository.ItemRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ItemRepository itemRepository;
    private final RedisService redisService;

    ModelMapper mapper = new ModelMapper();

    @Value("${tosspayments.secret_key}")
    String SECRET_KEY;

    @Override
    public String createOrderId(String nowDate, int totalPrice) throws Exception {
        return redisService.createOrderId(nowDate, totalPrice);
    }

    @Override
    @Transactional
    public List<ItemDto> itemToPayment(String itemList) {

        JsonParser jsonParser = new JsonParser();
        JsonArray jsonElements = (JsonArray) jsonParser.parse(itemList);
        JsonObject jsonItem = (JsonObject) jsonElements.get(0);

        Long itemId = Long.parseLong(jsonItem.get("itemId").getAsString());
        int quantity = Integer.parseInt(jsonItem.get("quantity").getAsString());

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("해당 상품이 존재하지 않습니다. 상품번호 : " + itemId));

        ItemDto itemDto = mapper.map(item, ItemDto.class);
        itemDto.setTotalPrice(item.getPrice() * quantity);
        itemDto.setQuantity(quantity);

        List<ItemDto> itemDtoList = new ArrayList<>();
        itemDtoList.add(itemDto);

        return itemDtoList;
    }

    @Override
    public ResponseEntity<JsonNode> tossPayment(String paymentKey, String orderId, int amount) throws Exception {

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        // header 담기
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(SECRET_KEY, "");
        headers.setContentType(MediaType.APPLICATION_JSON);

        // body 만들기
        Map<String, String> payloadMap = new HashMap<>();
        payloadMap.put("orderId", orderId);
        payloadMap.put("amount", String.valueOf(amount));

        // http header와 body 합치기
        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(payloadMap), headers);

        // post 요청하기
        return restTemplate.postForEntity(
                "https://api.tosspayments.com/v1/payments/" + paymentKey, request, JsonNode.class);
    }

    @Override
    public TossVirtualAccount getVirtualAccountInfo(JsonNode successNode) {

        JsonNode virtualAccount = successNode.get("virtualAccount");

        return TossVirtualAccount.builder()
                .accountNumber(virtualAccount.get("accountNumber").asText())
                .accountType(virtualAccount.get("accountType").asText())
                .bank(virtualAccount.get("bank").asText())
                .customerName(virtualAccount.get("customerName").asText())
                .dueDate(virtualAccount.get("dueDate").asText())
                .refundStatus(virtualAccount.get("refundStatus").asText())
                .expired(virtualAccount.get("expired").asBoolean())
                .settlementStatus(virtualAccount.get("settlementStatus").asText())
                .build();
    }

}
