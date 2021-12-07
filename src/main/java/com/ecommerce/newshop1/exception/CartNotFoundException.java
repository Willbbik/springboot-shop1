package com.ecommerce.newshop1.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CartNotFoundException extends RuntimeException{

    public CartNotFoundException(String message) { super(message);}

}
