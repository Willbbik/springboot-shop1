package com.ecommerce.newshop1.redis;




import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class redis {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    void testString() {

        // given
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String key = "test2";

        // when
        valueOperations.set(key, "hello");

        //then
        String value = valueOperations.get(key);
        assertThat(value).isEqualTo("hello");
    }

}




