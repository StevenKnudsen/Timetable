package com.nobes.timetable.core.save;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nobes.timetable.visualizer.domain.NobesVisualizerCourse;
import com.nobes.timetable.visualizer.domain.NobesVisualizerCoursegroup;
import com.nobes.timetable.visualizer.service.INobesVisualizerCourseService;
import com.nobes.timetable.visualizer.service.INobesVisualizerCoursegroupService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.url}")
    private String url;

    /**
     * Save all the info in the input Excel files into nobes_visualizer_course table in the database
     * print the success info in console if import succeed
     * @param file the Excel file to be saved
     */
    public void courseImport(File file) {

        ExcelReader reader = ExcelUtil.getReader(file);
        List<List<Object>> rows = reader.read();

        for (int i = 1; i < rows.size(); i++) {

            // add the course information to the database only if it does not exist
            if (visualizerCourseService.list(new LambdaQueryWrapper<NobesVisualizerCourse>()
                    .eq(NobesVisualizerCourse::getCatalog, getCell(rows.get(i).get(4)))
                    .eq(NobesVisualizerCourse::getSubject, getCell(rows.get(i).get(3)))).size() == 0) {

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
        }

        log.info("Courses import success");
    }

    /**
     * This method truncates the "nobes_visualizer_course" table in the mydatabase database using the MySQL JDBC driver.
     * It uses a JDBC connection to the database and executes a TRUNCATE TABLE statement to clear all data from the table.
     * The database connection information, including the URL, username, and password, is hardcoded and needs to be changed when using this method in another database.
     */
    public void truncate() {

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

    public void truncateCourseGroup() {

        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            Statement stmt = conn.createStatement();

            String[] tables = {"nobes_visualizer_coursegroup"};

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
     */
    public void courseGroupImport(File file) throws Exception {
        ExcelReader reader = ExcelUtil.getReader(file);
        Sheet sheet = reader.getSheet();

        List<NobesVisualizerCoursegroup> originalCourseGroups = visualizerCoursegroupService.list(null);

        int lastRowNum = sheet.getLastRowNum();
        int lastCellNum = sheet.getRow(0).getLastCellNum();

        for (int i = 2; i < lastRowNum; i++) {
            for (int j = 0; j < lastCellNum; j++) {
                if (sheet.getRow(i).getCell(j) != null) {
                    String group = sheet.getRow(0).getCell(j).getStringCellValue();
                    String color = new DataFormatter().formatCellValue(sheet.getRow(1).getCell(j));
                    String courseName = sheet.getRow(i).getCell(j).getStringCellValue();

                    boolean contains = originalCourseGroups.stream().anyMatch(courseGroup -> courseGroup.getCourseName().equals(courseName) && courseGroup.getCourseGroup().equals(group));

                    if (!contains) {
                        if (courseName != null && !courseName.equals("")) {
                            NobesVisualizerCoursegroup coursegroup = new NobesVisualizerCoursegroup().setCourseName(courseName).setCourseGroup(group).setCourseColor(color);
                            visualizerCoursegroupService.save(coursegroup);
                        }
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
