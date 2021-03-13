package com.example.motion.Widget;

import android.widget.ImageView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.module.UpFetchModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.motion.Entity.Course;
import com.example.motion.R;

import java.util.List;

public class RelatedCoursesAdapter extends BaseQuickAdapter<Course, BaseViewHolder>{

    public RelatedCoursesAdapter(@LayoutRes int layoutResId, @Nullable List<Course> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Course item) {
        helper.setText(R.id.course_item_name, item.getCourseName())
                .setText(R.id.course_item_text, item.getDegree());//应为课程分类
        Glide.with(getContext()).load(item.getBackgroundUrl()).into((ImageView) helper.getView(R.id.course_item_bgImg));
    }
}