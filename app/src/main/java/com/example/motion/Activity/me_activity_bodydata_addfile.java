package com.example.motion.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.motion.R;
import com.example.motion.Utils.UserInfoManager;
import com.example.motion.VolleyRequest.PostJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                Float weight = null;
                Float height = null;
                String sWeight = etWeight.getText().toString();
                String sHeight = etHeight.getText().toString();
                Pattern p = Pattern.compile("[0-9.]*");
                if(p.matcher(sWeight).matches() && etWeight.length()!= 0) {
                    weight = Float.parseFloat(etWeight.getText().toString());
                }
                if(p.matcher(sHeight).matches() && etHeight.length()!= 0) {
                    height = Float.parseFloat(etHeight.getText().toString());
                }
                if (weight != null && height != null){
                    //http保存信息（成员ID、身高体重）
                    String url = "http://106.55.25.94:8080/api/community/saveHealthRecord";
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
                }
                else Toast.makeText(me_activity_bodydata_addfile.this, "请输入身高体重", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
