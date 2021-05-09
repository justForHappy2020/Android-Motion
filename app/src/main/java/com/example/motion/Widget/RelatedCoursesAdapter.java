package com.example.motion.Widget;

import android.widget.ImageView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.motion.Entity.Course;
import com.example.motion.GlideTransforms.GlideRadiusTransform;
import com.example.motion.R;

import java.util.List;

public class RelatedCoursesAdapter extends BaseQuickAdapter<Course, BaseViewHolder>{

    public RelatedCoursesAdapter(@LayoutRes int layoutResId, @Nullable List<Course> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Course item) {
        helper.setText(R.id.course_item_name, item.getCourseName())
                .setText(R.id.course_item_text, item.getTargetAge());
        Glide.with(getContext())
                .load(item.getBackgroundUrl())
                .transform(new CenterCrop(getContext()), new GlideRadiusTransform(getContext(),11))
                .into((ImageView) helper.getView(R.id.course_item_bgImg));
    }
}