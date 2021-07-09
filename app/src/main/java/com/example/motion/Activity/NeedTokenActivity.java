package com.example.motion.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.motion.Utils.UserInfoManager;

public class NeedTokenActivity extends BaseNetworkActivity{

    protected Boolean isTokenEmpty;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(isTokenEmpty = UserInfoManager.getUserInfoManager(this).isTokenEmpty()){
            Toast.makeText(getApplicationContext(),"请登录", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, register_activity_register.class);
            finish();
            startActivity(intent);
        }

    }
}
