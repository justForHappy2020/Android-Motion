package com.example.motion.Widget;

import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.module.UpFetchModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.motion.Entity.CourseTag;
import com.example.motion.GlideTransforms.GlideRadiusTransform;
import com.example.motion.Entity.MultipleItem;
import com.example.motion.R;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class MultipleItemQuickAdapter extends BaseMultiItemQuickAdapter<MultipleItem, BaseViewHolder> implements UpFetchModule, LoadMoreModule{

    public MultipleItemQuickAdapter(List data) {
        super(data);
        addItemType(MultipleItem.MASONRYPOST, R.layout.community_item_share_simplified);
        addItemType(MultipleItem.USER, R.layout.community_item_user_result);
        addItemType(MultipleItem.SHAREABB, R.layout.community_item_share);
        addItemType(MultipleItem.ACTION, R.layout.sport_item_course_action);
        addItemType(MultipleItem.SHAREFULL,R.layout.community_item_share_full);
        addItemType(MultipleItem.COURSEFULL,R.layout.sport_item_course_full);
        addItemType(MultipleItem.Me_mycourse_history,R.layout.me_item_mycourse_history);
        addItemType(MultipleItem.Me_mycourse_collections,R.layout.me_item_mycourse_collections);
        addItemType(MultipleItem.Me_mycourse_reserve,R.layout.me_item_mycourse_reserve);
        addItemType(MultipleItem.Me_mycollections_articals,R.layout.me_item_mycollections_articals);

    }

    @Override
    protected void convert(final BaseViewHolder helper, final MultipleItem item) {

        switch (helper.getItemViewType()) {
            case MultipleItem.MASONRYPOST:
                helper.setText(R.id.masonry_item_textContent, item.getShareAbb().getContent().replace("\\n", "\t").replace("\\r","\r").replace("\\t","\t"))//缩略展示，将换行替换为空格，以展示更多内容
                        .setText(R.id.masonry_item_username, item.getShareAbb().getNickName())
                        .setText(R.id.masonry_item_num,String.valueOf(item.getShareAbb().getLikeNumbers()));

                Glide.with(getContext())
                        .load(item.getShareAbb().getHeadPortraitUrl())
                        .asBitmap()
                        .placeholder(R.drawable.headprotrait)
                        .error(R.drawable.ic_load_pic_error)
                        .into((RoundedImageView) helper.getView(R.id.masonry_item_portrait_img));

                helper.getView(R.id.masonry_item_post_img).setTag(R.id.masonry_item_post_img,item.getShareAbb().getImgUrls());
                Glide.with(getContext())
                        .load(item.getShareAbb().getImgUrls())
                        .error(R.drawable.ic_load_pic_error)
                        // 取消动画，防止第一次加载不出来
                        .dontAnimate()
                        .into(new SimpleTarget<GlideDrawable>() {
                            @Override
                            public void onResourceReady(GlideDrawable resource,GlideAnimation<? super GlideDrawable> glideAnimation) {
                                String tag = (String) helper.getView(R.id.masonry_item_post_img).getTag(R.id.masonry_item_post_img);
                                // 如果一样，显示图片
                                if (TextUtils.equals(item.getShareAbb().getImgUrls(), tag)) {
                                    helper.setImageDrawable(R.id.masonry_item_post_img,resource);
                                }
                            }
                        });

                break;
            case MultipleItem.USER:
                helper.setText(R.id.user_id, item.getUser().getNickName());
                Glide.with(getContext())
                        .load(item.getUser().getHeadPortraitUrl())
                        .placeholder(R.drawable.headprotrait)
                        .error(R.drawable.ic_load_pic_error)
                        .transform(new CenterCrop(getContext()), new GlideRadiusTransform(getContext(),15))
                        .into((ImageView) helper.getView(R.id.user_head));
            break;
            case MultipleItem.SHAREABB:
                helper.setText(R.id.users_id, item.getShareAbb().getNickName())
                        .setText(R.id.content, item.getShareAbb().getContent())
                        .setText(R.id.praises, item.getShareAbb().getLikeNumbers())
                        .setText(R.id.comments,item.getShareAbb().getCommentNumbers());
                if(item.getShareAbb().getRelations()==0||item.getShareAbb().getRelations()==2)helper.setText(R.id.share_follow,"关注");
                else{
                    helper.setText(R.id.share_follow ,"已关注");
                    helper.setBackgroundResource(R.id.share_follow,R.drawable.button_radius_green_stroke);
                }
                if(item.getShareAbb().isLike())helper.setImageResource(R.id.postlike,R.drawable.like_click);
                else helper.setImageResource(R.id.postlike,R.drawable.like);

                Glide.with(getContext())
                        .load(item.getShareAbb().getHeadPortraitUrl())
                        .asBitmap()
                        .placeholder(R.drawable.headprotrait)
                        .error(R.drawable.ic_load_pic_error)
                        .into((RoundedImageView)helper.getView(R.id.share_users_head));
                Glide.with(getContext()).load(item.getShareAbb().getImgUrls()).placeholder(R.drawable.ic_placeholder).into((ImageView) helper.getView(R.id.content_pics));
                break;

            case MultipleItem.ACTION:
                DecimalFormat decimalFormat = new DecimalFormat("00");
                helper.setText(R.id.item_movement_name, item.getAction().getActionName())
                        .setText(R.id.item_movement_duration, decimalFormat.format(item.getAction().getDuration()*item.getAction().getTotal()/item.getAction().getCount() /60)+"'"+decimalFormat.format(item.getAction().getDuration()*item.getAction().getTotal()/item.getAction().getCount() %60)+"\"");

                Glide.with(getContext())
                        .load(item.getAction().getActionImgs())
                        .placeholder(R.drawable.ic_placeholder)
                        .transform(new FitCenter(getContext()), new GlideRadiusTransform(getContext(),20))
                        .into((ImageView) helper.getView(R.id.item_movement_img));
                break;

            case MultipleItem.ADDIMAGE:
                //helper.setImageResource(R.id.community4_item_image, item.getAddimage().getImgUrl());
                break;
                /*
            case MultipleItem.NORMCOURSE:
                String item_text = item.getCourse().getDegree() + "·" + item.getCourse().getDuration() + "·" +item.getCourse().getHits() + "万人已参加";//后期需要完善
                helper.setText(R.id.course_item_norm_name, item.getCourse().getCourseName())
                        .setText(R.id.course_item_norm_text, item_text);
                Glide.with(getContext())
                        .load(item.getCourse().getBackgroundUrl())
                        .error(R.drawable.ic_load_pic_error)
                        .transform(new CenterCrop(getContext()), new GlideRoundTransform(getContext(),15))
                        .into((ImageView) helper.getView(R.id.course_item_norm_bgImg));
                break;

                 */
            case MultipleItem.SHAREFULL:
                helper.setText(R.id.item_post_full_username, item.getShareAbb().getNickName())
                        .setText(R.id.item_post_full_text, item.getShareAbb().getContent())
                        .setText(R.id.item_post_full_likenumber, item.getShareAbb().getLikeNumbers())
                        .setText(R.id.item_post_full_commentnumber,item.getShareAbb().getCommentNumbers());
                if(item.getShareAbb().isLike())helper.setImageResource(R.id.item_post_full_like,R.drawable.like_click);
                else helper.setImageResource(R.id.item_post_full_like,R.drawable.like);

                Glide.with(getContext())
                        .load(item.getShareAbb().getHeadPortraitUrl())
                        .asBitmap()
                        .placeholder(R.drawable.headprotrait)
                        .error(R.drawable.ic_load_pic_error)
                        .into((RoundedImageView)helper.getView(R.id.item_post_full_userhead));
                NineGridImageView<String> nineGridImageView = helper.getView(R.id.item_post_full_nine_grid);
                nineGridImageView.setAdapter(item.getNineGridAdapter());
                List<String> imgUrls = new ArrayList<>();
                imgUrls.add(item.getShareAbb().getImgUrls());
                imgUrls.add(item.getShareAbb().getImgUrls());
                nineGridImageView.setImagesData(imgUrls);//后期需要改接口返回图片为list
                break;
            case MultipleItem.COURSEFULL:
                helper.setText(R.id.item_course_name, item.getCourse().getCourseName())
                        .setText(R.id.item_course_tag, item.getCourse().getLabels());
                if(item.getCourse().getIsOnline()== CourseTag.TAG_ONLINE_NO){
                    helper.setText(R.id.item_course_online_state,getContext().getString(R.string.course_selection_tag_online_no));
                }else{
                    helper.setText(R.id.item_course_online_state,null);
                }
                Glide.with(getContext())
                        .load(item.getCourse().getBackgroundUrl())
                        .error(R.drawable.ic_load_pic_error)
                        .into((ImageView) helper.getView(R.id.item_course_img));
                break;
            case MultipleItem.Me_mycourse_history:
                helper.setText(R.id.meMycourseHistoryName1,item.getMe_mycourse_history().getHistoryName1())
                        .setText(R.id.meMycourseHistoryName2,item.getMe_mycourse_history().getHistoryName2())
                        .setText(R.id.meMycourseHistoryName3,item.getMe_mycourse_history().getHistoryName3())
                        .setText(R.id.meMycourseHistoryTimes,item.getMe_mycourse_history().getHistoryTimes());
                Glide.with(getContext())
                        .load(item.getMe_mycourse_history().getImgUrls())
                        .placeholder(R.drawable.headprotrait)
                        .error(R.drawable.ic_load_pic_error)
                        .transform(new CenterCrop(getContext()), new GlideRadiusTransform(getContext(),15))
                        .into((ImageView) helper.getView(R.id.meMycourseHistoryImg));

                break;

            case MultipleItem.Me_mycourse_collections:
                helper.setText(R.id.meMycourseCollectionsName1,item.getMe_mycourse_collections().getCollectionsName1())
                        .setText(R.id.meMycourseCollectionsName2,item.getMe_mycourse_collections().getCollectionsName2())
                        .setText(R.id.meMycourseCollectionsName3,item.getMe_mycourse_collections().getCollectionsName3());
                Glide.with(getContext())
                        .load(item.getMe_mycourse_collections().getImgUrls())
                        .placeholder(R.drawable.headprotrait)
                        .error(R.drawable.ic_load_pic_error)
                        .transform(new CenterCrop(getContext()), new GlideRadiusTransform(getContext(),15))
                        .into((ImageView) helper.getView(R.id.meMycourseCollectionsImg));
                break;

            case MultipleItem.Me_mycourse_reserve:
                helper.setText(R.id.meMycourseReserveName1,item.getMe_mycourse_reserve().getReserveName1())
                        .setText(R.id.meMycourseReserveName2,item.getMe_mycourse_reserve().getReserveName2())
                        .setText(R.id.meMycourseReserveName3,item.getMe_mycourse_reserve().getReserveName3())
                        .setText(R.id.meMycourseReserveTime,item.getMe_mycourse_reserve().getReserveTime());
                Glide.with(getContext())
                        .load(item.getMe_mycourse_reserve().getImgUrls())
                        .placeholder(R.drawable.headprotrait)
                        .error(R.drawable.ic_load_pic_error)
                        .transform(new CenterCrop(getContext()), new GlideRadiusTransform(getContext(),15))
                        .into((ImageView) helper.getView(R.id.meMycourseReserveImg));
                break;

            case MultipleItem.Me_mycollections_articals:
                helper.setText(R.id.meMyCollectionArticalName,item.getMe_mycollections_articals().getArticalName())
                        .setText(R.id.meMyCollectionsArticalTime,item.getMe_mycollections_articals().getArticalTime())
                        .setText(R.id.meMyCollectionsArticalAuthor,item.getMe_mycollections_articals().getArticalAuthor());
                Glide.with(getContext())
                        .load(item.getMe_mycollections_articals().getImgUrls())
                        .placeholder(R.drawable.headprotrait)
                        .error(R.drawable.ic_load_pic_error)
                        .transform(new CenterCrop(getContext()), new GlideRadiusTransform(getContext(),15))
                        .into((ImageView) helper.getView(R.id.meMycollectionsArticalImg));
                break;
        }
    }

}