package com.nobes.timetable.hierarchy.logic;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nobes.timetable.core.utils.OrikaUtils;
import com.nobes.timetable.hierarchy.domain.NobesTimetableCourse;
import com.nobes.timetable.hierarchy.domain.NobesTimetableLab;
import com.nobes.timetable.hierarchy.domain.NobesTimetableLecture;
import com.nobes.timetable.hierarchy.domain.NobesTimetableSem;
import com.nobes.timetable.hierarchy.dto.CourseDTO;
import com.nobes.timetable.hierarchy.service.INobesTimetableCourseService;
import com.nobes.timetable.hierarchy.service.INobesTimetableLabService;
import com.nobes.timetable.hierarchy.service.INobesTimetableSemService;
import com.nobes.timetable.hierarchy.vo.LabVO;
import com.nobes.timetable.hierarchy.vo.SemVO;
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
public class SemService {

    @Resource
    INobesTimetableCourseService SemTableService;

    @Resource
    INobesTimetableSemService iNobesTimetableSemService;

    @Resource
    MainService mainService;


    public ArrayList<SemVO> getSem(CourseDTO courseDTO) {

        String courseName = courseDTO.getCourseName();

        ArrayList<SemVO> sems = new ArrayList<>();

        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(courseName);
        matcher.find();
        String catalog = matcher.group(0).trim();
        String subject = courseName.substring(0, courseName.indexOf(catalog.charAt(0)) - 1);

        NobesTimetableCourse course = SemTableService.getOne(new LambdaQueryWrapper<NobesTimetableCourse>()
                .eq(NobesTimetableCourse::getCatalog, catalog)
                .eq(NobesTimetableCourse::getSubject, subject)
        );

        if (course.getLec().equals("0")) {
            return new ArrayList<>();
        }

        Integer courseId = course.getCourseId();

        List<NobesTimetableSem> sectionlist = iNobesTimetableSemService.list(new LambdaQueryWrapper<NobesTimetableSem>()
                .eq(NobesTimetableSem::getCourseId, courseId));

        for (NobesTimetableSem sem : sectionlist) {
            SemVO semVO = mainService.getSemObj(sem);
            sems.add(semVO);
        }

        return sems;
    }
}

