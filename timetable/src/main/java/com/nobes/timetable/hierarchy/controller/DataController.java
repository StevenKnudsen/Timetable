package com.nobes.timetable.hierarchy.controller;

import com.nobes.timetable.core.entity.ResultBody;
import com.nobes.timetable.hierarchy.dto.CourseDTO;
import com.nobes.timetable.hierarchy.dto.ProgDTO;
import com.nobes.timetable.hierarchy.dto.ProgramDTO;
import com.nobes.timetable.hierarchy.logic.*;
import com.nobes.timetable.hierarchy.vo.LabVO;
import com.nobes.timetable.hierarchy.vo.LectureVO;
import com.nobes.timetable.hierarchy.vo.SemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/nobes/timetable/hierarchy")
public class DataController {

    @Autowired
    MainService mainService;

    @Autowired
    CourseService courseService;

    @Resource
    LectureService lectureService;

    @Resource
    LabService labService;

    @Resource
    SemService semService;

    @Resource
    PlanService planService;


    @PostMapping("/getPlans")
    public ResultBody getPlans(@RequestBody @Validated ProgramDTO programDTO) throws Exception {
        HashMap<String, ArrayList> plan = planService.getPlan(programDTO);
        return ResultBody.success(plan);
    }

    //TODO: there is a redundant class in /dto called TermDTO, delete it if not using it

    @PostMapping("/getCourses")
    public ResultBody getCourses(@RequestBody @Validated ProgDTO progDTO) throws Exception {
        ArrayList courses = courseService.getCourses(progDTO);
        return ResultBody.success(courses);
    }


    @PostMapping("/getpaletteCourses")
    public ResultBody getpaletteCourses(@RequestBody @Validated ProgDTO progDTO) throws Exception {
        return ResultBody.success(courseService.getCourses(progDTO));
    }


    @PostMapping("/getpaletteLecs")
    public ResultBody getpaletteLecs(@RequestBody @Validated CourseDTO courseDTO) throws Exception {
        ArrayList<LectureVO> lectureVOS = lectureService.getLecture(courseDTO);
        return ResultBody.success(lectureVOS);
    }


    @PostMapping("/getpaletteLabs")
    public ResultBody getpaletteLabs(@RequestBody @Validated CourseDTO courseDTO) throws Exception {
        ArrayList<LabVO> lab = labService.getLab(courseDTO);
        return ResultBody.success(lab);
    }


    @PostMapping("/getpaletteSems")
    public ResultBody getpaletteSems(@RequestBody @Validated CourseDTO courseDTO) throws Exception {
        ArrayList<SemVO> sem = semService.getSem(courseDTO);
        return ResultBody.success(sem);
    }

}
