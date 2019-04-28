package com.mars.monkey.user.controller;

import com.mars.monkey.component.common.response.DeprecatedResponse;
import com.mars.monkey.user.model.incoming.IncomingInfo;
import com.mars.monkey.user.service.incoming.SalaryTaxCoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created on 3/25/2019.
 *
 * @author eyoufzh
 */
@RestController
@RequestMapping("/incoming")
public class IncomingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IncomingController.class);

    @Autowired
    private SalaryTaxCoreService salaryTaxCoreService;

    @PostMapping("calculate")
    public DeprecatedResponse calculate(@RequestBody IncomingInfo incomingInfo) {

        return DeprecatedResponse.success(salaryTaxCoreService.cal(incomingInfo));
    }

}
