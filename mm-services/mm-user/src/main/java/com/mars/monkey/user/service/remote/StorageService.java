package com.mars.monkey.user.service.remote;

import com.mars.monkey.component.common.response.DeprecatedResponse;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created on 4/17/2019.
 *
 * @author YouFeng Zhu
 */

@FeignClient(name = "storage-service", configuration = StorageService.MultipartSupportConfig.class)
public interface StorageService {

    @PostMapping(value = "/upload/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    DeprecatedResponse upload(@RequestPart(value = "file") MultipartFile file);

    class MultipartSupportConfig {

        @Bean
        @Primary
        @Scope("prototype")
        public Encoder multipartFormEncoder() {
            return new SpringFormEncoder();
        }
    }
}
