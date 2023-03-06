package com.nobes.timetable.core.save.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nobes.timetable.core.save.test.domain.Course;
import com.nobes.timetable.core.save.test.domain.Lab;
import com.nobes.timetable.core.save.test.domain.Lec;
import com.nobes.timetable.core.save.test.domain.Sem;
import com.nobes.timetable.core.save.test.service.ICourseService;
import com.nobes.timetable.core.save.test.service.ILabService;
import com.nobes.timetable.core.save.test.service.ILecService;
import com.nobes.timetable.core.save.test.service.ISemService;
import com.nobes.timetable.core.utils.OrikaUtils;
import com.nobes.timetable.hierarchy.domain.NobesTimetableTable;
import com.nobes.timetable.hierarchy.service.INobesTimetableTableService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


@Service
@Slf4j
public class ImportService {

    @Autowired
    TimeService timeService;

    @Resource
    INobesTimetableTableService TableService;

    @Resource
    ICourseService iCourseService;

    @Resource
    ILecService iLecService;

    @Resource
    ILabService iLabService;

    @Resource
    ISemService iSemService;


    /*
    * save the timetable Excel files to the database as original data table
    * */
    public void excelImport(File file) throws Exception {

        String url = "jdbc:mysql://localhost:3306/mydatabase?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&allowMultiQueries=true&useSSL=false";
        String username = "root";
        String password = "jxp51515";

        Connection connection = DriverManager.getConnection(url, username, password);

        Workbook workbook = WorkbookFactory.create(file);
        Sheet sheet = workbook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();

        for (int i = 1; i < lastRowNum + 1; i++) {

            String sql = "INSERT INTO nobes_timetable_table VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            NobesTimetableTable nobesTimetableTable = new NobesTimetableTable();

            if (sheet.getRow(i).getCell(0) == null) {
                nobesTimetableTable.setAcadOrg("null");
            } else {
                nobesTimetableTable.setAcadOrg(sheet.getRow(i).getCell(0).getStringCellValue());
            }

            if (sheet.getRow(i).getCell(1) == null) {
                nobesTimetableTable.setTerm("null");
            } else {
                nobesTimetableTable.setTerm(String.valueOf((int) sheet.getRow(i).getCell(1).getNumericCellValue()));
            }

            if (sheet.getRow(i).getCell(2) == null) {
                nobesTimetableTable.setShortDesc("null");
            } else {
                nobesTimetableTable.setShortDesc(sheet.getRow(i).getCell(2).getStringCellValue());
            }

            if (sheet.getRow(i).getCell(3) == null) {
                nobesTimetableTable.setClassNbr("null");
            } else {
                nobesTimetableTable.setClassNbr(String.valueOf((int) sheet.getRow(i).getCell(3).getNumericCellValue()));
            }

            if (sheet.getRow(i).getCell(4) == null) {
                nobesTimetableTable.setSubject("null");
            } else {
                nobesTimetableTable.setSubject(sheet.getRow(i).getCell(4).getStringCellValue());
            }

            if (sheet.getRow(i).getCell(5) == null) {
                nobesTimetableTable.setCatalog("null");
            } else if (sheet.getRow(i).getCell(5).getCellType() == CellType.NUMERIC) {
                nobesTimetableTable.setCatalog(String.valueOf((int) sheet.getRow(i).getCell(5).getNumericCellValue()));
            } else {
                nobesTimetableTable.setCatalog(sheet.getRow(i).getCell(5).getStringCellValue());
            }

            if (sheet.getRow(i).getCell(6) == null) {
                nobesTimetableTable.setComponent("null");
            } else {
                nobesTimetableTable.setComponent(sheet.getRow(i).getCell(6).getStringCellValue());
            }

            if (sheet.getRow(i).getCell(7) == null) {
                nobesTimetableTable.setSect("null");
            } else if (sheet.getRow(i).getCell(7).getCellType() == CellType.NUMERIC) {
                nobesTimetableTable.setSect(String.valueOf((int) sheet.getRow(i).getCell(7).getNumericCellValue()));
            } else {
                nobesTimetableTable.setSect(sheet.getRow(i).getCell(7).getStringCellValue());
            }

            if (sheet.getRow(i).getCell(8) == null) {
                nobesTimetableTable.setClassStatus("null");
            } else {
                nobesTimetableTable.setClassStatus(sheet.getRow(i).getCell(8).getStringCellValue());
            }

            if (sheet.getRow(i).getCell(9) == null) {
                nobesTimetableTable.setDescr("null");
            } else {
                nobesTimetableTable.setDescr(sheet.getRow(i).getCell(9).getStringCellValue());
            }

            if (sheet.getRow(i).getCell(10) == null) {
                nobesTimetableTable.setCrsStatus("null");
            } else {
                nobesTimetableTable.setCrsStatus(sheet.getRow(i).getCell(10).getStringCellValue());
            }

            if (sheet.getRow(i).getCell(11) == null) {
                nobesTimetableTable.setFacilID("null");
            } else {
                nobesTimetableTable.setFacilID(String.valueOf((int) sheet.getRow(i).getCell(11).getNumericCellValue()));

            }

            if (sheet.getRow(i).getCell(12) == null) {
                nobesTimetableTable.setPlace("null");
            } else {
                nobesTimetableTable.setPlace(sheet.getRow(i).getCell(12).getStringCellValue());
            }

            if (sheet.getRow(i).getCell(13) == null) {
                nobesTimetableTable.setPat("null");
            } else {
                nobesTimetableTable.setPat(sheet.getRow(i).getCell(13).getStringCellValue());
            }

            if (sheet.getRow(i).getCell(14) == null) {
                nobesTimetableTable.setStartDate("null");
            } else {
                nobesTimetableTable.setStartDate(sheet.getRow(i).getCell(14).getStringCellValue());
            }

            if (sheet.getRow(i).getCell(15) == null) {
                nobesTimetableTable.setEndDate("null");
            } else {
                nobesTimetableTable.setEndDate(sheet.getRow(i).getCell(15).getStringCellValue());
            }

            if (sheet.getRow(i).getCell(16) == null) {
                nobesTimetableTable.setHrsFrom("null");
            } else {
                nobesTimetableTable.setHrsFrom(timeService.getTime(sheet.getRow(i).getCell(16)));
            }

            if (sheet.getRow(i).getCell(17) == null) {
                nobesTimetableTable.setHrsTo("null");
            } else {
                nobesTimetableTable.setHrsTo(timeService.getTime(sheet.getRow(i).getCell(17)));
            }

            if (sheet.getRow(i).getCell(18) == null) {
                nobesTimetableTable.setMon("null");
            } else {
                nobesTimetableTable.setMon(sheet.getRow(i).getCell(18).getStringCellValue());
            }

            if (sheet.getRow(i).getCell(19) == null) {
                nobesTimetableTable.setTerm("null");
            } else {
                nobesTimetableTable.setTues(sheet.getRow(i).getCell(19).getStringCellValue());
            }

            if (sheet.getRow(i).getCell(20) == null) {
                nobesTimetableTable.setWed("null");
            } else {
                nobesTimetableTable.setWed(sheet.getRow(i).getCell(20).getStringCellValue());
            }

            if (sheet.getRow(i).getCell(21) == null) {
                nobesTimetableTable.setThurs("null");
            } else {
                nobesTimetableTable.setThurs(sheet.getRow(i).getCell(21).getStringCellValue());
            }

            if (sheet.getRow(i).getCell(22) == null) {
                nobesTimetableTable.setFri("null");
            } else {
                nobesTimetableTable.setFri(sheet.getRow(i).getCell(22).getStringCellValue());
            }

            if (sheet.getRow(i).getCell(23) == null) {
                nobesTimetableTable.setSat("null");
            } else {
                nobesTimetableTable.setSat(sheet.getRow(i).getCell(23).getStringCellValue());
            }

            if (sheet.getRow(i).getCell(24) == null) {
                nobesTimetableTable.setSun("null");
            } else {
                nobesTimetableTable.setSun(sheet.getRow(i).getCell(24).getStringCellValue());
            }

            if (sheet.getRow(i).getCell(25) == null) {
                nobesTimetableTable.setName("null");
            } else {
                nobesTimetableTable.setName(sheet.getRow(i).getCell(25).getStringCellValue());
            }

            if (sheet.getRow(i).getCell(26) == null) {
                nobesTimetableTable.setInstructor("null");
            } else {
                nobesTimetableTable.setInstructor(String.valueOf((int) sheet.getRow(i).getCell(26).getNumericCellValue()));
            }

            if (sheet.getRow(i).getCell(27) == null) {
                nobesTimetableTable.setEmail("null");
            } else {
                nobesTimetableTable.setEmail(sheet.getRow(i).getCell(27).getStringCellValue());
            }

            if (sheet.getRow(i).getCell(28) == null) {
                nobesTimetableTable.setClassType("null");
            } else {
                nobesTimetableTable.setClassType(sheet.getRow(i).getCell(28).getStringCellValue());
            }

            if (sheet.getRow(i).getCell(29) == null) {
                nobesTimetableTable.setCapEnrl("null");
            } else {
                nobesTimetableTable.setCapEnrl(String.valueOf((int) sheet.getRow(i).getCell(29).getNumericCellValue()));
            }

            if (sheet.getRow(i).getCell(30) == null) {
                nobesTimetableTable.setTotEnrl("null");
            } else {
                nobesTimetableTable.setTotEnrl(String.valueOf((int) sheet.getRow(i).getCell(30).getNumericCellValue()));
            }

            if (sheet.getRow(i).getCell(31) == null) {
                nobesTimetableTable.setCampus("null");
            } else {
                nobesTimetableTable.setCampus(sheet.getRow(i).getCell(31).getStringCellValue());
            }

            if (sheet.getRow(i).getCell(32) == null) {
                nobesTimetableTable.setLocation("null");
            } else {
                nobesTimetableTable.setLocation(sheet.getRow(i).getCell(32).getStringCellValue());
            }

            if (sheet.getRow(i).getCell(33) == null) {
                nobesTimetableTable.setNotesNbr("null");
            } else {
                nobesTimetableTable.setNotesNbr(String.valueOf((int) sheet.getRow(i).getCell(33).getNumericCellValue()));
            }

            if (sheet.getRow(i).getCell(34) == null) {
                nobesTimetableTable.setNoteNbr("null");
            } else if (sheet.getRow(i).getCell(34).getCellType() == CellType.NUMERIC) {
                nobesTimetableTable.setNoteNbr(String.valueOf((int) sheet.getRow(i).getCell(34).getNumericCellValue()));
            } else {
                nobesTimetableTable.setNoteNbr(sheet.getRow(i).getCell(34).getStringCellValue());
            }

            if (sheet.getRow(i).getCell(35) == null) {
                nobesTimetableTable.setNote("null");
            } else {
                nobesTimetableTable.setNote(sheet.getRow(i).getCell(35).getStringCellValue());
            }

            if (sheet.getRow(i).getCell(36) == null) {
                nobesTimetableTable.setRqGroup("null");
            } else {
                nobesTimetableTable.setRqGroup(String.valueOf((int) sheet.getRow(i).getCell(36).getNumericCellValue()));
            }

            if (sheet.getRow(i).getCell(37) == null) {
                nobesTimetableTable.setDescpRe("null");
            } else {
                nobesTimetableTable.setDescpRe(sheet.getRow(i).getCell(37).getStringCellValue());
            }

            if (sheet.getRow(i).getCell(38) == null) {
                nobesTimetableTable.setApprovedHrs("null");
            } else {
                nobesTimetableTable.setApprovedHrs(sheet.getRow(i).getCell(38).getStringCellValue());
            }

            if (sheet.getRow(i).getCell(39) == null) {
                nobesTimetableTable.setDuration("null");
            } else {
                nobesTimetableTable.setDuration(sheet.getRow(i).getCell(39).getStringCellValue());
            }

            if (sheet.getRow(i).getCell(40) == null) {
                nobesTimetableTable.setCareer("null");
            } else {
                nobesTimetableTable.setCareer(sheet.getRow(i).getCell(40).getStringCellValue());
            }

            if (sheet.getRow(i).getCell(41) == null) {
                nobesTimetableTable.setConsent("null");
            } else {
                nobesTimetableTable.setConsent(sheet.getRow(i).getCell(41).getStringCellValue());
            }

            if (sheet.getRow(i).getCell(42) == null) {
                nobesTimetableTable.setDescription("null");
            } else {
                nobesTimetableTable.setDescription(sheet.getRow(i).getCell(42).getStringCellValue());
            }

            if (sheet.getRow(i).getCell(43) == null) {
                nobesTimetableTable.setMaxUnits("null");
            } else {
                nobesTimetableTable.setMaxUnits(String.valueOf(sheet.getRow(i).getCell(43).getNumericCellValue()));
            }

            preparedStatement.setString(1, nobesTimetableTable.getAcadOrg());
            preparedStatement.setString(2, nobesTimetableTable.getTerm());
            preparedStatement.setString(3, nobesTimetableTable.getShortDesc());
            preparedStatement.setString(4, nobesTimetableTable.getClassNbr());
            preparedStatement.setString(5, nobesTimetableTable.getSubject());
            preparedStatement.setString(6, nobesTimetableTable.getCatalog());
            preparedStatement.setString(7, nobesTimetableTable.getComponent());
            preparedStatement.setString(8, nobesTimetableTable.getSect());
            preparedStatement.setString(9, nobesTimetableTable.getClassStatus());
            preparedStatement.setString(10, nobesTimetableTable.getDescr());
            preparedStatement.setString(11, nobesTimetableTable.getCrsStatus());
            preparedStatement.setString(12, nobesTimetableTable.getFacilID());
            preparedStatement.setString(13, nobesTimetableTable.getPlace());
            preparedStatement.setString(14, nobesTimetableTable.getPat());
            preparedStatement.setString(15, nobesTimetableTable.getStartDate());
            preparedStatement.setString(16, nobesTimetableTable.getEndDate());
            preparedStatement.setString(17, nobesTimetableTable.getHrsFrom());
            preparedStatement.setString(18, nobesTimetableTable.getHrsTo());
            preparedStatement.setString(19, nobesTimetableTable.getMon());
            preparedStatement.setString(20, nobesTimetableTable.getTues());
            preparedStatement.setString(21, nobesTimetableTable.getWed());
            preparedStatement.setString(22, nobesTimetableTable.getThurs());
            preparedStatement.setString(23, nobesTimetableTable.getFri());
            preparedStatement.setString(24, nobesTimetableTable.getSat());
            preparedStatement.setString(25, nobesTimetableTable.getSun());
            preparedStatement.setString(26, nobesTimetableTable.getName());
            preparedStatement.setString(27, nobesTimetableTable.getInstructor());
            preparedStatement.setString(28, nobesTimetableTable.getEmail());
            preparedStatement.setString(29, nobesTimetableTable.getClassType());
            preparedStatement.setString(30, nobesTimetableTable.getCapEnrl());
            preparedStatement.setString(31, nobesTimetableTable.getTotEnrl());
            preparedStatement.setString(32, nobesTimetableTable.getCampus());
            preparedStatement.setString(33, nobesTimetableTable.getLocation());
            preparedStatement.setString(34, nobesTimetableTable.getNotesNbr());
            preparedStatement.setString(35, nobesTimetableTable.getNoteNbr());
            preparedStatement.setString(36, nobesTimetableTable.getNote());
            preparedStatement.setString(37, nobesTimetableTable.getRqGroup());
            preparedStatement.setString(38, nobesTimetableTable.getDescpRe());
            preparedStatement.setString(39, nobesTimetableTable.getApprovedHrs());
            preparedStatement.setString(40, nobesTimetableTable.getDuration());
            preparedStatement.setString(41, nobesTimetableTable.getCareer());
            preparedStatement.setString(42, nobesTimetableTable.getConsent());
            preparedStatement.setString(43, nobesTimetableTable.getDescription());
            preparedStatement.setString(44, nobesTimetableTable.getMaxUnits());


            preparedStatement.executeUpdate();

            preparedStatement.close();
        }

        connection.close();

        log.info("Table Import Complete");
    }

