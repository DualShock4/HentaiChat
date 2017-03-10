package com.gabriel.hentaichat.mvp;

import com.gabriel.hentaichat.adapter.ContactAdapter;
import com.gabriel.hentaichat.model.FriendListGet;

/**
 * Created by gabriel on 2017/3/3.
 */

public interface ContactMVP {
    interface View {

    }

    interface Model {
        void getFriendList(ContactAdapter adapter);
    }

    interface Presenter {

        void updateFriendList(ContactAdapter adapter);
    }
}
