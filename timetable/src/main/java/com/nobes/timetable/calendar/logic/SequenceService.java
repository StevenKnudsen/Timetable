package com.nobes.timetable.calendar.logic;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nobes.timetable.calendar.domain.NobesTimetableSequence;
import com.nobes.timetable.calendar.dto.ProgDTO;
import com.nobes.timetable.calendar.factory.UniComponentFactory;
import com.nobes.timetable.calendar.service.INobesTimetableSequenceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class SequenceService {

    @Resource
    INobesTimetableSequenceService iSequenceService;

    @Autowired
    UniComponentFactory uniComponentFactory;

    public HashMap handle(ProgDTO progDTO) throws Exception {

        String program_Name = progDTO.getProgramName();
        String term_Name = progDTO.getTermName();
        String planName = progDTO.getPlanName();
        String componentId = progDTO.getComponentId();

        List<NobesTimetableSequence> courses = iSequenceService.list(new LambdaQueryWrapper<NobesTimetableSequence>()
                .eq(NobesTimetableSequence::getProgramName, program_Name)
                .eq(NobesTimetableSequence::getTermName, term_Name)
                .eq(NobesTimetableSequence::getPlanName, planName));

        ArrayList<String> names = new ArrayList<>();

        for (NobesTimetableSequence course : courses) {
            names.add(course.getCourseName());
        }


        String term = progDTO.getTermName().split(" ")[0].trim();

        for (String name : names) {
            if (name.contains("(")) {
                int position = names.indexOf(name);
                int index = name.indexOf("(");
                String newCourseName = name.substring(0, index);
                names.set(position, newCourseName);
            }
        }

        for (String name : names) {
            // TODO: if there is a or case, just take the first one, need to be fixed
            if (name.contains("or")) {
                int position = names.indexOf(name);
                String[] splitCourses = name.split("\\s*[Oo][Rr]\\s*");
                names.set(position, splitCourses[0].trim());
            }
        }

        HashMap map = uniComponentFactory.getStrategies(componentId).handle(names, term);
        return map;
    }
}
