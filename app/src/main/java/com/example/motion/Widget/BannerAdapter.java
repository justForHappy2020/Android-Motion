package com.example.motion.Widget;

import android.widget.ImageView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.motion.Entity.BannerTag;
import com.example.motion.Entity.CourseTagGroup;
import com.example.motion.R;

import java.util.List;

public class BannerAdapter extends BaseQuickAdapter<BannerTag, BaseViewHolder>{


    public BannerAdapter(int layoutResId, List<BannerTag> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BannerTag bannerTag) {
        helper.setText(R.id.tv_banner_name,bannerTag.getBannerName());
        Glide.with(getContext())
                .load(bannerTag.getImgUrl())
                .into((ImageView) helper.getView(R.id.iv_banner_img));
    }
}