package com.example.motion.MotionError;

import androidx.annotation.Nullable;

import com.android.volley.VolleyError;

/**
 * 抛出此错误表示请求成功，但返回了Token失效的提示
 */
public class TokenInvalidError extends VolleyError {
    final private String errorMessage = "TOKEN_INVALID";
    @Nullable
    @Override
    public String getMessage() {
        return errorMessage;
    }
}
