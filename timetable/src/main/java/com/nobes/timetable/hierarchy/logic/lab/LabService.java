package com.nobes.timetable.hierarchy.logic.lab;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nobes.timetable.core.utils.OrikaUtils;
import com.nobes.timetable.hierarchy.domain.NobesTimetableCourse;
import com.nobes.timetable.hierarchy.domain.NobesTimetableLab;
import com.nobes.timetable.hierarchy.dto.CourseDTO;
import com.nobes.timetable.hierarchy.logic.MainService;
import com.nobes.timetable.hierarchy.service.INobesTimetableCourseService;
import com.nobes.timetable.hierarchy.service.INobesTimetableLabService;
import com.nobes.timetable.hierarchy.vo.LabVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class LabService {

    @Resource
    INobesTimetableCourseService TimetableService;

    @Resource
    INobesTimetableLabService labSelectService;

    @Resource
    MainService mainService;


    public ArrayList<LabVO> getLab(CourseDTO courseDTO) {

        ArrayList<LabVO> labs = new ArrayList<>();

        String courseName = courseDTO.getCourseName();

        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(courseName);
        matcher.find();
        String catalog = matcher.group(0).trim();
        String subject = courseName.substring(0, courseName.indexOf(catalog.charAt(0)) - 1);

        NobesTimetableCourse course = TimetableService.getOne(new LambdaQueryWrapper<NobesTimetableCourse>()
                .eq(NobesTimetableCourse::getCatalog, catalog)
                .eq(NobesTimetableCourse::getSubject, subject)
        );

        if (course.getLec().equals("0")) {
            return new ArrayList<>();
        }

        Integer courseId = course.getCourseId();

        List<NobesTimetableLab> sectionlist = labSelectService.list(new LambdaQueryWrapper<NobesTimetableLab>()
                .eq(NobesTimetableLab::getCourseId, courseId));

        for (NobesTimetableLab lab : sectionlist) {

            LabVO labObj = mainService.getLabObj(lab);
            labs.add(labObj);
        }

        return labs;
    }
}
