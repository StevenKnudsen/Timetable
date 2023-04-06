package com.nobes.timetable.visualizer.controller;


import com.nobes.timetable.calendar.dto.ProgramDTO;
import com.nobes.timetable.core.entity.ResultBody;
import com.nobes.timetable.visualizer.dto.VisualDTO;
import com.nobes.timetable.visualizer.logic.VisualService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/nobes/timetable/visualizer")
public class VisualController {

    @Resource
    VisualService visualService;


    @PostMapping("/getInfo")
    public ResultBody getInfo(@RequestBody @Validated VisualDTO visualDTO) throws Exception {
        return ResultBody.success(visualService.getCourses(visualDTO));
    }
}
