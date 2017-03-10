package com.gabriel.hentaichat.view.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.gabriel.hentaichat.ConstantValues;
import com.gabriel.hentaichat.MyApplication;
import com.gabriel.hentaichat.R;
import com.gabriel.hentaichat.model.ContactModel;
import com.gabriel.hentaichat.util.SpUtil;
import com.tencent.TIMCallBack;
import com.tencent.TIMManager;
import com.tencent.TIMUser;
import com.tencent.tauth.Tencent;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class SplashActivity extends Activity {

    @BindView(R.id.iv_splash)
    ImageView imageView;
    private static final String TAG = "SplashActivity";

    private static boolean firstEnter = true;
    private Subject<Object> mObservable;
    private final int notSend = 1;
    private final int loginSuccess = 2;
    private final int loginFail = 3;
    private int mRx = notSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!firstEnter) {
            String username = TIMManager.getInstance().getLoginUser();
            if (!TextUtils.isEmpty(username)) {
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            finish();
            return;
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mObservable = PublishSubject.create().toSerialized();
        TIMUser user = new TIMUser();
        user.setAppIdAt3rd(String.valueOf(ConstantValues.SDK_APP_ID));
        user.setIdentifier(SpUtil.getString(ConstantValues.LOGIN_IDENTIFIER, ""));
        user.setAccountType(String.valueOf(ConstantValues.ACCOUNT_TYPE));
        TIMManager.getInstance().login((int) ConstantValues.SDK_APP_ID,
                user, SpUtil.getString(ConstantValues.LOGIN_SIG, ""), new TIMCallBack() {
                    @Override
                    public void onError(int i, final String s) {
                        mObservable.onNext(s);
                        mRx = loginFail;
                    }

                    @Override
                    public void onSuccess() {
                        ContactModel contactModel = new ContactModel();
                        contactModel.getFriendList();
                        mObservable.onNext("success");
                        mRx = loginSuccess;
                    }
                });
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView, "scaleX", 1.0f, 1.2f)
                .setDuration(1500);
        objectAnimator.start();
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                imageView.setScaleY(value);
            }
        });
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                firstEnter = false;
                mObservable.subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (mRx == loginFail) {
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                            finish();
                        } else if (mRx == loginSuccess) {
                            startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onNext(Object o) {
                        String s = (String) o;
                        if (s.equals("success")) {
                            startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                        } else {
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        }
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

}
