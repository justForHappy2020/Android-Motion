package com.example.motion.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.motion.R;

public class me_activity_bodydata_addfile extends Activity implements View.OnClickListener {
    private ImageView ivBack;
    private EditText etWeight;
    private EditText etHeight;
    private TextView tvFinish;

    private Intent intentAccept;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_activity_bodydata_addfile);
        initView();
    }

    private void initView() {
        intentAccept = getIntent();

        ivBack = findViewById(R.id.iv_back);
        etWeight = findViewById(R.id.et_weight);
        etHeight = findViewById(R.id.et_height);
        tvFinish = findViewById(R.id.tv_finish);

        ivBack.setOnClickListener(this);
        tvFinish.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_finish:
                int memberID = intentAccept.getIntExtra("member",0);
                String weight = String.valueOf(etWeight.getText());
                String height = String.valueOf(etHeight.getText());
                //http保存信息（成员ID、身高体重）
                finish();
                break;
        }
    }
}
