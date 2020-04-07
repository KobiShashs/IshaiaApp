package com.ishaia.api;

import com.ishaia.model.UploadResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST(".")
    Call<UploadResponse> upload(/*@Body Credentials credentials*/);
}
