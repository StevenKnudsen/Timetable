package com.nobes.timetable.hierarchy.logic;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nobes.timetable.core.entity.ResultBody;
import com.nobes.timetable.hierarchy.domain.NobesTimetableCourse;
import com.nobes.timetable.hierarchy.domain.NobesTimetableLecture;
import com.nobes.timetable.hierarchy.dto.CourseDTO;
import com.nobes.timetable.hierarchy.service.INobesTimetableCourseService;
import com.nobes.timetable.hierarchy.service.INobesTimetableLabService;
import com.nobes.timetable.hierarchy.service.INobesTimetableLectureService;
import com.nobes.timetable.hierarchy.vo.LabAndSem;
import com.nobes.timetable.hierarchy.vo.Seminar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class CourseService {

    @Resource
    INobesTimetableCourseService iNobesTimetableCourseService;

    @Resource
    INobesTimetableLectureService iNobesTimetableLectureService;

    @Resource
    INobesTimetableLabService iNobesTimetableLabService;


    @Autowired
    SeminarService seminarService;

    public ResultBody getCourse(CourseDTO courseDTO) {
        String courseName = courseDTO.getCourseName();

        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(courseName);
        matcher.find();
        String catalog = matcher.group(0);
        String subject = courseName.substring(0, courseName.indexOf(catalog.charAt(0)) - 1);

        NobesTimetableCourse course = iNobesTimetableCourseService.getOne(new LambdaQueryWrapper<NobesTimetableCourse>()
                .eq(NobesTimetableCourse::getCatalog, catalog)
                .eq(NobesTimetableCourse::getSubject, subject)
        );

        String approvedHrs = course.getApprovedHrs();

        Boolean hasLec = false;
        Boolean hasLab = false;
        Boolean hasSem = false;

        String zero = "0";

        if (approvedHrs.contains("-")) {
            String[] split = approvedHrs.split("-");
            if (!split[0].equals(zero)) {
                hasLec = true;
            }
            if (!split[1].equals(zero)) {
                hasSem = true;
            }
            if (split.length > 2) {
                if (!split[2].equals(zero)) {
                    hasLab = true;
                }
            }
        } else {
            hasLec = true;
        }

        Integer courseId = course.getCourseId();

//        if (hasLec) {
        HashMap<String, HashMap<String, HashMap<String, ArrayList<LabAndSem>>>> map = new HashMap<>();
        HashMap<String, HashMap<String, ArrayList<LabAndSem>>> courseMap = new HashMap<>();
        HashMap<String, ArrayList<LabAndSem>> lectureMap = new HashMap<>();

        List<NobesTimetableLecture> sectionList = iNobesTimetableLectureService.list(new LambdaQueryWrapper<NobesTimetableLecture>()
                .eq(NobesTimetableLecture::getCourseId, courseId));


        if (hasSem) {
            for (NobesTimetableLecture lecture : sectionList) {
                String sect = lecture.getSect();
                log.info(sect);
                StringBuilder stringBuilder = new StringBuilder();
                String key = subject + " " + catalog + " " + sect + " Sem";

                log.info(key);

                ArrayList<LabAndSem> seminar = seminarService.getSeminar(lecture);

                lectureMap.put(key, seminar);

                String key1 = subject + " " + catalog + " " + sect;
                log.info(key1);
                courseMap.put(key1, lectureMap);

            }

            map.put(courseName, courseMap);
        } else {
            map.put(courseName, null);
        }
//        }


        return ResultBody.success(map);

    }
}
