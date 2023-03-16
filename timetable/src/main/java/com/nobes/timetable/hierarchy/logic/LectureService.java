package com.nobes.timetable.hierarchy.logic;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nobes.timetable.hierarchy.domain.NobesTimetableCourse;
import com.nobes.timetable.hierarchy.domain.NobesTimetableLecture;
import com.nobes.timetable.hierarchy.dto.CourseDTO;
import com.nobes.timetable.hierarchy.service.INobesTimetableCourseService;
import com.nobes.timetable.hierarchy.service.INobesTimetableLectureService;
import com.nobes.timetable.hierarchy.vo.LectureVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LectureService {

    @Resource
    INobesTimetableCourseService NBCourseService;

    @Resource
    INobesTimetableLectureService NBLectureService;

    @Resource
    MainService mainService;

    public ArrayList<LectureVO> getLecture(CourseDTO courseDTO) throws Exception {

        ArrayList<LectureVO> lectureVOS = new ArrayList<>();

        String courseName = courseDTO.getCourseName();

        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(courseName);
        matcher.find();
        String catalog = matcher.group(0);
        String subject = courseName.substring(0, courseName.indexOf(catalog.charAt(0)) - 1);

        NobesTimetableCourse course = NBCourseService.getOne(new LambdaQueryWrapper<NobesTimetableCourse>()
                .eq(NobesTimetableCourse::getCatalog, catalog)
                .eq(NobesTimetableCourse::getSubject, subject)
        );

        if (course.getLec().equals("0")) {
            return new ArrayList<>();
        }

        Integer courseId = course.getCourseId();

        List<NobesTimetableLecture> sectionlist = NBLectureService.list(new LambdaQueryWrapper<NobesTimetableLecture>()
                .eq(NobesTimetableLecture::getCourseId, courseId));

        for (NobesTimetableLecture lecture : sectionlist) {
            LectureVO lectureVOObj = mainService.getLectureObj(lecture);
            lectureVOS.add(lectureVOObj);
        }
        return lectureVOS;

    }
}
