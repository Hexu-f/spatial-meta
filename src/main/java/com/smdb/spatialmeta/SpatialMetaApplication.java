package com.smdb.spatialmeta;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@MapperScan("com.smdb.spatialmeta.mapper")
@ServletComponentScan
public class SpatialMetaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpatialMetaApplication.class, args);
	}

}
