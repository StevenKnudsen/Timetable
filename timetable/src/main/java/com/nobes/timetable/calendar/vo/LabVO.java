package com.nobes.timetable.calendar.vo;

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

    private String Place;

    private ArrayList<String> times;

    private String InstructorName;

    private String InstructorEmail;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LabVO labVO = (LabVO) o;

        return Objects.equals(CourseId, labVO.CourseId) &&
                Objects.equals(times, labVO.times);
    }

    @Override
    public int hashCode() {

        return Objects.hash( CourseId, times);
    }
}

