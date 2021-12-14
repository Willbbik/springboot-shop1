package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("일반 회원가입 테스트")
    void joinNormal() {

        // given
        Member member = Member.builder()
                .userId("test")
                .password("password")
                .phoneNum("01081387026")
                .build();

        // when
        memberService.joinNormal(member);

        // then
        Optional<Member> findMember = memberService.findByUserId("test");
        assertEquals("test", findMember.get().getUserId());
    }


}