
package com.example.motion.Activity.calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.motion.Activity.calendar.base.activity.BaseActivity;
import com.example.motion.R;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;

import java.util.HashMap;
import java.util.Map;

public class ColorfulActivity extends BaseActivity implements
        CalendarView.OnCalendarSelectListener,
        CalendarView.OnYearChangeListener,
        View.OnClickListener {
    TextView mTextMonthDay;
    TextView mTextYear;
    TextView mTextLunar;
    TextView mTextCurrentDay;
    CalendarView mCalendarView;
    RelativeLayout mRelativeTool;
    private int mYear;
    CalendarLayout mCalendarLayout;
    private PopupWindow mPopWindow;
    public static void show(Context context) {
        context.startActivity(new Intent(context, ColorfulActivity.class));
    }
    @Override
    protected int getLayoutId() {
        return R.id.colorfulCalendar;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        setStatusBarDarkMode();
        mTextMonthDay = findViewById(R.id.tv_month_day);
        mTextYear = findViewById(R.id.tv_year);
        mTextLunar = findViewById(R.id.tv_lunar);
        mRelativeTool = findViewById(R.id.rl_tool);
        mCalendarView = findViewById(R.id.calendarView);
        mTextCurrentDay =  findViewById(R.id.tv_current_day);
        mTextMonthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCalendarLayout.isExpand()) {
                    mCalendarLayout.expand();
                    return;
                }
                mCalendarView.showYearSelectLayout(mYear);
                mTextLunar.setVisibility(View.GONE);
                mTextYear.setVisibility(View.GONE);
                mTextMonthDay.setText(String.valueOf(mYear));
            }
        });
        findViewById(R.id.fl_current).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToCurrent();
            }
        });
        mCalendarLayout = findViewById(R.id.calendarLayout);
        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnYearChangeListener(this);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));
    }

    protected void initData() {

        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();
        Map<String, Calendar> map = new HashMap<>();
        int[] countDay;
        Log.d("initData","year,motnth"+year+"/"+month);
        countDay = new int[]{ 1,2,3,5,6,7,8,9,11,12,15,16,17,18,19,21,24,25,27,29,30,0,0,0,0,0,0,0,0,0,0};
        for(int i = 0; i < countDay.length;i++) {//设置变色的日子
            map.put(getSchemeCalendar(year, month, countDay[i], 0xFFbc13f0).toString(),
                    getSchemeCalendar(year, month, countDay[i], 0xFFbc13f0));
            /*map.put(getSchemeCalendar(year, month, 3, 0xFFe69138, "事").toString(),
                    getSchemeCalendar(year, month, 3, 0xFFe69138, "事"));
            map.put(getSchemeCalendar(year, month, 9, 0xFFdf1356, "议").toString(),
                    getSchemeCalendar(year, month, 9, 0xFFdf1356, "议"));
            map.put(getSchemeCalendar(year, month, 13, 0xFFedc56d, "记").toString(),
                    getSchemeCalendar(year, month, 13, 0xFFedc56d, "记"));
            map.put(getSchemeCalendar(year, month, 14, 0xFFedc56d, "记").toString(),
                    getSchemeCalendar(year, month, 14, 0xFFedc56d, "记"));
            map.put(getSchemeCalendar(year, month, 15, 0xFFaacc44, "假").toString(),
                    getSchemeCalendar(year, month, 15, 0xFFaacc44, "假"));
            map.put(getSchemeCalendar(year, month, 18, 0xFFbc13f0, "记").toString(),
                    getSchemeCalendar(year, month, 18, 0xFFbc13f0, "记"));
            map.put(getSchemeCalendar(year, month, 25, 0xFF13acf0, "假").toString(),
                    getSchemeCalendar(year, month, 25, 0xFF13acf0, "假"));
            map.put(getSchemeCalendar(year, month, 27, 0xFF13acf0, "多").toString(),
                    getSchemeCalendar(year, month, 27, 0xFF13acf0, "多"));*/
            //此方法在巨大的数据量上不影响遍历性能，推荐使用
            mCalendarView.setSchemeDate(map);
        };


    }

