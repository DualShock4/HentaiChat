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
import com.gabriel.hentaichat.mvp.MessageMVP;
import com.tencent.TIMConversation;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageListener;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        List<TIMMessage> timMessageList = new ArrayList<>();
        mAdapter = new MessageAdapter(timMessageList, getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        TIMManager.getInstance().addMessageListener(this);
    }

    @Override
    public boolean onNewMessages(List<TIMMessage> list) {
        refreshData();
        mAdapter.notifyDataSetChanged();
        return false;
    }

    private void refreshData() {
        List<TIMConversation> conversionList = TIMManager.getInstance().getConversionList();
        List<TIMMessage> timMessageList = new ArrayList<>();
        for (TIMConversation timConversation : conversionList) {
            List<TIMMessage> lastMsgs = timConversation.getLastMsgs(1);
            timMessageList.add(lastMsgs.get(0));
        }
        mAdapter.resetData(timMessageList);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        TIMManager.getInstance().removeMessageListener(this);
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
        mAdapter.notifyDataSetChanged();
    }
}
