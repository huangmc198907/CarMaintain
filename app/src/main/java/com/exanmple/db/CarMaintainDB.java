package com.exanmple.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class CarMaintainDB {
    /** 数据表名称 */
    public static final String TABLE_NAME = "car_maintain";

    /** 表的字段名 */
    public static String KEY_ID = "id";
    public static String KEY_CAR_NAME = "name";
    public static String KEY_MAINTAIN_MILEAGE_CYCLE = "maintain_mileage_cycle";
    public static String KEY_MAINTAIN_TIME_CYCLE = "maintain_time_cycle";
    public static String KEY_ICON_BYTE = "icon_byte";

    private SQLiteDatabase mDatabase;
    /** 上下文 */
    private Context mContext;
    /** 数据库打开帮助类 */
    private MyDBMaster.DBOpenHelper mDbOpenHelper;

    public CarMaintainDB(Context context) {
        mContext = context;
    }

    public void setDatabase(SQLiteDatabase db){
        mDatabase = db;
    }

    /**
     * 插入一条数据
     * @param carMaintainBean
     * @return
     */
    public long insertData(CarMaintainBean carMaintainBean) {
        ContentValues values = new ContentValues();
        values.put(KEY_CAR_NAME, carMaintainBean.name);
        values.put(KEY_MAINTAIN_MILEAGE_CYCLE, carMaintainBean.maintain_mileage_cycle);
        values.put(KEY_MAINTAIN_TIME_CYCLE, carMaintainBean.maintain_time_cycle);
        values.put(KEY_ICON_BYTE, carMaintainBean.icon_byte);
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
     * @param carMaintainBean
     * @return
     */
    public long updateData(int id, CarMaintainBean carMaintainBean) {
        ContentValues values = new ContentValues();
        values.put(KEY_CAR_NAME, carMaintainBean.name);
        values.put(KEY_MAINTAIN_MILEAGE_CYCLE, carMaintainBean.maintain_mileage_cycle);
        values.put(KEY_MAINTAIN_TIME_CYCLE, carMaintainBean.maintain_time_cycle);
        values.put(KEY_ICON_BYTE, carMaintainBean.icon_byte);
        return mDatabase.update(TABLE_NAME, values, KEY_ID + "=" + id, null);
    }

    /**
     * 查询一条数据
     * @param id
     * @return
     */
    public List<CarMaintainBean> queryData(int id) {
        if (!MyDBConfig.HaveData(mDatabase,TABLE_NAME)){
            return null;
        }
        Cursor results = mDatabase.query(TABLE_NAME, new String[]{
                        KEY_ID,
                        KEY_CAR_NAME,
                        KEY_MAINTAIN_MILEAGE_CYCLE,
                        KEY_MAINTAIN_TIME_CYCLE,
                        KEY_ICON_BYTE },
                KEY_ID + "=" + id , null, null, null, null);
        return convertUtil(results);
    }

    /**
     * 查询所有数据
     * @return
     */
    public List<CarMaintainBean> queryDataList() {
        if (!MyDBConfig.HaveData(mDatabase,TABLE_NAME)){
            return null;
        }
        Cursor results = mDatabase.query(TABLE_NAME, new String[]{
                        KEY_ID,
                        KEY_CAR_NAME,
                        KEY_MAINTAIN_MILEAGE_CYCLE,
                        KEY_MAINTAIN_TIME_CYCLE,
                        KEY_ICON_BYTE },
                null, null, null, null, null );
        return convertUtil(results);
    }

    /**
     * 查询结果转换
     * @param cursor
     * @return
     */
    private List<CarMaintainBean> convertUtil(Cursor cursor) {
        int resultCounts = cursor.getCount();
        if (resultCounts == 0 || !cursor.moveToFirst()) {
            return null;
        }
        List<CarMaintainBean> mList = new ArrayList<>();
        for (int i = 0; i < resultCounts; i++) {
            CarMaintainBean carMaintainBean = new CarMaintainBean();
            carMaintainBean.id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
            carMaintainBean.name = cursor.getString(cursor.getColumnIndex(KEY_CAR_NAME));
            carMaintainBean.maintain_mileage_cycle = cursor.getInt(cursor.getColumnIndex(KEY_MAINTAIN_MILEAGE_CYCLE));
            carMaintainBean.maintain_time_cycle = cursor.getInt(cursor.getColumnIndex(KEY_MAINTAIN_TIME_CYCLE));
            carMaintainBean.icon_byte = cursor.getBlob(cursor.getColumnIndex(KEY_ICON_BYTE));
            mList.add(carMaintainBean);
            Log.d("TEST_DEBUG", "id="+carMaintainBean.id+" 汽车名称="+carMaintainBean.name+" 保养公里数="+carMaintainBean.maintain_mileage_cycle+" 保养周期="+carMaintainBean.maintain_time_cycle+"\n");
            cursor.moveToNext();
        }
        return mList;
    }
}
