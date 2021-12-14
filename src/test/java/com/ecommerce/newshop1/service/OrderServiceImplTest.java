package com.ecommerce.newshop1.service;


import com.ecommerce.newshop1.dto.OrderDto;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.entity.Order;
import com.ecommerce.newshop1.repository.MemberRepository;
import com.ecommerce.newshop1.repository.OrderRepository;
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
    @Transactional
    void searchAllByMember() {

        //given
        Member member = Member.builder()
                .userId("test")
                .password("password")
                .role(Role.MEMBER)
                .sns(Sns.NONE)
                .phoneNum("01081387026")
                .build();
        memberService.joinNormal(member);

        for(int i = 1; i <= 30; i++){
            orderRepository.save(Order.builder()
                    .member(member)
                    .createdDate(LocalDateTime.now())
                    .orderNum("qwedsahdalhqwfjkhfkwjh")
                    .build());
        }
        Member findMember = memberRepository.findByuserId("test").get();

        //when
        List<OrderDto> orderDtoList = orderRepository.searchAllByMember(null, findMember);

        //then
        Assertions.assertThat(orderDtoList).hasSize(30);

    }



}