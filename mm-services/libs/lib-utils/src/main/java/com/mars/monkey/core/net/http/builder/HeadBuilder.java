package com.mars.monkey.core.net.http.builder;

import com.mars.monkey.core.net.http.OkHttpUtils;
import com.mars.monkey.core.net.http.request.OtherRequest;
import com.mars.monkey.core.net.http.request.RequestCall;

/**
 * Created on 2018/2/22.
 *
 * @author YouFeng.Zhu
 */
public class HeadBuilder extends GetBuilder {
    @Override
    public RequestCall build() {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, https, url, tag, params, headers, id).build();
    }
}
