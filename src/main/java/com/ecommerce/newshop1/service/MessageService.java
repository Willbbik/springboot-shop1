package com.ecommerce.newshop1.service;

import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.regex.Pattern;

@Service
public class MessageService {

    private static final Logger log = LoggerFactory.getLogger(MessageService.class);

    @Value("${coolsms.apiKey}")
    private String apiKey;

    @Value("${coolsms.apiSecret}")
    private String apiSecret;

    @Value("${coolsms.fromNumber")
    private String fromNumber;

    public void sendMessage(String toNumber, int randomNumber){

        Message coolsms = new Message(apiKey, apiSecret);

        HashMap<String, String> params = new HashMap<String, String>();

        params.put("to", toNumber);
        params.put("from", fromNumber);
        params.put("type", "SMS");
        params.put("text", "[에게모니] 인증번호 "+randomNumber+" 를 입력하세요." );
        params.put("app_version", "test app 1.2");

        try{
            JSONObject obj = (JSONObject) coolsms.send(params);
        }catch(CoolsmsException e){
            log.info("MessageService 40 line : send failed and " + e.getMessage());
        }
    }

    // 랜덤 번호 생성
    public int randomNum(){
        int min_num = 100000;
        int max_num = 999999;

        int random_num = (int) (Math.random() * (max_num - min_num) + min_num);
        return random_num;
    }

    // 전화번호 검사
    public boolean phoneValidationCheck(String phoneNum){
        String phonePattern = "^(010[1-9][0-9]{7})$";
        return Pattern.matches(phonePattern, phoneNum);
    }


}
