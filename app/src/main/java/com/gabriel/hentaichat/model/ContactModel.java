package com.gabriel.hentaichat.model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gabriel.hentaichat.ConstantValues;
import com.gabriel.hentaichat.adapter.ContactAdapter;
import com.gabriel.hentaichat.mvp.ContactMVP;
import com.gabriel.hentaichat.util.ApiUtil;
import com.gabriel.hentaichat.util.SQLiteUtil;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gabriel on 2017/3/3.
 */

public class ContactModel implements ContactMVP.Model {

    @Override
    public void getFriendList() {
        FriendListPost post = new FriendListPost();
        post.TagList = new ArrayList<>();
        post.TagList.add("Tag_SNS_IM_Group");
        post.TagList.add("Tag_SNS_IM_Remark");
        post.TagList.add("Tag_Profile_IM_Nick");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantValues.QCLOUD_BASE_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiUtil.IFriendList friendList = retrofit.create(ApiUtil.IFriendList.class);
        friendList.getFriendList(ConstantValues.SIG,
                ConstantValues.QCLOUD_MANAGER,
                String.valueOf(ConstantValues.SDK_APP_ID),
                String.valueOf((int) (Math.random() * Integer.MAX_VALUE)),
                "json", post)
                .enqueue(new Callback<FriendListGet>() {
                    @Override
                    public void onResponse(Call<FriendListGet> call, Response<FriendListGet> response) {
                        saveData(response.body());
                    }

                    @Override
                    public void onFailure(Call<FriendListGet> call, Throwable t) {

                    }
                });

    }

    private void saveData(final FriendListGet friendListGet) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                SQLiteDatabase friendListDb = SQLiteUtil.getFriendListDb();
                for (FriendListGet.FriendInfo friendInfo : friendListGet.InfoItem) {
                    ContentValues values = new ContentValues();
                    values.put(SQLiteUtil.FRIEND_DB_IDENTIFIER, friendInfo.Info_Account);
                    if (friendInfo.SnsProfileItem != null) {
                        for (FriendListGet.FriendInfo.InfoDetail infoDetail : friendInfo.SnsProfileItem) {
                            if (infoDetail.Tag.equals("Tag_Profile_IM_Nick")) {
                                values.put(SQLiteUtil.FRIEND_DB_NAME, infoDetail.Value);
                            } else if (infoDetail.Tag.equals("Tag_SNS_IM_Remark")) {
                                values.put(SQLiteUtil.FRIEND_DB_REMARK, infoDetail.Value);
                            } else if (infoDetail.Tag.equals("Tag_SNS_IM_Group")) {
                                values.put(SQLiteUtil.FRIEND_DB_TEAM, infoDetail.Value);
                            }
                        }
                    }
                    friendListDb.insert(SQLiteUtil.FRIEND_DB_TABLE_NAME, null, values);
                }
            }
        }.start();
    }
}
