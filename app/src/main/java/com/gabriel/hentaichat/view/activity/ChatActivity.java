package com.gabriel.hentaichat.view.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gabriel.hentaichat.ConstantValues;
import com.gabriel.hentaichat.R;
import com.gabriel.hentaichat.adapter.ChatAdapter;
import com.gabriel.hentaichat.mvp.ChatMVP;
import com.gabriel.hentaichat.presenter.ChatPresenter;
import com.tencent.TIMMessage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity implements ChatMVP.View, View.OnClickListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.chat_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.btn_voice)
    ImageButton btn_voice;
    @BindView(R.id.btn_keyboard)
    ImageButton btn_keyboard;
    @BindView(R.id.voice_panel)
    TextView voice_panel;
    @BindView(R.id.input)
    EditText input;
    @BindView(R.id.btn_add)
    ImageButton btn_add;
    @BindView(R.id.btn_send)
    ImageButton btn_send;
    @BindView(R.id.voice_sending)
    TextView voice_sending;
    @BindView(R.id.text_panel)
    LinearLayout text_panel;
    private ChatMVP.Presenter presenter;
    private ChatAdapter mAdapter;
    private List<TIMMessage> mTimMessages;
    private float downY;
    private boolean mIsRecording;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        initToolbar();

        btn_voice.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        btn_keyboard.setOnClickListener(this);
        voice_panel.setOnTouchListener(new View.OnTouchListener() {


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mIsRecording = true;
                        downY = event.getY();
                        voice_sending.setVisibility(View.VISIBLE);
                        voice_panel.setText(getText(R.string.chat_release_send));
                        presenter.recordSound();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (downY - event.getY() > 200) {
                            voice_sending.setText(getText(R.string.chat_release_cancel));
                        } else {
                            voice_sending.setText(getText(R.string.chat_cancel_send));
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        mIsRecording = false;
                        voice_sending.setText(getText(R.string.chat_cancel_send));
                        voice_sending.setVisibility(View.GONE);
                        voice_panel.setText(getText(R.string.chat_press_talk));
                        if (downY - event.getY() > 200) {
                            presenter.sendSoundMessage(false);
                        } else {
                            presenter.sendSoundMessage(true);
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        recordFail();
                        break;
                }
                return true;
            }
        });
        mTimMessages = new ArrayList<>();
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        hideInput();
                        break;
                }
                return false;
            }
        });
        presenter = new ChatPresenter(this, getIntent().getStringExtra(ConstantValues.FRIEND_IDENTIFIER));
        presenter.getMessage(new TIMMessage());
    }

    private void hideInput() {
        input.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
    }

    private void initToolbar() {
        String name = getIntent().getStringExtra(ConstantValues.FRIEND_NAME);
        if (!TextUtils.isEmpty(name)) {
            toolbar.setTitle(name);
        } else {
            toolbar.setTitle("未命名");
        }
        setSupportActionBar(toolbar);
    }

    @Override
    public void updateRecyclerView() {
        if (mAdapter == null) {
            mAdapter = new ChatAdapter(mTimMessages);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        if (mAdapter.getItemCount() > 0) {
            recyclerView.scrollToPosition(mAdapter.getItemCount()-1);
        }
    }

    @Override
    public List<TIMMessage> getAdapterData() {
        return mTimMessages;
    }

    @Override
    public void recordFail() {
        if (mIsRecording) {
            mIsRecording = false;
            voice_sending.setText(getText(R.string.chat_cancel_send));
            voice_sending.setVisibility(View.GONE);
            voice_panel.setText(getText(R.string.chat_press_talk));
            presenter.sendSoundMessage(false);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestory();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                String message = input.getText().toString();
                if (!TextUtils.isEmpty(message)) {
                    presenter.sendTextMessage(message);
                    input.setText("");
                }
                break;
            case R.id.btn_voice:
                hideInput();
                btn_voice.setVisibility(View.GONE);
                text_panel.setVisibility(View.GONE);
                btn_keyboard.setVisibility(View.VISIBLE);
                voice_panel.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_keyboard:
                btn_voice.setVisibility(View.VISIBLE);
                text_panel.setVisibility(View.VISIBLE);
                btn_keyboard.setVisibility(View.GONE);
                voice_panel.setVisibility(View.GONE);
                break;
        }

    }


}
