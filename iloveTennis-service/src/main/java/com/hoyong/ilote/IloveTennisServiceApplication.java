package com.hoyong.ilote;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession
@EnableJpaRepositories(basePackages={ "com.hoyong.ilote" })
@SpringBootApplication
public class IloveTennisServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IloveTennisServiceApplication.class, args);
	}

}
