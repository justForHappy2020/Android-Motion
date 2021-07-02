package com.example.motion.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;

import com.example.motion.Entity.Action;
import com.example.motion.Entity.Action_Table;
import com.example.motion.Entity.Course;
import com.example.motion.Entity.Course_Action;
import com.example.motion.Entity.Course_Action_Table;
import com.example.motion.Entity.Course_Table;
import com.example.motion.R;
import com.lijunhuayc.downloader.downloader.DownloadProgressListener;
import com.lijunhuayc.downloader.downloader.DownloaderConfig;
import com.lijunhuayc.downloader.downloader.WolfDownloader;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseCacheUtil {
    private Context context;
    private File cachePathRoot;
    private List<Course> cachedCourseList;
    private OnProcessStateChangeListener stateChangeListener;
    private onCacheStateChangeListener cacheStateChangeListener;
    private int allFileSize;//byte
    private int[] nowDownloadedSizes;//byte size of all downloading tasks
    private CourseDownloadDialog downloadDialog;
    private ArrayList<WolfDownloader> downloaderList;

    public CourseCacheUtil(Context context,File cachePathRoot) {
        this.context = context;
        this.cachePathRoot = cachePathRoot;
    }

    // 提供注册事件监听的方法,用来触发监听器事件
    public void setOnChangeListener(OnProcessStateChangeListener stateChangeListener) {
        this.stateChangeListener = stateChangeListener;
    }

    public void setOnCacheChangeListener(onCacheStateChangeListener cacheStateChangeListener) {
        this.cacheStateChangeListener = cacheStateChangeListener;
    }

    public void onActionsProcessDone(boolean isSuccess, List<Action> processedActionList) {
        //stateChangeListener.onActionsProcessDone(isSuccess,processedActionList);
    }

    public void onProcessDone(boolean isSuccess,Course course, List<Action> processedActionList,Object message){
        stateChangeListener.onProcessDone(isSuccess,course,message);
    }

    public void process(Course course,List<Action> actionList){

        //boolean isActionsSaveSuccess = false;
        ArrayList<Integer> needCacheActionPositions = new ArrayList<>();

        for(int i=0;i<actionList.size();i++){
            String filePath = cachePathRoot.toString() + "/ActionVideos/"+actionList.get(i).getActionID().toString()+".mp4";
            if(actionVideoFileAuth(filePath,actionList.get(i)) && null != new SQLite().select().from(Action.class).where(Action_Table.actionID.eq(actionList.get(i).getActionID())).querySingle()){
                Course_Action ca = new Course_Action(course,actionList.get(i));
                if(new Select().from(Course_Action.class).where(Course_Action_Table.course_courseId.eq(course.getCourseId()),Course_Action_Table.action_actionID.eq(actionList.get(i).getActionID())).querySingle() == null){
                    Log.d("CourseCacheUtil","ca.exists false");
                    ca.save();
                }

                actionList.get(i).setActionLocUrl(filePath);//getCachedAction(actionList.get(i).getActionID()).getActionLocUrl()
                //actionList.get(i).save();
            }else {
                needCacheActionPositions.add(i);
            }
        }

        if(needCacheActionPositions.isEmpty()){
            course.save();
            course.getActionList();
            onProcessDone(true,course,actionList,"SUCCESS");
        }else{
            cacheActions(course,actionList,needCacheActionPositions, new onCacheStateChangeListener() {
                @Override
                public void onActionsCacheDone(boolean isSuccess, List<Action> cachedRawActionList,Object message) {
                    onProcessDone(isSuccess,course,cachedRawActionList,message);
                    if(isSuccess){
                        Log.d("CourseCacheUtil","process onActionsCacheDone true");
                        course.save();
                    }else{
                        Log.d("CourseCacheUtil","process onActionsCacheDone false");
                        course.delete();
                    }
                }
            });
        }
    }

    /*
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
            onProcessDone(true,);
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
     */

    public Course findCachedCourseById(Long courseId){
        Course course = new SQLite().select().from(Course.class).where(Course_Table.courseId.eq(courseId)).querySingle();
        return course;
    }

    public void cacheActions(Course course,List<Action> rawActionList,ArrayList<Integer> needCachePositions,onCacheStateChangeListener cacheStateChangeListener){
        downloaderList = new ArrayList<>();

        Log.d("cacheActions","rawActionList.size="+rawActionList.size());
        Log.d("cacheActions","needCachePositions.size="+needCachePositions.size());

        Map<Long,Boolean> downloadSuccessIdList = new HashMap<>();
        View downloadDialogView = View.inflate(context, R.layout.sport_dialog_download_default,null);
        downloadDialog = new CourseDownloadDialog(context,downloadDialogView);

        downloadDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if(downloadSuccessIdList.size() < needCachePositions.size()){
                    for(int i=0;i<downloaderList.size();i++){
                        downloaderList.get(i).stopDownload();
                        downloaderList.get(i).exitDownload();
                    }
                }

            }
        });

        nowDownloadedSizes = new int[needCachePositions.size()];

        for(int i=0;i<needCachePositions.size();i++){
            final Long actionId = rawActionList.get(needCachePositions.get(i)).getActionID();
            int finalI = i;

            String filePath = cachePathRoot.toString() + "/ActionVideos/"+rawActionList.get(needCachePositions.get(i)).getActionID().toString()+".mp4";

            rawActionList.get(needCachePositions.get(i)).delete();

            WolfDownloader wolfDownloader = new DownloaderConfig()
                    .setThreadNum(1)
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
                            Log.d("CourseCacheUtil","onDownloadSuccess! actionId:"+actionId);

                            rawActionList.get(needCachePositions.get(finalI)).setActionLocUrl(cachePathRoot.toString() + "/ActionVideos/"+ actionId+".mp4");
                            rawActionList.get(needCachePositions.get(finalI)).save();
                            Course_Action ca = new Course_Action(course,rawActionList.get(needCachePositions.get(finalI)));
                            if(new Select().from(Course_Action.class).where(Course_Action_Table.course_courseId.eq(course.getCourseId()),Course_Action_Table.action_actionID.eq(rawActionList.get(finalI).getActionID())).querySingle() == null){
                                Log.d("CourseCacheUtil","ca.exists false");
                                ca.save();
                            }

                            downloadSuccessIdList.put(actionId,true);

                            if(downloadSuccessIdList.size() == needCachePositions.size()){
                                Log.d("CourseCacheUtil","onAllDownloadFinish! ");

                                //size checking here
                                if(actionsVideoFileSizeAuth(rawActionList)){
                                    course.getActionList();
                                    cacheStateChangeListener.onActionsCacheDone(true,rawActionList,"success");
                                }else{
                                    cacheStateChangeListener.onActionsCacheDone(false,rawActionList,"failed reason:file doesn't exist or size smaller than json say");
                                }

                                downloadDialog.dismiss();
                                onStopDownload();
                            }
                        }

                        @Override
                        public void onDownloadFailed() {
                            Log.d("CourseCacheUtil","onDownloadFailed! actionId:"+actionId);
                            cacheStateChangeListener.onActionsCacheDone(false,rawActionList,"failed reason:file download failed,actionId:"+actionId);
                        }

                        @Override
                        public void onPauseDownload() {

                        }

                        @Override
                        public void onStopDownload() {
                            //cacheStateChangeListener.onActionsCacheDone(false,rawActionList,"exit");
                            Log.d("CourseCacheUtil","onStopDownload! actionId:"+actionId);
                            //cacheStateChangeListener.onActionsCacheDone(false,rawActionList);
                        }
                    })
                    .buildWolf(context);
            downloaderList.add(wolfDownloader);
            wolfDownloader.startDownload();
        }
        //}

        downloadDialog.show();

    }

    private boolean actionsVideoFileSizeAuth(List<Action> list){
        for(int i=0;i<list.size();i++){
            Log.d("CourseCacheUtil","locUrl:"+list.get(i).getActionLocUrl()+" getSizeByte:"+list.get(i).getSizeByte());
            if(!actionVideoFileAuth(list.get(i).getActionLocUrl(),list.get(i))){
                return false;
            }
        }
        return true;
    }

    private boolean actionVideoFileAuth(String filePath,Action action){
        try {
            File f = new File(filePath);
            if(!f.exists()||!f.isFile()) {
                Log.d("CourseCacheUtil","actionId:"+action.getActionID()+ "!f.exists()||!f.isFile()");
                return false;
            }else{
                long Byte = f.length();

                if(Byte<action.getSizeByte()){
                    Log.d("CourseCacheUtil","actionId:"+action.getActionID()+"Byte<action.getSizeByte()->"+"file Byte = "+Byte+" json getSizeByte:"+action.getSizeByte());
                    return false;
                }
            }
        }
        catch (Exception e) {
            Log.d("CourseCacheUtil","actionId:"+action.getActionID()+"filePath="+filePath+ e.toString());
            return false;
        }
        return true;
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

    public boolean isFileIsExists(String filePathStr) {
        try
        {
            File f = new File(filePathStr);
            if(!f.exists()||!f.isFile())
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
        if(null != findCachedCourseById(courseId)){
            return true;
        }else{
            return false;
        }
    }

    public List<Course> getAllCachedCourseList() {
        cachedCourseList = new ArrayList<>();
        cachedCourseList.addAll(new SQLite().select().from(Course.class).queryList());
        return cachedCourseList;
    }

    interface onCacheStateChangeListener {
        void onActionsCacheDone(boolean isSuccess,List<Action> cachedActionList,Object message);
    }


}
