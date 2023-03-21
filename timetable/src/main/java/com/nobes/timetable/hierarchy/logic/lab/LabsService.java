package com.nobes.timetable.hierarchy.logic.lab;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nobes.timetable.hierarchy.domain.NobesTimetableCourse;
import com.nobes.timetable.hierarchy.dto.CourseDTO;
import com.nobes.timetable.hierarchy.dto.ProgDTO;
import com.nobes.timetable.hierarchy.logic.MainService;
import com.nobes.timetable.hierarchy.service.INobesTimetableCourseService;
import com.nobes.timetable.hierarchy.vo.CourseVO;
import com.nobes.timetable.hierarchy.vo.LabVO;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LabsService {

    @Resource
    INobesTimetableCourseService courseSelectService;

    @Resource
    MainService cService;

    @Resource
    LabService labService;

    public HashMap getLabs(ProgDTO progDTO) throws Exception {

        String program_Name = progDTO.getProgramName();
        String term_Name = progDTO.getTermName();
        String planName = progDTO.getPlanName();

        String head;

        switch (program_Name) {
            case "Computer Engineering":
                head = "CE";
                break;
            case "Mechanical Engineering":
                head = "MECE";
                break;
            case "Electrial Engineering":
                head = "EE";
                break;
            case "Petroleum Engineering":
                head = "PE";
                break;
            case "Civil Engineering":
                head = "CIVIL";
                break;
            case "Engineering Physics":
                head = "ENGP";
                break;
            case "Mining Engineering":
                head = "MINI";
                break;
            case "Chemical Engineering":
                head = "CHE";
                break;
            case "Materials Engineering":
                head = "MATE";
                break;
            default:
                head = null;
        }

        String excelName = head + "Sequencing.xls";
        String path ="src/main/java/com/nobes/timetable/" + excelName;
        File file = new File(path);
        Workbook workbook = WorkbookFactory.create(file);

        ArrayList<String> names = new ArrayList<>();
        ArrayList<CourseVO> cours = new ArrayList<>();

        for (int i = 0; i < workbook.getNumberOfSheets(); i++ ) {
            if (workbook.getSheetAt(i).getSheetName().equalsIgnoreCase(planName)) {
                Sheet sheet = workbook.getSheetAt(i);

                int lastColNum = sheet.getRow(0).getLastCellNum();
                int lastRowNum = sheet.getLastRowNum();

                for (int j = 0; j < lastColNum; j++) {
                    if (sheet.getRow(0).getCell(j).getStringCellValue().equals(term_Name)) {
                        for (int z = 1; z < lastRowNum + 1; z++) {
                            String course = sheet.getRow(z).getCell(j).getStringCellValue();
                            names.add(course);
                        }
                    }
                }
            }
        }

        HashMap<String, ArrayList<LabVO>> labMap = new HashMap<>();

        for (int i = 0; i < names.size(); i++) {

            String courseName = names.get(i);

            if (courseName.equals("COMP")) {
                CourseVO courseVO = new CourseVO();
                courseVO.setSubject("COMP");
                courseVO.setCourseName("COMP");
                cours.add(courseVO);
                labMap.put(courseName,null);
            } else if (courseName.equals("ITS")) {
                CourseVO courseVO = new CourseVO();
                courseVO.setSubject("ITS");
                courseVO.setCourseName("ITS");
                cours.add(courseVO);
                labMap.put(courseName,null);
            } else if (courseName.equals("PROG 1")) {
                CourseVO courseVO = new CourseVO();
                courseVO.setSubject("PROG 1");
                courseVO.setCourseName("PROG 1");
                cours.add(courseVO);
                labMap.put(courseName,null);
            } else if (courseName.equals("PROG 2")) {
                CourseVO courseVO = new CourseVO();
                courseVO.setSubject("PROG 2");
                courseVO.setCourseName("PROG 2");
                cours.add(courseVO);
                labMap.put(courseName,null);
            } else {
                Pattern pattern = Pattern.compile("\\d+");
                Matcher matcher = pattern.matcher(courseName);
                matcher.find();
                String catalog = matcher.group(0);
                String subject = courseName.substring(0, courseName.indexOf(catalog.charAt(0)) - 1);

                NobesTimetableCourse course = courseSelectService.getOne(new LambdaQueryWrapper<NobesTimetableCourse>().eq(NobesTimetableCourse::getCatalog, catalog).eq(NobesTimetableCourse::getSubject, subject), false);

                // find course information
                CourseVO courseVO;

                if (course != null) {
                    courseVO = cService.getCourseObj(course);
                    cours.add(courseVO);
                }

                String coursename = subject + " " + catalog;
                CourseDTO courseDTO = new CourseDTO();
                courseDTO.setCourseName(coursename);

                ArrayList<LabVO> labs = labService.getLab(courseDTO);

                labMap.put(coursename, labs);

            }
        }

        return labMap;
    }
}
