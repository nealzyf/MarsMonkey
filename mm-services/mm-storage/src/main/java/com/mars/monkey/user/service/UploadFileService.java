package com.mars.monkey.user.service;

import com.mars.monkey.utils.transform.JsonTransformUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created on 4/10/2019.
 *
 * @author YouFeng Zhu
 */
@Service
public class UploadFileService {

    private final static Logger LOGGER = LoggerFactory.getLogger(UploadFileService.class);
    private String ACCESS_KEY = "dak9vXcQbTGccTkB_x-K6rTYzsGK53dvGF9QIOoF";
    private String SECRET_KEY = "GcD5Xv_j2ywKDmVKp-gIUCEch-q7LgGfVj9zcqHf";

    private String bucketname = "image-test";
    private String domain = "http://ppfbh5348.bkt.clouddn.com/";

    private Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    private UploadManager uploadManager;

    @PostConstruct
    private void init() {
        Zone z = Zone.huadong();
        Configuration cfg = new Configuration(z);
        uploadManager = new UploadManager(cfg);
    }

    public String uploadFile(byte[] data) {
        try {
            Response res = uploadManager.put(data, UUID.randomUUID().toString(), auth.uploadToken(bucketname));
            return contactUrl(parseKey(res));
        } catch (QiniuException e) {
            LOGGER.error("Fail to upload file.", e);
            return null;
        }
    }

    public String uploadFile(MultipartFile file) {
        try {
            return uploadFile(file.getBytes());
        } catch (IOException e) {
            LOGGER.error("Fail to upload file.", e);
            return null;
        }
    }

    private String parseKey(Response res) throws QiniuException {
        Map<String, Object> body = JsonTransformUtil.readValue(res.bodyString(), Map.class);
        return body.get("key").toString();
    }

    private String contactUrl(String key) {
        LOGGER.info("Finish to Upload file . URL: {}", domain + key);
        return domain + key;
    }

}
