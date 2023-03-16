package com.nobes.timetable.hierarchy.logic.reqhelp;

import java.util.ArrayList;
import java.util.Random;

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

    public static String generateRandomColorCode() {
        Random random = new Random();
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);

        String colorCode = String.format("#%02x%02x%02x", red, green, blue);
        return colorCode;
    }
}
