package com.example.motion.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.motion.Entity.DyxItem;
import com.example.motion.Entity.HealthRecord;
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

    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private View alertView;

    private int memberID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_activity_bodydata_main);
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
        builder = new AlertDialog.Builder(mContext);
        final LayoutInflater inflater = me_activity_bodydata_main.this.getLayoutInflater();
        alertView = inflater.inflate(R.layout.module_dialog_0420, null,false);
        builder.setView(alertView);
        alert = builder.create();
        Window window = alert.getWindow();
        window.setGravity(Gravity.BOTTOM);
        alertView.findViewById(R.id.Ibtn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        alertView.findViewById(R.id.Riv_MyBabyPortrait).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                Intent intent = new Intent(getApplicationContext(), me_activity_bodydata_buildbabyfile.class);
                startActivity(intent);
            }
        });

        alertView.findViewById(R.id.Riv_MyParentPortrait).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                Intent intent = new Intent(getApplicationContext(), me_activity_bodydata_buildparentfile.class);
                startActivity(intent);
            }
        });

        portraitAdapter = new DyxQuickAdapter(memberList);
        portraitAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                //最后一个item为"添加成员"
                if(memberList.get(position).getMember().getMemberName().equals("null")){
                    alert.show();
                }
                //切换成员、成员头像变大、http获取健康记录、刷新页面
                else {
                    for(int i = 0 ;i<adapter.getItemCount();i++){
                        adapter.getViewByPosition(i,R.id.Riv_portrait_small).setVisibility(View.GONE);
                    }
                    adapter.getViewByPosition(position,R.id.Riv_portrait_small).setVisibility(View.VISIBLE);
                    recordList.clear();
                    HealthRecord record4 = new HealthRecord((long)2,"2020-05-08",66,180,21,"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201607%2F28%2F20160728100434_nXMmc.thumb.400_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1623330327&t=1767a207561829362d49fb5362f270af");
                    HealthRecord record5 = new HealthRecord((long)2,"2020-05-08",55,165,22,"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201607%2F28%2F20160728100434_nXMmc.thumb.400_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1623330327&t=1767a207561829362d49fb5362f270af");
                    HealthRecord record6 = new HealthRecord((long)2,"2020-05-08",48,145,18,"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201607%2F28%2F20160728100434_nXMmc.thumb.400_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1623330327&t=1767a207561829362d49fb5362f270af");
                    recordList.add(new DyxItem(DyxItem.HEALTHRECORD , record4));
                    recordList.add(new DyxItem(DyxItem.HEALTHRECORD , record5));
                    recordList.add(new DyxItem(DyxItem.HEALTHRECORD , record6));
                    recordAdater.notifyDataSetChanged();
                }
            }
        });
        rvPortrait.setAdapter(portraitAdapter);
        recordAdater = new DyxQuickAdapter(recordList);
        rvRecord.setAdapter(recordAdater);
    }

    private void initData() {
        ////http请求数据实体，存入2个list中。
        Member member1 = new Member((long) 1,"mom","woman","http://bpic.588ku.com/element_pic/18/05/04/a4605af6e0f30bad35d0556f71b8e44c.jpg","1975-04-09");
        Member member2 = new Member((long) 2,"kid","man","http://5b0988e595225.cdn.sohucs.com/images/20170819/eaf8683041844976b3a45b9325628a5a.jpeg","2010-04-09");
        Member addButton = new Member((long) 2,"null","null","https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic.51yuansu.com%2Fpic2%2Fcover%2F00%2F48%2F15%2F5815dc80681ad_610.jpg&refer=http%3A%2F%2Fpic.51yuansu.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1623141929&t=c25a614e346923a7f878b6a43c6d0699","null");

        memberList.add(new DyxItem(DyxItem.PORTRAIT , member1));
        memberList.add(new DyxItem(DyxItem.PORTRAIT , member2));
        memberList.add(new DyxItem(DyxItem.PORTRAIT , addButton));

        HealthRecord record1 = new HealthRecord((long)1,"2020-05-09",68,174,22,"https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2374733373,2441380433&fm=26&gp=0.jpg");
        HealthRecord record2 = new HealthRecord((long)1,"2020-05-09",50,160,20,"https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2374733373,2441380433&fm=26&gp=0.jpg");
        HealthRecord record3 = new HealthRecord((long)1,"2020-05-09",55,164,622,"https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2374733373,2441380433&fm=26&gp=0.jpg");
        recordList.add(new DyxItem(DyxItem.HEALTHRECORD , record1));
        recordList.add(new DyxItem(DyxItem.HEALTHRECORD , record2));
        recordList.add(new DyxItem(DyxItem.HEALTHRECORD , record3));
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
