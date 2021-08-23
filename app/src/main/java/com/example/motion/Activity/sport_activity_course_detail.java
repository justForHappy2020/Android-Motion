package com.example.motion.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.motion.Entity.Action;
import com.example.motion.Entity.Course;
import com.example.motion.Entity.CourseTag;
import com.example.motion.Entity.MultipleItem;
import com.example.motion.MontionRequest.CollectCourseRequest;
import com.example.motion.R;
import com.example.motion.Utils.UserInfoManager;
import com.example.motion.Utils.CourseCacheUtil;
import com.example.motion.Utils.OnProcessStateChangeListener;
import com.example.motion.Widget.MultipleItemQuickAdapter;
import com.example.motion.VolleyRequest.PostJsonRequest;
import com.example.motion.Widget.RelatedCoursesAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.zzhoujay.richtext.RichText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;

/**
 * Intent传入参数：
 * 1)Long courseId 需要查看详情的课程id
 */

public class sport_activity_course_detail extends NeedTokenActivity implements View.OnClickListener{

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

    private Long courseId;
    private Course course = new Course();
    private List<MultipleItem> actionInMultiList;
    private List<Course> relatedCoursesList;
    private List<Action> actionList;
    private MultipleItemQuickAdapter actionAdapter;
    private RelatedCoursesAdapter courseAdapter;

    private Handler handler;


    private int currentOne;
    private ImageButton ivLastAction;
    private ImageButton ivNextAction;
    private ImageView ivHeaderImage;
    private Button btnStartAction;
    private TextView tvActionName;
    private TextView tvActionContent;


    private static final int INIT_RELATED_COURSES_SUCCESS = 1;
    private static final int INIT_RELATED_COURSES_FAILED= 2;

    private static final int INIT_COURSE_ACTIONS_SUCCESS = 3;
    private static final int INIT_COURSE_ACTIONS_FAILED= 4;

    private static final int GET_SUCCESS = 5;
    private static final int GET_FAILED= 6;

