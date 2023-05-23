package com.nobes.timetable.calendar.logic;

import cn.hutool.core.lang.hash.Hash;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nobes.timetable.calendar.domain.NobesTimetableSequence;
import com.nobes.timetable.calendar.dto.ProgramDTO;
import com.nobes.timetable.calendar.service.INobesTimetableSequenceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.POIReadOnlyDocument;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * This class represents the Plan Service responsible for retrieving the plans and corresponding terms in a plan of a program
 * by searching the nobes_timetable_sequence table in the database
 * */
@Service
@Slf4j
public class PlanService {


    /**
     * inject a bean of INobesTimetableSequenceService type
     * */
    @Resource
    INobesTimetableSequenceService iNobesTimetableSequenceService;

    /**
     * Retrieves the plans and corresponding terms of the given program from the database by iNobesTimetableSequenceService (a service interface).
     * @param programDTO the ProgramDTO object containing the program name as input.
     * @return a HashMap containing the plans and corresponding terms of the given program.
     * @throws IOException if any error occurs during the retrieval process.
     */
    public LinkedHashMap<String, ArrayList> getPlan(ProgramDTO programDTO) throws IOException {

        LinkedHashMap<String, ArrayList> programMap = new LinkedHashMap<>();
        String program_Name = programDTO.getProgramName();

        // query the database where programName = program_Name and return a list store all the information
        List<NobesTimetableSequence> list = iNobesTimetableSequenceService.list(new LambdaQueryWrapper<NobesTimetableSequence>()
                .eq(NobesTimetableSequence::getProgramName, program_Name));

        ArrayList<String> plans = new ArrayList<>();

        // remove duplicate plans if there are
        for (NobesTimetableSequence nobesTimetableSequence : list) {
            if (!plans.contains(nobesTimetableSequence.getPlanName())) {
                plans.add(nobesTimetableSequence.getPlanName());
            }
        }

        for (String plan : plans) {
            // query the database where programName = program_Name and planName = plan
            List<NobesTimetableSequence> sequences = iNobesTimetableSequenceService.list(new LambdaQueryWrapper<NobesTimetableSequence>()
                    .eq(NobesTimetableSequence::getProgramName, program_Name)
                    .eq(NobesTimetableSequence::getPlanName, plan));

            ArrayList<String> terms = new ArrayList<>();

            // remove duplicate terms
            for (NobesTimetableSequence sequence : sequences) {
                if (!terms.contains(sequence.getTermName())) {
                    terms.add(sequence.getTermName());
                }
            }

            programMap.put(plan, terms);
        }

        return programMap;
    }
}
