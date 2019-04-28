package com.mars.monkey.core.net.http.builder;

import java.util.Map;

/**
 * Created on 2018/2/22.
 *
 * @author YouFeng.Zhu
 */
public interface HasParamsable
{
    OkHttpRequestBuilder params(Map<String, String> params);
    OkHttpRequestBuilder addParams(String key, String val);
}
