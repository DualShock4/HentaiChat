package com.gabriel.hentaichat.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.gabriel.hentaichat.R;
import com.gabriel.hentaichat.mvp.HomeMVP;
import com.gabriel.hentaichat.view.fragment.ContactFragment;
import com.gabriel.hentaichat.view.fragment.MessageFragment;
import com.gabriel.hentaichat.view.fragment.TrendFragment;
import com.tencent.TIMFriendshipManager;
import com.tencent.tauth.Tencent;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements HomeMVP.View{

    @BindView(R.id.home_toolbar)
    Toolbar toolbar;

    @BindView(android.R.id.tabhost)
    FragmentTabHost tabHost;

    private Tencent mTencent;

    private Class[] fragmentArray = {MessageFragment.class, ContactFragment.class, TrendFragment.class};
    private String[] mTextViewArray = {"message","contact" , "trend"};
    private int[] mImageViewArray = {R.drawable.ic_message, R.drawable.ic_contact, R.drawable.ic_trend};
    private String[] mTitleArray = {"消息", "联系人", "趋势"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        initToolbar();
        tabHost.setup(this, getSupportFragmentManager(), R.id.home_real_tab_content);
        for (int i = 0; i < fragmentArray.length; i++) {
            TabHost.TabSpec tabSpec = tabHost.newTabSpec(mTextViewArray[i])
                    .setIndicator(getTabItemView(i));
            tabHost.addTab(tabSpec, fragmentArray[i], null);
            tabHost.getTabWidget().setDividerDrawable(null);
        }
    }

    private void initToolbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(HomeActivity.this, AddFriendActivity.class));
                return true;
            }
        });
    }

    private View getTabItemView(int index) {
        View view = View.inflate(this, R.layout.item_home_tabhost, null);
        ImageView icon = (ImageView) view.findViewById(R.id.tab_host_icon);
        icon.setImageResource(mImageViewArray[index]);
        TextView title = (TextView) view.findViewById(R.id.tab_host_title);
        title.setText(mTitleArray[index]);
        return view;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }
}
