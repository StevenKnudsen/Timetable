package com.nobes.timetable.calendar.factory.strategies.seminar;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nobes.timetable.calendar.domain.NobesTimetableCourse;
import com.nobes.timetable.calendar.dto.CourseIdDTO;
import com.nobes.timetable.calendar.factory.strategies.UniComponentStrategy;
import com.nobes.timetable.calendar.service.INobesTimetableCourseService;
import com.nobes.timetable.calendar.vo.CourseVO;
import com.nobes.timetable.calendar.vo.SemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This class represents a concrete implementation of the UniComponentStrategy interface for handling seminar information.
 * The seminar information is retrieved based on the course names, and the returned HashMap
 * contains the course name as key and an ArrayList of SeminarVO objects as value.
 * */
@Component(value = "3")
@Slf4j
public class SemsService implements UniComponentStrategy {

    @Resource
    INobesTimetableCourseService courseSelectService;

    @Resource
    SemService semService;


    /**
     * rewrite the handle function in public interface to retrieve seminar information for the given course names and term
     * @param names an ArrayList of course names
     * @param term a string representing the selected term
     * @return a HashMap containing all the detailed seminar information for the given course names
     * @throws Exception if an error occurs while retrieving the seminar information
     * */
    @Override
    public HashMap handle(ArrayList<String> names, String term) throws Exception {
        HashMap<String, ArrayList<SemVO>> semMap = new HashMap<>();

        for (int i = 0; i < names.size(); i++) {

            String courseName = names.get(i);

            if (courseName.equals("COMP")) {
                CourseVO courseVO = new CourseVO();
                courseVO.setSubject("COMP");
                courseVO.setCourseName("COMP");
                semMap.put(courseName, null);
            } else if (courseName.equals("ITS")) {
                CourseVO courseVO = new CourseVO();
                courseVO.setSubject("ITS");
                courseVO.setCourseName("ITS");
                semMap.put(courseName, null);
            } else if (courseName.equals("PROG")) {
                CourseVO courseVO = new CourseVO();
                courseVO.setSubject("PROG");
                courseVO.setCourseName("PROG");
                semMap.put(courseName, null);
            } else if (courseName.contains("WKEXP")) {
                semMap.put(courseName, null);
            } else {
                Pattern pattern = Pattern.compile("\\d+");
                Matcher matcher = pattern.matcher(courseName);
                matcher.find();
                String catalog = matcher.group(0);
                String subject = courseName.substring(0, courseName.indexOf(catalog.charAt(0)) - 1);

                NobesTimetableCourse course = courseSelectService.getOne(new LambdaQueryWrapper<NobesTimetableCourse>()
                        .eq(NobesTimetableCourse::getCatalog, catalog)
                        .eq(NobesTimetableCourse::getSubject, subject)
                        .eq(NobesTimetableCourse::getAppliedTerm, term), false);

                if (course != null) {
                    Integer courseId = course.getCourseId();

                    if (!course.getSem().equals("0") && !course.getSem().equals("UNASSIGNED")) {
                        String coursename = subject + " " + catalog;
                        CourseIdDTO courseIdDTO = new CourseIdDTO();
                        courseIdDTO.setCourseId(courseId);

                        ArrayList<SemVO> sems = semService.getSem(courseIdDTO);

                        if (!sems.isEmpty() && !(sems == null)) {
                            semMap.put(coursename, sems);
                        }
                    }
                } else {
                    semMap.put(courseName, null);
                }

            }
        }

        return semMap;
    }
}
