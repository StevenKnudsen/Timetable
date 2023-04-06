package com.nobes.timetable.calendar.factory.strategies.lecture;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nobes.timetable.calendar.domain.NobesTimetableLecture;
import com.nobes.timetable.calendar.dto.CourseIdDTO;
import com.nobes.timetable.calendar.logic.MainService;
import com.nobes.timetable.calendar.service.INobesTimetableLectureService;
import com.nobes.timetable.calendar.vo.LectureVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class LecService {
    @Resource
    INobesTimetableLectureService NBLectureService;

    @Resource
    MainService mainService;

    public ArrayList<LectureVO> getLecture(CourseIdDTO courseDTO) throws Exception {

        ArrayList<LectureVO> lectureVOS = new ArrayList<>();

        Integer courseId = courseDTO.getCourseId();

        List<NobesTimetableLecture> sectionlist = NBLectureService.list(new LambdaQueryWrapper<NobesTimetableLecture>()
                .eq(NobesTimetableLecture::getCourseId, courseId));


        for (NobesTimetableLecture lecture : sectionlist) {
            LectureVO lectureVOObj = mainService.getLectureObj(lecture);

            if (lectureVOObj != null) {
                lectureVOS.add(lectureVOObj);
            }
        }

        HashSet<LectureVO> nonDuoLecs = new HashSet<>(lectureVOS);

        ArrayList<LectureVO> lecs = new ArrayList<>(nonDuoLecs);

        return lecs;

    }
}
