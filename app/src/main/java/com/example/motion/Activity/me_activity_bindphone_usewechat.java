package com.example.motion.Activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.motion.R;
import com.example.motion.Utils.HttpUtils;
import com.example.motion.Utils.KeyboardUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.motion.Utils.HttpUtils.connectHttp;

public class me_activity_bindphone_usewechat extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_back;
    private EditText et_phone;
    private EditText et_code;
    private Button btn_getcode;
    private Button btn_save;

    private int httpcode;
    private String mobile;
    private String str_phone;//查询到的UserID对应的号码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_activity_bindphone_changephone);
        initview();
    }

    private void initview() {
        iv_back = findViewById(R.id.iv_back);
        et_phone = findViewById(R.id.et_phonenum);
        et_code = findViewById(R.id.et_code);
        btn_getcode = findViewById(R.id.btn_getcode);
        btn_save = findViewById(R.id.btn_save);

        btn_getcode.setOnClickListener(this);
        btn_getcode.setEnabled(Boolean.FALSE);

        et_phone.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(et_phone.getText()) && !TextUtils.isEmpty(et_phone.getText())) {
                    btn_getcode.setEnabled(Boolean.TRUE);//启用按钮
                }else{
                    btn_getcode.setEnabled(Boolean.FALSE);//不启用按钮
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        //设置延迟并弹出小键盘
        et_code.setFocusable(true);
        Timer timer = new Timer();
        timer.schedule(new TimerTask(){
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et_code,InputMethodManager.SHOW_FORCED);
                et_code.setOnFocusChangeListener(new View.OnFocusChangeListener(){
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(hasFocus) KeyboardUtils.showKeyboard(et_code);
                        else KeyboardUtils.hideKeyboard(et_code);
                    }
                });
            }
        },998);
    }

    // 根据 UserId  查找电话 与输入电话对比
private void checkPhone_number() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "";//url
                String responseData = null;
                JSONObject json = new JSONObject();
                try {
                    json.put("User","" );//UserId
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    responseData = HttpUtils.connectHttpGet(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(responseData);
                    httpcode = jsonObject1.getInt("code");
                    if (httpcode == 200) {
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("User");//
                        int phone_number = jsonObject2.getInt("PhoneNumber");//获取电话
                        str_phone = Integer.toString(phone_number);
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_getcode:
                mobile = et_phone.getText().toString().trim();
                if(mobile == str_phone){
                    Toast.makeText(this,"已绑定手机号",Toast.LENGTH_SHORT).show();// 该UserId已经设置手机号
                }
                if(mobile.length()!=11){
                    Toast.makeText(this,  "手机号格式不正确，请重新输入", Toast.LENGTH_SHORT).show();
                }
                else{
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //设置JSON数据
                                JSONObject json = new JSONObject();
                                try {
                                    json.put("PhoneNumber", mobile);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                String url = "";// url
                                //String url = "http://159.75.2.94:8080/api/user/getCode";
                                String responseData = connectHttp(url,json);
                                getfeedback(responseData);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if(httpcode==200){
                                Toast.makeText(me_activity_bindphone_usewechat.this,"验证码已发送",Toast.LENGTH_SHORT).show();
                                new CountDownTimer(60000, 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        btn_getcode.setEnabled(false);
                                        btn_getcode.setText(String.format("重新获取(%ds)",millisUntilFinished/1000));
                                    }

                                    @Override
                                    public void onFinish() {
                                        btn_getcode.setEnabled(Boolean.TRUE);
                                        btn_getcode.setText("重新获取");
                                    }
                                }.start();
                            }
                        }
                        private void getfeedback(String responseData) {
                            try {
                                //解析JSON数据
                                JSONObject jsonObject1 = new JSONObject(responseData);
                                httpcode = jsonObject1.getInt("code");
/*                        JSONArray jsonArray = jsonObject1.getJSONArray("codes");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            //相应的内容
                            String message = jsonObject.getString("message");
                            int code = jsonObject.getInt("code");
                        }*/


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
                    if(httpcode!=200){
                        Toast.makeText(this,"ERROR",Toast.LENGTH_SHORT).show();
                        btn_getcode.setEnabled(Boolean.TRUE);
                    }

                }
                break;

            case R.id.iv_back:
//              Intent intent = new Intent(this,me_activity_me_homepage);
//              startActivity(intent);
                break;

            case R.id.btn_save:
                final String code = et_code.getText().toString().trim();
                Thread thread2 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //设置JSON数据
                            JSONObject json = new JSONObject();
                            try {
                                json.put("code", code);
                                json.put("PhoneNumber",mobile);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String responseData = connectHttp("http://159.75.2.94:8080/api/user/login",json);//okhttp
                            //getfeedback(responseData);
                            try {
                                JSONObject jsonObject = new JSONObject(responseData);
                                //message = jsonObject.getString("message");
                                httpcode = jsonObject.getInt("code");
                                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
//                                isNewUser = jsonObject1.getBoolean("newUser");
//                                userId = jsonObject1.getLong("userId");
//                                token = jsonObject1.getString("token");
                            } catch (JSONException e){
                                e.printStackTrace();
                            }
//                            SharedPreferences.Editor editor = saveSP.edit();
//                            editor.putLong("userId",userId).commit();
//                            editor.putString("token",token).commit();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if(httpcode==200){
//                            //判定是否新用户，新用户跳转注册页面，旧用户跳转主页
//                            if(isNewUser) startActivity(intent);
//                            else startActivity(intent2);
                            //更改成功 跳转回首页
//                            Intent intent = new Intent(this,homepage_activity_homepage);
//                            startActivity(intent);
                        }
                    }

/*                    private void getfeedback(String responseData) {
                        try {
                            JSONObject jsonObject1 = new JSONObject(responseData);
                            JSONArray jsonArray = jsonObject1.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                //相应的内容
                                String message = jsonObject.getString("message");
                                String data = jsonObject.getString("data");
                                int code = jsonObject.getInt("code");
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }*/

                });
                thread2.start();
                try {
                    thread2.join(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(httpcode!=200)Toast.makeText(me_activity_bindphone_usewechat.this,"验证码有误，请重新输入",Toast.LENGTH_SHORT).show();
                break;
        }

    }
}
//problem
//用户填写手机号已经存在是指该用户已经填写过手机号还是该手机号已经被注册