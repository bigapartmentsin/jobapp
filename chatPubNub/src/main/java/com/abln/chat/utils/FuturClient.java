package com.abln.chat.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.CertificatePinner;
import okhttp3.Dispatcher;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.platform.Platform;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * It is a singleton class, We will have only one instance across the app.
 */

public class FuturClient {
    public static OkHttpClient okHttpClient;

    /**
     * Private constructor to avoid duplicate instance.
     */
    private FuturClient() {
    }

    public static OkHttpClient getClient() {
        try {
            if (okHttpClient == null) {
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                httpClient.hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
                httpClient.retryOnConnectionFailure(true);
                httpClient.addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Interceptor.Chain chain) throws IOException {
                        Request original = chain.request();
                        Request request = original.newBuilder()
                                .header("Content-Type", "application/json")
                                .method(original.method(), original.body())
                                .build();
                        return chain.proceed(request);
                    }
                });



                Dispatcher dispatcher = new Dispatcher();
                dispatcher.setMaxRequests(5);
                dispatcher.setMaxRequestsPerHost(10);
                httpClient.connectTimeout(1, TimeUnit.MINUTES)
                        .readTimeout(1, TimeUnit.MINUTES)
                        .dispatcher(dispatcher);
                okHttpClient = httpClient.build();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return okHttpClient;
    }


}
