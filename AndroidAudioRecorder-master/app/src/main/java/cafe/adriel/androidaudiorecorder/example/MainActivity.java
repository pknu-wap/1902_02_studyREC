package cafe.adriel.androidaudiorecorder.example;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_RECORD_AUDIO = 0;

    //시간으로 파일명 생성

    static long time = System.currentTimeMillis();  //시간 받기
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");

    //포멧 변환  형식 만들기
    static Date dd = new Date(time);  //받은 시간을 Date 형식으로 바꾸기
    static String strTime = sdf.format(dd); //Data 정보를 포멧 변환하기


    Intent intent;

    // 48kHz크기로 wav파일을 지원한다고 함.
    public static String AUDIO_FILE_PATH =
            Environment.getExternalStorageDirectory().getPath() + "/StudyRec/recorded_audio " + strTime + ".wav";

    public static Context context;
    public String text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = new Intent(this, MyServiceApp.class);


        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        sdPath += "/StudyRec";                  //이 부분은 메인 함수에서 최초 실행되는 게 좋을 듯,
        File file = new File(sdPath);
        file.mkdirs();                          //루트가 없으면 생성 있으면 통과하는

        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(
                    new ColorDrawable(ContextCompat.getColor(this, R.color.colorPrimaryDark)));
        }

        Util.requestPermission(this, Manifest.permission.RECORD_AUDIO);
        Util.requestPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        context = this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_RECORD_AUDIO) {
            if (resultCode == RESULT_OK) {
                stopService(intent);

                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                Log.e("111111", "들어왔음");
                show();
                Toast.makeText(this, "Audio recorded successfully!", Toast.LENGTH_SHORT).show();
            }

            else if (resultCode == RESULT_CANCELED) {
                stopService(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                Toast.makeText(this, "Audio was not recorded", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void recordAudio(View v) {
        startService(intent);
        AndroidAudioRecorder.with(this)
                // Required
                .setFilePath(AUDIO_FILE_PATH)

                // 뒷배경색 변경하는거 values/colors.xml에 있음.
                .setColor(ContextCompat.getColor(this, R.color.recorder_bg))
                .setRequestCode(REQUEST_RECORD_AUDIO)

                // Optional
                .setSource(AudioSource.MIC)
                .setChannel(AudioChannel.STEREO)
                .setSampleRate(AudioSampleRate.HZ_48000)
                .setAutoStart(false)
                .setKeepDisplayOn(true)

                // Start recording
                .record();
    }

    public void storageLayout(View v){
        Intent intent = new Intent(MainActivity.this, RResultActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    //대화창에서 수정 선택 시
    public void show(){

        final EditText edittext = new EditText(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("AlertDialog Title");
        builder.setMessage("녹음 파일 이름 입력");
        builder.setView(edittext);
        builder.setPositiveButton("입력",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        text = edittext.getText().toString();        // EditText에 입력된 문자열값을 얻기

                        //recorded_audio.wac -> edittext 창에서 받아온 문자열 값인 text 로 이름 변경
                        File filePre = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/StudyRec","recorded_audio " + strTime + ".wav");
                        File fileNow = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/StudyRec",text);

                        //파일 이름이 잘 바뀌었으면
                        if(filePre.renameTo(fileNow)){
                            Toast.makeText(getApplicationContext(), text+"파일 저장", Toast.LENGTH_SHORT).show();
                            AUDIO_FILE_PATH =
                                    Environment.getExternalStorageDirectory().getPath() + "/StudyRec/"+text;
                            //파일 이름이 잘 바뀌지 않았으면
                        }else{
                            Toast.makeText(getApplicationContext(), text+"파일 저장 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();

    }
}