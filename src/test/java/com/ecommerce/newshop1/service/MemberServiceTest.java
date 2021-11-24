package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.MemberDto;
import com.ecommerce.newshop1.utils.enums.Role;
import com.ecommerce.newshop1.utils.enums.Sns;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;


public class MemberServiceTest {


    @Test
    public void 회원가입(){

        //given
        MemberDto memberDto = MemberDto.builder()
                .userId("asdasd")
                .password("asdasdasd")
                .sns(Sns.NONE)
                .role(Role.MEMBER)
                .phoneNum("01081387026")
                .build();

    }


    @Test
    public void 아이디찾기(){
        //given

        String userid = "test";


        //then
    }


}
