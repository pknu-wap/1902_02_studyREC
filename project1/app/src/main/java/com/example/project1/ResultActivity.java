package com.example.project1;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;

import static android.widget.Toast.LENGTH_SHORT;

public class ResultActivity extends AppCompatActivity {

    MediaPlayer player;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard, "recorded.mp4");
        final String filename = file.getAbsolutePath();
        Log.d("MainActivity", "저장할 파일 명 : " + filename);

        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playAudio();
            }

            private void playAudio() {
                try {
                    closePlayer();

                    player = new MediaPlayer();
                    player.setDataSource(filename);
                    player.prepare();
                    player.start();


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private void closePlayer() {
                if (player != null) {
                    player.release();
                    player = null;
                }
            }
        });

        findViewById(R.id.pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseAudio();
            }

            private void pauseAudio() {
                if (player != null) {
                    int position = player.getCurrentPosition();
                    player.pause();


                }
            }
        });

        findViewById(R.id.restart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resumeAudio();
            }

            private void resumeAudio() {
                if (player != null && !player.isPlaying()) {
                    int position = 0;
                    player.seekTo(position);
                    player.start();


                }
            }
        });

        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopAudio();
            }

            private void stopAudio() {
                if (player != null && player.isPlaying()) {
                    player.stop();


                }
            }
        });



    }
}