    /*
     * populate the course data from Excel files to the course table
     * */
    public void courseImport() {
        HashSet<String> courseNames = new HashSet<>();

        List<NobesTimetableTable> courses = TableService.list(null);

        Integer courseId = 1;

        for (NobesTimetableTable course : courses) {
            String subject = course.getSubject();
            String catalog = course.getCatalog();
            String description = course.getDescription();
            String maxUnits = course.getMaxUnits();
            String courseName = subject + " " + catalog;



            if (!courseNames.contains(courseName)) {
                String approvedHrs = course.getApprovedHrs();

                Course newcourse = OrikaUtils.convert(course, Course.class);

                if (approvedHrs.contains("-")) {
                    String[] hrs = approvedHrs.split("-");
                    newcourse.setLec(hrs[0]);
                    newcourse.setLab(hrs[1]);

                    if (hrs.length > 2) {
                        newcourse.setSem(hrs[2]);
                    } else {
                        newcourse.setSem("0");
                    }

                } else {
                    newcourse.setLec(approvedHrs);
                    newcourse.setLab(approvedHrs);
                    newcourse.setSem(approvedHrs);
                }

                courseNames.add(courseName);
                newcourse.setCourseId(courseId);
                newcourse.setDescp(description);
                newcourse.setPre(null);
                newcourse.setCo(null);
                newcourse.setAccreditUnits(maxUnits);
                courseId += 1;
                iCourseService.save(newcourse);
            }

        }

        log.info("Course Update Complete");

    }

