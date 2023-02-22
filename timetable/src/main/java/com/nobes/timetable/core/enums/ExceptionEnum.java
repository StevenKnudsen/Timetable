package com.nobes.timetable.core.enums;

import com.nobes.timetable.core.exception.BaseErrorInfoInterface;

public enum ExceptionEnum implements BaseErrorInfoInterface {
    SUCCESS(200, "success"),
    NOT_FOUND(404, "not found");

    private Integer respCode;
    /**
     * 错误描述
     */
    private String respDesc;

    ExceptionEnum(Integer respCode, String respDesc) {
        this.respCode = respCode;
        this.respDesc = respDesc;
    }

    @Override
    public Integer getResultCode() {
        return respCode;
    }

    @Override
    public String getResultMsg() {
        return respDesc;
    }
}
