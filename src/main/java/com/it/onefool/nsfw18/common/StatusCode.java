package com.it.onefool.nsfw18.common;

import com.it.onefool.nsfw18.common.constants.ResponseConstants;

/**
 * 枚举类状态码
 *
 * @author Onefool
 * @version 1.0
 */
public enum StatusCode {
    //区分与http状态码
    SUCCESS(20000, ResponseConstants.Companion.getSUCCESS()),
    PARAM_ERROR(40000, ResponseConstants.Companion.getPARAM_ERROR()),
    UNAUTHORIZED(40001, ResponseConstants.Companion.getUNAUTHORIZED()),
    FORBIDDEN(40002, ResponseConstants.Companion.getFORBIDDEN()),
    LICENSE_EXPIRED(40003, ResponseConstants.Companion.getLICENSE_EXPIRED()),
    NOT_FOUND(40004, ResponseConstants.Companion.getNOT_FOUND()),
    REQUEST_FORBIDDEN(40005, ResponseConstants.Companion.getREQUEST_FORBIDDEN()),
    NOT_FOUND_COMMENT(40010, ResponseConstants.Companion.getNOT_FOUND_COMMENT()),
    LABEL_PARAM_ERROR(40011, ResponseConstants.Companion.getLABEL_PARAM_ERROR()),
    FAILURE(50000, ResponseConstants.Companion.getFAILURE()),
    CUSTOM_FAILURE(50001, ResponseConstants.Companion.getCUSTOM_FAILURE()),
    NEED_LOGIN(50002, ResponseConstants.Companion.getNEED_LOGIN()),
    ADD_LABEL_ERROR(50003, ResponseConstants.Companion.getADD_LABEL_ERROR()),
    TRANSACTION_INSERTION_FAILED(50004, ResponseConstants.Companion.getTRANSACTION_INSERTION_FAILED());
    private final Integer code;

    private final String message;

    StatusCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    //获取状态码
    public Integer code() {
        return code;
    }

    //获取信息
    public String message() {
        return message;
    }

    @Override
    public String toString() {
        return String.valueOf(this.code);
    }
}
