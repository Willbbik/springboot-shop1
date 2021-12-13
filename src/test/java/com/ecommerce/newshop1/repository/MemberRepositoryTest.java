package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.utils.enums.Role;
import com.ecommerce.newshop1.utils.enums.Sns;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(properties = "classpath:/application.yml," +
        "classpath:/yml/coolsms.yml," +
        "classpath:/yml/tosspayments.yml," +
        "classpath:/yml/oauth2.yml")
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void saveMember(){

        // given
        Member member = Member.builder()
                .id(3L)
                .userId("test")
                .password("password")
                .phoneNum("01081387026")
                .sns(Sns.NONE)
                .role(Role.MEMBER)
                .build();

        // when
        Member member1 = memberRepository.save(member);

        // then
        Assertions.assertEquals("test", member1.getUserId());

    }


}