package com.ecommerce.newshop1.service;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

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
    public int authNumCheck(String phoneNum, String dtoAuthNum){

        ValueOperations<String, Object> vop = redisTemplate.opsForValue();

        Object realAuthNum = vop.get(phoneNum);
        int authNum = Integer.parseInt(dtoAuthNum); // 사용자가 입력한 인증번호

        if(realAuthNum != null){                    // 인증번호가 존재한다면
            int authResult = (int)realAuthNum;
            if(authResult == authNum){
                return 1;           // 인증번호가 같다면 1
            } else {
                return 2;           // 다르다면 2
            }
        }
        return 3;                   // 존재하지 않는다면 3
    }


}
