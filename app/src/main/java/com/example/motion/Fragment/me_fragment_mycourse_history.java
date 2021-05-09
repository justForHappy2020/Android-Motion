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
import com.example.motion.Entity.me_mycourse_history;
import com.example.motion.R;
import com.example.motion.Widget.MultipleItemQuickAdapter;

import java.util.ArrayList;
import java.util.List;

public class me_fragment_mycourse_history extends Fragment {

    private List<List> dataSet = new ArrayList();
    private List<MultipleItem> historyList = new ArrayList();
    MultipleItemQuickAdapter quickAdapter;
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.me_fragment_mycourse_history,container,false);
        initData();
        initView(view);
        return view;
    }

    public void initView(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.meMyCourseHistoryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        quickAdapter = new MultipleItemQuickAdapter(historyList);
        recyclerView.setAdapter(quickAdapter);
    }
    public void initData(){
       // me_mycourse_history history1 = new me_mycourse_history("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3385472845,2539383542&fm=11&gp=0.jpg","写代码","数据结构","好好学习","x1");
       // me_mycourse_history history2 = new me_mycourse_history("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3385472845,2539383542&fm=11&gp=0.jpg","代码","数据","学习","x2");
        // me_mycourse_history history3 = new me_mycourse_history("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3385472845,2539383542&fm=11&gp=0.jpg","写","结构","好好","x13");

        //historyList.add(history1);
        //historyList.add(history2);
        //historyList.add(history3);

        me_mycourse_history history;
        for (int i = 0; i < 5; i++) {
            history = new me_mycourse_history();
            history.setImgUrls("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3385472845,2539383542&fm=11&gp=0.jpg");
            history.setHistoryName1("写代码");
            history.setHistoryName2("数据结构");
            history.setHistoryName3("学习");
            history.setHistoryTimes("x1");
            historyList.add(new MultipleItem(MultipleItem.Me_mycourse_history,history));
        }
        dataSet.add(historyList);
    }
}
