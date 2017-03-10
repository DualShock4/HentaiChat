package com.gabriel.hentaichat.presenter;

import com.gabriel.hentaichat.adapter.ContactAdapter;
import com.gabriel.hentaichat.model.ContactModel;
import com.gabriel.hentaichat.mvp.ContactMVP;

/**
 * Created by gabriel on 2017/3/3.
 */

public class ContactPresenter implements ContactMVP.Presenter {

    private ContactMVP.View view;
    private ContactMVP.Model model;

    public ContactPresenter(ContactMVP.View view) {
        this.view = view;
        model = new ContactModel();
    }

    @Override
    public void updateFriendList(ContactAdapter adapter) {
        model.getFriendList(adapter);
    }
}
