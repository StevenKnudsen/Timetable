package com.nobes.timetable.core.controller;

import com.nobes.timetable.core.dto.VisualizerImportDTO;
import com.nobes.timetable.core.save.ImportTimetableService;
import com.nobes.timetable.core.save.ImportVisualizerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
@Slf4j
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
    public void timetableImport(@RequestBody @Validated VisualizerImportDTO visualizerImportDTO) throws Exception {

        importTimetableService.truncate();

        importTimetableService.excelImport(new File(visualizerImportDTO.getFilePath()));

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
    @PostMapping("/sequenceImport")
    public void sequenceImport(@RequestBody @Validated VisualizerImportDTO visualizerImportDTO) throws Exception {

        importTimetableService.sequenceImport(new File(visualizerImportDTO.getFilePath()));
        log.info(visualizerImportDTO.getFilePath());

    }

    /**
     *  Import for visualizer project
     *  @throws Exception if an error occurs
     * */
    @PostMapping("/visualizerImport")
    public void visualizerImport(@RequestBody @Validated VisualizerImportDTO visualizerImportDTO) throws Exception {
        importVisualizerService.courseGroupImport(new File(visualizerImportDTO.getFilePath()));
    }

    /**
     *  Truncate tables for visualizer
     * */
    @GetMapping("/truncateVisualizer")
    public void truncateVisualizer() throws Exception {

        importVisualizerService.truncate();

    }

}
