package com.mars.monkey.user.controller;

import com.mars.monkey.component.common.response.DeprecatedResponse;
import com.mars.monkey.component.common.response.Response;
import com.mars.monkey.security.annotation.AuthUser;
import com.mars.monkey.security.model.AuthMMUser;
import com.mars.monkey.user.dao.entity.User;
import com.mars.monkey.user.service.UserService;
import com.mars.monkey.utils.transform.JsonTransformUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
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
@RequestMapping("/user")
@EnableAspectJAutoProxy
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping(value = "/info")
    //    @PreAuthorize("hasRole('ADMIN')")
    //    @PreAuthorize("authenticated")
    public Response<User> getUserInfo( @AuthUser AuthMMUser authUser, @RequestBody(required = false) Object body) {
        LOGGER.info("{}" , JsonTransformUtil.toJson(authUser));
        return Response.success(userService.getUserInfo(authUser.getUserId()));
    }

    @PostMapping(value = "/register")
    public DeprecatedResponse register(@RequestBody User user) {
        //        userService.register(user);
        return DeprecatedResponse.success();
    }

}
