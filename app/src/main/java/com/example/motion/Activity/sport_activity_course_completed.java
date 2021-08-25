package com.example.motion.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.example.motion.Entity.Course;
import com.example.motion.Entity.User;
import com.example.motion.MontionRequest.CourseCompletedRequest;
import com.example.motion.R;
import com.example.motion.Utils.UserInfoManager;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class sport_activity_course_completed extends BaseNetworkActivity implements View.OnClickListener {

    private TextView tvOK;
    private TextView tvNickname;
    private TextView tvCurrentTime;
    private TextView tvCourseName;
    private TextView tvCount;
    private TextView tvUsedTime;
    private TextView tvBtnFeelingHard;
    private TextView tvBtnFeelingEasy;
    private TextView tvBtnFeelingNotBad;
    private RoundedImageView ivUserHead;
    private Button btnPostIt;

    private int timeSeconds;
    private int totalTime;
    private Course course;
    private User user;

    private DecimalFormat decimalFormat;

    private Map<String,String> courseCompletedParam;
    private boolean isCourseCompleteUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sport_activity_course_completed);

        initView();
        initEvent();
        initData();
    }

    private void initView(){
        tvOK = findViewById(R.id.tv_ok);
        tvNickname = findViewById(R.id.tv_nickname);
        tvCurrentTime= findViewById(R.id.tv_date);
        tvCourseName= findViewById(R.id.tv_sport_completed_course_name);
        tvCount= findViewById(R.id.tv_sport_compeleted_course_count);
        tvUsedTime= findViewById(R.id.tv_sport_compeleted_course_usedtime);
        tvBtnFeelingHard = findViewById(R.id.tv_feelings_hard);
        tvBtnFeelingEasy= findViewById(R.id.tv_feelings_easy);
        tvBtnFeelingNotBad= findViewById(R.id.tv_feelings_notbad);
        ivUserHead = findViewById(R.id.iv_head);
        btnPostIt = findViewById(R.id.btn_ok);
    }

    private void initEvent(){
        tvOK.setOnClickListener(this);
        tvBtnFeelingHard.setOnClickListener(this);
        tvBtnFeelingEasy.setOnClickListener(this);
        tvBtnFeelingNotBad.setOnClickListener(this);
        btnPostIt.setOnClickListener(this);
    }

    private void initData(){
        courseCompletedParam = new HashMap<>();
        isCourseCompleteUpload = false;
        decimalFormat = new DecimalFormat("00");
        timeSeconds = getIntent().getIntExtra("timeSeconds",0);

        course = (Course)getIntent().getSerializableExtra("course");
        user = (UserInfoManager.getUserInfoManager(sport_activity_course_completed.this).getUser());

        courseCompletedParam.put("time",String.valueOf(timeSeconds));
        courseCompletedParam.put("token",user.getToken());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try{
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                    Date date = new Date(System.currentTimeMillis());
                    tvCurrentTime.setText(simpleDateFormat.format(date));

                    tvUsedTime.setText(decimalFormat.format(timeSeconds/60)+":"+decimalFormat.format(timeSeconds%60));
                    if(course!=null){
                        courseCompletedParam.put("courseId",String.valueOf(course.getCourseId()));
                        tvCourseName.setText(course.getCourseName());
                    }
                    tvNickname.setText(user.getNickName());
                    Glide.with(sport_activity_course_completed.this)
                            .load(user.getHeadPortraitUrl())
                            .into(ivUserHead);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        requestQueue.add(new CourseCompletedRequest(courseCompletedParam, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isCourseCompleteUpload = true;
                tvCount.setText(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                checkVolleyError(error);
                tvCount.setText("1");
            }
        }));

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
            case R.id.btn_ok:
                finish();
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!isCourseCompleteUpload){

        }
    }
}