package com.gabriel.hentaichat.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Outline;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gabriel.hentaichat.ConstantValues;
import com.gabriel.hentaichat.R;
import com.gabriel.hentaichat.mvp.LoginMVP;
import com.gabriel.hentaichat.presenter.LoginPresenter;
import com.gabriel.hentaichat.util.SpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements LoginMVP.View {

    @BindView(R.id.login_qq)
    ImageButton login_qq;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.iv_identifier)
    ImageView iv_identifier;
    @BindView(R.id.et_identifier)
    EditText et_identifier;
    @BindView(R.id.et_username)
    EditText et_username;
    @BindView(R.id.et_pwd)
    EditText et_pwd;
    @BindView(R.id.sign_up)
    TextView signUp;
    @BindView(R.id.forget_pwd)
    TextView forget_pwd;
    private LoginMVP.Presenter presenter;
    private static final String TAG = "LoginActivity";
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        presenter = new LoginPresenter(this);
        initViews();
    }

    private void initViews() {
        et_identifier.setText(SpUtil.getString(ConstantValues.LOGIN_IDENTIFIER, ""));
        login_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.qq_login();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_username.getText().toString();
                String pwd = et_pwd.getText().toString();
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(pwd)) {
                    presenter.login("86-"+username, pwd);
                } else {
                    Toast.makeText(LoginActivity.this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        iv_identifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.reaskLoginIdentifierImage();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = View.inflate(LoginActivity.this, R.layout.dialog_sign_up, null);
                Button check = (Button) view.findViewById(R.id.check);
                final TextView getIdentifierCode = (TextView) view.findViewById(R.id.get_identifier_code);
                final EditText et_identifier = (EditText) view.findViewById(R.id.identifier_code);
                final EditText et_phone = (EditText) view.findViewById(R.id.phone_number);
                check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String phone = et_phone.getText().toString();
                        String code = et_identifier.getText().toString();
                        if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(code)) {
                            presenter.checkIdentifier(phone, code);
                        } else {
                            Toast.makeText(LoginActivity.this, "请输入手机号码和验证码", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                getIdentifierCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String phone = et_phone.getText().toString();
                        if (!TextUtils.isEmpty(phone)) {
                            presenter.getIdentifierCode(phone);
                            getIdentifierCode.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(LoginActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                dialog = builder.setView(view).show();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        outline.setOval(view.getWidth() / 8, view.getWidth() / 8,
                                view.getWidth() / 8 * 7, view.getHeight() / 8 * 7);
                    }
                }
            };
            login_qq.setOutlineProvider(viewOutlineProvider);
            login_qq.setClipToOutline(true);
            Log.i(TAG, "initViews: outline");
        }
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void showInputPwdDialog() {
        dialog.dismiss();
        View view = View.inflate(LoginActivity.this, R.layout.dialog_input_pwd, null);
        final EditText et_pwd = (EditText) view.findViewById(R.id.pwd);
        final EditText et_pwd_confirm = (EditText) view.findViewById(R.id.pwd_confirm);
        Button signUp = (Button) view.findViewById(R.id.sign_up);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = et_pwd.getText().toString();
                String pwd_confirm = et_pwd_confirm.getText().toString();
                if (!TextUtils.isEmpty(pwd) && pwd.equals(pwd_confirm)) {
                    presenter.signUp(pwd);
                    dialog.dismiss();
                }
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        dialog = builder.setView(view).show();
    }

    @Override
    public void goToHome() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @Override
    public void showIdentifierPic(Bitmap bitmap) {
        iv_identifier.setVisibility(View.VISIBLE);
        et_identifier.setVisibility(View.VISIBLE);
        iv_identifier.setImageBitmap(bitmap);
    }
}
