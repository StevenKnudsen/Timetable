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
 * @since 2023-03-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "courseId", type = IdType.AUTO)
    private Integer courseId;

    @TableField("AcadOrg")
    private String AcadOrg;

    @TableField("Term")
    private String Term;

    private String subject;

    @TableField("Catalog")
    private String Catalog;

    @TableField("Descr")
    private String Descr;

    @TableField("Lec")
    private String Lec;

    @TableField("Lab")
    private String Lab;

    @TableField("Sem")
    private String Sem;


    @TableField("Prerequisite")
    private String pre;

    @TableField("Corequisite")
    private String co;

    @TableField("AccreditUnits")
    private String AccreditUnits;

    private String descp;


}
