package cafe.adriel.androidaudiorecorder;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MyService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        android.util.Log.i("서비스 테스트lib", "onCreate()");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        android.util.Log.i("서비스 테스트lib", "onDestroy()");

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        android.util.Log.i("서비스 테스트lib", "onStartCommand()");

        return super.onStartCommand(intent, flags, startId);
    }
}