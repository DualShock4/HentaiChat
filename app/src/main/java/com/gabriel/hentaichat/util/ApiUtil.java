package com.gabriel.hentaichat.util;

import com.gabriel.hentaichat.model.FriendListGet;
import com.gabriel.hentaichat.model.FriendListPost;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by gabriel on 2017/3/2.
 */

public class ApiUtil {

    public interface IFriendList {
        @POST("sns/friend_get_all")
        Call<FriendListGet> getFriendList(
                @Query("usersig") String usersig,
                @Query("identifier") String identifier,
                @Query("sdkappid") String sdkappid,
                @Query("random") String random,
                @Query("contenttype") String contenttype,
                @Body FriendListPost friendListPost);
    }


}
