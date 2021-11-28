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


	public static void main(String[] args) {

//		SpringApplication.run(Newshop1Application.class, args);
		new SpringApplicationBuilder(Newshop1Application.class)
				.properties("spring.config.location=classpath:/yml/coolsms.yml, classpath:/application.yml" +
						", classpath:/yml/oauth2.yml")
				.run();
	}


}

