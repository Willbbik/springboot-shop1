package com.ecommerce.newshop1;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableJpaAuditing
@SpringBootApplication
public class Newshop1Application {

	private static final String YMLS = "spring.config.location=" +
			"classpath:/application.yml," +
			"classpath:/yml/coolsms.yml," +
			"classpath:/yml/tosspayments.yml," +
			"classpath:/yml/oauth2.yml," +
			"classpath:/yml/aws.yml," +
			"classpath:/yml/kakao.yml"
			;

	public static void main(String[] args) {

		new SpringApplicationBuilder(Newshop1Application.class)
				.properties(YMLS)
				.run(args);
	}


}

