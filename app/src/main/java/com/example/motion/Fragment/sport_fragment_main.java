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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.motion.Activity.me_activity_mycollections;
import com.example.motion.Activity.me_activity_mycourse;
import com.example.motion.Activity.register_activity_register;
import com.example.motion.Activity.search_course_activity;

import com.example.motion.Activity.sport_activity_course_detail;
import com.example.motion.Entity.MultipleItem;
import com.example.motion.Entity.User;
import com.example.motion.Entity.sportMainItem;
import com.example.motion.R;
import com.example.motion.Utils.UserInfoManager;
import com.example.motion.VolleyRequest.MyStringRequest;
import com.example.motion.Widget.MultipleItemQuickAdapter;

import com.example.motion.Entity.Course;
import com.example.motion.Entity.DyxItem;
import com.example.motion.Entity.MultipleItem;
import com.example.motion.Entity.sportMainItem;
import com.example.motion.R;
import com.example.motion.Utils.UserInfoManager;
import com.example.motion.Widget.DyxQuickAdapter;
import com.example.motion.Widget.MultipleItemQuickAdapter;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class sport_fragment_main extends BaseNetworkFragment implements
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
    BasePopupView popupView;
    /**
     * 未界面控件
     */
    private LinearLayout llNotLoginBar;
    private RecyclerView rvPracticed;
    private RecyclerView rvCollected;
    private RecyclerView rvDownload;
    private MultipleItemQuickAdapter practicedAdapter;
    private MultipleItemQuickAdapter collectionAdapter;
    private MultipleItemQuickAdapter downloadAdapter;
    private List<MultipleItem> practicedList = new ArrayList();
    private List<MultipleItem> collectedList = new ArrayList();
    private List<MultipleItem> downloadList = new ArrayList();

    private String dialogMessage = "";
    private Handler handler;
    private SharedPreferences readSP;
    private String token;

    private boolean hasNext;
    private final int COURSE_NUM_IN_ONE_PAGE = 10;
    private User user = new User();

    private final int LOAD_PRACTICED_COURSES_SUCCESS = 1;
    private final int LOAD_COLLECTION_SUCCESS = 2;
    private final int LOAD_PRACTICED_COURSES_FAILED = 3;
    private final int LOAD_COLLECTION_FAILED = 4;
    private final int LOAD_USER_INFO_SUCCESS = 5;
    private final int LOAD_USER_INFO_FAILED = 6;

    private ImageView ivPortrait;
    private TextView tv_name;
    private TextView tv_days_count;
    private TextView tv_course_count;
    private TextView tv_exercise_time;
    private TextView tv_continue_day_count;

    //日历模块变量
    private String[] emptyList;
    private String[] dateList;
