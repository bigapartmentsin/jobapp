package com.abln.chat.ui.Network;

import com.abln.chat.utils.FuturClient;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.abln.chat.utils.IMNetworkOperation;

import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Dispatcher;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class IMNetworkCall {
    private IMNetworkOperation networkOperation;
    private Retrofit mRetrofit;
    private String jsonResponse;
   // http://www.pdf995.com/samples/pdf.pdf
    public IMNetworkCall() {
        mRetrofit = new Retrofit
                .Builder()
                .baseUrl("https://www.futur.ai/")//TODO Url
                .addConverterFactory(GsonConverterFactory.create())
                .client(getClient())
                .build();
    }

    public void makeNetworkCall(String url, final IMNetworkResponse imNetworkResponse) {
        networkOperation = mRetrofit.create(IMNetworkOperation.class);
        System.out.println("TIME1-------"+ Calendar.getInstance().getTimeInMillis());
        Call<Object> networkCall = networkOperation.makeGetCall(url);
        networkCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                System.out.println("TIME2-----"+ Calendar.getInstance().getTimeInMillis());

                if (response != null && response.body() != null) {
                    jsonResponse = response.body().toString();

                      System.out.println("response"+jsonResponse);

                    System.out.println("response"+response.body());
                    Gson gson = new Gson();
                    if (jsonResponse.startsWith("[")) {
                        JsonArray jsonArray = gson.toJsonTree(response.body()).getAsJsonArray();
                        imNetworkResponse.processFinish(jsonArray.toString());
                    } else {
                        JsonObject jsonObject = gson.toJsonTree(response.body()).getAsJsonObject();
                        imNetworkResponse.processFinish(jsonObject.toString());
                    }
                }else {
                    imNetworkResponse.processFinish("");

                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
            System.out.println("new msg");
            imNetworkResponse.processFinish("");
            }
        });
    }

    public static OkHttpClient getClient() {
        OkHttpClient okHttpClient;
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
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
        httpClient.connectTimeout(10, TimeUnit.MINUTES)
                .readTimeout(10, TimeUnit.MINUTES)

                .dispatcher(dispatcher);

        okHttpClient = httpClient.build();

        return okHttpClient;
    }
}
