package com.abln.futur.common;


import com.abln.futur.activites.JobData;
import com.abln.futur.activites.JobInfo;
import com.abln.futur.activites.MobileApplicants;
import com.abln.futur.common.models.AccountOne;
import com.abln.futur.common.models.AppliedData;
import com.abln.futur.common.models.GetImages;
import com.abln.futur.common.models.GetTotalNumberPost;
import com.abln.futur.common.models.ImageData;
import com.abln.futur.common.models.Mkey;
import com.abln.futur.common.models.ModelAnaly;
import com.abln.futur.common.models.Mstories;
import com.abln.futur.common.models.PostDatabase;
import com.abln.futur.common.models.RequestGlobal;
import com.abln.futur.common.models.SetUser;
import com.abln.futur.common.models.TotalNumber;
import com.abln.futur.common.models.UpdateKey;
import com.abln.futur.common.models.UserData;
import com.abln.futur.common.newview.Experience;
import com.abln.futur.common.postjobs.GetNormalpost;
import com.abln.futur.common.postjobs.post;
import com.abln.futur.common.savedlist.PostedJobsmodel;
import com.abln.futur.common.savedlist.UserList;
import com.abln.futur.common.searchjob.Result;
import com.abln.futur.common.searchjob.Search;
import com.abln.futur.module.job.activities.MyPostedJobListRequest;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.Map;

import dagger.BindsOptionalOf;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

import static com.abln.futur.common.ConstantUtils.th;


public interface Handler {




    @Multipart
    @POST("/v1/thumbnail")
    Single<BaseResponse<JsonObject>> addthumbnail(@Part("apikey") RequestBody description, @Part MultipartBody.Part file);


    @Multipart
    @POST("v1/s1")
    Single<BaseResponse<JsonObject>> addNewFiles(@Part("apikey") RequestBody description, @Part MultipartBody.Part file);


    @Multipart
    @POST("{a}")
    Single<BaseResponse<JsonObject>> addClinicFiles(@Path(value = "aa", encoded = true) String info ,@Part("apikey") RequestBody description, @Part MultipartBody.Part file);



    @Multipart
    @POST("v1/addpost")
    Single<BaseResponse<GetNormalpost>> addNewPost(@PartMap() Map<String, RequestBody> data, @Part MultipartBody.Part file);


    @Multipart
    @POST("v1/user-avatar")
    Single<BaseResponse<JsonObject>> uploadImg(@PartMap() Map<String, RequestBody> data, @Part MultipartBody.Part file);


    @Multipart
    @POST("v1/s1")
    Single<BaseResponse<JsonObject>> s1(@PartMap() Map<String, RequestBody> data, @Part MultipartBody.Part file);


    @Multipart
    @POST("v1/s2")
    Single<BaseResponse<JsonObject>> s2( @PartMap() Map<String, RequestBody> data, @Part MultipartBody.Part file);




    @Multipart
    @POST("v1/s3")
    Single<BaseResponse<JsonObject>> s3( @PartMap() Map<String, RequestBody> data, @Part MultipartBody.Part file);


    @Multipart
    @POST("v1/s4")
    Single<BaseResponse<JsonObject>> s4( @PartMap() Map<String, RequestBody> data, @Part MultipartBody.Part file);



    @Multipart
    @POST("v1/s5")
    Single<BaseResponse<JsonObject>> s5( @PartMap() Map<String, RequestBody> data, @Part MultipartBody.Part file);



    @Multipart
    @POST("v1/s6")
    Single<BaseResponse<JsonObject>> s6(@PartMap() Map<String, RequestBody> data, @Part MultipartBody.Part file);



    @Multipart
    @POST("v1/s7")
    Single<BaseResponse<JsonObject>> s7(@PartMap() Map<String, RequestBody> data, @Part MultipartBody.Part file);


    @Multipart
    @POST("v1/s8")
    Single<BaseResponse<JsonObject>> s8(@PartMap() Map<String, RequestBody> data, @Part MultipartBody.Part file);



    @POST("{a}")
    Single<BaseResponse<Address>> fulldata(@Path(value = "a",encoded = true)String info ,@Body post data);


    @POST("{a}")
    Single<BaseResponse<JsonObject>> onSaved(@Path(value = "a",encoded = true)String info ,@Body post data);



    @POST("{a}")
    Single<BaseResponse<JsonObject>> onApply(@Path(value = "a",encoded = true) String info ,@Body post data);

    @Multipart
    @POST("v1/s9")
    Single<BaseResponse<JsonObject>> s9(@PartMap() Map<String, RequestBody> data, @Part MultipartBody.Part file);


    @Multipart
    @POST("v1/s10")
    Single<BaseResponse<JsonObject>> s10(@PartMap() Map<String, RequestBody> data, @Part MultipartBody.Part file);


    @POST("{a}")
    Single<BaseResponse<GetNormalpost>> normal(@Path(value = "a",encoded = true) String info , @Body post removeImageReq);


    @POST("{a}")
    Single<BaseResponse<Address>> listofaddress(@Path(value = "a",encoded = true) String info ,@Body post removeImageReq);


    @POST("{a}")
    Single<BaseResponse<Address>> searchjobs(@Path(value = "a",encoded = true) String info , @Body Search search);


    @POST("{a}")
    Single<BaseResponse<Result>> addtitel(@Path(value = "a",encoded = true) String info ,@Body post search);


    @POST("{a}")
    Single<BaseResponse<JsonObject>> checkresume(@Path(value = "a",encoded = true) String info ,@Body post search);


    @POST("{a}")
    Single<BaseResponse<Experience>> getExperience(@Path(value = "a",encoded = true) String info ,@Body post search);


