package com.nobes.timetable.hierarchy.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {

    private String AcadOrg;

    private String Term;

    private String subject;

    private String Catalog;

    private String Descr;

    private String ApprovedHrs;

    private String descp;

    private String AccreditUnits;
}
