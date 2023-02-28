package com.nobes.timetable.hierarchy.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lecture {

    private String ClassNbr;

    private String LecName;

    private String Place;

    private String StartDate;

    private String EndDate;

    private String TimeDuration;

    private String InstructorName;

    private String InstructorEmail;

    private String Location;

    private String Weekdays;
}
