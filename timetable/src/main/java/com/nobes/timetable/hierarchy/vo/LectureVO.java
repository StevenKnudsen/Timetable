package com.nobes.timetable.hierarchy.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LectureVO {

    private String LecName;

    private Integer CourseId;

    private String section;

    private String Place;

    private ArrayList times;

    private String InstructorName;

    private String InstructorEmail;

    private String Location;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LectureVO lectureVO = (LectureVO) o;

        if (InstructorName == null || lectureVO.getInstructorName() == null) {
            return Objects.equals(LecName, lectureVO.LecName) &&
                    Objects.equals(CourseId, lectureVO.CourseId) &&
                    Objects.equals(section, lectureVO.section) &&
                    Objects.equals(Place, lectureVO.Place) &&
                    Objects.equals(times, lectureVO.times) &&
                    Objects.equals(InstructorEmail, lectureVO.InstructorEmail);
        } else {
            return Objects.equals(LecName, lectureVO.LecName) &&
                    Objects.equals(CourseId, lectureVO.CourseId) &&
                    Objects.equals(section, lectureVO.section) &&
                    Objects.equals(Place, lectureVO.Place) &&
                    Objects.equals(times, lectureVO.times) &&
                    Objects.equals(InstructorName, lectureVO.InstructorName) &&
                    Objects.equals(InstructorEmail, lectureVO.InstructorEmail);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(LecName, CourseId, section, Place, times, InstructorName, InstructorEmail, Location);
    }
}
