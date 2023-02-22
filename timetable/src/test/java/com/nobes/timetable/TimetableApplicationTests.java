package com.nobes.timetable;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TimetableApplicationTests {

	@Test
	void contextLoads() {
		String s = "Summer";
		System.out.println(s += 1);
	}

}
