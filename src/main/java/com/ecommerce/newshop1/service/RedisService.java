package com.ecommerce.newshop1.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final Logger log = LoggerFactory.getLogger(RedisService.class);

    // 인증번호 저장
    public void setAuthNo(String phoneNum, int authNum) throws Exception {

        ValueOperations<String, Object> vop = redisTemplate.opsForValue();
        Duration time = Duration.ofMinutes(3);

        try {
            vop.set(phoneNum, authNum, time);
        } catch (Exception e) {
            log.info("RedisService exception : 28 line : " + e.getMessage());
        }
    }


    // 주문번호 생성후 중복확인하고 저장
    public String createOrderId(String nowDate, int totalPrice) throws Exception {

        ValueOperations<String, Object> vop = redisTemplate.opsForValue();
        Duration time = Duration.ofDays(3);

        String orderId = nowDate + randomNum();
        String result = (String) vop.get(orderId);

        if(result == null){
            vop.set(orderId, totalPrice, time);
            return orderId;
        }else{
            return createOrderId(nowDate, totalPrice);
        }
    }



    // 인증번호 가져오기
    public int getAuthNum(String phoneNum) throws Exception {
        ValueOperations<String, Object> vop = redisTemplate.opsForValue();
        Object result = vop.get(phoneNum);

        if(!(result instanceof Integer)){
            return 1;
        }else{
            return (int)result;
        }
    }

    // 인증번호 비교하기
    public int authNumCheck(String phoneNum, String givenAuthNum){

        ValueOperations<String, Object> vop = redisTemplate.opsForValue();

        Object getAuthResult = vop.get(phoneNum);
        int authNum = Integer.parseInt(givenAuthNum);

        if(getAuthResult != null){
            int authResult = (int)getAuthResult;
            if(authResult == authNum){
                return 1;
            } else {
                return 2;
            }
        }
        return 3;
    }

    // 랜덤 번호 생성
    public int randomNum(){
        int min_num = 100005000;
        int max_num = 900000500;

        int random_num = (int) (Math.random() * (max_num - min_num) + min_num);
        return random_num;
    }

}
