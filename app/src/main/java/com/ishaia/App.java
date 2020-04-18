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

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {
    private static final String TAG = App.class.getSimpleName();
    //private static final String BASE_URL = "https://ishaia7.pythonanywhere.com/";
    public static final String BASE_URL = "https://ec2-3-21-102-110.us-east-2.compute.amazonaws.com/";
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


        final TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };

        // Install the all-trusting trust manager
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        // Create an ssl socket factory with our all-trusting manager
        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();


        OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        httpBuilder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
        httpBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        //OkHttpClient okHttpClient = httpBuilder.build();

//        OkHttpClient okHttpClient2 = new OkHttpClient().newBuilder()
//                .connectTimeout(10, TimeUnit.SECONDS)
//                .readTimeout(10, TimeUnit.SECONDS)
//                .writeTimeout(10, TimeUnit.SECONDS)
//                .build();

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
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .build();



        //ApiService
        apiService = retrofit.create(ApiService.class);

    }


    public ApiService getApiService() {
        return apiService;
    }

}