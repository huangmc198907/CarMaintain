package com.exanmple.carmaintain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.exanmple.db.BaoJun560;
import com.exanmple.db.MyDBMaster;

public class MainActivity extends AppCompatActivity {
    private Button carSelectButton,itemSetButton,lastSetButton,myBotton;
    public static MyDBMaster myDBMaster;
    private Context mContext;
    public final static float TEXT_BIG_SIZE = 22;
    public final static float TEXT_MIDDLE_SIZE = 25;
    public final static float TEXT_LITTLE_SIZE = 27;

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
