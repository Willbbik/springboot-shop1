package com.ecommerce.newshop1.exception;


import com.ecommerce.newshop1.utils.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException{

    private final ErrorCode errorCode;

}
