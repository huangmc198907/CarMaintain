package com.exanmple.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.documentfile.provider.DocumentFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MyDBMaster {
    /** 上下文 */
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private DBOpenHelper mDbOpenHelper;
    public static boolean dbIsCreate = false;

    /** 数据表操作类实例化 */
    public CarMaintainDB carMaintainDB;
    public CarMaintainItemDB carMaintainItemDB;
    public MyCarMaintainRecordDB myCarMaintainRecordDB;

    public MyDBMaster(Context context){
        mContext = context;
        carMaintainDB = new CarMaintainDB(mContext);
        carMaintainItemDB = new CarMaintainItemDB(mContext);
        myCarMaintainRecordDB = new MyCarMaintainRecordDB(mContext);
    }

    /**
     * 打开数据库
     */
    public void openDataBase() {
        mDbOpenHelper = new DBOpenHelper(mContext, MyDBConfig.DB_NAME, null, MyDBConfig.DB_VERSION);
        try {
            mDatabase = mDbOpenHelper.getWritableDatabase();//获取可写数据库
        } catch (SQLException e) {
            mDatabase = mDbOpenHelper.getReadableDatabase();//获取只读数据库
        }
        // 设置数据库的SQLiteDatabase
        carMaintainDB.setDatabase(mDatabase);
        carMaintainItemDB.setDatabase(mDatabase);
        myCarMaintainRecordDB.setDatabase(mDatabase);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public boolean copyStream(InputStream inputStream, OutputStream outputStream) {
        try {
            byte[] buffer = new byte[1024];
            int byteRead;

            while ((byteRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, byteRead);
            }
            inputStream.close();
            outputStream.flush();
            outputStream.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public boolean exportDataBase(String dirPath){
        boolean ret = false;
        Log.d("--Method--", dirPath);
        String outFileName = dirPath.concat("/").concat(MyDBConfig.DB_NAME);
        String inFileName = mContext.getDatabasePath(MyDBConfig.DB_NAME).getPath();
        Log.d("--Method--", outFileName);
        Log.d("--Method--", inFileName);
        try {
            File outFile = new File(outFileName);
            File inFile = new File(inFileName);

            if (!inFile.exists() || !inFile.isFile() || !inFile.canRead()) {
                return false;
            }

            InputStream inputStream = new FileInputStream(inFileName);
            OutputStream outputStream = null;
            if(!Environment.isExternalStorageEmulated(outFile)){
                DocumentFile dfile = DocumentFile.fromFile(outFile);
                outputStream = mContext.getContentResolver().openOutputStream(dfile.getUri());
            }else{
                outputStream = new FileOutputStream(outFileName);
            }

            return copyStream(inputStream, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public boolean importDataBase(String fileName){
        try {
            boolean ret = false;
            File file = new File(fileName);
            String infileName = fileName;
            String outfileName = mContext.getDatabasePath(MyDBConfig.DB_NAME).getPath();
            if(!Environment.isExternalStorageEmulated(file)){
                infileName = mContext.getCacheDir().getPath() + "/" + "tmp.db";
                DocumentFile dfile = DocumentFile.fromFile(file);
                InputStream inputStream = mContext.getContentResolver().openInputStream(dfile.getUri());
                if(false == copyStream(inputStream, new FileOutputStream(infileName))){
                    return false;
                }
            }

            DBOpenHelper dbOpenHelper = new DBOpenHelper(mContext, infileName, null, MyDBConfig.DB_VERSION);
            SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

            if (MyDBConfig.HaveData(db, CarMaintainDB.TABLE_NAME) && MyDBConfig.HaveData(db, CarMaintainItemDB.TABLE_NAME)
                    && MyDBConfig.HaveData(db, MyCarMaintainRecordDB.TABLE_NAME)) {
                db.close();

                return copyStream(new FileInputStream(infileName), new FileOutputStream(outfileName));
            }
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    /**
     * 关闭数据库
     */
    public void closeDataBase() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    /** 创建该数据库下CarMaintain表的语句 */
    private static final String mCarMaintainStr = "create table if not exists " + CarMaintainDB.TABLE_NAME + " (" +
            CarMaintainDB.KEY_ID + " integer primary key autoincrement , " +
            CarMaintainDB.KEY_CAR_NAME + " text not null , " +
            CarMaintainDB.KEY_MAINTAIN_MILEAGE_CYCLE + " integer , " +
            CarMaintainDB.KEY_MAINTAIN_TIME_CYCLE + " integer , " +
            CarMaintainDB.KEY_ICON_BYTE + " blob not null );";

    /** 创建该数据库下CarMaintainItem表的语句 */
    private static final String mCarMaintainItemStr = "create table if not exists " + CarMaintainItemDB.TABLE_NAME + " (" +
            CarMaintainItemDB.KEY_ID + " integer primary key autoincrement , " +
            CarMaintainItemDB.KEY_CAR_NAME + " text not null , " +
            CarMaintainItemDB.KEY_ITEM_NAME + " text not null , " +
            CarMaintainItemDB.KEY_ITEM_MILEAGE_CYCLE + " integer , " +
            CarMaintainItemDB.KEY_ITEM_TIME_CYCLE + " integer , " +
            CarMaintainItemDB.KEY_ICON_BYTE + " blob not null );";

    /** 创建该数据库下MyCarMaintainRecord表的语句 */
    private static final String mMyCarMaintainRecordStr = "create table if not exists " + MyCarMaintainRecordDB.TABLE_NAME + " (" +
            MyCarMaintainRecordDB.KEY_ID + " integer primary key autoincrement , " +
            MyCarMaintainRecordDB.KEY_CAR_NAME + " text not null , " +
            MyCarMaintainRecordDB.KEY_LICENSE_PLATE + " text not null , " +
            MyCarMaintainRecordDB.KEY_ITEM_NAME + " text not null , " +
            MyCarMaintainRecordDB.KEY_ITEM_MILEAGE + " integer , " +
            MyCarMaintainRecordDB.KEY_ITEM_DATE + " text not null );";

    /** 删除该数据库下CarMaintain表的语句 */
    private static final String mCarMaintainDelSql = "DROP TABLE IF EXISTS " + CarMaintainDB.TABLE_NAME;

    /** 删除该数据库下CarMaintainItem表的语句 */
    private static final String mCarMaintainItemDelSql = "DROP TABLE IF EXISTS " + CarMaintainItemDB.TABLE_NAME;

    /** 删除该数据库下MyCarMaintainRecord表的语句 */
    private static final String mMyCarMaintainRecordDelSql = "DROP TABLE IF EXISTS " + MyCarMaintainRecordDB.TABLE_NAME;

    /**
     * 数据表打开帮助类
     */
    public static class DBOpenHelper extends SQLiteOpenHelper {

        public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(mCarMaintainStr);
            db.execSQL(mCarMaintainItemStr);
            db.execSQL(mMyCarMaintainRecordStr);
            dbIsCreate = true;
            Log.d("TEST_DEBUG","onCreate");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(mCarMaintainDelSql);
            db.execSQL(mCarMaintainItemDelSql);
            db.execSQL(mMyCarMaintainRecordDelSql);
            onCreate(db);
        }
    }
}
