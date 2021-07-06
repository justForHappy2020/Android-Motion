package com.example.motion.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.motion.Entity.User;
import com.example.motion.Fragment.me_fragment_main;
import com.example.motion.R;
import com.example.motion.Utils.KeyboardUtils;
import com.example.motion.Utils.UserInfoManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class register_activity_register extends BaseNetworkActivity implements View.OnClickListener {
    private ImageView iv_delete;
    private EditText et_phone;
    private EditText et_code;
    private Button btn_getcode;
    private CheckBox btn_agree;
    private Button btn_login;
    private ImageView iv_wechat;
    private TextView tv_callService;//跳转客服

    private SharedPreferences saveSP ;

    private int httpcode;
    private String mobile;
    private String loginCode;//填写的验证码
    private Boolean isNewUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity_register);
        initview();
        loadDefaultPhoneNumber();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initview() {
        iv_delete = findViewById(R.id.iv_delete);
        et_phone = findViewById(R.id.et_phonenum);
        et_code = findViewById(R.id.et_code);
        btn_getcode = findViewById(R.id.btn_getcode);
        btn_agree = findViewById(R.id.btn_agree);
        btn_login = findViewById(R.id.btn_login);
        iv_wechat = findViewById(R.id.iv_wechat);//微信登录
        tv_callService = findViewById(R.id.tv_callService);//客服
        saveSP = this.getSharedPreferences("saveSp",MODE_PRIVATE);

        iv_delete.setOnClickListener(this);
        btn_getcode.setOnClickListener(this);
        btn_agree.setOnClickListener(this);
        btn_login.setOnClickListener(this);

        /**
         * 从第一阶段移除
         */
        //iv_wechat.setOnClickListener(this);

        tv_callService.setOnClickListener(this);//客服

        btn_getcode.setEnabled(Boolean.FALSE);
        btn_agree.setChecked(Boolean.FALSE);//checkbox设置

        et_phone.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(et_phone.getText()) && et_phone.getText().toString().trim().length() == 11 && btn_agree.isChecked()) {
//                    btn_getcode.setBackgroundColor(Color.parseColor("#673AB7"));
                    btn_getcode.setTextColor(Color.parseColor("#673AB7"));
                    btn_getcode.setEnabled(Boolean.TRUE);//启用按钮
                    btn_login.setEnabled(Boolean.TRUE);
                }else{
                    //btAcquireCode.setBackgroundColor(Color.GREEN);
//                    btn_getcode.setBackgroundColor(Color.parseColor("#D1C4E9"));
                    btn_getcode.setTextColor(Color.parseColor("#FF808080"));
                    btn_getcode.setEnabled(Boolean.FALSE);//不启用按钮
                    btn_login.setEnabled(Boolean.FALSE);
                }//判断是否启用获取验证码按钮
//                if (et_phone.getText().toString().trim().length()==11 && et_code.getText().toString().trim().length()==4 && btn_agree.isChecked()){
//                    btn_login.setBackgroundColor(Color.parseColor("#673AB7"));
//                    btn_login.setEnabled(Boolean.TRUE);//不启用按钮
//                }else {
//                    btn_login.setBackgroundColor(Color.parseColor("#D1C4E9"));
//                    btn_login.setEnabled(Boolean.FALSE);//不启用按钮
//                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

