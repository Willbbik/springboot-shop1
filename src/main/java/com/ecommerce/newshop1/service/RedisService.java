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

        String check = phoneNum+"_check";

        try {
            vop.set(phoneNum, authNum, time);
            vop.set(check, false, time);
        } catch (Exception e) {
            log.info("RedisService exception : setAuthNo method error : " + e.getMessage());
            throw new Exception("인증번호 저장 로직에서 에러 발생");
        }
    }


    // 인증번호 가져오기
    public int getAuthNum(String phoneNum){
        ValueOperations<String, Object> vop = redisTemplate.opsForValue();
        Object result = vop.get(phoneNum);

        if(!(result instanceof Integer)){
            return 1;
        }else{
            return (int)result;
        }
    }

    public void deleteKey(String key){

        redisTemplate.delete(key);
    }

    // 인증번호 검사후 확인했다고 설정
    public void setAuthCheck(String phoneNum) throws Exception{

        ValueOperations<String, Object> vop = redisTemplate.opsForValue();
        Duration time = Duration.ofHours(12);
        String check = phoneNum+"_check";

        try{
            vop.set(check, true, time);
        }catch (Exception e){
            log.error("redisService, setAuthCheck 메소드 에러");
            throw new Exception("redisService, setAuthCheck 메소드 에러");
        }
    }

    // 인증번호 검사 했는지 확인 메소드
    public boolean confirmPhoneCheck(String phoneNum){

        ValueOperations<String, Object> vop = redisTemplate.opsForValue();
        Object result = vop.get(phoneNum+"_check");

        if(result instanceof Boolean){
            return (Boolean) result;
        }else{
            return false;
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
