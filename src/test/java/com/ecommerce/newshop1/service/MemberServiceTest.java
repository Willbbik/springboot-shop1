package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.MemberDto;
import com.ecommerce.newshop1.entity.MemberEntity;
import com.ecommerce.newshop1.repository.MemberRepository;
import com.ecommerce.newshop1.utils.enums.Role;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


public class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @Test
    public void 회원가입(){

        //given
        MemberDto memberDto = MemberDto.builder()
                .userid("test")
                .pswd("passwordsda")
                .sns("none")
                .role(Role.MEMBER)
                .phonenum("01081387026")
                .build();

        //when
        Long id = memberService.joinNormal(memberDto, "none");

        //then
        Optional<MemberEntity> dto = memberRepository.findById(id);
        MemberEntity memberEntity = dto.get();

        assertThat(memberEntity.getUserid()).isEqualTo("test");

    }


    @Test
    public void 아이디찾기(){
        //given

        String userid = "test";

        //when
        Object qwe = memberService.findByUserId(userid);

        //then
    }


}
