package com.nobes.timetable.hierarchy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nobes.timetable.core.entity.ResultBody;
import com.nobes.timetable.hierarchy.domain.NobesTimetableTable;
import com.nobes.timetable.hierarchy.dto.CourseDTO;
import com.nobes.timetable.hierarchy.dto.ProgDTO;
import com.nobes.timetable.hierarchy.logic.CourseService;
import com.nobes.timetable.hierarchy.logic.SequenceService;
import com.nobes.timetable.hierarchy.service.INobesTimetableTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/nobes/timetable/hierarchy")
public class DataController {

    @Autowired
    CourseService courseService;

    @Autowired
    SequenceService sequenceService;

    @Resource
    INobesTimetableTableService service;

    @PostMapping("/getCourseInfo")
    public ResultBody getCourseInfo(@RequestBody @Validated CourseDTO courseDTO) throws Exception {

        return ResultBody.success(courseService.getCourse(courseDTO));

    }


    @PostMapping("/getCourse")
    public ResultBody getCourse(@RequestBody @Validated ProgDTO progDTO) throws Exception {

        return ResultBody.success(sequenceService.calendarScrap(progDTO));
    }



    /*
    * test controller
    * */
    @GetMapping("/test")
    public ResultBody test() throws Exception {
        return ResultBody.success(service.getOne(new LambdaQueryWrapper<NobesTimetableTable>()
                .eq(NobesTimetableTable::getSubject,"WKEXP")
                .eq(NobesTimetableTable::getCatalog, "906")));
    }
}
