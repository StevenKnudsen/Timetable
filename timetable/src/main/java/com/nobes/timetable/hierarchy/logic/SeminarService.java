package com.nobes.timetable.hierarchy.logic;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nobes.timetable.core.entity.ResultBody;
import com.nobes.timetable.core.utils.OrikaUtils;
import com.nobes.timetable.hierarchy.domain.NobesTimetableLecture;
import com.nobes.timetable.hierarchy.domain.NobesTimetableSem;
import com.nobes.timetable.hierarchy.service.INobesTimetableSemService;
import com.nobes.timetable.hierarchy.vo.LabAndSem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;

@Component
@Slf4j
public class SeminarService {

    @Resource
    INobesTimetableSemService iNobesTimetableSemService;

    public ArrayList<LabAndSem> getSeminar(NobesTimetableLecture lecture) {

        ArrayList<LabAndSem> seminars = new ArrayList<>();

        String subject = lecture.getSubject();
        String catalog = lecture.getCatalog();
        String semSect = lecture.getSemSect();
        String[] split = semSect.split(",");


        for (int i = 0; i < split.length; i++) {
            NobesTimetableSem seminar = iNobesTimetableSemService.getOne(new LambdaQueryWrapper<NobesTimetableSem>()
                    .eq(NobesTimetableSem::getSect, split[i])
                    .eq(NobesTimetableSem::getCatalog, catalog)
                    .eq(NobesTimetableSem::getSubject, subject));


            String component = seminar.getComponent();
            String sect = seminar.getSect();
            StringBuilder semName = new StringBuilder();
            StringBuilder duration = new StringBuilder();

            String mon = seminar.getMon();
            String tues = seminar.getTues();
            String wed = seminar.getWed();
            String thurs = seminar.getThrus();
            String fri = seminar.getFri();

            HashMap<String, String> weekdays = new HashMap<>();
            weekdays.put("Monday", mon);
            weekdays.put("Tuesday", tues);
            weekdays.put("Wednesday", wed);
            weekdays.put("Thursday", thurs);
            weekdays.put("Friday", fri);

            weekdays.forEach((key, value) -> {
                if (value.equals("Y")) {
                    duration.append(key).append(",");
                }
            });

            duration.deleteCharAt(duration.length() - 1);
            semName.append(subject)
                    .append(" ")
                    .append(catalog)
                    .append(" ")
                    .append(component)
                    .append(" ")
                    .append(sect);

            LabAndSem sem = OrikaUtils.convert(seminar, LabAndSem.class);

            sem.setName(semName.toString()).setDate(duration.toString());

            seminars.add(sem);

        }

        return seminars;
    }
}
