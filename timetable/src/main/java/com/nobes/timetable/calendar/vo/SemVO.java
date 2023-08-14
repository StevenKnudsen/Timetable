package com.nobes.timetable.calendar.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Objects;

/**
 * return type of the Seminars
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SemVO {

    private String SemName;

    private Integer CourseId;

    private String section;

    private String color;

    private String Place;

    private ArrayList<String> times;

    private String InstructorName;

    private String InstructorEmail;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SemVO semVO = (SemVO) o;

        return Objects.equals(CourseId, semVO.CourseId) &&
                Objects.equals(times, semVO.times);

    }

    @Override
    public int hashCode() {
        return Objects.hash(CourseId, times);
    }
}
