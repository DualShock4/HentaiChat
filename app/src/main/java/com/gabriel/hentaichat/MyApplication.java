package com.gabriel.hentaichat;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.gabriel.hentaichat.model.ContactModel;
import com.gabriel.hentaichat.service.MyService;
import com.tencent.TIMManager;

/**
 * Created by gabriel on 2017/1/23.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        TIMManager.getInstance().init(getApplicationContext());
        startService(new Intent(context, MyService.class));
    }

    public static Context getContext() {
        return context;
    }

}
