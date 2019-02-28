package imooc.com.bearlive.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;

import imooc.com.bearlive.R;
import imooc.com.bearlive.createroom.CreateLiveActivity;
import imooc.com.bearlive.editprofile.EditProfileFragment;
import imooc.com.bearlive.livelist.LiveListFragment;

public class MainActivity extends AppCompatActivity {

    private FrameLayout mContainer;
    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findAllViews();
        setTabs();
    }

    private void setTabs() {
        mTabHost.setup(this, getSupportFragmentManager(), R.id.fragment_container);

        //添加fragment.

        {
            TabHost.TabSpec profileTab = mTabHost.newTabSpec("livelist").setIndicator(getIndicator(R.drawable.tab_livelist));
            mTabHost.addTab(profileTab, LiveListFragment.class, null);
            mTabHost.getTabWidget().setDividerDrawable(null);
        }

        {
            TabHost.TabSpec profileTab = mTabHost.newTabSpec("createlive").setIndicator(getIndicator(R.drawable.tab_publish_live));
            mTabHost.addTab(profileTab, null, null);
            mTabHost.getTabWidget().setDividerDrawable(null);
        }

        {
            TabHost.TabSpec profileTab = mTabHost.newTabSpec("profile").setIndicator(getIndicator(R.drawable.tab_profile));
            mTabHost.addTab(profileTab, EditProfileFragment.class, null);
            mTabHost.getTabWidget().setDividerDrawable(null);
        }

        mTabHost.getTabWidget().getChildTabViewAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到创建直播的页面。
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, CreateLiveActivity.class);
                startActivity(intent);
            }
        });
    }

    private View getIndicator(int resId) {
        View tabView = LayoutInflater.from(this).inflate(R.layout.view_indicator, null);
        ImageView tabImg = (ImageView) tabView.findViewById(R.id.tab_icon);
        tabImg.setImageResource(resId);
        return tabView;
    }

    private void findAllViews() {
        mContainer = (FrameLayout) findViewById(R.id.fragment_container);
        mTabHost = (FragmentTabHost) findViewById(R.id.fragment_tabhost);
    }
}
