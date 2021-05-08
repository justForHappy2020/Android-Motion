package com.example.motion.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.example.motion.Entity.Course;
import com.example.motion.Entity.CourseTag;
import com.example.motion.Entity.CourseTagGroup;
import com.example.motion.Entity.MultipleItem;
import com.example.motion.R;
import com.example.motion.Utils.HttpUtils;
import com.example.motion.Widget.MultipleItemQuickAdapter;
import com.example.motion.Widget.MyStringRequest;
import com.example.motion.Widget.SelectionTagsAdapter;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class sport_activity_course_selection extends BaseNetworkActivity implements View.OnClickListener {
    private final int LOAD_COURSES_SUCCESS = 1;
    private final int LOAD_TAGS_SUCCESS = 2;
    private final int LOAD_COURSES_FAILED = 3;
    private final int LOAD_TAGS_FAILED = 4;

    private final int COURSE_NUM_IN_ONE_PAGE = 10;
    private ImageView ivBack;
    private ImageView ivSearch;
    private RecyclerView rvCourseTags;//1级标签
    private RecyclerView rvCourseShow;
    private Button btnUnselect;
    private Button btnSelect;

    private View emptyView;

    private Handler handler;
    private List<MultipleItem> showingCourseList;
    private List<CourseTagGroup> courseTagGroupList;
    private MultipleItemQuickAdapter courseAdapter;
    private SelectionTagsAdapter tagsAdapter;

    private int currentPage = 1;//要分页查询的页面
    private boolean hasNext;

    //test tool
    private AlertDialog.Builder builder;
    private String dialogMessage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sport_activity_courseselection);
        initView();
        initHandler();
        //test
        builder = new AlertDialog.Builder(this);

        getHttpCourse(new HashMap());
        getHttpCourseTags();
        initData();
    }

    private void initView() {
        ivBack = findViewById(R.id.iv_back);
        ivSearch = findViewById(R.id.iv_search);
        rvCourseTags = findViewById(R.id.rv_course_tags);
        rvCourseShow = findViewById(R.id.rv_course_show);

        btnUnselect = findViewById(R.id.btn_unselect);
        btnSelect = findViewById(R.id.btn_select);

        ivBack.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        btnUnselect.setOnClickListener(this);
        btnSelect.setOnClickListener(this);

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

                        Toast.makeText(sport_activity_course_selection.this, "请求成功", Toast.LENGTH_SHORT).show();
                        break;
                    case LOAD_COURSES_FAILED:
                        Log.d("HANDLER","LOAD_COURSES_FAILED");
                        Toast.makeText(sport_activity_course_selection.this, "LOAD_COURSES_FAILED,"+msg.obj, Toast.LENGTH_LONG).show();

                    case LOAD_TAGS_SUCCESS:
                        for(int i=0;i<courseTagGroupList.size();i++){
                            for(int j=0;j<courseTagGroupList.get(i).getCourseTagList().size();j++){
                                Log.d("LOAD_TAGS_SUCCESS","courseTagGroupList.get"+i+".getCourseTagList.get"+j+"= "+courseTagGroupList.get(i).getCourseTagList().get(j).getTagName());
                            }
                        }
                        tagsAdapter.notifyDataSetChanged();
                        break;
                    case LOAD_TAGS_FAILED:
                        Log.d("HANDLER","LOAD_TAGS_FAILED");
                        Toast.makeText(sport_activity_course_selection.this, "LOAD_TAGS_FAILED,"+msg.obj, Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
    }

    private void initData(){

        LinearLayoutManager layoutM = new LinearLayoutManager(getBaseContext());
        LinearLayoutManager layoutM2 = new LinearLayoutManager(getBaseContext());

        layoutM.setOrientation(LinearLayoutManager.VERTICAL);
        layoutM2.setOrientation(LinearLayoutManager.VERTICAL);
        rvCourseShow.setLayoutManager(layoutM);
        rvCourseTags.setLayoutManager(layoutM2);

        showingCourseList = new ArrayList<>();
        courseAdapter = new MultipleItemQuickAdapter(showingCourseList);
        rvCourseShow.setAdapter(courseAdapter);
        rvCourseShow.setNestedScrollingEnabled(false);

        courseAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = new Intent(getBaseContext(),sport_activity_course_detail.class);
                intent.putExtra("courseId",showingCourseList.get(position).getCourse().getCourseId());
                startActivity(intent);
            }
        });

        emptyView = View.inflate(this,R.layout.sport_empty_view_courseselection,null);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(sport_activity_course_selection.this, "Retrying", Toast.LENGTH_SHORT).show();
                getHttpCourse(new HashMap());
                getHttpCourseTags();
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

        courseTagGroupList = new ArrayList<>();
        tagsAdapter = new SelectionTagsAdapter(R.layout.sport_item_tag_menu,courseTagGroupList);

        rvCourseTags.setAdapter(tagsAdapter);
        rvCourseTags.setNestedScrollingEnabled(false);

    }

    //首次以及拉取更多课程数据
    private void configLoadMoreCourse() {
        if(hasNext){
            Log.d("sport_activity_course_selection","configLoadMoreCourse");
            startSelection(++currentPage);
        }else{
            courseAdapter.getLoadMoreModule().loadMoreEnd();
        }
    }

    //String url = "https://www.fastmock.site/mock/1f8fe01c6b3cdb34a1d2ad4b1a45a8c0/motion/api/getCourseById?size=10";
    //http://10.34.25.45:8080/api/course/getCourseById?size=10&page=1&courseClassIdstr=1,2,3;4,5,6;&isOnline=3&isHot=1

    private void getHttpCourse(Map params) {
        List<MultipleItem> onePageCourses = new ArrayList<>();

        String url = "http://10.34.25.45:8080/api/course/getCourseById?size=" + COURSE_NUM_IN_ONE_PAGE;
        //String url = "https://www.fastmock.site/mock/1f8fe01c6b3cdb34a1d2ad4b1a45a8c0/motion/api/getCourseById?size=10";
        if(params.isEmpty()){
            url+="&page=1&courseClassIdstr=&isOnline=1&isHot=1";
        }else{
            Iterator iter = params.keySet().iterator();
            while (iter.hasNext()) {
                Object key = iter.next();
                Object val = params.get(key);
                url+=("&"+key.toString()+"="+val.toString());
            }
        }
        Log.d("sport_activity_course_selection","requestUrl:" + url);

        //test tool
        dialogMessage += "\n\ngetHttpCourse requestingUrl:\n" + url;
        //end of test tool

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
                        Course course = new Course();
                        course.setCourseId(jsonCourseObject.getLong("courseId"));
                        course.setCourseName(jsonCourseObject.getString("courseName"));
                        course.setBackgroundUrl(jsonCourseObject.getString("backgroundUrl"));
                        course.setTargetAge(jsonCourseObject.getString("targetAge"));
                        course.setIsOnline(jsonCourseObject.getInt("online"));

                        JSONArray JSONArrayLabels = jsonCourseObject.getJSONArray("labels");
                        String labels = "";
                        for (int j = 0; j < JSONArrayLabels.length(); j++) {
                            labels += (JSONArrayLabels.get(j) + "/");
                        }
                        course.setLabels(labels);
                        onePageCourses.add(new MultipleItem(MultipleItem.COURSEFULL, course));
                    }
                    showingCourseList.addAll(onePageCourses);


                    Log.d("sport_activity_course_selection","getHttpCourse_responseStr:"+responseStr);
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
            @Override
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

    private void getHttpCourseTags() {
        courseTagGroupList = new ArrayList<>();
        //String url ="https://www.fastmock.site/mock/318b7fee143da8b159d3e46048f8a8b3/api/getSorts";
        String url ="http://10.34.25.45:8080/api/CourseClass/getSorts";

        //test tool
        dialogMessage += "\n\ngetHttpCourseTags requestingUrl:\n" + url;
        //end of test tool

        MyStringRequest getTagsStringRequest = new MyStringRequest(Request.Method.GET,  url, new Response.Listener<String>() {
            @Override
            public void onResponse(String responseStr) {
                try {
                    JSONObject jsonRootObject = new JSONObject(responseStr);
                    JSONArray jsonTagGroupArray = jsonRootObject.getJSONArray("data");

                    for(int j=0;j<jsonTagGroupArray.length();j++){
                        CourseTagGroup courseTagGroup = new CourseTagGroup();
                        courseTagGroup.setCourseTagList(new ArrayList<>());
                        courseTagGroup.getCourseTagList().add(new CourseTag(0,"全部"));

                        JSONObject jsonTagsRootObject = jsonTagGroupArray.getJSONObject(j);
                        courseTagGroup.setGroupId(jsonTagsRootObject.getInt("id"));
                        courseTagGroup.setGroupName(jsonTagsRootObject.getString("name"));

                        JSONArray JSONArrayTags = jsonTagsRootObject.getJSONArray("tab");
                        for (int i = 0; i < JSONArrayTags.length(); i++) {
                            JSONObject jsonTagObject = JSONArrayTags.getJSONObject(i);

                            CourseTag courseTag = new CourseTag();
                            courseTag.setTagId(jsonTagObject.getInt("courseClassId"));
                            courseTag.setTagName(jsonTagObject.getString("classValue"));
                            courseTag.setTagUrl(jsonTagObject.getString("classUrl"));
                            courseTagGroup.getCourseTagList().add(courseTag);
                        }
                        courseTagGroupList.add(courseTagGroup);
                    }
                    CourseTagGroup isOnlineCourseTagGroup = new CourseTagGroup(CourseTagGroup.TAG_GROUP_IS_ONLINE,"是否上线",new ArrayList<>());
                    CourseTagGroup sortCourseTagGroup = new CourseTagGroup(CourseTagGroup.TAG_GROUP_SORT,"排序",new ArrayList<>());

                    isOnlineCourseTagGroup.getCourseTagList().add(new CourseTag(CourseTag.TAG_ONLINE_YES,getString(R.string.course_selection_tag_online_yes)));
                    isOnlineCourseTagGroup.getCourseTagList().add(new CourseTag(CourseTag.TAG_ONLINE_NO,getString(R.string.course_selection_tag_online_no)));
                    sortCourseTagGroup.getCourseTagList().add(new CourseTag(CourseTag.TAG_SORT_HOT,getString(R.string.course_selection_tag_sort_hot)));
                    sortCourseTagGroup.getCourseTagList().add(new CourseTag(CourseTag.TAG_SORT_DATE,getString(R.string.course_selection_tag_sort_date)));

                    courseTagGroupList.add(isOnlineCourseTagGroup);
                    courseTagGroupList.add(sortCourseTagGroup);

                    Message msg = handler.obtainMessage();
                    msg.what = LOAD_TAGS_SUCCESS;
                    handler.sendMessage(msg);

                    //test tool
                    dialogMessage+="\n\ngetHttpCourseTags response:\n"+jsonRootObject.toString();
                    //end of test tool

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("sport_activity_course_selection","getHttpCourseTags_onErrorResponse");

                //test tool
                dialogMessage+="\ngetHttpCourseTags error: "+volleyError.toString();
                //end of test tool

                Message msg = handler.obtainMessage();
                msg.what = LOAD_TAGS_FAILED;
                msg.obj = volleyError.toString();
                handler.sendMessage(msg);
            }
        });
        getTagsStringRequest.setTag("getHttp");
        requestQueue.add(getTagsStringRequest);

    }

    private void startSelection(int page){
        String courseClassIdstr = "";
        Map params = new HashMap();
        params.put("page",page);

        for(int i=0;i<courseTagGroupList.size();i++){
            TabLayout groupRootView = (TabLayout)tagsAdapter.getViewByPosition(i,R.id.tl_tags);
            Log.d("startSelection","Group_Name:"+courseTagGroupList.get(i).getGroupName()+" selected_tag_name:"+courseTagGroupList.get(i).getCourseTagList().get(groupRootView.getSelectedTabPosition()).getTagName());

            if(courseTagGroupList.get(i).getGroupId() > 0){
                int selectedId = courseTagGroupList.get(i).getCourseTagList().get(groupRootView.getSelectedTabPosition()).getTagId();
                if(selectedId==0){
                    for(int j=1;j<courseTagGroupList.get(i).getCourseTagList().size();j++){
                        courseClassIdstr += String.valueOf(courseTagGroupList.get(i).getCourseTagList().get(j).getTagId())+",";
                    }

                }else{
                    courseClassIdstr += String.valueOf(selectedId)+",";
                }
            }else if(courseTagGroupList.get(i).getGroupId() == CourseTagGroup.TAG_GROUP_IS_ONLINE){
                params.put("isOnline",courseTagGroupList.get(i).getCourseTagList().get(groupRootView.getSelectedTabPosition()).getTagId());
            }else if(courseTagGroupList.get(i).getGroupId() == CourseTagGroup.TAG_GROUP_SORT){
                params.put("isHot",courseTagGroupList.get(i).getCourseTagList().get(groupRootView.getSelectedTabPosition()).getTagId());
            }
        }
        if(!courseClassIdstr.equals("")){
            courseClassIdstr = courseClassIdstr.substring(0,courseClassIdstr.length() - 1)+";";
        }
        params.put("courseClassIdstr",courseClassIdstr);

        getHttpCourse(params);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_search:
                //test function
                builder.setMessage(dialogMessage)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create()
                        .show();
                //跳转到搜索课程页
                break;
            case R.id.btn_unselect:
                //取消所有选择
                //showingCourseList.clear();
                for(int i=0;i<courseTagGroupList.size();i++){
                    TabLayout groupRootView = (TabLayout)tagsAdapter.getViewByPosition(i,R.id.tl_tags);
                    if(groupRootView.getTabAt(0) != null){
                        groupRootView.getTabAt(0).select();
                    }
                }
                break;
            case R.id.btn_select:
                //开始筛选课程
                if(!courseTagGroupList.isEmpty()){
                    //test tool
                    dialogMessage="";
                    //end

                    showingCourseList.clear();
                    startSelection(1);
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        requestQueue.cancelAll("getHttp");
    }
}