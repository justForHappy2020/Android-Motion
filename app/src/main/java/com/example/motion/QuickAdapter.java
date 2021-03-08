package com.example.motion;

import android.widget.ImageView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.module.UpFetchModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.motion.Entity.Course;

import java.util.List;

public class QuickAdapter extends BaseQuickAdapter<Course, BaseViewHolder> implements UpFetchModule, LoadMoreModule {

    public QuickAdapter(@LayoutRes int layoutResId, @Nullable List<Course> data) {
        super(layoutResId, data);
    }



    @Override
    protected void convert(BaseViewHolder helper, Course item) {
        //可链式调用赋值
        String item_text = item.getDegree() + " . " + item.getDuration() + " . " +item.getHits() + "万人已参加";
        helper.setText(R.id.course_item_name, item.getCourseName())
                .setText(R.id.course_item_text, item_text);
        Glide.with(getContext()).load(item.getBackgroundUrl()).into((ImageView) helper.getView(R.id.course_item_bgImg));
    }
}