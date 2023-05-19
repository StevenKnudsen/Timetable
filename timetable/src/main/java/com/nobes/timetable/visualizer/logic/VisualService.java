package com.nobes.timetable.visualizer.logic;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nobes.timetable.calendar.domain.NobesTimetableAu;
import com.nobes.timetable.calendar.domain.NobesTimetableSequence;
import com.nobes.timetable.calendar.service.INobesTimetableAuService;
import com.nobes.timetable.calendar.service.INobesTimetableSequenceService;
import com.nobes.timetable.visualizer.domain.NobesVisualizerCourse;
import com.nobes.timetable.visualizer.dto.VisualDTO;
import com.nobes.timetable.visualizer.logic.reqHelp.ReqService;
import com.nobes.timetable.visualizer.logic.util.CourseGroupService;
import com.nobes.timetable.visualizer.logic.util.GradAttService;
import com.nobes.timetable.visualizer.service.INobesVisualizerCourseService;
import com.nobes.timetable.visualizer.vo.VisualVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class is service for the visualizer that provide several functions to get all the courses information
 */
@Service
@Slf4j
public class VisualService {

    /**
     * inject beans
     */
    @Resource
    INobesTimetableSequenceService sService;

    @Resource
    INobesVisualizerCourseService iNobesVisualizerCourseService;

    @Resource
    INobesTimetableAuService iTimetableAuService;

    @Resource
    ReqService reqService;

    @Autowired
    GradAttService gradAttService;

    @Autowired
    CourseGroupService courseGroupService;

    /**
     * This function is used to find all the course information in the given program and given term
     * i.e., prerequisites, corequisites, grad attributes, course description
     *
     * @param visualDTO visualDTO containing the program name and term name
     * @return a Hashmap which keys are course name and values are VisualVO objects that contains course info
     */
    public HashMap getCourses(VisualDTO visualDTO) throws Exception {
        HashMap<String, ArrayList<VisualVO>> programMap = new HashMap<>();

        String planName = visualDTO.getPlanName();
        String programName = visualDTO.getProgramName();

        // get all the terms and courses by the given program name and plan name
        List<NobesTimetableSequence> courseList = sService.list(new LambdaQueryWrapper<NobesTimetableSequence>()
                .eq(NobesTimetableSequence::getProgramName, programName)
                .eq(NobesTimetableSequence::getPlanName, planName));


        // group by the term names and map each course name to a list associated with that terms
        Map<String, List<String>> map = courseList.stream()
                .collect(Collectors.groupingBy(NobesTimetableSequence::getTermName,
                        Collectors.mapping(NobesTimetableSequence::getCourseName, Collectors.toList())));

        Set<String> keySet = map.keySet();

        ArrayList<String> orderList = new ArrayList<>();

        // remove duplicate terms
        for (NobesTimetableSequence sequence : courseList) {
            if (!orderList.contains(sequence.getTermName())) {
                orderList.add(sequence.getTermName());
            }
        }

        for (String key : keySet) {
            List<String> value = map.get(key);

            ArrayList<VisualVO> visualVOS = new ArrayList<>();

            for (String courseName : value) {

                if (courseName.contains("COMP") || courseName.contains("ITS") || courseName.contains("PROG")) {

                    // if the course is a selective or elective course
                    ArrayList gradAtts = gradAttService.getGradAtts(courseName);

                    ArrayList<String> courseGroup = courseGroupService.getCourseGroup(courseName, null);

                    VisualVO visualVO = new VisualVO();
                    visualVO.setCourseName(courseName)
                            .setTitle(courseName.contains("COMP") ? "Complementary Studies" :
                                    courseName.contains("ITS") ? "ITS Electives" :
                                            courseName.contains("PROG") ? "Program/Technical Electives" :
                                                    "")
                            .setAttribute(gradAtts)
                            .setGroup(courseGroup)
                            .setDescription(courseName.equals("COMP") ? "A Complementary Studies of the student's choice. Please consult the calendar for more information." :
                                    courseName.equals("ITS") ? "A ITS Electives of the student's choice. Please consult the calendar for more information." :
                                            courseName.equals("PROG") ? "A program/technical elective of the student's choice. Please consult the calendar for more information." :
                                                    "");

                    visualVOS.add(visualVO);

                } else if (courseName.contains("WKEXP")) {
                    // if the course is a coop term course like WKEXP 901
                    Pattern pattern = Pattern.compile("\\d+");
                    Matcher matcher = pattern.matcher(courseName);
                    matcher.find();
                    String catalog = matcher.group(0);
                    String subject = courseName.substring(0, courseName.indexOf(catalog.charAt(0)) - 1);

                    NobesVisualizerCourse course = iNobesVisualizerCourseService.getOne(new LambdaQueryWrapper<NobesVisualizerCourse>()
                            .eq(NobesVisualizerCourse::getCatalog, catalog)
                            .eq(NobesVisualizerCourse::getSubject, subject), false);

                    String description = "";

                    if (!course.isNull()) {
                        String progUnits = course.getProgUnits();
                        String calcFeeIndex = course.getCalcFeeIndex();
                        String duration = course.getDuration();
                        String alphaHours = course.getAlphaHours();
                        String courseDescription = course.getCourseDescription();

                        description = "★ " + progUnits.replaceAll("[^0-9]", "") + " (fi " + calcFeeIndex + ") " + "(" + duration + ", " + alphaHours + ") " + courseDescription;

                    } else {
                        description = course.getCourseDescription();
                    }

                    ArrayList<String> gradAtts = new ArrayList<>();

                    for (int i = 0; i < 12; i++) {
                        gradAtts.add("0");
                    }

                    ArrayList<String> courseGroup = courseGroupService.getCourseGroup(courseName, null);

                    VisualVO visualVO = new VisualVO();
                    visualVO.setCourseName(courseName)
                            .setTitle(courseName + " - " + course.getTitle())
                            .setDescription(description)
                            .setAttribute(gradAtts)
                            .setGroup(courseGroup);

                    visualVOS.add(visualVO);

                } else if (courseName.toLowerCase().contains("or")) {

                    // TODO: if there is an or, currently just take the first one, to be fixed
                    String[] ors = courseName.toLowerCase().split("or");

                    List<String> collect = Arrays.stream(ors).map(String::toUpperCase).collect(Collectors.toList());

                    for (int i = 0; i < collect.size(); i++) {
                        if (i + 1 < collect.size()) {
                            getCourseInfo(visualVOS, collect.get(i).trim(), removeParentheses(collect.get(i + 1).trim()));
                        } else {
                            getCourseInfo(visualVOS, collect.get(i).trim(), null);
                        }
                    }

                } else {
                    // in general cases
                    getCourseInfo(visualVOS, courseName, null);
                }
            }

            programMap.put(key, visualVOS);
        }

        LinkedHashMap<String, ArrayList<VisualVO>> orderedProgramMap = new LinkedHashMap<>();

        // remove duplicate course information
        for (String key : orderList) {
            if (programMap.containsKey(key)) {
                orderedProgramMap.put(key, programMap.get(key));
            }
        }

        return orderedProgramMap;
    }

