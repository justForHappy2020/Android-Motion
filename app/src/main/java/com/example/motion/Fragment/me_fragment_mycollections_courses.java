package com.example.motion.Fragment;

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

import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.example.motion.Entity.MultipleItem;
import com.example.motion.Entity.me_mycourse_collections;
import com.example.motion.R;
import com.example.motion.Utils.HttpUtils;
import com.example.motion.Widget.MultipleItemQuickAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class me_fragment_mycollections_courses extends Fragment {
    private List<List> dataSet = new ArrayList();
    private List<MultipleItem> collectionsList = new ArrayList();
    private int TOTAL_PAGES;
    MultipleItemQuickAdapter quickAdapter;
    RecyclerView recyclerView;

    private int httpcode;
    private int currentPage; //要分页查询的页面
    private Boolean hasNext;
    private String token = "123";
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        currentPage = 1;
        View view = inflater.inflate(R.layout.me_fragment_mycourse_collections,container,false);
        //initData();
        initView(view);
        //configLoadMoreData();
        return view;
    }

    public void initView(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.meMyCollectionsRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        quickAdapter = new MultipleItemQuickAdapter(collectionsList);

        configLoadMoreData();

        quickAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            //int mCurrentCunter = 0;
            @Override
            public void onLoadMore() {
                if (currentPage > TOTAL_PAGES) {
                    quickAdapter.getLoadMoreModule().loadMoreEnd();
                } else {
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            configLoadMoreData();
                        }
                    }, 1000);

                }

            }
        });

        recyclerView.setAdapter(quickAdapter);
    }
//    public void initData(){
//
//        me_mycourse_collections collections;
//        for (int i = 0; i < 5; i++) {
//            collections = new me_mycourse_collections();
//            collectionsList.add(new MultipleItem(12,collections));
//            collections.setImgUrls("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3385472845,2539383542&fm=11&gp=0.jpg");
//            collections.setCollectionsName1("写代码");
//            collections.setCollectionsName2("数据结构");
//            collections.setCollectionsName3("学习");
//        }
//        dataSet.add(collectionsList);
//    }

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
                        TOTAL_PAGES = jsonObject2.getInt("pageSize");
                        //得到筛选的课程list
                        JSONArray JSONArrayCollections = jsonObject2.getJSONArray("collectionsList");
                        for (int i = 0; i < JSONArrayCollections.length(); i++) {
                            JSONObject jsonObject = JSONArrayCollections.getJSONObject(i);
                            //相应的内容
                            me_mycourse_collections collections = new me_mycourse_collections();
                            collections.setCollectionsName1(jsonObject.getString("courseName"));
                            collections.setCollectionsName2(jsonObject.getString("targetAge"));
                            collections.setCollectionsName3(jsonObject.getString("labels"));
                            collectionsList.add(new MultipleItem(12,collections));
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
/*        else for (int i = 0; i <courseList.size(); i++)btCourse[i].setText(courseList.get(i).getCourseName() + "\n" + courseList.get(i).getDegree() + " . " +
                courseList.get(i).getDuration() + " . " +courseList.get(i).getHits() + "万人已参加");//展示课程*/
    }

    private void configLoadMoreData() {
        String url;//http请求的url
        url = "http://10.34.25.45:8080/api/user/userHasCollected?size="+1+"&page="+1+"&token="+token;
        getHttpSearch(url);
        dataSet.add(collectionsList);
        quickAdapter.addData(dataSet.get(currentPage-1));
        currentPage++;
        quickAdapter.getLoadMoreModule().loadMoreEnd();
    }
}
