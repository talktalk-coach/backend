package com.codit.talktalkcoach;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TalkTalkCoachApplication {

	public static void main(String[] args) {
		SpringApplication.run(TalkTalkCoachApplication.class, args);
	}

}
