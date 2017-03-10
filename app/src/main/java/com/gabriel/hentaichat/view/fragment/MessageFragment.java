package com.gabriel.hentaichat.view.fragment;

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
import com.gabriel.hentaichat.adapter.ChatAdapter;
import com.gabriel.hentaichat.adapter.MessageAdapter;
import com.gabriel.hentaichat.mvp.MessageMVP;
import com.tencent.TIMConversation;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageListener;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;

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
    private List<TIMMessage> mTIMMessageList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        getInfo();
        mAdapter = new MessageAdapter(mTIMMessageList, getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        TIMManager.getInstance().addMessageListener(MessageFragment.this);
    }

    @Override
    public boolean onNewMessages(List<TIMMessage> list) {
        mTIMMessageList.addAll(list);
        mAdapter.notifyDataSetChanged();
        return false;
    }

    private void getInfo() {
        List<TIMConversation> conversionList = TIMManager.getInstance().getConversionList();
        mTIMMessageList = new ArrayList<>();
        for (TIMConversation timConversation : conversionList) {
            List<TIMMessage> lastMsgs = timConversation.getLastMsgs(1);
            mTIMMessageList.add(lastMsgs.get(0));
        }

    }
}
