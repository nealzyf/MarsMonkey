package com.mars.monkey.core.net.http.request;

import com.mars.monkey.core.net.http.OkHttpUtils;
import java.io.IOException;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;
import okhttp3.Authenticator;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 2018/2/22.
 *
 * @author YouFeng.Zhu
 */
public class RequestCall {
    private final static Logger LOGGER = LoggerFactory.getLogger(RequestCall.class);
    private OkHttpRequest okHttpRequest;
    private Request request;
    private Call call;

    private long readTimeOut;
    private long writeTimeOut;
    private long connTimeOut;
    private Proxy proxy;
    private Authenticator authenticator;

    public RequestCall(OkHttpRequest request) {
        this.okHttpRequest = request;
    }

    public RequestCall readTimeOut(long readTimeOut) {
        this.readTimeOut = readTimeOut;
        return this;
    }

    public RequestCall writeTimeOut(long writeTimeOut) {
        this.writeTimeOut = writeTimeOut;
        return this;
    }

    public RequestCall connTimeOut(long connTimeOut) {
        this.connTimeOut = connTimeOut;
        return this;
    }

    public RequestCall proxy(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

    public RequestCall authenticator(Authenticator authenticator) {
        this.authenticator = authenticator;
        return this;
    }

    public Call buildCall() {
        request = generateRequest();

        OkHttpClient client;

        if (okHttpRequest.isHttps()) {
            client = OkHttpUtils.getInstance().getOkHttpClient();
        } else {
            client = OkHttpUtils.getInstance().getOkHttpsClient();
        }

        if (readTimeOut > 0 || writeTimeOut > 0 || connTimeOut > 0 || proxy != null) {
            readTimeOut = readTimeOut > 0 ? readTimeOut : OkHttpUtils.DEFAULT_MILLISECONDS;
            writeTimeOut = writeTimeOut > 0 ? writeTimeOut : OkHttpUtils.DEFAULT_MILLISECONDS;
            connTimeOut = connTimeOut > 0 ? connTimeOut : OkHttpUtils.DEFAULT_MILLISECONDS;

            OkHttpClient.Builder newBuilder = client.newBuilder().readTimeout(readTimeOut, TimeUnit.MILLISECONDS).writeTimeout(writeTimeOut,
                    TimeUnit.MILLISECONDS).connectTimeout(connTimeOut, TimeUnit.MILLISECONDS);
            if (proxy != null) {
                newBuilder.proxy(proxy);
            }
            if (authenticator != null) {
                newBuilder.authenticator(authenticator);
            }

            OkHttpClient clone = newBuilder.build();

            call = clone.newCall(request);
        } else {
            call = client.newCall(request);
        }
        return call;
    }

    private Request generateRequest() {
        return okHttpRequest.generateRequest();
    }

    public Call getCall() {
        return call;
    }

    public Request getRequest() {
        return request;
    }

    public OkHttpRequest getOkHttpRequest() {
        return okHttpRequest;
    }

    public Response execute() throws IOException {
        buildCall();
        return call.execute();
    }

    public Response execute(int retry) throws IOException {
        return execute(retry, 0);
    }

    public Response execute(int retry, int interval) throws IOException {
        return execute(retry, interval, 0);
    }

    public Response execute(int retry, int interval, int timeoutIncrease) throws IOException {
        buildCall();
        int count = 0;
        while (true) {
            try {
                return call.execute();
            } catch (IOException e) {
                if (count < retry) {
                    count++;
                    LOGGER.warn("Fail to send Http request , count [{}]. Request : [{}] , Exception : [{}].", count, this.request, e.getMessage());
                    if (timeoutIncrease != 0) {
                        this.readTimeOut += timeoutIncrease;
                        this.connTimeOut += timeoutIncrease;
                        this.writeTimeOut += timeoutIncrease;
                    }
                } else {
                    LOGGER.warn("Fail to send Http request > [{}] : [{}].", retry, this.request, e);
                    throw e;
                }
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e1) {
                    LOGGER.warn("InterruptedException.", e1);
                }
                buildCall();
            }
        }

    }

    public void cancel() {
        if (call != null) {
            call.cancel();
        }
    }

}
