package com.abln.futur.interfaces;


import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

/**
 * Created by Bharath on 06/24/2019.
 */

public interface NetworkOperation {


    @POST
    Call<Object> getNetworkCall(
            @Url String apiUrl,
            @HeaderMap Map<String, String> headerMap,
            @Body Object requestBody
    );


    @GET
    Call<Object> makeGetCall(
            @Url String url
    );

    @POST("addpost")
    Call<ResponseBody> uploadMultiFile(@Body RequestBody file);

    @Multipart
    @POST("update-resume")
    Call<Object> updateResume(@Part MultipartBody.Part body, @Part("apikey") RequestBody apiKey);

    @Multipart
    @POST("user-avatar")
    Call<Object> updateUserAvatar(@Part MultipartBody.Part body, @Part("apikey") RequestBody apiKey);


//    @Multipart
//    @POST("addpost")
//    Call<Object> uploadMultipleFilesDynamic(
//            @Part("apikey") RequestBody apikey,
//            @Part("company_name") RequestBody company_name,
//            @Part("job_title") RequestBody job_title,
//            @Part("job_experience") RequestBody job_experience,
//            @Part("expdate") RequestBody expdate,
//            @Part("lat") RequestBody lat,
//            @Part("lng") RequestBody lng,
//            @Part MultipartBody.Part pdfFile,
//            @Part List<MultipartBody.Part> files_s1tos10);


    @Multipart
    @POST("addpost")
    Call<Object> uploadMultipleFilesDynamic(
            @Part("apikey") RequestBody apikey,
            @Part("company_name") RequestBody company_name,
            @Part("job_title") RequestBody job_title,
            @Part("job_experience") RequestBody job_experience,
            @Part("expdate") RequestBody expdate,
            @Part("lat") RequestBody lat,
            @Part("lng") RequestBody lng,
            @Part MultipartBody.Part pdfFile,
            @Part MultipartBody.Part files_s1,
            @Part MultipartBody.Part files_s2,
            @Part MultipartBody.Part files_s3,
            @Part MultipartBody.Part files_s4,
            @Part MultipartBody.Part files_s5,
            @Part MultipartBody.Part files_s6,
            @Part MultipartBody.Part files_s7,
            @Part MultipartBody.Part files_s8,
            @Part MultipartBody.Part files_s9,
            @Part MultipartBody.Part files_s10);


}
