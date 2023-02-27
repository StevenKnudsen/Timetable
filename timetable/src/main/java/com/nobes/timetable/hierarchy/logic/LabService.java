package com.nobes.timetable.hierarchy.logic;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nobes.timetable.core.utils.OrikaUtils;
import com.nobes.timetable.hierarchy.domain.NobesTimetableLab;
import com.nobes.timetable.hierarchy.domain.NobesTimetableLecture;
import com.nobes.timetable.hierarchy.service.INobesTimetableLabService;
import com.nobes.timetable.hierarchy.vo.LabAndSem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;

@Component
@Slf4j
public class LabService {
    public ArrayList<LabAndSem> getLab(NobesTimetableLecture lecture, INobesTimetableLabService iNobesTimetableLabService) {

        ArrayList<LabAndSem> labs = new ArrayList<>();

        String subject = lecture.getSubject();
        String catalog = lecture.getCatalog();
        String semSect = lecture.getSemSect();
        String[] split = semSect.split(",");

        for (int i = 0; i < split.length; i++) {
            NobesTimetableLab lab = iNobesTimetableLabService.getOne(new LambdaQueryWrapper<NobesTimetableLab>()
                    .eq(NobesTimetableLab::getSect, split[i])
                    .eq(NobesTimetableLab::getCatalog, catalog)
                    .eq(NobesTimetableLab::getSubject, subject));


            String component = lab.getComponent();
            String sect = lab.getSect();
            StringBuilder labName = new StringBuilder();
            StringBuilder duration = new StringBuilder();

            String mon = lab.getMon();
            String tues = lab.getTues();
            String wed = lab.getWed();
            String thurs = lab.getThrus();
            String fri = lab.getFri();

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
            labName.append(subject)
                    .append(" ")
                    .append(catalog)
                    .append(" ")
                    .append(component)
                    .append(" ")
                    .append(sect);

            LabAndSem sem = OrikaUtils.convert(lab, LabAndSem.class);

            sem.setName(labName.toString()).setDate(duration.toString());

            labs.add(sem);
        }

        return labs;
    }
}
