package com.nobes.timetable.hierarchy.logic;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nobes.timetable.core.utils.OrikaUtils;
import com.nobes.timetable.hierarchy.domain.*;
import com.nobes.timetable.hierarchy.logic.reqhelp.ParseHelpService;
import com.nobes.timetable.hierarchy.logic.reqhelp.ReqService;
import com.nobes.timetable.hierarchy.service.*;
import com.nobes.timetable.hierarchy.vo.CourseVO;
import com.nobes.timetable.hierarchy.vo.LabVO;
import com.nobes.timetable.hierarchy.vo.LectureVO;
import com.nobes.timetable.hierarchy.vo.SemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;

@Component
@Slf4j
public class MainService {

    @Resource
    INobesTimetableAuService iNobesTimetableAuService;

    @Resource
    ReqService reqService;

    public CourseVO getCourseObj(NobesTimetableCourse nobesTimetableCourse) throws Exception {
        CourseVO courseVO = OrikaUtils.convert(nobesTimetableCourse, CourseVO.class);

        String courseName = courseVO.getSubject() + " " + courseVO.getCatalog();
        courseVO.setCoureName(courseName);
        NobesTimetableAu courseAU = iNobesTimetableAuService.getOne(new LambdaQueryWrapper<NobesTimetableAu>()
                .eq(NobesTimetableAu::getCourseName, courseName));

        HashMap<String, Double> auCount = new HashMap<>();
        auCount.put("Math", Double.parseDouble(courseAU.getMath()));
        auCount.put("Natural Sciences", Double.parseDouble(courseAU.getNaturalSciences()));
        auCount.put("Complimentary Studies", Double.parseDouble(courseAU.getComplimentaryStudies()));
        auCount.put("Engineering Science", Double.parseDouble(courseAU.getEngineeringScience()));
        auCount.put("Engineering Design", Double.parseDouble(courseAU.getEngineeringDesign()));
        auCount.put("Others", Double.parseDouble(courseAU.getOther()));
        auCount.put("ES(sp)", Double.parseDouble(courseAU.getESsp()));
        auCount.put("ED(sp)", Double.parseDouble(courseAU.getEDsp()));

        courseVO.setAU_Count(auCount);

        ArrayList<String> preReqs = reqService.pullPreReqs(nobesTimetableCourse.getDescription());
        ArrayList<String> coReqs = reqService.pullCoReqs(nobesTimetableCourse.getDescription());

        courseVO.setPrerequisite(preReqs);
        courseVO.setCorequisite(coReqs);

        return courseVO;
    }


    public LectureVO getLectureObj(NobesTimetableLecture nobesTimetableLecture) {
        LectureVO lectureVO = OrikaUtils.convert(nobesTimetableLecture, LectureVO.class);

        String lectureName = nobesTimetableLecture.getSubject() + " "
                + nobesTimetableLecture.getCatalog() + " "
                + nobesTimetableLecture.getComponent() + " "
                + nobesTimetableLecture.getSect();

        lectureVO.setLecName(lectureName);
        lectureVO.setSection(nobesTimetableLecture.getSect());

        String mon = nobesTimetableLecture.getMon();
        String tues = nobesTimetableLecture.getTues();
        String wed = nobesTimetableLecture.getWed();
        String thurs = nobesTimetableLecture.getThrus();
        String fri = nobesTimetableLecture.getFri();

        ArrayList<String> lecs = new ArrayList<>();
        HashMap<String, String> weekdays = new HashMap<>();
        weekdays.put("MON", mon);
        weekdays.put("TUE", tues);
        weekdays.put("WED", wed);
        weekdays.put("THU", thurs);
        weekdays.put("FRI", fri);

        weekdays.forEach((key, value) -> {
            if (value.equals("Y")) {
                lecs.add(key);
            }
        });

        String hrsFrom = nobesTimetableLecture.getHrsFrom();
        String hrsTo = nobesTimetableLecture.getHrsTo();

        String hrs = hrsFrom + "-" + hrsTo;


        for (int i = 0; i < lecs.size(); i++) {
            lecs.set(i, lecs.get(i) + "_" + hrs);
        }

        lectureVO.setTimes(lecs);

        lectureVO.setColor(ParseHelpService.generateRandomColorCode());

        return lectureVO;
    }


