package com.nobes.timetable.visualizer.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ReqVO {

    private ArrayList<String> preRe;

    private ArrayList<String> coRe;

    private ArrayList<String> postReq;

    private ArrayList<String> cocoRe;

}
