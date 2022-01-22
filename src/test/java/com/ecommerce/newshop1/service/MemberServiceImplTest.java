package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.enums.Role;
import com.ecommerce.newshop1.enums.Sns;
import com.ecommerce.newshop1.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceImplTest {

    @Autowired
    MemberServiceImpl memberService;

    @Autowired
    CartServiceImpl cartService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("일반 회원가입 테스트")
    void joinNormal() {

        // given
        Member member = Member.builder()
                .userId("t")
                .password("password")
                .phoneNum("01012341234")
                .role(Role.MEMBER)
                .sns(Sns.NONE)
                .build();

        // when
        Long id = memberService.joinNormal(member).getId();

        // then
        Optional<Member> findMember = memberRepository.findById(id);

        assertAll(
                () -> assertTrue(findMember.isPresent()),
                () -> assertTrue(findMember.get().getUserId().equals(member.getUserId()))
        );

    }


    @Test
    @DisplayName("회원가입시 장바구니 생성유무 확인")
    void 장바구니생성확인(){
        // given
        Member member = Member.builder()
                .userId("t")
                .password("password")
                .phoneNum("01012341234")
                .role(Role.MEMBER)
                .sns(Sns.NONE)
                .build();

        // when
        memberService.joinNormal(member);
        cartService.createCart(member);

        // then
        assertAll(
                () -> assertNotNull(member.getCart())
        );

    }


}