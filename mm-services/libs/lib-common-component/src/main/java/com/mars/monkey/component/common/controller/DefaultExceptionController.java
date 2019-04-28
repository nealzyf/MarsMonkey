package com.mars.monkey.component.common.controller;

import com.mars.monkey.component.common.response.RespCode;
import com.mars.monkey.component.common.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DefaultExceptionController {
    private final static Logger LOGGER = LoggerFactory.getLogger(DefaultExceptionController.class);


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response handleException(Exception e) {
        LOGGER.error("Unexcepted exception: \n", e);
        return Response.<String>withCode(RespCode.INTERNAL_ERROR).withMessage("Unexcepted exception").withData(e.getMessage()).build();
    }
}
