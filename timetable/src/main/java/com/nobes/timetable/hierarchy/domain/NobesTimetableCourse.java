package com.nobes.timetable.hierarchy.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 *   domain
 * </p>
 *
 * @author xiangpen
 * @since 2023-02-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class NobesTimetableCourse implements Serializable {

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
    @TableField("descp")
    private String description;

    @TableField("AccreditUnits")
    private String AccreditUnits;


}
