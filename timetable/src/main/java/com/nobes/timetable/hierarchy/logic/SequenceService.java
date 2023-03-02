package com.nobes.timetable.hierarchy.logic;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nobes.timetable.hierarchy.domain.NobesTimetableProgram;
import com.nobes.timetable.hierarchy.dto.ProgDTO;
import com.nobes.timetable.hierarchy.service.INobesTimetableProgramService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Map;


@Service
@Slf4j
public class SequenceService {
    @Resource
    INobesTimetableProgramService iNobesTimetableProgramService;

    @Autowired
    ScrapService scrapService;

    public ArrayList<String> calendarScrap(ProgDTO progDTO) throws Exception {

        String program_Name = progDTO.getProgramName();
        String term_Name = progDTO.getTermName();

        NobesTimetableProgram nobesTimetableProgram = iNobesTimetableProgramService.getOne(new LambdaQueryWrapper<NobesTimetableProgram>()
                .eq(NobesTimetableProgram::getProgramName, program_Name));

        Integer catoid = nobesTimetableProgram.getCatoid();
        Integer poid = nobesTimetableProgram.getPoid();
        String url = "https://calendar.ualberta.ca/preview_program.php?catoid=" + catoid.toString() + "&poid=" + poid.toString();
        Document document = Jsoup.connect(url).get();

        Map<String, ArrayList> sequenceMap = scrapService.getCourses(document);

        return sequenceMap.get(term_Name);
    }

}
