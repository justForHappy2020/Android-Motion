package com.example.motion.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.motion.Entity.Action;
import com.example.motion.Entity.MultipleItem;
import com.example.motion.R;
import com.example.motion.Utils.UserInfoManager;
import com.example.motion.Widget.PostJsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static com.example.motion.Utils.HttpUtils.connectHttp;

public class me_activity_bodydata_addfile extends NeedTokenActivity implements View.OnClickListener {
    private final int BODY_DATA_FILE_ADD_FAIL = 0;
    private final int BODY_DATA_FILE_ADD_SUCCESS = 1;

    private ImageView ivBack;
    private EditText etWeight;
    private EditText etHeight;
    private TextView tvFinish;
    private int httpcode;
    private Handler handler;
    //private  String token = "438092e5-cdd5-4ba3-9e27-430949b90b89";
    //private SharedPreferences readSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_activity_bodydata_addfile);
        //checkToken();
        initView();
        initHandler();
    }

    private void initView() {
        ivBack = findViewById(R.id.iv_back);
        etWeight = findViewById(R.id.et_weight);
        etHeight = findViewById(R.id.et_height);
        tvFinish = findViewById(R.id.tv_finish);

        ivBack.setOnClickListener(this);
        tvFinish.setOnClickListener(this);
    }
/*
    private void checkToken() {
        readSP=this.getSharedPreferences("saveSp",MODE_PRIVATE);
        token = readSP.getString("token","");
        if (token.isEmpty()){
            finish();
            Toast.makeText(this,"请登录", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, register_activity_register.class);
            startActivity(intent);
        }
    }

 */

    private void initHandler(){
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case BODY_DATA_FILE_ADD_FAIL:
                        Toast.makeText(me_activity_bodydata_addfile.this,"ERROR",Toast.LENGTH_SHORT).show();

                        break;
                    case BODY_DATA_FILE_ADD_SUCCESS:
                        Toast.makeText(me_activity_bodydata_addfile.this,"SUCCESSFUL",Toast.LENGTH_SHORT).show();
                        Intent intent;
                        intent = new Intent(me_activity_bodydata_addfile.this , me_activity_bodydata_main.class);
                        startActivity(intent);
                        finish();
                        break;

                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_finish:
                Float weight = Float.parseFloat(etWeight.getText().toString());
                Float height = Float.parseFloat(etHeight.getText().toString());
                //http保存信息（成员ID、身高体重）
                String url = "http://10.34.25.45:8080/api/community/saveHealthRecord";
                JSONObject json = new JSONObject();
                try {
                    json.put("token", UserInfoManager.getUserInfoManager(me_activity_bodydata_addfile.this).getToken());
                    json.put("height", height);
                    json.put("weight", weight);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                PostJsonRequest postJsonRequest = new PostJsonRequest(Request.Method.POST,url,json.toString(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseStr) {

                        try {
                            JSONObject jsonObject1 = new JSONObject(responseStr);
                            Message msg = handler.obtainMessage();
                            msg.what = BODY_DATA_FILE_ADD_SUCCESS;
                            handler.sendMessage(msg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Message msg = handler.obtainMessage();
                        msg.what = BODY_DATA_FILE_ADD_FAIL;
                        msg.obj=volleyError.toString();
                        handler.sendMessage(msg);
                    }
                });

                requestQueue.add(postJsonRequest);
                /*
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //设置JSON数据
                            JSONObject json = new JSONObject();
                            try {
                                json.put("token", UserInfoManager.getUserInfoManager(me_activity_bodydata_addfile.this).getToken());
                                json.put("height", height);
                                json.put("weight", weight);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String url = "http://10.34.25.45:8080/api/community/saveHealthRecord";
                            String responseData = connectHttp(url,json);
                            try {
                                JSONObject jsonObject1 = new JSONObject(responseData);
                                //相应的内容
                                httpcode = jsonObject1.getInt("code");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
                try {
                    thread.join(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                 */

                break;
        }
    }
}
