package com.nobes.timetable.hierarchy.logic;


import com.nobes.timetable.hierarchy.dto.ProgDTO;
import com.nobes.timetable.hierarchy.factory.UniComponentFactory;
import com.nobes.timetable.hierarchy.vo.CourseVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

@Service
@Slf4j
public class SequenceService {

    @Autowired
    UniComponentFactory uniComponentFactory;

    public HashMap handle(ProgDTO progDTO) throws Exception {

        String program_Name = progDTO.getProgramName();
        String term_Name = progDTO.getTermName();
        String planName = progDTO.getPlanName();
        String componentId = progDTO.getComponentId();

        String head;

        switch (program_Name) {
            case "Computer Engineering":
                head = "CE";
                break;
            case "Mechanical Engineering":
                head = "MECE";
                break;
            case "Electrical Engineering":
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

        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            if (workbook.getSheetAt(i).getSheetName().equalsIgnoreCase(planName)) {
                Sheet sheet = workbook.getSheetAt(i);

                int lastColNum = sheet.getRow(0).getLastCellNum();
                int lastRowNum = sheet.getLastRowNum();

                for (int j = 0; j < lastColNum; j++) {
                    if (sheet.getRow(0).getCell(j).getStringCellValue().equals(term_Name)) {

                        int z = 1;

                        while (!(sheet.getRow(z).getCell(j) == null)) {
                            String course = sheet.getRow(z).getCell(j).getStringCellValue();
                            names.add(course);

                            if (z + 1 == lastRowNum) break;

                            z += 1;
                        }

                        break;
                    }

                }
                break;
            }

        }

        String term = progDTO.getTermName().split(" ")[0].trim();

        for (String name : names) {
            if (name.contains("(")) {
                int position = names.indexOf(name);
                int index = name.indexOf("(");
                String newCourseName = name.substring(0, index);
                names.set(position, newCourseName);
            }
        }

        for (String name : names) {
            // TODO: if there is a or case, just take the first one, need to be fixed
            if (name.contains("or")) {
                int position = names.indexOf(name);
                String[] courses = name.split("\\s*[Oo][Rr]\\s*");
                names.set(position, courses[0].trim());
            }
        }

        HashMap map = uniComponentFactory.getStrategies(componentId).handle(names, term);
        return map;
    }
}
