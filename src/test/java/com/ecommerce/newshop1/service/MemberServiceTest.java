package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.MemberDto;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.repository.MemberRepository;
import com.ecommerce.newshop1.utils.enums.Role;
import com.ecommerce.newshop1.utils.enums.Sns;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;


public class MemberServiceTest {

    private MemberService memberService = new MemberService();

    @Test
    public void 회원가입(){

        //given
        MemberDto memberDto = MemberDto.builder()
                .userid("asdasd")
                .password("asdasdasd")
                .sns(Sns.NONE)
                .role(Role.MEMBER)
                .phonenum("01081387026")
                .build();

        //when
        String userId = memberService.joinNormal(memberDto).getUserid();

        //then
        Member result = memberService.findByUserId(userId).get();

        assertThat(result.getUserid()).isEqualTo("asdasd");

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
