package com.nobes.timetable.core.save;

import java.io.File;

import static com.nobes.timetable.core.save.service.ImportService.excelImport;

public class Transfer {

    public static void main(String[] args) throws Exception {

        excelImport(new File("src/main/java/com/nobes/timetable/table.xls"));

    }
}
