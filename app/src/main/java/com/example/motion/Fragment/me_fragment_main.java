package com.example.motion.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.motion.Activity.me_activity_bindphone_usephone;
import com.example.motion.Activity.me_activity_bodydata_main;
import com.example.motion.Activity.me_activity_mycollections;
import com.example.motion.Activity.me_activity_mycourse;
import com.example.motion.Activity.me_activity_help;
import com.example.motion.Activity.me_activity_setting;
import com.example.motion.Activity.my_activity_me_data;
import com.example.motion.R;
import com.makeramen.roundedimageview.RoundedImageView;



public class me_fragment_main extends Fragment implements View.OnClickListener {

    private ImageView iv_share;
    private ImageView iv_email;
    private TextView tv_myplace;
    private ImageView iv_myplace;//我的空间右箭头
    private TextView tv_name;
    private ImageView iv_edit_profile;//名字右箭头

    private TextView tv_focus_num;// 关注数目
    private TextView tv_focus;//关注
    private TextView tv_fans_num;// 粉丝数目
    private TextView tv_fans;//粉丝
    private TextView tv_m_num;// m币数目
    private TextView tv_m;//m

    private TextView tv_bodydata;
    private TextView tv_myclass;
    private TextView tv_mycollection;
    private TextView tv_identify;
    private TextView tv_mall;
    private TextView tv_dailytasks;
    private TextView tv_ordercenter;
    private TextView tv_mydownload;

    private TableRow tr_bindphone;
    private TableRow tr_myadress;
    private TableRow tr_help;
    private TableRow tr_setting;

    private RoundedImageView riv_portrait;
    private int httpcode;

    //从其他页面获取UserID后的个人信息
    private int UserID;
    // User
    private String NickName;
    private String HeadPortaitUrl;
    // UserData
    private int FansNumber;
    private int FocusNumber;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.me_fragment_main, container, false);
        Log.d("me_fragment_main","onCreateView");
        initView(view);
        //initListener();
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


        tr_bindphone = view.findViewById(R.id.tr_bingphone);
        iv_edit_profile = view.findViewById(R.id.iv_edit_profile);
        tr_setting = view.findViewById(R.id.tr_setting);
        tr_help = view.findViewById(R.id.tr_help);
        tr_bindphone.setOnClickListener(this);
        iv_edit_profile.setOnClickListener(this);
        tr_help.setOnClickListener(this);
        tr_setting.setOnClickListener(this);
        bodyData.setOnClickListener(this);
        myCourse.setOnClickListener(this);
        myCollections.setOnClickListener(this);
        identify.setOnClickListener(this);
        pointStore.setOnClickListener(this);
        dailyMission.setOnClickListener(this);
        orderCenter.setOnClickListener(this);
        download.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
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
            //绑定手机号
            case R.id.tr_bingphone:
                Log.d("onclick","tr_bingphone");
                intent = new Intent(getActivity(), me_activity_bindphone_usephone.class);//我的.我.绑定手机号主页
                startActivity(intent);
                break;
            case R.id.tr_myadress:
                //Intent intent = new Intent();// M币.管理收货地址主页
                //startActivity(intent);
                break;
            case R.id.tr_help:
                Log.d("onclick","tr_help");
                intent = new Intent(getActivity(), me_activity_help.class);//我的.我.帮助与反馈
                startActivity(intent);
                break;
            case R.id.tr_setting:
                Log.d("onclick","tr_setting");
                intent = new Intent(getActivity(), me_activity_setting.class);//我的.我.设置
                startActivity(intent);
                break;
            case R.id.tv_bodydata:
                Log.d("onclick","tv_bodydata");
                intent = new Intent(getActivity(), me_activity_bodydata_main.class);//我的.我.绑定手机号主页
                startActivity(intent);
                break;
            case R.id.iv_edit_profile:
                intent = new Intent(getActivity(), my_activity_me_data.class);
                startActivity(intent);
                break;
        }
    }
}