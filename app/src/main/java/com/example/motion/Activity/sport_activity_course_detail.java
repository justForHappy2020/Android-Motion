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
import com.example.motion.Utils.CourseCacheUtil;
import com.example.motion.Utils.HttpUtils;
import com.example.motion.Utils.OnActionProcessStateChangeListener;
import com.example.motion.Widget.MultipleItemQuickAdapter;
import com.example.motion.Widget.RelatedCoursesAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

    private Course course = new Course();;
    private List<MultipleItem> actionInMutiList;
    private List<Course> relatedCoursesList;
    private List<Action> actionList;
    private MultipleItemQuickAdapter actionAdapter;
    private RelatedCoursesAdapter courseAdapter;

    private Handler handler;

    private static final int INIT_RELATED_COURSES_SUCCESS = 1;
    private static final int INIT_RELATED_COURSES_FAILED= 2;

    private static final int INIT_COURSE_ACTIONS_SUCCESS = 3;
    private static final int INIT_COURSE_ACTIONS_FAILED= 4;

    private static final int GET_SUCCESS = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sport_activity_course_detail);

        initHandler();
        initView();

        courseId2Course(1,course);

        initEvent();
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
                    case GET_SUCCESS:
                        Log.d("HANDLER","GET_SUCCESS");
                        initData();
                        Log.d("HANDLER_courseActionList.size",String.valueOf(actionInMutiList.size()));
                        actionAdapter.notifyDataSetChanged();
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

    private void courseId2Course(int courseId, Course course){
        //String url = "http://10.34.25.45:8080/api/course/courseId2Course?courseId="+courseId;
        String url = "https://www.fastmock.site/mock/1f8fe01c6b3cdb34a1d2ad4b1a45a8c0/motion/api/courseId2Course?courseId="+courseId;
        actionList = new ArrayList<>();
        actionInMutiList = new ArrayList<>();

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String responseData = null;
                try {
                    responseData = HttpUtils.connectHttpGet(url);
                    JSONObject jsonObject1 = new JSONObject(responseData);
                    int httpcode = jsonObject1.getInt("code");
                    if(httpcode == 200){
                        //Log.i("responseData ",responseData);
                        
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("data");
                        //得到course
                        course.setCourseId(jsonObject2.getLong("courseId"));
                        course.setCourseName(jsonObject2.getString("courseName"));
                        course.setBackgroundUrl(jsonObject2.getString("backgroundUrl"));
                        course.setDuration(jsonObject2.getString("duration"));
                        course.setHit(jsonObject2.getInt("hits"));
                        course.setCreateTime(jsonObject2.getString("createTime"));
                        course.setCourseIntro(jsonObject2.getString("courseIntro"));
                        course.setTargetAge(jsonObject2.getString("targetAge"));
                        course.setIsOnline("online");
                        JSONArray JSONArrayAction = jsonObject2.getJSONArray("actionList");
                        for (int i = 0; i < JSONArrayAction.length(); i++) {
                            JSONObject jsonObject = JSONArrayAction.getJSONObject(i);
                            //相应的内容
                            Action action = new Action();
                            action.setActionID(jsonObject.getLong("actionId"));
                            action.setActionName(jsonObject.getString("actionName"));
                            action.setActionImgs(jsonObject.getString("actionImgs"));
                            action.setActionUrl(jsonObject.getString("actionUrl"));
                            action.setDuration(jsonObject.getInt("duration"));
                            action.setIntro(jsonObject.getString("intro"));
                            action.setType(jsonObject.getInt("type"));//
                            action.setCount(jsonObject.getInt("count"));
                            action.setTotal(jsonObject.getInt("total"));
                            action.setRestDuration(jsonObject.getInt("restDuration"));
                            actionList.add(action);
                            actionInMutiList.add(new MultipleItem(MultipleItem.ACTION,action));
                        }

                        Message msg = handler.obtainMessage();
                        msg.what = GET_SUCCESS;
                        handler.sendMessage(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void initData(){
        //course = (Course)getIntent().getSerializableExtra("course");
        //--only for test--
/*
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

 */
        //-----------------

        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(null != course){
                            tvCourseName.setText(course.getCourseName() + course.getTargetAge());
                            tvCourseClassification.setText(course.getType());//getCourseClassification
                            tvCourseDuration.setText(course.getDuration());
                            tvCourseMovementsNum.setText(String.valueOf(actionList.size()));

                            tvCourseIntroAdvice.setText("");
                            tvCourseIntroSuitable.setText("");
                            tvCourseIntroEffect.setText("");

                            Glide.with(getBaseContext())
                                    .load(course.getBackgroundUrl())
                                    .error(R.drawable.ic_load_pic_error)
                                    .placeholder(R.drawable.ic_placeholder)
                                    .into(ivBackgroundImg);

                            switch (course.getIsOnline()){
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
                });
            }
        }).start();


    }

    private void initCourseActions(){

        LinearLayoutManager layoutM = new LinearLayoutManager(getBaseContext());
        layoutM.setOrientation(LinearLayoutManager.HORIZONTAL);

        actionAdapter = new MultipleItemQuickAdapter(actionInMutiList);
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
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.ib_course_like:
                //test
                actionList.get(0).save();
                //like here
                break;
            case R.id.btn_course_select:

                //---only for test---

                CourseCacheUtil ccu = new CourseCacheUtil(this,getCacheDir());
                ccu.setOnChangeListener(new OnActionProcessStateChangeListener() {
                    @Override
                    public void onActionsProcessDone(boolean isSuccess, List<Action> processedActionList) {
                        if(isSuccess){
                            actionList = processedActionList;

                            Intent intent = new Intent(getBaseContext(),sport_activity_course_start.class);
                            intent.putExtra("course",course);
                            intent.putExtra("actionList",(Serializable)processedActionList);
                            startActivity(intent);

                            Log.d("course_detail","CourseCacheUtil_cache_success");
                        }else{
                            Log.d("course_detail","CourseCacheUtil_cache_fail");
                        }
                    }


                });
                ccu.processActions(actionList);



                /*
                Intent intent = new Intent(this,sport_activity_course_start.class);
                intent.putExtra("course",course);
                intent.putExtra("actionList", (Serializable) ccu.cacheCourse(course,actionList));
                startActivity(intent);
                 */
                //-------------------

                break;
        }
    }

}
