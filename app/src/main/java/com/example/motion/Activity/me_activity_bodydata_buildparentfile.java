package com.example.motion.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.motion.R;

import java.util.Calendar;

public class me_activity_bodydata_buildparentfile  extends Activity implements View.OnClickListener {

    private ImageView ivBack;
    private EditText etParentsName;
    private TextView tvParentsBrith;
    private TextView tvSexName;
    private TextView tvFinish;

    private Context mContext;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private String birth = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_activity_bodydata_buildparentfile);
        initView();
    }

    private void initView() {
        mContext = me_activity_bodydata_buildparentfile.this;
        ivBack = findViewById(R.id.iv_back);
        etParentsName = findViewById(R.id.et_parents_name);
        tvParentsBrith = findViewById(R.id.tv_parents_brith);
        tvSexName = findViewById(R.id.tv_sex_name);
        tvFinish = findViewById(R.id.tv_finish);

        ivBack.setOnClickListener(this);
        tvParentsBrith.setOnClickListener(this);
        tvSexName.setOnClickListener(this);
        tvFinish.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_parents_brith:
                birth = "";
                Calendar calendar = Calendar.getInstance();
                new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        birth = year+"-"+ (month+1)+"-"+dayOfMonth;
                        tvParentsBrith.setText(birth);
                    }
                }
                        ,calendar.get(Calendar.YEAR)
                        ,calendar.get(Calendar.MONTH)
                        ,calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.tv_sex_name:
                final String[] sex = getResources().getStringArray(R.array.me_choose_sex);
                alert = null;
                builder = new AlertDialog.Builder(mContext);
                alert = builder.setTitle(getResources().getString(R.string.me_choose_sex1))
                        .setSingleChoiceItems(sex, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                tvSexName.setText(sex[which]);
                            }
                        }).create();
                alert.show();
                alert.setCanceledOnTouchOutside(true);
                break;
            case R.id.tv_finish:
                String parentName = String.valueOf(etParentsName.getText());
                //http保存信息
                break;
        }
    }
}