    public LabVO getLabObj(NobesTimetableLab nobesTimetableLab) {

        String labName = nobesTimetableLab.getSubject() + " "
                + nobesTimetableLab.getCatalog() + " "
                + nobesTimetableLab.getComponent() + " "
                + nobesTimetableLab.getSect();


        String component = nobesTimetableLab.getComponent();
        String sect = nobesTimetableLab.getSect();

        ArrayList<String> labtimes = new ArrayList<>();

        String mon = nobesTimetableLab.getMon();
        String tues = nobesTimetableLab.getTues();
        String wed = nobesTimetableLab.getWed();
        String thurs = nobesTimetableLab.getThrus();
        String fri = nobesTimetableLab.getFri();

        HashMap<String, String> weekdays = new HashMap<>();
        weekdays.put("Monday", mon);
        weekdays.put("Tuesday", tues);
        weekdays.put("Wednesday", wed);
        weekdays.put("Thursday", thurs);
        weekdays.put("Friday", fri);

        weekdays.forEach((key, value) -> {
            if (value.equals("Y")) {
                labtimes.add(key);
            }
        });

        String hrsFrom = nobesTimetableLab.getHrsFrom();
        String hrsTo = nobesTimetableLab.getHrsTo();

        String hrs = hrsFrom + "-" + hrsTo;


        for (int i = 0; i < labtimes.size(); i++) {
            labtimes.set(i, labtimes.get(i) + "_" + hrs);
        }

        LabVO labVO = OrikaUtils.convert(nobesTimetableLab, LabVO.class);
        labVO.setTimes(labtimes);
        labVO.setLabName(labName);
        labVO.setColor(ParseHelpService.generateRandomColorCode());
        labVO.setSection(sect);

        return labVO;
    }


    public SemVO getSemObj(NobesTimetableSem nobesTimetableSem) {
        String labName = nobesTimetableSem.getSubject() + " "
                + nobesTimetableSem.getCatalog() + " "
                + nobesTimetableSem.getComponent() + " "
                + nobesTimetableSem.getSect();


        String sect = nobesTimetableSem.getSect();

        ArrayList<String> SemTimes = new ArrayList<>();

        String mon = nobesTimetableSem.getMon();
        String tues = nobesTimetableSem.getTues();
        String wed = nobesTimetableSem.getWed();
        String thurs = nobesTimetableSem.getThrus();
        String fri = nobesTimetableSem.getFri();

        HashMap<String, String> weekdays = new HashMap<>();
        weekdays.put("Monday", mon);
        weekdays.put("Tuesday", tues);
        weekdays.put("Wednesday", wed);
        weekdays.put("Thursday", thurs);
        weekdays.put("Friday", fri);

        weekdays.forEach((key, value) -> {
            if (value.equals("Y")) {
                SemTimes.add(key);
            }
        });

        String hrsFrom = nobesTimetableSem.getHrsFrom();
        String hrsTo = nobesTimetableSem.getHrsTo();

        String hrs = hrsFrom + "-" + hrsTo;


        for (int i = 0; i < SemTimes.size(); i++) {
            SemTimes.set(i, SemTimes.get(i) + "_" + hrs);
        }

        SemVO semVO = OrikaUtils.convert(nobesTimetableSem, SemVO.class);
        semVO.setTimes(SemTimes);
        semVO.setSemName(labName);
        semVO.setColor(ParseHelpService.generateRandomColorCode());
        semVO.setSection(sect);

        return semVO;
    }

}
