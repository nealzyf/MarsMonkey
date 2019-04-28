package com.mars.monkey.component.common.controller;

import com.mars.monkey.component.common.exception.ServiceViewException;
import com.mars.monkey.component.common.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ServiceExceptionController {
    private final static Logger LOGGER = LoggerFactory.getLogger(ServiceExceptionController.class);

    @ExceptionHandler(ServiceViewException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Response> handleServiceViewException(ServiceViewException e) {
        logServiceViewException(e);
        Response response = Response.withCode(e.getClassifiedError().getErrorCode()).withMessage(e.getClassifiedError().getErrorDescription()).withData(e.getAdditionalError()).build();
        if (e.getClassifiedError() == ServiceViewException.NOT_AUTHORIZED) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    private void logServiceViewException(ServiceViewException e) {
        LOGGER.warn("ServiceViewException message : [{}] .  ClassifiedError -> [{}].  ", e.getMessage(), e.getClassifiedError());
    }

}