//        et_code.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (et_phone.getText().toString().trim().length()==11 && et_code.getText().toString().trim().length()==4 && btn_agree.isChecked()){
//                    btn_login.setSelected(Boolean.TRUE);
//                    btn_login.setEnabled(Boolean.TRUE);
//                }else {
//                    btn_login.setSelected(Boolean.FALSE);
//                    btn_login.setEnabled(Boolean.FALSE);
//                }
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        //设置延迟并弹出小键盘
        et_code.setFocusable(true);
        Timer timer = new Timer();
        timer.schedule(new TimerTask(){
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et_code, InputMethodManager.SHOW_FORCED);
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

    private void loadDefaultPhoneNumber(){
        if(null != et_phone){
            et_phone.setText(UserInfoManager.getUserInfoManager(this).getUser().getPhoneNumber());
            btn_login.setEnabled(Boolean.TRUE);
            Log.d("loadDefaultPhoneNumber","phone:"+UserInfoManager.getUserInfoManager(this).getUser().getPhoneNumber());
        }

    }

    private void jumpCalling(Context context) {//客服电话跳转
        String phoneNumber = getString(R.string.service_phone_number); ;
        Intent intentP = new Intent();
        intentP.setAction(Intent.ACTION_DIAL);
        intentP.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri data = Uri.parse("tel:"+phoneNumber);
        intentP.setData(data);

        if (intentP.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intentP);
        } else {
            //要调起的应用不存在时的处理
            Toast.makeText(context,  "callingError", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_getcode:
                mobile = et_phone.getText().toString().trim();
                if(mobile.length()!=11){
                    Toast.makeText(this,  "手机号格式不正确，请重新输入", Toast.LENGTH_SHORT).show();
                }
                else{
                    JSONObject json = new JSONObject();
                    try {
                        json.put("phoneNumber", mobile);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                   String url = "http://106.55.25.94:8080/api/user/getVerificationCode";// url
//                    String url = "https://www.fastmock.site/mock/8b3e2487a581d723a901a354dfc6f3fd/data/api/user/getCode";
                    JsonObjectRequest getCode = new JsonObjectRequest(url, json,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject obj) {//处理response
                                    System.out.println("----------:" + obj);//测试语句
                                    try {
//                                        JSONObject response = obj.getJSONObject("code");
                                        Toast.makeText(register_activity_register.this,"验证码已发送", Toast.LENGTH_SHORT).show();
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
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.getMessage();
                            btn_getcode.setEnabled(Boolean.TRUE);
                        }

                    });
                    requestQueue.add(getCode);

                }
                break;

            case R.id.iv_delete:
              Intent intent = new Intent(this, viewpager_activity_main.class);
              intent.putExtra("id",4);
              startActivity(intent);
              break;


            case R.id.btn_agree:
                btn_agree.setChecked(true);
                if (!TextUtils.isEmpty(et_phone.getText().toString()) && et_phone.getText().toString().trim().length() == 11 && btn_agree.isChecked()) {
//                    btn_getcode.setBackgroundColor(Color.parseColor("#673AB7"));
                    btn_getcode.setTextColor(Color.parseColor("#673AB7"));
                    btn_getcode.setEnabled(true);//启用按钮
                    btn_login.setEnabled(true);
                }else{
                    //btAcquireCode.setBackgroundColor(Color.GREEN);
//                    btn_getcode.setBackgroundColor(Color.parseColor("#D1C4E9"));
                    btn_getcode.setTextColor(Color.parseColor("#FF808080"));
                    btn_getcode.setEnabled(false);//不启用按钮
                    btn_login.setEnabled(false);
                }
                break;

            case R.id.btn_login:
                mobile = et_phone.getText().toString().trim();
                loginCode = et_code.getText().toString().trim();
                if (loginCode.isEmpty() || mobile.isEmpty() || !btn_agree.isChecked()){
                    Toast.makeText(register_activity_register.this,"缺少选项", Toast.LENGTH_SHORT).show();
//                    btn_login.setBackgroundColor(Color.parseColor("#673AB7"));
//                    btn_login.setEnabled(Boolean.FALSE);
                }
                else {
                    btn_login.setEnabled(Boolean.TRUE);
                    JSONObject json_login = new JSONObject();
                    try {
                        json_login.put("code", loginCode);
                        json_login.put("phoneNumber", mobile);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String url = "http://106.55.25.94:8080/api/user/login";
                    JsonObjectRequest getCode = new JsonObjectRequest(url, json_login,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject obj) {//处理response
                                    System.out.println("----------:" + obj);
                                    try {
                                        if(obj.getInt("code") == 200){
                                            JSONObject response = obj.getJSONObject("data");
                                            Toast.makeText(register_activity_register.this, "发送成功", Toast.LENGTH_SHORT).show();
                                            Long userId = response.getLong("userId");
                                            String nickName = response.getString("nickName");
                                            String headPortrait = response.getString("headPortrait");
                                            String token = response.getString("token");
                                            isNewUser = response.getBoolean("newUser");

                                            SharedPreferences.Editor editor = saveSP.edit();
                                            editor.putLong("userId",userId).commit();
                                            editor.putString("nickName",nickName).commit();
                                            editor.putString("headPortrait",headPortrait).commit();
                                            editor.putString("token",token).commit();
                                            editor.putString("phoneNumber",mobile).commit();
                                            Intent intent = new Intent(register_activity_register.this, viewpager_activity_main.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else if(obj.getInt("code")==600){
                                            Toast.makeText(register_activity_register.this,"登录已过期，请重新登录",Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            String message = obj.getString("message");
                                            Toast.makeText(register_activity_register.this,message,Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.getMessage();
                            Toast.makeText(register_activity_register.this,"登录失败",Toast.LENGTH_SHORT).show();
                        }

                    });
                    requestQueue.add(getCode);
                    //判定是否新用户，新用户跳转注册页面，旧用户跳转主页
//                    更改成功 跳转回首页
//                            Intent intent_homrpage = new Intent(this,homepage_activity_homepage);
//                            startActivity(intent);

                }
                   break;
            case R.id.tv_callService:
                jumpCalling(this);
              break;
        }
    }
}

//iv_delete 注释
// 跳转主页242
// 初始跳转没弄
