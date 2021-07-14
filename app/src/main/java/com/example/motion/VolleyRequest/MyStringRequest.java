package com.example.motion.VolleyRequest;

import androidx.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import static com.example.motion.MontionRequest.BaseServer.TOKEN_INVALID;

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

        try {
            JSONObject root = new JSONObject(parsed);
            switch(root.getInt("code")){
                case TOKEN_INVALID:
                    return Response.error(new VolleyError("TOKEN_INVALID"));
                default:
                    return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return Response.error(new VolleyError("JSON parse fail"));
        }

    }
}
