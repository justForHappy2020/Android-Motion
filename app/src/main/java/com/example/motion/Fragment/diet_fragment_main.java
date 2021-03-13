package com.example.motion.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.motion.R;


public class diet_fragment_main extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.diet_fragment_main, container, false);
        initView(view);
        return view;
    }

    private void initView(View view){
        ImageView img2= view.findViewById(R.id.diet_main_search);

    }
}