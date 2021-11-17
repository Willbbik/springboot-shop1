package com.ecommerce.newshop1;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Newshop1Application.class)
class Newshop1ApplicationTests {

	@Test
	void contextLoads() {
	}

}
