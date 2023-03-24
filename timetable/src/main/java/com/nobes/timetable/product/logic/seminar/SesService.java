package com.nobes.timetable.product.logic.seminar;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nobes.timetable.hierarchy.domain.NobesTimetableCourse;
import com.nobes.timetable.hierarchy.dto.CourseIdDTO;
import com.nobes.timetable.hierarchy.dto.ProgDTO;
import com.nobes.timetable.hierarchy.service.INobesTimetableCourseService;
import com.nobes.timetable.hierarchy.vo.CourseVO;
import com.nobes.timetable.hierarchy.vo.SemVO;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SesService {

    @Resource
    INobesTimetableCourseService courseSelectService;

    @Resource
    SService sService;

    public HashMap getSems(ProgDTO progDTO) throws Exception {

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
        String path = "src/main/java/com/nobes/timetable/" + excelName;
        File file = new File(path);
        Workbook workbook = WorkbookFactory.create(file);

        ArrayList<String> names = new ArrayList<>();
        ArrayList<CourseVO> cours = new ArrayList<>();

        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
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

        HashMap<String, ArrayList<SemVO>> semMap = new HashMap<>();

        for (int i = 0; i < names.size(); i++) {

            String courseName = names.get(i);

            if (courseName.equals("COMP")) {
                CourseVO courseVO = new CourseVO();
                courseVO.setSubject("COMP");
                courseVO.setCourseName("COMP");
                cours.add(courseVO);
                semMap.put(courseName, null);
            } else if (courseName.equals("ITS")) {
                CourseVO courseVO = new CourseVO();
                courseVO.setSubject("ITS");
                courseVO.setCourseName("ITS");
                cours.add(courseVO);
                semMap.put(courseName, null);
            } else if (courseName.equals("PROG 1")) {
                CourseVO courseVO = new CourseVO();
                courseVO.setSubject("PROG 1");
                courseVO.setCourseName("PROG 1");
                cours.add(courseVO);
                semMap.put(courseName, null);
            } else if (courseName.equals("PROG 2")) {
                CourseVO courseVO = new CourseVO();
                courseVO.setSubject("PROG 2");
                courseVO.setCourseName("PROG 2");
                cours.add(courseVO);
                semMap.put(courseName, null);
            } else if (courseName.contains("or")) {
                // TODO: if there is a or case, just take the first one, need to be fixed

                String[] courses = courseName.split("\\s*[Oo][Rr]\\s*");

                String[] strings = courses[1].trim().split("\\s(?=\\d)");
                ;

                String catalog = strings[1].trim();
                String subject = strings[0].trim();

                NobesTimetableCourse course = courseSelectService.getOne(new LambdaQueryWrapper<NobesTimetableCourse>()
                        .eq(NobesTimetableCourse::getCatalog, catalog)
                        .eq(NobesTimetableCourse::getSubject, subject), false);

                Integer courseId = course.getCourseId();

                if (!course.getSem().equals("0") && !course.getSem().equals("UNASSIGNED")) {
                    String coursename = subject + " " + catalog;
                    CourseIdDTO courseIdDTO = new CourseIdDTO();
                    courseIdDTO.setCourseId(courseId);

                    ArrayList<SemVO> sems = sService.getSem(courseIdDTO);

                    semMap.put(coursename, sems);
                }

            } else {
                Pattern pattern = Pattern.compile("\\d+");
                Matcher matcher = pattern.matcher(courseName);
                matcher.find();
                String catalog = matcher.group(0);
                String subject = courseName.substring(0, courseName.indexOf(catalog.charAt(0)) - 1);

                NobesTimetableCourse course = courseSelectService.getOne(new LambdaQueryWrapper<NobesTimetableCourse>().eq(NobesTimetableCourse::getCatalog, catalog).eq(NobesTimetableCourse::getSubject, subject), false);

                Integer courseId = course.getCourseId();

                if (!course.getSem().equals("0") && !course.getSem().equals("UNASSIGNED")) {
                    String coursename = subject + " " + catalog;
                    CourseIdDTO courseIdDTO = new CourseIdDTO();
                    courseIdDTO.setCourseId(courseId);

                    ArrayList<SemVO> sems = sService.getSem(courseIdDTO);

                    semMap.put(coursename, sems);
                }
            }
        }

        return semMap;
    }
}
