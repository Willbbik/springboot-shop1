package com.ecommerce.newshop1.service;


import com.ecommerce.newshop1.dto.OrderDto;
import com.ecommerce.newshop1.entity.Delivery;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.entity.Order;
import com.ecommerce.newshop1.repository.MemberRepository;
import com.ecommerce.newshop1.repository.OrderRepository;
import com.ecommerce.newshop1.utils.enums.DepositStatus;
import com.ecommerce.newshop1.utils.enums.Role;
import com.ecommerce.newshop1.utils.enums.Sns;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest
@Transactional
class OrderServiceImplTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    OrderService orderService;

    @Test
    void searchAllByMember() {

        //given
        Member member = Member.builder()
                .userId("test")
                .password("password")
                .role(Role.MEMBER)
                .sns(Sns.NONE)
                .phoneNum("01081387026")
                .build();
        memberRepository.save(member);

        for(int i = 1; i <= 30; i++){
            orderRepository.save(Order.builder()
                    .member(member)
                    .createdDate(LocalDateTime.now())
                    .orderNum("qwedsahdalhqwfjkhfkwjh")
                    .build());
        }

        Member findMember = memberRepository.findByuserId("test").get();

        //when
        List<OrderDto> orderDtoList = orderService.searchAllByMember(null, findMember);

        //then
        Assertions.assertThat(orderDtoList).hasSize(3);
        Assertions.assertThat(orderDtoList.get(0).getMember()).isEqualTo(findMember);

    }


    @Test
    void updateOrderDepositStatus() {

        //given
        Member member = Member.builder()
                .userId("test")
                .password("password")
                .phoneNum("01012345678")
                .role(Role.MEMBER)
                .sns(Sns.NONE)
                .build();
        memberRepository.save(member);


        Delivery delivery = Delivery.builder()
                .depositStatus(DepositStatus.DEPOSIT_READY)
                .build();
        Order order = Order.builder()
                .member(member)
                .delivery(delivery)
                .orderNum("123456789")
                .createdDate(LocalDateTime.now())
                .build();

        orderRepository.save(order);

        //when
        orderService.updateOrderDepositStatus("123456789");

        //then
        assertEquals(DepositStatus.DEPOSIT_SUCCESS, order.getDelivery().getDepositStatus());
    }


}