package com.nobes.timetable.hierarchy.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {

    private String AcadOrg;

    private String Term;

    private String subject;

    private String Catalog;

    private String Descr;

    private String ApprovedHrs;

    private String description;

    private String prerequisite;

    private String corequisite;

    private HashMap<String, Double> AU_Count;
}
