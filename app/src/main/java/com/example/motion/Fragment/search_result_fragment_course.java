package com.example.motion.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.motion.Entity.Course;
import com.example.motion.Entity.DyxItem;
import com.example.motion.Entity.MultipleItem;
import com.example.motion.R;
import com.example.motion.Widget.DyxQuickAdapter;

import java.util.ArrayList;
import java.util.List;

public class search_result_fragment_course extends BaseNetworkFragment implements View.OnClickListener {
    private RecyclerView rvSearchCourse;
    String searchContent;
    private DyxQuickAdapter courseAdapter;
    private List<DyxItem> courseList = new ArrayList();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.search_fragment_course, container, false);
        Bundle bundle = getArguments();
        searchContent = bundle.getString("searchContent");
        initView(view);
        initData();
        return view;
    }
    public void initView(View view){
        rvSearchCourse=view.findViewById(R.id.rv_search_course_show);
        rvSearchCourse.setLayoutManager(new LinearLayoutManager(getActivity()));
        courseAdapter = new DyxQuickAdapter(courseList);
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
    @Override
    public void onClick(View v) {

    }
}
