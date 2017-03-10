package com.gabriel.hentaichat.mvp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;

/**
 * Created by gabriel on 2017/1/23.
 */

public interface LoginMVP {
    interface Model{

    }

    interface View {
        Activity getActivity();

        void showInputPwdDialog();

        void goToHome();

        void showIdentifierPic(Bitmap bitmap);
    }

    interface Presenter{
        void checkIdentifier(String phone, String code);

        void signUp(String pwd);

        void login(String username, String pwd);

        void qq_login();

        void onDestroy();

        void onActivityResult(int requestCode, int resultCode, Intent data);

        void getIdentifierCode(String phone);

        void reaskLoginIdentifierImage();
    }
}
