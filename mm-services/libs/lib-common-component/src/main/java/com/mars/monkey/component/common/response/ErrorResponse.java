package com.mars.monkey.component.common.response;

/**
 * Created on 3/24/2017.
 *
 * @author YouFeng.Zhu
 */
@Deprecated
public class ErrorResponse {

    private int status;
    private String message;
    private String detail;

    public ErrorResponse(String message, String detail) {
        this.message = message;
        this.detail = detail;
    }

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public ErrorResponse(String message) {
        this.status = RespCode.FAILURE;
        this.message = message;
    }

    public ErrorResponse() {

    }
    public int getStatus() {

        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    private static class ErrorResponseBuilder {

    }

}
