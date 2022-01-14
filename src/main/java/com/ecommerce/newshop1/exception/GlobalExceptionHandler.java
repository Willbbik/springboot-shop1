package com.ecommerce.newshop1.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@ControllerAdvice(basePackages = "com.ecommerce.newshop1.controller")
public class GlobalExceptionHandler{

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleException(Exception e){
        log.error("Exception {}",  e.getMessage());
        log.error("Exception reason : {}", e.getCause());
        return "error/400";
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public String illegalArgumentException(IllegalArgumentException e){
        log.error("IllegalArgumentException {}",  e.getMessage());
        return "error/400";
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public String itemNotFound(ItemNotFoundException e){
        log.error("ItemNotFoundException {}", e.getMessage());
        log.error("ItemNotFoundException reason : {}", e.getCause());
        return "error/500";
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public String memberNotFound(MemberNotFoundException e){
        log.error("MemberNotFoundException {}", e.getMessage());
        log.error("MemberNotFoundException reason : {}", e.getCause());
        return "error/500";
    }

    @ExceptionHandler(ParameterNotFoundException.class)
    public String parameterNotFound(ParameterNotFoundException e){

        log.error("ParameterNotFoundException {}", e.getMessage());
        log.error("ParameterNotFoundException reason : {}", e.getCause());
        return "error/400";
    }

    @ExceptionHandler(QnANotFoundException.class)
    public String qnaNotFound(QnANotFoundException e){
        log.error("qnaNotFoundException {} :", e.getMessage());
        log.error("qnaNotFoundException reason {} :", e.getCause());
        return "error/400";
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public String orderNotFound(OrderNotFoundException e){
        log.error("orderNotFoundException {} : ", e.getMessage());
        log.error("orderNotFoundException reason {} :", e.getCause());
        return "error/400";
    }

    @ExceptionHandler(BoardNotFoundException.class)
    public String boardNotFoundException(BoardNotFoundException e){
        log.error("BoardNotFoundException {} : ", e.getMessage());
        log.error("BoardNotFoundException reason {} :", e.getCause());
        return "error/404";
    }

    @ExceptionHandler(BoardCommentNotFoundException.class)
    public String BoardCommentNotFound(BoardCommentNotFoundException e){
        log.error("BoardCommentNotFoundException {} : ", e.getMessage());
        log.error("BoardCommentNotFoundException reason {} :", e.getCause());
        return "error/boardCommentError";
    }

    @ExceptionHandler(NotLoginException.class)
    public String notLogin(NotLoginException e){

        log.error("NotLoginException {} ", e.getMessage());
        return "member/login";
    }

}
