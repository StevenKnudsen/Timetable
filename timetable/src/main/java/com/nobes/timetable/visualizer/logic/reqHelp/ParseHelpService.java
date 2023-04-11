package com.nobes.timetable.visualizer.logic.reqHelp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ParseHelpService {

    public static int countNums(String str) {
        int numCounter = 0;
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                numCounter++;
            }
        }
        return numCounter;
    }

    public static String pullDept(List<String> reqlist, int indx) {
        for (int n = 0; n < reqlist.get(indx).length(); n++) {

            if (Character.isDigit(reqlist.get(indx).charAt(n))) {
                String dept = reqlist.get(indx).substring(0, n - 1);
                return dept;
            }
        }
        return "-1";
    }

}
