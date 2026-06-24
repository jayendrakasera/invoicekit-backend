package com.invoicekit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class InvoicekitApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvoicekitApplication.class, args);
	}

}
