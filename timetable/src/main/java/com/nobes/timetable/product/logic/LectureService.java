package com.nobes.timetable.product.logic;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nobes.timetable.product.domain.CourseInfo;
import com.nobes.timetable.product.dto.LectureDTO;
import com.nobes.timetable.product.service.ICourseInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class LectureService {

    @Resource
    ICourseInfoService iCourseInfoService;

    public HashMap<String, HashMap<String, String>> getCourseInfo(LectureDTO lectureDTO) {

        String course = lectureDTO.getCourseName();

        HashMap<String, HashMap<String, String>> courseInfoMap = new HashMap<>();

        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(course);
        matcher.find();
        String catalog = matcher.group(0);
        String subject = course.substring(0, course.indexOf(catalog.charAt(0)) - 1);

        List<CourseInfo> Lecs = iCourseInfoService.list(new LambdaQueryWrapper<CourseInfo>()
                .eq(CourseInfo::getCatalog, catalog)
                .eq(CourseInfo::getSubject, subject)
                .eq(CourseInfo::getComponent, "LEC"));


        for (CourseInfo courseInfo : Lecs) {

            HashMap<String, String> infoMap = new HashMap<>();

            String lec_subject = courseInfo.getSubject();
            String lec_catalog = courseInfo.getCatalog();
            lec_subject += " ";
            String course_name = lec_subject.concat(lec_catalog);
            course_name += " ";

            String component = courseInfo.getComponent();
            String lec_sect = courseInfo.getSect();
            component += " ";
            String course_sect = component.concat(lec_sect);

            String lec_name = course_name.concat(course_sect);

            infoMap.put("LectureSect", course_sect);

            String startDate = courseInfo.getStartDate();
            String endDate = courseInfo.getEndDate();
            startDate += " - ";
            startDate += endDate;

            infoMap.put("Duration", startDate);

            String startTime = courseInfo.getHrsFrom();
            String endTime = courseInfo.getHrsTo();
            startTime += " - ";
            startTime += endTime;
            infoMap.put("Time", startTime);

            String mon = courseInfo.getMon();
            String tues = courseInfo.getTues();
            String wed = courseInfo.getWed();
            String thurs = courseInfo.getThurs();
            String fri = courseInfo.getFri();
            StringBuilder duration = new StringBuilder();

            HashMap<String, String> weekdays = new HashMap<>();
            weekdays.put("Monday", mon);
            weekdays.put("Tuesday", tues);
            weekdays.put("Wednesday", wed);
            weekdays.put("Thursday", thurs);
            weekdays.put("Friday", fri);

            weekdays.forEach((key, value) -> {
                if (value.equals("Y")) {
                    duration.append(key).append(",");
                }
            });

            duration.deleteCharAt(duration.length() - 1);

            infoMap.put("LectureDate", String.valueOf(duration));

            String name = courseInfo.getName();
            infoMap.put("ProfessorName", name);

            String campus = courseInfo.getCampus();
            infoMap.put("OnCampus", campus);

            courseInfoMap.put(lec_name, infoMap);
        }

        return courseInfoMap;
    }
}
