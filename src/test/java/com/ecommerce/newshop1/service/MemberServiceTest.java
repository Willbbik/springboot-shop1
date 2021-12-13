package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.MemberDto;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.repository.MemberRepository;
import com.ecommerce.newshop1.utils.enums.Role;
import com.ecommerce.newshop1.utils.enums.Sns;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;

    @Mock
    RedisService redisService;

    @Mock
    CustomUserDetailsService customUserDetailsService;

    @Mock
    SecurityService security;

    @Mock
    PasswordEncoder passwordEncoder;


    @Test
    void joinNormal() {

        MemberService memberService = new MemberService(memberRepository, redisService, customUserDetailsService, security, passwordEncoder);

        // given
        MemberDto dto = MemberDto.builder()
                .id(1L)
                .userId("test")
                .password("password")
                .phoneNum("01081387026")
                .role(Role.MEMBER)
                .sns(Sns.NONE)
                .build();

        // when
        memberService.joinNormal(dto);

        // 비어있는지 아닌지
        Optional<Member> member = memberService.findByUserId("test");

        System.out.println(member);


    }
}