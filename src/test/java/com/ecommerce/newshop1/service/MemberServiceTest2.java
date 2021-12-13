package com.ecommerce.newshop1.service;


import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.repository.MemberRepository;
import com.ecommerce.newshop1.utils.enums.Role;
import com.ecommerce.newshop1.utils.enums.Sns;
import org.apache.catalina.core.ApplicationContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
public class MemberServiceTest2 {


    @Autowired
    MemberRepository memberRepository;

    @Test
    void joinNormal(){

        Member member = Member.builder()
                .id(1L)
                .userId("userid")
                .password("password")
                .phoneNum("01081387026")
                .role(Role.MEMBER)
                .sns(Sns.NONE)
                .build();

        Member joinMember = memberRepository.save(member);
        Member findMember = memberRepository.findById(1L).get();

        assertThat(joinMember.getId()).isEqualTo(findMember.getId());

    }



}
