package com.example.motion.Utils;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.example.motion.Entity.Action;
import com.example.motion.Entity.Action_Table;
import com.example.motion.Entity.Course;
import com.example.motion.Entity.Course_Table;
import com.example.motion.R;
import com.example.motion.Widget.CourseDownloadDialog;
import com.lijunhuayc.downloader.downloader.DownloadProgressListener;
import com.lijunhuayc.downloader.downloader.DownloaderConfig;
import com.lijunhuayc.downloader.downloader.WolfDownloader;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseCacheUtil {
    private Context context;
    private File cachePathRoot;
    private List<Course> cachedCourseList;
    private OnActionProcessStateChangeListener stateChangeListener;
    private onCacheStateChangeListener cacheStateChangeListener;
    private int allFileSize;//byte
    private int[] nowDownloadedSizes;//byte
    private CourseDownloadDialog downloadDialog;


    public CourseCacheUtil(Context context,File cachePathRoot) {
        this.context = context;
        this.cachePathRoot = cachePathRoot;
    }

    // 提供注册事件监听的方法
    public void setOnChangeListener(OnActionProcessStateChangeListener stateChangeListener) {
        this.stateChangeListener = stateChangeListener;
    }

    public void setOnCacheChangeListener(onCacheStateChangeListener cacheStateChangeListener) {
        this.cacheStateChangeListener = cacheStateChangeListener;
    }

    public void onActionsProcessDone(boolean isSuccess, List<Action> processedActionList) {
        stateChangeListener.onActionsProcessDone(isSuccess,processedActionList);
    }

    public void processActions(List<Action> actionList){

        boolean isActionsSaveSuccess = false;
        ArrayList<Integer> needCacheActionIds = new ArrayList<>();

        for(int i=0;i<actionList.size();i++){
            if(isActionExistLocal(actionList.get(i).getActionID())){
                actionList.get(i).setActionLocUrl(getCachedAction(actionList.get(i).getActionID()).getActionLocUrl());
            }else {
                needCacheActionIds.add(i);
            }
        }

        if(needCacheActionIds.isEmpty()){
            onActionsProcessDone(true,actionList);
        }else{
            cacheActions(actionList,needCacheActionIds, new onCacheStateChangeListener() {
                @Override
                public void onActionsCacheDone(boolean isSuccess, List<Action> cachedRawActionList) {
                    if(isSuccess){
                        onActionsProcessDone(true,cachedRawActionList);
                    }else{
                        Log.d("onActionsCacheDone","isSuccess == failed");
                    }
                }
            });
        }

    }

    public Course getCachedCourse(Long courseId){
        Course course = new SQLite().select().from(Course.class).where(Course_Table.courseId.eq(courseId)).querySingle();
        return course;
    }

    public void cacheActions(List<Action> rawActionList,ArrayList<Integer> needCachePositions,onCacheStateChangeListener cacheStateChangeListener){

        Map<Long,Boolean> downloadSuccessList = new HashMap<>();
        View downloadDialogView = View.inflate(context, R.layout.sport_dialog_download_default,null);
        downloadDialog = new CourseDownloadDialog(context,downloadDialogView);

        nowDownloadedSizes = new int[needCachePositions.size()];

        for(int i=0;i<needCachePositions.size();i++){
            rawActionList.get(needCachePositions.get(i));
            int finalI = i;

            WolfDownloader wolfDownloader = new DownloaderConfig()
                    .setThreadNum(3)
                    .setDownloadUrl(rawActionList.get(needCachePositions.get(i)).getActionUrl())
                    .setSaveDir(new File(cachePathRoot.toString() + "/ActionVideos"))
                    .setFileName(String.valueOf(rawActionList.get(needCachePositions.get(i)).getActionID())+".mp4")
                    .setDownloadListener(new DownloadProgressListener() {
                        @Override
                        public void onDownloadTotalSize(int totalSize) {
                            Log.d("onDownloadTotalSize","位置："+needCachePositions.get(finalI)+" totalSize:"+totalSize);
                            allFileSize += totalSize;
                        }

                        @Override
                        public void updateDownloadProgress(int size, float percent, float speed) {
                            int nowDownloadedSize = 0;
                            nowDownloadedSizes[finalI] = size;
                            Log.d("updateDownloadProgress","位置："+needCachePositions.get(finalI)+" size：" + size);

                            for(int j=0;j<nowDownloadedSizes.length;j++){
                                Log.d("updateDownloadProgress","for looping, j="+j+"nowDownloadedSizes[j]="+nowDownloadedSizes[j]);
                                nowDownloadedSize += nowDownloadedSizes[j];
                            }

                            Log.d("updateDownloadProgress","nowDownloadedSize:"+nowDownloadedSize);

                            downloadDialog.setProgress(nowDownloadedSize);
                            downloadDialog.setProgressMax(allFileSize);
                            downloadDialog.setProgressText(nowDownloadedSize,allFileSize);
                        }

                        @Override
                        public void onDownloadSuccess(String apkPath) {
                            rawActionList.get(finalI).setActionLocUrl(cachePathRoot.toString() + "/ActionVideos/"+ rawActionList.get(finalI).getActionID()+".mp4");

                            rawActionList.get(finalI).save();

                            downloadSuccessList.put(rawActionList.get(needCachePositions.get(finalI)).getActionID(),true);
                            if(downloadSuccessList.size() == needCachePositions.size()){
                                cacheStateChangeListener.onActionsCacheDone(true,rawActionList);
                                downloadDialog.dismiss();

                                onStopDownload();
                            }

                        }

                        @Override
                        public void onDownloadFailed() {

                        }

                        @Override
                        public void onPauseDownload() {

                        }

                        @Override
                        public void onStopDownload() {

                        }
                    })
                    .buildWolf(context);
            wolfDownloader.startDownload();
        }

        downloadDialog.show();

    }

    public Action getCachedAction(Long actionId){
        Action action = new SQLite().select().from(Action.class).where(Action_Table.actionID.eq(actionId)).querySingle();
        return action;
    }

    public boolean isActionExistLocal(Long actionId){
        Action action = getCachedAction(actionId);
        if(null != action){
            Log.d("isActionExistLocal","id:"+action.getActionID()+" IS exist in Database");
            if(isFileIsExists(action.getActionLocUrl())){
                Log.d("isActionExistLocal","id:"+action.getActionID()+"url:"+action.getActionUrl()+" file exist");
                return true;
            }else{
                Log.d("isActionExistLocal","id:"+action.getActionID()+"url:"+action.getActionUrl()+" file DO NOT exist");
                return false;
            }

        }else{
            Log.d("isActionExistLocal","id:"+actionId+" is NOT exist in Database");
            return false;
        }
    }

    public boolean isFileIsExists(String filePathStr)
    {
        try
        {
            File f=new File(filePathStr);
            if(!f.exists())
            {
                return false;
            }

        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    public boolean isCourseExistLocal(Long courseId){
        if(null != getCachedCourse(courseId)){
            return true;
        }else{
            return false;
        }
    }

    //未完善
    public List<Course> getCachedCourseList() {
        cachedCourseList = new ArrayList<>();
        cachedCourseList.addAll(new SQLite().select().from(Course.class).queryList());
        return cachedCourseList;
    }


    //未完善
    public List<Action> getCachedActionList(Long[] actionIds){
        List<Action> actionList = new ArrayList<>();
        for(int i = 0;i<actionIds.length;i++){
            actionList.add(new SQLite().select().from(Action.class).where(Action_Table.actionID.eq(actionIds[i])).querySingle());
        }
        return actionList;
    }


    interface onCacheStateChangeListener {
        void onActionsCacheDone(boolean isSuccess,List<Action> cachedActionList);
    }


}

