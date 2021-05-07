package com.example.motion.Widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.motion.Activity.sport_activity_course_completed;
import com.example.motion.R;

import java.util.Timer;
import java.util.TimerTask;

public class SportBreakDialog extends Dialog {
    public final int BREAK_OVER = 5;
    public final int BREAK_SKIP = 6;

    private Button btnSkip;
    private TextView tvBreakTime;
    private Handler handler;
    private int breakSeconds;


    public SportBreakDialog(Context context) {
        super(context, R.style.Theme_AppCompat_Dialog);
        setOwnerActivity((Activity)context);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sport_dialog_course_break);
        //按空白处不能取消
        setCanceledOnTouchOutside(true);
        //设置window背景，默认的背景会有Padding值，不能全屏
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        //一定要在setContentView之后调用，否则无效
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        initView();
        initHandler();
    }

    public SportBreakDialog setOnSkipBtnClickListener(View.OnClickListener listener){
        btnSkip.setOnClickListener(listener);
        return this;
    }

    public SportBreakDialog setBreakSeconds(int breakSeconds){
        this.breakSeconds = breakSeconds;
        return this;
    }

    public int getBreakSeconds(){
        return breakSeconds;
    }

    private void initView(){
        btnSkip = findViewById(R.id.btn_break_skip);
        tvBreakTime = findViewById(R.id.tv_break_time);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder= new AlertDialog.Builder(getContext());
                builder.setMessage(R.string.sport_course_start_break_skip_hint)
                        .setCancelable(true)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Message msg = new Message();
                                msg.what = BREAK_SKIP;
                                handler.handleMessage(msg);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                         })
                        .show();
            }
        });
    }

    private void initHandler(){
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case BREAK_SKIP:
                    case BREAK_OVER:
                        dismiss();
                        break;
                }
            }
        };




        /*
        timerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(breakSeconds>0){
                        --breakSeconds;
                        tvBreakTime.setText(String.valueOf(breakSeconds));
                    }else{
                        Message msg = new Message();
                        msg.what = BREAK_OVER;
                        handler.handleMessage(msg);

                    }
                }
            }
        });

        timerThread.start();
      */


        /*
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {new Runnable() {
                    @Override
                    public void run() {
                        Log.d("SportBreakDialog","postDelayed_running");

                        if(breakSeconds>0){
                            --breakSeconds;
                            tvBreakTime.setText(String.valueOf(breakSeconds));
                        }else{
                            Message msg = new Message();
                            msg.what = BREAK_OVER;
                            handler.handleMessage(msg);
                        }

                    }
                };
                handler.postDelayed(this, 1000);
            }
        }, 1000);

         */
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Dialog","onStop");
    }

    @Override
    public void cancel() {
        super.cancel();

        Log.d("Dialog","cancel");
    }

    @Override
    public void dismiss() {
        super.dismiss();
        Log.d("Dialog","dissmiss");
    }

    @Override
    public void show() {
        super.show();
        tvBreakTime.setText(String.valueOf(breakSeconds));
        Timer timer = new Timer();
        timer.schedule(new mTimerTask(),0,1000);

    }

    class mTimerTask extends TimerTask{

        @Override
        public void run() {
            Log.d("Timer","runing");
            if(breakSeconds>0){
                tvBreakTime.setText(String.valueOf(breakSeconds));
                --breakSeconds;
            }else{
                cancel();
                Message msg = new Message();
                msg.what = BREAK_OVER;
                handler.handleMessage(msg);
            }
        }
    }
}
