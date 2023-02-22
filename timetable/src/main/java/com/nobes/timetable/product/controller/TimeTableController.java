package com.nobes.timetable.product.controller;


import com.nobes.timetable.core.entity.ResultBody;
import com.nobes.timetable.product.dto.LectureDTO;
import com.nobes.timetable.product.dto.ProgDTO;
import com.nobes.timetable.product.logic.LectureService;
import com.nobes.timetable.product.logic.SequenceService;
import com.nobes.timetable.product.test.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/nobes/timetable/product")
public class TimeTableController {

    @Autowired
    SequenceService sequenceService;

    @Resource
    LectureService lectureService;

    @Autowired
    Test test;


    @PostMapping("/getCourseInfo")
    public ResultBody getCourseInfo(@RequestBody @Validated ProgDTO progDTO) throws Exception {

        ArrayList<String> courseList= sequenceService.calendarScrap(progDTO);
        return ResultBody.success(courseList);

    }

    @PostMapping("/getLectureInfo")
    public ResultBody getLectureInfo(@RequestBody @Validated LectureDTO lectureDTO) throws Exception {
        HashMap<String, HashMap<String, String>> courseInfo = lectureService.getCourseInfo(lectureDTO);
        return ResultBody.success(courseInfo);
    }

}
