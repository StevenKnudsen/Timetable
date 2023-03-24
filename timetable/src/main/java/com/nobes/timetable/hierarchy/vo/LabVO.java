package com.nobes.timetable.hierarchy.vo;

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
public class LabVO {

    private String LabName;

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

        LabVO labVO = (LabVO) o;

        if (InstructorName == null || labVO.getInstructorName() == null) {
            return Objects.equals(LabName, labVO.LabName) &&
                    Objects.equals(CourseId, labVO.CourseId) &&
                    Objects.equals(section, labVO.section) &&
                    Objects.equals(Place, labVO.Place) &&
                    Objects.equals(times, labVO.times) &&
                    Objects.equals(InstructorEmail, labVO.InstructorEmail);
        } else {
            return Objects.equals(LabName, labVO.LabName) &&
                    Objects.equals(CourseId, labVO.CourseId) &&
                    Objects.equals(section, labVO.section) &&
                    Objects.equals(Place, labVO.Place) &&
                    Objects.equals(times, labVO.times) &&
                    Objects.equals(InstructorName, labVO.InstructorName) &&
                    Objects.equals(InstructorEmail, labVO.InstructorEmail);
        }


    }

    @Override
    public int hashCode() {
        return Objects.hash(LabName, CourseId, section, Place, times, InstructorName, InstructorEmail);
    }
}
