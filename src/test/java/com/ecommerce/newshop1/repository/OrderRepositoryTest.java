//package com.ecommerce.newshop1.repository;
//
//import com.ecommerce.newshop1.entity.Member;
//import com.ecommerce.newshop1.entity.Order;
//import com.ecommerce.newshop1.service.MemberService;
//import com.ecommerce.newshop1.utils.enums.Role;
//import com.ecommerce.newshop1.utils.enums.Sns;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//class OrderRepositoryTest {
//
//    @Autowired
//    MemberRepository memberRepository;
//
//    @Autowired
//    OrderRepository orderRepository;
//
//
//    @Test
//    void searchAllByMember(){
//
//        //given
//        Member member = new Member();
//            member.setId(3L);
//            member.setUserId("test");
//            member.setPassword("password");
//            member.setPhoneNum("01081387026");
//            member.setSns(Sns.NONE);
//            member.setRole(Role.MEMBER);
//
//        member = memberRepository.save(member);
//
//        for(int i = 1; i <= 30; i++){
//            orderRepository.save(Order.builder()
//                    .member(member)
//                    .orderNum("qwhasldajhsdlkasjhd")
//                    .createdDate(LocalDateTime.now())
//                    .build());
//        }
//
//        Member findMember = memberRepository.findByuserId("userid").get();
//
//        //when
//        List<Order> orderList = orderRepository.searchAllByMember(null, findMember);
//
//        //then
//        Assertions.assertThat(orderList).hasSize(3);
//        Assertions.assertThat(orderList.get(0).getMember()).isEqualTo(findMember);
//
//    }
//
//
//
//}