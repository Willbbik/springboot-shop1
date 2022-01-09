package com.ecommerce.newshop1.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BoardCommentNotFoundException extends RuntimeException{

    public BoardCommentNotFoundException(String message) { super(message); }

}
