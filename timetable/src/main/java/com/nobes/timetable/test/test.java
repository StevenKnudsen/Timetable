package com.nobes.timetable.test;

import com.nobes.timetable.core.utils.OrikaUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class test {

    public test() {
    }

    public static void main(String[] args) {

        splittest();

        System.out.println(converttest().getCourse());

        test test1 = new test();

        HashMap maptest = test1.maptest();
        HashMap map = test1.maptest1();
        System.out.println(maptest);
        System.out.println(map);

    }

    private HashMap maptest() {
        HashMap<String, String> map = new HashMap<>();
        map.put("Xiangpeng", "Jiage");
        map.put("Ji", "Han");
        map.put("husband", "wife");
        return map;
    }

    private HashMap maptest1() {
        HashMap<String, Map<String, String>> map = new HashMap<>();
        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("Xiangpeng", "Jiage");
        hashMap.put("Ji", "Han");
        hashMap.put("husband", "wife");
        map.put("key",hashMap);
        return map;
    }

    private static Teacher converttest() {
        Student student = new Student("xiang", "21", "1111111");
        Teacher convert = OrikaUtils.convert(student, Teacher.class);
        return convert;
    }

    private static void splittest() {
        String case1 = "3-1-0";
        String case2 = ".75-.75-0";
        String case3 = "0-0-7";
        String case4 = "1 Day";
        String case5 = "3-3S/2-0";
        String case6 = "UNASSIGNED";
        String case7 = "0-0-7";
        String case8 = "0-1S-6";

        Boolean hasLec = false;
        Boolean hasLab = false;
        Boolean hasSem = false;
        String zero = "0";

        if (case6.contains("-")) {
            String[] split = case6.split("-");

            if (!split[0].equals(zero)) {
                hasLec = true;
            }

            if (!split[1].equals(zero)) {
                hasSem = true;
            }

            if (!split[2].equals(zero)) {
                hasLab = true;
            }
        } else {
            hasLec = true;
        }

        System.out.println("isLec = " + hasLec);
        System.out.println("isSem = " + hasSem);
        System.out.println("isLab = " + hasLab);
    }
}
