package com.nobes.timetable.visualizer.logic.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nobes.timetable.visualizer.domain.NobesVisualizerCoursegroup;
import com.nobes.timetable.visualizer.service.INobesVisualizerCoursegroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class CourseGroupService {

    @Resource
    INobesVisualizerCoursegroupService iNobesVisualizerCoursegroupService;

    /**
     * For a given course and its accreditation unit map, find its course group
     * if the course is in this group, the value in that corresponding index will be set to "1", else "0"
     *
     * @param courseName the name of the course
     * @param AUCount    the accreditation unit map
     * @return an ArrayList indicating the course group
     */
    public ArrayList<String> getCourseGroup(String courseName, HashMap<String, String> AUCount) {

        // remove the brackets
        if (courseName.contains("(")) {
            courseName = courseName.substring(0, courseName.indexOf("("));
        }

        // initialize an empty list
        ArrayList<String> courseGroup = new ArrayList<>(Collections.nCopies(21, "0"));

        if (courseName.toUpperCase().contains("COMP")) {
            courseGroup.set(5, "1");
        } else if (courseName.toUpperCase().contains("PROG")) {
            courseGroup.set(6, "1");
        } else if (courseName.toUpperCase().contains("ITS")) {
            courseGroup.set(7, "1");
        } else if (courseName.toUpperCase().contains("WKEXP")) {
            courseGroup.set(4, "1");
        } else {
            // for general cases, first query the course group table to get the course group
            List<NobesVisualizerCoursegroup> coursegroups = iNobesVisualizerCoursegroupService.list(new LambdaQueryWrapper<NobesVisualizerCoursegroup>()
                    .eq(NobesVisualizerCoursegroup::getCourseName, courseName.toUpperCase()));

            for (NobesVisualizerCoursegroup group : coursegroups) {

                String groups = group.getCourseGroup();
                if (groups.contains("Math")) {
                    courseGroup.set(0, "1");
                } else if (groups.contains("Natural Sciences")) {
                    courseGroup.set(1, "1");
                } else if (groups.contains("Engineering Sciences")) {
                    courseGroup.set(2, "1");
                } else if (groups.contains("Engineering Design")) {
                    courseGroup.set(3, "1");
                } else if (groups.contains("Engineering Profession")) {
                    courseGroup.set(4, "1");
                } else if (groups.contains("Other")) {
                    courseGroup.set(8, "1");
                } else if (groups.contains("Computing Science")) {
                    courseGroup.set(9, "1");
                } else if (groups.contains("Mechatronics")) {
                    courseGroup.set(10, "1");
                } else if (groups.contains("SEMINARS")) {
                    courseGroup.set(11, "1");
                } else if (groups.contains("LABS")) {
                    courseGroup.set(12, "1");
                } else if (groups.contains("CODING")) {
                    courseGroup.set(13, "1");
                } else if (groups.contains("CAD")) {
                    courseGroup.set(14, "1");
                } else if (groups.contains("Group Work")) {
                    courseGroup.set(15, "1");
                } else if (groups.contains("Solid Mechanics")) {
                    courseGroup.set(16, "1");
                } else if (groups.contains("Thermo Fluids")) {
                    courseGroup.set(17, "1");
                } else if (groups.contains("Electrical")) {
                    courseGroup.set(18, "1");
                } else if (groups.contains("Control")) {
                    courseGroup.set(19, "1");
                } else if (groups.contains("Management")) {
                    courseGroup.set(20, "1");
                }
            }

            // if the course has accreditation in other fields, add that to its course group,
            // i.e., like Course A's AUCount: { "Math" : 44.7 }, then Course A is in Group: "Math"
            AUCount.forEach((key, value) -> {
                switch (key) {
                    case "Math":
                        courseGroup.set(0, "1");
                        break;
                    case "Natural Sciences":
                        courseGroup.set(1, "1");
                        break;
                    case "Complimentary Studies":
                        courseGroup.set(2, "1");
                        break;
                    case "Engineering Design":
                        courseGroup.set(3, "1");
                        break;
                    case "Engineering Science":
                        courseGroup.set(4, "1");
                        break;
                    case "Other":
                        courseGroup.set(5, "1");
                        break;
                    default:
                        break;
                }
            });
        }


        return courseGroup;
    }
}
