package com.example.motion.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.motion.R;

public class me_activity_help extends BaseNetworkActivity implements View.OnClickListener{
    private ImageView ivBack;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.me_activity_feedback);
        initView();
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView(){
        ivBack = findViewById(R.id.me_feedback_back);
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.me_feedback_back:
                finish();
                break;
        }
    }


}
