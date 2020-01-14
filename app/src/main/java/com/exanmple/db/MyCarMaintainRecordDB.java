package com.exanmple.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class MyCarMaintainRecordDB {
    /** 数据表名称 */
    public static final String TABLE_NAME = "my_car_maintain_record";

    /** 表的字段名 */
    public static String KEY_ID = "id";
    public static String KEY_CAR_NAME = "name";
    public static String KEY_LICENSE_PLATE = "license_plate";
    public static String KEY_ITEM_NAME = "item_name";
    public static String KEY_ITEM_MILEAGE = "item_mileage";
    public static String KEY_ITEM_TIME = "item_time";

    private SQLiteDatabase mDatabase;
    /** 上下文 */
    private Context mContext;
    /** 数据库打开帮助类 */
    private MyDBMaster.DBOpenHelper mDbOpenHelper;

    public MyCarMaintainRecordDB(Context context) {
        mContext = context;
    }

    public void setDatabase(SQLiteDatabase db){
        mDatabase = db;
    }

    /**
     * 插入一条数据
     * @param myCarMaintainRecordBean
     * @return
     */
    public long insertData(MyCarMaintainRecordBean myCarMaintainRecordBean) {
        ContentValues values = new ContentValues();
        values.put(KEY_CAR_NAME, myCarMaintainRecordBean.name);
        values.put(KEY_LICENSE_PLATE, myCarMaintainRecordBean.license_plate);
        values.put(KEY_ITEM_NAME, myCarMaintainRecordBean.item_name);
        values.put(KEY_ITEM_MILEAGE, myCarMaintainRecordBean.item_mileage);
        values.put(KEY_ITEM_TIME, myCarMaintainRecordBean.item_time);
        return mDatabase.insert(TABLE_NAME, null, values);
    }

    /**
     * 删除一条数据
     * @param id
     * @return
     */
    public long deleteData(int id) {
        return mDatabase.delete(TABLE_NAME, KEY_ID + "=" + id, null);
    }

    /**
     * 删除所有数据
     * @return
     */
    public long deleteAllData() {
        return mDatabase.delete(TABLE_NAME, null, null);
    }

    /**
     * 更新一条数据
     * @param id
     * @param myCarMaintainRecordBean
     * @return
     */
    public long updateData(int id, MyCarMaintainRecordBean myCarMaintainRecordBean) {
        ContentValues values = new ContentValues();
        values.put(KEY_CAR_NAME, myCarMaintainRecordBean.name);
        values.put(KEY_LICENSE_PLATE, myCarMaintainRecordBean.license_plate);
        values.put(KEY_ITEM_NAME, myCarMaintainRecordBean.item_name);
        values.put(KEY_ITEM_MILEAGE, myCarMaintainRecordBean.item_mileage);
        values.put(KEY_ITEM_TIME, myCarMaintainRecordBean.item_time);
        return mDatabase.update(TABLE_NAME, values, KEY_ID + "=" + id, null);
    }

    /**
     * 查询一条数据
     * @param id
     * @return
     */
    public List<MyCarMaintainRecordBean> queryData(int id) {
        if (!MyDBConfig.HaveData(mDatabase,TABLE_NAME)){
            return null;
        }
        Cursor results = mDatabase.query(TABLE_NAME, new String[]{
                        KEY_ID,
                        KEY_CAR_NAME,
                        KEY_LICENSE_PLATE,
                        KEY_ITEM_NAME,
                        KEY_ITEM_MILEAGE,
                        KEY_ITEM_TIME },
                KEY_ID + "=" + id , null, null, null, null);
        return convertUtil(results);
    }

    /**
     * 查询所有数据
     * @return
     */
    public List<MyCarMaintainRecordBean> queryDataList() {
        if (!MyDBConfig.HaveData(mDatabase,TABLE_NAME)){
            return null;
        }
        Cursor results = mDatabase.query(TABLE_NAME, new String[]{
                        KEY_ID,
                        KEY_CAR_NAME,
                        KEY_LICENSE_PLATE,
                        KEY_ITEM_NAME,
                        KEY_ITEM_MILEAGE,
                        KEY_ITEM_TIME },
                null, null, null, null, null);
        return convertUtil(results);
    }

    /**
     * 查询结果转换
     * @param cursor
     * @return
     */
    private List<MyCarMaintainRecordBean> convertUtil(Cursor cursor) {
        int resultCounts = cursor.getCount();
        if (resultCounts == 0 || !cursor.moveToFirst()) {
            return null;
        }
        List<MyCarMaintainRecordBean> mList = new ArrayList<>();
        for (int i = 0; i < resultCounts; i++) {
            MyCarMaintainRecordBean myCarMaintainRecordBean = new MyCarMaintainRecordBean();
            myCarMaintainRecordBean.id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
            myCarMaintainRecordBean.name = cursor.getString(cursor.getColumnIndex(KEY_CAR_NAME));
            myCarMaintainRecordBean.license_plate = cursor.getString(cursor.getColumnIndex(KEY_LICENSE_PLATE));
            myCarMaintainRecordBean.item_name = cursor.getString(cursor.getColumnIndex(KEY_ITEM_NAME));
            myCarMaintainRecordBean.item_mileage = cursor.getInt(cursor.getColumnIndex(KEY_ITEM_MILEAGE));
            myCarMaintainRecordBean.item_time = cursor.getString(cursor.getColumnIndex(KEY_ITEM_TIME));
            mList.add(myCarMaintainRecordBean);
            cursor.moveToNext();
        }
        return mList;
    }
}
