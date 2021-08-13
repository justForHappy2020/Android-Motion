package com.example.motion.Widget;

import android.util.Log;
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
        try{
            helper.setText(R.id.tv_banner_name,bannerTag.getBannerName());
            if(bannerTag.getImgUrl() != null && ! bannerTag.getImgUrl().isEmpty()){
                Log.d("ranlychan","not null or empty:"+bannerTag.getBannerName()+bannerTag.getImgUrl());
                Glide.with(getContext())
                        .load(bannerTag.getImgUrl())
                        .into((ImageView) helper.getView(R.id.iv_banner_img));

            }else{
                Log.d("ranlychan","null or empty"+bannerTag.getImgDrawableId());
                Glide.with(getContext())
                        .load(bannerTag.getImgDrawableId())
                        .into((ImageView) helper.getView(R.id.iv_banner_img));
            }

        }catch (Exception e){
            e.printStackTrace();
            Log.d("ranlychan","an error");
        }

    }
}