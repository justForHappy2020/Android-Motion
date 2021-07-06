package com.example.motion.Widget;

import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.motion.Entity.Course;
import com.example.motion.GlideTransforms.GlideRadiusTransform;
import com.example.motion.R;

import java.text.DecimalFormat;
import java.util.List;

public class DownloadedCoursesAdapter extends BaseQuickAdapter<Course, BaseViewHolder>{

    private final int ByteToMB = 1024*1024;
    private DecimalFormat decimalFormat;
    private String MByteText;

    public DownloadedCoursesAdapter(@LayoutRes int layoutResId, @Nullable List<Course> data) {
        super(layoutResId, data);
        decimalFormat = new DecimalFormat("0.##");
    }

    @Override
    protected void convert(BaseViewHolder helper, Course item) {
        helper.setText(R.id.tv_item_course_name_dl,item.getCourseName())
                .setText(R.id.tv_item_course_size_dl,toMB(item.getCourseSize()));
    }

    private String toMB(int byteSize){
        float MByteSize = 0;
        MByteSize = (float)byteSize / (float)ByteToMB;
        return decimalFormat.format(MByteSize)+ MByteText;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        MByteText = getContext().getString(R.string.MByte);
    }
}