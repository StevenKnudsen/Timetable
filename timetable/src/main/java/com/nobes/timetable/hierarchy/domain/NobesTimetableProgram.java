package com.nobes.timetable.hierarchy.domain;

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
 * @since 2023-02-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class NobesTimetableProgram implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("AcadOrg")
    private String AcadOrg;

    @TableField("Term")
    private String Term;

    @TableField("ShortDesc")
    private String ShortDesc;

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

    @TableField("ClassStatus")
    private String ClassStatus;

    @TableField("Descr")
    private String Descr;

    @TableField("CrsStatus")
    private String CrsStatus;

    @TableField("FacilID")
    private String FacilID;

    @TableField("Place")
    private String Place;

    @TableField("Pat")
    private String Pat;

    @TableField("StartDate")
    private String StartDate;

    @TableField("EndDate")
    private String EndDate;

    @TableField("HrsFrom")
    private String HrsFrom;

    @TableField("HrsTo")
    private String HrsTo;

    @TableField("Mon")
    private String Mon;

    @TableField("Tues")
    private String Tues;

    @TableField("Wed")
    private String Wed;

    @TableField("Thurs")
    private String Thurs;

    @TableField("Fri")
    private String Fri;

    private String name;

    private String email;

    @TableField("ClassType")
    private String ClassType;

    @TableField("CapEnrl")
    private String CapEnrl;

    @TableField("TotEnrl")
    private String TotEnrl;

    @TableField("Campus")
    private String Campus;

    @TableField("Location")
    private String Location;

    @TableField("NoteNbr")
    private String NoteNbr;

    @TableField("Note")
    private String Note;

    @TableField("DescrRe")
    private String DescrRe;

    private String descrbajs;

    @TableField("MaxUnits")
    private String MaxUnits;

    private Integer id;


}
