package com.nobes.timetable.core.save.service;


import cn.hutool.db.PageResult;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.nobes.timetable.core.save.service.ParseHelpService.countNums;
import static com.nobes.timetable.core.save.service.ParseHelpService.pullDept;

@Service
@Slf4j
@NoArgsConstructor
public class ReqService {

    public ArrayList pullDependencies(String description) throws Exception{

        ArrayList prerequisite = pullPreReqs(description);

        return prerequisite;
    }

    public ArrayList<String> pullPreReqs(String description) throws Exception {
        description.replace("-requisite", "requisite");

        Integer singlestart = description.indexOf("Prerequisite: ");
        if (singlestart == -1) {
            singlestart = description.indexOf("prerequisite: ");
        }

        Integer multstart = description.indexOf("Prerequisites: ");
        if (multstart == -1) {
            multstart = description.indexOf("prerequisites: ");
        }

        Integer missingcolstart = description.indexOf("Prerequisite ");
        if (missingcolstart == -1) {
            missingcolstart = description.indexOf("prerequisite ");
        }

        String preRe;

        if (singlestart != -1) {
            singlestart += 14;
            int singleend = description.indexOf(".", singlestart);
            preRe = description.substring(singlestart, singleend);
        } else if (multstart != -1) {
            multstart += 15;
            int multend = description.indexOf(".", multstart);
            preRe = description.substring(multstart, multend);
        } else if (missingcolstart != -1) {
            missingcolstart += 13;
            int missingcolend = description.indexOf(".", missingcolstart);
            preRe = description.substring(missingcolstart, missingcolend);
        } else {
            return new ArrayList<String>();
        }

        ArrayList<String> preRes = process(preRe);

        return preRes;

    }

    public ArrayList<String> pullCoReqs(String description) {
        description = description.replace("-requisite", "requisite");

        int singlestart = description.indexOf("Corequisite: ");
        if (singlestart == -1) {
            singlestart = description.indexOf("corequisite: ");
        }

        int multstart = description.indexOf("Corequisites: ");
        if (multstart == -1) {
            multstart = description.indexOf("corequisites: ");
        }

        int missingcolstart = description.indexOf("Corequisite ");
        if (missingcolstart == -1) {
            missingcolstart = description.indexOf("corequisite ");
        }

        String coRe;

        if (singlestart != -1) {
            singlestart += 13;
            int singleend = description.indexOf(".", singlestart);
            coRe = description.substring(singlestart, singleend);
        } else if (multstart != -1) {
            multstart += 14;
            int multend = description.indexOf(".", multstart);
            coRe = description.substring(multstart, multend);
        } else if (missingcolstart != -1) {
            missingcolstart += 12;
            int missingcolend = description.indexOf(".", missingcolstart);
            coRe = description.substring(missingcolstart, missingcolend);
        } else {
            return new ArrayList<>();
        }

        ArrayList<String> coRes = process(coRe);

        return coRes;
    }

