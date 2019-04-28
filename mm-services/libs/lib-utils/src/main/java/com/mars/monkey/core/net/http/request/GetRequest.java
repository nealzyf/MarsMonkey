package com.mars.monkey.core.net.http.request;

import java.util.Map;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created on 2018/2/22.
 *
 * @author YouFeng.Zhu
 */
public class GetRequest extends OkHttpRequest
{
    public GetRequest(boolean https, String url, Object tag, Map<String, String> params, Map<String, String> headers,int id)
    {
        super(https ,url, tag, params, headers,id);
    }

    @Override
    protected RequestBody buildRequestBody()
    {
        return null;
    }

    @Override
    protected Request buildRequest(RequestBody requestBody)
    {
        return builder.get().build();
    }


}
