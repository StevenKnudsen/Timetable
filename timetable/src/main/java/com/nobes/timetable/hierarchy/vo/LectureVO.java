package com.nobes.timetable.hierarchy.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LectureVO {

    private String LecName;

    private String section;

    private String color;

    private String Place;

    private ArrayList times;

    private String InstructorName;

    private String InstructorEmail;

    private String Location;

}
