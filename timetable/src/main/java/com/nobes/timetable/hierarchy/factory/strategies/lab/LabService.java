package com.nobes.timetable.hierarchy.factory.strategies.lab;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nobes.timetable.hierarchy.domain.NobesTimetableLab;
import com.nobes.timetable.hierarchy.dto.CourseIdDTO;
import com.nobes.timetable.hierarchy.logic.MainService;
import com.nobes.timetable.hierarchy.service.INobesTimetableLabService;
import com.nobes.timetable.hierarchy.vo.LabVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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

        HashSet<LabVO> nonDuoLabs = new HashSet<>(labs);

        ArrayList<LabVO> labVOs = new ArrayList<>(nonDuoLabs);

        return labVOs;
    }
}
