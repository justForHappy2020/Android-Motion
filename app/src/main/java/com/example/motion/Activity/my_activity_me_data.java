package com.example.motion.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.motion.Entity.User;
import com.example.motion.R;
import com.example.motion.Utils.UserInfoManager;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;

import static com.example.motion.Utils.ClientUploadUtils.upload;
import static com.example.motion.Utils.HttpUtils.connectHttp;

public class my_activity_me_data extends NeedTokenActivity implements View.OnClickListener{

    private ImageView ivBack;
    private RoundedImageView ivChangeportrait;
    private TextView tvName;
    private TextView tvSex;
    private TextView tvBirth;
    private Button btnSave;

    //dialog的组件
    private EditText etInput_name;
    private Button btnCancel;
    private Button btnAgree;

    private SharedPreferences saveSP;
    private int httpCode;
    private String url;//图片的URL
    private static final int MY_ADD_CASE_CALL_PHONE2 = 7; //打开相册的请求码
    private Context mContext;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private AlertDialog alert1 = null;
    private AlertDialog.Builder builder1 = null;
    private View alertView;
    private String birth = "";
    private String name = "";
    private int gender;

    //private String token;
    private SharedPreferences readSP;
    private Intent intentAccept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_activity_me_data);
        //checkToken();
        initView();
    }
