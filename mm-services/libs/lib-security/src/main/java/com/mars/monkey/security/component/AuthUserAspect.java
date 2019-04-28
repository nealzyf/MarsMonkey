package com.mars.monkey.security.component;

import com.mars.monkey.component.common.exception.ServiceViewException;
import com.mars.monkey.security.model.AuthMMUser;
import com.mars.monkey.security.model.TokenUser;
import com.mars.monkey.security.utils.SecurityUtils;
import java.lang.annotation.Annotation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.SoftException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created on 5/9/2019.
 *
 * @author YouFeng Zhu
 */
@Aspect
@Component
public class AuthUserAspect {

    //    @TokenUser
    private final static Logger LOGGER = LoggerFactory.getLogger(AuthUserAspect.class);

    //    @Pointcut("args(Long,Map) && @args(com.mars.monkey.security.annotation.TokenUser)")
    //    @Pointcut("@args(com.mars.monkey.security.annotation.TokenUser)")
    //    @Pointcut("@annotation(com.mars.monkey.security.annotation.TokenUser)")
    //    @Pointcut("execution(com.mars.monkey.component.common.response.DeprecatedResponse *(com.mars.monkey.security.model.TokenUser))" )
    @Pointcut("execution(public * *(.., @com.mars.monkey.security.annotation.AuthUser (*), ..)) && within(com.mars.monkey..*.*Controller)")
    public void authUserInterceptor() {

    }

    @Before("authUserInterceptor()")
    public void before(JoinPoint thisJoinPoint) {

        MethodSignature signature = (MethodSignature) thisJoinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        Class<?>[] parameterTypes = signature.getMethod().getParameterTypes();
        Annotation[][] annotations;
        try {
            annotations = thisJoinPoint.getTarget().getClass().
                    getMethod(methodName, parameterTypes).getParameterAnnotations();
        } catch (Exception e) {
            throw new SoftException(e);
        }
        //        int i = 0;
        Object[] args = thisJoinPoint.getArgs();
        for (int i = 0; i < args.length; i++)
        {
            if(args[i] == null){
                continue;
            }
            for (Annotation annotation : annotations[i]) {
                if (annotation.annotationType() == com.mars.monkey.security.annotation.AuthUser.class) {
                    if (args[i] instanceof AuthMMUser) {
                        AuthMMUser authUser = (AuthMMUser) args[i];
                        TokenUser tokenUser = SecurityUtils.getUser();
                        if (tokenUser == null) {
                            throw ServiceViewException.withClassifiedError(ServiceViewException.NOT_AUTHORIZED).withAdditionalError("Not login or session expired.").build();
                        }
                        authUser.setUserId(tokenUser.getUserId());
                        authUser.setUsername(tokenUser.getUsername());

                    } else {
                        LOGGER.warn("{}, The type of parameter [{}] is [{}], ignored of annotation [{}]", thisJoinPoint, i, args[i].getClass(),
                                com.mars.monkey.security.annotation.AuthUser.class);
                    }

                }
            }
        }
    }
}
