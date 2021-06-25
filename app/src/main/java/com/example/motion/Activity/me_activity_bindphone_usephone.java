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

public class me_activity_bindphone_usephone extends AppCompatActivity implements View.OnClickListener {

    private Button btn_changephone;
    private ImageView iv_back;
    private TextView tv_phone_number;

    SharedPreferences saveSP;
    private int UserID;
    private String token;
    private String phoneNumber;

    private int httpcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_activity_bindphone_usephone);
        initdata();
        initview();
//        initPhone_number();
    }

    private void initdata() {
        saveSP=this.getSharedPreferences("saveSp",MODE_PRIVATE);
        //判断token是否存在，获得phoneNumber
        token = saveSP.getString("token","");
        if (token.isEmpty()){
            finish();
            Toast.makeText(this,"请登录", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, register_activity_register.class);
            startActivity(intent);
        }else {
            phoneNumber = saveSP.getString("phoneNumber","");
        }

    }

    private void initview(){
        btn_changephone = findViewById(R.id.btn_changephone);
        iv_back = findViewById(R.id.iv_back);
        tv_phone_number = findViewById(R.id.tv_your_phone);

        btn_changephone.setOnClickListener(this);

        if (phoneNumber!=null){
            tv_phone_number.setText(phoneNumber);
        }
        else {
            tv_phone_number.setText(null);
        }
    }






    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                Intent intent1 = new Intent(this, me_fragment_main.class);
                Intent intentText = new Intent(this, register_activity_register.class);
                startActivity(intent1);
                break;
            case R.id.btn_changephone:
                Intent intent2 = new Intent(this,me_activity_bindphone_changephone.class);
                startActivity(intent2);
                break;
//          case R.id.tv_your_phone:
//
//                break;
        }
    }
}
