package com.nobes.timetable.hierarchy.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Objects;

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

    private ArrayList times;

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

        if (InstructorName == null || semVO.getInstructorName() == null) {
            return Objects.equals(SemName, semVO.SemName) &&
                    Objects.equals(CourseId, semVO.CourseId) &&
                    Objects.equals(section, semVO.section) &&
                    Objects.equals(Place, semVO.Place) &&
                    Objects.equals(times, semVO.times) &&
                    Objects.equals(InstructorEmail, semVO.InstructorEmail);
        } else {
            return Objects.equals(SemName, semVO.SemName) &&
                    Objects.equals(CourseId, semVO.CourseId) &&
                    Objects.equals(section, semVO.section) &&
                    Objects.equals(Place, semVO.Place) &&
                    Objects.equals(times, semVO.times) &&
                    Objects.equals(InstructorName, semVO.InstructorName) &&
                    Objects.equals(InstructorEmail, semVO.InstructorEmail);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(SemName, CourseId, section, Place, times, InstructorName, InstructorEmail);
    }
}
