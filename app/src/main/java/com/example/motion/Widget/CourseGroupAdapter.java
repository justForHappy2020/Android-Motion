package com.example.motion.Widget;

import android.content.res.ColorStateList;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.motion.Entity.CourseTagGroup;
import com.example.motion.R;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class CourseGroupAdapter extends BaseQuickAdapter<CourseTagGroup, BaseViewHolder>{

    public CourseGroupAdapter(@LayoutRes int layoutResId, @Nullable List<CourseTagGroup> courseTagGroups) {
        super(layoutResId, courseTagGroups);
    }

    @Override
    protected void convert(BaseViewHolder helper, CourseTagGroup tagGroup) {
        if(helper.getLayoutPosition()%2!=0){
            helper.getView(R.id.ll_course_tag_group_header).setBackgroundResource(R.drawable.homepage_types_radius_orange);
        }
            helper.setText(R.id.tv_course_tag_group_name, tagGroup.getGroupName())
                    .setText(R.id.tv_course_name_1,tagGroup.getCourseTagList().get(0).getTagName())
                    .setText(R.id.tv_course_name_2,tagGroup.getCourseTagList().get(1).getTagName())
                    .setText(R.id.tv_course_name_3,tagGroup.getCourseTagList().get(2).getTagName());
            Glide.with(getContext())
                    .load(tagGroup.getCourseTagList().get(0).getTagUrl())
                    .into((ImageView) helper.getView(R.id.iv_course_tag_img_1));
            Glide.with(getContext())
                    .load(tagGroup.getCourseTagList().get(1).getTagUrl())
                    .into((ImageView) helper.getView(R.id.iv_course_tag_img_2));
            Glide.with(getContext())
                    .load(tagGroup.getCourseTagList().get(2).getTagUrl())
                    .into((ImageView) helper.getView(R.id.iv_course_tag_img_3));

    }
}