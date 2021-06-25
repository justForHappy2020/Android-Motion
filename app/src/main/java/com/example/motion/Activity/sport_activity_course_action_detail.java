package com.example.motion.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.motion.Entity.Action;
import com.example.motion.Entity.Course;
import com.example.motion.R;
import com.example.motion.Utils.CourseCacheUtil;
import com.example.motion.Utils.OnProcessStateChangeListener;
import com.zzhoujay.richtext.RichText;

import java.util.ArrayList;
import java.util.List;

public class sport_activity_course_action_detail extends Activity implements View.OnClickListener {

    private int currentOne;

    private ImageButton ivLastAction;
    private ImageButton ivNextAction;
    private ImageView ivHeaderImage;
    private Button btnStartAction;
    private TextView tvActionName;
    private TextView tvActionContent;

    private Course course;
    private List<Action> actionList;
    private List<String> introList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sport_activity_course_action_detail);

        Intent intent = getIntent();
        course = (Course)intent.getSerializableExtra("course");
        actionList =(List<Action>) intent.getSerializableExtra("actionList");
        currentOne = intent.getIntExtra("courseActionPosition",0);

        initIntroData();
        initView();

    }

    private void initView(){
        tvActionName =findViewById(R.id.movement_detail_name);
        ivLastAction =findViewById(R.id.movement_detail_last);
        ivNextAction =findViewById(R.id.movement_detail_next);
        ivHeaderImage =findViewById(R.id.movement_detail_mainimage);
        btnStartAction =findViewById(R.id.movement_detail_start);
        tvActionContent =findViewById(R.id.movement_detail_content);

        ivLastAction.setOnClickListener(this);
        ivNextAction.setOnClickListener(this);
        btnStartAction.setOnClickListener(this);

        RichText.initCacheDir(this);
        switchContent(currentOne);

    }


    private void switchContent(int position){
        if(currentOne == 0) {
            ivLastAction.setEnabled(false);
            ivNextAction.setEnabled(true);
        }
        if(currentOne == actionList.size()-1){
            ivLastAction.setEnabled(true);
            ivNextAction.setEnabled(false);
        }
        if(currentOne>0 && currentOne<actionList.size()-1){
            ivLastAction.setEnabled(true);
            ivNextAction.setEnabled(true);
        }

        Glide.with(this).load(actionList.get(currentOne).getActionImgs()).placeholder(R.drawable.ic_placeholder).into(ivHeaderImage);
        tvActionName.setText(actionList.get(position).getActionName());
        RichText.fromMarkdown(introList.get(position))
                .showBorder(false)
                .bind(this)
                .into(tvActionContent);
    }

    private void initIntroData(){

        introList = new ArrayList<>();//把每一个action的intro内容装入introList

        for(int j=0;j<actionList.size();j++){
            introList.add(actionList.get(j).getIntro());
        }

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.movement_detail_last:
                if(currentOne>=1){
                    switchContent(--currentOne);
                }
                break;
            case R.id.movement_detail_next:
                if(currentOne<=actionList.size()-1){
                    switchContent(++currentOne);
                }
                break;
            case R.id.movement_detail_start:
                CourseCacheUtil ccu = new CourseCacheUtil(this,getCacheDir());
                ccu.setOnChangeListener(new OnProcessStateChangeListener() {

                    @Override
                    public void onProcessDone(boolean isSuccess, Course courseWithActions,Object message) {
                        if(isSuccess){
                            //course = courseWithActions;
                            Intent intent = new Intent(getBaseContext(),sport_activity_course_start.class);
                            intent.putExtra("courseWithActions",courseWithActions);
                            startActivity(intent);

                            Log.d("course_detail","CourseCacheUtil_cache_success");
                        }else{
                            Log.d("course_detail","CourseCacheUtil_cache_fail,"+(String)message);
                        }
                    }

                });
                ccu.process(course,actionList);


                break;

        }
    }
}