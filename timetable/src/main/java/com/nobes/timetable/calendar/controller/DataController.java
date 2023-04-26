package com.nobes.timetable.calendar.controller;

import com.nobes.timetable.core.entity.ResultBody;
import com.nobes.timetable.calendar.dto.ProgDTO;
import com.nobes.timetable.calendar.dto.ProgramDTO;
import com.nobes.timetable.calendar.logic.PlanService;
import com.nobes.timetable.calendar.logic.SequenceService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *  This class represents a RESTful API controller for handling requests related to a timetable calendar.
 *
 *  It contains methods for retrieving plans in a term in a program, lecture information, lab information, and semester information.
 *
 *  The class uses the Spring framework's annotations for mapping HTTP requests to controller methods.
 *
 * */
@RestController
@RequestMapping("/nobes/timetable/calendar")
public class DataController {

    @Resource
    PlanService planService;

    @Resource
    SequenceService sequenceService;



    /**
     *  Retrieves a HashMap containing plans based on the given program name.
     *
     *  @param programDTO the program DTO containing necessary programName to retrieve plans
     *  @return a ResultBody (a public return type) containing the retrieved plan Map
     *  @throws Exception if an error occurs while retrieving the plans
     *
     * */
    @PostMapping("/getPlans")
    public ResultBody getPlans(@RequestBody @Validated ProgramDTO programDTO) throws Exception {
        HashMap<String, ArrayList> plan = planService.getPlan(programDTO);
        return ResultBody.success(plan);
    }


    /**
     *  Retrieves a HashMap containing lecture information
     *
     *  @param progDTO the progDTO containing selected program, plan, and term with a componentId indicated asking for lecture(1), lab(2), or seminar(3)
     *  @return a ResultBody (a public return type) containing the retrieved lecture Map
     *  @throws Exception if an error occurs
     *
     * */
    @PostMapping("/getLecsInfo")
    public ResultBody getLecsInfo(@RequestBody @Validated ProgDTO progDTO) throws Exception {
        return ResultBody.success(sequenceService.handle(progDTO));
    }

    /**
     *  Retrieves a HashMap containing lab information
     *
     *  @param progDTO the progDTO containing selected program, plan, and term with a componentId indicated asking for lecture(1), lab(2), or seminar(3)
     *  @return a ResultBody (a public return type) containing the retrieved lab Map
     *  @throws Exception if an error occurs
     *
     * */
    @PostMapping("/getLabsInfo")
    public ResultBody getLabsInfo(@RequestBody @Validated ProgDTO progDTO) throws Exception {
        return ResultBody.success(sequenceService.handle(progDTO));
    }

    /**
     *  Retrieves a HashMap containing seminar information
     *
     *  @param progDTO the progDTO containing selected program, plan, and term with a componentId indicated asking for lecture(1), lab(2), or seminar(3)
     *  @return a ResultBody (a public return type) containing the retrieved seminar Map
     *  @throws Exception if an error occurs
     *
     * */
    @PostMapping("/getSemsInfo")
    public ResultBody getSemsInfo(@RequestBody @Validated ProgDTO progDTO) throws Exception {
        return ResultBody.success(sequenceService.handle(progDTO));
    }

}
