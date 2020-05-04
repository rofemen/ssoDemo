package com.rofe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan({"com.rofe.filter","com.rofe.listener"})
public class SsoCenterApplication {

	public static void main(String[] args) {
		SpringApplication.run(SsoCenterApplication.class, args);
	}

}
