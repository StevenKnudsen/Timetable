package com.nobes.timetable.calendar.controller;

import com.nobes.timetable.calendar.dto.*;
import com.nobes.timetable.calendar.logic.CourseService;
import com.nobes.timetable.calendar.logic.OptimizeService;
import com.nobes.timetable.calendar.logic.PlanService;
import com.nobes.timetable.calendar.logic.SequenceService;
import com.nobes.timetable.core.entity.ResultBody;
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

    @Resource
    CourseService courseService;

    @Resource
    OptimizeService optimizeService;


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

    /**
     *  Retrieves a HashMap containing seminar information
     *
     *  @param allCourseDTO the allCourseDTO containing selected program
     *  @return a ResultBody (a public return type) containing all the courses in this program
     *  @throws Exception if an error occurs
     *
     * */
    @PostMapping("/getAllCourses")
    public ResultBody getAllCourses(@RequestBody @Validated AllCourseDTO allCourseDTO) throws Exception {
        return ResultBody.success(courseService.getAllCourse(allCourseDTO));
    }

    /**
     *  Retrieves a HashMap containing seminar information
     *
     *  @param indivCourseDTO the indivCourseDTO containing course name and term
     *  @return a ResultBody (a public return type) containing the lecture information about this course in a specific term
     *  @throws Exception if an error occurs
     *
     * */
    @PostMapping("/getIndividualCourseLecsInfo")
    public ResultBody getIndividualCourseLecsInfo(@RequestBody @Validated IndivCourseDTO indivCourseDTO) throws Exception {
        return ResultBody.success(courseService.getIndivCourseLecsInfo(indivCourseDTO));
    }

    /**
     *  Retrieves a HashMap containing seminar information
     *
     *  @param indivCourseDTO the indivCourseDTO containing course name and term
     *  @return a ResultBody (a public return type) containing the lab information about this course in a specific term
     *  @throws Exception if an error occurs
     *
     * */
    @PostMapping("/getIndividualCourseLabsInfo")
    public ResultBody getIndividualCourseLabsInfo(@RequestBody @Validated IndivCourseDTO indivCourseDTO) throws Exception {
        return ResultBody.success(courseService.getIndivCourseLabsInfo(indivCourseDTO));
    }

    /**
     *  Retrieves a HashMap containing seminar information
     *
     *  @param indivCourseDTO the indivCourseDTO containing course name and term
     *  @return a ResultBody (a public return type) containing the seminar information about this course in a specific term
     *  @throws Exception if an error occurs
     *
     * */
    @PostMapping("/getIndividualCourseSemsInfo")
    public ResultBody getIndividualCourseSemsInfo(@RequestBody @Validated IndivCourseDTO indivCourseDTO) throws Exception {
        return ResultBody.success(courseService.getIndivCourseSemsInfo(indivCourseDTO));
    }

    /**
     *  Retrieves a 2D Array represeting the updated timetable
     *
     *  @param optimizedTableDTO the optimizedTableDTO
     *  @return a ResultBody (a public return type) containing the updated timetable
     *  @throws Exception if an error occurs
     *
     * */
    @PostMapping("/optimizeTimeTable")
    public ResultBody optimizeTimeTable(@RequestBody @Validated OptimizedTableDTO optimizedTableDTO) throws Exception {
        return optimizeService.optimizedGenerate(optimizedTableDTO);
    }
}
