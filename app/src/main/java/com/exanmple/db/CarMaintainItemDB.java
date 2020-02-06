package com.exanmple.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class CarMaintainItemDB {
    /** 数据表名称 */
    public static final String TABLE_NAME = "car_maintain_item";

    /** 表的字段名 */
    public static String KEY_ID = "id";
    public static String KEY_CAR_NAME = "name";
    public static String KEY_ITEM_NAME = "item_name";
    public static String KEY_ITEM_MILEAGE_CYCLE = "item_mileage_cycle";
    public static String KEY_ITEM_TIME_CYCLE = "item_time_cycle";
    public static String KEY_ICON_BYTE = "icon_byte";

    private SQLiteDatabase mDatabase;
    /** 上下文 */
    private Context mContext;
    /** 数据库打开帮助类 */
    private MyDBMaster.DBOpenHelper mDbOpenHelper;

    public CarMaintainItemDB(Context context) {
        mContext = context;
    }

    public void setDatabase(SQLiteDatabase db){
        mDatabase = db;
    }

    /**
     * 插入一条数据
     * @param carMaintainItemBean
     * @return
     */
    public long insertData(CarMaintainItemBean carMaintainItemBean) {
        ContentValues values = new ContentValues();
        values.put(KEY_CAR_NAME, carMaintainItemBean.name);
        values.put(KEY_ITEM_NAME, carMaintainItemBean.item_name);
        values.put(KEY_ITEM_MILEAGE_CYCLE, carMaintainItemBean.item_mileage_cycle);
        values.put(KEY_ITEM_TIME_CYCLE, carMaintainItemBean.item_time_cycle);
        values.put(KEY_ICON_BYTE, carMaintainItemBean.icon_byte);
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
     * @param carMaintainItemBean
     * @return
     */
    public long updateData(int id, CarMaintainItemBean carMaintainItemBean) {
        ContentValues values = new ContentValues();
        values.put(KEY_CAR_NAME, carMaintainItemBean.name);
        values.put(KEY_ITEM_NAME, carMaintainItemBean.item_name);
        values.put(KEY_ITEM_MILEAGE_CYCLE, carMaintainItemBean.item_mileage_cycle);
        values.put(KEY_ITEM_TIME_CYCLE, carMaintainItemBean.item_time_cycle);
        values.put(KEY_ICON_BYTE, carMaintainItemBean.icon_byte);
        return mDatabase.update(TABLE_NAME, values, KEY_ID + "=" + id, null);
    }

    /**
     * 查询一条数据
     * @param id
     * @return
     */
    public List<CarMaintainItemBean> queryData(int id) {
        if (!MyDBConfig.HaveData(mDatabase,TABLE_NAME)){
            return null;
        }
        Cursor results = mDatabase.query(TABLE_NAME, new String[]{
                        KEY_ID,
                        KEY_CAR_NAME,
                        KEY_ITEM_NAME,
                        KEY_ITEM_MILEAGE_CYCLE,
                        KEY_ITEM_TIME_CYCLE,
                        KEY_ICON_BYTE },
                KEY_ID + "=" + id , null, null, null, null);
        return convertUtil(results);
    }

    /**
     * 查询所有数据
     * @return
     */
    public List<CarMaintainItemBean> queryDataList() {
        if (!MyDBConfig.HaveData(mDatabase,TABLE_NAME)){
            return null;
        }
        Cursor results = mDatabase.query(TABLE_NAME, new String[]{
                        KEY_ID,
                        KEY_CAR_NAME,
                        KEY_ITEM_NAME,
                        KEY_ITEM_MILEAGE_CYCLE,
                        KEY_ITEM_TIME_CYCLE,
                        KEY_ICON_BYTE },
                null, null, null, null, null);
        return convertUtil(results);
    }

    /**
     * 查询结果转换
     * @param cursor
     * @return
     */
    private List<CarMaintainItemBean> convertUtil(Cursor cursor) {
        int resultCounts = cursor.getCount();
        if (resultCounts == 0 || !cursor.moveToFirst()) {
            return null;
        }
        List<CarMaintainItemBean> mList = new ArrayList<>();
        for (int i = 0; i < resultCounts; i++) {
            CarMaintainItemBean carMaintainItemBean = new CarMaintainItemBean();
            carMaintainItemBean.id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
            carMaintainItemBean.name = cursor.getString(cursor.getColumnIndex(KEY_CAR_NAME));
            carMaintainItemBean.item_name = cursor.getString(cursor.getColumnIndex(KEY_ITEM_NAME));
            carMaintainItemBean.item_mileage_cycle = cursor.getInt(cursor.getColumnIndex(KEY_ITEM_MILEAGE_CYCLE));
            carMaintainItemBean.item_time_cycle = cursor.getInt(cursor.getColumnIndex(KEY_ITEM_TIME_CYCLE));
            carMaintainItemBean.icon_byte = cursor.getBlob(cursor.getColumnIndex(KEY_ICON_BYTE));
            mList.add(carMaintainItemBean);
            cursor.moveToNext();
        }
        return mList;
    }
}
