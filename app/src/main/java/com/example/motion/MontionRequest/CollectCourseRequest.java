package com.example.motion.MontionRequest;

import android.util.Log;

import androidx.annotation.GuardedBy;
import androidx.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.example.motion.Beans.PagedItemListBean;
import com.example.motion.Entity.Course;
import com.example.motion.Entity.MultipleItem;
import com.example.motion.VolleyRequest.MyStringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.example.motion.MontionRequest.BaseServer.GET_COURSE_BY_ID;

public class CollectCourseRequest extends MyStringRequest {
    private final static int GetCourseByIdMethod = Method.GET;
    private final static String relativeUrl = "/api/course/collectCourse";
    private final static String fullRequestUrl = BaseServer.getServerUrl(GET_COURSE_BY_ID) + relativeUrl;

    private Map<String,String> paramMap;

    @Override
    public String getUrl() {
        String url = super.getUrl();
        url+="?";

        String params = "";
        Iterator iter = paramMap.keySet().iterator();
        while (iter.hasNext()) {
            Object key = iter.next();
            Object val = paramMap.get(key);
            params+=("&"+key.toString()+"="+val.toString());
        }
        url+=params.substring(1);

        Log.d("getUrl",super.getUrl());
        return url;
    }

    public CollectCourseRequest(Map<String,String> param, Response.Listener<String> listener,
                                @Nullable Response.ErrorListener errorListener) {

        super(GetCourseByIdMethod, fullRequestUrl, listener,errorListener);
        paramMap = param;
    }

}
