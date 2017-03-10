package com.gabriel.hentaichat.mvp;

import com.tencent.TIMMessage;

import java.util.List;

/**
 * Created by gabriel on 2017/3/5.
 */

public interface ChatMVP {
    interface Model {
    }

    interface View {
        void updateRecyclerView();

        List<TIMMessage> getAdapterData();

        void recordFail();
    }

    interface Presenter {
        void getMessage(TIMMessage timMessage);

        void onDestory();

        void sendTextMessage(String input);

        void recordSound();

        void sendSoundMessage(boolean needToSend);
    }


}
