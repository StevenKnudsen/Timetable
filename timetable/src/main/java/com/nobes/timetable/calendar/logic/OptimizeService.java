package com.nobes.timetable.calendar.logic;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nobes.timetable.calendar.domain.NobesTimetableCourse;
import com.nobes.timetable.calendar.dto.CourseIdDTO;
import com.nobes.timetable.calendar.dto.OptimizedTableDTO;
import com.nobes.timetable.calendar.factory.strategies.lab.LabService;
import com.nobes.timetable.calendar.factory.strategies.lecture.LecService;
import com.nobes.timetable.calendar.factory.strategies.seminar.SemService;
import com.nobes.timetable.calendar.service.INobesTimetableCourseService;
import com.nobes.timetable.calendar.vo.LabVO;
import com.nobes.timetable.calendar.vo.LectureVO;
import com.nobes.timetable.calendar.vo.SemVO;
import com.nobes.timetable.core.entity.ResultBody;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

@Service
@Slf4j
@Data
public class OptimizeService {

    private List<List<List<String>>> timetable;
    private List<String> courseList = new ArrayList<>();

    private LinkedHashMap<String, LinkedHashMap<String, ArrayList<ArrayList<List<Integer>>>>> coordinateMap = new LinkedHashMap<>();

    @Resource
    INobesTimetableCourseService nobesCourseService;

    @Resource
    LabService laboratoryService;

    @Resource
    SemService seminarService;

    @Resource
    LecService lectureService;

    public ResultBody optimizedGenerate(OptimizedTableDTO optimizedTableDTO) throws Exception {
        List<List<List<String>>> timetable = optimizedTableDTO.getTimetable();
        setTimetable(timetable);
        List<String> inputCourseList = optimizedTableDTO.getCourseList();
        setCourseList(inputCourseList);
        String term = optimizedTableDTO.getTerm();
        Integer startRow = optimizedTableDTO.getStartRow();
        Integer endRow = optimizedTableDTO.getEndRow();
        String appliedTerm = "";

        if (term.toLowerCase().contains("fall")) {
            appliedTerm = "Fall";
        } else if (term.toLowerCase().contains("winter")) {
            appliedTerm = "Winter";
        } else if (term.toLowerCase().contains("summer")) {
            appliedTerm = "Summer";
        } else if (term.toLowerCase().contains("spring")) {
            appliedTerm = "Spring";
        }

        LinkedHashMap<String, LinkedHashMap<String, ArrayList<ArrayList<List<Integer>>>>> courseCoordinateMap = new LinkedHashMap<>();

        for (String course : courseList) {
            String clearCourseName = course.replaceAll(" Lab", "").replaceAll(" Sem", "");
            String[] parts = clearCourseName.split(" ");
            String subject;
            String catalog;


            if (parts.length < 3) {
                // "MATH 100"
                subject = parts[0].trim();
                catalog = parts[1].trim();
            } else {
                // "EN PH 131"
                subject = parts[0] + " " + parts[1];
                catalog = parts[2];
            }

            if (catalog.toLowerCase().contains("lab")) {
                catalog = catalog.substring(0, catalog.toLowerCase().indexOf(" lab"));
            } else if (catalog.toLowerCase().contains("sem")) {
                catalog = catalog.substring(0, catalog.toLowerCase().indexOf(" sem"));
            }

            NobesTimetableCourse courseInfo = nobesCourseService.getOne(new LambdaQueryWrapper<NobesTimetableCourse>()
                    .eq(NobesTimetableCourse::getAppliedTerm, appliedTerm)
                    .eq(NobesTimetableCourse::getSubject, subject)
                    .eq(NobesTimetableCourse::getCatalog, catalog), false);

            Integer courseId = courseInfo.getCourseId();

            CourseIdDTO courseIdDTO = new CourseIdDTO();
            courseIdDTO.setCourseId(courseId);

            // TODO: Need to deal with preferred profs, do not have enough info so far

            //            List<List<String>> profs = optimizedTableDTO.getProfs();
            //            List<String> preferredProf = profs.get(courseList.indexOf(course));
            //            if (preferredProf.contains("ALL")) {}

            HashMap<String, ArrayList<String>> optionsMap = new HashMap<>();

            if (course.toLowerCase().contains("lab")) {
                ArrayList<LabVO> labs = laboratoryService.getLab(courseIdDTO);
                optionsMap = (HashMap<String, ArrayList<String>>) labs.stream().collect(Collectors.toMap(lab -> lab.getLabName().replaceAll("LAB", "Lab"), LabVO::getTimes));
            } else if (course.toLowerCase().contains("sem")) {
                ArrayList<SemVO> sems = seminarService.getSem(courseIdDTO);
                optionsMap = (HashMap<String, ArrayList<String>>) sems.stream().collect(Collectors.toMap(sem -> sem.getSemName().replaceAll("SEM", "Sem"), SemVO::getTimes));
            } else {
                ArrayList<LectureVO> lectures = lectureService.getLecture(courseIdDTO);
                optionsMap = (HashMap<String, ArrayList<String>>) lectures.stream().collect(Collectors.toMap(lec -> lec.getLecName().replaceAll(" LEC", ""), LectureVO::getTimes));
            }

            LinkedHashMap<String, ArrayList<ArrayList<List<Integer>>>> coordinates = getCoordinates(optionsMap, startRow, endRow);

            courseCoordinateMap.put(course, coordinates);
        }

        setCoordinateMap(courseCoordinateMap);

        // if all the courses can be filled into the schedule
        if (generateSchedule(0)) {
            return ResultBody.success(timetable);
        } else {
            return ResultBody.success("Random Generate failed");
        }

    }

