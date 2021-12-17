//package com.ecommerce.newshop1.repository;
//
//import com.ecommerce.newshop1.entity.Member;
//import com.ecommerce.newshop1.utils.enums.Role;
//import com.ecommerce.newshop1.utils.enums.Sns;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//
//@SpringBootTest
//class MemberRepositoryTest {
//
//    @Autowired
//    MemberRepository memberRepository;
//
//    @Test
//    void saveMember(){
//
//        // given
//        Member member = new Member();
//            member.setId(3L);
//            member.setUserId("test");
//            member.setPassword("password");
//            member.setPhoneNum("01081387026");
//            member.setSns(Sns.NONE);
//            member.setRole(Role.MEMBER);
//
//        // when
//        Member member1 = memberRepository.save(member);
//
//        // then
//        Assertions.assertEquals("test", member1.getUserId());
//
//    }
//
//
//}