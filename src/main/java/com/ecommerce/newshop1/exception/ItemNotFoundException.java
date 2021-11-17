package com.ecommerce.newshop1.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ItemNotFoundException extends RuntimeException{

    public ItemNotFoundException(String message) {
        super(message);
    }

    public ItemNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
