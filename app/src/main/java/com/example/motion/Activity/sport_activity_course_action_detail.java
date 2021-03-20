package com.example.motion.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.motion.Entity.Action;
import com.example.motion.Entity.Course;
import com.example.motion.R;
import com.zzhoujay.richtext.RichText;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class sport_activity_course_action_detail extends Activity implements View.OnClickListener {

    private int currentOne=0;

    private ImageView lastMovement;
    private ImageView nextMovement;
    private ImageView mainImage;
    private Button startMovement;
    private TextView movementNameTextView;
    private TextView contentTextView;

    private Course course;
    private List<Action> actionList;
    private List<String> introList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sport_activity_course_action_detail);

        Intent intent = getIntent();
        course=(Course)intent.getSerializableExtra("course");
        actionList =(List<Action>) intent.getSerializableExtra("actionList");
        currentOne=intent.getIntExtra("courseActionPosition",0);

        initIntroData();
        initView();
    }

    private void initView(){
        movementNameTextView=findViewById(R.id.movement_detail_name);
        lastMovement=findViewById(R.id.movement_detail_last);
        nextMovement=findViewById(R.id.movement_detail_next);
        mainImage=findViewById(R.id.movement_detail_mainimage);
        startMovement=findViewById(R.id.movement_detail_start);
        contentTextView=findViewById(R.id.movement_detail_content);

        lastMovement.setOnClickListener(this);
        nextMovement.setOnClickListener(this);
        startMovement.setOnClickListener(this);

        switchContent(currentOne);

    }


    private void switchContent(int position){
        Glide.with(this).load(actionList.get(currentOne).getActionImgs()).placeholder(R.drawable.ic_placeholder).into(mainImage);
        movementNameTextView.setText(actionList.get(position).getActionName());

        RichText.initCacheDir(this);

        RichText.from(introList.get(position))
                .showBorder(false)
                .bind(this)
                .into(contentTextView);//也可改用markdown,即fromMarkdown()

        //Spanned spanned = HtmlCompat.fromHtml(introList.get(position),0);
        //contentTextView.setText(spanned);

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
                if(currentOne>1){
                    //lastMovement.setImageResource(R.drawable.ic_back_black);
                    //nextMovement.setImageResource(R.drawable.ic_more_black);
                    switchContent(--currentOne);
                }else if(currentOne==1){
                    switchContent(--currentOne);
                    //lastMovement.setImageResource(R.drawable.ic_back_gray);
                }else{
                    //lastMovement.setImageResource(R.drawable.ic_back_gray);
                }
                break;
            case R.id.movement_detail_next:
                if(currentOne<actionList.size()-2){
                    //nextMovement.setImageResource(R.drawable.ic_more_black);
                    //lastMovement.setImageResource(R.drawable.ic_back_black);
                    switchContent(++currentOne);
                }else if(currentOne==actionList.size()-2){
                    switchContent(++currentOne);
                    //nextMovement.setImageResource(R.drawable.ic_more_gray);
                }else{
                    //nextMovement.setImageResource(R.drawable.ic_more_gray);
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