    /**
     * use recursion to generate the timetable
     * */
    public Boolean generateSchedule(Integer courseIndex) throws Exception {

        // iterate all the courses and exit
        List<String> courseList = getCourseList();
        if (courseIndex == courseList.size()) {
            return true;
        }

        LinkedHashMap<String, LinkedHashMap<String, ArrayList<ArrayList<List<Integer>>>>> coordinateMap = getCoordinateMap();

        LinkedHashMap<String, ArrayList<ArrayList<List<Integer>>>> courseOptions = coordinateMap.get(courseList.get(courseIndex));

        // key is one section about a course
        for (String key : courseOptions.keySet()) {
            ArrayList<ArrayList<List<Integer>>> sectionTimePeriodInAWeek = courseOptions.get(key);

            if (sectionTimePeriodInAWeek.size() != 0) {
                boolean isConflict = false;
                int sectionTimePeriodInAWeekSize = sectionTimePeriodInAWeek.size();
                int startIndex = 0;

                while (!isConflict) {
                    ArrayList<List<Integer>> sectionTimePeriodInADay = sectionTimePeriodInAWeek.get(startIndex);
                    for (List<Integer> halfAnHour : sectionTimePeriodInADay) {
                        if (checkConflict(halfAnHour.get(0), halfAnHour.get(1))) {
                            isConflict = true;
                            break;
                        }
                    }

                    if (startIndex + 1 < sectionTimePeriodInAWeekSize) {
                        startIndex++;
                    } else break;
                }


                if (!isConflict) {
                    putInTimeTable(key, sectionTimePeriodInAWeek);

                    if(generateSchedule(courseIndex + 1)) {
                        return true;
                    }

                    removeFromTimeTable(key, sectionTimePeriodInAWeek);
                }
            } else {
                return false;
            }
        }

        return false;
    }

    /**
     * check time conflict, if there is a conflict return true else false
     * */
    public Boolean checkConflict(Integer xCoordinate, Integer yCoordinate) {
        List<List<List<String>>> timetable = getTimetable();
        List<String> nullList = Arrays.asList(null, "", null);
        return !timetable.get(xCoordinate).get(yCoordinate).equals(nullList);
    }

    /**
     * put the selected time period into timetable
     * */
    public void putInTimeTable(String key, ArrayList<ArrayList<List<Integer>>> sectionTimePeriodInAWeek) {
        List<List<List<String>>> timetable = getTimetable();

        for (ArrayList<List<Integer>> sectionTimePeriodInADay : sectionTimePeriodInAWeek) {
            List<String> randomGenerateSectionCell = Arrays.asList("#275D38", "", key);

            for (List<Integer> sectionTimePeriodInHalfHour : sectionTimePeriodInADay) {
                Integer xIndex = sectionTimePeriodInHalfHour.get(0);
                Integer yIndex = sectionTimePeriodInHalfHour.get(1);

                timetable.get(xIndex).set(yIndex, randomGenerateSectionCell);
            }
        }

        setTimetable(timetable);
    }

