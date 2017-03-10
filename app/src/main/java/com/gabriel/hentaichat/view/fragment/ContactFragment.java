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
import com.gabriel.hentaichat.adapter.ContactAdapter;
import com.gabriel.hentaichat.adapter.MessageAdapter;
import com.gabriel.hentaichat.model.FriendListGet;
import com.gabriel.hentaichat.mvp.ContactMVP;
import com.gabriel.hentaichat.presenter.ContactPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gabriel on 2017/2/10.
 */

public class ContactFragment extends Fragment implements ContactMVP.View {

    @BindView(R.id.contact_recycler_view)
    RecyclerView recyclerView;
    private ContactAdapter mAdapter;
    private FriendListGet mFriendList;
    private ContactMVP.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        mFriendList = new FriendListGet();
        mAdapter = new ContactAdapter(getActivity(), mFriendList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        presenter = new ContactPresenter(this);
        presenter.updateFriendList(mAdapter);
    }
}
