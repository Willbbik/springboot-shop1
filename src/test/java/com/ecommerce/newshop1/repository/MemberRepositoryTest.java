package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.enums.Role;
import com.ecommerce.newshop1.enums.Sns;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("회원가입")
    void saveMember(){

        // given
        Member member = new Member();
            member.setUserId("test");
            member.setPassword("password");
            member.setPhoneNum("01012341234");
            member.setSns(Sns.NONE);
            member.setRole(Role.MEMBER);

        // when
        Long id = memberRepository.save(member).getId();

        // then
        Member findMember = memberRepository.findById(id).get();
        Assertions.assertTrue(member.equals(findMember));

    }


}