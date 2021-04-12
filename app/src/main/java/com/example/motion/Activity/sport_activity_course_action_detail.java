package com.example.motion.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.motion.Entity.Action;
import com.example.motion.Entity.Course;
import com.example.motion.R;
import com.zzhoujay.richtext.RichText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        RichText.initCacheDir(this);
        RichText.from(introList.get(position))
                .showBorder(false)
                .bind(this)
                .into(tvActionContent);//也可改用markdown,即fromMarkdown()
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
                /*
                Intent intent;
                //String rootDir = this.getCacheDir().getPath() + "/videoCache/"+course.getCourseId()+"/";

                actionVideoUrl = actionList.get(currentOne).getActionUrl();

                VideoCacheUtil vcu = new VideoCacheUtil(rootDir,getApplicationContext());
                if(vcu.isExistInLocal(actionVideoUrl)){
                    intent = new Intent(this,play.class);
                    intent.putExtra("movementVideoLocPath",vcu.getVideoLocPath(actionVideoUrl));
                    Log.d("movementVideoLocPath",vcu.getVideoLocPath(actionVideoUrl));
                }else{
                    intent = new Intent(this,activity_download.class);
                    intent.putExtra("movementVideoUrl",actionVideoUrl);
                    intent.putExtra("cacheRootDir",rootDir);
                }
                intent.putExtra("currentOne",currentOne);
                intent.putExtra("course",(Serializable) course);
                startActivity(intent);
                break;

 */

        }
    }
}
