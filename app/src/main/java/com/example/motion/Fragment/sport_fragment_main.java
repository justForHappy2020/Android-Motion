//package com.example.motion.Fragment;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import com.example.motion.R;
//
//
//public class sport_fragment_main extends Fragment {
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.sport_fragment_main, container, false);
//        initView(view);
//        return view;
//    }
//
//    private void initView(View view){
//        ImageView img2= view.findViewById(R.id.diet_main_search);
//
//    }
//}
package com.example.motion.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.motion.Activity.me_activity_mycollections;
import com.example.motion.Activity.me_activity_mycourse;
import com.example.motion.Activity.search_course_activity;
import com.example.motion.R;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;

import java.util.HashMap;
import java.util.Map;


public class sport_fragment_main extends Fragment implements
        CalendarView.OnCalendarSelectListener,
        CalendarView.OnYearChangeListener,
        View.OnClickListener{
    TextView mTextMonthDay;
    TextView mTextYear;
    TextView mTextLunar;
    TextView mTextCurrentDay;
    CalendarView mCalendarView;
    RelativeLayout mRelativeTool;
    ImageView search;
    ImageView collect;
    ImageView history;
    private int mYear;
    CalendarLayout mCalendarLayout;
    private View.OnClickListener onClickListener;
    private PopupWindow mPopWindow;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sport_fragment_main, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view){
        ImageView img2= view.findViewById(R.id.diet_main_search);
        //setStatusBarDarkMode();
        mTextMonthDay = view.findViewById(R.id.tv_month_day);
        mTextYear = view.findViewById(R.id.tv_year);
        mTextLunar = view.findViewById(R.id.tv_lunar);
        mRelativeTool = view.findViewById(R.id.rl_tool);
        mCalendarView = view.findViewById(R.id.calendarView);
        mTextCurrentDay =  view.findViewById(R.id.tv_current_day);
        collect = view.findViewById(R.id.iv_sport_collected);
        history = view.findViewById(R.id.iv_sport_practiced);
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), me_activity_mycollections.class);
                startActivity(intent);
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), me_activity_mycourse.class);
                startActivity(intent);
            }
        });
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
        view.findViewById(R.id.fl_current).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToCurrent();
                showPopupWindow();
            }
        });
        mCalendarLayout = view.findViewById(R.id.calendarLayout);
        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnYearChangeListener(this);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));

        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), search_course_activity.class);
                startActivity(intent);
            }
        };
        search = view.findViewById(R.id.sport_main_search);
        search.setOnClickListener(onClickListener);


    }
    protected int getLayoutId() {
        return R.id.colorfulCalendar;
    }

    @Override
    public void onClick(View v) {

    }

    private void showPopupWindow(){
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.exercise_calander_detail, null);
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
        View rootview = LayoutInflater.from(getActivity()).inflate(R.layout.sport_fragment_main, null);
        //mPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }
    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        if(getActivity() != null){
            showPopupWindow();
        }
    }

    @Override
    public void onYearChange(int year) {

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
        }
    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        //calendar.setScheme(text);
        return calendar;
    }
}