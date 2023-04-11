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
 * @since 2023-04-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("nobes_visualizer_grad")
public class NobesVisualizerGrad implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("courseName")
    private String courseName;

    @TableField("KnowledgeBase")
    private String KnowledgeBase;

    @TableField("ProblemAnalysis")
    private String ProblemAnalysis;

    @TableField("Investigation")
    private String Investigation;

    @TableField("Design")
    private String Design;

    @TableField("EngineeringTools")
    private String EngineeringTools;

    @TableField("IndivAndTeamwork")
    private String IndivAndTeamwork;

    @TableField("CommunicationSkills")
    private String CommunicationSkills;

    @TableField("Professionalism")
    private String Professionalism;

    @TableField("ImpactOnSociety")
    private String ImpactOnSociety;

    @TableField("EthicsandEquity")
    private String EthicsandEquity;

    @TableField("EconomicsAndMgt")
    private String EconomicsAndMgt;

    @TableField("LifeLongLearning")
    private String LifeLongLearning;


}
