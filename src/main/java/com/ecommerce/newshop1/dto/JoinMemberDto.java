package com.ecommerce.newshop1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinMemberDto {

    private Long id;

    @NotNull
    private String userId;

    @NotNull
    private String password;

    @NotNull
    private String pswdCheck;

    @NotNull
    private String phoneNum;

    @NotNull
    private String authNum;


}
