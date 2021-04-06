package com.example.motion.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.motion.Activity.me_activity_mycollections;
import com.example.motion.Activity.me_activity_mycourse;
import com.example.motion.R;


public class me_fragment_main extends Fragment {
    private View.OnClickListener onClickListener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.me_fragment_main, container, false);
        initView(view);
        return view;
    }

    private void initView(View view){
        TextView bodyData = view.findViewById(R.id.tv_bodydata);
        TextView myCourse = view.findViewById(R.id.tv_myclass);
        TextView myCollections = view.findViewById(R.id.tv_mycollection);
        TextView identify = view.findViewById(R.id.tv_identify);
        TextView pointStore = view.findViewById(R.id.tv_mall);
        TextView dailyMission = view.findViewById(R.id.tv_dailytasks);
        TextView orderCenter = view.findViewById(R.id.tv_ordercenter);
        TextView download = view.findViewById(R.id.tv_mydownload);
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMeClick(v);
            }
        };
        bodyData.setOnClickListener(onClickListener);
        myCourse.setOnClickListener(onClickListener);
        myCollections.setOnClickListener(onClickListener);
        identify.setOnClickListener(onClickListener);
        pointStore.setOnClickListener(onClickListener);
        dailyMission.setOnClickListener(onClickListener);
        orderCenter.setOnClickListener(onClickListener);
        download.setOnClickListener(onClickListener);

    }

    public void onMeClick(View view){
        Intent intent = null;
        switch (view.getId()) {
            case R.id.tv_myclass:
                intent = new Intent(getActivity(), me_activity_mycourse.class);//点击搜索用户按钮跳转到搜索用户界面
                startActivity(intent);
                break;
            case R.id.tv_mycollection:
                intent = new Intent(getActivity(), me_activity_mycollections.class);//点击搜索动态按钮跳转到搜索动态界面
                startActivity(intent);
                break;
        }
    }
}