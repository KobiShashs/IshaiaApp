package com.ishaia;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ishaia.api.ApiService;
import com.ishaia.model.UploadResponse;
import com.ishaia.security.ByteArrayToBase64Adapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Button buttonGet;
    private TextView textViewResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    private void getExampleCommand() {
      App.get().getLoginService().upload().enqueue(new Callback<UploadResponse>() {
          @Override
          public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
             textViewResult.setText(response.body().getContent());
          }

          @Override
          public void onFailure(Call<UploadResponse> call, Throwable t) {
            textViewResult.setText("Sorry...not abailable...");
          }
      });
    }

    private void setViews() {
        buttonGet = (Button) findViewById(R.id.button_get);
        textViewResult = (TextView) findViewById(R.id.text_view_result);
    }

    private void playSound(int soundResId) {
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), soundResId);
        if (mp == null) {
            Log.w("playSound", "Error creating MediaPlayer object to play sound.");
            return;
        }

        mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.e("playSound", "Found an error playing media. Error code: " + what);
                mp.release();
                return true;
            }
        });

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });

        mp.start();
    }
}
