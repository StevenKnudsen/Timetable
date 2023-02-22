package com.nobes.timetable.hierarchy.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class LabAndSem {

    private String ClassNbr;

    private String Name;

    private String Place;

    private String StartDate;

    private String EndDate;

    private String HrsFrom;

    private String HrsTo;

    private String Date;
}
