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

import com.example.motion.Entity.MultipleItem;
import com.example.motion.Entity.me_mycourse_reserve;
import com.example.motion.R;
import com.example.motion.Widget.MultipleItemQuickAdapter;

import java.util.ArrayList;
import java.util.List;

public class me_fragment_mycourse_reserve extends Fragment {

    private List<List> dataSet = new ArrayList();
    private List<MultipleItem> reserveList = new ArrayList();
    MultipleItemQuickAdapter quickAdapter;
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.me_fragment_mycourse_reserve,container,false);
        initData();
        initView(view);
        return view;
    }

    public void initView(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.meMyCourseReserveRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        quickAdapter = new MultipleItemQuickAdapter(reserveList);
        recyclerView.setAdapter(quickAdapter);
    }

    public void initData(){

        me_mycourse_reserve reserve;
        for (int i = 0; i < 5; i++) {
            reserve = new me_mycourse_reserve();
            reserveList.add(new MultipleItem(13,reserve));
            reserve.setImgUrls("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3385472845,2539383542&fm=11&gp=0.jpg");
            reserve.setReserveName1("写代码");
            reserve.setReserveName2("数据结构");
            reserve.setReserveName3("学习");
            reserve.setReserveTime("2月30号上线");
        }
        dataSet.add(reserveList);
    }
}