    /**
     * check time conflict, if there is a conflict return true else false
     * */
    public void removeFromTimeTable(String key, ArrayList<ArrayList<List<Integer>>> sectionTimePeriodInAWeek) {
        List<List<List<String>>> timetable = getTimetable();

        for (ArrayList<List<Integer>> sectionTimePeriodInADay : sectionTimePeriodInAWeek) {
            List<String> emptyCell = Arrays.asList(null, "", null);

            for (List<Integer> sectionTimePeriodInHalfHour : sectionTimePeriodInADay) {
                Integer xIndex = sectionTimePeriodInHalfHour.get(0);
                Integer yIndex = sectionTimePeriodInHalfHour.get(1);

                timetable.get(xIndex).set(yIndex, emptyCell);
            }
        }

        setTimetable(timetable);
    }


    /**
     * change the time period to coordinates and save to a map where section is the key and the coordinates are the value
     * */
    public LinkedHashMap<String, ArrayList<ArrayList<List<Integer>>>> getCoordinates(HashMap<String, ArrayList<String>> optionsMap, Integer startRow, Integer endRow) throws Exception {

        LinkedHashMap<String, ArrayList<ArrayList<List<Integer>>>> courseOptions = new LinkedHashMap<>();

        for (String key : optionsMap.keySet()) {
            ArrayList<ArrayList<List<Integer>>> optionCoordinates = new ArrayList<>();
            ArrayList<String> option = optionsMap.get(key);

            for (String timePeriod : option) {
                ArrayList<List<Integer>> coordinates = new ArrayList<>();
                String date = timePeriod.split("_")[0].trim();
                String time = timePeriod.split("_")[1].trim();

                int yIndex = dateProcess(date);
                int xStartIndex = startTimeProcess(time);
                int xEndIndex = endTimeProcess(time);

                if (xStartIndex <= startRow) break;
                if (xEndIndex >= endRow) break;

                for (int i = xStartIndex; i <= xEndIndex; i++) {
                    List<Integer> coordinate = Arrays.asList(i, yIndex);
                    coordinates.add(coordinate);
                }

                optionCoordinates.add(coordinates);
            }

            courseOptions.put(key, optionCoordinates);
        }

        return courseOptions;
    }

    /**
     * get y index according to the date
     * */
    public int dateProcess(String date) {
        int yCoordinate;

        switch (date.toUpperCase()) {
            case "MON":
                yCoordinate = 0;
                break;
            case "TUE":
                yCoordinate = 1;
                break;
            case "WED":
                yCoordinate = 2;
                break;
            case "THU":
                yCoordinate = 3;
                break;
            case "FRI":
                yCoordinate = 4;
                break;
            default:
                yCoordinate = 0;
        }

        return yCoordinate;
    }

    /**
     * get the start index according to the course start time
     * */
    public int startTimeProcess(String time) {
        int startIndex = 0;

        String startTime = time.split("-")[0];
        int startInHour = Integer.parseInt(startTime.split(":")[0]);
        int startInMin = Integer.parseInt(startTime.split(":")[1]);

        if (startInMin == 0) {
            startIndex = (startInHour - 8) * 2;
        } else if (startInMin > 0 && startInMin < 30) {
            startIndex = (int) ((startInHour + 0.5 - 8) * 2);
        } else if (startInMin > 30) {
            startIndex = (startInHour + 1 - 8) * 2;
        }

        return startIndex;
    }

    /**
     * get the end index according to the course start time
     * */
    public int endTimeProcess(String time) {
        int endIndex = 0;

        String endTime = time.split("-")[1];
        int endInHour = Integer.parseInt(endTime.split(":")[0]);
        int endInMin = Integer.parseInt(endTime.split(":")[1]);

        if (endInMin == 0) {
            endIndex = (int) ((endInHour - 0.5 - 8) * 2);
        } else if (endInMin > 0 && endInMin < 30) {
            endIndex = (endInHour - 8) * 2;
        } else if (endInMin > 30) {
            endIndex = (int) ((endInHour + 0.5 - 8) * 2);
        }

        return endIndex;
    }
}