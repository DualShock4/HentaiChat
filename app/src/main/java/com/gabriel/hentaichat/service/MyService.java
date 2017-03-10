package com.gabriel.hentaichat.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;

import com.gabriel.hentaichat.model.ContactModel;
import com.tencent.TIMConversationType;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageListener;

import java.io.IOException;
import java.util.List;

public class MyService extends Service {
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
                boolean needToNotify = false;
                for (TIMMessage timMessage : list) {
                    if (timMessage.getConversation().getType().equals(TIMConversationType.C2C)) {
                        //只要有一条c2c消息，就响铃通知用户
                        needToNotify = true;
                    } else if (timMessage.getConversation().getType().equals(TIMConversationType.System)) {
                        //如果有系统通知，刷新好友列表
                        ContactModel contactModel = new ContactModel();
                        contactModel.getFriendList();
                    }
                }
                if (!needToNotify) return false;
                try {
                    Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
                    vibrator.vibrate(new long[]{0, 200,200,200}, -1);
                    Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(getApplicationContext(), uri);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }
}
