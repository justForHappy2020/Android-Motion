package com.example.motion.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
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

    private int timeSeconds;
    private int httpcode;

    private int width;// 屏幕宽度（像素）
    private int height;// 屏幕高度（像素）


    private int courseActionPosition;
    private int nowPlayTimes = 0;
    // Current playback position (in milliseconds).
    private int nowVideoPosition = 0;

    private String token;
    private Course course;
    private List<Action> actionList;


    private Handler handler;
    private DecimalFormat decimalFormat =new DecimalFormat("00");

    private VideoView mVideoView;
    private ProgressBar progressBar;
    private TextView mBufferingTextView;
    private TextView tvShowTime;
    private TextView tvActionName;
    private ImageButton last;
    private ImageButton stop;
    private ImageButton next;
    private ImageButton ibQuit;
    private ImageButton ibLandscape;
    private Button btnDetail;
    private ConstraintLayout clRootContainer;
    private FrameLayout bottomFramlayout;
    private ConstraintSet constraintSet = new ConstraintSet();
    private View nextActionBar;

    //暂停对话框
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;
    private View layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sport_activity_course_start);

        Intent intent = getIntent();
        course = (Course)intent.getSerializableExtra("course");
        actionList = (ArrayList<Action>)intent.getSerializableExtra("actionList");

        courseActionPosition = intent.getIntExtra("courseActionPosition",0);

        if (savedInstanceState != null) {
            nowVideoPosition = savedInstanceState.getInt(PLAYBACK_TIME);
        }

        initHandler();
        initView();

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
                            mVideoView.setVideoURI(Uri.parse(actionList.get(courseActionPosition).getActionUrl()));
                        }
                        break;
                    case NEXT_ACTION:
                        courseActionPosition++;
                        nowPlayTimes = 0;
                        mVideoView.seekTo(0);
                        if(courseActionPosition<actionList.size()){
                            tvActionName.setText(actionList.get(courseActionPosition).getActionName());
                        }else{
                            Intent intent = new Intent(getBaseContext(),sport_activity_course_completed.class);
                            startActivity(intent);
                            finish();
                            break;
                        }
                    case NEXT_VIDEO:
                        mVideoView.setVideoURI(Uri.parse(actionList.get(courseActionPosition).getActionUrl()));
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
                        if(mVideoView.isPlaying()){
                            progressBar.setProgress(nowPlayTimes*mVideoView.getDuration()+mVideoView.getCurrentPosition());
                            timeSeconds++;
                            tvShowTime.setText(decimalFormat.format(timeSeconds/60)+":"+decimalFormat.format(timeSeconds%60));
                        }
                       }
                });
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    private void initView(){
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels;     // 屏幕宽度（像素）
        height = metric.heightPixels;   // 屏幕高度（像素）

        SharedPreferences readSP = getSharedPreferences("saved_token",MODE_PRIVATE);
        token = readSP.getString("token","");

        mVideoView = findViewById(R.id.video_view);
        mBufferingTextView = findViewById(R.id.tv_buffering);
        progressBar = findViewById(R.id.progressBar);
        tvShowTime = findViewById(R.id.tv_showtime);
        tvActionName = findViewById(R.id.tv_action_name);
        last = findViewById(R.id.last);
        stop = findViewById(R.id.stop);
        next = findViewById(R.id.next);
        btnDetail = findViewById(R.id.btn_detail);
        ibQuit = findViewById(R.id.ib_quit);
        ibLandscape = findViewById(R.id.ib_landscape);

        clRootContainer = findViewById(R.id.constraintLayout);
        bottomFramlayout = findViewById(R.id.bottom_framelayout);
        nextActionBar = getLayoutInflater().inflate(R.layout.sport_include_course_start_info_bar,null);

        last.setOnClickListener(this);
        stop.setOnClickListener(this);
        next.setOnClickListener(this);
        btnDetail.setOnClickListener(this);
        ibQuit.setOnClickListener(this);
        ibLandscape.setOnClickListener(this);

        tvActionName.setText(actionList.get(courseActionPosition).getActionName());

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

    @Override
    protected void onStart() {
        super.onStart();

        // Load the media each time onStart() is called.
        initializePlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();

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
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Media playback takes a lot of resources, so everything should be
        // stopped and released at this time.
        releasePlayer();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current playback position (in milliseconds) to the
        // instance state bundle.
        outState.putInt(PLAYBACK_TIME, mVideoView.getCurrentPosition());
    }

    private void initializePlayer() {
        // Show the "Buffering..." message while the video loads.
        mBufferingTextView.setVisibility(VideoView.VISIBLE);

        // Buffer and decode the video sample.
        Uri videoUri = Uri.parse(actionList.get(courseActionPosition).getActionUrl());

        mVideoView.setVideoURI(videoUri);

        //mVideoView.setAutofillId();
        //mVideoView.setClipToOutline(true);

        // Listener for onPrepared() event (runs after the media is prepared).
        mVideoView.setOnPreparedListener(
                new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        progressBar.setMax(mVideoView.getDuration()*actionList.get(courseActionPosition).getTime());

                        // Hide buffering message.
                        mBufferingTextView.setVisibility(VideoView.INVISIBLE);

                        // Restore saved position, if available.
                        if (nowVideoPosition > 0) {
                            mVideoView.seekTo(nowVideoPosition);
                        } else {
                            // Skipping to 1 shows the first frame of the video.
                            mVideoView.seekTo(1);
                        }

                        //改变视频的大小和位置
                        //setVideoViewPosition();
                        changeToPortraitLayout();
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
                        if(nowPlayTimes >=actionList.get(courseActionPosition).getTime()){
                            msg.what = NEXT_ACTION;
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
                changeToLandscapeLayout();
                break;
            }
            case Configuration.ORIENTATION_PORTRAIT:{
                changeToPortraitLayout();
                break;
            }
            default: {//竖屏
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
        /*
        framelayout_cir.setVisibility(View.VISIBLE);
        progressBar_cir.setVisibility(View.VISIBLE);
        stop_cir.setVisibility(View.VISIBLE);

        bottom_framelayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        bottom_linerlayout.setVisibility(View.GONE);

         */


        ConstraintSet cs = new ConstraintSet();
        //获取当前目标控件的约束集合
        cs.clone(this, R.layout.sport_activity_course_start);

        //修改mVideoView约束
        //清除约束
        cs.clear(mVideoView.getId());
        cs.clear(mBufferingTextView.getId());
        cs.connect(mVideoView.getId(), ConstraintSet.LEFT, clRootContainer.getId(), ConstraintSet.LEFT);
        cs.connect(mVideoView.getId(), ConstraintSet.TOP, clRootContainer.getId(), ConstraintSet.TOP);
        cs.connect(mVideoView.getId(), ConstraintSet.RIGHT, clRootContainer.getId(), ConstraintSet.RIGHT);
        cs.connect(mVideoView.getId(), ConstraintSet.BOTTOM, clRootContainer.getId(), ConstraintSet.BOTTOM);
        //cs.constrainHeight(mVideoView.getId(), mPortraitPosition.getHeight());
        cs.constrainWidth(mVideoView.getId(), height);
        cs.setDimensionRatio(mVideoView.getId(),"1");

        //将修改过的约束重新应用到ConstrainLayout
        cs.applyTo(clRootContainer);

      /*  ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                mPortraitPosition.getHeight(), mPortraitPosition.getHeight());
        mVideoView.setLayoutParams(params);//设置VideoView的布局参数*/
    }

    //竖屏
    private void changeToPortraitLayout() {
        /*
        framelayout_cir.setVisibility(View.INVISIBLE);
        progressBar_cir.setVisibility(View.INVISIBLE);
        stop_cir.setVisibility(View.INVISIBLE);

        bottom_framelayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        bottom_linerlayout.setVisibility(View.VISIBLE);

         */

        ConstraintSet cs = new ConstraintSet();
        //获取当前目标控件的约束集合
        cs.clone(this, R.layout.sport_activity_course_start);

        //修改mVideoView约束
        //清除约束

        cs.clear(mVideoView.getId());
        cs.clear(mBufferingTextView.getId());
        cs.connect(mVideoView.getId(), ConstraintSet.LEFT, clRootContainer.getId(), ConstraintSet.LEFT);
        cs.connect(mVideoView.getId(), ConstraintSet.TOP, clRootContainer.getId(), ConstraintSet.TOP);
        cs.connect(mVideoView.getId(), ConstraintSet.RIGHT, clRootContainer.getId(), ConstraintSet.RIGHT);
        cs.connect(mVideoView.getId(), ConstraintSet.BOTTOM, clRootContainer.getId(), ConstraintSet.BOTTOM);
        cs.constrainHeight(mVideoView.getId(), height);
        cs.setDimensionRatio(mVideoView.getId(),"1");

        //将修改过的约束重新应用到ConstrainLayout
        cs.applyTo(clRootContainer);
    }


    // Release all media-related resources. In a more complicated app this
    // might involve unregistering listeners or releasing audio focus.
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
                }else{
                    mVideoView.start();
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
                showNextAction("动作名称",isShow);
                isShow = false;
                //changeToLandscapeLayout();
                break;

        }
    }

    private void showNextAction(String actionName,Boolean isShow){
        constraintSet.clone(clRootContainer);
        if(isShow){
            clRootContainer.addView(nextActionBar);
            constraintSet.connect(nextActionBar.getId(),constraintSet.BOTTOM,bottomFramlayout.getId(),constraintSet.TOP);
            constraintSet.connect(nextActionBar.getId(),constraintSet.START,ConstraintSet.PARENT_ID,constraintSet.START);
            constraintSet.connect(nextActionBar.getId(),constraintSet.END,ConstraintSet.PARENT_ID,constraintSet.END);

            //constraintSet.connect(tvActionName.getId(),constraintSet.BOTTOM,nextActionBar.getId(),constraintSet.TOP);


        }else{
            clRootContainer.removeView(nextActionBar);
            constraintSet.connect(tvActionName.getId(),constraintSet.BOTTOM,bottomFramlayout.getId(),constraintSet.TOP);
        }

    }

}