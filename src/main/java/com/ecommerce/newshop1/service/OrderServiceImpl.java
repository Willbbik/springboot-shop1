package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.AddressDto;
import com.ecommerce.newshop1.dto.OrderDto;
import com.ecommerce.newshop1.dto.OrderItemDto;
import com.ecommerce.newshop1.dto.SearchDto;
import com.ecommerce.newshop1.entity.*;
import com.ecommerce.newshop1.exception.ItemNotFoundException;
import com.ecommerce.newshop1.exception.MemberNotFoundException;
import com.ecommerce.newshop1.repository.*;
import com.ecommerce.newshop1.enums.DeliveryStatus;
import com.ecommerce.newshop1.enums.PayType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
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

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final RedisService redisService;
    private final CartService cartService;
    private final SecurityService security;

    ModelMapper mapper = new ModelMapper();

    @Value("${tosspayments.secret_key}")
    String SECRET_KEY;

    @Override
    public Long getLastOrderId(List<OrderDto> orderList, Long lastOrderId){

        if(orderList.size() > 1){
            int lastIndex = orderList.size() - 1;
            return orderList.get(lastIndex).getId();
        }else if(orderList.size() == 1){
            return orderList.get(0).getId();
        }else{
            return lastOrderId;
        }
    }

    @Override
    public String createOrderId(String nowDate, int totalPrice) throws Exception {
        return redisService.createOrderId(nowDate, totalPrice);
    }

    @Override
    public Long searchTotalOrderItem(DeliveryStatus deliveryStatus, SearchDto searchDto) {
        return orderRepository.searchTotalOrderItem(deliveryStatus, searchDto);
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
        AddressDto addressDto = (AddressDto) session.getAttribute("addressDto");
        PayType payType = (PayType) session.getAttribute("payType");
        String orderName = (String) session.getAttribute("orderName");
        List<OrderItemDto> itemList  = (List<OrderItemDto>) session.getAttribute("orderItems");
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
        delivery.setDeliveryStatus(DeliveryStatus.DEPOSIT_READY);
        delivery.setOrderName(orderName);

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
        session.removeAttribute("orderName");
        session.removeAttribute("addressDto");
        session.removeAttribute("payType");
        session.removeAttribute("cartItemIdList");
    }


    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> searchAllByMember(Long orderId, Member member) {
        List<Order> orderList = orderRepository.searchAllByMember(orderId, member);
        return orderList.stream()
                .map(p -> mapper.map(p, OrderDto.class))
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public void updateOrderToDepositSuccess(String orderId) {

        Order order = orderRepository.findByOrderNum(orderId);
        order.getDelivery().setDeliveryStatus(DeliveryStatus.DEPOSIT_SUCCESS);
        for(OrderItem orderItem : order.getOrderItems()){
            orderItem.setDeliveryStatus(DeliveryStatus.DEPOSIT_SUCCESS);
        }
        orderRepository.save(order);
    }


    @Override
    @Transactional
    public List<OrderItemDto> searchByDeliveryStatus(DeliveryStatus deliveryStatus, Pageable pageable) {
        List<OrderItemDto> orderItemDtos = orderRepository.searchByDeliveryStatus(deliveryStatus, pageable);
        for(OrderItemDto dto : orderItemDtos){
            dto.setDeliveryAddress(dto.getOrder().getDelivery().getDeliveryAddress());
        }
        return orderItemDtos;
    }

    @Override
    @Transactional
    public List<OrderDto> searchByDepositSuccess(DeliveryStatus deliveryStatus, Pageable pageable) {
        return orderRepository.searchByDepositSuccess(deliveryStatus, pageable);

    }

    @Override
    @Transactional
    public List<OrderItemDto> searchAllByDeliveryStatus(DeliveryStatus deliveryStatus, Pageable pageable, SearchDto searchDto) {
        return orderRepository.searchAllByDeliveryStatus(deliveryStatus, pageable, searchDto);
    }
}
