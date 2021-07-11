package com.example.motion.VolleyRequest;

import androidx.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class MyStringRequest extends StringRequest {
    public MyStringRequest(int method, String url, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public MyStringRequest(String url, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    @Override
    @SuppressWarnings("DefaultCharset")
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        parsed = new String(response.data,Charset.defaultCharset());
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }
}
