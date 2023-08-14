package com.nobes.timetable.core.enums;

import com.nobes.timetable.core.exception.BaseErrorInfoInterface;

public enum ExceptionEnum implements BaseErrorInfoInterface {
    SUCCESS(200, "success"),
    ERROR(404, "error");

    private final Integer respCode;
    /**
     * mistake description
     */
    private final String respDesc;

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
