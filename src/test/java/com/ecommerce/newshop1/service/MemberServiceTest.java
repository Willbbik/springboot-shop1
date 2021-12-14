package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.MemberDto;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.repository.MemberRepository;
import com.ecommerce.newshop1.utils.enums.Role;
import com.ecommerce.newshop1.utils.enums.Sns;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    @Transactional
    @DisplayName("일반 회원가입 테스트")
    void joinNormal() {

        // given
        Member member = Member.builder()
                .userId("test")
                .password("password")
                .phoneNum("01081387026")
                .role(Role.MEMBER)
                .sns(Sns.NONE)
                .build();

        // when
        memberService.joinNormal(member);

        // then
        Optional<Member> findMember = memberService.findByUserId("test");
        assertEquals("test", findMember.get().getUserId());
    }


}