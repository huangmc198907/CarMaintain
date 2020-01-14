package com.exanmple.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MyDBConfig{
    /**  数据库名称 */
    public static final String DB_NAME = "car.db";
    /**  数据库版本 */
    public static final int DB_VERSION = 1;

    /**
     * 判断数据表是否为空
     * @param db
     * @param tablename
     * @return
     */
    public static boolean HaveData(SQLiteDatabase db, String tablename){
        Cursor cursor;
        boolean a = false;
        cursor = db.rawQuery("select name from sqlite_master where type='table' ", null);
        while(cursor.moveToNext()){
            //遍历出表名
            String name = cursor.getString(0);
            if(name.equals(tablename)){
                a = true;
            }
            Log.i("System.out", name);
        }
        if(a){
            cursor = db.query(tablename,null,null,null,null,null,null);
            //检查是不是空表
            return cursor.getCount() > 0;
        }else {
            return false;
        }
    }
}
