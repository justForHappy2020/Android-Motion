package com.example.motion.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.example.motion.Entity.DyxItem;
import com.example.motion.Entity.Feedback;
import com.example.motion.Entity.HealthRecord;
import com.example.motion.R;
import com.example.motion.Utils.UserInfoManager;
import com.example.motion.Widget.DyxQuickAdapter;
import com.example.motion.Widget.MyStringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class me_activity_help_my_feedback extends NeedTokenActivity implements View.OnClickListener{
    private final int GET_FEEDBACK_FAILED = 0;
    private final int GET_FEEDBACK_SUCCESS = 1;
    private boolean hasNext;
    private Handler handler;
    private String token;
    private Long page = Long.valueOf(1);
    private ImageView ivBack;
    private RecyclerView rvFeedback;
    private DyxQuickAdapter feedbackAdapter;
    private List<DyxItem> feedbackList = new ArrayList();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.me_activity_feedback_myfeedback);
        super.onCreate(savedInstanceState);
        initHandler();
        initView();
        initData();
    }

    private void initHandler(){
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case GET_FEEDBACK_FAILED:
                        Log.d("me_activity_feedback_Handler","GET_FEEDBACK_FAILED:"+msg.obj);
                        feedbackAdapter.notifyDataSetChanged();
                        break;
                    case GET_FEEDBACK_SUCCESS:
                        Log.d("me_activity_feedback","SEND_FEEDBACK_SUCCESS");
                        feedbackAdapter.notifyDataSetChanged();
                        break;
                }
            }
        };
    }

    private void initView(){
        token = UserInfoManager.getUserInfoManager(this).getToken();
        ivBack = findViewById(R.id.me_feedback_back);
        rvFeedback = findViewById(R.id.rv_feedback);
        ivBack.setOnClickListener(this);
        LinearLayoutManager feedbackManager = new LinearLayoutManager(this);
        rvFeedback.setLayoutManager(feedbackManager);
        feedbackAdapter = new DyxQuickAdapter(feedbackList);
        rvFeedback.setAdapter(feedbackAdapter);
        feedbackAdapter.setAnimationEnable(true);
        feedbackAdapter.getLoadMoreModule().setAutoLoadMore(false);
        feedbackAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                configLoadMoreCourse();
            }
        });
    }

    //首次以及拉取更多数据
    private void configLoadMoreCourse() {
        if(hasNext){
            Log.d("feedback","configLoadMoreFeedback");
            page++;
            initData();
        }
    }

    private void initData(){
        String url = "http://106.55.25.94:8080/api/user/getFeedback?token=" + token + "&page=" + page + "&size=10";
        MyStringRequest getTagsStringRequest = new MyStringRequest(Request.Method.GET,  url, new Response.Listener<String>() {
            @Override
            public void onResponse(String responseStr) {
                try {
                    JSONObject jsonRootObject = new JSONObject(responseStr);
                    JSONObject jsonObject1 = jsonRootObject.getJSONObject("data");
                    //hasNext = jsonObject1.getBoolean("hasNext");
                    JSONArray JSONArrayFeedback = jsonObject1.getJSONArray("myFeedbackList");
                    for (int i = 0; i < JSONArrayFeedback.length(); i++) {
                        JSONObject jsonObject2 = JSONArrayFeedback.getJSONObject(i);
                        //相应的内容
                        Feedback feedback = new Feedback();
                        feedback.setContent(jsonObject2.getString("content"));
                        feedback.setCreateTime(jsonObject2.getString("createTime"));
                        feedback.setFeedbackId(jsonObject2.getLong("feedbackId"));
                        feedback.setPicUrls(jsonObject2.getString("picUrls"));
                        feedbackList.add(new DyxItem(DyxItem.FEEDBACK , feedback));
                    }
                    if(!hasNext)feedbackAdapter.getLoadMoreModule().loadMoreEnd();
                    Message msg = handler.obtainMessage();
                    msg.what = GET_FEEDBACK_SUCCESS;
                    handler.sendMessage(msg);

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Message msg = handler.obtainMessage();
                msg.what = GET_FEEDBACK_FAILED;
                msg.obj = volleyError.toString();
                handler.sendMessage(msg);
            }
        });
        getTagsStringRequest.setTag("getHttp");
        requestQueue.add(getTagsStringRequest);
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