package com.ecommerce.newshop1.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BoardNotFoundException extends RuntimeException{

    public BoardNotFoundException (String message) { super(message); }

}
