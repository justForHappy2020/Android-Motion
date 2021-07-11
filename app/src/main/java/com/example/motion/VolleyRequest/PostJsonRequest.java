package com.example.motion.VolleyRequest;

import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.raizlabs.android.dbflow.annotation.provider.ContentUri;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.example.motion.MontionRequest.BaseServer.TOKEN_INVALID;

public class PostJsonRequest extends Request<String> {
    private final String CONTENT_TYPE_JSON_UTF8 = "application/json;charset=utf-8";
    private Response.Listener<String> responseListener;
    private String requestJsonStr;
    private String url;


    public PostJsonRequest(int method, String url,String requestJsonStr, Response.Listener<String> responseListener,@Nullable Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.responseListener=responseListener;
        this.url=url;
        this.requestJsonStr=requestJsonStr;

    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        if (VolleyLog.DEBUG) {
            if (response.headers != null) {
                for (Map.Entry<String, String> entry : response.headers
                        .entrySet()) {
                    VolleyLog.d(entry.getKey() + "=" + entry.getValue());
                }
            }
        }

        String parsed;
        try {
            parsed = new String(response.data,CONTENT_TYPE_JSON_UTF8);
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }

        try {
            JSONObject root = new JSONObject(parsed);
            switch(root.getInt("code")){
                case TOKEN_INVALID:
                    return Response.error(new VolleyError("TOKEN_INVALID"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



        return Response.success(parsed,
                HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(String response) {
        responseListener.onResponse(response);
    }

    @Override
    public String getBodyContentType() {
        return CONTENT_TYPE_JSON_UTF8;
    }

    @Override
    public byte[] getBody() {
        return this.requestJsonStr.getBytes();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        VolleyLog.d("getHeaders");
        Map<String, String> headers = super.getHeaders();

        if (headers == null || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();
        }


        return headers;
    }

}
