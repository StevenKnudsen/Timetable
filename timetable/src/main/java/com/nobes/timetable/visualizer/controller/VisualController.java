package com.nobes.timetable.visualizer.controller;


import com.nobes.timetable.core.entity.ResultBody;
import com.nobes.timetable.visualizer.dto.VisualDTO;
import com.nobes.timetable.visualizer.logic.ReqMapService;
import com.nobes.timetable.visualizer.logic.VisualService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *  This class represents a RESTful API controller for handling requests related to the visualizer.
 *
 *  It contains methods for retrieving plans in a term in a program, lecture information, lab information, and semester information.
 *
 *  The class uses the Spring framework's annotations for mapping HTTP requests to controller methods.
 *
 * */
@RestController
@RequestMapping("/nobes/timetable/visualizer")
public class VisualController {

    @Resource
    VisualService visualService;

    @Resource
    ReqMapService reqMapService;

    /**
     *  Send a post request to this api to retrieve a HashMap which key are terms and values are all the courses information
     *  in that term
     *
     *  @param visualDTO the visualDTO containing selected programName and planName
     *  @return a ResultBody (a public return type) containing the retrieved plan Map
     *  @throws Exception if an error occurs while retrieving the plans
     *
     * */
    @PostMapping("/getInfo")
    public ResultBody getInfo(@RequestBody @Validated VisualDTO visualDTO) throws Exception {
        return ResultBody.success(visualService.getCourses(visualDTO));
    }

    /**
     *  Send a post request to this api to retrieve a HashMap which contains requisites information
     *  for given program and plan
     *
     *  @param visualDTO the visualDTO containing selected programName and planName
     *  @return a ResultBody (a public return type) containing the retrieved Req Map
     *  @throws Exception if an error occurs while retrieving the requisites
     *
     * */
    @PostMapping("/getReqMap")
    public ResultBody getReqMap(@RequestBody @Validated VisualDTO visualDTO) throws Exception {
        return ResultBody.success(reqMapService.getCourseReqMap(visualDTO));
    }
}
