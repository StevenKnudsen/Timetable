package com.nobes.timetable.calendar.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author xiangpen
 * @since 2023-02-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class NobesTimetableAu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("courseName")
    private String courseName;

    private String title;

    private String faculty;

    private String catagory;

    private String credit;

    private String weight;

    private String lec;

    private String sem;

    private String lab;

    private String totalAu;

    @TableField("Math")
    private String Math;

    @TableField("NaturalSciences")
    private String NaturalSciences;

    @TableField("ComplimentaryStudies")
    private String ComplimentaryStudies;

    @TableField("EngineeringScience")
    private String EngineeringScience;

    @TableField("EngineeringDesign")
    private String EngineeringDesign;

    @TableField("Other")
    private String Other;

    @TableField("ESsp")
    private String ESsp;

    @TableField("EDsp")
    private String EDsp;

    @TableField("PEng")
    private String PEng;

    @TableField("EIT")
    private String eit;


}