    public ArrayList<String> process(String preRe) {

        preRe = preRe.trim();
        preRe = preRe.replaceAll("\n", " ");
        preRe = preRe.replaceAll("0 ", "0, ");
        preRe = preRe.replaceAll("1 ", "1, ");
        preRe = preRe.replaceAll("2 ", "2, ");
        preRe = preRe.replaceAll("3 ", "3, ");
        preRe = preRe.replaceAll("4 ", "4, ");
        preRe = preRe.replaceAll("5 ", "5, ");
        preRe = preRe.replaceAll("6 ", "6, ");
        preRe = preRe.replaceAll("7 ", "7, ");
        preRe = preRe.replaceAll("8 ", "8, ");
        preRe = preRe.replaceAll("9 ", "9, ");
        preRe = preRe.replaceAll(" and", ",");
        preRe = preRe.replaceAll("  ", " ");

        String[] reqs = preRe.split(", ");
        List<String> reqList = Arrays.asList(reqs);
        ArrayList<String> reqArrayList = new ArrayList<>(reqList);

        if (reqList == null) {
            return new ArrayList<String>();
        }

        ArrayList<String> preprocess = preprocess(reqArrayList);

        Integer i = 0;

        while (i < preprocess.size()) {
            preprocess.set(i, preprocess.get(i).trim());

            if (preprocess.get(i).contains("-")) {
                preprocess.remove(i);
                continue;
            }

            int countNums = countNums(preprocess.get(i));

            if (preprocess.get(i).length() > 5) {
                if (preprocess.get(i).substring(0, 5).equals("both ") || preprocess.get(i).substring(0, 5).equals("Both ")) {
                    // Two courses are required, remove both and pull the department name if required
                    preprocess.set(i, preprocess.get(i).replace("both ", "").replace("Both ", ""));
                    int numcounter = countNums(preprocess.get(i + 1));
                    if (numcounter == 3 && preprocess.get(i + 1).length() == 3) {
                        // Only a course number is present, must pull the department name
                        String dept = pullDept(preprocess, i);
                        if (dept != "-1") {
                            preprocess.set(i + 1, dept + " " + preprocess.get(i + 1));
                        }
                    }
                }
            }

            if (preprocess.get(i).length() > 7) {
                if (preprocess.get(i).substring(0, 7).equals("One of ") || preprocess.get(i).substring(0, 7).equals("one of ") ||
                        preprocess.get(i).substring(0, 7).equals("Either ") || preprocess.get(i).substring(0, 7).equals("either ")) {
                    if ((preprocess.get(i).substring(0, 6).equals("Either") || preprocess.get(i).substring(0, 6).equals("either"))
                            && preprocess.get(i + 1).substring(0, 11).equals("or one of ")) {
                        preprocess.set(i + 1, preprocess.get(i + 1).replace("or one of ", ""));
                    }
                    preprocess.set(i, preprocess.get(i).replace("One of ", "").replace("one of ", "")
                            .replace("Either ", "").replace("either ", ""));

                    int j = i + 1;

                    if (j < preprocess.size()) {
                        while (!preprocess.get(j).contains("or") && !preprocess.get(j).contains("Or")) {

                            int numcounter = countNums(preprocess.get(j));

                            if (numcounter == 3 && preprocess.get(j).length() == 3) {
                                String dept = pullDept(preprocess, j - 1);

                                preprocess.set(j, dept + " " + preprocess.get(j));
                            }

                            preprocess.set(i, preprocess.get(i) + " or " + preprocess.get(j));
                            preprocess.remove(j);

                            if (j >= preprocess.size()) {
                                break;
                            }
                        }

                        if (j < preprocess.size()) {
                            if (preprocess.get(j).contains("or") || preprocess.get(j).contains("Or")) {
                                int numcounter = countNums(preprocess.get(j));

                                if (numcounter == 3 && preprocess.get(j).length() == 6) {
                                    String dept = pullDept(preprocess, j - 1);
                                    if (dept != "-1") {
                                        preprocess.set(j, "or " + dept + preprocess.get(j).substring(2));
                                    }
                                }

                                if ((preprocess.get(j).substring(0, 8).equalsIgnoreCase("or both "))) {
                                    if (preprocess.get(j).length() == 11) {
                                        String dept = pullDept(preprocess, j - 1);

                                        preprocess.set(j, "or both " + dept + preprocess.get(j).substring(7));
                                    }

                                    int nums = countNums(preprocess.get(j + 1));
                                    if (nums == 3 && preprocess.get(j + 1).length() == 3) {
                                        String dept = pullDept(preprocess, j);

                                        preprocess.set(j + 1, "and " + dept.substring(8) + " " + preprocess.get(j + 1));
                                        preprocess.set(j, preprocess.get(j) + " " + preprocess.get(j + 1));
                                        preprocess.remove(j + 1);
                                    }
                                }

                                preprocess.set(i, preprocess.get(i) + " " + preprocess.get(j));
                                preprocess.remove(j);
                                if (j >= preprocess.size()) {
                                    break;
                                }
                            }

                        }
                    }
                    i += 1;
                }
            }

            if ((preprocess.get(i).substring(0, 2).equals("or")) || (preprocess.get(i).substring(0, 2).equals("Or"))) {
                if (preprocess.get(i).length() > 6) {
                    preprocess.set(i-1, preprocess.get(i-1) + " " + preprocess.get(i));
                    preprocess.remove(i);
                }
            }

            if (countNums == 3 && preprocess.get(i).length() == 3) {
                String dept = pullDept(preprocess, i - 1);
                if (!dept.equals("-1")) {
                    preprocess.set(i, dept + " " + preprocess.get(i));
                }

            }
            i += 1;


        }

        return preprocess;
    }

    public  ArrayList<String> preprocess(ArrayList<String> reqlist) {
        ArrayList<String> newlist = new ArrayList<String>();

        int i = 0;
        while (i < reqlist.size()) {
            // Remove all commas and brackets
            String curr = reqlist.get(i);
            curr = curr.replace("(", "").replace(")", "").replace(",", "");
            reqlist.set(i, curr);

            if (curr.contains(";")) {
                // Treat a semicolon similar to a comma, split at the semicolon
                // and append to reqlist
                String[] semicolsplit = curr.split(";");
                reqlist.remove(i);
                int k = i;
                for (String splititem : semicolsplit) {
                    splititem = splititem.trim();
                    reqlist.add(k, splititem);
                    k++;
                }
            }

            // A slash between courses indicates the same as "or".
            // Replace all slashes with " or "
            String[] splitslash = curr.split("/");
            if (splitslash.length > 1) {
                // There was a slash present
                int j = i;
                int k = 0;
                while (k < splitslash.length) {
                    // Replace all slashes with "or "
                    if (k != 0) {
                        if (!(splitslash[k].startsWith("or") || splitslash[k].startsWith("Or"))) {
                            splitslash[k] = "or " + splitslash[k];
                        }
                    }
                    k++;
                }
                // splitslash has corrected entries, replace reqlist[i] with concatenated
                // entries from splitslash
                reqlist.remove(i);
                for (String split : splitslash) {
                    reqlist.add(j, split);
                    j++;
                }
            }

            i++;
        }

        int j = 0;
        while (j < reqlist.size()) {
            // Must have at least 3 numbers to be the name of a course
            int numcounter = countNums(reqlist.get(j));
            if (numcounter < 3) {
                j++;
                continue;
            }

            if (reqlist.get(j).length() > 16) {
                // String is too long to be the name of a course
                j++;
                continue;
            }

            int semicolindx = reqlist.get(j).indexOf(";");
            if (semicolindx != -1) {
                // Remove all text after a semicolon
                newlist.add(reqlist.get(j).substring(0, semicolindx));
            } else {
                // If no semicolon and passed the above cases, it is a valid course
                newlist.add(reqlist.get(j));
            }

            j++;
        }

        return newlist;
    }


}
