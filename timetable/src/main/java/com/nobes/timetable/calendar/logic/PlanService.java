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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
public class PlanService {

    @Resource
    INobesTimetableSequenceService iNobesTimetableSequenceService;

    public HashMap<String, ArrayList> getPlan(ProgramDTO programDTO) throws IOException {

        HashMap<String, ArrayList> programMap = new HashMap<>();
        String program_Name = programDTO.getProgramName();

        List<NobesTimetableSequence> list = iNobesTimetableSequenceService.list(new LambdaQueryWrapper<NobesTimetableSequence>()
                .eq(NobesTimetableSequence::getProgramName, program_Name));

        ArrayList<String> plans = new ArrayList<>();

        for (NobesTimetableSequence nobesTimetableSequence : list) {
            if (!plans.contains(nobesTimetableSequence.getPlanName())) {
                plans.add(nobesTimetableSequence.getPlanName());
            }
        }

        for (String plan : plans) {
            List<NobesTimetableSequence> sequences = iNobesTimetableSequenceService.list(new LambdaQueryWrapper<NobesTimetableSequence>()
                    .eq(NobesTimetableSequence::getProgramName, program_Name)
                    .eq(NobesTimetableSequence::getPlanName, plan));

            ArrayList<String> terms = new ArrayList<>();

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
