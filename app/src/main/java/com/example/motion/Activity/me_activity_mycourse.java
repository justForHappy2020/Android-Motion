package com.example.motion.Activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.motion.Fragment.me_fragment_mycourse_collections;
import com.example.motion.Fragment.me_fragment_mycourse_history;
import com.example.motion.Fragment.me_fragment_mycourse_reserve;
import com.example.motion.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class me_activity_mycourse extends AppCompatActivity {
    private ViewPager vp;
    ArrayList<View> views = new ArrayList<>();
    private List<Fragment> mFragments;
    String[] titles = {"最近练过", "我的收藏", "我的预定"};
    private TabLayout tabLayout;
    private FragmentPagerAdapter mAdapter;
    private String token = "123456";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_activity_mycourse);
        //
        //tabLayout = (TabLayout)findViewById(R.id.meMyCourseTabLayout);
        initView();
        initDatas();
       // tabLayout.setupWithViewPager(vp);
       // tabLayout.setTabMode(TabLayout.MODE_FIXED);
       // vp.setAdapter(new MyAdapter());//设置适配器
    }
    private void initDatas(){
        Bundle bundle = new Bundle();
        me_fragment_mycourse_history f1 = new me_fragment_mycourse_history();
        me_fragment_mycourse_collections f2 = new me_fragment_mycourse_collections();
        me_fragment_mycourse_reserve f3 = new me_fragment_mycourse_reserve();

        f1.setArguments(bundle);
        f2.setArguments(bundle);
        f3.setArguments(bundle);

        mFragments = new ArrayList<>();
        mFragments.add(f1);
        mFragments.add(f2);
        mFragments.add(f3);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        };

        vp.setAdapter(mAdapter);
    }
    private void initView(){
        //View v1 = getLayoutInflater().inflate(R.layout.me_fragment_mycourse_history, null);

        //View v3 = getLayoutInflater().inflate(R.layout.me_fragment_mycourse_reserve, null);
        //views.add(v1);
        //views.add(v2);
        //views.add(v3);
        vp = this.findViewById(R.id.meMyCourseViewPager);
        tabLayout = this.findViewById(R.id.meMyCourseTabLayout);
        vp.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(vp);
        tabLayout.setTabMode(tabLayout.MODE_FIXED);
        onClick();
    }
    public void onClick(){
        findViewById(R.id.meMyCourseGoBack).setOnClickListener(new View.OnClickListener() {//点击取消按钮返回到课程主页
            public void onClick (View v){
                finish();
            }
        });
    }
    class MyAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //重写销毁滑动视图布局（将子视图移出视图存储集合（ViewGroup））
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }

        //重写初始化滑动视图布局（从视图集合中取出对应视图，添加到ViewGroup）
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = views.get(position);
            container.addView(v);
            return v;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
