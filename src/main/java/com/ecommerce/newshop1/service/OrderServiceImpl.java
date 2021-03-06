package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.*;
import com.ecommerce.newshop1.entity.*;
import com.ecommerce.newshop1.exception.ItemNotFoundException;
import com.ecommerce.newshop1.exception.OrderNotFoundException;
import com.ecommerce.newshop1.repository.*;
import com.ecommerce.newshop1.enums.DeliveryStatus;
import com.ecommerce.newshop1.enums.PayType;
import com.ecommerce.newshop1.utils.CommonService;
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
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final ItemRepository itemRepository;
    private final CartService cartService;
    private final MemberService memberService;
    private final CommonService commonService;
    ModelMapper mapper = new ModelMapper();

    @Value("${tosspayments.secret_key}")
    String SECRET_KEY;

    @Override
    public Long getLastOrderId(List<OrderDto> orderList, Long lastOrderId){

        if(orderList.isEmpty()){
            return lastOrderId;
        }else{
            return orderList.stream()
                    .min(Comparator.comparingLong(OrderDto::getId))
                    .get().getId();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Order findById(Long id) {

        return orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public Order findByOrderNum(String orderNum) {

        return orderRepository.findByOrderNum(orderNum)
                .orElseThrow(() -> new OrderNotFoundException("?????? ??????????????? ????????? ???????????? ????????????." + orderNum));
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByDeliveryStatus(DeliveryStatus deliveryStatus) {
        return deliveryRepository.countByDeliveryStatus(deliveryStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public String createOrderNum() {

        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String random = String.valueOf(commonService.randomNum());

        String orderNum = date + random;
        boolean result = orderRepository.existsByOrderNum(orderNum);

        if(!result){
            return orderNum;
        }
        return createOrderNum();
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
                .orElseThrow(() -> new ItemNotFoundException("?????? ????????? ???????????? ????????????. ???????????? : " + itemId));

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
    public ResponseEntity<JsonNode> tossPayment(String paymentKey, String orderNum, int amount) throws Exception {

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        // header ??????
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(SECRET_KEY, "");
        headers.setContentType(MediaType.APPLICATION_JSON);

        // body ?????????
        Map<String, String> payloadMap = new HashMap<>();
        payloadMap.put("orderId", orderNum);
        payloadMap.put("amount", String.valueOf(amount));

        // http header??? body ?????????
        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(payloadMap), headers);

        // post ????????????
        return restTemplate.postForEntity(
                "https://api.tosspayments.com/v1/payments/" + paymentKey, request, JsonNode.class);
    }

    @Override
    public OrderPaymentInformation getVirtualAccountInfo(JsonNode successNode) {

        JsonNode virtualAccount = successNode.get("virtualAccount");

        return OrderPaymentInformation.builder()
                .payType(PayType.VIRTUAL_ACCOUNT.getTitle())
                .customerName(virtualAccount.get("customerName").asText())
                .accountNumber(virtualAccount.get("accountNumber").asText())
                .bank(virtualAccount.get("bank").asText())
                .dueDate(virtualAccount.get("dueDate").asText())
                .build();
    }

    @Override
    @Transactional
    public String doOrder(HttpSession session, OrderPaymentInformation paymentInfo, Delivery delivery) {

        // ???????????? ?????? ?????? ????????????
        String orderNum = (String) session.getAttribute("orderNum");
        String orderName = (String) session.getAttribute("orderName");
        List<OrderItemDto> itemList  = (List<OrderItemDto>) session.getAttribute("orderItems");
        List<Long> cartItemIdList = (List<Long>) session.getAttribute("cartItemIdList");
        AddressDto addressDto = (AddressDto) session.getAttribute("addressDto");
        PayType payType = (PayType) session.getAttribute("payType");

        // order??? ???????????? ?????????
        Member member = memberService.getCurrentMember();
        List<OrderItem> orderItems = itemList.stream()
                .map(p -> mapper.map(p, OrderItem.class)).collect(Collectors.toList());

        // ?????? ??????
        delivery.setDeliveryAddress(mapper.map(addressDto, DeliveryAddress.class));
        delivery.setOrderName(orderName);

        // ??????
        Order order = Order.createOrder(member, delivery, orderItems, payType, paymentInfo, orderNum);
        orderRepository.save(order);

        // ?????????????????? ????????? ?????? ?????? ????????? ?????????
        if(cartItemIdList != null) {
            cartService.deleteCartItemAll(cartItemIdList);
            session.removeAttribute("cartItemIdList");
        }

        // ???????????? ??? ???????????? ???????????? ??????
        session.removeAttribute("orderNum");
        session.removeAttribute("orderItems");
        session.removeAttribute("orderName");
        session.removeAttribute("addressDto");
        session.removeAttribute("payType");
        session.removeAttribute("totalPrice");

        return orderNum;
    }


    @Override
    @Transactional(readOnly = true)
    public Model getModelPayInfo(Order order, Model model){
        mapper.getConfiguration().setAmbiguityIgnored(true);

        OrderPaymentInfoDto payInfo = mapper.map(order.getPaymentInfo(), OrderPaymentInfoDto.class);
        OrderDto orderInfo = mapper.map(order, OrderDto.class);
        AddressDto address = mapper.map(order.getDelivery().getDeliveryAddress(), AddressDto.class);
        List<OrderItemDto> orderItems = order.getOrderItems().stream()
                                        .map(p -> mapper.map(p, OrderItemDto.class))
                                        .collect(Collectors.toList());

        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("orderItems", orderItems);
        model.addAttribute("payInfo", payInfo);
        model.addAttribute("address", address);
        model.addAttribute("method", payInfo.getPayType());

        return model;
    }


    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> searchAllByMember(Long lastOrderId, Member member) {
        List<Order> orderList = orderRepository.searchAllByMember(lastOrderId, member);
        return orderList.stream()
                .map(p -> mapper.map(p, OrderDto.class))
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public void updateOrderToDepositSuccess(String orderNum) {

        Order order = orderRepository.findByOrderNum(orderNum)
                .orElseThrow(() -> new OrderNotFoundException("???????????? ?????? ???????????????."));
        order.getDelivery().setDeliveryStatus(DeliveryStatus.DEPOSIT_SUCCESS);
        order.setDepositDate(LocalDateTime.now());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> searchByDeliveryStatus(DeliveryStatus deliveryStatus, Pageable pageable) {

        return orderRepository.searchByDeliveryStatus(deliveryStatus, pageable);
    }

}
