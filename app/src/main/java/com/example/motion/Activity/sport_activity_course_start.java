package com.example.motion.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.motion.Entity.Action;
import com.example.motion.Entity.Course;
import com.example.motion.R;
import com.example.motion.Utils.HttpUtils;
import com.example.motion.Widget.SportBreakDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class sport_activity_course_start extends Activity implements View.OnClickListener{

    // Tag for the instance state bundle.
    private static final String PLAYBACK_TIME = "play_time";

    private final int LAST_ACTION = 1;
    private final int NEXT_VIDEO = 2;
    private final int NEXT_ACTION = 3;
    private final int COURSE_COMPLETED = 4;
    private final int BREAK_OVER = 5;
    public final int BREAK_SKIP = 6;

    private int totalTimeSeconds;
    private int actionTimeSeconds;
    private int actionCount;

    private int httpcode;

    private int width;// 屏幕宽度（像素）
    private int height;// 屏幕高度（像素）
    private int progressBarHeight;
    private Boolean isLandscape;


    private int courseActionPosition;
    private int nowPlayTimes = 0;//播放次数
    // Current playback position (in milliseconds).
    private int nowVideoPosition = 0;
    private int nowControlBarShownTime = 0;

    private String token;
    private Course course;
    private List<Action> actionList;

    private Handler handler;
    private DecimalFormat decimalFormat = new DecimalFormat("00");

    private VideoView mVideoView;
    private ProgressBar progressBar;
    private TextView mBufferingTextView;
    private TextView tvShowTime;
    private TextView tvActionName;
    private TextView tvCountOrTime;
    private ImageButton ibLast;
    private ImageButton ibStop;
    private ImageButton ibNext;
    private ImageButton ibQuit;
    private ImageButton ibLandscape;
    private Button btnDetail;
    private ConstraintLayout clRootContainer;
    private FrameLayout flTouchArea;
    private LinearLayout llPlayControlBar;
    private ConstraintSet constraintSet = new ConstraintSet();
    private View nextActionBar;

    private SportBreakDialog breakDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sport_activity_course_start);

        Intent intent = getIntent();
        course = (Course)intent.getSerializableExtra("courseWithActions");
        actionList = course.getActionList();

        courseActionPosition = intent.getIntExtra("courseActionPosition",0);

        if (savedInstanceState != null) {
            nowVideoPosition = savedInstanceState.getInt(PLAYBACK_TIME);
        }

        initHandler();
        initView();
        initData();

        initializePlayer();

        setVideoViewPosition();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("VIDEO_STATE","onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("VIDEO_STATE","onRestart,nowVideoPosition="+nowVideoPosition);
        if(nowVideoPosition>0){
            mVideoView.seekTo(nowVideoPosition);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.pause();
        nowVideoPosition = mVideoView.getCurrentPosition();
        Log.d("VIDEO_STATE","onPause,currentPosition="+mVideoView.getCurrentPosition());

        // In Android versions less than N (7.0, API 24), onPause() is the
        // end of the visual lifecycle of the app.  Pausing the video here
        // prevents the sound from continuing to play even after the app
        // disappears.
        //
        // This is not a problem for more recent versions of Android because
        // onStop() is now the end of the visual lifecycle, and that is where
        // most of the app teardown should take place.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mVideoView.pause();

            Log.d("VIDEO_STATE","videoPaused,now currentPosition="+mVideoView.getCurrentPosition());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("VIDEO_STATE","onStop");
        // Media playback takes a lot of resources, so everything should be
        // stopped and released at this time.

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("VIDEO_STATE","onDestroy");
        releasePlayer();
    }

    @Override
    public void onBackPressed() {
        Log.d("VIDEO_STATE","onBackPressed");

        //mVideoView.pause();
        //releasePlayer();
        //finish();
        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the current playback position (in milliseconds) to the
        // instance state bundle.
        outState.putInt(PLAYBACK_TIME, nowVideoPosition);

        Log.d("VIDEO_STATE","onSaveInstanceState,nowVideoPosition="+nowVideoPosition);
    }

    private void initHandler() {
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case LAST_ACTION:
                        if(courseActionPosition>=1){
                            courseActionPosition--;
                            tvActionName.setText(actionList.get(courseActionPosition).getActionName());
                            initCountOrTimeTv(actionList.get(courseActionPosition),tvCountOrTime);
                            mVideoView.setVideoURI(Uri.parse(actionList.get(courseActionPosition).getActionLocUrl()));
                        }
                        break;

                    case BREAK_OVER:
                    case BREAK_SKIP:
                    case NEXT_ACTION:
                        courseActionPosition++;
                        nowPlayTimes = 0;
                        mVideoView.seekTo(0);
                        progressBar.setProgress(0);
                        if(courseActionPosition<actionList.size()){
                            tvActionName.setText(actionList.get(courseActionPosition).getActionName());
                            initCountOrTimeTv(actionList.get(courseActionPosition),tvCountOrTime);
                        }else{
                            progressBar.setMax(progressBar.getMax());
                            Intent intent = new Intent(getBaseContext(),sport_activity_course_completed.class);
                            intent.putExtra("timeSeconds", totalTimeSeconds);
                            intent.putExtra("course",course);
                            startActivity(intent);
                            finish();
                            break;
                        }
                    case NEXT_VIDEO:
                        mVideoView.setVideoURI(Uri.parse(actionList.get(courseActionPosition).getActionLocUrl()));

                        break;

                    case COURSE_COMPLETED:
                        Intent intent = new Intent(getBaseContext(),sport_activity_course_completed.class);
                        startActivity(intent);
                        finish();
                        break;
                }
            }
        };

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(mVideoView.isPlaying() && courseActionPosition < actionList.size()){
                            Log.d("VIDEO","WindowHasFoucs?"+String.valueOf(hasWindowFocus()));
                            Log.d("VIDEO","VideoHasFoucs?"+String.valueOf(mVideoView.hasFocus()));
                            //Log.d("VIDEO_STATE","mVideoView.getCurrentPosition()="+mVideoView.getCurrentPosition());

                            if(llPlayControlBar.getVisibility() == View.VISIBLE && nowControlBarShownTime>3){
                                //llPlayControlBar.setVisibility(View.INVISIBLE);
                            }else if(nowControlBarShownTime<3){
                                //nowControlBarShownTime++;
                            }

                            progressBar.setProgress(nowPlayTimes*mVideoView.getDuration()+mVideoView.getCurrentPosition());
                            totalTimeSeconds++;
                            tvShowTime.setText(decimalFormat.format(totalTimeSeconds /60)+":"+decimalFormat.format(totalTimeSeconds %60));
                            increaseActionCountOrTime(actionList.get(courseActionPosition),tvCountOrTime);
                        }
                       }
                });
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    private void initCountOrTimeTv(Action currentAction,TextView tvCountOrTime){
        actionTimeSeconds = 0;
        actionCount = 0;
        switch (currentAction.getType()){
            case 1:
                tvCountOrTime.setText("0/" + currentAction.getTotal());
                break;
            case 2:
                tvCountOrTime.setText("0/" + currentAction.getDuration() /60+"'"+currentAction.getDuration() %60+"\"");
                break;
        }
    }

    private void increaseActionCountOrTime(Action currentAction,TextView tvCountOrTime){
        actionTimeSeconds++;
        switch (currentAction.getType()){
            case 1:
                int durationOfOneMove = currentAction.getDuration()/currentAction.getCount();

                //Log.d("increaseActionCountOrTime_durationOfOneMove",String.valueOf(durationOfOneMove));
                //Log.d("increaseActionCountOrTime_getCurrentPosition",String.valueOf(durationOfOneMove));

                if(actionTimeSeconds%durationOfOneMove == 0 && actionCount<=currentAction.getTotal()){
                    tvCountOrTime.setText(++actionCount + "/" + currentAction.getTotal());
                }

                break;
            case 2:
                if(actionTimeSeconds<=currentAction.getDuration()){
                    tvCountOrTime.setText(actionTimeSeconds + "/" + currentAction.getDuration() /60 + "'"+currentAction.getDuration() %60 + "\"");
                }

                break;
        }
    }

    private void initView(){
        /*
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels;     // 屏幕宽度（像素）
        height = metric.heightPixels;   // 屏幕高度（像素）
         */

        flTouchArea = findViewById(R.id.fl_touch_area);
        mVideoView = findViewById(R.id.video_view);
        mBufferingTextView = findViewById(R.id.tv_buffering);
        progressBar = findViewById(R.id.progressBar);
        tvShowTime = findViewById(R.id.tv_showtime);
        tvActionName = findViewById(R.id.tv_action_name);
        tvCountOrTime = findViewById(R.id.tv_action_count_or_time);
        ibLast = findViewById(R.id.last);
        ibStop = findViewById(R.id.stop);
        ibNext = findViewById(R.id.next);
        btnDetail = findViewById(R.id.btn_detail);
        ibQuit = findViewById(R.id.ib_quit);
        ibLandscape = findViewById(R.id.ib_landscape);

        clRootContainer = findViewById(R.id.constraintLayout);
        llPlayControlBar = findViewById(R.id.ll_play_control_bar);
        nextActionBar = getLayoutInflater().inflate(R.layout.sport_include_course_start_info_bar,null);

        ibLast.setOnClickListener(this);
        ibStop.setOnClickListener(this);
        ibNext.setOnClickListener(this);
        btnDetail.setOnClickListener(this);
        ibQuit.setOnClickListener(this);
        ibLandscape.setOnClickListener(this);
        flTouchArea.setOnClickListener(this);



        tvActionName.setText(actionList.get(courseActionPosition).getActionName());
        initCountOrTimeTv(actionList.get(courseActionPosition),tvCountOrTime);

        // Set up the media controller widget and attach it to the video view.
        MediaController controller = new MediaController(this);
        controller.setMediaPlayer(mVideoView);
        controller.setVisibility(View.GONE);
        mVideoView.setMediaController(controller);
        //使用post是由于会使用mPortraitPosition的宽高信息
        mVideoView.post(new Runnable() {
            @Override
            public void run() {
                onStart();
            }
        });
    }

    private void initData(){
        breakDialog = new SportBreakDialog(this);
        progressBarHeight =   new ConstraintLayout.LayoutParams(progressBar.getLayoutParams()).height;

    }

    //记录播放记录http
    private void savedPlayRecord(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://159.75.2.94:8080/api/course/playTheVideo?token=" + token + "&&courseId=" + actionList.get(courseActionPosition).getActionID();
                String responseData = null;
                try {
                    responseData = HttpUtils.connectHttpGet(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(responseData);
                    httpcode = jsonObject1.getInt("code");
                    if (httpcode == 200) {
                        Log.d("test", "successful");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(httpcode!=200) Toast.makeText(sport_activity_course_start.this,"ERROR", Toast.LENGTH_SHORT).show();
    }


    private void initializePlayer() {
        // Show the "Buffering..." message while the video loads.
        mBufferingTextView.setVisibility(VideoView.VISIBLE);

        // Buffer and decode the video sample.
        Uri videoUri = Uri.parse(actionList.get(courseActionPosition).getActionLocUrl());

        mVideoView.setVideoURI(videoUri);
        //mVideoView.setAutofillId();
        //mVideoView.setClipToOutline(true);

        // Listener for onPrepared() event (runs after the media is prepared).
        mVideoView.setOnPreparedListener(
                new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        int shouldPlayTimes = (actionList.get(courseActionPosition).getTotal()/actionList.get(courseActionPosition).getCount());
                        if(shouldPlayTimes<=0){
                            shouldPlayTimes = actionList.get(courseActionPosition).getTotal();
                        }
                        int max = mVideoView.getDuration()*shouldPlayTimes;
                        Log.d("mVideoView","onPrepared,max="+max);
                        Log.d("mVideoView","mVideoView.getDuration()="+mVideoView.getDuration());
                        Log.d("mVideoView","actionList.get(courseActionPosition).getTotal()="+actionList.get(courseActionPosition).getTotal());
                        Log.d("mVideoView","actionList.get(courseActionPosition).getCount()="+actionList.get(courseActionPosition).getCount());


                        progressBar.setMax(max);

                        // Hide buffering message.
                        mBufferingTextView.setVisibility(VideoView.INVISIBLE);
                        //改变视频的大小和位置
                        //changeToPortraitLayout();
                        // Start playing!
                        mVideoView.start();
                    }
                });

        // Listener for onCompletion() event (runs after media has finished
        // playing).
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        nowPlayTimes++;
                        Message msg = new Message();
                        if(nowPlayTimes >= actionList.get(courseActionPosition).getTotal()/actionList.get(courseActionPosition).getCount()){
                            //如果有休息就打开休息dialog
                            if(actionList.get(courseActionPosition).getRestDuration()>0){
                                breakDialog.setBreakSeconds(actionList.get(courseActionPosition).getRestDuration());
                                breakDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {
                                        Message dismissMsg = new Message();
                                        dismissMsg.what = NEXT_ACTION;
                                        dismissMsg.arg1 = actionList.get(courseActionPosition).getRestDuration() - breakDialog.getBreakSeconds();//arg1为实际休息的时长
                                        handler.handleMessage(dismissMsg);
                                    }
                                });
                                breakDialog.show();
                            }else{
                                Log.d("SportBreakDialog","notShowing, getRestDuration "+actionList.get(courseActionPosition).getRestDuration());
                                //无则NEXT_ACTION
                                msg.what = NEXT_ACTION;
                            }

                        }else{
                            msg.what = NEXT_VIDEO;
                        }
                        handler.handleMessage(msg);
                    }
                });
    }

    private void setVideoViewPosition() {
        //判断当前横竖屏配置
        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_LANDSCAPE: {//横屏

                Log.d("VideoPlay","横屏");
                changeToLandscapeLayout();
                break;
            }
            case Configuration.ORIENTATION_PORTRAIT:{

                Log.d("VideoPlay","竖屏");
                changeToPortraitLayout();
                break;
            }
            default: {//竖屏

                Log.d("VideoPlay","default 竖屏");
                changeToPortraitLayout();
                break;
            }
        }

    }

    /**
     * 屏幕方向改变时被调用
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        setVideoViewPosition();
        super.onConfigurationChanged(newConfig);
    }

    //横屏
    private void changeToLandscapeLayout() {
        isLandscape = true;
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        width =  metric.heightPixels;   // 屏幕宽度（像素）
        height = metric.widthPixels;    // 屏幕高度（像素）

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(progressBar.getLayoutParams());
        params.height = 20;
        progressBar.setLayoutParams(params);

        ConstraintSet cs = new ConstraintSet();
        //获取当前目标控件的约束集合
        cs.clone(clRootContainer);

        //修改mVideoView约束
        //清除约束
        cs.clear(mVideoView.getId());
        //cs.clear(llPlayControlBar.getId());
        cs.clear(mBufferingTextView.getId());
        cs.connect(mVideoView.getId(), ConstraintSet.LEFT, clRootContainer.getId(), ConstraintSet.LEFT);
        cs.connect(mVideoView.getId(), ConstraintSet.TOP, clRootContainer.getId(), ConstraintSet.TOP);
        cs.connect(mVideoView.getId(), ConstraintSet.RIGHT, clRootContainer.getId(), ConstraintSet.RIGHT);
        cs.connect(mVideoView.getId(), ConstraintSet.BOTTOM, clRootContainer.getId(), ConstraintSet.BOTTOM);

        cs.connect(llPlayControlBar.getId(),ConstraintSet.LEFT,clRootContainer.getId(),ConstraintSet.LEFT);
        cs.connect(llPlayControlBar.getId(), ConstraintSet.TOP, clRootContainer.getId(), ConstraintSet.TOP);
        cs.connect(llPlayControlBar.getId(), ConstraintSet.RIGHT, clRootContainer.getId(), ConstraintSet.RIGHT);
        cs.connect(llPlayControlBar.getId(), ConstraintSet.BOTTOM, clRootContainer.getId(), ConstraintSet.BOTTOM);


        cs.connect(progressBar.getId(),ConstraintSet.LEFT,clRootContainer.getId(),ConstraintSet.LEFT);
        cs.connect(progressBar.getId(), ConstraintSet.RIGHT, clRootContainer.getId(), ConstraintSet.RIGHT);
        cs.connect(progressBar.getId(), ConstraintSet.BOTTOM, clRootContainer.getId(), ConstraintSet.BOTTOM);

        cs.connect(tvActionName.getId(), ConstraintSet.BOTTOM, progressBar.getId(), ConstraintSet.TOP);

        cs.constrainWidth(mVideoView.getId(), height);
        cs.setDimensionRatio(mVideoView.getId(),"1");

        //将修改过的约束重新应用到ConstrainLayout
        cs.applyTo(clRootContainer);

    }

    //竖屏
    private void changeToPortraitLayout() {
        isLandscape = false;
        flTouchArea.callOnClick();

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels;     // 屏幕宽度（像素）
        height = metric.heightPixels;   // 屏幕高度（像素）

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(progressBar.getLayoutParams());
        params.height = progressBarHeight;
        progressBar.setLayoutParams(params);

        ConstraintSet cs = new ConstraintSet();
        //获取当前目标控件的约束集合
        cs.clone(clRootContainer);

        //修改mVideoView约束
        //清除约束

        cs.clear(mVideoView.getId());
        cs.clear(mBufferingTextView.getId());
        cs.clear(llPlayControlBar.getId(),ConstraintSet.TOP);

        cs.connect(mVideoView.getId(), ConstraintSet.LEFT, clRootContainer.getId(), ConstraintSet.LEFT);
        cs.connect(mVideoView.getId(), ConstraintSet.TOP, clRootContainer.getId(), ConstraintSet.TOP);
        cs.connect(mVideoView.getId(), ConstraintSet.RIGHT, clRootContainer.getId(), ConstraintSet.RIGHT);
        cs.connect(mVideoView.getId(), ConstraintSet.BOTTOM, clRootContainer.getId(), ConstraintSet.BOTTOM);

        cs.connect(llPlayControlBar.getId(),ConstraintSet.LEFT,clRootContainer.getId(),ConstraintSet.LEFT);
        cs.connect(llPlayControlBar.getId(), ConstraintSet.RIGHT, clRootContainer.getId(), ConstraintSet.RIGHT);
        cs.connect(llPlayControlBar.getId(), ConstraintSet.BOTTOM, clRootContainer.getId(), ConstraintSet.BOTTOM);


        cs.connect(progressBar.getId(),ConstraintSet.LEFT,clRootContainer.getId(),ConstraintSet.LEFT);
        cs.connect(progressBar.getId(), ConstraintSet.RIGHT, clRootContainer.getId(), ConstraintSet.RIGHT);
        cs.connect(progressBar.getId(), ConstraintSet.BOTTOM, clRootContainer.getId(), ConstraintSet.BOTTOM);

        cs.connect(tvActionName.getId(), ConstraintSet.BOTTOM, progressBar.getId(), ConstraintSet.TOP);


        cs.constrainHeight(mVideoView.getId(), height);
        cs.setDimensionRatio(mVideoView.getId(),"1");

        //将修改过的约束重新应用到ConstrainLayout
        cs.applyTo(clRootContainer);


    }

    // Release all media-related resources. In a more complicated app this
    // might involve unregistering listeners or releasing audio focus
    private void releasePlayer() {
        mVideoView.stopPlayback();
    }

    Boolean isShow = true;
    @Override
    public void onClick(View view) {
        Intent intent = null;
        Message msg = new Message();
        switch (view.getId()){
            case R.id.last:
                msg.what = LAST_ACTION;
                handler.handleMessage(msg);
                break;
            case R.id.stop:
                if(mVideoView.isPlaying()){
                    mVideoView.pause();
                    ibStop.setImageResource(R.drawable.ic_homepage_main_clicked);
                    //ibStop.setBackgroundResource(R.drawable.button_round_white);
                }else{
                    mVideoView.start();
                    ibStop.setImageResource(R.drawable.ic_sport_video_pause);
                    ibStop.setBackgroundResource(R.color.transparent);
                }

                break;
            case R.id.next:
                if(courseActionPosition<actionList.size()){
                    msg.what = NEXT_ACTION;
                }
                handler.handleMessage(msg);
                break;
            case R.id.btn_detail:
                intent = new Intent(this,sport_activity_course_action_detail.class);
                intent.putExtra("courseActionPosition",courseActionPosition);
                intent.putExtra("actionList",(Serializable) actionList);
                startActivity(intent);
                break;
            case R.id.ib_quit:
                mVideoView.pause();
                releasePlayer();
                finish();
                break;
            case R.id.ib_landscape:
                if(isLandscape){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }else{
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }

                break;
            //case R.id.constraintLayout:
            case R.id.fl_touch_area:
                if(llPlayControlBar.getVisibility() == View.VISIBLE && isLandscape){
                    llPlayControlBar.setVisibility(View.INVISIBLE);
                }else{
                    llPlayControlBar.setVisibility(View.VISIBLE);
                }
                break;

        }
    }

    private void showNextAction(String actionName,Boolean isShow){
        constraintSet.clone(clRootContainer);
        if(isShow){
            clRootContainer.addView(nextActionBar);
            constraintSet.connect(nextActionBar.getId(),constraintSet.BOTTOM, progressBar.getId(),constraintSet.TOP);
            constraintSet.connect(nextActionBar.getId(),constraintSet.START,ConstraintSet.PARENT_ID,constraintSet.START);
            constraintSet.connect(nextActionBar.getId(),constraintSet.END,ConstraintSet.PARENT_ID,constraintSet.END);

            //constraintSet.connect(tvActionName.getId(),constraintSet.BOTTOM,nextActionBar.getId(),constraintSet.TOP);


        }else{
            clRootContainer.removeView(nextActionBar);
            constraintSet.connect(tvActionName.getId(),constraintSet.BOTTOM, progressBar.getId(),constraintSet.TOP);
        }

    }

    private void showBreakDialog(int breakSeconds){
        breakDialog.setBreakSeconds(breakSeconds)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    Message msg = new Message();
                    msg.what = BREAK_OVER;
                    //msg.arg1 = breakDialog.getBreakSeconds();//need to be modified
                    handler.handleMessage(msg);
                }
            });

        breakDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoView.setFocusable(false);
    }

}