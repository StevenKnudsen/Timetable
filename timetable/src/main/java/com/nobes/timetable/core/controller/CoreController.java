package com.nobes.timetable.core.controller;

import com.nobes.timetable.core.save.ImportTimetableService;
import com.nobes.timetable.core.save.ImportVisualizerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;

/**
 *  This class represents a RESTful API controller for handling requests related to a database import.
 *
 *  In order to import or truncate databases, send a get request to these api to do corresponding database manipulation
 *
 * */
@RestController
@RequestMapping("/nobes/timetable/core")
public class CoreController {

    @Resource
    ImportTimetableService importTimetableService;

    @Resource
    ImportVisualizerService importVisualizerService;


    /**
     *  Import for timetable project
     *  @throws Exception if an error occurs
     * */
    @GetMapping("/timetableImport")
    public void timetableImport() throws Exception {

        importTimetableService.truncate();

        importTimetableService.excelImport(new File("src/main/java/com/nobes/timetable/t8.xls"));

        importTimetableService.courseImport();

        importTimetableService.lecImport();

        importTimetableService.labImport();

        importTimetableService.semImport();

    }

    /**
     * truncate tables for the timetable project
     * */
    @GetMapping("/truncateTimetable")
    public void truncateTimetable() throws Exception {

        importTimetableService.truncateOther();

        importTimetableService.truncate();

    }

    /**
     * api for importing the sequence table into database
     * */
    @GetMapping("/sequenceImport")
    public void sequenceImport() throws Exception {

        importTimetableService.sequenceImport(new File("src/main/java/com/nobes/timetable/PESequencing.xls"));

    }

    /**
     *  Import for visualizer project
     *  @throws Exception if an error occurs
     * */
    @GetMapping("/visualizerImport")
    public void visualizerImport() throws Exception {

        importVisualizerService.courseImport(new File("src/main/java/com/nobes/timetable/Courses.xls"));

        importVisualizerService.courseGroupImport(new File("src/main/java/com/nobes/timetable/CourseCategories.xlsx"));
    }

    /**
     *  Truncate tables for visualizer
     * */
    @GetMapping("/truncateVisualizer")
    public void truncateVisualizer() throws Exception {

        importVisualizerService.truncate();

    }

}
