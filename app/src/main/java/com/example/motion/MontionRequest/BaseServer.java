package com.example.motion.MontionRequest;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

public class BaseServer {
    private final static String SERVER_BASE_URL = "http://106.55.25.94:8080";

    public final static int GET_SPECIAL_SORT = 1;
    public final static int GET_COURSE_TAG_GROUP = 2;
    public final static int GET_COURSE_BY_ID = 3;

    /**
     * 自定义状态码，与http状态码有区别
     */
    public final static int TOKEN_INVALID = 600;


    public static String getServerUrl(int requestId){
        switch (requestId){
            case GET_SPECIAL_SORT:
                return "https://www.fastmock.site/mock/318b7fee143da8b159d3e46048f8a8b3/api";//"http://106.55.25.94:8080";
            default:
                return "http://106.55.25.94:8080";
        }

    }


}
