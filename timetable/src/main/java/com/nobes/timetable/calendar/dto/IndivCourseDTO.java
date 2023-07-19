package com.nobes.timetable.calendar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndivCourseDTO {
    private String courseName;

    private String term;
}
