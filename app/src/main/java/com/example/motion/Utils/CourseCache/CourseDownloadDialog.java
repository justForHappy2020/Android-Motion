package com.example.motion.Utils.CourseCache;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.motion.R;

import java.text.DecimalFormat;

public class CourseDownloadDialog extends Dialog {

    private final int ByteToMB = 1024*1024;
    private View layoutView;
    private ProgressBar progressBar;
    private TextView tvProgressText;
    private TextView tvExit;
    private DecimalFormat decimalFormat;


    public CourseDownloadDialog(@NonNull Context context, int layoutId) {
        super(context, R.style.Theme_AppCompat_Dialog);
        setOwnerActivity((Activity)context);
        this.layoutView = View.inflate(context,layoutId,null);
    }

    public CourseDownloadDialog(@NonNull Context context, View layoutView) {
        super(context, R.style.Theme_AppCompat_Dialog);
        setOwnerActivity((Activity)context);
        this.layoutView = layoutView;
    }

    public CourseDownloadDialog(@NonNull Context context) {
        super(context, R.style.Theme_AppCompat_Dialog);
        setOwnerActivity((Activity)context);
        this.layoutView = View.inflate(context,R.layout.sport_dialog_download_default,null);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(layoutView);
        setCanceledOnTouchOutside(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        initView();
        decimalFormat = new DecimalFormat("0.##");
    }

    private void initView(){
        progressBar = findViewById(R.id.download_progressBar);
        tvProgressText = findViewById(R.id.tv_download_progressText);
        tvExit = findViewById(R.id.tv_download_quit);
    }

    public void setProgress(int progress){
        progressBar.setProgress(progress);
        //tvProgressText.setText("正在下载 "+progress+"/"+progressBar.getMax());
    }

    public void setProgressMax(int maxProgress){
        progressBar.setMax(maxProgress);
    }

    public void setProgressText(int progress,int max){
        float progressf = (float)progress / (float)ByteToMB;
        float maxf = (float)max / (float)ByteToMB;

        String downloadingText = getContext().getString(R.string.sport_dialog_download_downloading);
        String MByteText = getContext().getString(R.string.MByte);
        tvProgressText.setText(downloadingText + " " + decimalFormat.format(progressf) + MByteText +"/" + decimalFormat.format(maxf) + MByteText);
    }

    public void setOnExitClickListener(View.OnClickListener listener){
        tvExit.setOnClickListener(listener);
    }
}
