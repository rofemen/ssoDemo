package com.rofe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan(basePackages = "com.rofe.filter")
public class SsoSysSecondApplication {
	public static void main(String[] args) {
		SpringApplication.run(SsoSysSecondApplication.class, args)	;
	}

}
