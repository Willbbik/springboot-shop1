package com.ecommerce.newshop1.utils;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Service
public class CommonService {

    public String getErrorMessage(BindingResult errors){

        String message = "";
        for(FieldError error : errors.getFieldErrors()){
            message = error.getDefaultMessage();
            break;
        }
        return message;
    }

}
