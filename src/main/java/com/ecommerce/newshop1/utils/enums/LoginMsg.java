package com.ecommerce.newshop1.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  LoginMsg {

    USERID_NULL("아이디를 입력해 주세요."),
    PASSWORD_NULL("비밀번호를 입력해 주세요."),
    LOGIN_FAILURE("아이디와 비밀번호를 정확히 입력해 주세요.");

    private String value;
}
