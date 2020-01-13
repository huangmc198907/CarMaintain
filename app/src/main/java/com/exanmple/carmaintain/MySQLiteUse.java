package com.exanmple.carmaintain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MySQLiteUse extends SQLiteOpenHelper {
    static MySQLiteUse mySQLUse = null;
    private SQLiteDatabase db;
    private StringBuilder sb;

    public MySQLiteUse(Context context, String name, SQLiteDatabase.CursorFactory factory,
                       int version) {
        super(context, name, factory, version);
        mySQLUse = this;
    }

    @Override
    //数据库第一次创建时被调用
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE person(personid INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR(20))");

    }

    //软件版本号发生改变时调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE person ADD phone VARCHAR(12) NULL");
    }

    public void initMySQlite() {
        if(mySQLUse != null){
            db = mySQLUse.getWritableDatabase();
            ContentValues values1 = new ContentValues();
            values1.put("name", "呵呵~" + 1);
            db.insert("person", null, values1);

            //参数依次是:表名，列名，where约束条件，where中占位符提供具体的值，指定group by的列，进一步约束
            //指定查询结果的排序方式
            Cursor cursor = db.query("person", null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    int pid = cursor.getInt(cursor.getColumnIndex("personid"));
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    sb.append("id：" + pid + "：" + name + "\n");
                } while (cursor.moveToNext());
            }
            cursor.close();

            ContentValues values2 = new ContentValues();
            values2.put("name", "嘻嘻~");
            //参数依次是表名，修改后的值，where条件，以及约束，如果不指定三四两个参数，会更改所有行
            db.update("person", values2, "name = ?", new String[]{"呵呵~2"});

            //参数依次是表名，以及where条件与约束
            db.delete("person", "personid = ?", new String[]{"3"});
        }
    }

}
