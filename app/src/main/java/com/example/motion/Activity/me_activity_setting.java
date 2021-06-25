package com.example.motion.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.motion.R;

public class me_activity_setting extends AppCompatActivity implements View.OnClickListener{
    ImageView gb;
    TextView tvExit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.me_activity_setting);
        initView();
        super.onCreate(savedInstanceState);
    }

    public void initView(){
        gb = findViewById(R.id.meSettingGoBack);
        gb.setOnClickListener(this);
        tvExit = findViewById(R.id.textView3);
        tvExit.setOnClickListener(this);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.meSettingGoBack:
                finish();
                break;
            case R.id.textView3:
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
