package com.nobes.timetable;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
//@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.nobes.timetable.core.save.*"))
@MapperScan({
		"com.nobes.timetable.dao",
		"com.nobes.timetable.*.dao",
		"com.nobes.timetable.*.*.dao",
		"com.nobes.timetable.*.*.*.dao"
})
@EnableAsync
public class TimetableApplication {
	public static void main(String[] args) {
		SpringApplication.run(TimetableApplication.class, args);
	}

}
