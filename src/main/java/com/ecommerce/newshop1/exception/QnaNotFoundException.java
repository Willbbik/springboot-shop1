package com.ecommerce.newshop1.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class QnaNotFoundException extends RuntimeException{

    public QnaNotFoundException(String message) { super(message); }

}
