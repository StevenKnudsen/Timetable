package com.nobes.timetable.hierarchy.logic;

import com.nobes.timetable.hierarchy.dto.ProgramDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@Service
@Slf4j
public class PlanService {

    public HashMap<String, ArrayList> getPlan(ProgramDTO programDTO) throws IOException {

        HashMap<String, ArrayList> programMap = new HashMap<>();
        String program_Name = programDTO.getProgramName();
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
        String path ="src/main/java/com/nobes/timetable/" + excelName;
        File file = new File(path);
        Workbook workbook = WorkbookFactory.create(file);
        int numberOfSheets = workbook.getNumberOfSheets();

        for (int i = 0; i < numberOfSheets; i++) {
            ArrayList<String> terms = new ArrayList<>();
            String plan = workbook.getSheetName(i);
            Row row = workbook.getSheetAt(i).getRow(0);
            
            for (int j = 0; j < row.getLastCellNum(); j++) {
                String term = row.getCell(j).getStringCellValue();
                terms.add(term);

            }

            programMap.put(plan, terms);
        }

        return programMap;
    }
}
