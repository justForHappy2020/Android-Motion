package com.example.motion.MontionRequest;

import android.util.Log;

import androidx.annotation.GuardedBy;
import androidx.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.example.motion.Entity.CourseTag;
import com.example.motion.Entity.CourseTagGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.example.motion.MontionRequest.BaseServer.GET_COURSE_TAG_GROUP;

public class GetCourseTagGroupRequest extends Request<List<CourseTagGroup>> {
    private final static int GetSpecialSortRequestMethod = Method.GET;
    private final static String relativeUrl = "/api/CourseClass/getSorts";
    private final static String fullRequestUrl = BaseServer.getServerUrl(GET_COURSE_TAG_GROUP) + relativeUrl;

    /** Lock to guard mListener as it is cleared on cancel() and read on delivery. */
    private final Object mLock = new Object();

    @Nullable
    @GuardedBy("mLock")
    private Response.Listener<List<CourseTagGroup>> mListener;

    public GetCourseTagGroupRequest(Response.Listener<List<CourseTagGroup>> listener,
                                    @Nullable Response.ErrorListener errorListener) {

        super(GetSpecialSortRequestMethod, fullRequestUrl, errorListener);
        mListener = listener;
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
    protected Response<List<CourseTagGroup>> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        parsed = new String(response.data, StandardCharsets.UTF_8);

        /**
         * only for test
         */
        Log.d("GetSpecialSortRequest",parsed);

        List<CourseTagGroup> courseTagGroupList = new ArrayList<>();

        JSONObject jsonRootObject = null;
        try {
            jsonRootObject = new JSONObject(parsed);
            JSONArray jsonTagGroupArray = jsonRootObject.getJSONArray("data");
            
            for(int j=0;j<jsonTagGroupArray.length();j++){
                CourseTagGroup courseTagGroup = new CourseTagGroup();
                courseTagGroup.setCourseTagList(new ArrayList<>());
                JSONObject jsonTagsRootObject = jsonTagGroupArray.getJSONObject(j);
                courseTagGroup.setGroupId(jsonTagsRootObject.getInt("id"));
                courseTagGroup.setGroupName(jsonTagsRootObject.getString("name"));

                JSONArray JSONArrayTags = jsonTagsRootObject.getJSONArray("tab");
                for (int i = 0; i < JSONArrayTags.length(); i++) {
                    JSONObject jsonTagObject = JSONArrayTags.getJSONObject(i);

                    CourseTag courseTag = new CourseTag();
                    courseTag.setTagId(jsonTagObject.getInt("courseClassId"));
                    courseTag.setTagName(jsonTagObject.getString("classValue"));
                    courseTag.setTagUrl(jsonTagObject.getString("classUrl"));
                    courseTagGroup.getCourseTagList().add(courseTag);
                }
                courseTagGroupList.add(courseTagGroup);
            }
            
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Response.success(courseTagGroupList, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(List<CourseTagGroup> response) {
        Response.Listener<List<CourseTagGroup>> listener;
        synchronized (mLock) {
            listener = mListener;
        }
        if (listener != null) {
            listener.onResponse(response);
        }
    }

}
