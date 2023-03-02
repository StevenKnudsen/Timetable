package com.nobes.timetable;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@MapperScan({
		"com.nobes.timetable.dao",
		"com.nobes.timetable.*.dao",
		"com.nobes.timetable.*.*.dao"
})
@EnableAsync
@Configuration
public class TimetableApplication {
	public static void main(String[] args) {
		SpringApplication.run(TimetableApplication.class, args);
	}

}
