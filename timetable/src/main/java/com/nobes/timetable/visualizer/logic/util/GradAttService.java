package com.nobes.timetable.visualizer.logic.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nobes.timetable.visualizer.domain.NobesVisualizerGrad;
import com.nobes.timetable.visualizer.service.INobesVisualizerGradService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;

@Service
@Slf4j
public class GradAttService {

    @Resource
    INobesVisualizerGradService iNobesVisualizerGradService;

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
}
