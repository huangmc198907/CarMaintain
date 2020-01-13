package com.exanmple.carmaintain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button carSelectButton,itemSetButton,lastSetButton,myBotton;
    private MySQLiteUse mySQLiteUse;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        carSelectButton = (Button) findViewById(R.id.button_car_select);
        itemSetButton = (Button) findViewById(R.id.button_item_set);
        lastSetButton = (Button) findViewById(R.id.button_last_set);
        myBotton = (Button) findViewById(R.id.button_my);

        mContext = MainActivity.this;
        mySQLiteUse = new MySQLiteUse(mContext, "car.db", null, 1);

        MySQLiteUse.mySQLUse.initMySQlite();

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
