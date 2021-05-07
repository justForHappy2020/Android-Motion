package com.example.motion.Utils;

import com.example.motion.Entity.Action;
import com.example.motion.Entity.Course;

import java.util.List;

public interface OnProcessStateChangeListener {
    // 回调方法
    //void onActionsProcessDone(boolean isSuccess, List<Action> processedActionList);
    void onProcessDone(boolean isSuccess,Course courseWithActions,Object message);
}
