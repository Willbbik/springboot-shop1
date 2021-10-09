package com.ecommerce.newshop1.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JoinMsg {

    USERID_NULL("아이디는 필수정보 입니다."),
    USERID_EXIST("이미 사용중이거나 탈퇴한 아이디입니다."),
    USERID_VALIDATION("5~20자의 영문 소문자, 숫자와 특수기호(_),(-)만 사용 가능합니다. (띄어쓰기 불가능)"),

    PSWD1_NULL("비밀번호는 필수정보 입니다."),
    PSWD1_VALIDATION("8~25자 영문 대 소문자, 숫자를 사용하세요. (특수문자 사용가능) (띄어쓰기 불가능)"),

    PSWD2_NULL("비밀번호 재확인은 필수정보 입니다."),
    PSWD2_DIFFERENCE("비밀번호가 일치하지 않습니다."),

    PHONENUM_NULL("전화번호는 필수정보 입니다."),
    PHONENUM_VALIDATION("형식에 맞지 않는 번호입니다."),

    AUTHNUM_NOSEND("인증이 필요합니다."),
    AUTHNUM_DIFFERENCE("인증번호를 다시 확인해주세요."),
    AUTHNUM_TIMEOUT("인증을 다시 진행해주세요.");


    private String value;

}
