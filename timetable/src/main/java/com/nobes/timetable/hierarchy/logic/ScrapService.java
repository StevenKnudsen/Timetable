package com.nobes.timetable.hierarchy.logic;

import com.nobes.timetable.product.logic.LectureService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class ScrapService {

    public Map<String, ArrayList> getCourses(Document document) {

        Map<String, ArrayList> sequenceMap = new HashMap<>();

        Elements elements = document.select("div.acalog-core");

        Integer term_number = 3;

        for (int i = 0; i < elements.size(); i++) {
            Element tags = elements.get(i);
            Elements term = tags.select("h4");
            if (term.size() != 0) {
                String termName = term.text();

                if (termName.equals("Summer")) {
                    termName += " COOP ";
                    termName += term_number;
                }

                if (termName.equals("Winter")) {
                    termName += " COOP ";
                    termName += term_number;
                }

                Elements courses = tags.getElementsByTag("ul");
                ArrayList<String> courseList = new ArrayList<>();

                for (int j = 0; j < courses.size(); j++) {
                    Element course = courses.get(j);
                    Elements acalog = course.getElementsByTag("li");

                    for (int k = 0; k < acalog.size(); k++) {

                        String coursename = acalog.get(k).text();
                        String c_name;

                        if (!coursename.equals("")) {

                            if (coursename.contains("Program and Technical Elective")) {
                                c_name = coursename;
                            } else if (coursename.contains("Complementary Studies Elective")) {
                                c_name = coursename;
                            } else if (coursename.contains("ITS Elective")) {
                                c_name = coursename;
                            } else {
                                String[] sp = coursename.split("-");
                                c_name = sp[0].substring(0, sp[0].length() - 1);
                            }

                            if (acalog.get(k).select("strong") != null) {
                                c_name += " ";
                                c_name += acalog.get(k).select("Strong").text();
                            }

                            courseList.add(c_name);
                        }
                    }
                }



                String temp = "";
                ArrayList<Integer> deleteList = new ArrayList<>();

                for (int x = 0; x < courseList.size(); x++) {

                    if (courseList.get(x).substring(courseList.get(x).length() - 2).equals("OR")) {
                        temp += courseList.get(x);
                        temp += " ";
                        deleteList.add(x);
                    } else {
                        String comb = temp + courseList.get(x);
                        courseList.set(x, comb.substring(0,comb.length() - 1));
                        temp = "";
                    }
                }

                for (Integer integer : deleteList) {
                    courseList.remove(integer.intValue());
                }


                for (int z = 0; z < termName.length(); z++) {
                    if (Character.isDigit(termName.charAt(z))) {
                        term_number += 1;
                        sequenceMap.put(termName, courseList);
                        break;
                    }
                }
            }
        }

        return sequenceMap;
    }
}
