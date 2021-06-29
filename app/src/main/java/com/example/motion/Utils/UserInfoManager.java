package com.example.motion.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.motion.Entity.User;
import com.raizlabs.android.dbflow.annotation.NotNull;

import static android.content.Context.MODE_PRIVATE;

public class UserInfoManager {
    public final static Long FALSE_USER_ID = Long.valueOf(-1);
    //创建 SingleObject 的一个对象
    private static UserInfoManager userInfoManager = new UserInfoManager();
    private Context context;
    private SharedPreferences sp;
    private User user;

    //让构造函数为 private，这样该类就不会被实例化
    private UserInfoManager(){

    }

    //获取唯一可用的对象
    public static UserInfoManager getUserInfoManager(Context context){
        userInfoManager.context = context;
        userInfoManager.sp = context.getSharedPreferences("saveSp",MODE_PRIVATE);
        return userInfoManager;
    }

    public User getUser(){
        userInfoManager.user = new User();
        userInfoManager.user.setUserId(userInfoManager.sp.getLong("userId",FALSE_USER_ID));
        userInfoManager.user.setNickName(userInfoManager.sp.getString("nickName",""));
        userInfoManager.user.setHeadPortraitUrl(userInfoManager.sp.getString("headPortrait",""));
        userInfoManager.user.setPhoneNumber(userInfoManager.sp.getString("phoneNumber",""));
        userInfoManager.user.setGender(userInfoManager.sp.getInt("gender",0));
        userInfoManager.user.setToken(userInfoManager.sp.getString("token",""));
        userInfoManager.user.setCreateTime(userInfoManager.sp.getString("userCreateTime",""));
        userInfoManager.user.setIntro(userInfoManager.sp.getString("intro",""));

        Log.d("UserInfoManager","setUserId:"+userInfoManager.user.getUserId());
        Log.d("UserInfoManager","setNickName:"+userInfoManager.user.getNickName());
        Log.d("UserInfoManager","setHeadPortraitUrl:"+userInfoManager.user.getHeadPortraitUrl());
        Log.d("UserInfoManager","setPhoneNumber:"+userInfoManager.user.getPhoneNumber());
        Log.d("UserInfoManager","setGender:"+userInfoManager.user.getGender());
        Log.d("UserInfoManager","setToken:"+userInfoManager.user.getToken());
        Log.d("UserInfoManager","setCreateTime:"+userInfoManager.user.getCreateTime());
        Log.d("UserInfoManager","setIntro:"+userInfoManager.user.getIntro());

        return userInfoManager.user;
    }


    public String getToken(){
        return userInfoManager.sp.getString("token","");
    }

    public Boolean isTokenEmpty(){
        return userInfoManager.sp.getString("token","").isEmpty();
    }


}
