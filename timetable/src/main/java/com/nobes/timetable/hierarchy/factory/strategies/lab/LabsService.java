package com.nobes.timetable.hierarchy.factory.strategies.lab;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nobes.timetable.hierarchy.domain.NobesTimetableCourse;
import com.nobes.timetable.hierarchy.dto.CourseIdDTO;
import com.nobes.timetable.hierarchy.factory.strategies.UniComponentStrategy;
import com.nobes.timetable.hierarchy.service.INobesTimetableCourseService;
import com.nobes.timetable.hierarchy.vo.CourseVO;
import com.nobes.timetable.hierarchy.vo.LabVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component(value = "2")
@Slf4j
public class LabsService implements UniComponentStrategy {

    @Resource
    INobesTimetableCourseService courseSelectService;

    @Resource
    LabService labService;

    @Override
    public HashMap handle(ArrayList<String> names, String term) throws Exception {

        HashMap<String, ArrayList<LabVO>> labMap = new HashMap<>();

        for (int i = 0; i < names.size(); i++) {

            String courseName = names.get(i);

            if (courseName.equals("COMP")) {
                CourseVO courseVO = new CourseVO();
                courseVO.setSubject("COMP");
                courseVO.setCourseName("COMP");
                labMap.put(courseName,null);
            } else if (courseName.equals("ITS")) {
                CourseVO courseVO = new CourseVO();
                courseVO.setSubject("ITS");
                courseVO.setCourseName("ITS");
                labMap.put(courseName,null);
            } else if (courseName.equals("PROG")) {
                CourseVO courseVO = new CourseVO();
                courseVO.setSubject("PROG");
                courseVO.setCourseName("PROG");
                labMap.put(courseName, null);
            } else if (courseName.equals("PROG 1")) {
                CourseVO courseVO = new CourseVO();
                courseVO.setSubject("PROG 1");
                courseVO.setCourseName("PROG 1");
                labMap.put(courseName,null);
            } else if (courseName.equals("PROG 2")) {
                CourseVO courseVO = new CourseVO();
                courseVO.setSubject("PROG 2");
                courseVO.setCourseName("PROG 2");
                labMap.put(courseName,null);
            } else if (courseName.contains("WKEXP")) {
                labMap.put(courseName, null);
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

                    if (!course.getLab().equals("0") && !course.getLab().equals("UNASSIGNED")) {
                        String coursename = subject + " " + catalog;
                        CourseIdDTO courseIdDTO = new CourseIdDTO();
                        courseIdDTO.setCourseId(courseId);

                        ArrayList<LabVO> labs = labService.getLab(courseIdDTO);

                        labMap.put(coursename, labs);
                    }
                } else {
                    labMap.put(courseName, null);
                }

            }
        }

        return labMap;
    }
}
