package com.nobes.timetable.visualizer.logic;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nobes.timetable.calendar.domain.NobesTimetableSequence;
import com.nobes.timetable.calendar.service.INobesTimetableSequenceService;
import com.nobes.timetable.visualizer.domain.NobesVisualizerCourse;
import com.nobes.timetable.visualizer.dto.VisualDTO;
import com.nobes.timetable.visualizer.service.INobesVisualizerCourseService;
import com.nobes.timetable.visualizer.service.INobesVisualizerCoursegroupService;
import com.nobes.timetable.visualizer.vo.VisualVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VisualService {

    @Resource
    INobesTimetableSequenceService sService;

    @Resource
    INobesVisualizerCourseService iNobesVisualizerCourseService;

    @Resource
    INobesVisualizerCoursegroupService iNobesVisualizerCoursegroupService;

    public HashMap getCourses(VisualDTO visualDTO) throws Exception {
        HashMap<String, ArrayList<VisualVO>> programMap = new HashMap<>();

        String planName = visualDTO.getPlanName();
        String programName = visualDTO.getProgramName();

        List<NobesTimetableSequence> courseList = sService.list(new LambdaQueryWrapper<NobesTimetableSequence>()
                .eq(NobesTimetableSequence::getProgramName, programName)
                .eq(NobesTimetableSequence::getPlanName, planName));

        Map<String, List<String>> map = courseList.stream()
                .collect(Collectors.groupingBy(NobesTimetableSequence::getTermName,
                        Collectors.mapping(NobesTimetableSequence::getCourseName, Collectors.toList())));

        map.forEach((key, value) -> {

            ArrayList<VisualVO> visualVOS = new ArrayList<>();

            value.forEach(courseName -> {
                courseName.trim();
                String[] split = courseName.split("\\s+");
                String subject = split[0];
                String catalog = split[1];

                NobesVisualizerCourse course = iNobesVisualizerCourseService.getOne(new LambdaQueryWrapper<NobesVisualizerCourse>()
                        .eq(NobesVisualizerCourse::getCatalog, catalog)
                        .eq(NobesVisualizerCourse::getSubject, subject), false);

                String progUnits = course.getProgUnits();
                String calcFeeIndex = course.getCalcFeeIndex();
                String duration = course.getDuration();
                String alphaHours = course.getAlphaHours();
                String courseDescription = course.getCourseDescription();

                String description = "★ " + progUnits.replaceAll("[^0-9]", "") + " (fi" + calcFeeIndex + ") " + "(" + duration + ", " + alphaHours + ") " + courseDescription;

                VisualVO visualVO = new VisualVO();
                visualVO.setCourseName(courseName)
                        .setTitle(course.getTitle())
                        .setDescription(description);

                visualVOS.add(visualVO);
            });

            programMap.put(key, visualVOS);
        });

        return programMap;
    }
}
