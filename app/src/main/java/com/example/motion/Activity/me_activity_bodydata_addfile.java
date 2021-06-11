package com.example.motion.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.motion.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static com.example.motion.Utils.HttpUtils.connectHttp;

public class me_activity_bodydata_addfile extends Activity implements View.OnClickListener {
    private ImageView ivBack;
    private EditText etWeight;
    private EditText etHeight;
    private TextView tvFinish;
    private int httpcode;
    private  String token;
    private SharedPreferences readSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_activity_bodydata_addfile);
        checkToken();
        initView();
    }

    private void initView() {
        ivBack = findViewById(R.id.iv_back);
        etWeight = findViewById(R.id.et_weight);
        etHeight = findViewById(R.id.et_height);
        tvFinish = findViewById(R.id.tv_finish);

        ivBack.setOnClickListener(this);
        tvFinish.setOnClickListener(this);
    }

    private void checkToken() {
        readSP=this.getSharedPreferences("saveSP",MODE_PRIVATE);
        token = readSP.getString("token","");
        if (token.isEmpty()){
            finish();
            Toast.makeText(this,"请登录", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, register_activity_register.class);
            startActivity(intent);
        }
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
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //设置JSON数据
                            JSONObject json = new JSONObject();
                            try {
                                json.put("token", token);
                                //json.put("headPortrait", "https://pics2.baidu.com/feed/b64543a98226cffcaa9bbee9f1799a96f703eab3.jpeg?token=99bafe934f8d5948be9b5cacfd50c5e5");//test
                                json.put("height", height);
                                json.put("weight", weight);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String url = "http://10.34.25.45:8080/api/community/saveHealthRecord";
                            String responseData = connectHttp(url,json);
                            getfeedback(responseData);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    private void getfeedback(String responseData) {
                        try {
                            //解析JSON数据
                            JSONObject jsonObject1 = new JSONObject(responseData);
                            httpcode = jsonObject1.getInt("code");
                        } catch (JSONException e){
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
                if(httpcode==200){
                    Intent intent;
                    intent = new Intent(this , me_activity_bodydata_main.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(this,"ERROR",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }
}
