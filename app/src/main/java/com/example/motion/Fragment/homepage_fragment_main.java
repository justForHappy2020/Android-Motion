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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.motion.Activity.sport_activity_course_selection;
import com.example.motion.Entity.BannerTag;
import com.example.motion.Entity.CourseTagGroup;
import com.example.motion.R;
import com.example.motion.MontionRequest.GetCourseTagGroupRequest;
import com.example.motion.MontionRequest.GetSpecialSortRequest;
import com.example.motion.Widget.BannerAdapter;
import com.example.motion.Widget.CourseGroupAdapter;

import java.util.ArrayList;
import java.util.List;

public class homepage_fragment_main extends BaseNetworkFragment {
    private final int LOAD_TAGS_SUCCESS = 1;
    private final int LOAD_TAGS_FAILED = 2;
    private final int LOAD_BANNER_SUCCESS = 3;
    private final int LOAD_BANNER_FAILED = 4;

    private ImageView ivMoreCourse;

    private RecyclerView rvCourseTagGroup;
    private RecyclerView rvBanner;

    private CourseGroupAdapter courseGroupAdapter;
    private BannerAdapter bannerAdapter;

    private List<CourseTagGroup> courseTagGroupList;
    private List<BannerTag> bannerTagList;

    private Handler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homepage_fragment_main, container, false);
        initView(view);
        initHandler();
        initCourseTagGroupRecyclerView();
        initBannerRecyclerView();

        getHttpCourseTags();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getHttpBanner();
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
                        checkVolleyError(msg.obj);
                        Log.d("HANDLER","HOMEPAGE_LOAD_TAGS_FAILED"+msg.obj);
                        //Toast.makeText(getContext(), "LOAD_TAGS_FAILED,"+msg.obj, Toast.LENGTH_LONG).show();
                        break;
                    case LOAD_BANNER_SUCCESS:
                        bannerAdapter.notifyDataSetChanged();
                        Log.d("HANDLER","LOAD_BANNER_SUCCESS");
                        break;
                    case LOAD_BANNER_FAILED:
                        //checkVolleyError(msg.obj);
                        Log.d("HANDLER","LOAD_BANNER_FAILED");
                        break;
                }
            }
        };
    }

    private void initView(View view){
        ivMoreCourse = view.findViewById(R.id.ivNavItem6);
        rvCourseTagGroup = view.findViewById(R.id.rv_homepage_course_tag_group);
        rvBanner = view.findViewById(R.id.rv_banner);

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
                try{
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

                }catch (Exception e){
                    e.printStackTrace();
                }

                Log.d("onItemChildClick","position:"+String.valueOf(position)+"SelectedGroupId:"+courseTagGroupList.get(position).getGroupId());

                intent.putExtra("SelectedTagId",selectedTagId);
                intent.putExtra("SelectedGroupId",courseTagGroupList.get(position).getGroupId());
                startActivity(intent);
            }
        });
    }

    private void initBannerRecyclerView(){
        GridLayoutManager layoutM = new GridLayoutManager(getContext(),3);
        layoutM.setOrientation(LinearLayoutManager.VERTICAL);

        rvBanner.setLayoutManager(layoutM);

        bannerTagList = new ArrayList<>();
        bannerAdapter = new BannerAdapter(R.layout.homepage_item_banner_tag,bannerTagList);

        rvBanner.setAdapter(bannerAdapter);
        bannerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = new Intent(getActivity(),sport_activity_course_selection.class);

                intent.putExtra("SelectedGroupId",bannerTagList.get(position).getSortId());
                intent.putExtra("SelectedTagId",bannerTagList.get(position).getTagId());
                startActivity(intent);
            }
        });
    }

    private void getHttpCourseTags() {
        requestQueue.add(new GetCourseTagGroupRequest(new Response.Listener<List<CourseTagGroup>>() {
            @Override
            public void onResponse(List<CourseTagGroup> response) {
                courseTagGroupList.clear();
                courseTagGroupList.addAll(response);

                Message msg = handler.obtainMessage();
                msg.what = LOAD_TAGS_SUCCESS;
                handler.sendMessage(msg);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Message msg = handler.obtainMessage();
                msg.what = LOAD_TAGS_FAILED;
                msg.obj = error.toString();
                handler.sendMessage(msg);
            }
        }));
        /*
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

         */
    }

    private void getHttpBanner(){
        requestQueue.add(new GetSpecialSortRequest(new Response.Listener<List<BannerTag>>() {
            @Override
            public void onResponse(List<BannerTag> response) {
                bannerTagList.clear();
                bannerTagList.addAll(response);

                Message msg = handler.obtainMessage();
                msg.what = LOAD_BANNER_SUCCESS;
                handler.sendMessage(msg);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Message msg = handler.obtainMessage();
                msg.what = LOAD_BANNER_FAILED;
                msg.obj = error.toString();
                handler.sendMessage(msg);
            }
        }));
    }
}