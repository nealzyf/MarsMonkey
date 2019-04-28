//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.mars.monkey.component.common.service;

import com.mars.monkey.component.common.utils.IpUtils;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class EnvService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnvService.class);
    @Autowired
    private Environment environment;
    private String PROFILE_ACTIVE;
    private String PROP_HOST_IP;
    private String PROP_HOST_NAME;
    private Integer SERVER_PORT;
    private String APPLICATION_NAME;

    public EnvService() {
    }

    @PostConstruct
    private void init() {
        LOGGER.debug("Property init start");
        try {
            this.PROP_HOST_IP = IpUtils.getIp();
        } catch (Exception var3) {
            LOGGER.error("Property [PROP_HOST_IP] fetch error...", var3);
        }

        try {
            this.PROP_HOST_NAME = IpUtils.getHostName();
        } catch (Exception var2) {
            LOGGER.error("Property [PROP_HOST_NAME] fetch error...", var2);
        }

        if (StringUtils.isNotBlank(this.getEnvVariableVal("server.port"))) {
            this.SERVER_PORT = Integer.parseInt(this.getEnvVariableVal("server.port"));
        }

        if (StringUtils.isNotBlank(this.getEnvVariableVal("spring.application.name"))) {
            this.APPLICATION_NAME = this.getEnvVariableVal("spring.application.name");
        }

        this.PROFILE_ACTIVE = this.getEnvVariableVal("spring.profiles.active");
        if (null == this.PROFILE_ACTIVE) {

            LOGGER.error("Property fetch error, spring.profiles.active is miss...");

        } else {
            this.PROFILE_ACTIVE = this.PROFILE_ACTIVE.toUpperCase();
        }

        LOGGER.debug("Property init false");

    }

    public String getProfileActive() {
        return this.PROFILE_ACTIVE;
    }

    public String getHostIp() {
        return this.PROP_HOST_IP;
    }

    public String getHostName() {
        return this.PROP_HOST_NAME;
    }

    public Integer getApplicationPort() {
        return this.SERVER_PORT;
    }

    public String getApplicationName() {
        return this.APPLICATION_NAME;
    }

    public String getEnvVariableVal(String key) {
        return StringUtils.isNotBlank(key) ? this.environment.getProperty(key) : null;
    }

    public Integer getEnvVariableVal(String key, Integer dvi) {
        String r = this.getEnvVariableVal(key);
        return StringUtils.isNotBlank(r) ? Integer.parseInt(r) : dvi;
    }

    public Long getEnvVariableVal(String key, Long dvl) {
        String r = this.getEnvVariableVal(key);
        return StringUtils.isNotBlank(r) ? Long.parseLong(r) : dvl;
    }
}
