package com.nobes.timetable.calendar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgDTO {

    private String programName;

    private String termName;

    private String planName;

    private String componentId;

}
