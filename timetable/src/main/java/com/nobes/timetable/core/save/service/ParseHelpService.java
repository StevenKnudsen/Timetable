package com.nobes.timetable.core.save.service;

import java.util.ArrayList;

public class ParseHelpService {

    public static int countNums(String s) {
        int numcounter = 0;
        for (int i = 0; i < s.length(); i++) {
            if (Character.isDigit(s.charAt(i))) {
                numcounter += 1;
            }
        }
        return numcounter;
    }

    public static String pullDept(ArrayList<String> preprocess, int index) {
        String str = preprocess.get(index);
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                String dept = str.substring(0, i - 1);
                return dept;
            }
        }
        return "-1";
    }
}
