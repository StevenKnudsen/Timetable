package com.nobes.timetable.calendar.factory.strategies.lab;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nobes.timetable.calendar.domain.NobesTimetableLab;
import com.nobes.timetable.calendar.dto.CourseIdDTO;
import com.nobes.timetable.calendar.logic.MainService;
import com.nobes.timetable.calendar.service.INobesTimetableLabService;
import com.nobes.timetable.calendar.vo.LabVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class LabService {

    @Resource
    INobesTimetableLabService labSelectService;

    @Resource
    MainService mainService;


    public ArrayList<LabVO> getLab(CourseIdDTO courseidDTO) {

        ArrayList<LabVO> labs = new ArrayList<>();
        Integer courseId = courseidDTO.getCourseId();

        List<NobesTimetableLab> sectionlist = labSelectService.list(new LambdaQueryWrapper<NobesTimetableLab>()
                .eq(NobesTimetableLab::getCourseId, courseId));


        for (NobesTimetableLab lab : sectionlist) {

            LabVO labObj = mainService.getLabObj(lab);

            if (labObj != null) {
                labs.add(labObj);
            }

        }

        ArrayList<LabVO> labVOS = new ArrayList<>();

        for (LabVO lab : labs) {
            if (!labVOS.contains(lab)) {
                labVOS.add(lab);
            }
        }

        return labVOS;
    }
}
