package com.example.motion.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.motion.R;

public class me_activity_setting extends BaseNetworkActivity implements View.OnClickListener{
    private ImageView ivBack;

    private ImageView gb;
    private TextView tvExit;
    private TextView tvAbout;
    private AlertDialog alert1 = null;
    private AlertDialog.Builder builder1 = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.me_activity_setting);
        super.onCreate(savedInstanceState);
        initView();
    }


    public void initView(){
        tvAbout = findViewById(R.id.tv_about);
        tvAbout.setOnClickListener(this);
        gb = findViewById(R.id.me_setting_back);
        gb.setOnClickListener(this);
        tvExit = findViewById(R.id.tv_loginout_account);
        tvExit.setOnClickListener(this);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.me_setting_back:
                finish();
                break;
            case R.id.tv_loginout_account:
                SharedPreferences preferences = this.getSharedPreferences("saveSp",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("token");
                editor.commit();
                finish();
                Intent intent = new Intent(this, register_activity_register.class);
                startActivity(intent);
                break;
            case R.id.tv_about:
                builder1 = new AlertDialog.Builder(me_activity_setting.this);
                alert1 = builder1.setTitle("关于Motion")
                        .setMessage("这是一款让孩子使用或者让家长使用带着孩子跟练的app，让孩子在电子世界中找到运动的快乐，找到适合他们自己年龄段和目标的运动视频来指导他们进行运动。\n")
                        .create();
                alert1.show();
                alert1.setCanceledOnTouchOutside(true);
                break;
        }
    }
}
