package com.example.motion.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.motion.R;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import static com.example.motion.Utils.ClientUploadUtils.upload;

public class my_activity_me_data extends Activity implements View.OnClickListener{

    private ImageView ivBack;
    private RoundedImageView ivChangeportrait;
    private TextView tvName;
    private TextView tvSex;
    private TextView tvBirth;

    private SharedPreferences saveSP;
    private int httpCode;
    private String url;//图片的URL
    private static final int MY_ADD_CASE_CALL_PHONE2 = 7; //打开相册的请求码
    private Context mContext;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_activity_me_data);
        initView();
    }

    private void initView(){
        ivBack = findViewById(R.id.iv_back);
        ivChangeportrait = findViewById(R.id.iv_changeportrait);
        tvName = findViewById(R.id.tv_name);
        tvSex = findViewById(R.id.tv_sex);
        tvBirth  = findViewById(R.id.tv_birth);
        mContext = my_activity_me_data.this;


        ivBack.setOnClickListener(this);
        ivChangeportrait.setOnClickListener(this);
        tvName.setOnClickListener(this);
        tvSex.setOnClickListener(this);
        tvBirth.setOnClickListener(this);

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
                final String httpurl = "http://159.75.2.94:8080/api/user/uploadImageAndroid";


                //http请求
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String responseData = upload(httpurl,filepath).string();//http请求
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
                ivChangeportrait.setVisibility(View.GONE);
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
                break;
            case R.id.tv_sex:
                final String[] sex = new String[]{"男", "女"};
                break;
            case R.id.tv_birth:
                break;
        }
    }
}
