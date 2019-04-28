package com.mars.monkey.user.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * @author 徐靖峰
 * Date 2018-04-19
 */

//@Configuration
//    @EnableResourceServer
//    @EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceServerConfiguration.class);

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        //            resources.resourceId(DEMO_RESOURCE_ID).stateless(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().permitAll();
        //        http.authorizeRequests().antMatchers("/order/**").authenticated();
        //                .antMatchers("/user/**").authenticated();//配置order访问控制，必须认证过后才可以访问

    }



}

