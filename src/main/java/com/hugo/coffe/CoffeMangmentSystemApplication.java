package com.hugo.coffe;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
public class CoffeMangmentSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoffeMangmentSystemApplication.class, args);
	}
	@Bean
	ModelMapper modelMapper(){
		return new ModelMapper();
	}
}
