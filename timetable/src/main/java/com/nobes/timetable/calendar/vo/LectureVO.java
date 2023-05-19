package com.nobes.timetable.calendar.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * return type of the Lectures
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LectureVO {

    private String LecName;

    private Integer CourseId;

    private String section;

    private String Place;

    private String courseTitle;

    private ArrayList times;

    private String InstructorName;

    private String InstructorEmail;

    private String descp;

    private String Location;

    private HashMap<String, String> AUCount;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LectureVO lectureVO = (LectureVO) o;

        return Objects.equals(CourseId, lectureVO.CourseId) &&
                Objects.equals(times, lectureVO.times);

    }

    @Override
    public int hashCode() {

        return Objects.hash(CourseId, times);

    }
}
