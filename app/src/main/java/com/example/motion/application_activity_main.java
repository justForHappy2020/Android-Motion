package com.example.motion;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;


public class application_activity_main extends FragmentActivity implements View.OnClickListener {
    //声明ViewPager
    private ViewPager mViewPager;
    //适配器
    private FragmentPagerAdapter mAdapter;

    //装载Fragment的集合
    private List<Fragment> mFragments;

    //四个Tab对应的布局
    private LinearLayout llHomepage;
    private LinearLayout llSport;
    private LinearLayout llCommunity;
    private LinearLayout llDiet;
    private LinearLayout llMe;

    private ImageView ivHomePage;
    private ImageView ivSport;
    private ImageView ivCommunity;
    private ImageView ivDiet;
    private ImageView ivMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.application_activity_main);
        initViews();//初始化控件
        initEvents();//初始化事件
        initDatas();//初始化数据
    }

    private void initDatas() {
        mFragments = new ArrayList<>();
        //将四个Fragment加入集合中
        mFragments.add(new homepage_fragment_main());
        mFragments.add(new sport_fragment_main());
        mFragments.add(new community_fragment_main());
        mFragments.add(new diet_fragment_main());
        mFragments.add(new me_fragment_main());

        //初始化适配器
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {//从集合中获取对应位置的Fragment
                return mFragments.get(position);
            }

            @Override
            public int getCount() {//获取集合中Fragment的总数
                return mFragments.size();
            }
        };
        //不要忘记设置ViewPager的适配器
        mViewPager.setAdapter(mAdapter);
        //设置ViewPager的切换监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            //页面滚动事件
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            //页面选中事件
            @Override
            public void onPageSelected(int position) {
                //设置position对应的集合中的Fragment
                mViewPager.setCurrentItem(position);
                resetImgs();
                selectTab(position);
            }

            @Override
            //页面滚动状态改变事件
            public void onPageScrollStateChanged(int state) {

            }
        });
        //预加载fragment个数，防止切换页面卡顿
        mViewPager.setOffscreenPageLimit(5);
    }

    private void initEvents() {
        llHomepage.setOnClickListener(this);
        llSport.setOnClickListener(this);
        llCommunity.setOnClickListener(this);
        llDiet.setOnClickListener(this);
        llMe.setOnClickListener(this);


    }

    //初始化控件
    private void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

        llHomepage = (LinearLayout) findViewById(R.id.ll_homepage);
        llSport = (LinearLayout) findViewById(R.id.ll_sport);
        llCommunity = (LinearLayout) findViewById(R.id.ll_communnity);
        llDiet = (LinearLayout) findViewById(R.id.ll_diet);
        llMe = (LinearLayout) findViewById(R.id.ll_me);

        ivHomePage = findViewById(R.id.iv_homepage);
        ivSport = findViewById(R.id.iv_sport);
        ivCommunity = findViewById(R.id.iv_community);
        ivDiet = findViewById(R.id.iv_diet);
        ivMe = findViewById(R.id.iv_me);

    }

    @Override
    public void onClick(View v) {
        resetImgs();
        //根据点击的Tab切换不同的页面及设置对应的ImageButton为深色
        switch (v.getId()) {
            case R.id.ll_homepage:
                selectTab(0);
                break;
            case R.id.ll_sport:
                selectTab(1);
                break;
            case R.id.ll_communnity:
                selectTab(2);
                break;
            case R.id.ll_diet:
                selectTab(3);
                break;
            case R.id.ll_me:
                selectTab(4);
                break;
        }


    }

    private void selectTab(int i) {

        //根据点击的Tab设置对应的ImageButton为深色
        switch (i) {
            case 0:
                ivHomePage.setImageResource(R.drawable.ic_homepage_main_clicked);
                break;
            case 1:
                ivSport.setImageResource(R.drawable.ic_sport_main_clicked);
                break;
            case 2:
                ivCommunity.setImageResource(R.drawable.ic_community_main_clicked);
                break;
            case 3:
                ivDiet.setImageResource(R.drawable.ic_diet_main_clicked);
                break;
            case 4:
                ivMe.setImageResource(R.drawable.ic_me_main_clicked);
                break;
        }
        //设置当前点击的Tab所对应的页面
        mViewPager.setCurrentItem(i);
    }

    //将四个ImageButton设置为浅灰色
    private void resetImgs() {
        ivHomePage.setImageResource(R.drawable.ic_homepage_main_unclicked);
        ivSport.setImageResource(R.drawable.ic_sport_main_unclicked);
        ivCommunity.setImageResource(R.drawable.ic_community_main_unclicked);
        ivDiet.setImageResource(R.drawable.ic_diet_main_unclicked);
        ivMe.setImageResource(R.drawable.ic_me_main_unclicked);
    }

}
