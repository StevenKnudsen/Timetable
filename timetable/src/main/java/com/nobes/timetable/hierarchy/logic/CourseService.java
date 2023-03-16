package com.nobes.timetable.hierarchy.logic;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nobes.timetable.hierarchy.domain.NobesTimetableCourse;
import com.nobes.timetable.hierarchy.dto.ProgDTO;
import com.nobes.timetable.hierarchy.service.INobesTimetableCourseService;
import com.nobes.timetable.hierarchy.vo.CourseVO;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class CourseService {

    @Resource
    INobesTimetableCourseService courseSelectService;

    @Resource
    MainService cService;

    public ArrayList getCourses(ProgDTO progDTO) throws Exception {

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
                head = "ME";
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
            if (workbook.getSheetAt(i).getSheetName().equals(planName)) {
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

        for (int i = 0; i < names.size(); i++) {

            String courseName = names.get(i);

            if (!courseName.equals("COMP")) {
                Pattern pattern = Pattern.compile("\\d+");
                Matcher matcher = pattern.matcher(courseName);
                matcher.find();
                String catalog = matcher.group(0);
                String subject = courseName.substring(0, courseName.indexOf(catalog.charAt(0)) - 1);

                NobesTimetableCourse course = courseSelectService.getOne(new LambdaQueryWrapper<NobesTimetableCourse>().eq(NobesTimetableCourse::getCatalog, catalog).eq(NobesTimetableCourse::getSubject, subject), false);
                // find course information
                if (course != null) {
                    CourseVO courseVOObj = cService.getCourseObj(course);
                    cours.add(courseVOObj);
                }
            } else {
                CourseVO courseVO = new CourseVO();
                courseVO.setSubject("COMP");
                cours.add(courseVO);
            }
        }

        return cours;
    }

}
