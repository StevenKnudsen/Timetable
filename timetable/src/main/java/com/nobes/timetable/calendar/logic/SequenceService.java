package com.nobes.timetable.calendar.logic;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nobes.timetable.calendar.domain.NobesTimetableSequence;
import com.nobes.timetable.calendar.dto.ProgDTO;
import com.nobes.timetable.calendar.factory.UniComponentFactory;
import com.nobes.timetable.calendar.service.INobesTimetableSequenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * This class represents a public service to first find all the courses in a given term, plan, and program
 * and then select strategy based on the given componentId
 * */
@Service
@Slf4j
public class SequenceService {


    /**
     * Inject a bean of INobesTimetableSequenceService type
     * */
    @Resource
    INobesTimetableSequenceService iSequenceService;

    /**
     * Inject a bean of UniComponentFactory to select the strategy
     * */
    @Autowired
    UniComponentFactory uniComponentFactory;


    /**
     * Find all the courses by the given information, then decide lec, lab, or sem strategy, and call
     * the corresponding method finally return the result hashmap
     * @param progDTO the ProgDTO object containing the program, term, plan and componentId.
     * @return a HashMap containing the course names and their result based on the componentId.
     * @throws Exception if any error occurs.
     *
     */
    public HashMap handle(ProgDTO progDTO) throws Exception {

        String program_Name = progDTO.getProgramName();
        String term_Name = progDTO.getTermName();
        String planName = progDTO.getPlanName();
        String componentId = progDTO.getComponentId();

        // query all the courses by the given information
        List<NobesTimetableSequence> courses = iSequenceService.list(new LambdaQueryWrapper<NobesTimetableSequence>()
                .eq(NobesTimetableSequence::getProgramName, program_Name)
                .eq(NobesTimetableSequence::getTermName, term_Name)
                .eq(NobesTimetableSequence::getPlanName, planName));

        ArrayList<String> names = new ArrayList<>();

        for (NobesTimetableSequence course : courses) {
            names.add(course.getCourseName());
        }


        String term = progDTO.getTermName().split(" ")[0].trim();

        // reformat the course name
        for (String name : names) {
            if (name.contains("(")) {
                int position = names.indexOf(name);
                int index = name.indexOf("(");
                String newCourseName = name.substring(0, index);
                names.set(position, newCourseName);
            }
        }

        // if there is an "or" in the course name, then just take the course name before or (the first one)
        for (String name : names) {
            // TODO: if there is a or case, just take the first one, need to be fixed
            if (name.contains("or")) {
                int position = names.indexOf(name);
                String[] splitCourses = name.split("\\s*[Oo][Rr]\\s*");
                names.set(position, splitCourses[0].trim());
            }
        }

        // select which strategy we are going to use
        HashMap map = uniComponentFactory.getStrategies(componentId).handle(names, term);

        // return the result hashmap
        return map;
    }
}