    public void getCourseInfo(ArrayList<VisualVO> visualVOS, String courseName, String orCaseCourse) {

        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(courseName);
        matcher.find();
        String catalog = matcher.group(0);
        String subject = courseName.substring(0, courseName.indexOf(catalog.charAt(0)) - 1);


        // get the AU info by querying the AU table in the database
        NobesTimetableAu au = iTimetableAuService.getOne(new LambdaQueryWrapper<NobesTimetableAu>()
                .eq(NobesTimetableAu::getCourseName, subject + " " + catalog), false);

        HashMap<String, String> AUCount = new HashMap<>();

        if (au != null) {
            AUCount = Stream.of(
                            new AbstractMap.SimpleEntry<>("Math", au.getMath()),
                            new AbstractMap.SimpleEntry<>("Natural Sciences", au.getNaturalSciences()),
                            new AbstractMap.SimpleEntry<>("Complimentary Studies", au.getComplimentaryStudies()),
                            new AbstractMap.SimpleEntry<>("Engineering Design", au.getEngineeringDesign()),
                            new AbstractMap.SimpleEntry<>("Engineering Science", au.getEngineeringScience()),
                            new AbstractMap.SimpleEntry<>("Other", au.getOther()),
                            new AbstractMap.SimpleEntry<>("Specific Engineering Design", au.getEDsp()),
                            new AbstractMap.SimpleEntry<>("Specific Engineering Science", au.getESsp())
                    )
                    .filter(entry -> !entry.getValue().equals("0"))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1, HashMap::new));
        }

        // get the grad attributes
        ArrayList<String> gradAtts = gradAttService.getGradAtts(courseName);

        NobesVisualizerCourse course = iNobesVisualizerCourseService.getOne(new LambdaQueryWrapper<NobesVisualizerCourse>()
                .eq(NobesVisualizerCourse::getCatalog, catalog)
                .eq(NobesVisualizerCourse::getSubject, subject), false);

        if (course == null) {
            log.info(courseName);
        }
        // get the description
        String description;

        if (!course.isNull()) {
            String progUnits = course.getProgUnits();
            String calcFeeIndex = course.getCalcFeeIndex();
            String duration = course.getDuration();
            String alphaHours = course.getAlphaHours();
            String courseDescription = course.getCourseDescription();

            description = "★ " + progUnits.replaceAll("[^0-9]", "") + " (fi " + calcFeeIndex + ") " + "(" + duration + ", " + alphaHours + ") " + courseDescription;

        } else {
            description = course.getCourseDescription();
        }

        // get the course group
        ArrayList<String> courseGroup = courseGroupService.getCourseGroup(courseName, AUCount);

        VisualVO visualVO = new VisualVO();
        visualVO.setCourseName(courseName)
                .setTitle(courseName + " - " + course.getTitle())
                .setDescription(description)
                .setAUCount(AUCount)
                .setAttribute(gradAtts)
                .setGroup(courseGroup)
                .setOrCase(orCaseCourse);

        visualVOS.add(visualVO);
    }

    public String removeParentheses(String str) {
        while (true) {
            int startIndex = str.indexOf('(');
            int endIndex = str.indexOf(')');

            if (startIndex == -1 || endIndex == -1 || endIndex < startIndex) {
                break;
            }

            str = str.substring(0, startIndex) + str.substring(endIndex + 1);
        }

        return str;
    }

}
