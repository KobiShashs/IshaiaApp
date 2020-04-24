package com.ishaia.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ishaia.App;
import com.ishaia.R;
import com.ishaia.api.ApiService;
import com.ishaia.model.UploadResponse;
import com.ishaia.utils.FileUtils;
import com.ishaia.utils.UnsafeOkHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button buttonGet;
    private TextView textViewResult;

    private Button buttonPost;
    private TextView textViewResult2;

    private Button buttonMyFiles;
    private Button buttonPostWithArg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);
        setViews();
        setListeners();
    }

    private void setListeners() {
        buttonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getExampleCommand();
            }
        });

        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postExampleDownloadFile();
            }
        });

        buttonMyFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyFilesActivity.class);
                startActivity(intent);
            }
        });

        buttonPostWithArg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChooseImageActivity.class);
                startActivity(intent);
            }
        });
    }

    private void postExampleDownloadFile() {
        Call<ResponseBody> call = App.get().getApiService().downloadFile();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "server contacted and has file");
                    boolean writtenToDisk = FileUtils.writeResponseBodyToDisk(getApplicationContext(), response.body());
                    Log.d(TAG, "file download was a success? " + writtenToDisk);
                } else {
                    Log.d(TAG, "server contact failed");
                }

                textViewResult2.setText("Download was successful!");

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                textViewResult2.setText("Sorry...not available...");
            }
        });
    }

    private void getExampleCommand() {

        OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(App.get().BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<UploadResponse> call = apiService.upload();

        call.enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                textViewResult.setText(response.body().getContent());
            }

            @Override
            public void onFailure(Call<UploadResponse> call, Throwable t) {
                textViewResult.setText("Sorry...not available...");
            }
        });
    }

    private void setViews() {
        buttonGet = (Button) findViewById(R.id.button_get);
        textViewResult = (TextView) findViewById(R.id.text_view_result);
        buttonPost = (Button) findViewById(R.id.button_post);
        textViewResult2 = (TextView) findViewById(R.id.text_view_result2);
        buttonMyFiles = (Button) findViewById(R.id.button_my_files);
        buttonPostWithArg = (Button) findViewById(R.id.button_post_image);
    }


}
