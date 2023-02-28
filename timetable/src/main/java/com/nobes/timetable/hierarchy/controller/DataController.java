package com.nobes.timetable.hierarchy.controller;

import com.nobes.timetable.core.entity.ResultBody;
import com.nobes.timetable.hierarchy.dto.CourseDTO;
import com.nobes.timetable.hierarchy.dto.ProgDTO;
import com.nobes.timetable.hierarchy.logic.CourseService;
import com.nobes.timetable.hierarchy.logic.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/nobes/timetable/hierarchy")
public class DataController {

    @Autowired
    CourseService courseService;

    @Autowired
    SequenceService sequenceService;

    @PostMapping("/getCourseInfo")
    public ResultBody getCourseInfo(@RequestBody @Validated CourseDTO courseDTO) throws Exception {

        return ResultBody.success(courseService.getCourse(courseDTO));

    }

    @PostMapping("/getCourse")
    public ResultBody getCourse(@RequestBody @Validated ProgDTO progDTO) throws Exception {

        return ResultBody.success(sequenceService.calendarScrap(progDTO));
    }
}
