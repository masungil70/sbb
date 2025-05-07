package org.kosa.sbb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * 정적문서 : html, css, js, image
 * 동적문서 : jsp, php ... (backend 프로그램, servlet, Controller method) 
 * */
@SpringBootApplication
public class SbbApplication {

	public static void main(String[] args) {
		SpringApplication.run(SbbApplication.class, args);
	}

}
