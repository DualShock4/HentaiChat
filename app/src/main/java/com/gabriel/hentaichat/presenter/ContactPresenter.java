package com.gabriel.hentaichat.presenter;

import android.content.ContentValues;
import android.database.Cursor;

import com.gabriel.hentaichat.adapter.ContactAdapter;
import com.gabriel.hentaichat.model.ContactModel;
import com.gabriel.hentaichat.mvp.ContactMVP;
import com.gabriel.hentaichat.util.SQLiteUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        Cursor cursor = SQLiteUtil.getFriendListDb().query(SQLiteUtil.FRIEND_DB_TABLE_NAME,
                null, null, null, null, null, null);
        List<HashMap<String, String>> friendList = new ArrayList<>();
        while (cursor.moveToNext()) {
            HashMap<String, String> hashMap = new HashMap<>();
            String id = cursor.getString(cursor.getColumnIndex(SQLiteUtil.FRIEND_DB_IDENTIFIER));
            String name = cursor.getString(cursor.getColumnIndex(SQLiteUtil.FRIEND_DB_NAME));
            hashMap.put("id", id);
            hashMap.put("name", name);
            friendList.add(hashMap);
        }
        view.updateRecyclerView(friendList);
    }
}
