package com.example.motion.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.motion.Activity.register_activity_register;
import com.example.motion.MotionError.TokenInvalidError;
import com.example.motion.R;

public class BaseNetworkFragment extends Fragment {

    protected RequestQueue requestQueue;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        //informNetworkState(isNetworkAvailable());
    }

    private void informNetworkState(Boolean available){
        if(!available){
            Toast.makeText(getContext(), getText(R.string.NO_NETWORK), Toast.LENGTH_SHORT).show();
        }
    }
    
    private boolean isNetworkAvailable() {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    Log.i("BaseNetworkActivity", "isNetworkAvailable: " + "===状态===" + networkInfo[i].getState() + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected void checkVolleyError(Object e){
        if(e != null){
            if(e instanceof TimeoutError){
                Toast.makeText(getContext(), "网络连接超时", Toast.LENGTH_SHORT).show();
                return;
            }
            if(e instanceof ServerError){
                Toast.makeText(getContext(), "服务器内部错误", Toast.LENGTH_SHORT).show();
                return;
            }
            if(e instanceof NetworkError){
                Toast.makeText(getContext(), "网络异常", Toast.LENGTH_SHORT).show();
                return;
            }
            if(e instanceof ParseError){
                Toast.makeText(getContext(), "服务器响应解析失败", Toast.LENGTH_SHORT).show();
                return;
            }
            if(e instanceof TokenInvalidError){
                Toast.makeText(getContext(), "登陆状态失效，请尝试重新登陆", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), register_activity_register.class);
                startActivity(intent);
                return;
            }
            Toast.makeText(getContext(), "未知错误", Toast.LENGTH_SHORT).show();
        }
    }
}