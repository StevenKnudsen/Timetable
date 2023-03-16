package com.nobes.timetable.hierarchy.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class LabVO {

    private String LabName;

    private String section;

    private String color;

    private String ClassNbr;

    private String Place;

    private String StartDate;

    private String EndDate;

    private ArrayList times;

    private String InstructorName;

    private String InstructorEmail;
}
