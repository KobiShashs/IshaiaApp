package com.ishaia.security;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EncryptionInterceptor implements Interceptor {
    private static final String TAG = EncryptionInterceptor.class.getSimpleName();
    private final CryptoStrategy mEncryptionStrategy;

    //injects the type of encryption to be used
    public EncryptionInterceptor(CryptoStrategy mEncryptionStrategy) {
        this.mEncryptionStrategy = mEncryptionStrategy;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        RequestBody rawBody = request.body();
        String encryptedBody = "";


        MediaType mediaType = MediaType.parse("text/plain; charset=utf-8");
        if (mEncryptionStrategy != null) {
            try {
                String rawBodyString = CryptoUtil.requestBodyToString(rawBody);
                encryptedBody = mEncryptionStrategy.encrypt(rawBodyString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("No encryption strategy!");
        }
        RequestBody body = RequestBody.create(mediaType, encryptedBody);

        //build new request
        request = request.
                newBuilder()
                .header("Content-Type", body.contentType().toString())
                .header("Content-Length", String.valueOf(body.contentLength()))
                .method(request.method(), body).build();

        return chain.proceed(request);
    }

}