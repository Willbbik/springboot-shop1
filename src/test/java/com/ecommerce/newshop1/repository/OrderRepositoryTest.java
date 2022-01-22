package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.entity.Order;
import com.ecommerce.newshop1.enums.Role;
import com.ecommerce.newshop1.enums.Sns;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderRepository orderRepository;


    @Test
    @DisplayName("특정 사용자가 주문한 주문들을 가져온다")
    void searchAllByMember(){

        //given
        Member member = new Member();
            member.setUserId("t");
            member.setPassword("password");
            member.setPhoneNum("01012341234");
            member.setSns(Sns.NONE);
            member.setRole(Role.MEMBER);

        member = memberRepository.save(member);

        for(int i = 1; i <= 30; i++){
            orderRepository.save(Order.builder()
                    .member(member)
                    .orderNum("testordernum"+i)
                    .createdDate(LocalDateTime.now())
                    .build());
        }

        //when
        Member findMember = memberRepository.findById(member.getId()).get();
        List<Order> orderList = orderRepository.searchAllByMember(null, findMember);

        //then
        assertAll(
                () -> assertTrue(!orderList.isEmpty()),
                () -> assertTrue(orderList.get(0).getMember().equals(findMember))
        );
    }



}