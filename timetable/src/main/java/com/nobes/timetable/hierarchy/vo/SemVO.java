package com.nobes.timetable.hierarchy.vo;

import com.baomidou.mybatisplus.annotation.TableField;
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
public class SemVO {

    private String SemName;

    private String section;

    private String color;

    private String Place;

    private ArrayList times;

    private String InstructorName;

    private String InstructorEmail;
}
