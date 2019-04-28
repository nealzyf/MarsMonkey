package com.mars.monkey.core.net.http.builder;

import com.mars.monkey.core.net.http.request.PostStringRequest;
import com.mars.monkey.core.net.http.request.RequestCall;

import okhttp3.MediaType;

/**
 * Created on 2018/2/22.
 *
 * @author YouFeng.Zhu
 */
public class PostStringBuilder extends OkHttpRequestBuilder<PostStringBuilder> {
    private String content;
    private MediaType mediaType;

    public PostStringBuilder content(String content) {
        this.content = content;
        return this;
    }

    public PostStringBuilder mediaType(MediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    @Override
    public RequestCall build() {
        return new PostStringRequest(https, url, tag, params, headers, content, mediaType, id).build();
    }

}
