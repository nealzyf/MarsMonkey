package com.mars.monkey.user.service;

import com.mars.monkey.component.common.response.DeprecatedResponse;
import com.mars.monkey.user.service.remote.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created on 4/17/2019.
 *
 * @author YouFeng Zhu
 */
@Service
public class ProfileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileService.class);

    @Autowired
    private StorageService storageService;

    public String uploadProfilePhoto(MultipartFile file) {
        DeprecatedResponse response = storageService.upload(file);
        return response.getData().toString();
    }
}
