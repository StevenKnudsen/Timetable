package com.nobes.timetable.calendar.factory.strategies.seminar;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nobes.timetable.calendar.domain.NobesTimetableSem;
import com.nobes.timetable.calendar.dto.CourseIdDTO;
import com.nobes.timetable.calendar.logic.MainService;
import com.nobes.timetable.calendar.service.INobesTimetableSemService;
import com.nobes.timetable.calendar.vo.SemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 *
 *  This class provides a method for retrieving seminar information for a given course ID.
 *
 * */
@Component
@Slf4j
public class SemService {

    @Resource
    INobesTimetableSemService iNobesTimetableSemService;

    @Resource
    MainService mainService;


    /**
     * Find all the seminar sections of a given course, if two or more seminar section's time is the same
     * Think of these as one
     *
     * @param courseDTO the DTO containing the course ID
     * @return the list of non-duplicate lectures
     */
    public ArrayList<SemVO> getSem(CourseIdDTO courseDTO) {

        Integer courseId = courseDTO.getCourseId();

        ArrayList<SemVO> sems = new ArrayList<>();

        List<NobesTimetableSem> sectionlist = iNobesTimetableSemService.list(new LambdaQueryWrapper<NobesTimetableSem>()
                .eq(NobesTimetableSem::getCourseId, courseId));

        for (NobesTimetableSem sem : sectionlist) {
            SemVO semVO = mainService.getSemObj(sem);

            if (semVO != null) {
                sems.add(semVO);
            }

        }

        HashSet<SemVO> nonDuoSems = new HashSet<>(sems);

        ArrayList<SemVO> semVOS = new ArrayList<>(nonDuoSems);

        return semVOS;
    }
}

