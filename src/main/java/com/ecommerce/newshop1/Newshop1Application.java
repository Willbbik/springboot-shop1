package com.ecommerce.newshop1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Newshop1Application {

	public static void main(String[] args) {

//		SpringApplication.run(Newshop1Application.class, args);
		new SpringApplicationBuilder(Newshop1Application.class)
				.properties("spring.config.location=classpath:/yml/coolsms.yml, classpath:/application.yml" +
						", classpath:/yml/kakao.yml")
				.run();
	}


}

