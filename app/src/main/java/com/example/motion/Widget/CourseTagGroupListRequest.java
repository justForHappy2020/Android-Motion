package com.example.motion.Widget;

import androidx.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.example.motion.Entity.CourseTagGroup;

import java.util.List;
import java.util.Map;

public class CourseTagGroupListRequest extends Request<List<CourseTagGroup>> {
    private Response.Listener<List<CourseTagGroup>> responseListener;
    private Map params;

    public CourseTagGroupListRequest(int method, Map params, String url, Response.Listener<List<CourseTagGroup>> responseListener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.responseListener=responseListener;
        this.params=params;
    }

    @Override
    protected Response<List<CourseTagGroup>> parseNetworkResponse(NetworkResponse response) {

        return null;
    }

    @Override
    protected void deliverResponse(List<CourseTagGroup> response) {
        responseListener.onResponse(response);
    }

    @Override
    public Map getParams() {
        return params;
    }
}
