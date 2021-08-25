package com.example.motion;

import android.app.Application;
import android.content.Context;

import com.example.motion.Utils.UserInfoManager;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;


public class MotionApplication extends Application{


    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        dbFlowInit();

        //CrashReport.initCrashReport(getApplicationContext());
        Bugly.init(getApplicationContext(), "a6b8dfa5cb", true);
    }

    private void dbFlowInit(){
        try {
            FlowManager.init(new FlowConfig.Builder(getApplicationContext())
                    .openDatabasesOnInit(true) .build());
            FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

