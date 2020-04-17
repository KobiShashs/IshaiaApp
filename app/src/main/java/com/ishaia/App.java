package com.ishaia;
import android.app.Application;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ishaia.api.ApiService;
import com.ishaia.security.DecryptionImpl;
import com.ishaia.security.DecryptionInterceptor;
import com.ishaia.security.EncryptionImpl;
import com.ishaia.security.EncryptionInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {
    private static final String TAG = App.class.getSimpleName();
    //private static final String BASE_URL = "https://ishaia7.pythonanywhere.com/";
    private static final String BASE_URL = "http://ec2-3-21-102-110.us-east-2.compute.amazonaws.com/";
    private static App INSTANCE;


    private ApiService apiService;

    public static App get() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();

//        //Gson Builder
//        GsonBuilder gsonBuilder = new GsonBuilder();
//        Gson gson = gsonBuilder.create();

        // HttpLoggingInterceptor
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("HttpLoggingInterceptor",message);
            }
        });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        /**
         * injection of interceptors to handle encryption and decryption
         */

        //Encryption Interceptor
        EncryptionInterceptor encryptionInterceptor = new EncryptionInterceptor(new EncryptionImpl());
        //Decryption Interceptor
        DecryptionInterceptor decryptionInterceptor = new DecryptionInterceptor(new DecryptionImpl());


        // OkHttpClient. Be conscious with the order
        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                //httpLogging interceptor for logging network requests
                .addInterceptor(httpLoggingInterceptor)
                //Encryption interceptor for encryption of request data
                .addInterceptor(encryptionInterceptor)
                // interceptor for decryption of request data
                .addInterceptor(decryptionInterceptor)
                .build();

        //Retrofit
//        Retrofit retrofit = new Retrofit.Builder()
//                .client(okHttpClient)
//                .baseUrl(BASE_URL)
//                // for serialization
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
                Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                //   .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();



        //ApiService
        apiService = retrofit.create(ApiService.class);

    }


    public ApiService getApiService() {
        return apiService;
    }

}