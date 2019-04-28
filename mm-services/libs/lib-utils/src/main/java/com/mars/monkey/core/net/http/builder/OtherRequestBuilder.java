package com.mars.monkey.core.net.http.builder;

import com.mars.monkey.core.net.http.request.OtherRequest;
import com.mars.monkey.core.net.http.request.RequestCall;

import okhttp3.RequestBody;

/**
 * Created on 2018/2/22.
 *
 * @author YouFeng.Zhu
 */
public class OtherRequestBuilder extends OkHttpRequestBuilder<OtherRequestBuilder> {
    private RequestBody requestBody;
    private String method;
    private String content;

    public OtherRequestBuilder(String method) {
        this.method = method;
    }

    @Override
    public RequestCall build() {
        return new OtherRequest(requestBody, content, method, https, url, tag, params, headers, id).build();
    }

    public OtherRequestBuilder requestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    public OtherRequestBuilder requestBody(String content) {
        this.content = content;
        return this;
    }

}
