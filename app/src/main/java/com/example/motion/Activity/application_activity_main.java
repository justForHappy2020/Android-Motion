package com.example.motion.Activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.motion.Fragment.community_fragment_main;
import com.example.motion.Fragment.diet_fragment_main;
import com.example.motion.Fragment.homepage_fragment_main;
import com.example.motion.Fragment.me_fragment_main;
import com.example.motion.Fragment.sport_fragment_main;
import com.example.motion.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;


public class application_activity_main extends FragmentActivity {
    //声明ViewPager
    private ViewPager mViewPager;
    //适配器
    private FragmentPagerAdapter mAdapter;
    //装载Fragment的集合
    private List<Fragment> mFragments;
    //底部导航栏
    private BottomNavigationView bottomNavigation;


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
                bottomNavigation.getMenu().getItem(position).setChecked(true);
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
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    //初始化控件
    private void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottomNavigation);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.main_homepage:
                    mViewPager.setCurrentItem(0);
                    return true;
                case R.id.main_sport:
                    mViewPager.setCurrentItem(1);
                    return true;
                case R.id.main_community:
                    mViewPager.setCurrentItem(2);
                    return true;
                case R.id.main_diet:
                    mViewPager.setCurrentItem(3);
                    return true;
                case R.id.main_me:
                    mViewPager.setCurrentItem(4);
                    return true;
            }
                return false;

        }
    };






}
