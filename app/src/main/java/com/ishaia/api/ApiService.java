package com.ishaia.api;

import com.ishaia.model.UploadImageResponse;
import com.ishaia.model.UploadResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;

public interface ApiService {

    @GET(".")
    Call<UploadResponse> upload(/*@Body Credentials credentials*/);

    @Streaming
    @POST(".")
    Call<ResponseBody> downloadFile(/*@Body Credentials credentials*/);

    @FormUrlEncoded
    @POST(".")
    Call<UploadImageResponse> uploadImage(@Field("image") String image);
}
