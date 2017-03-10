package com.gabriel.hentaichat.presenter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.gabriel.hentaichat.ConstantValues;
import com.gabriel.hentaichat.MyApplication;
import com.gabriel.hentaichat.model.LoginModel;
import com.gabriel.hentaichat.mvp.LoginMVP;
import com.gabriel.hentaichat.util.SpUtil;
import com.tencent.TIMCallBack;
import com.tencent.TIMManager;
import com.tencent.TIMUser;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import tencent.tls.platform.TLSAccountHelper;
import tencent.tls.platform.TLSErrInfo;
import tencent.tls.platform.TLSHelper;
import tencent.tls.platform.TLSLoginHelper;
import tencent.tls.platform.TLSPwdLoginListener;
import tencent.tls.platform.TLSPwdRegListener;
import tencent.tls.platform.TLSUserInfo;

/**
 * Created by gabriel on 2017/1/23.
 */

public class LoginPresenter implements LoginMVP.Presenter {
    private TLSAccountHelper accountHelper;
    private TLSPwdRegListener pwdRegListener;
    private TLSLoginHelper loginHelper;
    private TLSPwdLoginListener pwdLoginListener;

    private static final String TAG = "LoginPresenter";

    private LoginMVP.Model loginModel;

    private LoginMVP.View loginView;

    private Tencent mTencent;

    private IUiListener mQQLoginListener;
    private String pwd;

    public LoginPresenter(LoginMVP.View loginView) {
        this.loginView = loginView;
        loginModel = new LoginModel();
        mTencent = Tencent.createInstance(ConstantValues.APP_ID, MyApplication.getContext());
        loginHelper = TLSLoginHelper.getInstance()
                .init(MyApplication.getContext(), ConstantValues.SDK_APP_ID,
                        ConstantValues.ACCOUNT_TYPE, null);
        accountHelper = TLSAccountHelper.getInstance()
                .init(MyApplication.getContext(), ConstantValues.SDK_APP_ID,
                        ConstantValues.ACCOUNT_TYPE, null);
        pwdRegListener = new PwdRegListener();
        pwdLoginListener = new PwdLoginListener();
    }

    @Override
    public void checkIdentifier(String phone, String code) {
        accountHelper.TLSPwdRegVerifyCode(code, pwdRegListener);
    }

    @Override
    public void signUp(String pwd) {
        this.pwd = pwd;
        accountHelper.TLSPwdRegCommit(pwd, pwdRegListener);
    }

    @Override
    public void login(String username, String pwd) {
        loginHelper.TLSPwdLogin(username, pwd.getBytes(), pwdLoginListener);
    }

    @Override
    public void qq_login() {
        if (!mTencent.isSessionValid()) {
            mQQLoginListener = new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    Toast.makeText(MyApplication.getContext(), "该接口暂时未开放", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(UiError uiError) {

                }

                @Override
                public void onCancel() {

                }
            };
            mTencent.login(loginView.getActivity(), "all", mQQLoginListener);
        }
    }

    @Override
    public void onDestroy() {
        loginView = null;
        mQQLoginListener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, mQQLoginListener);
    }

    @Override
    public void getIdentifierCode(String phone) {
        accountHelper.TLSPwdRegAskCode("86-" + phone, pwdRegListener);
    }

    @Override
    public void reaskLoginIdentifierImage() {
        loginHelper.TLSPwdLoginReaskImgcode(pwdLoginListener);
    }

    /**
     * 腾讯云注册回调接口的实现类
     */
    private class PwdRegListener implements TLSPwdRegListener {

        @Override
        public void OnPwdRegAskCodeSuccess(int i, int i1) {
            Toast.makeText(MyApplication.getContext(), "短信已发送", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void OnPwdRegReaskCodeSuccess(int i, int i1) {
            Toast.makeText(MyApplication.getContext(), "短信已发送", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void OnPwdRegVerifyCodeSuccess() {
            loginView.showInputPwdDialog();
        }

        @Override
        public void OnPwdRegCommitSuccess(TLSUserInfo tlsUserInfo) {
            login(tlsUserInfo.identifier, pwd);
        }

        @Override
        public void OnPwdRegFail(TLSErrInfo tlsErrInfo) {
            Log.i(TAG, "OnPwdRegFail: " + tlsErrInfo.ErrCode+","+tlsErrInfo.ExtraMsg);
            Toast.makeText(MyApplication.getContext(), tlsErrInfo.Title + ":" + tlsErrInfo.Msg, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void OnPwdRegTimeout(TLSErrInfo tlsErrInfo) {
            Log.i(TAG, "OnPwdRegFail: " + tlsErrInfo.ErrCode+","+tlsErrInfo.ExtraMsg);
            Toast.makeText(MyApplication.getContext(), tlsErrInfo.Title + ":" + tlsErrInfo.Msg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 腾讯云登录回调接口的实现类
     */
    private class PwdLoginListener implements TLSPwdLoginListener {

        @Override
        public void OnPwdLoginSuccess(final TLSUserInfo tlsUserInfo) {
            final String userSig = TLSHelper.getInstance().getUserSig(tlsUserInfo.identifier);
            TIMUser user = new TIMUser();
            user.setAppIdAt3rd(String.valueOf(ConstantValues.SDK_APP_ID));
            user.setIdentifier(tlsUserInfo.identifier);
            user.setAppIdAt3rd(String.valueOf(ConstantValues.ACCOUNT_TYPE));
            TIMManager.getInstance().login((int) ConstantValues.SDK_APP_ID,
                    user, userSig, new TIMCallBack() {
                        @Override
                        public void onError(int i, String s) {
                            Toast.makeText(MyApplication.getContext(), s, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess() {
                            SpUtil.putString(ConstantValues.LOGIN_SIG, userSig);
                            SpUtil.putString(ConstantValues.LOGIN_IDENTIFIER, tlsUserInfo.identifier);
                            loginView.goToHome();
                        }
                    });
        }

        @Override
        public void OnPwdLoginReaskImgcodeSuccess(byte[] bytes) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            loginView.showIdentifierPic(bitmap);
        }

        @Override
        public void OnPwdLoginNeedImgcode(byte[] bytes, TLSErrInfo tlsErrInfo) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            loginView.showIdentifierPic(bitmap);
        }

        @Override
        public void OnPwdLoginFail(TLSErrInfo tlsErrInfo) {
            Toast.makeText(MyApplication.getContext(), tlsErrInfo.Title + ":" + tlsErrInfo.Msg,
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void OnPwdLoginTimeout(TLSErrInfo tlsErrInfo) {
            Toast.makeText(MyApplication.getContext(), tlsErrInfo.Title + ":" + tlsErrInfo.Msg,
                    Toast.LENGTH_SHORT).show();
        }
    }
}
