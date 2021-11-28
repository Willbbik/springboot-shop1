package com.ecommerce.newshop1.controller;

import com.ecommerce.newshop1.dto.CallbackPayload;
import com.ecommerce.newshop1.utils.enums.TossPayments;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class OrderController {


    @GetMapping("/order/checkout")
    public String checkout(@RequestParam List<String> itemId, @RequestParam List<Long> quantity){





        return "order/order_checkout";
    }


//    @PostMapping("/order/payment/check")
//    public String paymentCheck(){
//
//
//
//    }

    @RequestMapping("/order/virtual-account/callback")
    @ResponseStatus(HttpStatus.OK)
    public void paymentCheck(@RequestBody CallbackPayload callbackPayload){

        if(callbackPayload.getStatus().equals(TossPayments.DONE.getValue())){
            // 입금 완료일 때 처리
        }else if(callbackPayload.getStatus().equals(TossPayments.CANCELED.getValue())){
            // 입금 취소일 때 처리
        }
    }


}
