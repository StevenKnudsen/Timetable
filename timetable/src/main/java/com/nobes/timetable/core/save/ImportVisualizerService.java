package com.nobes.timetable.core.save;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nobes.timetable.visualizer.domain.NobesVisualizerCourse;
import com.nobes.timetable.visualizer.domain.NobesVisualizerCoursegroup;
import com.nobes.timetable.visualizer.service.INobesVisualizerCourseService;
import com.nobes.timetable.visualizer.service.INobesVisualizerCoursegroupService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


/**
 * The Visualizer import Service to save the information in Excel files that will be used for the visualizer into database
 * */
@Service
@Slf4j
public class ImportVisualizerService {

    @Resource
    INobesVisualizerCourseService visualizerCourseService;

    @Resource
    INobesVisualizerCoursegroupService visualizerCoursegroupService;

    /**
     * Save all the info in the input Excel files into nobes_visualizer_course table in the database
     * print the success info in console if import succeed
     * @param file the Excel file to be saved
     * @throws IOException if any error occurs during the retrieval process.
     */
    public void courseImport(File file) {

        ExcelReader reader = ExcelUtil.getReader(file);
        List<List<Object>> rows = reader.read();

        for (int i = 1; i < rows.size(); i++) {
            NobesVisualizerCourse nobesVisualizerCourse = new NobesVisualizerCourse();
            nobesVisualizerCourse.setFaculty(getCell(rows.get(i).get(0)))
                    .setDepartment(getCell(rows.get(i).get(1)))
                    .setCourseID(getCell(rows.get(i).get(2)))
                    .setSubject(getCell(rows.get(i).get(3)))
                    .setCatalog(getCell(rows.get(i).get(4)))
                    .setTitle(getCell(rows.get(i).get(5)))
                    .setEffDate(getCell(rows.get(i).get(6)))
                    .setStatus(getCell(rows.get(i).get(7)))
                    .setCalendarPrint(getCell(rows.get(i).get(8)))
                    .setProgUnits(getCell(rows.get(i).get(9)))
                    .setEngineeringUnits(getCell(rows.get(i).get(10)))
                    .setCalcFeeIndex(getCell(rows.get(i).get(11)))
                    .setActualFeeIndex(getCell(rows.get(i).get(12)))
                    .setDuration(getCell(rows.get(i).get(13)))
                    .setAlphaHours(getCell(rows.get(i).get(14)))
                    .setCourseDescription(getCell(rows.get(i).get(15)));

            visualizerCourseService.save(nobesVisualizerCourse);
        }

        log.info("Courses import success");
    }

    /**
     * This method truncates the "nobes_visualizer_course" table in the mydatabase database using the MySQL JDBC driver.
     * It uses a JDBC connection to the database and executes a TRUNCATE TABLE statement to clear all data from the table.
     * The database connection information, including the URL, username, and password, is hardcoded and needs to be changed when using this method in another database.
     * @throws SQLException if there is any error in the SQL statement execution.
     */
    public void truncate() {

        // TODO: change the database connection information when using it in another database
        String url = "jdbc:mysql://35.183.28.169:3306/mydatabase?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&allowMultiQueries=true&useSSL=false";
        String username = "root";
        String password = "Jxp_51515";

        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            Statement stmt = conn.createStatement();

            String[] tables = {"nobes_visualizer_course"};

            for (String table : tables) {
                String sql = "TRUNCATE TABLE " + table;
                stmt.executeUpdate(sql);
                System.out.println("Table " + table + " truncated successfully.");
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save all the info in the input Excel files into nobes_visualizer_coursegroup table in the database
     * print the success info in console if import succeed
     * @param file the Excel file to be saved
     * @throws IOException if any error occurs during the retrieval process.
     */
    public void courseGroupImport(File file) throws Exception {
        ExcelReader reader = ExcelUtil.getReader(file);
        Sheet sheet = reader.getSheet();

        List<NobesVisualizerCoursegroup> coursegroups = visualizerCoursegroupService.list(null);
        ArrayList<String> list = new ArrayList<>();

        // remove duplicate course
        for (NobesVisualizerCoursegroup group : coursegroups) {
            String courseName = group.getCourseName();
            if (!list.contains(courseName)) {
                list.add(courseName);
            }
        }

        int lastRowNum = sheet.getLastRowNum();
        int lastCellNum = sheet.getRow(0).getLastCellNum();

        for (int i = 2; i < lastRowNum; i++) {
            for (int j = 0; j < lastCellNum; j++) {
                if (sheet.getRow(i).getCell(j) != null) {
                    if (!list.contains(sheet.getRow(i).getCell(j).getStringCellValue())) {
                        NobesVisualizerCoursegroup nobesVisualizerCoursegroup = new NobesVisualizerCoursegroup();
                        nobesVisualizerCoursegroup.setCourseName(StrUtil.trimToNull(sheet.getRow(i).getCell(j).getStringCellValue()))
                                .setCourseGroup(StrUtil.trimToNull(sheet.getRow(0).getCell(j).getStringCellValue()))
                                .setCourseColor(StrUtil.trimToNull(sheet.getRow(1).getCell(j).getStringCellValue()));

                        // use the service interface for course-group table to save the processed data
                        visualizerCoursegroupService.save(nobesVisualizerCoursegroup);
                    }
                }
            }
        }

        log.info("courseGroup import success");

    }

    public String getCell(Object object) {
        String cellValue = "";

        if (object != null) {
            String cellStringValue = object.toString();

            if (cellStringValue.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
                cellValue = DateUtil.formatDate(DateUtil.parse(StrUtil.trimToNull(cellStringValue), "yyyy-MM-dd"));
            } else {
                cellValue = StrUtil.trimToNull(cellStringValue);
            }
        }
        return cellValue;
    }

}
