package com.gabriel.hentaichat.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gabriel.hentaichat.ConstantValues;
import com.gabriel.hentaichat.MyApplication;
import com.gabriel.hentaichat.R;
import com.gabriel.hentaichat.model.FriendListGet;
import com.gabriel.hentaichat.view.activity.ChatActivity;
import com.gabriel.hentaichat.view.customview.CircleImageView;

import java.util.ArrayList;

/**
 * Created by gabriel on 2017/2/28.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder>{

    private FriendListGet mFriendList;
    private Activity mActivity;
    public ContactAdapter(Activity activity,FriendListGet friendList) {
        mFriendList = friendList;
        mActivity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.
                from(MyApplication.getContext()).inflate(R.layout.item_friend_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ArrayList<FriendListGet.FriendInfo.InfoDetail> snsProfileItem = mFriendList.InfoItem.get(position).SnsProfileItem;
        String nick = null;
        String remark = null;
        for (FriendListGet.FriendInfo.InfoDetail infoDetail : snsProfileItem) {
            if (infoDetail.Tag.equals("Tag_SNS_IM_Remark")) {
                remark = infoDetail.Value;
            }
            if (infoDetail.Tag.equals("Tag_Profile_IM_Nick")) {
                nick = infoDetail.Value;
            }
        }
        if (!TextUtils.isEmpty(remark)) {
            holder.name.setText(remark);
        } else {
            holder.name.setText(nick);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mFriendList.FriendNum;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView avatar;
        TextView name;
        ViewHolder(View itemView) {
            super(itemView);
            avatar = (CircleImageView) itemView.findViewById(R.id.avatar);
            name = (TextView) itemView.findViewById(R.id.contact_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, ChatActivity.class);
                    intent.putExtra(ConstantValues.FRIEND_IDENTIFIER, mFriendList.InfoItem.get(getAdapterPosition()).Info_Account);
                    ArrayList<FriendListGet.FriendInfo.InfoDetail> snsProfileItem = mFriendList.InfoItem.get(getAdapterPosition()).SnsProfileItem;
                    for (FriendListGet.FriendInfo.InfoDetail infoDetail : snsProfileItem) {
                        if (infoDetail.Tag.equals("Tag_Profile_IM_Nick")) {
                            intent.putExtra(ConstantValues.FRIEND_NAME, infoDetail.Value);
                        }
                    }
                    mActivity.startActivity(intent);
                }
            });
        }
    }

    public void updateData(FriendListGet friendList) {
        mFriendList = friendList;
    }
}
