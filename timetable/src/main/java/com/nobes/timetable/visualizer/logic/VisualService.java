package com.nobes.timetable.visualizer.logic;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nobes.timetable.calendar.domain.NobesTimetableAu;
import com.nobes.timetable.calendar.domain.NobesTimetableSequence;
import com.nobes.timetable.calendar.service.INobesTimetableAuService;
import com.nobes.timetable.calendar.service.INobesTimetableSequenceService;
import com.nobes.timetable.visualizer.domain.NobesVisualizerCourse;
import com.nobes.timetable.visualizer.domain.NobesVisualizerGrad;
import com.nobes.timetable.visualizer.dto.VisualDTO;
import com.nobes.timetable.visualizer.logic.reqHelp.ReqService;
import com.nobes.timetable.visualizer.service.INobesVisualizerCourseService;
import com.nobes.timetable.visualizer.service.INobesVisualizerGradService;
import com.nobes.timetable.visualizer.vo.VisualVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class VisualService {

    @Resource
    INobesTimetableSequenceService sService;

    @Resource
    INobesVisualizerCourseService iNobesVisualizerCourseService;

    @Resource
    INobesVisualizerGradService iNobesVisualizerGradService;

    @Resource
    INobesTimetableAuService iTimetableAuService;

    @Resource
    ReqService reqService;

    public HashMap getCourses(VisualDTO visualDTO) throws Exception {
        HashMap<String, ArrayList<VisualVO>> programMap = new HashMap<>();

        String planName = visualDTO.getPlanName();
        String programName = visualDTO.getProgramName();

        List<NobesTimetableSequence> courseList = sService.list(new LambdaQueryWrapper<NobesTimetableSequence>()
                .eq(NobesTimetableSequence::getProgramName, programName)
                .eq(NobesTimetableSequence::getPlanName, planName));

        ArrayList<String> orderList = new ArrayList<>();

        for (NobesTimetableSequence sequence : courseList) {
            if (!orderList.contains(sequence.getTermName())) {
                orderList.add(sequence.getTermName());
            }
        }

        Map<String, List<String>> map = courseList.stream()
                .collect(Collectors.groupingBy(NobesTimetableSequence::getTermName,
                        Collectors.mapping(NobesTimetableSequence::getCourseName, Collectors.toList())));

        Set<String> keySet = map.keySet();

        for (String key : keySet) {
            List<String> value = map.get(key);

            ArrayList<VisualVO> visualVOS = new ArrayList<>();

            for (String courseName : value) {

                log.info(courseName + " is been processing");

                if (courseName.contains("COMP") || courseName.contains("ITS") || courseName.contains("PROG")) {

                    ArrayList gradAtts = getGradAtts(courseName);

                    VisualVO visualVO = new VisualVO();
                    visualVO.setCourseName(courseName)
                            .setTitle(courseName)
                            .setAttribute(gradAtts);

                    visualVOS.add(visualVO);
                } else if (courseName.contains("WKEXP")) {
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

                    VisualVO visualVO = new VisualVO();
                    visualVO.setCourseName(courseName)
                            .setTitle(courseName + " - " + course.getTitle())
                            .setDescription(description)
                            .setAttribute(gradAtts);

                    visualVOS.add(visualVO);

                } else {

                    if (courseName.contains("or")) {
                        String[] ors = courseName.split("or");
                        courseName = ors[0].trim();
                    }

                    Pattern pattern = Pattern.compile("\\d+");
                    Matcher matcher = pattern.matcher(courseName);
                    matcher.find();
                    String catalog = matcher.group(0);
                    String subject = courseName.substring(0, courseName.indexOf(catalog.charAt(0)) - 1);


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
                                        new AbstractMap.SimpleEntry<>("EDsp", au.getEDsp()),
                                        new AbstractMap.SimpleEntry<>("ESsp", au.getESsp())
                                )
                                .filter(entry -> !entry.getValue().equals("0")) // Filter out zero-valued strings
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1, HashMap::new));
                    }

                    ArrayList<String> gradAtts = getGradAtts(courseName);

                    NobesVisualizerCourse course = iNobesVisualizerCourseService.getOne(new LambdaQueryWrapper<NobesVisualizerCourse>()
                            .eq(NobesVisualizerCourse::getCatalog, catalog)
                            .eq(NobesVisualizerCourse::getSubject, subject), false);

                    String courseDescription1 = course.getCourseDescription();

                    ArrayList<String> preReqs = reqService.pullPreReqs(courseDescription1);
                    ArrayList<String> coReqs = reqService.pullCoReqs(courseDescription1);

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

                    VisualVO visualVO = new VisualVO();
                    visualVO.setCourseName(courseName)
                            .setTitle(courseName + " - " + course.getTitle())
                            .setDescription(description)
                            .setAUCount(AUCount)
                            .setAttribute(gradAtts)
                            .setPreReqs(preReqs)
                            .setCoReqs(coReqs);

                    visualVOS.add(visualVO);
                }

            }

            log.info(key);

            programMap.put(key, visualVOS);
        }

        LinkedHashMap<String, ArrayList<VisualVO>> orderedProgramMap = new LinkedHashMap<>();

        for (String key : orderList) {
            if (programMap.containsKey(key)) {
                orderedProgramMap.put(key, programMap.get(key));
            }
        }

        return orderedProgramMap;
    }

    public ArrayList<String> getGradAtts(String courseName) {

        ArrayList<String> gradAtts = new ArrayList<>();

        NobesVisualizerGrad gradAtt;

        if (courseName.contains("(")) {
            courseName = courseName.substring(0, courseName.indexOf("("));
        }

        if (courseName.contains("COMP")) {
            gradAtt = iNobesVisualizerGradService.getOne(new LambdaQueryWrapper<NobesVisualizerGrad>()
                    .eq(NobesVisualizerGrad::getCourseName, "CSOPT 100"), false);
        } else if (courseName.contains("ITS")) {
            gradAtt = iNobesVisualizerGradService.getOne(new LambdaQueryWrapper<NobesVisualizerGrad>()
                    .eq(NobesVisualizerGrad::getCourseName, "ITS ELEC"), false);
        } else if (courseName.contains("PROG")) {
            for (int i = 0; i < 12; i++) {
                gradAtts.add("0");
            }
            return gradAtts;

        } else {
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
}
