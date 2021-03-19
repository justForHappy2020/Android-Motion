package com.example.motion.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.motion.Entity.Action;
import com.example.motion.Entity.Course;
import com.example.motion.Entity.MultipleItem;
import com.example.motion.R;
import com.example.motion.Widget.MultipleItemQuickAdapter;
import com.example.motion.Widget.RelatedCoursesAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class sport_activity_course_detail extends BaseNetworkActivity implements View.OnClickListener{

    private ImageView ivBack;
    private ImageView ivBackgroundImg;
    private TextView tvCourseName;
    private TextView tvCourseClassification;
    private TextView tvCourseDuration;
    private TextView tvCourseMovementsNum;
    private TextView tvCourseOnlineData;
    private TextView tvCourseIntroAdvice;
    private TextView tvCourseIntroSuitable;
    private TextView tvCourseIntroEffect;
    private ImageButton ibLikeCourse;
    private Button btSelectCourse;

    private RecyclerView rvCourseActions;
    private RecyclerView rvRelatedCourses;

    private Course course;
    private List<MultipleItem> courseActionsList;
    private List<Course> relatedCoursesList;
    private List<Action> actionList;
    private MultipleItemQuickAdapter actionAdapter;
    private RelatedCoursesAdapter courseAdapter;

    private Handler handler;

    private static final int INIT_RELATED_COURSES_SUCCESS = 1;
    private static final int INIT_RELATED_COURSES_FAILED= 2;

    private static final int INIT_COURSE_ACTIONS_SUCCESS = 3;
    private static final int INIT_COURSE_ACTIONS_FAILED= 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sport_activity_course_detail);

        initHandler();
        initView();
        initEvent();
        initData();
        initCourseActions();
        initRelatedCourses();
    }

    private void initHandler(){
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case INIT_RELATED_COURSES_SUCCESS:
                        courseAdapter.notifyDataSetChanged();
                        break;
                    case INIT_RELATED_COURSES_FAILED:
                        Log.i("course_detail","INIT_RELATED_COURSES_FAILED");
                        Toast.makeText(getBaseContext(), getText(R.string.course_detail_failed_related_course), Toast.LENGTH_SHORT).show();
                        break;
                    case INIT_COURSE_ACTIONS_SUCCESS:
                        actionAdapter.notifyDataSetChanged();
                        break;
                    case INIT_COURSE_ACTIONS_FAILED:
                        Log.i("course_detail","INIT_COURSE_ACTIONS_FAILED");
                        Toast.makeText(getBaseContext(), getText(R.string.course_detail_failed_actions), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    private void initView(){
        ivBack = findViewById(R.id.iv_back);
        ivBackgroundImg = findViewById(R.id.iv_course_img_main);
        tvCourseName = findViewById(R.id.tv_course_name);
        tvCourseClassification = findViewById(R.id.tv_course_classification);
        tvCourseDuration = findViewById(R.id.tv_course_duriation);
        tvCourseMovementsNum = findViewById(R.id.tv_course_nums_movement);
        tvCourseOnlineData = findViewById(R.id.tv_course_online_date);
        tvCourseIntroAdvice = findViewById(R.id.tv_content_advice);
        tvCourseIntroSuitable = findViewById(R.id.tv_content_suitable);
        tvCourseIntroEffect = findViewById(R.id.tv_content_effect);
        ibLikeCourse = findViewById(R.id.ib_course_like);
        btSelectCourse = findViewById(R.id.btn_course_select);
        rvCourseActions = findViewById(R.id.rv_course_movements);
        rvRelatedCourses = findViewById(R.id.rv_course_related);
    }

    private void initEvent(){
        ivBack.setOnClickListener(this);
        ibLikeCourse.setOnClickListener(this);
        btSelectCourse.setOnClickListener(this);

    }

    private void initData(){
        //course = (Course)getIntent().getSerializableExtra("course");

        //--only for test--
        course = new Course();
        actionList = new ArrayList<>();

        course.setCourseId(Long.valueOf(10086));
        course.setBackgroundUrl("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimage.biaobaiju.com%2Fuploads%2F20190508%2F14%2F1557298531-XCGFxUNypS.jpg&refer=http%3A%2F%2Fimage.biaobaiju.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1618219760&t=fad175df947a43f0813cba0c11f335bc");
        course.setCourseName("初级健身");
        course.setDuration("00:30");
        course.setIFOnline("online");
        course.setTargetAge("4-6");
        course.setType("减脂/人气课/亲子运动系列");
        Action action1 = new Action(Long.valueOf(1),"抱腿","https://dss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=134894814,3258319714&fm=26&gp=0.jpg",getCacheDir().toString()+"/movement1.mp4","00:12",Long.valueOf(1),"介绍");
        Action action2 = new Action(Long.valueOf(1),"前爬","https://dss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1828457785,1350485149&fm=26&gp=0.jpg",getCacheDir().toString()+"/movement2.mp4","00:18",Long.valueOf(1),"介绍");
        action1.setType(Action.COUNTING);
        action1.setTime(2);

        action2.setType(Action.TIMING);
        action2.setTime(3);

        actionList.add(action1);
        actionList.add(action2);

        //-----------------

        if(null != course){
            tvCourseName.setText(course.getCourseName() + course.getTargetAge());
            tvCourseClassification.setText(course.getType());//getCourseClassification
            tvCourseDuration.setText(course.getDuration());
            tvCourseMovementsNum.setText(String.valueOf(actionList.size()));

            tvCourseIntroAdvice.setText("");
            tvCourseIntroSuitable.setText("");
            tvCourseIntroEffect.setText("");

            Glide.with(this)
                    .load(course.getBackgroundUrl())
                    .error(R.drawable.ic_load_pic_error)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(ivBackgroundImg);

            switch (course.getIFOnline()){
                case "online":
                    tvCourseOnlineData.setVisibility(View.GONE);
                    tvCourseOnlineData.setVisibility(View.GONE);
                    btSelectCourse.setText(R.string.course_detail_button_bottom_attend);
                    break;
                case "comming":
                    tvCourseOnlineData.setVisibility(View.VISIBLE);
                    tvCourseOnlineData.setText("");
                    btSelectCourse.setText(R.string.course_detail_button_bottom_reserved);
                    break;
            }
        }
    }

    private void initCourseActions(){
        courseActionsList = new ArrayList();

        for(int i=0;i<actionList.size();i++){
            courseActionsList.add(new MultipleItem(MultipleItem.ACTION,actionList.get(i)));
        }

        LinearLayoutManager layoutM = new LinearLayoutManager(this);
        layoutM.setOrientation(LinearLayoutManager.HORIZONTAL);

        actionAdapter = new MultipleItemQuickAdapter(courseActionsList);
        rvCourseActions.setLayoutManager(layoutM);
        rvCourseActions.setAdapter(actionAdapter);

        actionAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = new Intent(getBaseContext(),sport_activity_course_action_detail.class);
                intent.putExtra("courseActionPosition",position);
                intent.putExtra("actionList",(Serializable) actionList);
                startActivity(intent);
            }
        });

        /*
        final Thread getCourseActions = new Thread(new Runnable() {
            @Override
            public void run() {

                //courseActionsList = course.getActionList();

                Message handlerMsg = handler.obtainMessage();
                if(courseActionsList.isEmpty()){//should use http code to decide here
                    handlerMsg.what = INIT_COURSE_ACTIONS_FAILED;
                }else{
                    handlerMsg.what = INIT_COURSE_ACTIONS_SUCCESS;
                }
                handler.sendMessage(handlerMsg);
            }
        });
        rvCourseActions.post(new Runnable() {
            @Override
            public void run() {
                getCourseActions.start();
            }
        });
         */

    }

    private void initRelatedCourses(){
        relatedCoursesList = new ArrayList();

        courseAdapter = new RelatedCoursesAdapter(R.layout.sport_item_course_related, relatedCoursesList);
        rvRelatedCourses.setLayoutManager(new LinearLayoutManager(this));
        rvRelatedCourses.setAdapter(courseAdapter);

        final Thread getRelatedCourses = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //---only fot test---
                    sleep(3000);
                    Course testCourse = new Course();
                    testCourse.setCourseName("测试名称");
                    testCourse.setTargetAge("3-6岁");
                    testCourse.setBackgroundUrl("https://iknow-pic.cdn.bcebos.com/10dfa9ec8a136327fcb788d99f8fa0ec09fac786?x-bce-process=image/resize,m_lfit,w_600,h_800,limit_1/quality,q_85");
                    Log.i("initRelatedCourses","test_course_added");
                    relatedCoursesList.add(testCourse);
                    //-------------------

                    Message handlerMsg = handler.obtainMessage();
                    if(relatedCoursesList.isEmpty()){//should use http code to decide here
                        handlerMsg.what = INIT_RELATED_COURSES_FAILED;
                    }else{
                        handlerMsg.what = INIT_RELATED_COURSES_SUCCESS;
                    }
                    handler.sendMessage(handlerMsg);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        rvRelatedCourses.post(new Runnable() {
            @Override
            public void run() {
                getRelatedCourses.start();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.ib_course_like:
                //
                break;
            case R.id.btn_course_select:

                //---only for test---
                Intent intent = new Intent(this,sport_activity_course_start.class);
                intent.putExtra("course",course);
                intent.putExtra("actionList", (Serializable) actionList);
                startActivity(intent);
                //-------------------

                break;
        }
    }

}
