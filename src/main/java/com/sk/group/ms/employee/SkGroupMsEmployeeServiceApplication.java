package com.sk.group.ms.employee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.*")
public class SkGroupMsEmployeeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkGroupMsEmployeeServiceApplication.class, args);
	}

}
