package com.mars.monkey.component.common.controller;

import com.mars.monkey.component.common.service.EnvService;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created on 4/26/2019.
 *
 * @author YouFeng Zhu
 */

@RequestMapping({ "/common" })
@RestController
public class HealthController {
    private static final Logger log = LoggerFactory.getLogger(HealthController.class);
    private final String CURRENT_TIME = "currentTime";
    private final String CURRENT_APPLICATION_NAME = "applicationName";
    private final String CURRENT_APPLICATION_HOST_IP = "hostIp";
    private final String CURRENT_APPLICATION_HOST_NAME = "hostName";
    private final String CURRENT_APPLICATION_PORT = "port";

    @Autowired
    private EnvService envService;

    public HealthController() {
    }

    @GetMapping({ "/ping" })
    public Map<String, Object> ping() {
        Map<String, Object> map = new HashMap();
        map.put(CURRENT_APPLICATION_NAME, this.envService.getApplicationName());
        map.put(CURRENT_APPLICATION_HOST_IP, this.envService.getHostIp());
        map.put(CURRENT_APPLICATION_HOST_NAME, this.envService.getHostName());
        map.put(CURRENT_APPLICATION_PORT, this.envService.getApplicationPort());
        map.put(CURRENT_TIME, Calendar.getInstance());
        return map;
    }

}
