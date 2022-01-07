package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.OrderDto;
import com.ecommerce.newshop1.dto.OrderItemDto;
import com.ecommerce.newshop1.dto.SearchDto;
import com.ecommerce.newshop1.entity.Delivery;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.entity.Order;
import com.ecommerce.newshop1.entity.OrderPaymentInformation;
import com.ecommerce.newshop1.enums.DeliveryStatus;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface OrderService {

    String createOrderNum();

    Order findById(Long id);

    Order findByOrderNum(String orderNum);

    Model getModelPayInfo(Order order, Model model);

    List<OrderItemDto> itemToPayment(String itemList);

    ResponseEntity<JsonNode> tossPayment(String paymentKey, String orderId, int amount) throws Exception;

    OrderPaymentInformation getVirtualAccountInfo(JsonNode successNode);

    String doOrder(HttpSession session, OrderPaymentInformation paymentInfo, Delivery delivery);

    List<OrderDto> searchAllByMember(Long orderId, Member member);

    void updateOrderToDepositSuccess(String orderId);

    Long getLastOrderId(List<OrderDto> orderList, Long lastOrderId);

    List<OrderDto> searchByDepositSuccess(DeliveryStatus deliveryStatus, Pageable pageable);

    List<OrderItemDto> searchBySearchDtoAndDeliveryStatus(SearchDto searchDto, DeliveryStatus deliveryStatus, Pageable pageable);

    List<OrderItemDto> searchByDeliveryStatus(DeliveryStatus deliveryStatus, Pageable pageable);

    List<OrderItemDto> searchAllByDeliveryStatus(DeliveryStatus deliveryStatus, Pageable pageable, SearchDto searchDto);

    Long searchTotalOrderItem(DeliveryStatus deliveryStatus, SearchDto searchDto);

}
