package com.sk.group.ms.employee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan("com.sk.group.shared.*")
@ComponentScan("com.sk.group.*")
public class SkGroupMsEmployeeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkGroupMsEmployeeServiceApplication.class, args);
	}

}
