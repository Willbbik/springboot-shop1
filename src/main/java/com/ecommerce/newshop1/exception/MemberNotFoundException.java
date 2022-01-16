package com.ecommerce.newshop1.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MemberNotFoundException extends RuntimeException{

    public MemberNotFoundException (String message) { super(message); }


}
