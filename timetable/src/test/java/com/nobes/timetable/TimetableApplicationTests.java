package com.nobes.timetable;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TimetableApplicationTests {

    @Test
    public void test() {
        String name = "MCTR_V5 (Koch & Lynch)";
        String modifiedName = name.substring(0, name.indexOf("(") - 1)+ name.substring(name.indexOf(")") + 1);
        System.out.println(modifiedName);
    }

}
