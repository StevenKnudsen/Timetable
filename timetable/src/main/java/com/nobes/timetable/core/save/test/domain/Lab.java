package com.nobes.timetable.core.save.test.domain;

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
 * @since 2023-03-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Lab implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "LabId", type = IdType.AUTO)
    private Integer LabId;

    @TableField("ClassNbr")
    private String ClassNbr;

    @TableField("Subject")
    private String Subject;

    @TableField("Catalog")
    private String Catalog;

    @TableField("Component")
    private String Component;

    @TableField("Sect")
    private String Sect;

    @TableField("Place")
    private String Place;

    @TableField("StartDate")
    private String StartDate;

    @TableField("EndDate")
    private String EndDate;

    @TableField("HrsFrom")
    private String HrsFrom;

    @TableField("HrsTo")
    private String HrsTo;

    @TableField("InstructorName")
    private String InstructorName;

    @TableField("InstructorEmail")
    private String InstructorEmail;

    @TableField("Location")
    private String Location;

    @TableField("Mon")
    private String Mon;

    @TableField("Tues")
    private String Tues;

    @TableField("Wed")
    private String Wed;

    @TableField("Thrus")
    private String Thrus;

    @TableField("Fri")
    private String Fri;

    @TableField("CourseId")
    private Integer CourseId;


}
