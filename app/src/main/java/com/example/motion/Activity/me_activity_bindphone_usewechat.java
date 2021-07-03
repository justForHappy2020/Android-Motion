package com.example.motion.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.motion.Fragment.me_fragment_main;
import com.example.motion.R;
import com.example.motion.Utils.KeyboardUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class me_activity_bindphone_usewechat extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_back;
    private EditText et_phone;
    private EditText et_code;
    private Button btn_getcode;
    private Button btn_save;
    private SharedPreferences saveSP ;

    private int httpcode;
    private String mobile;
    private String get_phone;//查询到的UserID对应的号码
    private String loginCode;//填写的验证码
    private Boolean isNewUser;
    // Volley
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_activity_bindphone_usewechat);
        initview();
    }

    private void initview() {
        iv_back = findViewById(R.id.iv_back);
        et_phone = findViewById(R.id.et_phonenum);
        et_code = findViewById(R.id.et_code);
        btn_getcode = findViewById(R.id.btn_getcode);
        btn_save = findViewById(R.id.btn_save);
        saveSP = this.getSharedPreferences("saveSP",MODE_PRIVATE);

        btn_getcode.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_getcode.setEnabled(Boolean.FALSE);

        et_phone.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(et_phone.getText()) && et_phone.getText().toString().trim().length() == 11) {
//                    btn_getcode.setBackgroundColor(Color.parseColor("#673AB7"));
                    btn_getcode.setTextColor(Color.parseColor("#673AB7"));
                    btn_getcode.setEnabled(Boolean.TRUE);//启用按钮
                    btn_save.setEnabled(Boolean.TRUE);
                }else{
                    //btAcquireCode.setBackgroundColor(Color.GREEN);
//                    btn_getcode.setBackgroundColor(Color.parseColor("#D1C4E9"));
                    btn_getcode.setTextColor(Color.parseColor("#FF808080"));
                    btn_getcode.setEnabled(Boolean.FALSE);//不启用按钮
                    btn_save.setEnabled(Boolean.FALSE);
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
    /*
    * 检查输入的号码和旧号码
    * */
    private void checkPhone(){

        final AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(me_activity_bindphone_usewechat.this);
        alterDiaglog.setTitle("提示：");//文字
        alterDiaglog.setMessage("是否合并使用手机号注册的账户下的信息");//提示消息
        //积极的选择
        alterDiaglog.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(me_activity_bindphone_usewechat.this,"点击了 是", Toast.LENGTH_SHORT).show();
            }
        });
        //消极的选择
        alterDiaglog.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(me_activity_bindphone_usewechat.this,"点击了 否", Toast.LENGTH_SHORT).show();
            }
        });
        //中立的选择
        alterDiaglog.show();
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
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
                                        Toast.makeText(me_activity_bindphone_usewechat.this,"验证码已发送", Toast.LENGTH_SHORT).show();
                                        new CountDownTimer(60000, 1000) {
                                            @Override
                                            public void onTick(long millisUntilFinished) {
                                                btn_getcode.setEnabled(Boolean.FALSE);
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
                    queue.add(getCode);

                }
                break;

            case R.id.iv_back:
                Intent intent = new Intent(this, me_fragment_main.class);
                startActivity(intent);
                finish();
                break;

            case R.id.btn_save:
                mobile = et_phone.getText().toString().trim();
                loginCode = et_code.getText().toString().trim();
                if (loginCode.isEmpty() || mobile.isEmpty() ){
                    Toast.makeText(me_activity_bindphone_usewechat.this,"缺少选项", Toast.LENGTH_SHORT).show();
//                    btn_login.setBackgroundColor(Color.parseColor("#673AB7"));
//                    btn_save.setEnabled(Boolean.FALSE);
                }
                else {
                    JSONObject json_login = new JSONObject();
                    try {
                        json_login.put("code", loginCode);
                        json_login.put("phoneNumber", mobile);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String url = "http://106.55.25.94:8080/api/user/changePhone";
                    JsonObjectRequest getCode = new JsonObjectRequest(url, json_login,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject obj) {//处理response
                                    System.out.println("----------:" + obj);
                                    try {
                                        if (obj.getInt("code")==600){
                                            Toast.makeText(me_activity_bindphone_usewechat.this, "登录已过期，请重新登录", Toast.LENGTH_SHORT).show();
                                        }
                                        else if(obj.getInt("code")==200 && obj.getString("data").equals("成功")){
                                            Toast.makeText(me_activity_bindphone_usewechat.this, "修改成功", Toast.LENGTH_SHORT).show();
                                            SharedPreferences.Editor editor = saveSP.edit();
                                            editor.putString("phoneNumber",mobile).commit();
                                            Intent intent = new Intent(me_activity_bindphone_usewechat.this, viewpager_activity_main.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else {
                                            Toast.makeText(me_activity_bindphone_usewechat.this, "验证码错误", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.getMessage();
                            Toast.makeText(me_activity_bindphone_usewechat.this,"更改失败",Toast.LENGTH_SHORT).show();
                        }

                    });
                    queue.add(getCode);
                    //判定是否新用户，新用户跳转注册页面，旧用户跳转主页
//                    更改成功 跳转回首页
//                            Intent intent_homrpage = new Intent(this,homepage_activity_homepage);
//                            startActivity(intent);

                }
                break;
        }

    }
}
//problem
//用户填写手机号已经存在是指该用户已经填写过手机号还是该手机号已经被注册