//    private String[][] exerciseList;
    private String[] exerciseList;
    private int[] exerciseTime;
    int [] yearList;
    int [] monthList;
    int [] dayList;

    private String userInfoUrl;
    private String sportInfoUrl;
    private String calendarUrl;
    private String historyUrl;
    private String collectionUrl;
    private String emptyUrl = "";
    private String testToken = "078d3ab3-6b55-4d86-9957-0fd961d79972";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;

        if(UserInfoManager.getUserInfoManager(getContext()).isTokenEmpty()){
            view = inflater.inflate(R.layout.sport_fragment_main_not_login, container, false);
            initNotLoginView(view);
        }else{
            view = inflater.inflate(R.layout.sport_fragment_main, container, false);
            initView(view);
            initHandler();
            checkToken();
            initData();
//            initPracticedList();
//            initCollectionList();
//            initDownloadList();
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initLocalData();
        if(!UserInfoManager.getUserInfoManager(getContext()).isTokenEmpty()){
            initData();
        }else{

        }
    }

    private void checkToken() {
        readSP=getActivity().getSharedPreferences("saveSp",MODE_PRIVATE);
        token = readSP.getString("token","");
        if (token.isEmpty()){
            Toast.makeText(getActivity(),"请登录", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), register_activity_register.class);
            startActivity(intent);
        }
    }

    private void initHandler(){
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case LOAD_PRACTICED_COURSES_SUCCESS:
                        Log.d("HANDLER","LOAD_COURSES_SUCCESS");
                        practicedAdapter.notifyDataSetChanged();
                        practicedAdapter.getLoadMoreModule().loadMoreComplete();

                        Toast.makeText(getActivity(), "请求成功", Toast.LENGTH_SHORT).show();
                        break;
                    case LOAD_PRACTICED_COURSES_FAILED:
                        Log.d("HANDLER","LOAD_PRACTICED_COURSES_FAILED");
                        Toast.makeText(getActivity(), "LOAD_PRACTICED_COURSES_FAILED,"+msg.obj, Toast.LENGTH_LONG).show();
                        break;
                    case LOAD_COLLECTION_SUCCESS:
                        Log.d("HANDLER","LOAD_COURSES_SUCCESS");
                        collectionAdapter.notifyDataSetChanged();
                        collectionAdapter.getLoadMoreModule().loadMoreComplete();

                        Toast.makeText(getActivity(), "请求成功", Toast.LENGTH_SHORT).show();
                        break;
                    case LOAD_COLLECTION_FAILED:
                        Log.d("HANDLER","LOAD_COLLECTION_COURSES_FAILED");
                        Toast.makeText(getActivity(), "LOAD_COLLECTION_FAILED,"+msg.obj, Toast.LENGTH_LONG).show();
                        break;
                    case LOAD_USER_INFO_FAILED:
                        Log.d("me_fragment_main_Handler","LOAD_USER_INFO_FAILED");
                        break;
                    case LOAD_USER_INFO_SUCCESS:
                        Log.d("me_fragment_main_Handler","LOAD_USER_INFO_SUCCESS");
                        tv_name.setText(user.getNickName());
                        Glide.with(getContext()).load(user.getHeadPortraitUrl()).into(ivPortrait);
                        break;
                }
            }
        };
    }
    private void initLocalData(){
        user = UserInfoManager.getUserInfoManager(getContext()).getUser();
        tv_name.setText(user.getNickName());
        Glide.with(getContext()).load(user.getHeadPortraitUrl()).into(ivPortrait);
    }

    //将接口获取的日期进行转换并且展示
    private void transformDate(String [] dateList){
        int dateListLength = dateList.length;
        yearList = new int[dateListLength];
        monthList = new int[dateListLength];
        dayList = new int[dateListLength];
        String singleDate;
        String singleYear;
        String singleMonth;
        String singleDay;
        for(int i=0;i<dateListLength;i++){
            singleDate  =  dateList[i];
            singleYear = singleDate.substring(0,4);
            singleMonth = singleDate.substring(5,7);
            singleDay = singleDate.substring(8,10);
            yearList[i] = Integer.parseInt(singleYear);
            monthList[i] = Integer.parseInt(singleMonth);
            dayList[i] = Integer.parseInt(singleDay);
        }


        Map<String, Calendar> map = new HashMap<>();
//        int[] countDay;
//        countDay = new int[]{ 1,2,3,5,6,7,8,9,11,12,15,16,17,18,19,21,24,25,27,29,30,0,0,0,0,0,0,0,0,0,0};
        for(int i = 0; i < dateListLength;i++) {//设置变色的日子
            map.put(getSchemeCalendar(yearList[i], monthList[i], dayList[i], 0xFFbc13f0).toString(),
                    getSchemeCalendar(yearList[i], yearList[i], dayList[i], 0xFFbc13f0));
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

        ivPortrait = view.findViewById(R.id.sport_main_head);
        tv_name = view.findViewById(R.id.sport_main_user_name);
        tv_days_count = view.findViewById(R.id.sport_main_days_count);
        tv_course_count = view.findViewById(R.id.sport_main_course_count);
        tv_exercise_time = view.findViewById(R.id.sport_main_exercise_time_count);
        tv_continue_day_count = view.findViewById(R.id.sport_main_continue_days_count);

        rvPracticed = view.findViewById(R.id.rvSportMainPracticed);
        rvCollected = view.findViewById(R.id.rvSportMainCollection);
        rvDownload = view.findViewById(R.id.rvSportMainDownload);
        rvPracticed.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvDownload.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCollected.setLayoutManager(new LinearLayoutManager(getActivity()));

        practicedAdapter = new MultipleItemQuickAdapter(practicedList);
        collectionAdapter = new MultipleItemQuickAdapter(collectedList);
        downloadAdapter = new MultipleItemQuickAdapter(downloadList);
        practicedAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter quickAdapter, @NonNull View view, int position) {
                //Log.d("Adapter","Click");
                Intent intent;
                intent = new Intent(getActivity(), sport_activity_course_detail.class);
/*                for(int i = 0 ; i < dataSet.size() ; i++){
                    courseList = dataSet.get(i);
                    if(courseList.size()<=position)position = position - courseList.size();
                    else break;
                }*/
                intent.putExtra("course", practicedList.get(position).getCourse());
                startActivity(intent);
            }
        });
        collectionAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter quickAdapter, @NonNull View view, int position) {
                //Log.d("Adapter","Click");
                Intent intent;
                intent = new Intent(getActivity(), sport_activity_course_detail.class);
/*                for(int i = 0 ; i < dataSet.size() ; i++){
                    courseList = dataSet.get(i);
                    if(courseList.size()<=position)position = position - courseList.size();
                    else break;
                }*/
                intent.putExtra("course", collectedList.get(position).getCourse());
                startActivity(intent);
            }
        });
        rvPracticed.setAdapter(practicedAdapter);
        rvCollected.setAdapter(collectionAdapter);
        rvDownload.setAdapter(downloadAdapter);

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
        view.findViewById(R.id.fl_current).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToCurrent();
                //showPopupWindow();
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

    private void initNotLoginView(View view){
        llNotLoginBar = view.findViewById(R.id.ll_not_login);
        llNotLoginBar.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_not_login:
                Intent intent = new Intent(getActivity(), register_activity_register.class);
                startActivity(intent);
                break;
        } }
    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        //Log.e("onDateSelected", "  -- " + calendar.getYear() + "  --  " + calendar.getMonth() + "  -- " + calendar.getDay());
