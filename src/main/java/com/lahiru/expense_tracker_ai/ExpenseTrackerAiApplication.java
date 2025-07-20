package com.lahiru.expense_tracker_ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ExpenseTrackerAiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpenseTrackerAiApplication.class, args);
	}

}
