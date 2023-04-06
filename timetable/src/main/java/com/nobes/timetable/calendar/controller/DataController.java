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

@RestController
@RequestMapping("/nobes/timetable/calendar")
public class DataController {

    @Resource
    PlanService planService;

    @Resource
    SequenceService sequenceService;


    @PostMapping("/getPlans")
    public ResultBody getPlans(@RequestBody @Validated ProgramDTO programDTO) throws Exception {
        HashMap<String, ArrayList> plan = planService.getPlan(programDTO);
        return ResultBody.success(plan);
    }

    @PostMapping("/getLecsInfo")
    public ResultBody getLecsInfo(@RequestBody @Validated ProgDTO progDTO) throws Exception {
        return ResultBody.success(sequenceService.handle(progDTO));
    }

    @PostMapping("/getLabsInfo")
    public ResultBody getLabsInfo(@RequestBody @Validated ProgDTO progDTO) throws Exception {
        return ResultBody.success(sequenceService.handle(progDTO));
    }

    @PostMapping("/getSemsInfo")
    public ResultBody getSemsInfo(@RequestBody @Validated ProgDTO progDTO) throws Exception {
        return ResultBody.success(sequenceService.handle(progDTO));
    }

}
