package com.example.motion.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.example.motion.Entity.DyxItem;
import com.example.motion.R;
import com.example.motion.Widget.DyxQuickAdapter;

import java.util.ArrayList;
import java.util.List;

public class sport_activity_course_selection extends Activity implements View.OnClickListener {
    private ImageView ivBack;
    private ImageView ivSearch;
    private RecyclerView rvCoursTypechoose;//1级标签
    private RecyclerView rvCourseShow;
    private RecyclerView rvItembtn;//2级标签
    private Button btnUnselect;
    private Button btnSelect;
    private List<DyxItem> itemList1 = new ArrayList();
    private List<DyxItem> itemList2 = new ArrayList();
    private List<DyxItem> courseList = new ArrayList();
    private List<List> dataSet = new  ArrayList<>();
    private int httpcode;
    private DyxQuickAdapter courseAdapter;
    private DyxQuickAdapter btnAdater1;

    private int TOTAL_PAGES;
    private int currentPage;//要分页查询的页面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sport_activity_courseselection);
        initView();
    }

    private void initView() {
        ivBack = findViewById(R.id.iv_back);
        ivSearch = findViewById(R.id.iv_search);
        rvCoursTypechoose = findViewById(R.id.rv_course_typechoose);
        rvCourseShow = findViewById(R.id.rv_course_show);
        rvItembtn = rvCoursTypechoose.findViewById(R.id.rv_itembtn);
        btnUnselect = findViewById(R.id.btn_unselect);
        btnSelect = findViewById(R.id.btn_select);
        rvCourseShow = findViewById(R.id.rv_course_show);
        rvCoursTypechoose = findViewById(R.id.rv_course_typechoose);

        LinearLayoutManager courseLayoutManage = new LinearLayoutManager(this);
        LinearLayoutManager btnLayoutManage1 = new LinearLayoutManager(this);
        LinearLayoutManager btnLayoutManage2 = new LinearLayoutManager(this);
        btnLayoutManage2.setOrientation(LinearLayoutManager.HORIZONTAL);

        rvCourseShow.setLayoutManager(courseLayoutManage);
        rvCoursTypechoose.setLayoutManager(btnLayoutManage1);
        rvItembtn.setLayoutManager(btnLayoutManage2);

        initData();

        courseAdapter = new DyxQuickAdapter(courseList);
        configLoadMoreCourse();
        courseAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (currentPage > TOTAL_PAGES) {
                    courseAdapter.getLoadMoreModule().loadMoreEnd();
                } else {
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            configLoadMoreCourse();
                        }
                    }, 2000);

                }
            }
        });
        courseAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = null;
                //跳转到相关课程详情页

                startActivity(intent);
            }
        });
        rvCourseShow.setAdapter(courseAdapter);

        btnAdater1 = new DyxQuickAdapter(itemList1);
        rvCoursTypechoose.setAdapter(btnAdater1);

        //DyxQuickAdapter btnAdater2 = new DyxQuickAdapter(itemList2);


        ivBack.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        btnUnselect.setOnClickListener(this);
        btnSelect.setOnClickListener(this);
    }

    private void initData(){
        //http请求拉取标签、课程数据实体，存入3个list中。
        //courseList.add(new DyxItem(DyxItem.SELECTCOURSE ,course);
    }

    //首次以及拉取更多课程数据
    private void configLoadMoreCourse() {
        String url;//http请求的url
        //Http加载更多数据
        dataSet.add(courseList);
        courseAdapter.addData(dataSet.get(currentPage-1));
        currentPage++;
        courseAdapter.getLoadMoreModule().loadMoreEnd();
    }

   /* private void getHttpCourse(final String url) {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String responseData = null;
                try {
                    responseData = HttpUtils.connectHttpGet(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(responseData);
                    httpcode = jsonObject1.getInt("code");
                    if (httpcode == 200) {
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("data");
                        hasNext = jsonObject2.getBoolean("hasNext");
                        TOTAL_PAGES = jsonObject2.getInt("totalPages");
                        //得到筛选的课程list
                        JSONArray JSONArrayCourse = jsonObject2.getJSONArray("courseList");
                        for (int i = 0; i < JSONArrayCourse.length(); i++) {
                            JSONObject jsonObject = JSONArrayCourse.getJSONObject(i);
                            //相应的内容
                            Course course = new Course();
                            course.setCourseId(jsonObject.getLong("courseId"));
                            course.setCourseName(jsonObject.getString("courseName"));
                            course.setBackgroundUrl(jsonObject.getString("backgroundUrl"));
                            //course.setCourseUrl(jsonObject.getString("courseUrl"));
                            course.setBodyPart(jsonObject.getString("bodyPart"));
                            course.setDegree(jsonObject.getString("degree"));
                            course.setDuration(jsonObject.getString("duration"));
                            course.setHits(jsonObject.getInt("hits"));
                            course.setCreateTime(jsonObject.getString("createTime"));
                            course.setCalorie(jsonObject.getInt("calorie"));
                            course.setCourseIntro(jsonObject.getString("courseIntro"));
                            courseList.add(i, course);
                        }
                    }
                } catch (JSONException e) {
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
        if (httpcode != 200) {
            Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_SHORT).show();
        }

    }*/

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_search:
                //跳转到搜索课程页
                break;
            case R.id.btn_unselect:
                //取消所有选择
                break;
            case R.id.btn_select:
                //开始筛选课程
                break;
        }
    }
}
