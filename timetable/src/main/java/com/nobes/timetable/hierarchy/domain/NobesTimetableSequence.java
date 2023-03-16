package com.nobes.timetable.hierarchy.domain;

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
 * @since 2023-03-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class NobesTimetableSequence implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("ProgramName")
    private String ProgramName;

    @TableField("Term1")
    private String Term1;

    @TableField("Term2")
    private String Term2;

    @TableField("Term3")
    private String Term3;

    @TableField("Term4")
    private String Term4;

    @TableField("Term5")
    private String Term5;

    @TableField("Term6")
    private String Term6;

    @TableField("Term7")
    private String Term7;

    @TableField("Term8")
    private String Term8;

    @TableField("Term9")
    private String Term9;

    @TableField("Term10")
    private String Term10;

    @TableField("Term11")
    private String Term11;

    @TableField("Term12")
    private String Term12;

    @TableField("Term13")
    private String Term13;

    @TableField("Term14")
    private String Term14;

    @TableField("Term15")
    private String Term15;

    @TableField("Term16")
    private String Term16;


}
