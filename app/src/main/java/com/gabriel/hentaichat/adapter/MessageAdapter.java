package com.gabriel.hentaichat.adapter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gabriel.hentaichat.ConstantValues;
import com.gabriel.hentaichat.MyApplication;
import com.gabriel.hentaichat.R;
import com.gabriel.hentaichat.model.FriendListGet;
import com.gabriel.hentaichat.util.SQLiteUtil;
import com.gabriel.hentaichat.util.SpUtil;
import com.gabriel.hentaichat.util.TimeUtil;
import com.gabriel.hentaichat.view.activity.ChatActivity;
import com.gabriel.hentaichat.view.customview.CircleImageView;
import com.tencent.TIMConversation;
import com.tencent.TIMElem;
import com.tencent.TIMElemType;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMSNSSystemElem;
import com.tencent.TIMSNSSystemType;
import com.tencent.TIMTextElem;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabriel on 2017/2/28.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private Activity mActivity;
    private ViewHolder viewHolder;
    private List<TIMMessage> mTIMMessageList;

    public MessageAdapter(List<TIMMessage> list, Activity activity) {
        mActivity = activity;
        mTIMMessageList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.
                from(MyApplication.getContext()).inflate(R.layout.item_conversation, parent, false));
        this.viewHolder = viewHolder;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TIMMessage timMessage = mTIMMessageList.get(position);

        TIMElem element = timMessage.getElement(0);
        String identifier = timMessage.getConversation().getPeer();

        Cursor cursor = SQLiteUtil.getFriendListDb().query(SQLiteUtil.FRIEND_DB_TABLE_NAME,
                new String[]{SQLiteUtil.FRIEND_DB_NAME}, "identifier=?",
                new String[]{identifier}, null, null, null);
        String name = "";
        while (cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndex(SQLiteUtil.FRIEND_DB_NAME));
        }
        cursor.close();
        if (!TextUtils.isEmpty(name)) {
            viewHolder.name.setText(name);
        } else {
            viewHolder.name.setText(identifier);
        }
        viewHolder.message_time.setText(TimeUtil.getChatTimeStr(timMessage.timestamp()));

        if (element.getType() == TIMElemType.Text) {
            TIMTextElem textElem = (TIMTextElem) element;
            viewHolder.last_message.setText(textElem.getText());
        } else if (element.getType() == TIMElemType.Sound) {
            viewHolder.last_message.setText("语音消息");
        }

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mTIMMessageList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView avatar;
        TextView name;
        TextView last_message;
        TextView message_time;
        TextView unread_num;

        ViewHolder(View itemView) {
            super(itemView);
            avatar = (CircleImageView) itemView.findViewById(R.id.avatar);
            name = (TextView) itemView.findViewById(R.id.name);
            last_message = (TextView) itemView.findViewById(R.id.last_message);
            message_time = (TextView) itemView.findViewById(R.id.message_time);
            unread_num = (TextView) itemView.findViewById(R.id.unread_num);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, ChatActivity.class);
                    String identifier = mTIMMessageList.get(getAdapterPosition()).getConversation().getPeer();
                    Cursor cursor = SQLiteUtil.getFriendListDb().query(SQLiteUtil.FRIEND_DB_TABLE_NAME,
                            new String[]{SQLiteUtil.FRIEND_DB_NAME}, "identifier=?",
                            new String[]{identifier}, null, null, null);
                    while (cursor.moveToNext()) {
                        String name = cursor.getString(cursor.getColumnIndex(SQLiteUtil.FRIEND_DB_NAME));
                        intent.putExtra(ConstantValues.FRIEND_NAME, name);
                    }
                    cursor.close();
                    intent.putExtra(ConstantValues.FRIEND_IDENTIFIER, identifier);
                    mActivity.startActivity(intent);
                }
            });
        }
    }

    public void resetData(List<TIMMessage> timMessageList) {
        mTIMMessageList = timMessageList;
    }
}
