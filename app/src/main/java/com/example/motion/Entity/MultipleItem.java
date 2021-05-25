package com.example.motion.Entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;

public class MultipleItem implements MultiItemEntity {
    /**
     * 类名：MultipleItem
     * 功能：在构造时根据Share、User、Course、Comment或Movement等实体类生成，相当于一个规范的外包装，利于装入RecyclerView，实现多Item布局、多功能适配器等等
     */
    public static final int TEXTONLY = 1;
    public static final int BUTTON = 2;
    public static final int MINICOURSE = 3;
    public static final int MASONRYPOST = 4;
    public static final int USER = 5;
    public static final int SHAREABB = 6;
    public static final int ACTION = 7;
    public static final int ADDIMAGE = 8;
    public static final int SHAREFULL = 9;
    public static final int COURSEFULL = 10;

    public static final int Me_mycourse_history = 11;
    public static final int Me_mycourse_collections = 12;
    public static final int Me_mycourse_reserve = 13;
    public static final int Me_mycollections_articals = 14;


    private int itemType;
    private Course course;
    private ShareAbb shareAbb;
    private User user;
    private String text;
    private Action action;
    private NineGridImageViewAdapter<String> nineGridAdapter;
    private me_mycourse_history me_mycourse_history;
    private me_mycourse_collections me_mycourse_collections;
    private me_mycourse_reserve me_mycourse_reserve;
    private me_mycollections_articals me_mycollections_articals;

    public MultipleItem(int itemType,String text) {
        this.itemType = itemType;
        this.text = text;
    }

    public MultipleItem(int itemType, ShareAbb shareAbb) {
        this.itemType = itemType;
        this.shareAbb = shareAbb;
    }

    public MultipleItem(int itemType,Course course) {
        this.itemType = itemType;
        this.course=course;
    }
    public MultipleItem(int itemType, User user){
        this.itemType = itemType;
        this.user = user;
    }

    public MultipleItem(int itemType, Action action){
        this.itemType = itemType;
        this.action = action;
    }


    public MultipleItem(int itemType, ShareAbb shareAbb, NineGridImageViewAdapter<String> nineGridAdapter) {
        this.itemType = itemType;
        this.shareAbb = shareAbb;
        this.nineGridAdapter = nineGridAdapter;
    }
    public MultipleItem(int itemType, me_mycourse_history me_mycourse_history){
        this.itemType = itemType;
        this.me_mycourse_history = me_mycourse_history;
    }
    public MultipleItem(int itemType,me_mycourse_collections me_mycourse_collections){
        this.itemType = itemType;
        this.me_mycourse_collections = me_mycourse_collections;
    }
    public MultipleItem(int itemType,me_mycourse_reserve me_mycourse_reserve){
        this.itemType = itemType;
        this.me_mycourse_reserve = me_mycourse_reserve;
    }
    public MultipleItem(int itemType,me_mycollections_articals me_mycollections_articals){
        this.itemType = itemType;
        this.me_mycollections_articals = me_mycollections_articals;
    }
    @Override
    public int getItemType() {
        return itemType;
    }

    public String getText(){
        return text;
    }

    public Course getCourse(){
        return course;
    }

    public ShareAbb getShareAbb(){
        return shareAbb;
    }

    public User getUser() {
        return user;
    }


    public Action getAction(){
        return action;
    }

    public NineGridImageViewAdapter<String> getNineGridAdapter(){
        return nineGridAdapter;
    }

    public com.example.motion.Entity.me_mycollections_articals getMe_mycollections_articals() {
        return me_mycollections_articals;
    }

    public com.example.motion.Entity.me_mycourse_reserve getMe_mycourse_reserve() {
        return me_mycourse_reserve;
    }

    public com.example.motion.Entity.me_mycourse_collections getMe_mycourse_collections() {
        return me_mycourse_collections;
    }

    public me_mycourse_history getMe_mycourse_history() {
        return me_mycourse_history;
    }
}