package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.OrderItemDto;
import com.ecommerce.newshop1.dto.TossVirtualAccount;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface OrderService {

    String createOrderId(String nowDate, int totalPrice) throws Exception;

    List<OrderItemDto> itemToPayment(String itemList);

    ResponseEntity<JsonNode> tossPayment(String paymentKey, String orderId, int amount) throws Exception;

    TossVirtualAccount getVirtualAccountInfo(JsonNode successNode);

    void doOrder(HttpSession session);

}
