package com.nobes.timetable.core.controller;

import com.nobes.timetable.core.save.service.ImportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;

@RestController
@RequestMapping("/nobes/timetable/hierarchy")
public class CoreController {

    @Resource
    ImportService importService;

    @GetMapping("/importExcel")
    public void importExcel() throws Exception {

//        importService.truncateOther();

        importService.truncate();

        importService.excelImport(new File("src/main/java/com/nobes/timetable/table6.xls"));

        importService.courseImport();

        importService.lecImport();

        importService.labImport();

        importService.semImport();

    }

}
