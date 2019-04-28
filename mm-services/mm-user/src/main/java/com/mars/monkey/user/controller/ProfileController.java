package com.mars.monkey.user.controller;

import com.mars.monkey.component.common.response.DeprecatedResponse;
import com.mars.monkey.user.service.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created on 3/25/2019.
 *
 * @author eyoufzh
 */
@RestController
@RequestMapping("/user/profile")
public class ProfileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileController.class);


    @Autowired
    private ProfileService profileService;

    @PostMapping(value = "/photo")
    public DeprecatedResponse uploadProfilePhoto(@RequestParam MultipartFile file) {
        profileService.uploadProfilePhoto(file);
        return DeprecatedResponse.success();
    }

}
