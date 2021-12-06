package com.ecommerce.newshop1.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ParameterNotFoundException extends RuntimeException{

    public ParameterNotFoundException(String message) { super(message); }

}
