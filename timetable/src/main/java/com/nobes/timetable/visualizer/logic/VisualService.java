package com.nobes.timetable.visualizer.logic;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nobes.timetable.calendar.domain.NobesTimetableAu;
import com.nobes.timetable.calendar.domain.NobesTimetableSequence;
import com.nobes.timetable.calendar.service.INobesTimetableAuService;
import com.nobes.timetable.calendar.service.INobesTimetableSequenceService;
import com.nobes.timetable.visualizer.domain.NobesVisualizerCourse;
import com.nobes.timetable.visualizer.domain.NobesVisualizerCoursegroup;
import com.nobes.timetable.visualizer.domain.NobesVisualizerGrad;
import com.nobes.timetable.visualizer.dto.VisualDTO;
import com.nobes.timetable.visualizer.logic.reqHelp.ReqService;
import com.nobes.timetable.visualizer.service.INobesVisualizerCourseService;
import com.nobes.timetable.visualizer.service.INobesVisualizerCoursegroupService;
import com.nobes.timetable.visualizer.service.INobesVisualizerGradService;
import com.nobes.timetable.visualizer.vo.VisualVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.swing.text.StyledEditorKit;
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
    INobesVisualizerCoursegroupService iNobesVisualizerCoursegroupService;

    @Resource
    INobesVisualizerGradService iNobesVisualizerGradService;

    @Resource
    INobesTimetableAuService iTimetableAuService;

    @Resource
    ReqService reqService;

    /**
     * For a given course, find its grad attributes, and return as a list of 12 numbers (0-3)
     *
     * @param courseName the name of the course
     * @return an ArrayList of String objects representing the graduate attributes of the course
     */
    public ArrayList<String> getGradAtts(String courseName) {

        ArrayList<String> gradAtts = new ArrayList<>();

        NobesVisualizerGrad gradAtt;

        // remove the brackets
        if (courseName.contains("(")) {
            courseName = courseName.substring(0, courseName.indexOf("("));
        }

        if (courseName.contains("COMP")) {
            // if the course is a complementary studies, then search CSOPT 100
            gradAtt = iNobesVisualizerGradService.getOne(new LambdaQueryWrapper<NobesVisualizerGrad>()
                    .eq(NobesVisualizerGrad::getCourseName, "CSOPT 100"), false);
        } else if (courseName.contains("ITS")) {
            // if the course is an ITS elective, then search ITS ELEC
            gradAtt = iNobesVisualizerGradService.getOne(new LambdaQueryWrapper<NobesVisualizerGrad>()
                    .eq(NobesVisualizerGrad::getCourseName, "ITS ELEC"), false);
        } else if (courseName.contains("PROG")) {
            // TODO: if the course is a program elective, currently can not resolve it, not enough information
            for (int i = 0; i < 12; i++) {
                gradAtts.add("0");
            }
            return gradAtts;
        } else {
            // generally, if any grad attributes are null, use 0 to indicate that, else use appropriate numbers
            gradAtt = iNobesVisualizerGradService.getOne(new LambdaQueryWrapper<NobesVisualizerGrad>()
                    .eq(NobesVisualizerGrad::getCourseName, courseName), false);
        }

        if (gradAtt == null) {
            for (int i = 0; i < 12; i++) {
                gradAtts.add("0");
            }

            return gradAtts;
        }

        gradAtts.add(gradAtt.getKnowledgeBase() == null || gradAtt.getKnowledgeBase().isEmpty() ? "0" : gradAtt.getKnowledgeBase());
        gradAtts.add(gradAtt.getProblemAnalysis() == null || gradAtt.getProblemAnalysis().isEmpty() ? "0" : gradAtt.getProblemAnalysis());
        gradAtts.add(gradAtt.getInvestigation() == null || gradAtt.getInvestigation().isEmpty() ? "0" : gradAtt.getInvestigation());
        gradAtts.add(gradAtt.getDesign() == null || gradAtt.getDesign().isEmpty() ? "0" : gradAtt.getDesign());
        gradAtts.add(gradAtt.getEngineeringTools() == null || gradAtt.getEngineeringTools().isEmpty() ? "0" : gradAtt.getEngineeringTools());
        gradAtts.add(gradAtt.getIndivAndTeamwork() == null || gradAtt.getIndivAndTeamwork().isEmpty() ? "0" : gradAtt.getIndivAndTeamwork());
        gradAtts.add(gradAtt.getCommunicationSkills() == null || gradAtt.getCommunicationSkills().isEmpty() ? "0" : gradAtt.getCommunicationSkills());
        gradAtts.add(gradAtt.getProfessionalism() == null || gradAtt.getProfessionalism().isEmpty() ? "0" : gradAtt.getProfessionalism());
        gradAtts.add(gradAtt.getImpactOnSociety() == null || gradAtt.getImpactOnSociety().isEmpty() ? "0" : gradAtt.getImpactOnSociety());
        gradAtts.add(gradAtt.getEthicsandEquity() == null || gradAtt.getEthicsandEquity().isEmpty() ? "0" : gradAtt.getEthicsandEquity());
        gradAtts.add(gradAtt.getEconomicsAndMgt() == null || gradAtt.getEconomicsAndMgt().isEmpty() ? "0" : gradAtt.getEconomicsAndMgt());
        gradAtts.add(gradAtt.getLifeLongLearning() == null || gradAtt.getLifeLongLearning().isEmpty() ? "0" : gradAtt.getLifeLongLearning());

        return gradAtts;
    }

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
        ArrayList<String> courseGroup = new ArrayList<>(Collections.nCopies(9, "0"));

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
            NobesVisualizerCoursegroup group = iNobesVisualizerCoursegroupService.getOne(new LambdaQueryWrapper<NobesVisualizerCoursegroup>()
                    .eq(NobesVisualizerCoursegroup::getCourseName, courseName.toUpperCase()), false);

            if (group != null) {
                switch (group.getCourseGroup()) {
                    case "Math":
                        courseGroup.set(0, "1");
                        break;
                    case "Natural Sciences":
                        courseGroup.set(1, "1");
                        break;
                    case "Engineering Sciences":
                        courseGroup.set(2, "1");
                        break;
                    case "Engineering Design":
                        courseGroup.set(3, "1");
                        break;
                    case "Engineering Profession":
                        courseGroup.set(4, "1");
                        break;
                    case "Other":
                        courseGroup.set(8, "1");
                        break;
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
            } else {
                log.info(courseName + "is missing");
            }
        }

        return courseGroup;
    }

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

        ArrayList<String> orderList = new ArrayList<>();

        // remove duplicate terms
        for (NobesTimetableSequence sequence : courseList) {
            if (!orderList.contains(sequence.getTermName())) {
                orderList.add(sequence.getTermName());
            }
        }

        // group by the term names and map each course name to a list associated with that terms
        Map<String, List<String>> map = courseList.stream()
                .collect(Collectors.groupingBy(NobesTimetableSequence::getTermName,
                        Collectors.mapping(NobesTimetableSequence::getCourseName, Collectors.toList())));

        Set<String> keySet = map.keySet();

        for (String key : keySet) {
            List<String> value = map.get(key);

            ArrayList<VisualVO> visualVOS = new ArrayList<>();

            for (String courseName : value) {

                if (courseName.contains("COMP") || courseName.contains("ITS") || courseName.contains("PROG")) {

                    // if the course is a selective or elective course
                    ArrayList gradAtts = getGradAtts(courseName);

                    ArrayList<String> courseGroup = getCourseGroup(courseName, null);

                    VisualVO visualVO = new VisualVO();
                    visualVO.setCourseName(courseName)
                            .setTitle(courseName)
                            .setAttribute(gradAtts)
                            .setGroup(courseGroup);

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

                    ArrayList<String> courseGroup = getCourseGroup(courseName, null);

                    VisualVO visualVO = new VisualVO();
                    visualVO.setCourseName(courseName)
                            .setTitle(courseName + " - " + course.getTitle())
                            .setDescription(description)
                            .setAttribute(gradAtts)
                            .setGroup(courseGroup);

                    visualVOS.add(visualVO);

                } else if (courseName.contains("or")) {

                    // TODO: if there is an or, currently just take the first one, to be fixed
                    String[] ors = courseName.split("or");

                    for (int i = 0; i < ors.length; i++) {
                        if (i + 1 < ors.length) {
                            getCourseInfo(visualVOS, ors[i].trim(), ors[i + 1].trim());
                        } else {
                            getCourseInfo(visualVOS, ors[i].trim(), null);
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
        ArrayList<String> gradAtts = getGradAtts(courseName);

        NobesVisualizerCourse course = iNobesVisualizerCourseService.getOne(new LambdaQueryWrapper<NobesVisualizerCourse>()
                .eq(NobesVisualizerCourse::getCatalog, catalog)
                .eq(NobesVisualizerCourse::getSubject, subject), false);

        String courseDescription1 = course.getCourseDescription();

        // get the prerequisites and corequisites
        ArrayList<String> preReqs = reqService.pullPreReqs(courseDescription1);
        ArrayList<String> coReqs = reqService.pullCoReqs(courseDescription1);

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
        ArrayList<String> courseGroup = getCourseGroup(courseName, AUCount);

        VisualVO visualVO = new VisualVO();
        visualVO.setCourseName(courseName)
                .setTitle(courseName + " - " + course.getTitle())
                .setDescription(description)
                .setAUCount(AUCount)
                .setAttribute(gradAtts)
                .setPreReqs(preReqs)
                .setCoReqs(coReqs)
                .setGroup(courseGroup)
                .setOrCase(orCaseCourse);

        visualVOS.add(visualVO);
    }

}
