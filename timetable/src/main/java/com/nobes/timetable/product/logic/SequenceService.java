package com.nobes.timetable.product.logic;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nobes.timetable.product.domain.ProgramId;
import com.nobes.timetable.product.dto.ProgDTO;
import com.nobes.timetable.product.service.IProgramIdService;
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
    IProgramIdService iProgramIdService;

    @Autowired
    CoursesService coursesService;

    public ArrayList<String> calendarScrap(ProgDTO progDTO) throws Exception {

        String program_Name = progDTO.getProgramName();
        String term_Name = progDTO.getTermName();

        ProgramId programId = iProgramIdService.getOne(new LambdaQueryWrapper<ProgramId>()
                .eq(ProgramId::getProgramName, program_Name));

        Integer catoid = programId.getCatoid();
        Integer poid = programId.getPoid();
        String url = "https://calendar.ualberta.ca/preview_program.php?catoid=" + catoid.toString() + "&poid=" + poid.toString();
        Document document = Jsoup.connect(url).get();

        Map<String, ArrayList> sequenceMap = coursesService.getCourses(document);

        return sequenceMap.get(term_Name);
    }


}
