package com.nobes.timetable.calendar.logic;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nobes.timetable.calendar.domain.NobesTimetableCourse;
import com.nobes.timetable.calendar.domain.NobesTimetableSequence;
import com.nobes.timetable.calendar.dto.AllCourseDTO;
import com.nobes.timetable.calendar.dto.IndivCourseDTO;
import com.nobes.timetable.calendar.factory.strategies.lab.LabsService;
import com.nobes.timetable.calendar.factory.strategies.lecture.LecturesService;
import com.nobes.timetable.calendar.factory.strategies.seminar.SemsService;
import com.nobes.timetable.calendar.service.INobesTimetableCourseService;
import com.nobes.timetable.calendar.service.INobesTimetableSequenceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Service for querying information for a course
 */
@Service
public class CourseService {

    @Resource
    INobesTimetableSequenceService tableSequenceService;

    @Resource
    LecturesService indivLectureService;

    @Resource
    LabsService indivLabsService;

    @Resource
    SemsService indivSeminarService;

    public ArrayList<String> getAllCourse(AllCourseDTO allCourseDTO) throws Exception {
        String program = allCourseDTO.getProgram();

        List<String> coursesList = tableSequenceService.list(new LambdaQueryWrapper<NobesTimetableSequence>()
                        .eq(NobesTimetableSequence::getProgramName, program))
                .stream()
                .map(NobesTimetableSequence::getCourseName)
                .collect(Collectors.toList());

        ArrayList<String> intermediate = new ArrayList<>();
        for (int i = 0; i < coursesList.size(); i++) {
            String course = coursesList.get(i);

            if (course.contains("(")) {
                course = course.replaceAll("\\(.*?\\)", "");
            }

            if (course.contains("or")) {
                String[] courses = course.split("or");
                for (String orCaseCourse : courses) {
                    String trimOrCaseCourse = orCaseCourse.trim();
                    intermediate.add(trimOrCaseCourse);
                }
            } else {
                if (!(course.contains("ITS") || course.contains("PROG") || course.contains("COMP"))) {
                    intermediate.add(course);
                }
            }
        }

        TreeSet<String> courseTreeSet = new TreeSet<>(intermediate);

        return new ArrayList<>(courseTreeSet);
    }

    public HashMap getIndivCourseLecsInfo(IndivCourseDTO indivCourseDTO) throws Exception {
        String courseName = indivCourseDTO.getCourseName();
        String term = indivCourseDTO.getTerm();
        String season = term.split(" ")[0];

        ArrayList<String> courseList = new ArrayList<>();
        courseList.add(courseName);

        HashMap map = indivLectureService.handle(courseList, season);
        return map;
    }

    public HashMap getIndivCourseLabsInfo(IndivCourseDTO indivCourseDTO) throws Exception {
        String courseName = indivCourseDTO.getCourseName();
        String term = indivCourseDTO.getTerm();
        String season = term.split(" ")[0];

        ArrayList<String> courseList = new ArrayList<>();
        courseList.add(courseName);

        HashMap map = indivLabsService.handle(courseList, season);
        return map;
    }

    public HashMap getIndivCourseSemsInfo(IndivCourseDTO indivCourseDTO) throws Exception {
        String courseName = indivCourseDTO.getCourseName();
        String term = indivCourseDTO.getTerm();
        String season = term.split(" ")[0];

        ArrayList<String> courseList = new ArrayList<>();
        courseList.add(courseName);

        HashMap map = indivSeminarService.handle(courseList, season);
        return map;
    }

}