/*
    private void checkToken() {
        readSP=this.getSharedPreferences("saveSp",MODE_PRIVATE);
        token = readSP.getString("token","");
        if (token.isEmpty()){
            finish();
            Toast.makeText(this,"请登录", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, register_activity_register.class);
            startActivity(intent);
        }
    }

 */

    private void initView(){
        intentAccept = getIntent();
        User user = (User)intentAccept.getSerializableExtra("user");
        name = user.getNickName();
        url = user.getHeadPortraitUrl();
        gender = user.getGender();
        birth = user. getBirth();

        ivBack = findViewById(R.id.iv_back);
        ivChangeportrait = findViewById(R.id.iv_changeportrait);
        tvName = findViewById(R.id.tv_name);
        tvSex = findViewById(R.id.tv_sex);
        tvBirth  = findViewById(R.id.tv_birth);
        btnSave = findViewById(R.id.btn_save);
        mContext = my_activity_me_data.this;
        tvName.setText(user.getNickName());
        Glide.with(mContext).load(user.getHeadPortraitUrl()).into(ivChangeportrait);
        final String[] sex = getResources().getStringArray(R.array.me_choose_sex);
        tvSex.setText(sex[user.getGender()]);
        tvBirth.setText(user.getBirth());

        final LayoutInflater inflater = my_activity_me_data.this.getLayoutInflater();
        alertView = inflater.inflate(R.layout.me_dialog_me_data, null,false);
        etInput_name = alertView.findViewById(R.id.et_input_name);
        btnCancel = alertView.findViewById(R.id.btn_cancel);
        btnAgree = alertView.findViewById(R.id.btn_agree);
        builder = new AlertDialog.Builder(mContext);
        builder.setView(alertView);
        builder.setCancelable(false);
        alert = builder.create();

        ivBack.setOnClickListener(this);
        ivChangeportrait.setOnClickListener(this);
        tvName.setOnClickListener(this);
        tvSex.setOnClickListener(this);
        tvBirth.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnAgree.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        saveSP = getSharedPreferences("saved_photo",Context.MODE_PRIVATE);
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
                final String httpurl = "http://10.34.25.45:8080/api/user/modifyHptAndroid";


                //http请求
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String responseData = upload(httpurl , filepath , UserInfoManager.getUserInfoManager(my_activity_me_data.this).getToken()).string();//http请求
                            try {
                                JSONObject jsonObject1 = new JSONObject(responseData);
                                //相应的内容
                                httpCode = jsonObject1.getInt("code");
                                if(httpCode == 200){
                                    url= jsonObject1.getString("data");//URL?
                                    SharedPreferences.Editor editor = saveSP.edit();
                                    editor.putString("url",url);
                                    if (!editor.commit()) {
                                        System.out.println("ERROR commit");
                                    }
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
                if(url != null)startThread();
            } catch (Exception e) {
                //"上传失败");
            }
            if(httpCode==200) Toast.makeText(my_activity_me_data.this,"头像上传成功",Toast.LENGTH_SHORT).show();
            if(httpCode!=200)Toast.makeText(my_activity_me_data.this,"上传头像失败",Toast.LENGTH_SHORT).show();
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

    private Drawable loadImageFromNetwork(String imageUrl)
    {
        Drawable drawable = null;
        try {
            // 可以在这里通过文件名来判断，是否本地有此图片
            drawable = Drawable.createFromStream(
                    new URL(imageUrl).openStream(), "userIcon.jpg");
        } catch (IOException e) {
            Log.d("test", e.getMessage());
        }
        if (drawable == null) {
            Log.d("test", "null drawable");
        } else {
            Log.d("test", "not null drawable");
        }

        return drawable ;
    }

    //显示头像线程
    private void startThread() {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                final Drawable drawable = loadImageFromNetwork(url);
                // post() 特别关键，就是到UI主线程去更新图片
                ivChangeportrait.post(new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        ivChangeportrait.setImageDrawable(drawable) ;
                    }}) ;
            }

        });
        thread.start();
        try {
            thread.join(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_changeportrait:
                //  6.0之后动态申请权限 SD卡写入权限
                //ivChangeportrait.setVisibility(View.GONE);
                if (ContextCompat.checkSelfPermission(my_activity_me_data.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(my_activity_me_data.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_ADD_CASE_CALL_PHONE2);

                } else {
                    //打开相册
                    choosePhoto();
                }
                break;
            case R.id.tv_name:
                alert.show();
                break;
            case R.id.tv_sex:
                final String[] sex = getResources().getStringArray(R.array.me_choose_sex);
                alert1 = null;
                builder1 = new AlertDialog.Builder(mContext);
                alert1 = builder1.setTitle(getResources().getString(R.string.me_choose_sex1))
                                .setSingleChoiceItems(sex, -1, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        gender = which;
                                        tvSex.setText(sex[which]);
                                    }
                                }).create();
                alert1.show();
                alert1.setCanceledOnTouchOutside(true);
                break;
            case R.id.tv_birth:
                birth = "";
                Calendar calendar = Calendar.getInstance();
                new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String m , d;
                        if(++month<10)m = "0" + month;
                        else m = String.valueOf(month);
                        if(dayOfMonth<10)d = "0" + dayOfMonth;
                        else d = String.valueOf(dayOfMonth);
                        birth = year+"-"+ m + "-" + d;
                        tvBirth.setText(birth);
                    }
                }
                        ,calendar.get(Calendar.YEAR)
                        ,calendar.get(Calendar.MONTH)
                        ,calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.btn_cancel:
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.me_close_alert), Toast.LENGTH_SHORT).show();
                alert.dismiss();
                break;
            case R.id.btn_agree:
                name = etInput_name.getText().toString();
                alert.dismiss();
                tvName.setText(name);
                break;
            case R.id.btn_save:
                //http请求，保存数据。
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //设置JSON数据
                            JSONObject json = new JSONObject();
                            try {
                                json.put("token", UserInfoManager.getUserInfoManager(my_activity_me_data.this).getToken());
                                json.put("headPortrait", url);
                                json.put("nickName", name);
                                json.put("gender" , gender );
                                json.put("birthday" , birth);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String url = "http://10.34.25.45:8080/api/community/saveUserdata";
                            String responseData = connectHttp(url,json);
                            getfeedback(responseData);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    private void getfeedback(String responseData) {
                        try {
                            //解析JSON数据
                            JSONObject jsonObject1 = new JSONObject(responseData);
                            httpCode = jsonObject1.getInt("code");
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
                try {
                    thread.join(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(httpCode==200){
                    Toast.makeText(getApplicationContext(), "SUCCESSFUL", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this,"ERROR",Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(my_activity_me_data.this, viewpager_activity_main.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
