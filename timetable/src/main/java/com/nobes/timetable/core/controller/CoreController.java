package com.nobes.timetable.core.controller;

import com.nobes.timetable.core.entity.ResultBody;
import com.nobes.timetable.core.save.ImportTimetableService;
import com.nobes.timetable.core.save.ImportVisualizerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

/**
 * This class represents a RESTful API controller for handling requests related to a database import.
 * <p>
 * In order to import or truncate databases, send a get request to these api to do corresponding database manipulation
 */
@RestController
@RequestMapping("/nobes/timetable/core")
@Slf4j
public class CoreController {

    @Resource
    ImportTimetableService importTimetableService;

    @Resource
    ImportVisualizerService importVisualizerService;


    /**
     * Import for timetable project
     *
     * @throws Exception if an error occurs
     */
    @PostMapping("/timeTableImport")
    public ResultBody timeTableImport(@RequestPart("file") List<MultipartFile> files) throws Exception {

        try {
            for (MultipartFile file : files) {
                File convFile = File.createTempFile("temp", ".xls");
                // transfer the multipart file to the temp file
                file.transferTo(convFile);

                importTimetableService.truncate();

                importTimetableService.excelImport(convFile);

                Boolean isDuplicate = importTimetableService.courseImport();

                if (!isDuplicate) {
                    importTimetableService.lecImport();
                    importTimetableService.labImport();
                    importTimetableService.semImport();
                }
            }

            return ResultBody.success("Scheduler Courses upload succeed");

        } catch (Exception e) {
            e.printStackTrace();
            return ResultBody.success("Can not find the correct file information");
        }
    }

    /**
     * api for importing the sequence table into database
     */
    @PostMapping("/sequenceImport")
    public ResultBody sequenceImport(@RequestPart("file") List<MultipartFile> files, @RequestPart("program") String program) throws Exception {
        try {
            MultipartFile file = files.get(0);
            File convFile = File.createTempFile("temp", ".xls");
            // transfer the multipart file to the temp file
            file.transferTo(convFile);

            String message = importTimetableService.sequenceImport(convFile, program);
            return ResultBody.success(message);
        } catch (Exception e) {
            MultipartFile file = files.get(0);
            return ResultBody.success("Can not find " + file.getOriginalFilename());
        }
    }

    /**
     * Import for visualizer project
     *
     * @throws Exception if an error occurs
     */
    @PostMapping("/visualizerCourseImport")
    public ResultBody visualizerCourseImport(@RequestPart("file") List<MultipartFile> files) throws Exception {
        try {
            for (MultipartFile file : files) {
                File convFile = File.createTempFile("temp", ".xls");
                // transfer the multipart file to the temp file
                file.transferTo(convFile);
                importVisualizerService.courseImport(convFile);
            }
            return ResultBody.success("Visualizer Courses upload succeed");
        } catch (Exception e) {
            return ResultBody.success("Can not find the correct file information");
        }
    }


    /**
     * Import for visualizer project
     *
     * @throws Exception if an error occurs
     */
    @PostMapping("/visualizerGroupImport")
    public ResultBody visualizerGroupImport(@RequestPart("file") List<MultipartFile> files) throws Exception {
        try {
            for (MultipartFile file : files) {
                File convFile = File.createTempFile("temp", ".xls");
                // transfer the multipart file to the temp file
                file.transferTo(convFile);
                importVisualizerService.courseGroupImport(convFile);
            }
            return ResultBody.success("Visualizer Course Group upload succeed");
        } catch (Exception e) {
            return ResultBody.success("Can not find the correct file information");
        }
    }

    @PostMapping("/auImport")
    public ResultBody auImport(@RequestPart("file") List<MultipartFile> files) throws Exception {
        MultipartFile file = files.get(0);
        String message = importTimetableService.auImport(file);
        return ResultBody.success(message);
    }

    /**
     * truncate tables for the timetable course info
     */
    @GetMapping("/truncateTimetable")
    public void truncateTimetable() throws Exception {

        importTimetableService.truncateOther();

        importTimetableService.truncate();

    }

    /**
     * truncate tables for the timetable course info
     */
    @GetMapping("/truncateAU")
    public void truncateAU() throws Exception {
        importTimetableService.truncateAU();
    }

    /**
     * truncate tables for the timetable course info
     */
    @GetMapping("/truncateSequence")
    public void truncateSequence() throws Exception {
        importTimetableService.truncateSequence();
    }


    /**
     * Truncate tables for visualizer
     */
    @GetMapping("/truncateVisualizer")
    public void truncateVisualizer() throws Exception {
        importVisualizerService.truncate();
    }

    /**
     * Truncate tables for visualizer
     */
    @GetMapping("/truncateCourseGroup")
    public void truncateCourseGroup() throws Exception {
        importVisualizerService.truncateCourseGroup();
    }

}
