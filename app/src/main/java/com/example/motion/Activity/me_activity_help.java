package com.example.motion.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.motion.Entity.DyxItem;
import com.example.motion.R;
import com.example.motion.Utils.UserInfoManager;
import com.example.motion.Widget.DyxQuickAdapter;
import com.example.motion.VolleyRequest.PostJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.motion.Utils.ClientUploadUtils.upload;

public class me_activity_help extends NeedTokenActivity implements View.OnClickListener{
    private final int SEND_FEEDBACK_FAILED = 0;
    private final int SEND_FEEDBACK_SUCCESS = 1;
    private static final int MY_ADD_CASE_CALL_PHONE2 = 7; //打开相册的请求码
    private String token;
    private ImageView ivBack;
    private TextView tvMyFeedback;
    private TextView tvQuestion;
    private EditText etMainText;
    private EditText etQQ;
    private RecyclerView rvFeedbackPicture;
    private TextView tvSend;
    private Handler handler;
    private int httpCode;
    private DyxQuickAdapter pictureAdapter;
    private List<DyxItem> pictureList = new ArrayList();
    private AlertDialog alert1 = null;
    private AlertDialog.Builder builder1 = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.me_activity_feedback);
        super.onCreate(savedInstanceState);
        initHandler();
        initData();
        initView();
    }

    private void initHandler(){
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case SEND_FEEDBACK_FAILED:
                        Log.d("me_activity_feedback_Handler","SEND_FEEDBACK_FAILED:"+msg.obj);
                        pictureAdapter.notifyDataSetChanged();
                        break;
                    case SEND_FEEDBACK_SUCCESS:
                        Log.d("me_activity_feedback","SEND_FEEDBACK_SUCCESS");
                        pictureAdapter.notifyDataSetChanged();
                        break;
                }
            }
        };
    }

    private  void initData(){
        token = UserInfoManager.getUserInfoManager(this).getToken();
        pictureList.add(new DyxItem(DyxItem.PICTURE , "https://ss2.baidu.com/-vo3dSag_xI4khGko9WTAnF6hhy/baike/w=268/sign=792273edf9edab6474724ac6cf36af81/a08b87d6277f9e2fda25102e1d30e924b899f380.jpg"));
    }

    private void initView(){
        tvQuestion = findViewById(R.id.tv_question);
        ivBack = findViewById(R.id.me_feedback_back);
        tvMyFeedback = findViewById(R.id.tv_my_feedback);
        etMainText = findViewById(R.id.et_feedback_text);
        etQQ = findViewById(R.id.et_feedback_qq);
        rvFeedbackPicture = findViewById(R.id.feedback_picture);
        tvSend = findViewById(R.id.tv_send_feedback);
        tvQuestion.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        tvMyFeedback.setOnClickListener(this);
        tvSend.setOnClickListener(this);
        etMainText.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String s = etMainText.getText().toString();
                if (s.length() != 0) {
                    tvSend.setEnabled(true);
                }else{
                    tvSend.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        GridLayoutManager pictureManager = new GridLayoutManager(this,1,GridLayoutManager.HORIZONTAL,false);
        //pictureManager.setOrientation(GridLayoutManager.HORIZONTAL);
        rvFeedbackPicture.setLayoutManager(pictureManager);
        pictureAdapter = new DyxQuickAdapter(pictureList);
        pictureAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                //最后一个item为"添加照片"
                if(pictureList.get(position).getText().equals("https://ss2.baidu.com/-vo3dSag_xI4khGko9WTAnF6hhy/baike/w=268/sign=792273edf9edab6474724ac6cf36af81/a08b87d6277f9e2fda25102e1d30e924b899f380.jpg")){
                    if (ContextCompat.checkSelfPermission(me_activity_help.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(me_activity_help.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_ADD_CASE_CALL_PHONE2);

                    } else {
                        choosePhoto();
                    }
                }
                else {
                    //查看照片
                }
            }
        });
        rvFeedbackPicture.setAdapter(pictureAdapter);
    }

    /**
     * 打开相册
     */
    private void choosePhoto() {
        //这是打开系统默认的相册(就是你系统怎么分类,就怎么显示,首先展示分类列表)
        Intent picture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(picture, 2);
    }

    /**
     * startActivityForResult执行后的回调方法，接收返回的图片
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode != Activity.RESULT_CANCELED) {

            String state = Environment.getExternalStorageState();
            if (!state.equals(Environment.MEDIA_MOUNTED)) return;
            // 把原图显示到界面上
/*            Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
            Tiny.getInstance().source(readpic()).asFile().withOptions(options).compress(new FileWithBitmapCallback() {
                @Override
                public void callback(boolean isSuccess, Bitmap bitmap, String outfile, Throwable t) {
                    saveImageToServer(bitmap, outfile);//显示图片到imgView上
                }
            });*/
        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK
                && null != data) {
            try {
                Uri photoUri = data.getData();//获取路径
                //final String filename = photoUri.getPath();
                final String filepath = getRealPathFromUriAboveApi19(this,photoUri);//获取绝对路径
                final String httpurl = "http://106.55.25.94:8080/api/user/modifyHptAndroid";


                //http请求
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String responseData = upload(httpurl , filepath , UserInfoManager.getUserInfoManager(me_activity_help.this).getToken()).string();//http请求
                            try {
                                JSONObject jsonObject1 = new JSONObject(responseData);
                                //相应的内容
                                httpCode = jsonObject1.getInt("code");
                                if(httpCode == 200){
                                    String url= jsonObject1.getString("data");//图片URL
                                    pictureList.add(0 , new DyxItem(DyxItem.PICTURE , url));
                                    pictureAdapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });
                thread.start();
                thread.join(5000);
            } catch (Exception e) {
                //"上传失败");
            }
            if(httpCode==200) Toast.makeText(me_activity_help.this,"图片上传成功",Toast.LENGTH_SHORT).show();
            if(httpCode!=200)Toast.makeText(me_activity_help.this,"图片上传失败",Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("NewApi")
    private static String getRealPathFromUriAboveApi19(Context context, Uri uri) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的 uri, 则通过document id来进行处理
            String documentId = DocumentsContract.getDocumentId(uri);
            if (isMediaDocument(uri)) { // MediaProvider
                // 使用':'分割
                String id = documentId.split(":")[1];

                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = {id};
                filePath = getDataColumn(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs);
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                filePath = getDataColumn(context, contentUri, null, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())){
            // 如果是 content 类型的 Uri
            filePath = getDataColumn(context, uri, null, null);
        } else if ("file".equals(uri.getScheme())) {
            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            filePath = uri.getPath();
        }
        return filePath;
    }

    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     * @return
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;

        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.me_feedback_back:
                finish();
                break;
            case R.id.tv_my_feedback:
                Intent intent;
                intent = new Intent(this, me_activity_help_my_feedback.class);
                startActivity(intent);
                break;
            case R.id.tv_send_feedback:
                //发送反馈
                String mainText = String.valueOf(etMainText.getText());
                String s = etQQ.getText().toString();
                Long qq = Long.valueOf(-1);
                if(s.length() != 0) {
                    qq = Long.valueOf(s);
                }
                String pictureURL = "";
                for(int i = 0 ; i < pictureList.size()-1 ; i++){
                    pictureURL = pictureURL + pictureList.get(i).getText();
                    if(i+1< pictureList.size()-1)pictureURL = pictureURL + ";";
                }
                String url = "http://106.55.25.94:8080/api/user/sendFeedback";
                JSONObject json = new JSONObject();
                try {
                    json.put("token", token);
                    json.put("feedbackContent", mainText);
                    json.put("feedbackQQ", qq);
                    json.put("feedbackImg", pictureURL);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                PostJsonRequest postJsonRequest = new PostJsonRequest(Request.Method.POST,url,json.toString(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseStr) {

                        try {
                            JSONObject jsonObject1 = new JSONObject(responseStr);
                            Message msg = handler.obtainMessage();
                            msg.what = SEND_FEEDBACK_SUCCESS;
                            handler.sendMessage(msg);
                            Toast.makeText(me_activity_help.this,"SUCCESS",Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Message msg = handler.obtainMessage();
                        msg.what = SEND_FEEDBACK_FAILED;
                        msg.obj=volleyError.toString();
                        handler.sendMessage(msg);
                        Toast.makeText(me_activity_help.this,"ERROR",Toast.LENGTH_SHORT).show();
                    }
                });

                requestQueue.add(postJsonRequest);
                finish();
                break;
            case R.id.tv_question:
                builder1 = new AlertDialog.Builder(me_activity_help.this);
                alert1 = builder1.setTitle("常见问题")
                        .setMessage("1.有时候卡是很正常的\n2.有bug是很正常的")
                        .create();
                alert1.show();
                alert1.setCanceledOnTouchOutside(true);
                break;
        }
    }


}
