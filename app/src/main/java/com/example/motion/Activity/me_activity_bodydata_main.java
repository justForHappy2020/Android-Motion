package com.example.motion.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.motion.Entity.DyxItem;
import com.example.motion.Entity.Member;
import com.example.motion.R;
import com.example.motion.Widget.DyxQuickAdapter;

import java.util.ArrayList;
import java.util.List;

public class me_activity_bodydata_main  extends Activity implements View.OnClickListener {
    private Intent intent;
    private ImageView ivBack;
    private Button btAddRecord;
    private RecyclerView rvPortrait;
    private ListView lvName;
    private RecyclerView rvRecord;

    private Context mContext;
    private List<DyxItem> memberList = new ArrayList();
    private List<DyxItem> recordList = new ArrayList();
    private List<List> dataSet = new  ArrayList<>();
    private int httpcode;
    private DyxQuickAdapter portraitAdapter;
    private DyxQuickAdapter recordAdater;

    private int memberID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_activity_bodydata_main);
        initData();
        initView();
    }

    private void initView() {
        mContext = me_activity_bodydata_main.this;
        ivBack = findViewById(R.id.iv_back);
        btAddRecord = findViewById(R.id.btn_add_healthrecord);
        rvPortrait = findViewById(R.id.rv_portrait);
        lvName = findViewById(R.id.lv_name);
        rvRecord = findViewById(R.id.rv_health_record);

        ivBack.setOnClickListener(this);
        btAddRecord.setOnClickListener(this);

        LinearLayoutManager portraitManager = new LinearLayoutManager(this);
        LinearLayoutManager recordManager = new LinearLayoutManager(this);
        portraitManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        rvPortrait.setLayoutManager(portraitManager);
        rvRecord.setLayoutManager(recordManager);

        initData();

        portraitAdapter = new DyxQuickAdapter(memberList);
        rvPortrait.setAdapter(portraitAdapter);
        recordAdater = new DyxQuickAdapter(recordList);
        rvRecord.setAdapter(recordAdater);
    }

    private void initData() {
        ////http请求数据实体，存入2个list中。
        Member member1 = new Member((long) 1,"mom","woman","http://bpic.588ku.com/element_pic/18/05/04/a4605af6e0f30bad35d0556f71b8e44c.jpg","1975-04-09");
        Member member2 = new Member((long) 2,"kid","man","http://5b0988e595225.cdn.sohucs.com/images/20170819/eaf8683041844976b3a45b9325628a5a.jpeg","2010-04-09");
        memberList.add(new DyxItem(DyxItem.PORTRAIT , member1));
        memberList.add(new DyxItem(DyxItem.PORTRAIT , member2));


    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_add_healthrecord:
                intent = new Intent(this,me_activity_bodydata_addfile.class);
                //传成员ID
                memberID = 1;//test
                intent.putExtra("member",memberID);
                startActivity(intent);
                break;
        }
    }
}
