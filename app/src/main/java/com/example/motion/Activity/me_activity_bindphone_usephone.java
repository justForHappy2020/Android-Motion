package com.example.motion.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.motion.Fragment.me_fragment_main;
import com.example.motion.R;
import com.example.motion.Utils.UserInfoManager;

public class me_activity_bindphone_usephone extends NeedTokenActivity implements View.OnClickListener {

    private Button btn_changephone;
    private ImageView iv_back;
    private TextView tv_phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_activity_bindphone_usephone);
        initdata();
        initview();
//        initPhone_number();
    }

    private void initdata() {
        tv_phone_number.setText(UserInfoManager.getUserInfoManager(me_activity_bindphone_usephone.this).getUser().getPhoneNumber());
    }

    private void initview(){
        btn_changephone = findViewById(R.id.btn_changephone);
        iv_back = findViewById(R.id.iv_back);
        tv_phone_number = findViewById(R.id.tv_your_phone);

        btn_changephone.setOnClickListener(this);
        iv_back.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_changephone:
                Intent intent2 = new Intent(this,me_activity_bindphone_changephone.class);
                startActivity(intent2);
                break;
        }
    }
}
