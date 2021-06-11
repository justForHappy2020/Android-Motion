package com.example.motion.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

public class me_activity_bodydata_main  extends Activity implements View.OnClickListener {
    private Intent intent;
    private ImageView ivBack;
    private Button btAddRecord;
    private RecyclerView rvPortrait;
    private RecyclerView rvRecord;

    private LineChartView lcv_chart;//折线图视图
    private LineChartData data;//折线图对应的属性
    private boolean hasAxes = true;       //是否有轴，x和y轴
    private boolean hasAxesNames = true;   //是否有轴的名字
    private boolean hasLines = true;       //是否有线（点和点连接的线）
    private boolean hasPoints = true;       //是否有点（每个值的点）
    private ValueShape shape = ValueShape.CIRCLE;    //点显示的形式，圆形，正方向，菱形
    private boolean isFilled = false;                //是否是填充
    private boolean hasLabels = false;               //每个点是否有名字
    private boolean isCubic = false;                 //是否是立方的，线条是直线还是弧线
    private boolean hasLabelForSelected = false;       //每个点是否可以选择（点击效果）
    private boolean hasGradientToTransparent = false;      //是否有梯度的透明
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
    private SharedPreferences readSP;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_activity_bodydata_main);
        checkToken();
        initView();
    }

    private void checkToken() {
        readSP=this.getSharedPreferences("saveSp",MODE_PRIVATE);
        token = readSP.getString("token","");
        if (token.isEmpty()){
            finish();
            Toast.makeText(this,"请登录", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, register_activity_register.class);
            startActivity(intent);
        }
    }

    private void initView() {
        mContext = me_activity_bodydata_main.this;
        ivBack = findViewById(R.id.iv_back);
        btAddRecord = findViewById(R.id.btn_add_healthrecord);
        //rvPortrait = findViewById(R.id.rv_portrait);
        rvRecord = findViewById(R.id.rv_health_record);
        lcv_chart = (LineChartView)findViewById(R.id.lcv_chart);
        lcv_chart.setOnValueTouchListener(new LineChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int i, int i1, PointValue pointValue) {
                Toast.makeText(me_activity_bodydata_main.this, "Selected: " + pointValue, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onValueDeselected() {

            }
        });

        ivBack.setOnClickListener(this);
        btAddRecord.setOnClickListener(this);

        //LinearLayoutManager portraitManager = new LinearLayoutManager(this);
        LinearLayoutManager recordManager = new LinearLayoutManager(this);

        //rvPortrait.setLayoutManager(portraitManager);
        rvRecord.setLayoutManager(recordManager);
        rvRecord.setNestedScrollingEnabled(false);

        initData();
        generateChartData();//设置折线图属性、数据
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
                    //alert.show();
                }
                //切换成员、成员头像变大、http获取健康记录、刷新页面
                else {
                    for(int i = 0 ;i<adapter.getItemCount();i++){
                        adapter.getViewByPosition(i,R.id.Riv_portrait_small).setVisibility(View.GONE);
                    }
                    adapter.getViewByPosition(position,R.id.Riv_portrait_small).setVisibility(View.VISIBLE);
                    recordList.clear();
                    HealthRecord record4 = new HealthRecord("2020-05-08",66,180,21,"https://i0.hdslb.com/bfs/album/e105c18f19ded11de27d2325b24cf2d943ffebd4.png");
                    HealthRecord record5 = new HealthRecord("2020-05-08",55,165,22,"https://i0.hdslb.com/bfs/album/e0f389ff0674ba458fa3774afa7aad8c11a1216e.png");
                    HealthRecord record6 = new HealthRecord("2020-05-08",48,145,18,"https://i0.hdslb.com/bfs/album/cd6463e36109ef6dab8b61663a6749bd07d265f6.png");
                    recordList.add(new DyxItem(DyxItem.HEALTHRECORD , record4));
                    recordList.add(new DyxItem(DyxItem.HEALTHRECORD , record5));
                    recordList.add(new DyxItem(DyxItem.HEALTHRECORD , record6));
                    recordAdater.notifyDataSetChanged();
                }
            }
        });
        //rvPortrait.setAdapter(portraitAdapter);
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

        HealthRecord record1 = new HealthRecord("2020-05-09",68,174,22,"https://i0.hdslb.com/bfs/album/e105c18f19ded11de27d2325b24cf2d943ffebd4.png");
        HealthRecord record2 = new HealthRecord("2020-05-09",50,160,20,"https://i0.hdslb.com/bfs/album/cd6463e36109ef6dab8b61663a6749bd07d265f6.png");
        HealthRecord record3 = new HealthRecord("2020-05-09",55,164,30,"https://i0.hdslb.com/bfs/album/e0f389ff0674ba458fa3774afa7aad8c11a1216e.png");
        recordList.add(new DyxItem(DyxItem.HEALTHRECORD , record1));
        recordList.add(new DyxItem(DyxItem.HEALTHRECORD , record2));
        recordList.add(new DyxItem(DyxItem.HEALTHRECORD , record3));
    }

    private void generateChartData() {
        List<Line> lines = new ArrayList<Line>();
        List<PointValue> values = new ArrayList<PointValue>();
        for(int i = 0;i < recordList.size();i++){
            values.add(new PointValue(i,recordList.get(i).getHealthRecord().getWeight()));
        }
        Line line = new Line(values);
        line.setColor(ChartUtils.COLORS[0]);
        line.setShape(shape);
        line.setCubic(isCubic);
        line.setFilled(isFilled);
        line.setHasLabels(hasLabels);
        line.setHasLabelsOnlyForSelected(hasLabelForSelected);
        line.setHasLines(hasLines);
        line.setHasPoints(hasPoints);
        lines.add(line);

        data = new LineChartData(lines);

        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setTextColor(Color.BLACK);//设置x轴字体的颜色
                axisY.setTextColor(Color.BLACK);//设置y轴字体的颜色
                //axisX.setName("Axis X");
                axisY.setName("体重");
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        data.setBaseValue(Float.NEGATIVE_INFINITY);
        lcv_chart.setLineChartData(data);
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
