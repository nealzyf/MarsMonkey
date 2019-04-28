package com.mars.monkey.component.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import static com.mars.monkey.component.common.response.Response.CResponseBuilder.newBuilder;
import java.io.Serializable;

/**
 * Created on 5/15/2019.
 *
 * @author YouFeng Zhu
 */
public class Response<T> implements Serializable {

    private static final long serialVersionUID = 2051815931911707656L;

    private int code;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public Response() {
    }

    public Response(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> Response<T> success() {
        return new CResponseBuilder<T>().withCode(RespCode.SUCCESS).withMessage("success").build();
    }

    public static <T> Response<T> success(T data) {
        return new CResponseBuilder<T>().withCode(RespCode.SUCCESS).withMessage("success").withData(data).build();
    }

    public static <T> Response<T> fail() {
        return new CResponseBuilder<T>().withCode(RespCode.PROCESSING_FAILURE).withMessage("failure").build();
    }

    public static <T> Response<T> fail(T data) {
        return new CResponseBuilder<T>().withCode(RespCode.PROCESSING_FAILURE).withMessage("failure").withData(data).build();
    }

    public static Response failMsg(String message) {
        return newBuilder().withCode(RespCode.PROCESSING_FAILURE).withMessage(message).build();
    }

    public static <T> CResponseBuilder<T> withCode(int code) {
        return new CResponseBuilder<T>().withCode(code);
    }

    public static final class CResponseBuilder<T> {
        public T getData() {
            return data;
        }

        private int code;
        private String message;
        private T data;

        public CResponseBuilder() {
        }

        public static <T> CResponseBuilder<T> newBuilder() {
            return new CResponseBuilder<>();
        }

        public CResponseBuilder<T> withCode(int code) {
            this.code = code;
            return this;
        }

        public CResponseBuilder<T> withMessage(String message) {
            this.message = message;
            return this;
        }

        public CResponseBuilder<T> withData(T data) {
            this.data = data;
            return this;
        }

        public Response<T> build() {
            Response<T> response = new Response<>();
            response.setCode(code);
            response.setMessage(message);
            response.setData(data);
            return response;
        }
    }

}