    private static final int COURSE_COLLECT_SUCCESS = 7;
    private static final int COURSE_COLLECT_FAILED = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sport_activity_course_detail);

        courseId = getIntent().getLongExtra("courseId",97);

        initHandler();
        initView();
        initEvent();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(!isTokenEmpty){
            Log.d("sport_activity_course_detail","isTokenEmpty not empty");
            courseId2Course(courseId,course);
            initCourseActions();
            initRelatedCourses();
        }else{
            Log.d("sport_activity_course_detail","isTokenEmpty ");
        }
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
                        Log.d("HANDLER_courseActionList.size",String.valueOf(actionInMultiList.size()));
                        actionAdapter.notifyDataSetChanged();
                        break;
                    case GET_FAILED:
                        Toast.makeText(sport_activity_course_detail.this, "GET_FAILED,"+msg.obj, Toast.LENGTH_LONG).show();
                        break;
                    case COURSE_COLLECT_SUCCESS:
                        try{
                            if(course.isCollected()){
                                ibLikeCourse.setImageResource(R.drawable.ic_heart);
                                Toast.makeText(sport_activity_course_detail.this, "COURSE_COLLECT_CANCEL_SUCCESS", Toast.LENGTH_LONG).show();
                            }else{
                                ibLikeCourse.setImageResource(R.drawable.ic_heart_fill);
                                Toast.makeText(sport_activity_course_detail.this, "COURSE_COLLECT_SUCCESS", Toast.LENGTH_LONG).show();
                            }
                            course.setCollected(!course.isCollected());
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        //Toast.makeText(sport_activity_course_detail.this, "COURSE_COLLECT_SUCCESS", Toast.LENGTH_LONG).show();
                        break;
                    case COURSE_COLLECT_FAILED:
                        Toast.makeText(sport_activity_course_detail.this, "COURSE_COLLECT_FAILED,"+msg.obj, Toast.LENGTH_LONG).show();
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

    private void initActionDetailView(View actionDetailView){
        tvActionName = actionDetailView.findViewById(R.id.movement_detail_name);
        ivLastAction =  actionDetailView.findViewById(R.id.movement_detail_last);
        ivNextAction =  actionDetailView.findViewById(R.id.movement_detail_next);
        ivHeaderImage =  actionDetailView.findViewById(R.id.movement_detail_mainimage);
        btnStartAction =  actionDetailView.findViewById(R.id.movement_detail_start);
        tvActionContent =  actionDetailView.findViewById(R.id.movement_detail_content);

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
        RichText.fromMarkdown(actionList.get(position).getIntro())
                .showBorder(false)
                .bind(this)
                .into(tvActionContent);
    }

    private void initEvent(){
        ivBack.setOnClickListener(this);
        ibLikeCourse.setOnClickListener(this);
        btSelectCourse.setOnClickListener(this);
    }

    private void parseCourse(String responseStr){
        Log.d("parseCourse",responseStr);
        try {
            JSONObject jsonObject1 = new JSONObject(responseStr);
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
            course.setIsOnline(jsonObject2.getInt("onLine"));
            course.setCollected(jsonObject2.getBoolean("collected"));
            course.setCourseId2CourseJson(responseStr);
            JSONArray JSONArrayLabels = jsonObject2.getJSONArray("labelsName");
            String labels = "";
            for(int j=0;j<JSONArrayLabels.length();j++){
                labels += (JSONArrayLabels.get(j)+"/");
            }

            course.setLabels(labels);
            //course.save();//?
            JSONArray JSONArrayAction = jsonObject2.getJSONArray("actionList");
            for (int i = 0; i < JSONArrayAction.length(); i++) {
                JSONObject jsonObject = JSONArrayAction.getJSONObject(i);
                //相应的内容
                Action action = new Action();
                action.setActionID(jsonObject.getLong("actionId"));
                action.setActionName(jsonObject.getString("actionName"));
                action.setActionImgs(jsonObject.getString("actionImgs"));
                action.setActionUrl(jsonObject.getString("actionUrl"));

                //String time[] = jsonObject.getString("duration").split(":");
                //int duration = Integer.parseInt(time[0])*3600 +Integer.parseInt(time[1])*60+Integer.parseInt(time[2]);
                action.setDuration(jsonObject.getInt("duration"));

                action.setIntro(jsonObject.getString("actionIntro"));
                action.setType(2);//jsonObject.getInt("type")
                action.setCount(jsonObject.getInt("count"));
                action.setTotal(jsonObject.getInt("total"));
                action.setRestDuration(5);//jsonObject.getInt("restDuration")
                action.setSizeByte(jsonObject.getInt("size"));
                //action.setOwnerCourse(course);
                actionList.add(action);
                actionInMultiList.add(new MultipleItem(MultipleItem.ACTION,action));
            }
            Message msg = handler.obtainMessage();
            msg.what = GET_SUCCESS;
            handler.sendMessage(msg);

        } catch (JSONException e) {
            e.printStackTrace();
            Message msg = handler.obtainMessage();
            msg.what = GET_FAILED;
            msg.obj=e.toString();
            handler.sendMessage(msg);
        }


    }

    private void courseId2Course(Long courseId,Course course){
        String courseId2CourseJson = getIntent().getStringExtra("courseId2CourseJson");
        actionList = new ArrayList<>();
        actionInMultiList = new ArrayList<>();

        if(null != courseId2CourseJson  && !courseId2CourseJson.isEmpty()){
            parseCourse(courseId2CourseJson);
        }else {
            String url = "http://106.55.25.94:8080/api/course/getCourse";
            //String url = "https://www.fastmock.site/mock/318b7fee143da8b159d3e46048f8a8b3/api/courseId2All?courseId="+courseId;

            //String requestStr = "{\"courseId\": "+courseId+", \"token\":\""+ UserInfoManager.getUserInfoManager(this).getToken() +"\" }";

            JSONObject param = new JSONObject();
            try{
                param.put("courseId",courseId);
                param.put("token",UserInfoManager.getUserInfoManager(this).getToken());
            }catch (Exception e){
                e.printStackTrace();
            }

            System.out.println(param.toString());



            PostJsonRequest postJsonRequest = new PostJsonRequest(Request.Method.POST,url,param.toString(), new Response.Listener<String>() {
                @Override
                public void onResponse(String responseStr) {
                    parseCourse(responseStr);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                    Message msg = handler.obtainMessage();
                    msg.what = GET_FAILED;
                    msg.obj=volleyError.toString();
                    handler.sendMessage(msg);
                }
            });

            requestQueue.add(postJsonRequest);
        }

    }

    private void initData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(null != course){
                            tvCourseName.setText(course.getCourseName());//+ course.getTargetAge()
                            tvCourseClassification.setText(course.getLabels());//getCourseClassification
                            tvCourseDuration.setText(course.getDuration());
                            tvCourseMovementsNum.setText(String.valueOf(actionList.size()));
                            if(course.isCollected()){
                                ibLikeCourse.setImageResource(R.drawable.ic_heart_fill);
                            }else{
                                ibLikeCourse.setImageResource(R.drawable.ic_heart);
                            }

                            if(course.getCourseIntro()!=null){
                                String[] courseIntroArr = course.getCourseIntro().split("\\|");
                                tvCourseIntroAdvice.setText(courseIntroArr[0]);
                                tvCourseIntroSuitable.setText(courseIntroArr[1]);
                                tvCourseIntroEffect.setText(courseIntroArr[2]);
                            }

                            Glide.with(getBaseContext())
                                    .load(course.getBackgroundUrl())
                                    .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                                    .error(R.drawable.ic_load_pic_error)
                                    .into(ivBackgroundImg);

                            switch (course.getIsOnline()){
                                case CourseTag.TAG_ONLINE_YES:
                                    tvCourseOnlineData.setVisibility(View.GONE);
                                    tvCourseOnlineData.setVisibility(View.GONE);
                                    btSelectCourse.setText(R.string.course_detail_button_bottom_attend);
                                    break;
                                case CourseTag.TAG_ONLINE_NO:
                                    tvCourseOnlineData.setVisibility(View.VISIBLE);
                                    tvCourseOnlineData.setText(course.getCreateTime());
                                    btSelectCourse.setText(R.string.course_detail_button_bottom_reserved);
                                    break;
                            }
                        }
                    }
                });
            }
        }).start();

        actionAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                /*
                Intent intent = new Intent(getBaseContext(),sport_activity_course_action_detail.class);
                intent.putExtra("courseActionPosition",position);
                intent.putExtra("actionList",(Serializable) actionList);
                intent.putExtra("course",course);
                startActivity(intent);
                 */

                currentOne = position;
                BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(sport_activity_course_detail.this);
                View actionDetailView = getLayoutInflater().inflate(R.layout.sport_dialog_course_action_detail, null);
                initActionDetailView(actionDetailView);

                mBottomSheetDialog.setContentView(actionDetailView);
                mBottomSheetDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundColor(Color.TRANSPARENT);

                BottomSheetBehavior behavior = BottomSheetBehavior.from((View)actionDetailView.getParent());
                ViewGroup.LayoutParams params = actionDetailView.getLayoutParams();
                params.height = behavior.getPeekHeight();
                actionDetailView.setLayoutParams(params);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                mBottomSheetDialog.show();


            }
        });

    }

    private void initCourseActions(){

        LinearLayoutManager layoutM = new LinearLayoutManager(getBaseContext());
        layoutM.setOrientation(LinearLayoutManager.HORIZONTAL);

        actionAdapter = new MultipleItemQuickAdapter(actionInMultiList);
        rvCourseActions.setLayoutManager(layoutM);
        rvCourseActions.setAdapter(actionAdapter);



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
        courseAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = new Intent(getBaseContext(),sport_activity_course_detail.class);
                intent.putExtra("courseId",relatedCoursesList.get(position).getCourseId());
                startActivity(intent);
            }
        });

        final Thread getRelatedCourses = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //---only fot test---
                    sleep(3000);
                    Course testCourse = new Course();
                    testCourse.setCourseName("测试名称");
                    testCourse.setCourseId(Long.valueOf(97));
                    testCourse.setTargetAge("9-11岁");
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

    private void collectCourse(){
        Map<String,String> param = new HashMap<>();
        int flag;
        if(course.isCollected()){
            param.put("flag","0");
        }else{
            param.put("flag","1");
        }
        try{
            param.put("token",UserInfoManager.getUserInfoManager(this).getToken());
            param.put("courseId",String.valueOf(course.getCourseId()));
        }catch (Exception e){
            e.printStackTrace();
            Message msg = handler.obtainMessage();
            msg.what = COURSE_COLLECT_FAILED;
            msg.obj = "param build failed";
            msg.sendToTarget();
        }


        requestQueue.add(new CollectCourseRequest(param, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Message msg = handler.obtainMessage();
                msg.what = COURSE_COLLECT_SUCCESS;
                msg.sendToTarget();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Message msg = handler.obtainMessage();
                msg.what = COURSE_COLLECT_FAILED;
                msg.obj = error.toString();
                msg.sendToTarget();
            }
        }));
        /*
        String url = "http://106.55.25.94:8080/api/course/collectCourse?token="+token+"&courseId="+course.getCourseId()+"&flag="+flag;
        MyStringRequest stringRequest = new MyStringRequest(Request.Method.GET,  url, new Response.Listener<String>() {
            @Override
            public void onResponse(String responseStr) {
                Message msg = handler.obtainMessage();
                msg.what = COURSE_COLLECT_SUCCESS;
                handler.sendMessage(msg);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Message msg = handler.obtainMessage();
                msg.what = COURSE_COLLECT_FAILED;
                msg.obj = volleyError.toString();
                handler.sendMessage(msg);
            }
        });
        stringRequest.setTag("getHttp_collectCourse");
        requestQueue.add(stringRequest);

         */
    }

    private void reserveCourse(){

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
                collectCourse();
                break;
            case R.id.btn_course_select:

                switch (course.getIsOnline()){
                    case CourseTag.TAG_ONLINE_YES:
                        startCourse();
                        break;
                    case CourseTag.TAG_ONLINE_NO:
                        if(course.getIsOnline() == CourseTag.TAG_ONLINE_NO){
                            //book course API
                        }
                        break;
                }
                break;
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
                startCourse();
        }
    }

    private void startCourse(){
        CourseCacheUtil ccu = new CourseCacheUtil(this,getCacheDir());
        ccu.setOnChangeListener(new OnProcessStateChangeListener() {
            @Override
            public void onProcessDone(boolean isSuccess, Course courseWithActions,Object message) {
                if(isSuccess){

                    Intent intent = new Intent(getBaseContext(),sport_activity_course_start.class);
                    intent.putExtra("courseWithActions",courseWithActions);
                    startActivity(intent);

                    Log.d("course_detail","CourseCacheUtil_cache_success");
                }else{
                    Log.d("course_detail","CourseCacheUtil_cache_fail,"+(String)message);
                    Toast.makeText(sport_activity_course_detail.this, "CourseCacheUtil_cache_fail,"+(String)message, Toast.LENGTH_LONG).show();
                }
            }

        });
        ccu.process(course,actionList);

    }

}
