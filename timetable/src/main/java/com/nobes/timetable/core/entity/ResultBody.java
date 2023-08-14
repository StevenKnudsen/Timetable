package com.nobes.timetable.core.entity;

import com.nobes.timetable.core.enums.ExceptionEnum;
import lombok.Data;

@Data
public class ResultBody {
    private Integer respCode;

    private String respDesc;

    private Object obj;

    public ResultBody() {
    }

    public static ResultBody success(Object data) {
        ResultBody rb = new ResultBody();
        rb.setRespCode(ExceptionEnum.SUCCESS.getResultCode());
        rb.setRespDesc(ExceptionEnum.SUCCESS.getResultMsg());
        rb.setObj(data);
        return rb;
    }

    public static ResultBody error(Object data) {
        ResultBody rb = new ResultBody();
        rb.setRespCode(ExceptionEnum.ERROR.getResultCode());
        rb.setRespDesc(ExceptionEnum.ERROR.getResultMsg());
        rb.setObj(data);
        return rb;
    }
}
