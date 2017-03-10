package com.gabriel.hentaichat.mvp;

import com.gabriel.hentaichat.adapter.ContactAdapter;
import com.gabriel.hentaichat.model.FriendListGet;

import java.util.HashMap;
import java.util.List;

/**
 * Created by gabriel on 2017/3/3.
 */

public interface ContactMVP {
    interface View {

        void updateRecyclerView(List<HashMap<String, String>> friendList);
    }

    interface Model {
        void getFriendList();
    }

    interface Presenter {

        void updateFriendList(ContactAdapter adapter);
    }
}
