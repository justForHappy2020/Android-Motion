package com.example.motion.Activity;

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

    ImageView gb;
    TextView tvExit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.me_activity_setting);
        initView();
        super.onCreate(savedInstanceState);
        initView();
    }


    public void initView(){
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
                editor.clear();
                editor.commit();
                finish();
                Intent intent = new Intent(this, register_activity_register.class);
                startActivity(intent);
                break;
        }
    }
}
