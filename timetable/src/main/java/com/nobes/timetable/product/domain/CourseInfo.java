package com.nobes.timetable.product.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Data
@TableName(value = "courseinfo")
@AllArgsConstructor
@NoArgsConstructor
public class CourseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("AcadOrg")
    private String acadOrg;

    @TableField("Term")
    private String term;

    @TableField("ShortDesc")
    private String shortDesc;

    @TableField("ClassNbr")
    private String classNbr;

    @TableField("Subject")
    private String subject;

    @TableField("Catalog")
    private String catalog;

    @TableField("Component")
    private String component;

    @TableField("Sect")
    private String sect;

    @TableField("ClassStatus")
    private String classStatus;

    @TableField("Descr")
    private String descr;

    @TableField("CrsStatus")
    private String crsStatus;

    @TableField("FacilID")
    private String facilID;

    @TableField("Place")
    private String place;

    @TableField("Pat")
    private String pat;

    @TableField("StartDate")
    private String startDate;

    @TableField("EndDate")
    private String endDate;

    @TableField("HrsFrom")
    private String hrsFrom;

    @TableField("HrsTo")
    private String hrsTo;

    @TableField("Mon")
    private String mon;

    @TableField("Tues")
    private String tues;

    @TableField("Wed")
    private String wed;

    @TableField("Thurs")
    private String thurs;

    @TableField("Fri")
    private String fri;

    @TableField("name")
    private String name;

    @TableField("email")
    private String email;

    @TableField("ClassType")
    private String classType;

    @TableField("CapEnrl")
    private String capEnrl;

    @TableField("TotEnrl")
    private String totEnrl;

    @TableField("Campus")
    private String campus;

    @TableField("Location")
    private String location;

    @TableField("NoteNbr")
    private String noteNbr;

    @TableField("Note")
    private String note;

    @TableField("DescrRe")
    private String descrRe;

//    @TableField("descrbajs")
//    private String desc;

    @TableField("MaxUnits")
    private String maxUnits;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;


}
