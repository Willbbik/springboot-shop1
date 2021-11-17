package com.ecommerce.newshop1.exception;


import com.ecommerce.newshop1.utils.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@ControllerAdvice(basePackages = "com.ecommerce.newshop1.controller")
public class GlobalExceptionHandler{


    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleRuntimeException(CustomException e){
        log.error("Custom Exception {}",  e.getMessage());
        return ResponseEntity
                .status(ErrorCode.POSTS_NOT_FOUND.getStatus().value())
                .body(new ErrorResponse(e.getErrorCode()));
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleException(Exception e){
        log.error("Exception {}",  e.getMessage());
        log.error("Exception reason : {}", (Object) e.getStackTrace());
        return "error/400";
    }


    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> illegalArgumentException(IllegalArgumentException e){
        log.error("IllegalArgumentException {}",  e.getMessage());
        return ResponseEntity
                .status(ErrorCode.NO_EXISTS.getStatus().value())
                .body(new ErrorResponse(ErrorCode.NO_EXISTS));
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public String itemNotFound(ItemNotFoundException e){
        log.error("Exception {}", e.getMessage());
        return "error/404";
    }



}
