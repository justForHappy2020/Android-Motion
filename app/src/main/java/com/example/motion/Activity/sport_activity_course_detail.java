package com.example.motion.Activity;

import android.app.Activity;
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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.motion.Entity.Action;
import com.example.motion.Entity.Course;
import com.example.motion.R;
import com.example.motion.Widget.MultipleItemQuickAdapter;
import com.example.motion.Widget.RelatedCoursesAdapter;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class sport_activity_course_detail extends Activity implements View.OnClickListener {

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
    private List<Action> courseActionsList;
    private List<Course> relatedCoursesList;
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
        course = (Course)getIntent().getSerializableExtra("course");

        if(null != course){
            tvCourseName.setText(course.getCourseName());
            tvCourseClassification.setText("");//getCourseClassification
            tvCourseDuration.setText(course.getDuration());
            tvCourseMovementsNum.setText("");

            tvCourseOnlineData.setText("");//判断是不是

            tvCourseIntroAdvice.setText("");
            tvCourseIntroSuitable.setText("");
            tvCourseIntroEffect.setText("");

            Glide.with(this)
                    .load(course.getBackgroundUrl())
                    .error(R.drawable.ic_load_pic_error)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(ivBackgroundImg);
        }
    }

    private void initCourseActions(){
        courseActionsList = new ArrayList();

        actionAdapter = new MultipleItemQuickAdapter(courseActionsList);
        rvCourseActions.setLayoutManager(new LinearLayoutManager(this));
        rvCourseActions.setAdapter(actionAdapter);

        final Thread getCourseActions = new Thread(new Runnable() {
            @Override
            public void run() {
                //process here

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
                    //testCourse.setDegree("3-6岁");
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
                //
                break;
        }
    }
}
