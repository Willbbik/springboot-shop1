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
        int minNum = 100005000;
        int maxNum = 912315500;

        int randomNum = (int) (Math.random() * (maxNum - minNum) + minNum);
        return randomNum;
    }

    public int randomAuthNum(){

        int minNum = 100000;
        int maxNum = 999999;

        int authNum = (int) (Math.random() * (maxNum - minNum) + minNum);
        return authNum;
    }

}
