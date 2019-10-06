package cafe.adriel.androidaudiorecorder.example;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MyServiceApp extends Service {
    public MyServiceApp() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        android.util.Log.i("서비스 테스트", "onCreate()");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        android.util.Log.i("서비스 테스트", "onDestroy()");

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        android.util.Log.i("서비스 테스트", "onStartCommand()");

        return super.onStartCommand(intent, flags, startId);
    }
}