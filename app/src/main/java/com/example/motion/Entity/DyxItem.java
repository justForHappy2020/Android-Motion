package com.example.motion.Entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class DyxItem implements MultiItemEntity {
    /**
     * 类名：MultipleItem
     * 功能：在构造时根据Share、User、Course、Comment或Movement等实体类生成，相当于一个规范的外包装，利于装入RecyclerView，实现多Item布局、多功能适配器等等
     */
    public static final int SELECTBTN1 = 1;
    public static final int SELECTBTN2 = 2;
    public static final int SELECTCOURSE = 3;
    public static final int PORTRAIT = 4;
    public static final int HEALTHRECORD = 5;



    private int itemType;
    private Course course;
    private ShareAbb shareAbb;
    private User user;
    private Member member;
    private String text;
    private Action action;
    private AddImage addimage;
    private NineGridImageViewAdapter<String> nineGridAdapter;
    private List<DyxItem> itemList2 = new ArrayList();
    private HealthRecord healthRecord;

    public DyxItem(int itemType, HealthRecord healthRecord) {
        this.itemType = itemType;
        this.healthRecord = healthRecord;
    }

    public DyxItem(int itemType, List<DyxItem> itemList2) {
        this.itemType = itemType;
        this.itemList2 = itemList2;
    }

    public DyxItem(int itemType, String text) {
        this.itemType = itemType;
        this.text = text;
    }

    public DyxItem(int itemType, ShareAbb shareAbb) {
        this.itemType = itemType;
        this.shareAbb = shareAbb;
    }

    public DyxItem(int itemType, Course course) {
        this.itemType = itemType;
        this.course=course;
    }
    public DyxItem(int itemType, User user){
        this.itemType = itemType;
        this.user = user;
    }

    public DyxItem(int itemType, Action action){
        this.itemType = itemType;
        this.action = action;
    }

    public DyxItem(int itemType, AddImage addimage) {
        this.itemType = itemType;
        this.addimage = addimage;
    }

    public DyxItem(int itemType, Member member) {
        this.itemType = itemType;
        this.member = member;
    }

    public DyxItem(int itemType, ShareAbb shareAbb, NineGridImageViewAdapter<String> nineGridAdapter) {
        this.itemType = itemType;
        this.shareAbb = shareAbb;
        this.nineGridAdapter = nineGridAdapter;
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

    public AddImage getAddimage() { return addimage;}

    public NineGridImageViewAdapter<String> getNineGridAdapter(){
        return nineGridAdapter;
    }

    public List<DyxItem> getItemList2() {
        return itemList2;
    }

    public HealthRecord getHealthRecord() {
        return healthRecord;
    }

    public Member getMember() {
        return member;
    }
}