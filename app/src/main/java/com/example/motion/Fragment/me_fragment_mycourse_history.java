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
            //history.setImgUrls("https://image.baidu.com/search/detail?ct=503316480&z=undefined&tn=baiduimagedetail&ipn=d&word=%E5%A4%B4%E5%83%8F&step_word=&ie=utf-8&in=&cl=2&lm=-1&st=undefined&hd=undefined&latest=undefined&copyright=undefined&cs=3905966094,2907968810&os=1110949748,114522899&simid=0,0&pn=27&rn=1&di=214170&ln=3750&fr=&fmq=1621587174065_R&fm=&ic=undefined&s=undefined&se=&sme=&tab=0&width=undefined&height=undefined&face=undefined&is=0,0&istype=0&ist=&jit=&bdtype=0&spn=0&pi=0&gsm=0&objurl=https%3A%2F%2Fgimg2.baidu.com%2Fimage_search%2Fsrc%3Dhttp%253A%252F%252Fpic1.zhimg.com%252F50%252Fv2-fd4b5bb4e5156f2f9f75b6f9829c5a9d_hd.jpg%26refer%3Dhttp%253A%252F%252Fpic1.zhimg.com%26app%3D2002%26size%3Df9999%2C10000%26q%3Da80%26n%3D0%26g%3D0n%26fmt%3Djpeg%3Fsec%3D1624179173%26t%3Df8c5fa49c4605924fcf25a8e04b96d83&rpstart=0&rpnum=0&adpicid=0&force=undefined");
            history.setHistoryName1("写代码");
            history.setHistoryName2("数据结构");
            history.setHistoryName3("学习");
            history.setHistoryTimes("x1");
            historyList.add(new MultipleItem(MultipleItem.Me_mycourse_history,history));
        }
        dataSet.add(historyList);
    }
}
