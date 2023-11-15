package com.czandlh.resp;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

public class ResultResponse implements Serializable {

    public Boolean success;

    @JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.NON_NULL)
    private String message;

    @JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.NON_NULL)
    private String returnCode;

    @JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.NON_NULL)
    private Object returnData;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public Object getReturnData() {
        return returnData;
    }

    public void setReturnData(Object returnData) {
        this.returnData = returnData;
    }

    public ResultResponse success() {
        this.setSuccess(true);
        this.setReturnCode(Code.SUCCESS);
        this.setMessage(Code.SUCCESS);
        return this;
    }

    public ResultResponse success(Object returnData) {
        this.setSuccess(true);
        this.setReturnCode(Code.SUCCESS);
        this.setMessage(Code.SUCCESS);
        this.setReturnData(returnData);
        return this;
    }

    public ResultResponse fail(String code, String message) {
        this.setSuccess(false);
        this.setReturnCode(code);
        this.setMessage(message);
        return this;
    }

    public ResultResponse fail(String message) {
        this.setSuccess(false);
        this.setReturnCode(Code.SYSTEM_ERROR);
        this.setMessage(message);
        return this;
    }

    public ResultResponse failLogin(String message) {
        this.setSuccess(false);
        this.setReturnCode(Code.NOT_LOGIN);
        this.setMessage(message);
        return this;
    }

    public static class Code {
        public static final String SUCCESS = "200";
        public static final String SYSTEM_ERROR = "500";
        public static final String NOT_LOGIN = "401";
        public static final String TOKEN_EXPIRED = "登录已过期，请重新登陆";
    }


}
