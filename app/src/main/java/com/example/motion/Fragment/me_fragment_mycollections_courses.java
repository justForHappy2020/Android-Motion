package com.example.motion.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.example.motion.Activity.register_activity_register;
import com.example.motion.Activity.sport_activity_course_detail;
import com.example.motion.Entity.MultipleItem;
import com.example.motion.Entity.me_mycourse_collections;
import com.example.motion.R;
import com.example.motion.Widget.MultipleItemQuickAdapter;
import com.example.motion.VolleyRequest.MyStringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class me_fragment_mycollections_courses extends BaseNetworkFragment {
    //    private List<List> dataSet = new ArrayList();
//    private List<MultipleItem> collectionsList = new ArrayList();
//
//    MultipleItemQuickAdapter quickAdapter;
//    RecyclerView recyclerView;
//
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.me_fragment_mycourse_collections,container,false);
//        initData();
//        initView(view);
//        return view;
//    }
//
//    public void initView(View view){
//        recyclerView = (RecyclerView) view.findViewById(R.id.meMyCollectionsRecyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        quickAdapter = new MultipleItemQuickAdapter(collectionsList);
//        recyclerView.setAdapter(quickAdapter);
//    }
//    public void initData(){
//
//        me_mycourse_collections collections;
//        for (int i = 0; i < 5; i++) {
//            collections = new me_mycourse_collections();
//
//            collections.setImgUrls("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3385472845,2539383542&fm=11&gp=0.jpg");
//            collections.setCollectionsName1("写代码");
//            collections.setCollectionsName2("数据结构");
//            collections.setCollectionsName3("学习");
//            collectionsList.add(new MultipleItem(MultipleItem.Me_mycourse_collections,collections));
//        }
//        dataSet.add(collectionsList);
//    }
//}
    private final int LOAD_COURSES_SUCCESS = 1;
    private final int LOAD_COURSES_FAILED = 3;

    private final int COURSE_NUM_IN_ONE_PAGE = 10;

    private RecyclerView rvCourseCollected;
    private MultipleItemQuickAdapter courseAdapter;

    private List<MultipleItem> showingCourseList;



    private View emptyView;
    private Handler handler;

    private int currentPage = 1;//要分页查询的页面
    private boolean hasNext;

    private AlertDialog.Builder builder;
    private String dialogMessage = "";

    private SharedPreferences readSP;
    private String token ;
    private String testToken = "aa650cbc-d18a-42fd-926b-98cf1327e2b3";
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.me_fragment_mycourse_collections,container,false);

        initView(view);
        checkToken();
        initHandler();
        builder = new AlertDialog.Builder(getActivity());
        getHttpCourse(new HashMap());
        initData();
        return view;
    }

    public void initView(View view){
        rvCourseCollected = view.findViewById(R.id.meMyCollectionsRecyclerView);

    }
    private void checkToken() {
        readSP=getActivity().getSharedPreferences("saveSp",MODE_PRIVATE);
        token = readSP.getString("token","");
        if (token.isEmpty()){
            Toast.makeText(getActivity(),"请登录", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), register_activity_register.class);
            startActivity(intent);
        }
    }
    private void initHandler(){
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case LOAD_COURSES_SUCCESS:
                        Log.d("HANDLER","LOAD_COURSES_SUCCESS");
                        courseAdapter.notifyDataSetChanged();
                        courseAdapter.getLoadMoreModule().loadMoreComplete();

                        Toast.makeText(getActivity(), "请求成功", Toast.LENGTH_SHORT).show();
                        break;
                    case LOAD_COURSES_FAILED:
                        Log.d("HANDLER","LOAD_COURSES_FAILED");
                        Toast.makeText(getActivity(), "LOAD_COURSES_FAILED,"+msg.obj, Toast.LENGTH_LONG).show();

                }
            }
        };
    }
    private void getHttpCourse(Map params){
        List<MultipleItem> onePageCourses = new ArrayList<>();

        String url = "http://106.55.25.94:8080/api/course/getCollectionCourse?size=" + COURSE_NUM_IN_ONE_PAGE;
        if(params.isEmpty()){
//            url+="&page=1&token="+token;//真实token
            url+="&page=1&token="+testToken;//测试token9
        }else{
            Iterator iter = params.keySet().iterator();
            while (iter.hasNext()) {
                Object key = iter.next();
                Object val = params.get(key);
                url+=("&"+key.toString()+"="+val.toString());
            }
        }
        Log.d("me_fragment_mycourse_collections","requestUrl:" + url);
        dialogMessage += "\n\ngetHttpCourse requestingUrl:\n" + url;

        MyStringRequest stringRequest = new MyStringRequest(Request.Method.GET,  url, new Response.Listener<String>() {
            @Override
            public void onResponse(String responseStr) {

                try {
                    JSONObject jsonRootObject = new JSONObject(responseStr);

                    //Log.d("sport_activity_course_selection","getHttpCourse_responseStr:" + jsonRootObject.toString());

                    JSONObject jsonObject2 = jsonRootObject.getJSONObject("data");
                    hasNext = jsonObject2.getBoolean("hasNext");
                    //TOTAL_PAGES = jsonObject2.getInt("totalPages");
                    //得到筛选的课程list
                    JSONArray JSONArrayCourse = jsonObject2.getJSONArray("courseList");
                    for (int i = 0; i < JSONArrayCourse.length(); i++) {
                        JSONObject jsonCourseObject = JSONArrayCourse.getJSONObject(i);
                        //相应的内容
                        me_mycourse_collections courseCollected = new me_mycourse_collections();
                        courseCollected.setCourseId(jsonCourseObject.getLong("courseId"));
                        courseCollected.setCollectionsName1(jsonCourseObject.getString("courseName"));
                        courseCollected.setImgUrls(jsonCourseObject.getString("backgroundUrl"));
                        courseCollected.setCollectionsName2(jsonCourseObject.getString("targetAge"));
                        courseCollected.setIsOnline(jsonCourseObject.getInt("online"));

                        JSONArray JSONArrayLabels = jsonCourseObject.getJSONArray("labels");
                        String labels = "";
                        for (int j = 0; j < JSONArrayLabels.length(); j++) {
                            labels += (JSONArrayLabels.get(j) + "/");
                        }
                        courseCollected.setLabels(labels);
                        onePageCourses.add(new MultipleItem(MultipleItem.Me_mycourse_collections, courseCollected));
                    }
                    showingCourseList.addAll(onePageCourses);

                    Log.d("me_fragment_mycourse_collections","getHttpCourse_responseStr:"+responseStr);
                    Message msg = handler.obtainMessage();
                    msg.what = LOAD_COURSES_SUCCESS;
                    handler.sendMessage(msg);
                    //test tool
                    dialogMessage+="\n\ngetHttpCourse response:\n"+jsonRootObject.toString();
                    //end of test tool
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("sport_activity_course_selection","getHttpCourse_onErrorResponse");

                //test tool
                dialogMessage+="\ngetHttpCourse error: "+volleyError.toString();
                //end of test tool

                Message msg = handler.obtainMessage();
                msg.what = LOAD_COURSES_FAILED;
                msg.obj = volleyError.toString();
                handler.sendMessage(msg);

            }
        });
        stringRequest.setTag("getHttp");
        requestQueue.add(stringRequest);
    }
    public void initData(){

        rvCourseCollected.setLayoutManager(new LinearLayoutManager(getActivity()));
        courseAdapter = new MultipleItemQuickAdapter(showingCourseList);
        rvCourseCollected.setAdapter(courseAdapter);

        showingCourseList = new ArrayList<>();
        courseAdapter = new MultipleItemQuickAdapter(showingCourseList);
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
                intent.putExtra("course", showingCourseList.get(position).getCourse());
                startActivity(intent);
            }
        });
        rvCourseCollected.setAdapter(courseAdapter);
        rvCourseCollected.setNestedScrollingEnabled(false);

        emptyView = View.inflate(getActivity(),R.layout.me_fragment_mycollections_courses,null);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Retrying", Toast.LENGTH_SHORT).show();
                getHttpCourse(new HashMap());
            }
        });
        courseAdapter.setEmptyView(emptyView);
        courseAdapter.setAnimationEnable(true);
        courseAdapter.getLoadMoreModule().setAutoLoadMore(false);
        courseAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                configLoadMoreCourse();
            }
        });
    }
    private void configLoadMoreCourse() {
        if(hasNext){
            Log.d("me_fragment_mycourse_collections","configLoadMoreCourse");
        }else{
            courseAdapter.getLoadMoreModule().loadMoreEnd();
        }
    }
}