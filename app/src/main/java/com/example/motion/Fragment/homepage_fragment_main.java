package com.example.motion.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.motion.Activity.sport_activity_course_selection;
import com.example.motion.R;

public class homepage_fragment_main extends Fragment {
    private ImageView ivMoreCourse;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homepage_fragment_main, container, false);
        initView(view);
        return view;
    }

    private void initView(View view){
        ivMoreCourse = view.findViewById(R.id.ivNavItem6);
        ivMoreCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), sport_activity_course_selection.class);
                startActivity(intent);
            }
        });
    }
}