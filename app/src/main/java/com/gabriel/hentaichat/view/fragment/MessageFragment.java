package com.gabriel.hentaichat.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gabriel.hentaichat.R;
import com.gabriel.hentaichat.adapter.MessageAdapter;
import com.gabriel.hentaichat.model.ContactModel;
import com.gabriel.hentaichat.mvp.MessageMVP;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageListener;
import com.tencent.TIMSNSSystemElem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gabriel on 2017/2/10.
 */

public class MessageFragment extends Fragment implements MessageMVP.View, TIMMessageListener {

    @BindView(R.id.message_recycler_view)
    RecyclerView recyclerView;
    private MessageAdapter mAdapter;
    private List<TIMMessage> mNewFriendList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, view);
        TIMManager.getInstance().addMessageListener(this);
        initView();
        return view;
    }

    private void initView() {
        List<TIMMessage> timMessageList = new ArrayList<>();
        mAdapter = new MessageAdapter(timMessageList, getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        refreshView();
    }

    @Override
    public boolean onNewMessages(List<TIMMessage> list) {
        mNewFriendList = new ArrayList<>();
        for (TIMMessage timMessage : list) {
            if (timMessage.getElement(0) instanceof TIMSNSSystemElem) {
                mNewFriendList.add(timMessage);
            }
        }
        initView();
        return false;
    }

    private void refreshView() {
        List<TIMConversation> conversionList = TIMManager.getInstance().getConversionList();
        List<TIMMessage> timMessageList = new ArrayList<>();
        timMessageList.addAll(mNewFriendList);
        for (TIMConversation timConversation : conversionList) {
            if (timConversation.getType() == TIMConversationType.C2C) {
                List<TIMMessage> lastMsgs = timConversation.getLastMsgs(1);
                timMessageList.add(lastMsgs.get(0));
            }
        }
        mAdapter.resetData(timMessageList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        TIMManager.getInstance().removeMessageListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
    }

}