//    protected void initData2() {
//
//        int year = mCalendarView.getCurYear();
//        int month = mCalendarView.getCurMonth();
//
//        Map<String, Calendar> map = new HashMap<>();
//        map.put(getSchemeCalendar(year, month, 3, 0xFF40db25, "假").toString(),
//                getSchemeCalendar(year, month, 3, 0xFF40db25, "假"));
//        map.put(getSchemeCalendar(year, month, 6, 0xFFe69138, "事").toString(),
//                getSchemeCalendar(year, month, 6, 0xFFe69138, "事"));
//        map.put(getSchemeCalendar(year, month, 9, 0xFFdf1356, "议").toString(),
//                getSchemeCalendar(year, month, 9, 0xFFdf1356, "议"));
//        map.put(getSchemeCalendar(year, month, 13, 0xFFedc56d, "记").toString(),
//                getSchemeCalendar(year, month, 13, 0xFFedc56d, "记"));
//        map.put(getSchemeCalendar(year, month, 14, 0xFFedc56d, "记").toString(),
//                getSchemeCalendar(year, month, 14, 0xFFedc56d, "记"));
//        map.put(getSchemeCalendar(year, month, 15, 0xFFaacc44, "假").toString(),
//                getSchemeCalendar(year, month, 15, 0xFFaacc44, "假"));
//        map.put(getSchemeCalendar(year, month, 18, 0xFFbc13f0, "记").toString(),
//                getSchemeCalendar(year, month, 18, 0xFFbc13f0, "记"));
//        map.put(getSchemeCalendar(year, month, 25, 0xFF13acf0, "假").toString(),
//                getSchemeCalendar(year, month, 25, 0xFF13acf0, "假"));
//        map.put(getSchemeCalendar(year, month, 27, 0xFF13acf0, "多").toString(),
//                getSchemeCalendar(year, month, 27, 0xFF13acf0, "多"));
//        //此方法在巨大的数据量上不影响遍历性能，推荐使用
//        mCalendarView.setSchemeDate(map);
//
//
//
//    }


    @Override
    public void onClick(View v) {

    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
//        calendar.setScheme(text);
        return calendar;
    }

    private void showPopupWindow(){
        View contentView = LayoutInflater.from(ColorfulActivity.this).inflate(R.layout.exercise_calander_detail, null);
        mPopWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setContentView(contentView);
        //设置各个控件的点击响应
        TextView tv1 = (TextView)contentView.findViewById(R.id.exerciseHowLong);
        TextView tv2 = (TextView)contentView.findViewById(R.id.exerciseCourse1);
        TextView tv3 = (TextView)contentView.findViewById(R.id.exerciseCourse2);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        //显示PopupWindow
        View rootview = LayoutInflater.from(ColorfulActivity.this).inflate(R.layout.sport_fragment_main, null);
        mPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();
        showPopupWindow();
    }

    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
    }


}
//package com.example.motion.Activity.calendar;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.Intent;
//import android.view.View;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//
//import com.example.motion.Activity.calendar.base.activity.BaseActivity;
//import com.example.motion.R;
//import com.haibin.calendarview.Calendar;
//import com.haibin.calendarview.CalendarLayout;
//import com.haibin.calendarview.CalendarView;
//import java.util.HashMap;
//import java.util.Map;
//
//public class ColorfulActivity extends BaseActivity implements
//        CalendarView.OnCalendarSelectListener,
//        CalendarView.OnYearChangeListener,
//        View.OnClickListener {
//    TextView mTextMonthDay;
//    TextView mTextYear;
//    TextView mTextLunar;
//    TextView mTextCurrentDay;
//    CalendarView mCalendarView;
//    RelativeLayout mRelativeTool;
//    private int mYear;
//    CalendarLayout mCalendarLayout;
//    public static void show(Context context) {
//        context.startActivity(new Intent(context, ColorfulActivity.class));
//    }
//    @Override
//    protected int getLayoutId() {
//        return R.id.colorfulCalendar;
//    }
//
//    @SuppressLint("SetTextI18n")
//    @Override
//    protected void initView() {
//        setStatusBarDarkMode();
//        mTextMonthDay = findViewById(R.id.tv_month_day);
//        mTextYear = findViewById(R.id.tv_year);
//        mTextLunar = findViewById(R.id.tv_lunar);
//        mRelativeTool = findViewById(R.id.rl_tool);
//        mCalendarView = findViewById(R.id.calendarView);
//        mTextCurrentDay =  findViewById(R.id.tv_current_day);
//        mTextMonthDay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!mCalendarLayout.isExpand()) {
//                    mCalendarLayout.expand();
//                    return;
//                }
//                mCalendarView.showYearSelectLayout(mYear);
//                mTextLunar.setVisibility(View.GONE);
//                mTextYear.setVisibility(View.GONE);
//                mTextMonthDay.setText(String.valueOf(mYear));
//            }
//        });
//        findViewById(R.id.fl_current).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mCalendarView.scrollToCurrent();
//            }
//        });
//        mCalendarLayout = findViewById(R.id.calendarLayout);
//        mCalendarView.setOnCalendarSelectListener(this);
//        mCalendarView.setOnYearChangeListener(this);
//        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
//        mYear = mCalendarView.getCurYear();
//        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
//        mTextLunar.setText("今日");
//        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));
//    }
//
//    @Override
//    protected void initData() {
//
//        int year = mCalendarView.getCurYear();
//        int month = mCalendarView.getCurMonth();
//        Map<String, Calendar> map = new HashMap<>();
//        int[] countDay;
//       // countDay = new int[]{ 1,2,3,5,6,7,8,9,11,12,15,16,17,18,19,21,24,25,27,29,30,0,0,0,0,0,0,0,0,0,0};
//       // for(int i = 0; i < countDay.length;i++) {//设置变色的日子
//            //map.put(getSchemeCalendar(year, month, countDay[i], 0xFFbc13f0, "假").toString(),
//            //        getSchemeCalendar(year, month, countDay[i], 0xFFbc13f0, "假"));
//            map.put(getSchemeCalendar(year, month, 1, 0xFFbc13f0, "假").toString(),
//                    getSchemeCalendar(year, month, 1, 0xFFbc13f0, "假"));
//            map.put(getSchemeCalendar(year, month, 3, 0xFFe69138, "事").toString(),
//                    getSchemeCalendar(year, month, 3, 0xFFe69138, "事"));
//            map.put(getSchemeCalendar(year, month, 9, 0xFFdf1356, "议").toString(),
//                    getSchemeCalendar(year, month, 9, 0xFFdf1356, "议"));
//            map.put(getSchemeCalendar(year, month, 13, 0xFFedc56d, "记").toString(),
//                    getSchemeCalendar(year, month, 13, 0xFFedc56d, "记"));
//            map.put(getSchemeCalendar(year, month, 14, 0xFFedc56d, "记").toString(),
//                    getSchemeCalendar(year, month, 14, 0xFFedc56d, "记"));
//            map.put(getSchemeCalendar(year, month, 15, 0xFFaacc44, "假").toString(),
//                    getSchemeCalendar(year, month, 15, 0xFFaacc44, "假"));
//            map.put(getSchemeCalendar(year, month, 18, 0xFFbc13f0, "记").toString(),
//                    getSchemeCalendar(year, month, 18, 0xFFbc13f0, "记"));
//            map.put(getSchemeCalendar(year, month, 25, 0xFF13acf0, "假").toString(),
//                    getSchemeCalendar(year, month, 25, 0xFF13acf0, "假"));
//            map.put(getSchemeCalendar(year, month, 27, 0xFF13acf0, "多").toString(),
//                    getSchemeCalendar(year, month, 27, 0xFF13acf0, "多"));
//            //此方法在巨大的数据量上不影响遍历性能，推荐使用
//            mCalendarView.setSchemeDate(map);
//        //};
//
//
//    }
//
//
//    @Override
//    public void onClick(View v) {
//    }
//
//    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
//        Calendar calendar = new Calendar();
//        calendar.setYear(year);
//        calendar.setMonth(month);
//        calendar.setDay(day);
//        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
//        calendar.setScheme(text);
//        return calendar;
//    }
//
//
//
//    @Override
//    public void onCalendarOutOfRange(Calendar calendar) {
//
//    }
//
//    @SuppressLint("SetTextI18n")
//    @Override
//    public void onCalendarSelect(Calendar calendar, boolean isClick) {
//        mTextLunar.setVisibility(View.VISIBLE);
//        mTextYear.setVisibility(View.VISIBLE);
//        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
//        mTextYear.setText(String.valueOf(calendar.getYear()));
//        mTextLunar.setText(calendar.getLunar());
//        mYear = calendar.getYear();
//    }
//
//    @Override
//    public void onYearChange(int year) {
//        mTextMonthDay.setText(String.valueOf(year));
//    }
//
//
//}
