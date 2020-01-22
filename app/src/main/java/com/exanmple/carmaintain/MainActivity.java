package com.exanmple.carmaintain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.exanmple.db.BaoJun560;
import com.exanmple.db.MyDBConfig;
import com.exanmple.db.MyDBMaster;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private Button carSelectButton,itemSetButton,lastSetButton,myBotton;
    public static MyDBMaster myDBMaster;
    private Context mContext;
    public final static float TEXT_BIG_SIZE = 30;
    public final static float TEXT_MIDDLE_SIZE = 40;
    public final static float TEXT_LITTLE_SIZE = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WindowManager wm1 = this.getWindowManager();
        int width1 = wm1.getDefaultDisplay().getWidth();

        carSelectButton = (Button) findViewById(R.id.button_car_select);
        itemSetButton = (Button) findViewById(R.id.button_item_set);
        lastSetButton = (Button) findViewById(R.id.button_last_set);
        myBotton = (Button) findViewById(R.id.button_my);
        carSelectButton.setTextSize(width1 / TEXT_BIG_SIZE);
        itemSetButton.setTextSize(width1 / TEXT_BIG_SIZE);
        lastSetButton.setTextSize(width1 / TEXT_BIG_SIZE);
        myBotton.setTextSize(width1 / TEXT_BIG_SIZE);

        mContext = MainActivity.this;

        //启动数据库
        myDBMaster = new MyDBMaster(getApplicationContext());
        myDBMaster.openDataBase();

        if(myDBMaster.dbIsCreate == true)
            new BaoJun560(MainActivity.myDBMaster);

        carSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CarSelectActivity.class));
            }
        });
        itemSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ItemSetActivity.class));
            }
        });
        lastSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LastSetActivity.class));
            }
        });
        myBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MyActivity.class));
            }
        });
    }
}
