package com.gabriel.hentaichat.presenter;

import com.gabriel.hentaichat.model.MessageModel;
import com.gabriel.hentaichat.mvp.MessageMVP;

/**
 * Created by gabriel on 2017/3/3.
 */

public class MessagePresenter implements MessageMVP.Presenter {
    private MessageMVP.Model model;
    private MessageMVP.View view;

    public MessagePresenter(MessageMVP.View view) {
        model = new MessageModel();
        this.view = view;
    }
}
