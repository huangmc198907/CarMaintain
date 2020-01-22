package com.exanmple.carmaintain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.exanmple.db.MyCarMaintainRecordBean;

import java.util.List;

public class MyActivity extends AppCompatActivity {
    private Button maintainRecordButton,nextMaintinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        WindowManager wm1 = this.getWindowManager();
        int width1 = wm1.getDefaultDisplay().getWidth();

        TextView textView = (TextView)findViewById(R.id.my_text);
        List maintain_list = MainActivity.myDBMaster.myCarMaintainRecordDB.queryDataList();
        if(null != maintain_list){
            textView.setText("已设置车辆信息");
            textView.setTextSize(width1 / MainActivity.TEXT_BIG_SIZE);
        }

        maintainRecordButton = (Button) findViewById(R.id.button_maintain_record);
        nextMaintinButton = (Button) findViewById(R.id.button_next_maintain);
        maintainRecordButton.setTextSize(width1 / MainActivity.TEXT_BIG_SIZE);
        nextMaintinButton.setTextSize(width1 / MainActivity.TEXT_BIG_SIZE);
        maintainRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyActivity.this,MaintainRecordActivity.class));
            }
        });
        nextMaintinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyActivity.this,NextActivity.class));
            }
        });
    }
}
