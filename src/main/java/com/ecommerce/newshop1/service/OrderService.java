package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.AddressDto;
import com.ecommerce.newshop1.dto.OrderDto;
import com.ecommerce.newshop1.dto.OrderItemDto;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.entity.OrderPaymentInformation;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface OrderService {

    String createOrderId(String nowDate, int totalPrice) throws Exception;

    List<OrderItemDto> itemToPayment(String itemList);

    ResponseEntity<JsonNode> tossPayment(String paymentKey, String orderId, int amount) throws Exception;

    OrderPaymentInformation getVirtualAccountInfo(JsonNode successNode);

    void doOrder(HttpSession session, OrderPaymentInformation paymentInfo);

    List<OrderDto> searchAllByMember(Long orderId, Member member);

    void updateOrderDepositStatus(String orderId);


}
