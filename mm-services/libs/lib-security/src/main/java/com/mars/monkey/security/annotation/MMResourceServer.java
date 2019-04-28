package com.mars.monkey.security.annotation;

import com.mars.monkey.security.config.SecurityBeanConfig;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * Created on 4/19/2019.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import(SecurityBeanConfig.class)
public @interface MMResourceServer {
}
