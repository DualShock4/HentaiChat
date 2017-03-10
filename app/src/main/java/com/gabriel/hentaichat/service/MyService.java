package com.gabriel.hentaichat.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;

import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageListener;

import java.io.IOException;
import java.util.List;

public class MyService extends Service {
    private MediaPlayer mMediaPlayer;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        TIMManager.getInstance().addMessageListener(new TIMMessageListener() {
            @Override
            public boolean onNewMessages(List<TIMMessage> list) {
                try {
                    Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
                    vibrator.vibrate(1000);
                    Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                    if(mMediaPlayer == null) mMediaPlayer = new MediaPlayer();
                    mMediaPlayer.setDataSource(getApplicationContext(), uri);
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }
}
