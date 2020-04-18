package com.ishaia.model;

import com.google.gson.annotations.SerializedName;

public class ImageClass {

    @SerializedName("title")
    private String title;

    @SerializedName("image")
    private String image;

    @SerializedName("response")
    private String response;

    public String getResponse() {
        return response;
    }
}
