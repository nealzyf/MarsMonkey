package com.mars.monkey.component.common.annotation;

import com.mars.monkey.component.common.config.CommonComponentConfig;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

/**
 * Created on 4/19/2019.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(CommonComponentConfig.class)
public @interface MMLibComponent {
}
