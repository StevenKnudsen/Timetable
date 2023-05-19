package com.nobes.timetable.visualizer.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author xiangpen
 * @since 2023-04-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("nobes_visualizer_course")
public class NobesVisualizerCourse implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "Id", type = IdType.AUTO)
    private Integer Id;

    @TableField("Faculty")
    private String Faculty;

    @TableField("Department")
    private String Department;

    @TableField("CourseID")
    private String CourseID;

    @TableField("Subject")
    private String Subject;

    @TableField("Catalog")
    private String Catalog;

    @TableField("Title")
    private String Title;

    @TableField("EffDate")
    private String EffDate;

    @TableField("Status")
    private String Status;

    @TableField("CalendarPrint")
    private String CalendarPrint;

    @TableField("ProgUnits")
    private String ProgUnits;

    @TableField("EngineeringUnits")
    private String EngineeringUnits;

    @TableField("CalcFeeIndex")
    private String CalcFeeIndex;

    @TableField("ActualFeeIndex")
    private String ActualFeeIndex;

    @TableField("Duration")
    private String Duration;

    @TableField("AlphaHours")
    private String AlphaHours;

    @TableField("CourseDescription")
    private String CourseDescription;


    public boolean isNull() {
        return getProgUnits() == null || getCalcFeeIndex() == null || getDuration() == null || getAlphaHours() == null || getCourseDescription() == null;
    }

}
