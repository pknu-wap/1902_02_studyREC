package cafe.adriel.androidaudiorecorder.example;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.IOException;

public class ResultActivity extends AppCompatActivity {

    MediaPlayer player;
    int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

//        //파일저장
//        File sdcard = Environment.getExternalStorageDirectory();
//        File file = new File(sdcard, "recorded_audio.wav");
//        final String filename = file.getAbsolutePath();
//        Log.d("MainActivity", "저장할 파일 명 : " + filename);


        //다른 자바액티비티의 변수 가져오기
        //final String Filename = ((RecordActivity) RecordActivity.context).filename;

        final String filename = ((MainActivity) MainActivity.context).AUDIO_FILE_PATH;

        //재생버튼 눌렀을때
        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (player != null) {
                        player.release();
                        player = null;
                    }

                    player = new MediaPlayer();
                    player.setDataSource(filename);
                    player.prepare();
                    player.start();


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        });


        //일시정지버튼 눌렀을때
        findViewById(R.id.pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(player !=null)

                {
                    //현재값을 position에 저장해놓음. 일시정지니까
                    position = player.getCurrentPosition();
                    //그리고 멈춰
                    player.pause();

                }
            }
        });

        //재시작버튼 눌렀을때
        findViewById(R.id.restart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resumeAudio();
            }

            private void resumeAudio() {
                if (player != null && !player.isPlaying()) {
                    //아까 저장해놓은 위치 불러옴
                    player.seekTo(position);
                    //그리고 다시 시작해
                    player.start();
                }
            }
        });


        //정지버튼 눌렀을때
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
