package com.example.motion.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.motion.R;

public class sport_activity_course_completed extends BaseNetworkActivity implements View.OnClickListener {

    TextView tvOK;
    TextView tvNickname;
    TextView tvCurrentTime;
    TextView tvCourseName;
    TextView tvCount;
    TextView tvUsedTime;
    TextView tvBtnFeelingHard;
    TextView tvBtnFeelingEasy;
    TextView tvBtnFeelingNotBad;
    ImageView ivUserHead;
    Button btnPostIt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sport_activity_course_completed);

        initView();
        initEvent();
    }

    private void initView(){
        tvOK = findViewById(R.id.tv_ok);
        tvNickname = findViewById(R.id.tv_nickname);
        tvCurrentTime= findViewById(R.id.tv_date);
        tvCourseName= findViewById(R.id.tv_sport_compeleted_course_name);
        tvCount= findViewById(R.id.tv_sport_compeleted_course_count);
        tvUsedTime= findViewById(R.id.tv_sport_compeleted_course_usedtime);
        tvBtnFeelingHard = findViewById(R.id.tv_feelings_hard);
        tvBtnFeelingEasy= findViewById(R.id.tv_feelings_easy);
        tvBtnFeelingNotBad= findViewById(R.id.tv_feelings_notbad);
        ivUserHead = findViewById(R.id.iv_head);
        btnPostIt = findViewById(R.id.btn_post_it);
    }

    private void initEvent(){
        tvOK.setOnClickListener(this);
        tvBtnFeelingHard.setOnClickListener(this);
        tvBtnFeelingEasy.setOnClickListener(this);
        tvBtnFeelingNotBad.setOnClickListener(this);
        btnPostIt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_ok:

                break;
            case R.id.tv_feelings_hard:

                break;
            case R.id.tv_feelings_easy:

                break;
            case R.id.tv_feelings_notbad:

                break;
            case R.id.btn_post_it:

                break;
        }
    }
}