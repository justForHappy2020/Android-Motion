package com.example.motion.VolleyRequest;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.example.motion.Activity.me_activity_bindphone_changephone;
import com.example.motion.Activity.register_activity_register;
import com.example.motion.MotionApplication;
import com.example.motion.VolleyError.TokenInvalidError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import static androidx.core.content.ContextCompat.startActivity;
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
                    Intent intent = new Intent(MotionApplication.context, register_activity_register.class);
                    startActivity(MotionApplication.context,intent,null);
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