//        mTextLunar.setVisibility(View.VISIBLE);
//        mTextYear.setVisibility(View.VISIBLE);
//        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
//        mTextYear.setText(String.valueOf(calendar.getYear()));
//        mTextLunar.setText(calendar.getLunar());
//        mYear = calendar.getYear();
        int Year = calendar.getYear();
        int Month = calendar.getMonth();
        int Day = calendar.getDay();
        String []testList = dateList;
        if(dateList.length!=0){
            for(int i = 0;i<365;i++){
                if(Year == yearList[i] && Month == monthList[i] &&  Day ==dayList[i]){
                    if (isClick) {
                        String HowLong=Integer.toString(exerciseTime[i]);
//                        String[] Courses = new String[exerciseList[i].length];
//                        for(int n = 0;n<exerciseList.length;n++) {
//                            Courses[n] = exerciseList[i][n];
                        String Courses = exerciseList[i];//new String[exerciseList.length];

//带确认和取消按钮的弹窗
                        /*if(popupView==null)*/popupView = new XPopup.Builder(getActivity())
                                .dismissOnBackPressed(true)
                                .dismissOnTouchOutside(true)
//                        .hasNavigationBar(false)
//                        .navigationBarColor(Color.BLUE)
//                        .hasBlurBg(true)
//                         .dismissOnTouchOutside(false)
//                         .autoDismiss(false)
//                        .popupAnimation(PopupAnimation.NoAnimation)
//                        .isLightStatusBar(true)
//                        .setPopupCallback(new DemoXPopupListener())
//                                .asConfirm(Year+"年"+Month+"月"+Day+"日", "练习时长："+HowLong+"分钟"+"\n"+"练习课程:"+showCalendarExerciseList(Courses),
                                .asConfirm(Year+"年"+Month+"月"+Day+"日", "练习时长："+HowLong+"分钟"+"\n"+"练习课程:"+Courses,
                                        "  ", "确定",
                                        new OnConfirmListener() {
                                            @Override
                                            public void onConfirm() {
                                            }
                                        }, null, false);
                        popupView.show();
                    }
                }
            }}
        else {
            for(int i = 0;i<emptyList.length;i++){
            if(Year == yearList[i] && Month == monthList[i] &&  Day ==dayList[i]){
                if (isClick) {
                    String HowLong=Integer.toString(exerciseTime[i]);
//                    String[] Courses = new String[exerciseList[i].length];
//                    for(int n = 0;n<exerciseList.length;n++) {
//                        Courses[n] = exerciseList[i][n];
                       String Courses = exerciseList[i];

//带确认和取消按钮的弹窗
                    /*if(popupView==null)*/popupView = new XPopup.Builder(getActivity())
                            .dismissOnBackPressed(true)
                            .dismissOnTouchOutside(true)
//                        .hasNavigationBar(false)
//                        .navigationBarColor(Color.BLUE)
//                        .hasBlurBg(true)
//                         .dismissOnTouchOutside(false)
//                         .autoDismiss(false)
//                        .popupAnimation(PopupAnimation.NoAnimation)
//                        .isLightStatusBar(true)
//                        .setPopupCallback(new DemoXPopupListener())
                            //.asConfirm(Year+"年"+Month+"月"+Day+"日", "练习时长："+HowLong+"分钟"+"\n"+"练习课程:"+showCalendarExerciseList(Courses),
                            .asConfirm(Year+"年"+Month+"月"+Day+"日", "练习时长："+HowLong+"分钟"+"\n"+"练习课程:"+Courses,
                                    "  ", "确定",
                                    new OnConfirmListener() {
                                        @Override
                                        public void onConfirm() {
                                        }
                                    }, null, false);
                    popupView.show();
                }
            }
        }}

    }

    public String showCalendarExerciseList(String[] exerciseList){
        String result = "";
        int length = exerciseList.length;
        for (int m = 0; m<length;m++){
            result = result+exerciseList[m] + "\n";
        }
        return result;

    }
    @Override
    public void onYearChange(int year) {

    }

    protected void initData() {
        userInfoUrl = "http://106.55.25.94:8080/api/community/getUserdata?token=";
        sportInfoUrl =  "http://106.55.25.94:8080/api/user/getSportsCenterData?token=";
        calendarUrl = "http://106.55.25.94:8080/api/user/getSportsLogData?token=";
        historyUrl = "http://106.55.25.94:8080/api/course/getPracticedCourse?size=";
        collectionUrl = "http://106.55.25.94:8080/api/course/getCollectionCourse?size=";


        getHttpUserInfo(userInfoUrl);
        getHttpSportInfo(sportInfoUrl);
        getHttpCalendar(calendarUrl);
        getHttpPracticedCourse(new HashMap(),historyUrl);
        getHttpCollectedCourse(new HashMap(),collectionUrl);

    }

    private void getHttpUserInfo(String url){
        String targetUserInfoUrl= url + UserInfoManager.getUserInfoManager(getContext()).getToken();

        MyStringRequest getTagsStringRequest = new MyStringRequest(Request.Method.GET,  targetUserInfoUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String responseStr) {
                try {
                    JSONObject jsonRootObject = new JSONObject(responseStr);

                    JSONObject jsonDataObject = jsonRootObject.getJSONObject("data");
                    //相应的内容
                    user.setHeadPortraitUrl(jsonDataObject.getString("headPortrait"));
                    user.setNickName(jsonDataObject.getString("nickName"));
                    Message msg = handler.obtainMessage();
                    msg.what = LOAD_USER_INFO_SUCCESS;
                    handler.sendMessage(msg);

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("homepage_fragment_main_user_info","getHttpCourseTags_onErrorResponse");

                Message msg = handler.obtainMessage();
                msg.what = LOAD_USER_INFO_FAILED;
                msg.obj = volleyError.toString();
                handler.sendMessage(msg);
            }
        });
        getTagsStringRequest.setTag("getHttp");
        requestQueue.add(getTagsStringRequest);

    }

    private void getHttpSportInfo(String url){
        //String targetSportInfoUrl = url + UserInfoManager.getUserInfoManager(getContext()).getToken();
        String targetSportInfoUrl = url + testToken;
        MyStringRequest getTagsStringRequest = new MyStringRequest(Request.Method.GET,  targetSportInfoUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String responseStr) {
                try {
                    JSONObject jsonRootObject = new JSONObject(responseStr);

                    JSONObject jsonDataObject = jsonRootObject.getJSONObject("data");
                    //相应的内容
                    tv_days_count.setText(Integer.toString(jsonDataObject.getInt("daysCount")));
                    tv_course_count.setText(Integer.toString(jsonDataObject.getInt("courseCount")));
                    tv_exercise_time.setText(Integer.toString(jsonDataObject.getInt("exerciseTimeCount")));
                    tv_continue_day_count.setText(Integer.toString(jsonDataObject.getInt("continueDaysCount")));

                    Message msg = handler.obtainMessage();
                    msg.what = LOAD_USER_INFO_SUCCESS;
                    handler.sendMessage(msg);


                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("homepage_fragment_main_exercise_data","getHttpCourseTags_onErrorResponse");

                Message msg = handler.obtainMessage();
                msg.what = LOAD_USER_INFO_FAILED;
                msg.obj = volleyError.toString();
                handler.sendMessage(msg);
            }
        });
        getTagsStringRequest.setTag("getHttp");
        requestQueue.add(getTagsStringRequest);
    }

    private void getHttpCalendar(String url){
        dateList = new String[] {};
        exerciseList = new String[] {};
        //exerciseList = new String[][] {};
        exerciseTime = new int[] {};
//        String targetCalendarUrl = url + UserInfoManager.getUserInfoManager(getContext()).getToken();
        String targetCalendarUrl = url+ testToken;//测试token
        MyStringRequest getTagsStringRequest = new MyStringRequest(Request.Method.GET,  targetCalendarUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String responseStr) {
                try {
                    JSONObject jsonRootObject = new JSONObject(responseStr);
                    JSONArray JSONArrayCalendarInfo = jsonRootObject.getJSONArray("data");
                    //相应的内容
                    //JSONArray  = jsonDataObject.getJSONArray("");
//                    JSONObject testArray = new JSONObject() {
//                    };
//                    testArray = JSONArrayCalendarInfo;
                    dateList = new String[JSONArrayCalendarInfo.length()];
                    exerciseList = new String[JSONArrayCalendarInfo.length()];
                    exerciseTime = new int[JSONArrayCalendarInfo.length()];
                    for(int m=0;m<JSONArrayCalendarInfo.length();m++){
                        JSONObject jsonCourseObject = JSONArrayCalendarInfo.getJSONObject(m);
                        dateList[m] = jsonCourseObject.getString("exerciseDate");
                        if(jsonCourseObject.getString("exerciseCourseNameList").length()!=0)
                            exerciseList[m] = jsonCourseObject.getString("exerciseCourseNameList");
                        else
                            exerciseList[m] = "无训练课程";
 //                       if(jsonCourseObject.getJSONArray("exerciseCourseNameList").length()!=0)

//                        for(int n=0;n<jsonCourseObject.getJSONArray("exerciseCourseNameList").length();n++){
//                            exerciseList[m][n] = jsonCourseObject.getJSONArray("exerciseCourseNameList").getString(n);
//                        }
//                        else
//                            exerciseList[m][0] = "无练习课程";
                        exerciseTime[m] = jsonCourseObject.getInt("exercisetime");
                    }

                    Message msg = handler.obtainMessage();
                    msg.what = LOAD_USER_INFO_SUCCESS;
                    handler.sendMessage(msg);


                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("homepage_fragment_main_exercise_data","getHttpCourseTags_onErrorResponse");

                Message msg = handler.obtainMessage();
                msg.what = LOAD_USER_INFO_FAILED;
                msg.obj = volleyError.toString();
                handler.sendMessage(msg);
            }
        });
        getTagsStringRequest.setTag("getHttp");
        requestQueue.add(getTagsStringRequest);


        if(dateList.length!=0)
            transformDate(dateList);
        else{
            emptyList = new String[] {"2021-06-23"};
            exerciseList = new String[] {"感谢使用"};
            exerciseTime = new int[]{23};
            transformDate(emptyList);
        }
    }

    private void getHttpPracticedCourse(Map params,String url){
        List<MultipleItem> sportMainPracticedCourses = new ArrayList<>();

        String targetPracticedCourseUrl = url + COURSE_NUM_IN_ONE_PAGE;
        if(params.isEmpty()){
//            targetPracticedCourseUrl+="&page=1&token="+token;//真实token
            targetPracticedCourseUrl+="&page=1&token="+testToken;//测试token
        }else{
            Iterator iter = params.keySet().iterator();
            while (iter.hasNext()) {
                Object key = iter.next();
                Object val = params.get(key);
                targetPracticedCourseUrl+=("&"+key.toString()+"="+val.toString());
            }
        }
//        Log.d("sport_main_practiced_course","requestUrl:" + url);
//        dialogMessage += "\n\ngetHttpCourse requestingUrl:\n" + url;

        MyStringRequest stringRequest = new MyStringRequest(Request.Method.GET,  targetPracticedCourseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String responseStr) {
                try {
                    JSONObject jsonRootObject = new JSONObject(responseStr);

                    //Log.d("sport_activity_course_selection","getHttpCourse_responseStr:" + jsonRootObject.toString());

                    JSONObject jsonObject2 = jsonRootObject.getJSONObject("data");
                    hasNext = jsonObject2.getBoolean("hasNext");
                    //TOTAL_PAGES = jsonObject2.getInt("totalPages");
                    //得到筛选的课程list
                    JSONArray JSONArrayCourse = jsonObject2.getJSONArray("courseList");
//                    for (int i = 0; i < JSONArrayCourse.length(); i++) {
                    int showCase = JSONArrayCourse.length();
                    if (showCase>3)
                        showCase = 3;
                    for (int i = 0; i < showCase; i++) {
                        JSONObject jsonCourseObject = JSONArrayCourse.getJSONObject(i);
                        //相应的内容
                        sportMainItem practicedCourse = new sportMainItem();

                        String courseName = jsonCourseObject.getString("courseName");
                        String backgroundUrl = jsonCourseObject.getString("backgroundUrl");
                        String targetAge = jsonCourseObject.getString("targetAge");
                        practicedCourse.setCourseName(courseName);
                        practicedCourse.setImgUrl(backgroundUrl);
                        practicedCourse.setTargetAge(targetAge);
                        JSONArray JSONArrayLabels = jsonCourseObject.getJSONArray("labels");
                        String labels = "";
                        for (int j = 0; j < JSONArrayLabels.length(); j++) {
                            labels += (JSONArrayLabels.get(j) + "/");
                        }
                        practicedCourse.setLables(labels);
                        sportMainPracticedCourses.add(new MultipleItem(MultipleItem.sport_main_item, practicedCourse));
                    }
                    practicedList.addAll(sportMainPracticedCourses);

                    Log.d("me_fragment_mycourse_collections","getHttpCourse_responseStr:"+responseStr);
                    Message msg = handler.obtainMessage();
                    msg.what = LOAD_PRACTICED_COURSES_SUCCESS;
                    handler.sendMessage(msg);
                    //test tool
                    dialogMessage+="\n\ngetHttpCourse response:\n"+jsonRootObject.toString();
                    //end of test tool
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("sport_activity_course_selection","getHttpCourse_onErrorResponse");

                //test tool
                dialogMessage+="\ngetHttpCourse error: "+volleyError.toString();
                //end of test tool

                Message msg = handler.obtainMessage();
                msg.what = LOAD_PRACTICED_COURSES_FAILED;
                msg.obj = volleyError.toString();
                handler.sendMessage(msg);

            }
        });
        stringRequest.setTag("getHttp");
        requestQueue.add(stringRequest);
    }

    private void getHttpCollectedCourse(Map params,String url ){
        List<MultipleItem> sportMainCollectionCourses = new ArrayList<>();

        String targetCollectionUrl = url + COURSE_NUM_IN_ONE_PAGE;
        if(params.isEmpty()){
//            url+="&page=1&token="+token;//真实token
            targetCollectionUrl+="&page=1&token="+testToken;//测试token
        }else{
            Iterator iter = params.keySet().iterator();
            while (iter.hasNext()) {
                Object key = iter.next();
                Object val = params.get(key);
                targetCollectionUrl+=("&"+key.toString()+"="+val.toString());
            }
        }
        Log.d("sport_main_collected_course","requestUrl:" + url);
        dialogMessage += "\n\ngetHttpCourse requestingUrl:\n" + url;

        MyStringRequest stringRequest = new MyStringRequest(Request.Method.GET,  targetCollectionUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String responseStr) {

                try {
                    JSONObject jsonRootObject = new JSONObject(responseStr);

                    //Log.d("sport_activity_course_selection","getHttpCourse_responseStr:" + jsonRootObject.toString());

                    JSONObject jsonObject2 = jsonRootObject.getJSONObject("data");
                    hasNext = jsonObject2.getBoolean("hasNext");
                    //TOTAL_PAGES = jsonObject2.getInt("totalPages");
                    //得到筛选的课程list
                    JSONArray JSONArrayCourse = jsonObject2.getJSONArray("courseList");
                    int showCase = JSONArrayCourse.length();
                    if (showCase>3)
                        showCase = 3;
//                    for (int i = 0; i < JSONArrayCourse.length(); i++) {
                    for (int i = 0; i < showCase; i++) {
                        JSONObject jsonCourseObject = JSONArrayCourse.getJSONObject(i);
                        //相应的内容
                        sportMainItem collectedCourse = new sportMainItem();
                        collectedCourse.setCourseName(jsonCourseObject.getString("courseName"));
                        collectedCourse.setImgUrl(jsonCourseObject.getString("backgroundUrl"));
                        collectedCourse.setTargetAge(jsonCourseObject.getString("targetAge"));
                        JSONArray JSONArrayLabels = jsonCourseObject.getJSONArray("labels");
                        String labels = "";
                        for (int j = 0; j < JSONArrayLabels.length(); j++) {
                            labels += (JSONArrayLabels.get(j) + "/");
                        }
                        collectedCourse.setLables(labels);
                        sportMainCollectionCourses.add(new MultipleItem(MultipleItem.sport_main_item, collectedCourse));
                    }
                    collectedList.addAll(sportMainCollectionCourses);

                    Log.d("sport_main_collection","getHttpCourse_responseStr:"+responseStr);
                    Message msg = handler.obtainMessage();
                    msg.what = LOAD_COLLECTION_SUCCESS;
                    handler.sendMessage(msg);
                    //test tool
                    dialogMessage+="\n\ngetHttpCourse response:\n"+jsonRootObject.toString();
                    //end of test tool
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("sport_main_collection","getHttpCourse_onErrorResponse");

                //test tool
                dialogMessage+="\ngetHttpCourse error: "+volleyError.toString();
                //end of test tool

                Message msg = handler.obtainMessage();
                msg.what = LOAD_PRACTICED_COURSES_FAILED;
                msg.obj = volleyError.toString();
                handler.sendMessage(msg);

            }
        });
        stringRequest.setTag("getHttp");
        requestQueue.add(stringRequest);
    }

