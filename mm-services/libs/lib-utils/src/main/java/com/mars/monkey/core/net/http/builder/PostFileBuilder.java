package com.mars.monkey.core.net.http.builder;

import com.mars.monkey.core.net.http.request.PostFileRequest;
import com.mars.monkey.core.net.http.request.RequestCall;

import java.io.File;

import okhttp3.MediaType;

/**
 * Created on 2018/2/22.
 *
 * @author YouFeng.Zhu
 */
public class PostFileBuilder extends OkHttpRequestBuilder<PostFileBuilder> {
    private File file;
    private MediaType mediaType;

    public OkHttpRequestBuilder file(File file) {
        this.file = file;
        return this;
    }

    public OkHttpRequestBuilder mediaType(MediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    @Override
    public RequestCall build() {
        return new PostFileRequest(https, url, tag, params, headers, file, mediaType, id).build();
    }

}
