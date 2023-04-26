package com.nobes.timetable.visualizer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * class for the RESTFul api parameters
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisualDTO {

    private String programName;

    private String planName;
}
