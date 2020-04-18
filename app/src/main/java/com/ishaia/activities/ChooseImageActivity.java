package com.ishaia.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ishaia.App;
import com.ishaia.R;
import com.ishaia.api.ApiService;
import com.ishaia.model.UploadImageResponse;
import com.ishaia.model.UploadResponse;
import com.ishaia.utils.FileUtils;
import com.ishaia.utils.UnsafeOkHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChooseImageActivity extends AppCompatActivity {

    private ImageView img;
    private EditText imgTitle;
    private Button buttonChooseCamera;
    private Button buttonChooseGallery;
    private Button buttonUpload;

    private static final int IMG_REQUEST = 777;
    public static final int REQUEST_IMAGE = 100;
    public static final int REQUEST_PERMISSION = 200;
    private Bitmap bitmap;

    String imageFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_image);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        }

        setViews();
        setListeners();
    }

    private void setViews() {
        img = (ImageView) findViewById(R.id.image_view);
        imgTitle = (EditText) findViewById(R.id.edit_text_img_title);
        buttonChooseCamera = (Button) findViewById(R.id.button_choose_image_camera);
        buttonChooseGallery = (Button) findViewById(R.id.button_choose_image_gallery);
        buttonUpload = (Button) findViewById(R.id.button_upload);
    }

    private void setListeners() {
        buttonChooseCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCameraIntent();
            }
        });

        buttonChooseGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageFromGallery();
            }
        });

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonUploadCommand();
            }
        });


    }

    private void buttonUploadCommand() {
        String image = imageToString();
        String title  = imgTitle.getText().toString();


        OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(App.get().BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        ApiService apiService = retrofit.create(ApiService.class);

        Bitmap bitmap = FileUtils.getBitmapFromImageView(img);
        File file = FileUtils.getImage(bitmap);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

        System.out.println("aaaaa");
      // UploadImageResponse

        Call<UploadImageResponse> call = apiService.uploadTheImage3(image);
        // Call<UploadImageResponse> call = apiService.uploadImage2(filePart);
        call.enqueue(new Callback<UploadImageResponse>() {
            @Override
            public void onResponse(Call<UploadImageResponse> call, Response<UploadImageResponse> response) {
                System.out.println("bbbbbbb");
                System.out.println(call.request().url());
                System.out.println(response.code());
                System.out.println(response.isSuccessful());
                System.out.println(response.message());
                System.out.println(response.raw());
                System.out.println(response);
                System.out.println(new Gson().toJson(response));
                UploadImageResponse uploadImageResponse = response.body();
                String res = uploadImageResponse.getPicture()+"\n"+uploadImageResponse.getSong();
                Toast.makeText(ChooseImageActivity.this, "res:"+res, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<UploadImageResponse> call, Throwable t) {
                System.out.println("cccccccc");
                System.out.println(t.fillInStackTrace());
                System.out.println(t.getCause());
                System.out.println(t.getMessage());
                Toast.makeText(ChooseImageActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });





















//        App.get().getApiService().uploadImage(image).enqueue(new Callback<UploadImageResponse>() {
//            @Override
//            public void onResponse(Call<UploadImageResponse> call, Response<UploadImageResponse> response) {
//                UploadImageResponse uploadImageResponse = response.body();
//                String res = uploadImageResponse.getPicture()+"\n"+uploadImageResponse.getSong();
//                Toast.makeText(ChooseImageActivity.this, "res", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(Call<UploadImageResponse> call, Throwable t) {
//                System.out.println(t.fillInStackTrace());
//                System.out.println(t.getCause());
//                System.out.println(t.getMessage());
//                Toast.makeText(ChooseImageActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }


    private void openCameraIntent() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            }
            catch (IOException e) {
                e.printStackTrace();
                return;
            }
            Uri photoUri = FileProvider.getUriForFile(this, getPackageName() +".provider", photoFile);
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(pictureIntent, REQUEST_IMAGE);
        }
    }


        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            if (requestCode == REQUEST_PERMISSION && grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Thanks for granting Permission", Toast.LENGTH_SHORT).show();
                }
            }
        }





    private void selectImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                img.setImageBitmap(bitmap);
                img.setVisibility(View.VISIBLE);
                imgTitle.setVisibility(View.VISIBLE);
                imgTitle.setText("image" + System.currentTimeMillis());
                buttonChooseCamera.setEnabled(false);
                buttonChooseGallery.setEnabled(false);
                buttonUpload.setEnabled(true);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        else if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {

                img.setImageURI(Uri.parse(imageFilePath));
                bitmap = imageView2Bitmap(img);
                img.setImageBitmap(bitmap);
                img.setVisibility(View.VISIBLE);
                imgTitle.setVisibility(View.VISIBLE);
                imgTitle.setText("image" + System.currentTimeMillis());
                buttonChooseCamera.setEnabled(false);
                buttonChooseGallery.setEnabled(false);
                buttonUpload.setEnabled(true);
            }
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "You cancelled the operation", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String imageToString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }

    private Bitmap imageView2Bitmap(ImageView view){
        Bitmap bitmap = ((BitmapDrawable)view.getDrawable()).getBitmap();
        return bitmap;
    }

        private File createImageFile() throws IOException{

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String imageFileName = "IMG_" + timeStamp + "_";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File image = File.createTempFile(imageFileName, ".jpg", storageDir);
            imageFilePath = image.getAbsolutePath();

            return image;
        }
}
