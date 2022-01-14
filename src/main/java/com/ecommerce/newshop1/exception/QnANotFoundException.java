package com.ecommerce.newshop1.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class QnANotFoundException extends RuntimeException{

    public QnANotFoundException(String message) { super(message); }

}
