package com.nobes.timetable.core.save;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;


@Service
public class TimeService {

    /**
     * convert the cell in data format into a string
     * @param cell a cell in an Excel file
     * @return stirng format of that cell.
     */
    public String getTime(Cell cell) {

        String format;

        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            Date date = cell.getDateCellValue();
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            format = dateFormat.format(date);
        } else if (cell.getCellType() == CellType.STRING){
            format = cell.getStringCellValue();
        } else {
            format = null;
        }

        return format;
    }
}