    @Multipart
    @POST("v1/update-resume")
    Single<BaseResponse<JsonObject>> uploadpdf(@PartMap() Map<String, RequestBody> data, @Part MultipartBody.Part file);


    @POST("{a}")
    Single<BaseResponse<Address>> getSavedData(@Path(value = "a",encoded = true) String info ,@Body post search);


    @POST("{a}")
    Single<BaseResponse<Address>> getPosted(@Path(value = "a",encoded = true) String info ,@Body post search);



    @POST("{a}")
    Single<BaseResponse<Title>> getTitle(@Path(value = "a",encoded = true) String ifo , @Body post info);


    @Multipart
    @POST("v1/s10")
    Single<BaseResponse<JsonObject>> sendthubmnail(@PartMap() Map<String, RequestBody> partMap, @Part MultipartBody.Part file);



    @POST("{a}")
    Single<BaseResponse<Title>> getfulldata(@Path(value = "a",encoded = true) String info);



    @POST("{a}")
    Single<BaseResponse<JsonObject>> gettotaljobpost(@Path(value = "a",encoded = true) String info,@Body GetTotalNumberPost total);



    @POST("{a}")
    Single<BaseResponse<JsonObject>> accountone(@Path(value = "a",encoded = true) String info,@Body AccountOne one);


    @POST("{a}")
    Single<BaseResponse<JsonObject>> accounttwo(@Path(value = "a",encoded = true) String info,@Body AccountOne two);


    @POST("{a}")
    Single<BaseResponse<JsonObject>> accoutthree(@Path(value = "a",encoded = true) String info,@Body AccountOne three);


    @POST("{a}")
    Single<BaseResponse<JsonObject>> accountfour(@Path(value = "a",encoded = true) String info,@Body AccountOne four);


    @POST("{a}")
    Single<BaseResponse<TotalNumber>> getTotalData(@Path(value = "a",encoded = true) String info,@Body AccountOne five);


    @POST("{a}")
    Single<BaseResponse<UserData>> getuserdata2(@Path(value = "a",encoded = true) String info,@Body AccountOne data);




    @POST("{a}")
    Single<BaseResponse<JsonObject>> setuserdata2(@Path(value = "a",encoded = true) String info,@Body SetUser setUser);


    @POST("{a}")
    Single<BaseResponse<JsonObject>> getimages(@Path(value = "a",encoded = true) String info,@Body GetImages getImages);






    @POST("{a}")
    Single<BaseResponse<PostedJobsmodel>> getuserpost(@Path(value = "a", encoded = true) String info ,@Body post key);


    @POST("{a}")
    Single<BaseResponse<JsonObject>> remove(@Path(value = "a",encoded = true) String info,@Body MyPostedJobListRequest mjp);



    @POST("{a}")
    Single<BaseResponse<Mstories>> getstories(@Path(value = "a",encoded = true) String info,@Body Mkey key);



    @POST("{a}")
    Single<BaseResponse<JobData>> updateinfo(@Path(value = "a",encoded = true) String info,@Body UpdateKey key);



    @POST("{a}")
    Single<BaseResponse<JsonObject>> caccept(@Path(value = "a",encoded = true) String info,@Body RequestGlobal global);


    @POST("{a}")
    Single<BaseResponse<JsonObject>> creject(@Path(value = "a",encoded = true) String info,@Body RequestGlobal creject);

    @POST("{a}")
    Single<BaseResponse<JsonObject>> creview(@Path(value = "a",encoded = true) String info,@Body RequestGlobal creview);


    @POST("{a}")
    Single<BaseResponse<JsonObject>>   cchat(@Path(value = "a",encoded = true) String info,@Body RequestGlobal canchat);


    @POST("{a}")
    Single<BaseResponse<AppliedData>> recapplied(@Path(value = "a",encoded = true) String info,@Body RequestGlobal reqglobal);

    @POST("{a}")
    Single<BaseResponse<JsonObject>> reccanview(@Path(value = "a",encoded = true) String info,@Body RequestGlobal recanview);


    @POST("{a}")
    Single<BaseResponse<MobileApplicants>> mobileapplicaion(@Path(value = "a",encoded = true) String info,@Body RequestGlobal mobileapp);


    @POST("{a}")
    Single<BaseResponse<JsonObject>> revertstatus(@Path(value = "a",encoded = true) String info,@Body RequestGlobal revert);

    @POST("{a}")
    Single<BaseResponse<ModelAnaly>> getdatainfo(@Path(value = "a",encoded = true) String info,@Body RequestGlobal key);


    @POST("{a}")
    Single<BaseResponse<JsonObject>> uploadphoto(@Path(value = "a",encoded = true) String info,@Body RequestGlobal key);

    @POST("{a}")
    Single<BaseResponse<JsonObject>> getavatar(@Path(value = "a",encoded = true) String info,@Body RequestGlobal key);



    //v1/view-resume
    @POST("{a}")
    Single<BaseResponse<FileDataHandler>> viewfile(@Path(value = "a", encoded = true) String info,@Body RequestGlobal key);



    @POST("/v1/get-avatar")
    Single<BaseResponse<ImageData>> getavatar(@Body RequestGlobal key);


    @POST("v1/users-search")
    Single<BaseResponse<AppliedData>> getusersearch(@Body RequestGlobal key);


    @POST("v1/users-search2")
    Single<BaseResponse<AppliedData>> getUserfilterData(@Body RequestGlobal key);






    @POST("/v1/recruiter-post-info")
    Single<BaseResponse<PostDatabase>> getpostusers(@Body RequestGlobal key);


    @POST("/v1/send-rec-can")
    Single<BaseResponse<JsonObject>> senduserpost(@Body RequestGlobal key);














}