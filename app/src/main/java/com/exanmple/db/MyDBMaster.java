package com.exanmple.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
        mDbOpenHelper = new DBOpenHelper(mContext, MyDBConfig.DB_NAME, null,MyDBConfig.DB_VERSION);
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
            CarMaintainDB.KEY_ICON_PATH + " text not null );";

    /** 创建该数据库下CarMaintainItem表的语句 */
    private static final String mCarMaintainItemStr = "create table if not exists " + CarMaintainItemDB.TABLE_NAME + " (" +
            CarMaintainItemDB.KEY_ID + " integer primary key autoincrement , " +
            CarMaintainItemDB.KEY_CAR_NAME + " text not null , " +
            CarMaintainItemDB.KEY_ITEM_NAME + " text not null , " +
            CarMaintainItemDB.KEY_ITEM_MILEAGE_CYCLE + " integer , " +
            CarMaintainItemDB.KEY_ITEM_TIME_CYCLE + " integer );";

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
