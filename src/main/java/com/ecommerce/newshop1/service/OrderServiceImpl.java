package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.AddressDto;
import com.ecommerce.newshop1.dto.OrderItemDto;
import com.ecommerce.newshop1.entity.*;
import com.ecommerce.newshop1.exception.ItemNotFoundException;
import com.ecommerce.newshop1.exception.MemberNotFoundException;
import com.ecommerce.newshop1.repository.*;
import com.ecommerce.newshop1.utils.enums.DeliveryStatus;
import com.ecommerce.newshop1.utils.enums.DepositStatus;
import com.ecommerce.newshop1.utils.enums.PayType;
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

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final PaymentInfoRepository paymentInfoRepository;
    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final RedisService redisService;
    private final CartService cartService;
    private final SecurityService security;

    ModelMapper mapper = new ModelMapper();

    @Value("${tosspayments.secret_key}")
    String SECRET_KEY;

    @Override
    public String createOrderId(String nowDate, int totalPrice) throws Exception {
        return redisService.createOrderId(nowDate, totalPrice);
    }

    @Override
    @Transactional
    public List<OrderItemDto> itemToPayment(String itemList) {

        JsonParser jsonParser = new JsonParser();
        JsonArray jsonElements = (JsonArray) jsonParser.parse(itemList);
        JsonObject jsonItem = (JsonObject) jsonElements.get(0);

        Long itemId = Long.parseLong(jsonItem.get("itemId").getAsString());
        int quantity = Integer.parseInt(jsonItem.get("quantity").getAsString());

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("해당 상품이 존재하지 않습니다. 상품번호 : " + itemId));

        OrderItemDto itemDto = OrderItemDto.builder()
                .item(item)
                .quantity(quantity)
                .totalPrice(item.getPrice() * quantity)
                .deliveryStatus(DeliveryStatus.DEPOSIT_READY)
                .build();

        List<OrderItemDto> itemDtoList = new ArrayList<>();
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
    public OrderPaymentInformation getVirtualAccountInfo(JsonNode successNode) {

        JsonNode virtualAccount = successNode.get("virtualAccount");

        return OrderPaymentInformation.builder()
                .payType(PayType.VIRTUAL_ACCOUNT.getTitle())
                .accountNumber(virtualAccount.get("accountNumber").asText())
                .bank(virtualAccount.get("bank").asText())
                .dueDate(virtualAccount.get("dueDate").asText())
                .build();
    }

    @Override
    @Transactional
    public void doOrder(HttpSession session, OrderPaymentInformation paymentInfo) {

        // 세션에서 배송 정보 가져오기
        String orderId = (String) session.getAttribute("orderId");
        List<OrderItemDto> itemList  = (List<OrderItemDto>) session.getAttribute("orderItems");
        AddressDto addressDto = (AddressDto) session.getAttribute("addressDto");
        PayType payType = (PayType) session.getAttribute("payType");
        List<Long> cartItemIdList = (List<Long>) session.getAttribute("cartItemIdList");
        String userId = security.getName();

        // order객체에 저장하기 위해서
        Member member = memberRepository.findByuserId(userId)
                .orElseThrow(() -> new MemberNotFoundException("해당 아이디가 존재하지 않습니다. 아이디 : " + userId));
        List<OrderItem> orderItems = itemList.stream()
                .map(p -> mapper.map(p, OrderItem.class)).collect(Collectors.toList());

        // 배송지
        DeliveryAddress deliveryAddress = mapper.map(addressDto, DeliveryAddress.class);

        Delivery delivery = new Delivery();
        delivery.setDeliveryAddress(deliveryAddress);
        delivery.setDepositStatus(DepositStatus.DEPOSIT_READY);

        Order order = Order.createOrder(member, delivery, orderItems, payType, paymentInfo, orderId);

        // 주문
        orderRepository.save(order);
        // 장바구니에서 지우기
        if(cartItemIdList != null) {
            cartService.deleteCartItemAllById(cartItemIdList);
            session.removeAttribute("cartItemIdList");
        }

        // 주문완료 후 세션에서 배송정보 제거
        session.removeAttribute("orderId");
        session.removeAttribute("orderItems");
        session.removeAttribute("addressDto");
        session.removeAttribute("payType");
    }

}
