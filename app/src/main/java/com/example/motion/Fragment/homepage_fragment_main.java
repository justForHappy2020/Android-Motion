package com.example.motion.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.example.motion.Activity.BaseNetworkActivity;
import com.example.motion.Activity.sport_activity_course_selection;
import com.example.motion.Entity.CourseTag;
import com.example.motion.Entity.CourseTagGroup;
import com.example.motion.Entity.MultipleItem;
import com.example.motion.R;
import com.example.motion.Widget.CourseGroupAdapter;
import com.example.motion.Widget.MultipleItemQuickAdapter;
import com.example.motion.Widget.MyStringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class homepage_fragment_main extends BaseNetworkFragment {
    private final int LOAD_TAGS_SUCCESS = 1;
    private final int LOAD_TAGS_FAILED = 2;

    private ImageView ivMoreCourse;

    private RecyclerView rvCourseTagGroup;
    private CourseGroupAdapter courseGroupAdapter;
    private List<CourseTagGroup> courseTagGroupList;

    private Handler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homepage_fragment_main, container, false);
        initView(view);
        initHandler();
        initCourseTagGroupRecyclerView();

        getHttpCourseTags();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initHandler(){
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case LOAD_TAGS_SUCCESS:
                        Log.d("HANDLER","HOMEPAGE_LOAD_TAGS_SUCCESS");
                        for(int i=0;i<courseTagGroupList.size();i++){
                            for(int j=0;j<courseTagGroupList.get(i).getCourseTagList().size();j++){
                                Log.d("LOAD_TAGS_SUCCESS","courseTagGroupList.get"+i+".getCourseTagList.get"+j+"= "+courseTagGroupList.get(i).getCourseTagList().get(j).getTagName());
                            }
                        }
                        courseGroupAdapter.notifyDataSetChanged();
                        break;
                    case LOAD_TAGS_FAILED:
                        Log.d("HANDLER","HOMEPAGE_LOAD_TAGS_FAILED"+msg.obj);
                        Toast.makeText(getContext(), "LOAD_TAGS_FAILED,"+msg.obj, Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
    }

    private void initView(View view){
        ivMoreCourse = view.findViewById(R.id.ivNavItem6);
        rvCourseTagGroup = view.findViewById(R.id.rv_homepage_course_tag_group);
        ivMoreCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), sport_activity_course_selection.class);
                startActivity(intent);
            }
        });
    }

    private void initCourseTagGroupRecyclerView(){
        LinearLayoutManager layoutM = new LinearLayoutManager(getContext());
        layoutM.setOrientation(LinearLayoutManager.VERTICAL);

        rvCourseTagGroup.setLayoutManager(layoutM);

        courseTagGroupList = new ArrayList<>();
        courseGroupAdapter = new CourseGroupAdapter(R.layout.homepage_item_tag_group,courseTagGroupList);

        rvCourseTagGroup.setAdapter(courseGroupAdapter);
        rvCourseTagGroup.setNestedScrollingEnabled(false);

        courseGroupAdapter.addChildClickViewIds(R.id.tr_tag_1,R.id.tr_tag_2,R.id.tr_tag_3,R.id.tv_more_course_tag_group);
        courseGroupAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = new Intent(getActivity(),sport_activity_course_selection.class);
                int selectedTagId = 0;

                switch (view.getId()){
                    case R.id.tr_tag_1:
                        selectedTagId = courseTagGroupList.get(position).getCourseTagList().get(0).getTagId();
                        break;
                    case R.id.tr_tag_2:
                        selectedTagId = courseTagGroupList.get(position).getCourseTagList().get(1).getTagId();
                        break;
                    case R.id.tr_tag_3:
                        selectedTagId = courseTagGroupList.get(position).getCourseTagList().get(2).getTagId();
                        break;
                    case R.id.tv_more_course_tag_group:
                        selectedTagId = 0;
                        break;
                }
                Log.d("onItemChildClick","position:"+String.valueOf(position)+"SelectedGroupId:"+courseTagGroupList.get(position).getGroupId());

                intent.putExtra("SelectedTagId",selectedTagId);
                intent.putExtra("SelectedGroupId",courseTagGroupList.get(position).getGroupId());
                startActivity(intent);
            }
        });
    }

    private void getHttpCourseTags() {
        //courseTagGroupList = new ArrayList<>();
        courseTagGroupList.clear();
        //String url ="https://www.fastmock.site/mock/318b7fee143da8b159d3e46048f8a8b3/api/getSorts";
        String url ="http://106.55.25.94:8080/api/CourseClass/getSorts";


        MyStringRequest getTagsStringRequest = new MyStringRequest(Request.Method.GET,  url, new Response.Listener<String>() {
            @Override
            public void onResponse(String responseStr) {
                try {
                    JSONObject jsonRootObject = new JSONObject(responseStr);
                    JSONArray jsonTagGroupArray = jsonRootObject.getJSONArray("data");

                    for(int j=0;j<jsonTagGroupArray.length();j++){
                        CourseTagGroup courseTagGroup = new CourseTagGroup();
                        courseTagGroup.setCourseTagList(new ArrayList<>());
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

                    Message msg = handler.obtainMessage();
                    msg.what = LOAD_TAGS_SUCCESS;
                    handler.sendMessage(msg);

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("homepage_fragment_main","getHttpCourseTags_onErrorResponse");

                Message msg = handler.obtainMessage();
                msg.what = LOAD_TAGS_FAILED;
                msg.obj = volleyError.toString();
                handler.sendMessage(msg);
            }
        });
        getTagsStringRequest.setTag("getHttp");
        requestQueue.add(getTagsStringRequest);
    }
}