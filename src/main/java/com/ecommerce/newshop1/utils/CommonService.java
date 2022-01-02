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

    // 랜덤 번호 생성
    public int randomNum(){
        int min_num = 100005000;
        int max_num = 912315500;

        int random_num = (int) (Math.random() * (max_num - min_num) + min_num);
        return random_num;
    }

}
