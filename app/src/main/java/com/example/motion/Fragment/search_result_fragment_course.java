package com.example.motion.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.example.motion.Activity.sport_activity_course_detail;

import com.example.motion.Entity.Course;
import com.example.motion.Entity.DyxItem;
import com.example.motion.Entity.MultipleItem;
import com.example.motion.R;
import com.example.motion.Utils.HttpUtils;
import com.example.motion.Widget.DyxQuickAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import com.example.motion.Widget.DyxQuickAdapter;

import java.util.ArrayList;
import java.util.List;

public class search_result_fragment_course extends BaseNetworkFragment implements View.OnClickListener {
    private RecyclerView rvSearchCourse;
    String searchContent;
    private DyxQuickAdapter courseAdapter;
    private List<List> dataSet = new  ArrayList<>();
    private List<DyxItem> courseList = new ArrayList();
    private int httpcode;
    private Boolean hasNext;
    private int TOTAL_PAGES;
    private int currentPage; //要分页查询的页面
    private int size;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.search_fragment_course, container, false);

        currentPage = 1;
        size=5;
        TOTAL_PAGES = 5;
        Bundle bundle = getArguments();
        searchContent = bundle.getString("searchContent");
        initView(view);
        //initData();
        return view;
    }
    public void initView(View view){
        rvSearchCourse=view.findViewById(R.id.rv_search_course_show);
        rvSearchCourse.setLayoutManager(new LinearLayoutManager(getActivity()));
        courseAdapter = new DyxQuickAdapter(courseList);

        configLoadMoreData();

        courseAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            //int mCurrentCunter = 0;

            @Override
            public void onLoadMore() {
                if (currentPage > TOTAL_PAGES) {
                    courseAdapter.getLoadMoreModule().loadMoreEnd();
                } else {
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            configLoadMoreData();
                        }
                    }, 2000);

                }

            }
        });

        courseAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter quickAdapter, @NonNull View view, int position) {
                //Log.d("Adapter","Click");
                Intent intent;
                intent = new Intent(getActivity(), sport_activity_course_detail.class);
/*                for(int i = 0 ; i < dataSet.size() ; i++){
                    courseList = dataSet.get(i);
                    if(courseList.size()<=position)position = position - courseList.size();
                    else break;
                }*/
                intent.putExtra("course", courseList.get(position).getCourse());
                startActivity(intent);
            }
        });

        rvSearchCourse.setAdapter(courseAdapter);
    }
    //    private void initData(){
//        Course course;
//        for (int i = 0; i < 5; i++) {
//            course = new Course();
//
//            course.setBackgroundUrl("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3385472845,2539383542&fm=11&gp=0.jpg");
//            course.setCourseName("篮球培优课（适合4-6岁孩子练习）");
//            course.setCourseIntro("亲子A类/网球/坐位体前屈");
//            course.setIsOnline(0);
//            courseList.add(new DyxItem(DyxItem.SELECTCOURSE,course));
//        }
//
//    }
    @Override
    public void onClick(View v) {

    }
    private void getHttpSearch(final String url) {
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
                        //TOTAL_PAGES = jsonObject2.getInt("totalPages");
                        //得到筛选的课程list
                        JSONArray JSONArrayCourse = jsonObject2.getJSONArray("courseList");
                        for (int i = 0; i < JSONArrayCourse.length(); i++) {
                            JSONObject jsonObject = JSONArrayCourse.getJSONObject(i);
                            //相应的内容
                            Course course = new Course();
                            course.setCourseName(jsonObject.getString("courseName"));
                            course.setBackgroundUrl(jsonObject.getString("backgroundUrl"));
                            //course.setCourseUrl(jsonObject.getString("courseUrl"));
                            //course.setDuration(jsonObject.getString("duration"));
                            //course.setCreateTime(jsonObject.getString("createTime"));
                            course.setCourseIntro(jsonObject.getString("courseIntro"));
                            course.setIsOnline(jsonObject.getInt("online"));
                            courseList.add(new DyxItem(DyxItem.SELECTCOURSE,course));
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

    }

    private void configLoadMoreData() {
        String url;//http请求的url
        url = "http://106.55.25.94:8080/api/course/getCourseByName?page=" + currentPage+ "&size=" + size+"&courseName=" + searchContent;//(e.g.搜索"腹肌")
        getHttpSearch(url);
        dataSet.add(courseList);
        courseAdapter.addData(dataSet.get(currentPage-1));
        currentPage++;
        courseAdapter.getLoadMoreModule().loadMoreEnd();
        rvSearchCourse.setAdapter(courseAdapter);
    }
    private void initData(){
        Course course;
        for (int i = 0; i < 5; i++) {
            course = new Course();

            course.setBackgroundUrl("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3385472845,2539383542&fm=11&gp=0.jpg");
            course.setCourseName("篮球培优课（适合4-6岁孩子练习）");
            course.setCourseIntro("亲子A类/网球/坐位体前屈");
            course.setIsOnline(0);
            courseList.add(new DyxItem(DyxItem.SELECTCOURSE,course));
        }

    }

}
