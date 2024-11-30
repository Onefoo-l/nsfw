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
    USERNAME_OR_PASSWORD_ERROR(40006, ResponseConstants.Companion.getUSERNAME_OR_PASSWORD_ERROR()),
    NOT_FOUND_COMMENT(40010, ResponseConstants.Companion.getNOT_FOUND_COMMENT()),
    LABEL_PARAM_ERROR(40011, ResponseConstants.Companion.getLABEL_PARAM_ERROR()),
    COMMENTS_CANNOT_BE_EMPTY(40012, ResponseConstants.Companion.getCOMMENTS_CANNOT_BE_EMPTY()),
    FAILURE(50000, ResponseConstants.Companion.getFAILURE()),
    CUSTOM_FAILURE(50001, ResponseConstants.Companion.getCUSTOM_FAILURE()),
    NEED_LOGIN(50002, ResponseConstants.Companion.getNEED_LOGIN()),
    ADD_LABEL_ERROR(50003, ResponseConstants.Companion.getADD_LABEL_ERROR()),
    USERNAME_IS_EXIST(50004, ResponseConstants.Companion.getUSERNAME_IS_EXIST()),
    TRANSACTION_INSERTION_FAILED(50004, ResponseConstants.Companion.getTRANSACTION_INSERTION_FAILED()),
    DELETE_COMMENT_FAIL(50005, ResponseConstants.Companion.getDELETE_COMMENT_FAIL()),
    ERROR_UPLOAD_WITH_THE_FILE(50006, ResponseConstants.Companion.getERROR_UPLOAD_WITH_THE_FILE()),
    ERROR_DOW_WITH_THE_FILE(50007, ResponseConstants.Companion.getERROR_DOW_WITH_THE_FILE());

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
