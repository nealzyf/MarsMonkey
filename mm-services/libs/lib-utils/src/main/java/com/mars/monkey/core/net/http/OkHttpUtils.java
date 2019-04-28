package com.mars.monkey.core.net.http;

import com.mars.monkey.core.net.http.builder.OtherRequestBuilder;
import com.mars.monkey.core.net.http.builder.PostFormBuilder;
import com.mars.monkey.core.net.http.https.HttpsUtils;
import com.mars.monkey.core.net.http.builder.GetBuilder;
import com.mars.monkey.core.net.http.builder.HeadBuilder;
import com.mars.monkey.core.net.http.builder.PostFileBuilder;
import com.mars.monkey.core.net.http.builder.PostStringBuilder;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * Created on 2018/2/22.
 *
 * @author YouFeng.Zhu
 */
public class OkHttpUtils {
    public static final long DEFAULT_MILLISECONDS = 10_000L;
    private volatile static OkHttpUtils instance;
    private OkHttpClient okHttpClient;
    private OkHttpClient okHttpsClient;

    private OkHttpUtils() {
        this.okHttpClient = new OkHttpClient.Builder().connectTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS).readTimeout(DEFAULT_MILLISECONDS,
                TimeUnit.MILLISECONDS).writeTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS).build();
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        this.okHttpsClient = new OkHttpClient.Builder().sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager).connectTimeout(
                DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS).readTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS).writeTimeout(DEFAULT_MILLISECONDS,
                TimeUnit.MILLISECONDS).build();
    }

    private static OkHttpUtils initClient() {
        if (instance == null) {
            synchronized (OkHttpUtils.class) {
                if (instance == null) {
                    instance = new OkHttpUtils();
                }
            }
        }
        return instance;
    }

    public static OkHttpUtils getInstance() {
        return initClient();
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public OkHttpClient getOkHttpsClient() {
        return okHttpsClient;
    }

    public static GetBuilder get() {
        return new GetBuilder();
    }

    public static PostStringBuilder post() {
        return new PostStringBuilder();
    }

    public static PostFileBuilder postFile() {
        return new PostFileBuilder();
    }

    public static PostFormBuilder postForm() {
        return new PostFormBuilder();
    }

    public static OtherRequestBuilder put() {
        return new OtherRequestBuilder(METHOD.PUT);
    }

    public static HeadBuilder head() {
        return new HeadBuilder();
    }

    public static OtherRequestBuilder delete() {
        return new OtherRequestBuilder(METHOD.DELETE);
    }

    public static OtherRequestBuilder patch() {
        return new OtherRequestBuilder(METHOD.PATCH);
    }

    public void cancelTag(Object tag) {
        for (Call call : okHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : okHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    public static class METHOD {
        public static final String HEAD = "HEAD";
        public static final String DELETE = "DELETE";
        public static final String PUT = "PUT";
        public static final String PATCH = "PATCH";
    }
}

