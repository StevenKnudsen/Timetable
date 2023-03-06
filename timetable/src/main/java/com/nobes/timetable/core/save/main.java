package com.nobes.timetable.core.save;

import com.nobes.timetable.core.save.service.ReqService;

import java.util.ArrayList;

public class main {

    public static void main(String[] args) throws Exception {
        String desc = "Feasibility study and detailed design of a project which requires students to exercise creative ability, to make assumptions and decisions based on synthesis of technical knowledge, and in general, devise new designs, rather than analyse existing ones. Prerequisites: MEC E 200, 330 or 331, 340, 360, 362, 370 or 371, 380. Corequisite: ENG M 310 (or ENG M 401).";

        ReqService reqService = new ReqService();
        ArrayList arrayList = reqService.pullPreReqs(desc);
        System.out.println(arrayList);
    }
}
