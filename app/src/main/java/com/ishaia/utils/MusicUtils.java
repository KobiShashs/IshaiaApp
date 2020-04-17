package com.ishaia.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.ishaia.R;

import java.io.IOException;

public class MusicUtils {

    private static MediaPlayer mediaPlayer = new MediaPlayer();

    public static void playSound(Context ctx, int soundResId) {
        MediaPlayer mp = MediaPlayer.create(ctx, soundResId);
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

    public static void playSong(Context ctx,String songPath) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(songPath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            Log.v(ctx.getString(R.string.app_name), e.getMessage());
        }
    }
}
