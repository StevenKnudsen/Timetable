package com.nobes.timetable.test;

import com.nobes.timetable.core.utils.OrikaUtils;

import java.util.ArrayList;

public class test {

    public static void main(String[] args) {

        splittest();

        System.out.println(converttest().getCourse());

        ArrayList<Object> objects = new ArrayList<>();
        Student student = new Student("xiang", "21", "1111111");
        objects.add(student);
        Teacher teacher = new Teacher();
        objects.add(teacher);
        System.out.println(objects);

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