//    protected void initPracticedList(){
//        sportMainItem practicedCourse;
//        for (int i = 0; i < 3; i++) {
//            practicedCourse = new sportMainItem();
//            practicedCourse.setImgUrl("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3385472845,2539383542&fm=11&gp=0.jpg");
//            practicedCourse.setCourseName("篮球培优课");
//            practicedCourse.setTargetAge("适合4-6岁孩子练习");
//            practicedCourse.setLables("亲子A类/网球/坐位体前屈");
//            practicedList.add(new MultipleItem(MultipleItem.sport_main_item,practicedCourse));
//        }
//    }
//
//    protected void initCollectionList(){
//        sportMainItem collectedCourse;
//        for (int i = 0; i < 3; i++) {
//            collectedCourse = new sportMainItem();
//            collectedCourse.setImgUrl("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3385472845,2539383542&fm=11&gp=0.jpg");
//            collectedCourse.setCourseName("篮球培优课");
//            collectedCourse.setTargetAge("适合4-6岁孩子练习");
//            collectedCourse.setLables("亲子A类/网球/坐位体前屈");
//            collectedList.add(new MultipleItem(MultipleItem.sport_main_item,collectedCourse));
//        }
//    }

    protected void initDownloadList(){
        sportMainItem downloadCourse;
        for (int i = 0; i < 3; i++) {
            downloadCourse = new sportMainItem();
            downloadCourse.setImgUrl("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3385472845,2539383542&fm=11&gp=0.jpg");
            downloadCourse.setCourseName("篮球培优课");
            downloadCourse.setTargetAge("适合4-6岁孩子练习");
            downloadCourse.setLables("亲子A类/网球/坐位体前屈");
            downloadList.add(new MultipleItem(MultipleItem.sport_main_item, downloadCourse));
        }


    }

    protected void initPracticedList(){
        sportMainItem practicedCourse;
        for (int i = 0; i < 3; i++) {
            practicedCourse = new sportMainItem();

            practicedCourse.setImgUrl("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3385472845,2539383542&fm=11&gp=0.jpg");
            practicedCourse.setCourseName("篮球培优课");
            practicedCourse.setTargetAge("适合4-6岁孩子练习");
            practicedCourse.setLables("亲子A类/网球/坐位体前屈");
            practicedList.add(new MultipleItem(MultipleItem.sport_main_item,practicedCourse));
        }
    }

    protected void initCollectionList(){
        sportMainItem collectedCourse;
        for (int i = 0; i < 3; i++) {
            collectedCourse = new sportMainItem();

            collectedCourse.setImgUrl("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3385472845,2539383542&fm=11&gp=0.jpg");
            collectedCourse.setCourseName("篮球培优课");
            collectedCourse.setTargetAge("适合4-6岁孩子练习");
            collectedCourse.setLables("亲子A类/网球/坐位体前屈");
            collectedList.add(new MultipleItem(MultipleItem.sport_main_item,collectedCourse));
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