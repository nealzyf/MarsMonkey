package com.mars.monkey.component.common.controller;

import com.mars.monkey.component.common.exception.StandardExceptions;
import com.mars.monkey.component.common.exception.ValidateException;
import com.mars.monkey.component.common.response.DeprecatedResponse;
import com.mars.monkey.component.common.response.ErrorResponse;
import com.mars.monkey.component.common.response.RespCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class HttpExceptionController {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpExceptionController.class);

    @ExceptionHandler(ValidateException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public DeprecatedResponse handleValidateException(ValidateException e) {
        LOGGER.warn("Validate Exception: {}", e.getErrorResponse().getMessage());
        return DeprecatedResponse.status(e.getErrorResponse().getStatus()).data(e.getErrorResponse().getMessage());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateKeyException(DuplicateKeyException e) {
        LOGGER.warn("DuplicateKey: {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(RespCode.ALREAD_EXIST, "Resource already exist.");
        errorResponse.setDetail(e.getMessage());
        return errorResponse;
    }

    @ExceptionHandler(StandardExceptions.HttpCommonException.class)
    public ResponseEntity handleStandardException(StandardExceptions.HttpCommonException e) {
        LOGGER.warn("Validate Exception: {}", e);
        return ResponseEntity.status(e.getHttpStatus()).body(e.getHttpStatus());
    }

}
