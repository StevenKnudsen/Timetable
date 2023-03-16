package com.nobes.timetable.save;


import com.nobes.timetable.hierarchy.logic.reqhelp.ReqService;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class SaveTest {

    @Test
    void excelReadTest() throws IOException {
        File file = new File("src/main/java/com/nobes/timetable/table.xls");
        Workbook workbook = WorkbookFactory.create(file);
        Sheet sheet = workbook.getSheetAt(0);
    }

    @Test
    void reReqTest() throws Exception {
        ReqService reqService = new ReqService();
        ArrayList<String> pre1 = reqService.pullPreReqs("Introduction to applied hydraulics; control volume methods, open channel hydraulics, pipe systems, pumps, distribution and collection system hydraulics and design. Prerequisite: CIV E 330. Corequisite: either CIV E 221 or ENV E 325.");
        ArrayList<String> co1 = reqService.pullCoReqs("Introduction to applied hydraulics; control volume methods, open channel hydraulics, pipe systems, pumps, distribution and collection system hydraulics and design. Prerequisite: CIV E 330. Corequisite: either CIV E 221 or ENV E 325.");
        ArrayList<String> pre2 = reqService.pullPreReqs("Moments of inertia. Kinematics and kinetics of rigid body motion, energy and momentum methods, impact, mechanical vibrations. Prerequisites: ENGG 130, EN PH 131 and MATH 101. There is a consolidated exam.");
        ArrayList<String> co2 = reqService.pullCoReqs("Moments of inertia. Kinematics and kinetics of rigid body motion, energy and momentum methods, impact, mechanical vibrations. Prerequisites: ENGG 130, EN PH 131 and MATH 101. There is a consolidated exam.");
        ArrayList<String> co3 = reqService.pullCoReqs("Feasibility study and detailed design of a project which requires students to exercise creative ability, to make assumptions and decisions based on synthesis of technical knowledge, and in general, devise new designs, rather than analyse existing ones. Prerequisites: MEC E 200, 330 or 331, 340, 360, 362, 370 or 371, 380. Corequisite: ENG M 310 (or ENG M 401).");


        List<String> expectedpre1 = Arrays.asList("CIV E 330");
        List<String> expectedco1 = Arrays.asList("CIV E 221 or ENV E 325");
        Assertions.assertEquals(pre1, expectedpre1);
        Assertions.assertEquals(co1, expectedco1);

        List<String> expectedpre2 = Arrays.asList("ENGG 130","EN PH 131","MATH 101");
        ArrayList<String> expectedco2 = new ArrayList<>();
        Assertions.assertEquals(pre2, expectedpre2);
        Assertions.assertEquals(co2, expectedco2);

        Arrays.asList("ENG M 310 or ENG M 401");

    }

}
