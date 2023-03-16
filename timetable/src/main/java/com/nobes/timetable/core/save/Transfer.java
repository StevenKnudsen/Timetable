package com.nobes.timetable.core.save;


import com.nobes.timetable.core.save.service.ImportService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;

@SpringBootApplication
@ComponentScan(basePackages = {"com.nobes.timetable.hierarchy.service", "com.nobes.timetable.hierarchy.domain", "com.nobes.timetable.core.save"})
@MapperScan({
        "com.nobes.timetable.dao",
        "com.nobes.timetable.*.dao",
        "com.nobes.timetable.*.*.dao",
        "com.nobes.timetable.*.*.*.dao"
})
public class Transfer implements CommandLineRunner {

    @Autowired
    ImportService importService;

    public static void main(String[] args) {
        SpringApplication.run(Transfer.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        importService.excelImport(new File("src/main/java/com/nobes/timetable/table2.xls"));

        importService.courseImport();

        importService.lecImport();

        importService.labImport();

        importService.semImport();


    }
}
