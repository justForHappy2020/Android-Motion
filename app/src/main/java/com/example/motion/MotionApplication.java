package com.example.motion;

import android.app.Application;
import android.content.Context;

import com.example.motion.Utils.UserInfoManager;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;
import android.app.Application;
import com.pgyer.pgyersdk.PgyerSDKManager;


public class MotionApplication extends Application{


    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        dbFlowInit();

        //在attachBaseContext方法中调用初始化sdk
        initPgyerSDK(this);
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

    /**
     *  初始化蒲公英SDK
     * @param application
     */
    private static void initPgyerSDK(MotionApplication application){
        new PgyerSDKManager.Init()
                .setContext(application) //设置上下问对象
                .start();
    }

}