    /*
     * populate the lecture data from Excel files to the lecture table
     * */
    public void lecImport() {

        List<Course> courses = iCourseService.list(null);
        HashMap<String, Integer> map = new HashMap<>();
        Integer lectureId = 1;
        String defaultsect = "ALL";

        for (Course course : courses) {
            String courseName = course.getSubject() + " " + course.getCatalog();
            Integer courseId = course.getCourseId();

            map.put(courseName, courseId);
        }

        List<NobesTimetableTable> lecs = TableService.list(new LambdaQueryWrapper<NobesTimetableTable>()
                .eq(NobesTimetableTable::getComponent, "LEC"));

        for (NobesTimetableTable lec : lecs) {

            String catalog = lec.getCatalog();
            String subject = lec.getSubject();

            String name = subject + " " + catalog;
            Integer courseId = map.get(name);

            Lec newlec = OrikaUtils.convert(lec, Lec.class);
            newlec.setCourseId(courseId);
            newlec.setLectureId(lectureId);
            newlec.setInstructorName(lec.getName());
            newlec.setInstructorEmail(lec.getEmail());
            newlec.setThrus(lec.getThurs());
            newlec.setLabSect(defaultsect);
            newlec.setSemSect(defaultsect);

            lectureId += 1;

            iLecService.save(newlec);
        }

        log.info("Lecture Import Complete");
    }

