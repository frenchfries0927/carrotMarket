package com.carrotMarket.carrotMarket;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.carrotMarket.carrotMarket.NewProject.board.mapper")

public class CarrotMarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarrotMarketApplication.class, args);


	}

}
