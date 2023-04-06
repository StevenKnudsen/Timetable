package com.nobes.timetable.product.controller;


import com.nobes.timetable.core.entity.ResultBody;
import com.nobes.timetable.calendar.dto.ProgDTO;
import com.nobes.timetable.product.test.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/nobes/timetable/product")
public class TimeTableController {

    @Autowired
    Test test;


    @PostMapping("/getCourseInfo")
    public ResultBody getCourseInfo(@RequestBody @Validated ProgDTO progDTO) throws Exception {

//        ArrayList<String> courseList= sequenceService.calendarScrap(progDTO);
        return ResultBody.success(null);

    }


//    /*
//    * new controller
//    * */
//    @PostMapping("/getLecsInfo")
//    public ResultBody getLecsInfo(@RequestBody @Validated ProgDTO progDTO) throws Exception {
//        return ResultBody.success(lesService.getLecs(progDTO));
//    }
//
//    /*
//     * new controller
//     * */
//    @PostMapping("/getLabsInfo")
//    public ResultBody getLabsInfo(@RequestBody @Validated ProgDTO progDTO) throws Exception {
//        return ResultBody.success(lsService.getLabs(progDTO));
//    }
//
//
//    /*
//     * new controller
//     * */
//    @PostMapping("/getSemsInfo")
//    public ResultBody getSemsInfo(@RequestBody @Validated ProgDTO progDTO) throws Exception {
//        return ResultBody.success(sesService.getSems(progDTO));
//    }


//    @PostMapping("/getPaletteCourses")
//    public ResultBody getPaletteCourses(@RequestBody @Validated ProgDTO progDTO) throws Exception {
//        return ResultBody.success(courseService.getCourses(progDTO));
//    }
//
//
//    @PostMapping("/getPaletteLecs")
//    public ResultBody getPaletteLecs(@RequestBody @Validated CourseDTO courseDTO) throws Exception {
//        ArrayList<LectureVO> lectureVOS = lectureService.getLecture(courseDTO);
//        return ResultBody.success(lectureVOS);
//    }


//    @PostMapping("/getPaletteLabs")
//    public ResultBody getPaletteLabs(@RequestBody @Validated CourseDTO courseDTO) throws Exception {
//        ArrayList<LabVO> lab = labService.getLab(courseDTO);
//        return ResultBody.success(lab);
//    }

//
//    @PostMapping("/getPaletteSems")
//    public ResultBody getPaletteSems(@RequestBody @Validated CourseDTO courseDTO) throws Exception {
//        ArrayList<SemVO> sem = semService.getSem(courseDTO);
//        return ResultBody.success(sem);
//    }

//    @PostMapping("/getCourses")
//    public ResultBody getCourses(@RequestBody @Validated ProgDTO progDTO) throws Exception {
//        ArrayList courses = courseService.getCourses(progDTO);
//        return ResultBody.success(courses);
//    }


}
