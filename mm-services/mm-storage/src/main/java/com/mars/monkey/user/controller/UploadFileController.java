package com.mars.monkey.user.controller;

import com.mars.monkey.component.common.response.DeprecatedResponse;
import com.mars.monkey.user.service.UploadFileService;
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
@RequestMapping("/upload")
public class UploadFileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadFileController.class);

    @Autowired
    private UploadFileService uploadFileService;

    @PostMapping(value = "/file", consumes = { "multipart/form-data" })
    public DeprecatedResponse upload(@RequestParam MultipartFile file) {
        String url = uploadFileService.uploadFile(file);
        return DeprecatedResponse.success(url);
    }

}
