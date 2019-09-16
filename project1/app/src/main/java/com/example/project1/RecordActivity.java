package com.example.project1;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;

public class RecordActivity extends AppCompatActivity {
    MediaRecorder recorder;
    String filename;

    /*다른 액티비티에서 선언한 변수 가져올라고 썼었는데 필요가 없어짐
     *public static Context context;*/

    int position = 0; // 다시 시작 기능을 위한 현재 재생 위치 확인 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);


        /*위에 말했듯이 필요가 없어짐 그래도 이 기능 안잊게 일단 냅둠
         *context = this;
         */

        permissionCheck();  //권한 받아오기

        //파일저장
        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard, "recorded.mp4");
        final String filename = file.getAbsolutePath();
        Log.d("MainActivity", "저장할 파일 명 : " + filename);


        //녹음버튼 눌렀을때
        findViewById(R.id.record).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recorder = new MediaRecorder();

                /* 그대로 저장하면 용량이 크다.
                 * 프레임 : 한 순간의 음성이 들어오면, 음성을 바이트 단위로 전부 저장하는 것
                 * 초당 15프레임 이라면 보통 8K(8000바이트) 정도가 한순간에 저장됨
                 * 따라서 용량이 크므로, 압축할 필요가 있음 */
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 어디에서 음성 데이터를 받을 것인지
                recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); // 압축 형식 설정
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                recorder.setOutputFile(filename);

                try {
                    recorder.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                recorder.start();

                Toast.makeText(getApplicationContext(), "녹음 시작.", Toast.LENGTH_SHORT).show();

            }
        });


        //녹음 중지 버튼 눌렀을때
        findViewById(R.id.recordStop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRecording();
            }
        });
    }

    private void stopRecording() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
            Toast.makeText(this, "녹음 중지됨.", Toast.LENGTH_SHORT).show();
        }
    }



    //권한 받아오기 (마이크랑 sd카드)
    public void permissionCheck(){
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1);
        }
    }
}