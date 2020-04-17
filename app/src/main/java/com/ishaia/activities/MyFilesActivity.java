package com.ishaia.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ishaia.R;
import com.ishaia.utils.MusicUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyFilesActivity extends AppCompatActivity {

    private ListView list;
    private ArrayAdapter<String> listAdapter;

    private List<String> ListOfFiles = new ArrayList<String>();
    private static final String path = "/storage/emulated/0/Android/data/com.ishaia/files";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);

        list = (ListView) findViewById(R.id.list_view);
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: " + files.length);
        for (File file : files) {
            Log.d("Files", "FileName:" + file.getName());
            ListOfFiles.add(file.getName());
        }

        listAdapter = new ArrayAdapter<String>(this, R.layout.row_layout, R.id.file_name, ListOfFiles);
        list.setAdapter(listAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                MusicUtils.playSong(getApplicationContext(),path + "/" + ListOfFiles.get(position));

                Log.d("Path", path + ListOfFiles.get(position));
            }
        });

    }


}
