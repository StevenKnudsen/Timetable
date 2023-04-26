package com.nobes.timetable.calendar.logic;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nobes.timetable.core.utils.OrikaUtils;
import com.nobes.timetable.calendar.domain.*;
import com.nobes.timetable.calendar.service.INobesTimetableAuService;
import com.nobes.timetable.calendar.vo.CourseVO;
import com.nobes.timetable.calendar.vo.LabVO;
import com.nobes.timetable.calendar.vo.LectureVO;
import com.nobes.timetable.calendar.vo.SemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;


/**
 *
 * This class represents the Main Service responsible for converting NobesTimetableCourse, NobesTimetableLecture,
 * NobesTimetableLab, and NobesTimetableSem objects into corresponding CourseVO, LectureVO, LabVO, and SemVO (they are all return types) objects
 *
 * */
@Component
@Slf4j
public class MainService {

    /**
     * inject a bean of INobesTimetableAuService type
     * */
    @Resource
    INobesTimetableAuService iNobesTimetableAuService;



    /**
     * unused method
     * */
    public CourseVO getCourseObj(NobesTimetableCourse nobesTimetableCourse) throws Exception {
        CourseVO courseVO = OrikaUtils.convert(nobesTimetableCourse, CourseVO.class);

        String courseName = courseVO.getSubject() + " " + courseVO.getCatalog();
        courseVO.setCourseName(courseName);
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

        courseVO.setAUCount(auCount);

        return courseVO;
    }



    /**
     * Converts the given NobesTimetableLecture object into a corresponding LectureVO object and sets the lecture name, section, time and
     * duration for each lecture sessions.
     * @param nobesTimetableLecture the NobesTimetableLecture object to be converted.
     * @return the corresponding LectureVO.
     */
    public LectureVO getLectureObj(NobesTimetableLecture nobesTimetableLecture) {

        // (OrikaUtils.convert(B b, A.class)), OrikaUtils will convert a B type object b in to A type and copy all
        // the attribute value(same attribute name in A and B) from B to A
        LectureVO lectureVO = OrikaUtils.convert(nobesTimetableLecture, LectureVO.class);

        String lectureName = nobesTimetableLecture.getSubject()
                + nobesTimetableLecture.getCatalog() + " "
                + nobesTimetableLecture.getComponent() + " "
                + nobesTimetableLecture.getSect();

        lectureVO.setLecName(lectureName);//set the lecture session name
        lectureVO.setSection(nobesTimetableLecture.getSect());//set the section


        // set the lecture time and duration
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

        if (hrsFrom.equals(hrsTo)) {
            return null;
        }

        String hrs = hrsFrom + "-" + hrsTo;


        for (int i = 0; i < lecs.size(); i++) {
            lecs.set(i, lecs.get(i) + "_" + hrs);
        }

        lectureVO.setTimes(lecs);

        return lectureVO;
    }


    /**
     * Converts the given NobesTimetableLab object into a corresponding LabVO object and sets the section, time and
     * duration for each lab sessions.
     * @param nobesTimetableLab the NobesTimetableLab object to be converted.
     * @return the corresponding LabVO.
     */
    public LabVO getLabObj(NobesTimetableLab nobesTimetableLab) {

        LabVO labVO = OrikaUtils.convert(nobesTimetableLab, LabVO.class);

        String labName = nobesTimetableLab.getSubject()
                + nobesTimetableLab.getCatalog() + " "
                + nobesTimetableLab.getComponent() + " "
                + nobesTimetableLab.getSect();

        String sect = nobesTimetableLab.getSect();

        String mon = nobesTimetableLab.getMon();
        String tues = nobesTimetableLab.getTues();
        String wed = nobesTimetableLab.getWed();
        String thurs = nobesTimetableLab.getThrus();
        String fri = nobesTimetableLab.getFri();

        ArrayList<String> labs = new ArrayList<>();
        HashMap<String, String> weekdays = new HashMap<>();
        weekdays.put("MON", mon);
        weekdays.put("TUE", tues);
        weekdays.put("WED", wed);
        weekdays.put("THU", thurs);
        weekdays.put("FRI", fri);

        weekdays.forEach((key, value) -> {
            if (value.equals("Y")) {
                labs.add(key);
            }
        });

        String hrsFrom = nobesTimetableLab.getHrsFrom();
        String hrsTo = nobesTimetableLab.getHrsTo();

        if (hrsFrom.equals(hrsTo)) {
            return null;
        }

        String hrs = hrsFrom + "-" + hrsTo;


        for (int i = 0; i < labs.size(); i++) {
            labs.set(i, labs.get(i) + "_" + hrs);
        }

        labVO.setTimes(labs);
        labVO.setLabName(labName);
        labVO.setSection(sect);

        return labVO;
    }


    /**
     * Converts the given NobesTimetableSem object into a corresponding LabVO object and sets the section, time and
     * duration for each seminar sessions.
     * @param nobesTimetableSem the NobesTimetableSem object to be converted.
     * @return the corresponding SemVO.
     */
    public SemVO getSemObj(NobesTimetableSem nobesTimetableSem) {
        SemVO semVO = OrikaUtils.convert(nobesTimetableSem, SemVO.class);

        String labName = nobesTimetableSem.getSubject()
                + nobesTimetableSem.getCatalog() + " "
                + nobesTimetableSem.getComponent() + " "
                + nobesTimetableSem.getSect();


        String sect = nobesTimetableSem.getSect();

        String mon = nobesTimetableSem.getMon();
        String tues = nobesTimetableSem.getTues();
        String wed = nobesTimetableSem.getWed();
        String thurs = nobesTimetableSem.getThrus();
        String fri = nobesTimetableSem.getFri();

        ArrayList<String> sems = new ArrayList<>();
        HashMap<String, String> weekdays = new HashMap<>();
        weekdays.put("MON", mon);
        weekdays.put("TUE", tues);
        weekdays.put("WED", wed);
        weekdays.put("THU", thurs);
        weekdays.put("FRI", fri);

        weekdays.forEach((key, value) -> {
            if (value.equals("Y")) {
                sems.add(key);
            }
        });

        String hrsFrom = nobesTimetableSem.getHrsFrom();
        String hrsTo = nobesTimetableSem.getHrsTo();

        if (hrsFrom.equals(hrsTo)) {
            return null;
        }

        String hrs = hrsFrom + "-" + hrsTo;


        for (int i = 0; i < sems.size(); i++) {
            sems.set(i, sems.get(i) + "_" + hrs);
        }

        semVO.setTimes(sems);
        semVO.setSemName(labName);
        semVO.setSection(sect);

        return semVO;
    }

}
