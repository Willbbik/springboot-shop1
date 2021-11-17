package com.ecommerce.newshop1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class JoinMemberDto {

    private Long id;
    private String userid;
    private String pswd;
    private String pswdCheck;
    private String phoneNum;
    private String authNum;


}
