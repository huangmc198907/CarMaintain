package com.exanmple.carmaintain;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.exanmple.db.BaoJun560;
import com.exanmple.db.MyDBMaster;

public class MainActivity extends AppCompatActivity {
    private Button carSelectButton,itemSetButton,lastSetButton,myBotton;
    private String path;
    public static MyDBMaster myDBMaster;
    private Context mContext;
    public final static float TEXT_BIG_SIZE = 22;
    public final static float TEXT_MIDDLE_SIZE = 25;
    public final static float TEXT_LITTLE_SIZE = 27;
    public final static int IMPORT_CODE = 41;
    public final static int EXPORT_CODE = 42;

    final public static float getTextSize(Context context, int width){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (width / TEXT_BIG_SIZE / scale + 0.5f);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        carSelectButton = (Button) findViewById(R.id.button_car_select);
        itemSetButton = (Button) findViewById(R.id.button_item_set);
        lastSetButton = (Button) findViewById(R.id.button_last_set);
        myBotton = (Button) findViewById(R.id.button_my);
        float text_size = getTextSize(this, this.getWindowManager().getDefaultDisplay().getWidth());
        carSelectButton.setTextSize(text_size);
        itemSetButton.setTextSize(text_size);
        lastSetButton.setTextSize(text_size);
        myBotton.setTextSize(text_size);

        mContext = MainActivity.this;

        //启动数据库
        myDBMaster = new MyDBMaster(getApplicationContext());
        myDBMaster.openDataBase();

        if(myDBMaster.dbIsCreate == true)
            new BaoJun560(this, MainActivity.myDBMaster);

        carSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CarSelectActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        itemSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ItemSetActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        lastSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LastSetActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        myBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MyActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_db, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.import_db:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//选择数据库
                startActivityForResult(intent, IMPORT_CODE);
                break;
            case R.id.export_db:
                Intent intent1 = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                //intent.setType("*/*");//选择数据库
                startActivityForResult(intent1, EXPORT_CODE);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT | Build.VERSION_CODES.N)
    private String getPathFromUri(final Context context, final Uri uri) {
        if (uri == null) {
            return null;
        }
        // 判斷是否為Android 4.4之後的版本
        final boolean after44 = Build.VERSION.SDK_INT >= 19;
        if (after44 && DocumentsContract.isDocumentUri(context, uri) || DocumentsContract.isTreeUri(uri)) {
            // 如果是Android 4.4之後的版本，而且屬於文件URI
            final String authority = uri.getAuthority();
            Log.d("TEST_DEBUG","------="+ authority);
            // 判斷Authority是否為本地端檔案所使用的
            if ("com.android.externalstorage.documents".equals(authority)) {
                // 外部儲存空間
                final String id;
                if(DocumentsContract.isTreeUri(uri)){
                    id = DocumentsContract.getTreeDocumentId(uri);
                }else{
                    id = DocumentsContract.getDocumentId(uri);
                }
                final String[] divide = id.split(":");
                final String type = divide[0];
                if ("primary".equals(type)) {
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath().concat("/").concat(divide[1]);
                    return path;
                } else {
                    String path = "/storage/".concat(type).concat("/").concat(divide[1]);
                    return path;
                }
            } else if ("com.android.providers.downloads.documents".equals(authority)) {
                // 下載目錄
                final String id;
                if(DocumentsContract.isTreeUri(uri)){
                    id = DocumentsContract.getTreeDocumentId(uri);
                }else{
                    id = DocumentsContract.getDocumentId(uri);
                }

                Log.d("TEST_DEBUG", "docId===" + id);
                if (id.startsWith("raw:")) {
                    final String path = id.replaceFirst("raw:", "");
                    return path;
                }else if(id.startsWith("msf:")){
                    //final String[] divide = id.split(":");
                    //final Uri downloadUri = ContentUris.withAppendedId(Uri.parse("content://downloads/my_downloads"), Long.parseLong(divide[1]));
                    //String path = queryAbsolutePath(context, downloadUri);
                    //return path;
                    return null;
                }else if(id.startsWith("msd:")){
                    //final String[] divide = id.split(":");
                    //Log.d("TEST_DEBUG", "111dirID===" + id);
                    //final Uri downloadUri = ContentUris.withAppendedId(Uri.parse("content://downloads/my_downloads"), Long.parseLong(divide[1]));
                    //String path = queryAbsolutePath(context, downloadUri);
                    //return path;
                    return null;
                }else if("downloads".equals(id)) {
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath().concat("/").concat("Download");
                    return path;
                }else if(Long.parseLong(id) > 0){
                    final Uri downloadUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(id));
                    String path = queryAbsolutePath(context, downloadUri);
                    return path;
                }
                return null;
            } else if ("com.android.providers.media.documents".equals(authority)) {
                // 圖片、影音檔案
                final String id;
                if(DocumentsContract.isTreeUri(uri)){
                    id = DocumentsContract.getTreeDocumentId(uri);
                }else{
                    id = DocumentsContract.getDocumentId(uri);
                }
                final String[] divide = id.split(":");
                final String type = divide[0];
                Uri mediaUri = null;
                if ("image".equals(type)) {
                    mediaUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    mediaUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    mediaUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                } else {
                    return null;
                }
                mediaUri = ContentUris.withAppendedId(mediaUri, Long.parseLong(divide[1]));
                String path = queryAbsolutePath(context, mediaUri);
                return path;
            }
        } else {
            // 如果是一般的URI
            final String scheme = uri.getScheme();
            String path = null;
            if ("content".equals(scheme)) {
                // 內容URI
                path = queryAbsolutePath(context, uri);
            } else if ("file".equals(scheme)) {
                // 檔案URI
                path = uri.getPath();
            }
            return path;
        }
        return null;
    }

    private static String queryAbsolutePath(final Context context, final Uri uri) {
        final String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                Log.d("TEST_DEBUG", "11111===" + cursor.toString());
                final int index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                return cursor.getString(index);
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    private void importOrExportfile(int code){
        if(path != null) {
            String logString = code == IMPORT_CODE ? "导入" : "导出";
            boolean ret = false;
            if(code == IMPORT_CODE){
                ret = myDBMaster.importDataBase(path);
            }else if(code == EXPORT_CODE){
                ret = myDBMaster.exportDataBase(path);
            }
            if (true == ret) {
                Toast.makeText(this, "数据库"+ logString + "成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "数据库"+ logString + "失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXPORT_CODE || requestCode == IMPORT_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                importOrExportfile(requestCode);
            } else {
                // Permission Denied
            }
        }
    }

    private void checkPermission(int code){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    code);
        }else{
            if(code == IMPORT_CODE || code == EXPORT_CODE) {
                importOrExportfile(code);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT | Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode ==RESULT_OK) {
            myDBMaster.closeDataBase();
            if (requestCode == IMPORT_CODE || requestCode == EXPORT_CODE) {
                Uri uri = data.getData();
                path = getPathFromUri(this, uri);
                if(path != null){
                    String logString = requestCode == IMPORT_CODE ? "导入数据库路径=" : "导出数据库目录=";
                    Log.d("TEST_DEBUG",logString + path);
                    checkPermission(requestCode);
                } else {
                    String logString = requestCode == IMPORT_CODE ? "文件路径不能识别，选择其他目录的文件" : "目录路径不能识别，选择其他目录";
                    Toast.makeText(this, logString, Toast.LENGTH_SHORT).show();
                }
            }
            myDBMaster.openDataBase();
        }
    }
}
