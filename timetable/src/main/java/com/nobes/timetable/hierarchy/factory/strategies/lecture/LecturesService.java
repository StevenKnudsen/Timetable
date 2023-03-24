package com.nobes.timetable.hierarchy.factory.strategies.lecture;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nobes.timetable.hierarchy.domain.NobesTimetableCourse;
import com.nobes.timetable.hierarchy.dto.CourseIdDTO;
import com.nobes.timetable.hierarchy.factory.strategies.UniComponentStrategy;
import com.nobes.timetable.hierarchy.service.INobesTimetableCourseService;
import com.nobes.timetable.hierarchy.vo.CourseVO;
import com.nobes.timetable.hierarchy.vo.LectureVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component(value = "1")
@Slf4j
public class LecturesService implements UniComponentStrategy {

    @Resource
    INobesTimetableCourseService courseSelectService;

    @Resource
    LecService lService;

    @Override
    public HashMap handle(ArrayList<String> names, String term) throws Exception {

        HashMap<String, ArrayList<LectureVO>> lecMap = new HashMap<>();

        for (int i = 0; i < names.size(); i++) {

            String courseName = names.get(i);

            if (courseName.equals("COMP")) {
                CourseVO courseVO = new CourseVO();
                courseVO.setSubject("COMP");
                courseVO.setCourseName("COMP");
                lecMap.put(courseName, null);
            } else if (courseName.equals("ITS")) {
                CourseVO courseVO = new CourseVO();
                courseVO.setSubject("ITS");
                courseVO.setCourseName("ITS");
                lecMap.put(courseName, null);
            } else if (courseName.equals("PROG")) {
                CourseVO courseVO = new CourseVO();
                courseVO.setSubject("PROG");
                courseVO.setCourseName("PROG");
                lecMap.put(courseName, null);
            } else if (courseName.equals("PROG 1")) {
                CourseVO courseVO = new CourseVO();
                courseVO.setSubject("PROG 1");
                courseVO.setCourseName("PROG 1");
                lecMap.put(courseName, null);
            } else if (courseName.equals("PROG 2")) {
                CourseVO courseVO = new CourseVO();
                courseVO.setSubject("PROG 2");
                courseVO.setCourseName("PROG 2");
                lecMap.put(courseName, null);
            } else if (courseName.contains("WKEXP")) {
                Pattern pattern = Pattern.compile("\\d+");
                Matcher matcher = pattern.matcher(courseName);
                matcher.find();
                String section = matcher.group(0);

                LectureVO lectureVO = new LectureVO();
                ArrayList<LectureVO> lecs = new ArrayList<>();
                lectureVO.setLecName(courseName);
                ArrayList<String> times = new ArrayList<>();
                times.add("MON_08:00-18:00");
                times.add("TUE_08:00-18:00");
                times.add("WED_08:00-18:00");
                times.add("THU_08:00-18:00");
                times.add("FRI_08:00-18:00");
                lectureVO.setTimes(times);
                lectureVO.setSection(section);

                lecs.add(lectureVO);
                lecMap.put(courseName, lecs);
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

                    if (!course.getLec().equals("0") && !course.getLec().equals("UNASSIGNED")) {
                        String coursename = subject + " " + catalog;
                        CourseIdDTO courseIdDTO = new CourseIdDTO();
                        courseIdDTO.setCourseId(courseId);

                        ArrayList<LectureVO> lectures = lService.getLecture(courseIdDTO);

                        if (!lectures.isEmpty()) {
                            lecMap.put(coursename, lectures);
                        }
                    }
                } else {
                    lecMap.put(courseName, null);
                }

            }
        }

        return lecMap;
    }
}

