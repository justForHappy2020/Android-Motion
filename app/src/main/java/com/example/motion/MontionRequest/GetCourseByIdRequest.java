package com.example.motion.MontionRequest;

import android.util.Log;

import androidx.annotation.GuardedBy;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.example.motion.Beans.PagedItemListBean;
import com.example.motion.Entity.Course;
import com.example.motion.Entity.CourseTag;
import com.example.motion.Entity.CourseTagGroup;
import com.example.motion.Entity.MultipleItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.example.motion.MontionRequest.BaseServer.GET_COURSE_BY_ID;
import static com.example.motion.MontionRequest.BaseServer.GET_COURSE_TAG_GROUP;
import static com.example.motion.MontionRequest.BaseServer.GET_SPECIAL_SORT;

public class GetCourseByIdRequest extends Request<PagedItemListBean> {
    private final static int GetCourseByIdMethod = Method.GET;
    private final static String relativeUrl = "/api/course/getCourseById";
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

    /** Lock to guard mListener as it is cleared on cancel() and read on delivery. */
    private final Object mLock = new Object();

    @Nullable
    @GuardedBy("mLock")
    private Response.Listener<PagedItemListBean> mListener;

    public GetCourseByIdRequest(Map<String,String> param,Response.Listener<PagedItemListBean> listener,
                                @Nullable Response.ErrorListener errorListener) {

        super(GetCourseByIdMethod, fullRequestUrl, errorListener);
        mListener = listener;
        paramMap = param;
    }

    @Override
    public void cancel() {
        super.cancel();
        synchronized (mLock) {
            mListener = null;
        }
    }

    @Override
    @SuppressWarnings("DefaultCharset")
    protected Response<PagedItemListBean> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        parsed = new String(response.data, StandardCharsets.UTF_8);

        List<MultipleItem> onePageCourseList = new ArrayList<>();
        PagedItemListBean pagedItemListBean = new PagedItemListBean();
        
        JSONObject jsonRootObject = null;
        try {
            jsonRootObject = new JSONObject(parsed);
            JSONObject jsonDataObject = jsonRootObject.getJSONObject("data");
            pagedItemListBean.setHasNext(jsonDataObject.getBoolean("hasNext"));
            JSONArray JSONArrayCourse = jsonDataObject.getJSONArray("courseList");
            for (int i = 0; i < JSONArrayCourse.length(); i++) {
                JSONObject jsonCourseObject = JSONArrayCourse.getJSONObject(i);
                //相应的内容
                Course course = new Course();
                course.setCourseId(jsonCourseObject.getLong("courseId"));
                course.setCourseName(jsonCourseObject.getString("courseName"));
                course.setBackgroundUrl(jsonCourseObject.getString("backgroundUrl"));
                course.setTargetAge(jsonCourseObject.getString("targetAge"));
                course.setIsOnline(jsonCourseObject.getInt("online"));

                JSONArray JSONArrayLabels = jsonCourseObject.getJSONArray("labels");
                String labels = "";
                for (int j = 0; j < JSONArrayLabels.length(); j++) {
                    labels += (JSONArrayLabels.get(j) + "/");
                }
                course.setLabels(labels);
                onePageCourseList.add(new MultipleItem(MultipleItem.COURSEFULL, course));
            }
            pagedItemListBean.setCourseList(onePageCourseList);

        } catch (JSONException e) {
            e.printStackTrace();
            pagedItemListBean.setHasNext(false);
            deliverError(new VolleyError("JSON parse fail"));
        }

        return Response.success(pagedItemListBean, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(PagedItemListBean response) {
        Response.Listener<PagedItemListBean> listener;
        synchronized (mLock) {
            listener = mListener;
        }
        if (listener != null) {
            listener.onResponse(response);
        }
    }

}
