package com.mars.monkey.component.common.response;

import java.util.ArrayList;

/**
 * Created on 2017/10/26.
 *
 * @author YouFeng.Zhu
 */
@Deprecated
public class DeprecatedResponse {
    private int status;

    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public DeprecatedResponse(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public DeprecatedResponse() {
    }

    public DeprecatedResponse(int status, Object data) {
        this.status = status;
        this.data = data;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static DeprecatedResponse.ResponseBuilder status(int status) {
        return new DeprecatedResponse.ResponseBuilder(status);
    }

    public static DeprecatedResponse success() {
        return new DeprecatedResponse.ResponseBuilder(RespCode.SUCCESS).build();
    }

    public static DeprecatedResponse success(Object data) {
        return new DeprecatedResponse.ResponseBuilder(RespCode.SUCCESS).data(data);
    }

    public static class ResponseBuilder {

        private final int status;

        public ResponseBuilder(int status) {
            this.status = status;
        }

        public DeprecatedResponse build() {
            return data(new ArrayList<>());
        }

        public DeprecatedResponse data(Object data) {
            return new DeprecatedResponse(this.status, data);
        }
    }

    @Override
    public String toString() {
        return "DeprecatedResponse{" + "status=" + status + ", data=" + data + '}';
    }

}
