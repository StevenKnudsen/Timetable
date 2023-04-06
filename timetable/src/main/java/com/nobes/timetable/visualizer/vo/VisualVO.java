package com.nobes.timetable.visualizer.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class VisualVO {

    private String CourseName;

    private ArrayList<Integer> attribute;

    private String Title;

    private String description;

    private HashMap<String, String> AUCount;
}
