package com.example.motion.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.motion.R;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static com.example.motion.Utils.HttpUtils.connectHttp;

public class me_activity_me_homepage extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_share;
    private ImageView iv_email;
    private TextView tv_myplace;
    private ImageView iv_myplace;//我的空间右箭头
    private TextView tv_name;
    private ImageView iv_name;//名字右箭头

    private TextView tv_focus_num;// 关注数目
    private TextView tv_focus;//关注
    private TextView tv_fans_num;// 粉丝数目
    private TextView tv_fans;//粉丝
    private TextView tv_m_num;// m币数目
    private TextView tv_m;//m

    private TextView tv_bodydata;
    private TextView tv_myclass;
    private TextView tv_mycollection;
    private TextView tv_identify;
    private TextView tv_mall;
    private TextView tv_dailytasks;
    private TextView tv_ordercenter;
    private TextView tv_mydownload;

    private TableRow tr_bindphone;
    private TableRow tr_myadress;
    private TableRow tr_help;
    private TableRow tr_setting;

    private RoundedImageView riv_portrait;
    private int httpcode;

    //从其他页面获取UserID后的个人信息
    private int UserID;
    // User
    private String NickName;
    private String HeadPortaitUrl;
    // UserData
    private int FansNumber;
    private int FocusNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_activity_me_homepage);
        initview();
        GetInformation();
    }

    private void initview() {
        iv_email = findViewById(R.id.iv_email);
        iv_name = findViewById(R.id.iv_name);

        iv_myplace = findViewById(R.id.iv_myplace);
        tv_myplace = findViewById(R.id.tv_myplace);

        tv_name = findViewById(R.id.tv_name);
        iv_name = findViewById(R.id.iv_name);

        tv_focus_num = findViewById(R.id.tv_focus_num);
        tv_focus = findViewById(R.id.tv_focus);
        tv_fans_num = findViewById(R.id.tv_fans_num);
        tv_fans = findViewById(R.id.tv_fans);
        tv_m_num = findViewById(R.id.tv_m_num);
        tv_m = findViewById(R.id.tv_m);

        tv_bodydata = findViewById(R.id.tv_bodydata);
        tv_myclass = findViewById(R.id.tv_myclass);
        tv_mycollection = findViewById(R.id.tv_mycollection);
        tv_identify = findViewById(R.id.tv_identify);
        tv_mall= findViewById(R.id.tv_mall);
        tv_dailytasks = findViewById(R.id.tv_dailytasks);
        tv_ordercenter = findViewById(R.id.tv_ordercenter);
        tv_mydownload = findViewById(R.id.tv_mydownload);

        tr_bindphone = findViewById(R.id.tr_bingphone);
        tr_help = findViewById(R.id.tr_help);
        tr_myadress = findViewById(R.id.tr_myadress);
        tr_setting = findViewById(R.id.tr_setting);

        //监听
        iv_share.setOnClickListener(this);
        iv_email.setOnClickListener(this);

        iv_myplace.setOnClickListener(this);
        tv_myplace.setOnClickListener(this);

        iv_name.setOnClickListener(this);
        tv_name.setOnClickListener(this);

        tv_focus_num.setOnClickListener(this);
        tv_focus.setOnClickListener(this);
        tv_fans_num.setOnClickListener(this);
        tv_fans.setOnClickListener(this);
        tv_m_num.setOnClickListener(this);
        tv_m.setOnClickListener(this);

        tv_bodydata.setOnClickListener(this);
        tv_myclass.setOnClickListener(this);
        tv_mycollection.setOnClickListener(this);
        tv_identify.setOnClickListener(this);
        tv_mall.setOnClickListener(this);
        tv_dailytasks.setOnClickListener(this);
        tv_ordercenter.setOnClickListener(this);
        tv_mydownload.setOnClickListener(this);

        tr_bindphone.setOnClickListener(this);
        tr_myadress.setOnClickListener(this);
        tr_help.setOnClickListener(this);
        tr_setting.setOnClickListener(this);
    }

    public void GetInformation(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String responseData = null;
                try {
                    //设置JSON数据
                    JSONObject json = new JSONObject();
                    try {
                        json.put("UserID", UserID);//POST的 UserID
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String url = "";//
                    //String url = "http://159.75.2.94:8080/api/user/getCode";
                    responseData = connectHttp(url,json);
                    getfeedback(responseData);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(httpcode==200){
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        JSONObject jsonObject1 = jsonObject.getJSONObject("UserData");
                        FansNumber = jsonObject1.getInt("FansNumber");
                        FocusNumber = jsonObject1.getInt("FocusNumber");
                        tv_fans_num.setText(FansNumber);
                        tv_focus_num.setText(FocusNumber);

                        JSONObject jsonObject2 = jsonObject.getJSONObject("User");
                        NickName = jsonObject2.getString("NickName");
                        HeadPortaitUrl = jsonObject2.getString("HeadPortaitUrl");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            //分享/email，未定
            case R.id.iv_share:
                break;
            case R.id.iv_email:
                break;

              // 关注 粉丝 m
            case R.id.tv_focus:
//                Intent intent = new Intent();// 社区.首页.日常动态详情.个人主页.关注
//                startActivity(intent);
                break;
            case R.id.tv_focus_num:
                //和focus一样
                break;
            case R.id.tv_fans:
                //Intent intent = new Intent();// 社区.首页.日常动态详情.个人主页.粉丝
                //startActivity(intent);
                break;
            case R.id.tv_fans_num:
                // 和fans一样
                break;
            case R.id.tv_m:
                //Intent intent = new Intent();// M币.积分商城主页
                //startActivity(intent);
                break;
            case R.id.tv_m_num:
                // HTTPGET 获得数字------------------------------------------------------------------
                break;

             //我的空间 / 我的名字
            case R.id.iv_myplace:
                //Intent intent = new Intent();//社区.首页.日常动态详情.个人主页
                //startActivity(intent);
                break;
            case R.id.tv_myplace:
                //Intent intent = new Intent();//社区.首页.日常动态详情.个人主页
                //startActivity(intent);
                break;
            case R.id.iv_name:
                //HTTPGET 根据token获得名字，需要在bindphone使用-------------------------------------GetInformation里面寻
                break;
            case R.id.tv_name:
                //Intent intent = new Intent();//我的.我.编辑个人资料页面
                //startActivity(intent);
                break;

            //身体数据..
            case R.id.tv_bodydata:
                //Intent intent = new Intent();//我的.身体数据主页
                //startActivity(intent);
                break;
            case R.id.tv_myclass:
                //Intent intent = new Intent();//我的.我.我的课程
                //startActivity(intent);
                break;
            case R.id.tv_mycollection:
                //Intent intent = new Intent();//我的.我.我的收藏
                //startActivity(intent);
                break;
            case R.id.tv_identify:
                //Intent intent = new Intent();//我的.我.我的认证主页
                //startActivity(intent);
                break;
            case R.id.tv_mall:
                //Intent intent = new Intent();//M币.积分商城主页
                //startActivity(intent);
                break;
            case R.id.tv_dailytasks:
                //Intent intent = new Intent();//M币.每日任务
                //startActivity(intent);
                break;
            case R.id.tv_ordercenter:
                //Intent intent = new Intent();//M币.订单中心主页
                //startActivity(intent);
                break;
            case R.id.tv_mydownload:
                //Intent intent = new Intent();//我的.我.我的下载
                //startActivity(intent);
                break;

            //绑定手机号
            case R.id.tr_bingphone:
                Intent intent = new Intent(this,me_activity_bindphone_usephone.class);//我的.我.绑定手机号主页
                startActivity(intent);
                break;
            case R.id.tr_myadress:
                //Intent intent = new Intent();// M币.管理收货地址主页
                //startActivity(intent);
                break;
            case R.id.tr_help:
                //Intent intent = new Intent();//我的.我.帮助与反馈
                //startActivity(intent);
                break;
            case R.id.tr_setting:
                //Intent intent = new Intent();//我的.我.设置
                //startActivity(intent);
                break;
        }
    }
}
// 很多跳转暂时没页面