    /*
    * populate the lab data from Excel files to the lab table
    * */
    public void labImport() {
        List<Course> courses = iCourseService.list(null);
        HashMap<String, Integer> map = new HashMap<>();

        Integer labId = 1;

        for (Course course : courses) {
            String courseName = course.getSubject() + " " + course.getCatalog();
            Integer courseId = course.getCourseId();

            map.put(courseName, courseId);
        }

        List<NobesTimetableTable> labs = TableService.list(new LambdaQueryWrapper<NobesTimetableTable>()
                .eq(NobesTimetableTable::getComponent, "LAB"));

        for (NobesTimetableTable lab : labs) {
            String catalog = lab.getCatalog();
            String subject = lab.getSubject();

            String name = subject + " " + catalog;
            Integer courseId = map.get(name);

            Lab newlab = OrikaUtils.convert(lab, Lab.class);
            newlab.setCourseId(courseId);
            newlab.setLabId(labId);
            newlab.setThrus(lab.getThurs());
            labId += 1;
            newlab.setInstructorName(lab.getName());
            newlab.setInstructorEmail(lab.getEmail());

            iLabService.save(newlab);
        }

        log.info("Lab Update Complete");

    }

    public void semImport() {

        List<Course> courses = iCourseService.list(null);
        HashMap<String, Integer> map = new HashMap<>();

        Integer SemId = 1;

        for (Course course : courses) {
            String courseName = course.getSubject() + " " + course.getCatalog();
            Integer courseId = course.getCourseId();

            map.put(courseName, courseId);
        }

        List<NobesTimetableTable> sems = TableService.list(new LambdaQueryWrapper<NobesTimetableTable>()
                .eq(NobesTimetableTable::getComponent, "SEM"));

        for (NobesTimetableTable sem : sems) {
            String catalog = sem.getCatalog();
            String subject = sem.getSubject();

            String name = subject + " " + catalog;
            Integer courseId = map.get(name);

            Sem newsem = OrikaUtils.convert(sem, Sem.class);
            newsem.setCourseId(courseId);
            newsem.setSemId(SemId);
            newsem.setThrus(sem.getThurs());
            SemId += 1;
            newsem.setInstructorName(sem.getName());
            newsem.setInstructorEmail(sem.getEmail());

            iSemService.save(newsem);
        }

        log.info("Sem Update Complete");

    }
}
