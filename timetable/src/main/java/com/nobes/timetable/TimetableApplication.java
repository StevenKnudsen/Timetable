package com.nobes.timetable;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Dictionary;


@SpringBootApplication
@MapperScan({
		"com.nobes.timetable.dao",
		"com.nobes.timetable.*.dao",
		"com.nobes.timetable.*.*.dao"
})
@EnableAsync
public class TimetableApplication {
	public static void main(String[] args) {
		SpringApplication.run(TimetableApplication.class, args);
	}

}
