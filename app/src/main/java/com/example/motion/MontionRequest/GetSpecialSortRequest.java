package com.example.motion.MontionRequest;

import android.util.Log;

import androidx.annotation.GuardedBy;
import androidx.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.example.motion.Entity.BannerTag;
import com.example.motion.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.example.motion.Entity.CourseTag.TAG_ALL;
import static com.example.motion.Entity.CourseTag.TAG_SORT_HOT;
import static com.example.motion.Entity.CourseTagGroup.TAG_GROUP_SORT;
import static com.example.motion.MontionRequest.BaseServer.GET_SPECIAL_SORT;

public class GetSpecialSortRequest extends Request<List<BannerTag>> {
    private final static int GetSpecialSortRequestMethod = Method.GET;
    private final static String relativeUrl = "/api/CourseClass/getSpecialSort";
    private final static String fullRequestUrl = BaseServer.getServerUrl(GET_SPECIAL_SORT) + relativeUrl;


    /** Lock to guard mListener as it is cleared on cancel() and read on delivery. */
    private final Object mLock = new Object();

    @Nullable
    @GuardedBy("mLock")
    private Response.Listener<List<BannerTag>> mListener;

    public GetSpecialSortRequest( Response.Listener<List<BannerTag>> listener,
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
    protected Response<List<BannerTag>> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        parsed = new String(response.data, StandardCharsets.UTF_8);

        /**
         * only for test
         */
        Log.d("GetSpecialSortRequest",parsed);

        List<BannerTag> bannerTagList = new ArrayList<>();

        JSONObject jsonRootObject = null;
        try {
            jsonRootObject = new JSONObject(parsed);
            JSONArray jsonTagGroupArray = jsonRootObject.getJSONArray("data");

            for(int j=0;j<jsonTagGroupArray.length();j++){
                JSONObject bannerTagJsonObject = jsonTagGroupArray.getJSONObject(j);

                BannerTag bannerTag = new BannerTag();
                bannerTag.setSortId(bannerTagJsonObject.getInt("sortId"));
                bannerTag.setTagId(bannerTagJsonObject.getInt("tagId"));
                bannerTag.setImgUrl(bannerTagJsonObject.getString("imgUrl"));
                bannerTag.setBannerName(bannerTagJsonObject.getString("bannerName"));
                bannerTagList.add(bannerTag);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Response.success(bannerTagList, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(List<BannerTag> response) {
        Response.Listener<List<BannerTag>> listener;
        synchronized (mLock) {
            listener = mListener;
        }
        if (listener != null) {
            listener.onResponse(response);
        }
    }

}
