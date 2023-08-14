package com.nobes.timetable.calendar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptimizedTableDTO {

    private List<List<List<String>>> timetable;

    private List<String> courseList;

    private List<List<String>> profs;

    private String term;

    private Integer startRow;

    private Integer endRow;
}
