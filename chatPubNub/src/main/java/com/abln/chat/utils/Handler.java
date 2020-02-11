package com.abln.chat.utils;


import com.google.gson.JsonObject;

import java.util.Map;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;


public interface Handler {



    @POST("{a}")
    Single<BaseResponse<JsonObject>> revertstatus(@Path(value = "a",encoded = true) String info,@Body RequestGlobal revert);





    @POST("{a}")
    Single<BaseResponse<ModChat>> getdata(@Path(value = "a", encoded = true)String info ,@Body AccountOne data);




    // handling the main data  sets her .





}