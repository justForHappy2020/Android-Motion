package com.example.motion.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.motion.R;
import com.example.motion.Utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static com.example.motion.Utils.HttpUtils.connectHttp;

public class me_activity_bindphone_usephone extends AppCompatActivity implements View.OnClickListener {

    private Button btn_changephone;
    private ImageView iv_back;
    private TextView tv_phone_number;

    private SharedPreferences saveSP;
    private int UserID;
    private String token;
    private String phoneNumber;

    private int httpcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_activity_bindphone_usephone);
        initview();
        //initPhone_number();
    }

    private void initview(){
        btn_changephone = findViewById(R.id.btn_changephone);
        iv_back = findViewById(R.id.iv_back);
        tv_phone_number = findViewById(R.id.tv_your_phone);

        iv_back.setOnClickListener(this);
        btn_changephone.setOnClickListener(this);

    }
    // 获取 电话号码
    private void initPhone_number() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "";//url
                String responseData = null;
                JSONObject json = new JSONObject();
                try {
                    json.put("UserID",UserID );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    responseData = HttpUtils.connectHttp(url, json);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(responseData);
                    httpcode = jsonObject1.getInt("code");
                    if (httpcode == 200) {
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("User");//
                        int phone_number = jsonObject2.getInt("PhoneNumber");//获取电话
                        String str_phone = Integer.toString(phone_number);
                        tv_phone_number.setText(str_phone);
                    }
                } catch (JSONException e) {
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
        if(httpcode!=200) Toast.makeText(this,"ERROR", Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
//                Intent intent = new Intent(this,me_activity_me_homepage);
//                startActivity(intent);
                break;
            case R.id.btn_changephone:
                Intent intent = new Intent(this,me_activity_bindphone_changephone.class);
                startActivity(intent);
                break;
//            case R.id.tv_your_phone:
//
//                break;
        }
    }
}
//显示phone号码 v_your_phone
// 是用sharedPreference 获得本地存储得电话号码还是发请求获得电话号码