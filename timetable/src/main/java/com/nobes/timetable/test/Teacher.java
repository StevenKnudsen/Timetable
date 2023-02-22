package com.nobes.timetable.test;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Teacher {

    private String name;

    private String age;

    private String course;

    private String teachingyear;

    public Teacher(String name, String age) {
        this.name = name;
        this.age = age;
    }
}
