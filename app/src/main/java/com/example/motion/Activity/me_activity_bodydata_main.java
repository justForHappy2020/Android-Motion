package com.example.motion.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.motion.Entity.CourseTag;
import com.example.motion.Entity.CourseTagGroup;
import com.example.motion.Entity.DyxItem;
import com.example.motion.Entity.HealthRecord;
import com.example.motion.Entity.Member;
import com.example.motion.R;
import com.example.motion.Utils.HttpUtils;
import com.example.motion.Widget.DyxQuickAdapter;
import com.example.motion.Widget.MyStringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

public class me_activity_bodydata_main  extends NeedTokenActivity implements View.OnClickListener {
    private final int LOAD_USER_BODYDATA_FAILED = 0;
    private final int LOAD_USER_BODYDATA_SUCCESS = 1;

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
    private List<DyxItem> recordList = new ArrayList();;
    private List<List> dataSet = new  ArrayList<>();
    private int httpcode;
    private DyxQuickAdapter portraitAdapter;
    private DyxQuickAdapter recordAdater;
    private View bodyDataEmptyView;

    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private View alertView;

    private int memberID;
    private SharedPreferences readSP;
    private String token ;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_activity_bodydata_main);
        initHandler();
        //checkToken();
        initView();
        initData();
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

    private void initHandler(){
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case LOAD_USER_BODYDATA_FAILED:
                        Log.d("me_activity_bodydata_main_Handler","LOAD_USER_BODYDATA_FAILED:"+msg.obj);
                        recordAdater.notifyDataSetChanged();
                        break;
                    case LOAD_USER_BODYDATA_SUCCESS:
                        Log.d("me_activity_bodydata_main","LOAD_USER_BODYDATA_SUCCESS");
                        recordAdater.notifyDataSetChanged();

                        generateChartData();//设置折线图属性、数据
                        break;
                }
            }
        };
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
                    recordAdater.notifyDataSetChanged();
                }
            }
        });
        //rvPortrait.setAdapter(portraitAdapter);


        recordAdater = new DyxQuickAdapter(recordList);
        bodyDataEmptyView = View.inflate(this,R.layout.me_empty_view_bodydata,null);
        recordAdater.setEmptyView(bodyDataEmptyView);
        rvRecord.setAdapter(recordAdater);
    }

    private void initData() {

        Member member1 = new Member((long) 1,"mom","woman","http://bpic.588ku.com/element_pic/18/05/04/a4605af6e0f30bad35d0556f71b8e44c.jpg","1975-04-09");
        Member member2 = new Member((long) 2,"kid","man","http://5b0988e595225.cdn.sohucs.com/images/20170819/eaf8683041844976b3a45b9325628a5a.jpeg","2010-04-09");
        Member addButton = new Member((long) 2,"null","null","https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic.51yuansu.com%2Fpic2%2Fcover%2F00%2F48%2F15%2F5815dc80681ad_610.jpg&refer=http%3A%2F%2Fpic.51yuansu.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1623141929&t=c25a614e346923a7f878b6a43c6d0699","null");

        memberList.add(new DyxItem(DyxItem.PORTRAIT , member1));
        memberList.add(new DyxItem(DyxItem.PORTRAIT , member2));
        memberList.add(new DyxItem(DyxItem.PORTRAIT , addButton));

        //http请求健康记录LIST
        String url = "http://10.34.25.45:8080/api/community/getHealthRecord?token=" + token;
        MyStringRequest getTagsStringRequest = new MyStringRequest(Request.Method.GET,  url, new Response.Listener<String>() {
            @Override
            public void onResponse(String responseStr) {
                try {
                    JSONObject jsonRootObject = new JSONObject(responseStr);

                    JSONArray JSONArrayRecord = jsonRootObject.getJSONArray("data");
                    for (int i = 0; i < JSONArrayRecord.length(); i++) {
                        JSONObject jsonObject2 = JSONArrayRecord.getJSONObject(i);
                        //相应的内容
                        HealthRecord healthRecord = new HealthRecord();
                        healthRecord.setBmi((float) jsonObject2.getDouble("bmi"));
                        healthRecord.setWeight((float) jsonObject2.getDouble("weight"));
                        healthRecord.setHeight((float) jsonObject2.getDouble("height"));
                        healthRecord.setCreateTime(jsonObject2.getString("createTime"));
                        healthRecord.setStatus(jsonObject2.getInt("status"));
                        recordList.add(new DyxItem(DyxItem.HEALTHRECORD , healthRecord));
                    }

                    Message msg = handler.obtainMessage();
                    msg.what = LOAD_USER_BODYDATA_SUCCESS;
                    handler.sendMessage(msg);

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Message msg = handler.obtainMessage();
                msg.what = LOAD_USER_BODYDATA_FAILED;
                msg.obj = volleyError.toString();
                handler.sendMessage(msg);
            }
        });
        getTagsStringRequest.setTag("getHttp");
        requestQueue.add(getTagsStringRequest);

        /*
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://10.34.25.45:8080/api/community/getHealthRecord?token=" + token;
                String responseData = null;
                try {
                    responseData = HttpUtils.connectHttpGet(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try{
                    JSONObject jsonObject1 = null;
                    if(responseData != null)jsonObject1 = new JSONObject(responseData);
                    httpcode = jsonObject1.getInt("code");
                    if (httpcode == 200) {
                        JSONArray JSONArrayRecord = jsonObject1.getJSONArray("data");
                        for (int i = 0; i < JSONArrayRecord.length(); i++) {
                            JSONObject jsonObject2 = JSONArrayRecord.getJSONObject(i);
                            //相应的内容
                            HealthRecord healthRecord = new HealthRecord();
                            healthRecord.setBmi((float) jsonObject2.getDouble("bmi"));
                            healthRecord.setWeight((float) jsonObject2.getDouble("weight"));
                            healthRecord.setHeight((float) jsonObject2.getDouble("height"));
                            healthRecord.setCreateTime(jsonObject2.getString("createTime"));
                            healthRecord.setStatus(jsonObject2.getInt("status"));
                            recordList.add(new DyxItem(DyxItem.HEALTHRECORD , healthRecord));
                        }
                    }
                }catch (JSONException e){
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
        if(httpcode!=200)Toast.makeText(me_activity_bodydata_main.this,"ERROR", Toast.LENGTH_SHORT).show();

         */
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
