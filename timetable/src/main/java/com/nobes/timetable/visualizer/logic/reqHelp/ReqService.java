package com.nobes.timetable.visualizer.logic.reqHelp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.nobes.timetable.visualizer.logic.reqHelp.ParseHelpService.countNums;
import static com.nobes.timetable.visualizer.logic.reqHelp.ParseHelpService.pullDept;

@Service
@Slf4j
public class ReqService {
    public ArrayList<String> pullPreReqs(String description) {
        description = description.replace("-requisite", "requisite");
        int singleStart = description.indexOf("Prerequisite: ");
        if (singleStart == -1) {
            singleStart = description.indexOf("prerequisite: ");
        }

        int multStart = description.indexOf("Prerequisites: ");
        if (multStart == -1) {
            multStart = description.indexOf("prerequisites: ");
        }

        int missingColStart = description.indexOf("Prerequisite ");
        if (missingColStart == -1) {
            missingColStart = description.indexOf("prerequisite ");
        }

        String preStr;
        if (singleStart != -1) {
            singleStart += 14;
            int singleEnd = description.indexOf(".", singleStart);
            preStr = description.substring(singleStart, singleEnd);
        } else if (multStart != -1) {
            multStart += 15;
            int multEnd = description.indexOf(".", multStart);
            preStr = description.substring(multStart, multEnd);
        } else if (missingColStart != -1) {
            missingColStart += 13;
            int missingColEnd = description.indexOf(".", missingColStart);
            preStr = description.substring(missingColStart, missingColEnd);
        } else {
            return new ArrayList<>();
        }

        if (preStr.toLowerCase().contains("corequisite")) {
            int index = preStr.toLowerCase().indexOf("corequisite");
            preStr = preStr.substring(index + "corequisite".length()).trim();

            if (preStr.startsWith(":")) {
                preStr = preStr.substring(1).trim();
            }
        }

        // Assumes you have a process method that takes a String and returns an ArrayList<String>
        ArrayList<String> prereqs = (ArrayList<String>) process(preStr);

        return prereqs;
    }

    public ArrayList<String> pullCoReqs(String description) {
        description = description.replace("-requisite", "requisite");
        int singleStart = description.indexOf("Corequisite: ");
        if (singleStart == -1) {
            singleStart = description.indexOf("corequisite: ");
        }

        int multStart = description.indexOf("Corequisites: ");
        if (multStart == -1) {
            multStart = description.indexOf("corequisites: ");
        }

        int missingColStart = description.indexOf("Corequisite ");
        if (missingColStart == -1) {
            missingColStart = description.indexOf("corequisite ");
        }

        String preStr;
        if (singleStart != -1) {
            singleStart += 13;
            int singleEnd = description.indexOf(".", singleStart);
            preStr = description.substring(singleStart, singleEnd);
        } else if (multStart != -1) {
            multStart += 14;
            int multEnd = description.indexOf(".", multStart);
            preStr = description.substring(multStart, multEnd);
        } else if (missingColStart != -1) {
            missingColStart += 12;
            int missingColEnd = description.indexOf(".", missingColStart);
            preStr = description.substring(missingColStart, missingColEnd);
        } else {
            return new ArrayList<>();
        }

        // Assumes you have a process method that takes a String and returns an ArrayList<String>
        ArrayList<String> coreqs = (ArrayList<String>) process(preStr);

        return coreqs;
    }

