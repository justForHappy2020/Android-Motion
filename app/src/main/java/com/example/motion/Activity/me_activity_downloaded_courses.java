package com.example.motion.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.motion.Entity.Course;
import com.example.motion.R;
import com.example.motion.Utils.CourseCacheUtil;
import com.example.motion.Widget.DownloadedCoursesAdapter;

import java.util.ArrayList;
import java.util.List;

public class me_activity_downloaded_courses extends NeedTokenActivity implements View.OnClickListener {

    private final int DL_COURSES_LOAD_FAILED = 0;
    private final int DL_COURSES_LOAD_SUCCESS = 1;

    private RecyclerView rvDlCourses;
    private DownloadedCoursesAdapter courseAdapter;
    private List<Course> dlCourseList;
    private Handler handler;

    private TextView tvEdit;
    private ImageView ivBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_downloaded_courses);
        intiView();
        initAdapter();
        initRecyclerView();
        initHandler();
        initData();
    }

    private void initHandler(){
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case DL_COURSES_LOAD_FAILED:
                        Log.i("me_activity_downloaded_courses","DL_COURSES_LOAD_FAILED");
                        Toast.makeText(getBaseContext(), getText(R.string.me_activity_downloaded_courses_state_load_fail), Toast.LENGTH_SHORT).show();
                        break;
                    case DL_COURSES_LOAD_SUCCESS:
                        courseAdapter.setDiffNewData(dlCourseList);
                        for(int i=0;i<dlCourseList.size();i++){
                            Log.d("me_activity_downloaded_courses","dlCourseList.get"+i+".size="+dlCourseList.get(i).getCourseSize());
                        }

                        Log.d("me_activity_downloaded_courses","dlCourseList end");
                        break;
                }
            }
        };
    }

    private void intiView(){
        rvDlCourses = findViewById(R.id.rv_dl_courses);
        tvEdit = findViewById(R.id.tv_edit_dl_courses);
        ivBack = findViewById(R.id.iv_back_dl_courses);

        tvEdit.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    private void initRecyclerView(){

        LinearLayoutManager layoutM = new LinearLayoutManager(getBaseContext());
        layoutM.setOrientation(LinearLayoutManager.VERTICAL);
        rvDlCourses.setLayoutManager(layoutM);
        rvDlCourses.setAdapter(courseAdapter);


    }

    private void initAdapter(){

        dlCourseList = new ArrayList<>();
        courseAdapter = new DownloadedCoursesAdapter(R.layout.me_item_downloaded_courses,dlCourseList);
        View emptyView = getLayoutInflater().inflate(R.layout.me_empty_view_download_courses,null);
        courseAdapter.setEmptyView(emptyView);
        courseAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = new Intent(me_activity_downloaded_courses.this,sport_activity_course_detail.class);
                intent.putExtra("courseId2CourseJson",dlCourseList.get(position).getCourseId2CourseJson());
                startActivity(intent);
            }
        });

    }

    private void initData(){

        CourseCacheUtil ccu = new CourseCacheUtil(this,getCacheDir());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dlCourseList = ccu.getAllCachedCourseList();

                Message msg = handler.obtainMessage();
                msg.what = DL_COURSES_LOAD_SUCCESS;
                msg.sendToTarget();
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_edit_dl_courses:

                break;
            case R.id.iv_back_dl_courses:
                finish();
                break;
        }
    }
}