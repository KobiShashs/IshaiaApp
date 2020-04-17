package com.ishaia.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.ishaia.R;

public class ChooseImageActivity extends AppCompatActivity {

    private EditText imgTitle;
    private Button buttonChoose;
    private Button buttonUpload;
    private ImageView img;
    private  static  final int IMG_REQUEST = 777;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_image);
        setViews();
        setListeners();
    }

    private void setViews() {
        //imgTitle = (EditText)findViewById(R.id.img_title);
    }

    private void setListeners() {

    }


}
