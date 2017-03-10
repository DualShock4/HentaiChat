package com.gabriel.hentaichat.model;

import android.media.MediaPlayer;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.gabriel.hentaichat.MyApplication;
import com.gabriel.hentaichat.mvp.ChatMVP;
import com.tencent.TIMCallBack;
import com.tencent.TIMElem;
import com.tencent.TIMElemType;
import com.tencent.TIMImage;
import com.tencent.TIMImageElem;
import com.tencent.TIMMessage;
import com.tencent.TIMSoundElem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabriel on 2017/3/6.
 */

public class ChatModel implements ChatMVP.Model {

    public static void playSound(MediaPlayer mediaPlayer, TIMMessage timMessage) {
        mediaPlayer = new MediaPlayer();
        TIMElem element = timMessage.getElement(0);
        if (element.getType() == TIMElemType.Sound) {
            final TIMSoundElem soundElem = (TIMSoundElem) element;
            final String path = MyApplication.getContext().getFilesDir()
                    + File.separator + ((TIMSoundElem) element).getUuid();
            File soundFile = new File(path);
            if (soundFile.exists()) {
                try {
                    mediaPlayer.setDataSource(path);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                final MediaPlayer finalMediaPlayer = mediaPlayer;
                soundElem.getSoundToFile(path, new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {

                    }

                    @Override
                    public void onSuccess(){
                        soundElem.setPath(path);
                        try {
                            finalMediaPlayer.setDataSource(path);
                            finalMediaPlayer.prepare();
                            finalMediaPlayer.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }



        }
    }
}
