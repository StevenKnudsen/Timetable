package com.nobes.timetable.hierarchy.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nobes.timetable.core.entity.ResultBody;
import com.nobes.timetable.hierarchy.domain.NobesTimetableCourse;
import com.nobes.timetable.hierarchy.dto.CourseDTO;
import com.nobes.timetable.hierarchy.logic.CourseService;
import com.nobes.timetable.hierarchy.service.INobesTimetableCourseService;
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

    @PostMapping("/getCourse")
    public ResultBody getCourse(@RequestBody @Validated CourseDTO courseDTO) throws Exception {

        return courseService.getCourse(courseDTO);

    }
}
