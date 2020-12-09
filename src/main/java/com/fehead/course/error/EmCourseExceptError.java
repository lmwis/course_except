package com.fehead.course.error;

import com.fehead.lang.error.CommonError;
import com.fehead.lang.error.EmBusinessError;

/**
 * @Description: 无课表错误类型枚举
 *  implement 通用错误
 *  无课表额外用户信息相关错误:21001-21999
 * @Author: lmwis
 * @Date 2020-12-03 11:27
 * @Version 1.0
 */
public enum EmCourseExceptError implements CommonError {
    SUST_JWC_LOGIN_FAIL(21001,"教务处登录失败，用户名或密码错误"),
    USER_ONT_LOGIN_JWC(21002,"用户未登录教务系统")
    ;
    private EmCourseExceptError(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    private int errCode;
    private String errMsg;
    @Override
    public int getErrorCode() {
        return errCode;
    }

    @Override
    public String getErrorMsg() {
        return errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }

    public static EmCourseExceptError valueOfByCode(int errCode){
        for (EmCourseExceptError value : EmCourseExceptError.values()) {
            if(value.getErrorCode()==errCode){
                return value;
            }
        }
        return null;
    }
}
