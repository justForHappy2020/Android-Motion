package com.example.motion.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.motion.R;

public class me_activity_help extends AppCompatActivity implements View.OnClickListener{
    ImageView gb;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.me_activity_feedback);
        initView();
        super.onCreate(savedInstanceState);
    }
    public void initView(){
        gb = findViewById(R.id.meFeedbackGoBack);
        gb.setOnClickListener(this);
    }

    public void onClick(View view){
        gb.setOnClickListener(new View.OnClickListener() {//点击取消按钮返回到课程主页
            public void onClick (View v){
                finish();
            }
        });
    }
}
