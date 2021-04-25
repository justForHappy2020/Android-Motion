package com.example.motion.Widget;

import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.module.UpFetchModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.motion.Entity.DyxItem;
import com.example.motion.R;

import java.util.List;


public class DyxQuickAdapter extends BaseMultiItemQuickAdapter<DyxItem, BaseViewHolder> implements UpFetchModule, LoadMoreModule{

    public DyxQuickAdapter(List data) {
        super(data);
        addItemType(DyxItem.SELECTBTN1, R.layout.sport_recycle_item_btn);
        addItemType(DyxItem.SELECTBTN2, R.layout.sport_recycle_item_recyclebtn);
        addItemType(DyxItem.SELECTCOURSE, R.layout.sport_recycle_item_course);
        addItemType(DyxItem.PORTRAIT, R.layout.me_recycle_item_portraitsmall);
        addItemType(DyxItem.HEALTHRECORD, R.layout.me_recycle_item_healthrecord);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final DyxItem item) {

        switch (helper.getItemViewType()) {
            case DyxItem.SELECTBTN1:
                //int position = helper.getAdapterPosition();
                helper.setText(R.id.textView11 ,item.getItemList2().get(0).getText());
                RecyclerView mRecyclerView = helper.getView(R.id.rv_itembtn);
                LinearLayoutManager btnLayoutManage2 = new LinearLayoutManager(getContext());
                btnLayoutManage2.setOrientation(LinearLayoutManager.HORIZONTAL);
                mRecyclerView.setLayoutManager(btnLayoutManage2);
                DyxQuickAdapter btnAdater2 = new DyxQuickAdapter(item.getItemList2());//获取子列表,从第二个开始记录2级标签。
                mRecyclerView.setAdapter(btnAdater2);
                mRecyclerView.setNestedScrollingEnabled(false);
                mRecyclerView.setLayoutFrozen(true);
                break;
            case DyxItem.SELECTBTN2:
                helper.setText(R.id.btn_recycebtn , item.getText());
                //单选、复选、取消选择
                break;
            case DyxItem.SELECTCOURSE:
                helper.setText(R.id.tv_course_name , item.getCourse().getCourseName())
                        .setText(R.id.tv_course_type , item.getCourse().getCourseIntro())//这里是获取适合人群、分类，后期改
                        .setText(R.id.tv_course_status , getContext().getResources().getString(R.string.course_select_online));
                Glide.with(getContext()).load(item.getCourse().getBackgroundUrl()).placeholder(R.drawable.ic_placeholder).into((ImageView)helper.getView(R.id.iv_course_img));
                break;
            case DyxItem.PORTRAIT:
                Glide.with(getContext()).load(item.getUser().getHeadPortraitUrl()).placeholder(R.mipmap.ic_launcher).into((ImageView)helper.getView(R.id.Riv_portrait_big));
                break;
            case DyxItem.HEALTHRECORD:
                helper.setText(R.id.tv_time , item.getHealthRecord().getCreateTime())
                        .setText(R.id.tv_weight , item.getHealthRecord().getWeight())
                        .setText(R.id.tv_height , item.getHealthRecord().getHeight())
                        .setText(R.id.et_age , item.getHealthRecord().getBmi());
                Glide.with(getContext()).load(item.getHealthRecord().getPictureURL()).placeholder(R.drawable.wechat).into((ImageView)helper.getView(R.id.imageView3));
                break;
        }
    }

}