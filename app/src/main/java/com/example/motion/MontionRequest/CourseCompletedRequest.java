package com.example.motion.MontionRequest;

import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.example.motion.VolleyError.TokenInvalidError;
import com.example.motion.VolleyRequest.MyStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

import static com.example.motion.MontionRequest.BaseServer.GET_COURSE_BY_ID;
import static com.example.motion.MontionRequest.BaseServer.TOKEN_INVALID;

public class CourseCompletedRequest extends MyStringRequest {
    private final static int CourseCompletedMethod = Method.GET;
    private final static String relativeUrl = "/api/course/courseCompleted";
    private final static String fullRequestUrl = BaseServer.getServerUrl(GET_COURSE_BY_ID) + relativeUrl;

    private Map<String,String> paramMap;

    /**
     *
     * @param response
     * @return String totalTime : 课程累计完成次数
     */
    @Override
    @SuppressWarnings("DefaultCharset")
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        String totalTime;
        parsed = new String(response.data, Charset.defaultCharset());

        try {
            JSONObject root = new JSONObject(parsed);
            JSONObject data = root.getJSONObject("data");
            totalTime = String.valueOf(data.getInt("totalTime"));

            switch(root.getInt("code")){
                case TOKEN_INVALID:
                    return Response.error(new TokenInvalidError());
                default:
                    return Response.success(totalTime, HttpHeaderParser.parseCacheHeaders(response));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return Response.error(new ParseError());
        }

    }

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

    /**
     *
     * @param param :
     *     courseId: Long  //complete courseId
     *     token: String   //user token
     *     time: int       //practised course consuming duration (second)
     * @param listener :on success listener
     * @param errorListener :on fail listener
     */
    public CourseCompletedRequest(Map<String,String> param, Response.Listener<String> listener,
                                  @Nullable Response.ErrorListener errorListener) {

        super(CourseCompletedMethod, fullRequestUrl, listener,errorListener);
        paramMap = param;
    }

}
