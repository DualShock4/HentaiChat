package com.gabriel.hentaichat.presenter;

import android.media.MediaRecorder;
import android.os.SystemClock;
import android.widget.Toast;

import com.gabriel.hentaichat.MyApplication;
import com.gabriel.hentaichat.mvp.ChatMVP;
import com.gabriel.hentaichat.util.MessageEvent;
import com.gabriel.hentaichat.util.TimeUtil;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMElem;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageListener;
import com.tencent.TIMSoundElem;
import com.tencent.TIMTextElem;
import com.tencent.TIMValueCallBack;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by gabriel on 2017/3/5.
 */

public class ChatPresenter implements ChatMVP.Presenter, TIMMessageListener {

    private ChatMVP.View view;
    private ChatMVP.Model model;
    private TIMConversation mConversation;
    private String mIdentifier;
    private MediaRecorder mRecorder;
    private String mSoundPath;
    private long mStartRecordeTime;

    public ChatPresenter(ChatMVP.View view, String identifier) {
        this.view = view;
        mIdentifier = identifier;
        TIMManager.getInstance().addMessageListener(this);
        TIMManager.getInstance().addMessageListener(MessageEvent.getInstance());
        mConversation = TIMManager.getInstance().getConversation(TIMConversationType.C2C, mIdentifier);
    }

    @Override
    public void getMessage(TIMMessage timMessage) {
        mConversation.getLocalMessage(20, timMessage, new TIMValueCallBack<List<TIMMessage>>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(final List<TIMMessage> timMessages) {
                view.getAdapterData().addAll(timMessages);
                view.updateRecyclerView();
            }
        });
    }

    @Override
    public void sendTextMessage(String input) {
        TIMMessage timMessage = new TIMMessage();
        TIMTextElem timTextElem = new TIMTextElem();
        timTextElem.setText(input);
        timMessage.addElement(timTextElem);
        view.getAdapterData().add(0,timMessage);
        view.updateRecyclerView();
        mConversation.sendMessage(timMessage, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                view.updateRecyclerView();
            }

            @Override
            public void onSuccess(TIMMessage timMessage) {
                view.updateRecyclerView();
            }
        });
    }

    @Override
    public void recordSound() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mSoundPath = MyApplication.getContext().getFilesDir() + File.separator + mIdentifier +
                "_" + TimeUtil.getTimeStr(SystemClock.currentThreadTimeMillis() / 1000);
        mRecorder.setOutputFile(mSoundPath);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
        }
        mRecorder.start();
        mStartRecordeTime = SystemClock.currentThreadTimeMillis();
    }

    @Override
    public void sendSoundMessage(boolean needToSend) {
        long duration = (SystemClock.currentThreadTimeMillis() - mStartRecordeTime) / 1000;
        if (mRecorder != null) {
            try {
                mRecorder.stop();
                mRecorder.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (needToSend) {
            if (duration < 1) {
                Toast.makeText(MyApplication.getContext(), "录音时间过短", Toast.LENGTH_SHORT).show();
                return;
            }
            TIMMessage timMessage = new TIMMessage();
            TIMSoundElem soundElem = new TIMSoundElem();
            soundElem.setPath(mSoundPath);
            soundElem.setDuration(duration);
            if(timMessage.addElement(soundElem) != 0) {
                Toast.makeText(MyApplication.getContext(), "发送失败", Toast.LENGTH_SHORT).show();
                return;
            }
            view.getAdapterData().add(0,timMessage);
            view.updateRecyclerView();
            mConversation.sendMessage(timMessage, new TIMValueCallBack<TIMMessage>() {//发送消息回调
                @Override
                public void onError(int code, String desc) {//发送消息失败
                    view.updateRecyclerView();
                }

                @Override
                public void onSuccess(TIMMessage msg) {//发送消息成功
                    view.updateRecyclerView();
                }
            });
        } else {
            File file = new File(mSoundPath);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    @Override
    public boolean onNewMessages(List<TIMMessage> list) {
        view.getAdapterData().addAll(list);
        view.updateRecyclerView();
        Toast.makeText(MyApplication.getContext(), "new message", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onDestory() {
        view = null;
        TIMManager.getInstance().removeMessageListener(this);
    }
}