    public List<String> process(String prestr) {
        prestr = prestr.strip();
        prestr = prestr.replace("\n", " ");
        prestr = prestr.replaceAll("(\\d) ", "$1, ");
        prestr = prestr.replace(" and", ",");
        prestr = prestr.replace("  ", " ");

        ArrayList<String> reqlist = new ArrayList<>(Arrays.asList(prestr.split(", ")));

        if (reqlist.isEmpty()) {
            return new ArrayList<>();
        }

        reqlist = preprocess(reqlist);

        int i = 0;

        while (i < reqlist.size()) {
            reqlist.set(i, reqlist.get(i).strip());

            if (reqlist.get(i).contains("-")) {
                reqlist.remove(i);
                continue;
            }

            int numcounter = countNums(reqlist.get(i));

            if (reqlist.get(i).length() >= 5 && (reqlist.get(i).substring(0, 5).equalsIgnoreCase("both "))) {
                // Two courses are required, remove both and pull the department name if required
                reqlist.set(i, reqlist.get(i).replaceFirst("(?i)both ", ""));

                if (numcounter == 3 && reqlist.get(i + 1).length() == 3) {
                    // Only a course number is present, must pull the department name
                    String dept = pullDept(reqlist, i);
                    if (dept == null) {
                        throw new AssertionError("Error pulling department name from previous list reqlist.get(i), check pullDept()");
                    }

                    reqlist.set(i + 1, dept + " " + reqlist.get(i + 1));
                }
            }

            if (reqlist.get(i).length() >= 7 && (reqlist.get(i).substring(0, 7).equalsIgnoreCase("One of ") || reqlist.get(i).substring(0, 7).equalsIgnoreCase("Either "))) {
                // Same logic for "one of" and "either"
                if ((reqlist.get(i).substring(0, 6).equalsIgnoreCase("Either")) && reqlist.get(i + 1).substring(0, 11).equalsIgnoreCase("or one of ")) {
                    reqlist.set(i + 1, reqlist.get(i + 1).replaceFirst("(?i)or one of ", ""));
                }

                // Only one of the upcoming courses is required
                reqlist.set(i, reqlist.get(i).replaceAll("(?i)One of |one of |Either |either ", ""));

                int j = i + 1;

                if (j < reqlist.size()) {
                    while (!reqlist.get(j).contains("or") && !reqlist.get(j).contains("Or")) {
                        int nums = countNums(reqlist.get(j));

                        if (nums == 3 && reqlist.get(j).length() == 3) {
                            String dept = pullDept(reqlist, j - 1);
                            reqlist.set(j, dept + " " + reqlist.get(j));
                        }
                        reqlist.set(i, reqlist.get(i) + " or " + reqlist.get(j));
                        reqlist.remove(j);

                        if (j >= reqlist.size()) {
                            break;
                        }
                    }

                    if (j < reqlist.size()) {
                        if (reqlist.get(j).contains("or") || reqlist.get(j).contains("Or")) {
                            int nums = countNums(reqlist.get(j));

                            if (nums == 3 && reqlist.get(j).length() == 6) {
                                String dept = pullDept(reqlist, j - 1);
                                reqlist.set(j, "or " + dept + reqlist.get(j).substring(2));
                            }

                            if (reqlist.get(j).substring(0, 8) == "or both ") {
                                if (reqlist.get(j).length() == 11) {
                                    String dept = pullDept(reqlist, j - 1);
                                    reqlist.set(j, "or both " + dept + reqlist.get(j).substring(7));
                                }

                                int nums1 = countNums(reqlist.get(j + 1));

                                if (nums1 == 3 && reqlist.get(j + 1).length() == 3) {
                                    String dept = pullDept(reqlist, j);
                                    reqlist.set(j + 1, "and " + dept.substring(8) + " " + reqlist.get(j + 1));
                                    reqlist.set(j, reqlist.get(j) + " " + reqlist.get(j + 1));  // combine current and next
                                    reqlist.remove(j + 1);

                                }
                            }

                            reqlist.set(i, reqlist.get(i) + " " + reqlist.get(j));  // combine current and previous
                            reqlist.remove(j);
                            if (j >= reqlist.size()) {
                                break;
                            }

                        }
                    }
                }

                i += 1;

            } else if ((reqlist.get(i).substring(0, 2).equalsIgnoreCase("or")) && (reqlist.get(i).length() >= 6)) {
                reqlist.set(i - 1, reqlist.get(i - 1) + " " + reqlist.get(i));
                reqlist.remove(i);
            } else if (numcounter == 3 && reqlist.get(i).length() == 3) {

                String dept = pullDept(reqlist, i - 1);
                reqlist.set(i, dept + " " + reqlist.get(i));  // just add the department name to current item
                i += 1;
            } else {
                i += 1;
            }

        }

        return reqlist;
    }

    public ArrayList<String> preprocess(ArrayList<String> reqlist) {
        ArrayList<String> newlist = new ArrayList<String>();

        int i = 0;
        while (i < reqlist.size()) {
            // Remove all commas and brackets
            String currentStr = reqlist.get(i);
            currentStr = currentStr.replace("(", "").replace(")", "").replace(",", "");
            reqlist.set(i, currentStr);

            if (currentStr.contains(";")) {

                String[] semicolsplit = currentStr.split(";");
                reqlist.remove(i);
                int k = i;
                for (String splititem : semicolsplit) {
                    splititem = splititem.trim();
                    reqlist.add(k, splititem);
                    k += 1;
                }
            }

            // A slash between courses indicates the same as "or".
            // Replace all slashes with " or "
            String[] splitslash = currentStr.split("/");
            if (!splitslash[0].equals(currentStr)) {
                // There was a slash present
                int j = i;
                int k = 0;
                while (k < splitslash.length) {
                    // Replace all slashes with "or "
                    if (k != 0) {
                        if (!splitslash[k].substring(0, 2).equalsIgnoreCase("or")) {
                            splitslash[k] = "or " + splitslash[k];
                        }
                    }
                    k += 1;
                }
                // splitslash has corrected entries, replace currentStr with concatenated
                // entries from splitslash
                reqlist.remove(i);
                for (String splititem : splitslash) {
                    reqlist.add(j, splititem);  // pull from start of splitslash and delete that entry
                    j += 1;
                }
            }

            i += 1;
        }

        int j = 0;
        while (j < reqlist.size()) {
            // Must have at least 3 numbers to be the name of a course
            int numcounter = countNums(reqlist.get(j));
            if (numcounter < 3) {
                j += 1;
                continue;
            }

            if (reqlist.get(j).length() > 16) {
                // String is too long to be the name of a course
                j += 1;
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

            j += 1;
        }

        return newlist;
    }
}
