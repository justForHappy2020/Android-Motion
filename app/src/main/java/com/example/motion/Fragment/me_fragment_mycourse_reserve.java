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
import com.example.motion.Entity.me_mycourse_reserve;
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

public class me_fragment_mycourse_reserve extends BaseNetworkFragment {
    private final int LOAD_COURSES_SUCCESS = 1;
    private final int LOAD_COURSES_FAILED = 3;

    private final int COURSE_NUM_IN_ONE_PAGE = 10;

    private RecyclerView rvCourseReserved;
    private MultipleItemQuickAdapter courseAdapter;

    private List<MultipleItem> showingReserveList;



    private View emptyView;
    private Handler handler;

    private int currentPage = 1;//要分页查询的页面
    private boolean hasNext;

    private AlertDialog.Builder builder;
    private String dialogMessage = "";
    private SharedPreferences readSP;
    private String token;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.me_fragment_mycourse_reserve,container,false);

        initView(view);
        checkToken();
        initHandler();
        builder = new AlertDialog.Builder(getActivity());
        getHttpCourse(new HashMap());
        initData();
        return view;
    }

    public void initView(View view){
        rvCourseReserved = view.findViewById(R.id.meMyCourseReserveRecyclerView);

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
        String url = "http://106.55.25.94:8080/api/user/userHasBooked?size=" + COURSE_NUM_IN_ONE_PAGE;
        if(params.isEmpty()){
            url+="&page=1&token="+token;//真实token
        }else{
            Iterator iter = params.keySet().iterator();
            while (iter.hasNext()) {
                Object key = iter.next();
                Object val = params.get(key);
                url+=("&"+key.toString()+"="+val.toString());
            }
        }
        Log.d("me_fragment_mycourse_reserve","requestUrl:" + url);
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
                        Long courseId =jsonCourseObject.getLong("courseId");
                        String courseName = jsonCourseObject.getString("courseName");
                        String backgroundUrl = jsonCourseObject.getString("backgroundUrl");
                        String targetAge = jsonCourseObject.getString("targetAge");
                        int online = jsonCourseObject.getInt("online");
                        //相应的内容
                        me_mycourse_reserve courseReserved = new me_mycourse_reserve();
                        courseReserved.setCourseId(courseId);
                        courseReserved.setReserveName1(courseName);
                        courseReserved.setImgUrls(backgroundUrl);
                        courseReserved.setReserveName2(targetAge);
                        courseReserved.setIsOnline(online);

                        JSONArray JSONArrayLabels = jsonCourseObject.getJSONArray("labels");
                        String labels = "";
                        for (int j = 0; j < JSONArrayLabels.length(); j++) {
                            labels += (JSONArrayLabels.get(j) + "/");
                        }
                        courseReserved.setLabels(labels);
                        onePageCourses.add(new MultipleItem(MultipleItem.Me_mycourse_reserve, courseReserved));
                    }
                    showingReserveList.addAll(onePageCourses);

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

        rvCourseReserved.setLayoutManager(new LinearLayoutManager(getActivity()));
        //courseAdapter = new MultipleItemQuickAdapter(showingReserveList);
        //rvCourseReserved.setAdapter(courseAdapter);

        showingReserveList = new ArrayList<>();
        courseAdapter = new MultipleItemQuickAdapter(showingReserveList);
        courseAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                try {
                    Intent intent = new Intent(getContext(), sport_activity_course_detail.class);
                    intent.putExtra("courseId",showingReserveList.get(position).getMe_mycourse_reserve().getCourseId());
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        rvCourseReserved.setAdapter(courseAdapter);
        rvCourseReserved.setNestedScrollingEnabled(false);

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