package com.exanmple.carmaintain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MyActivity extends AppCompatActivity {
    private Button maintainRecordButton,nextMaintinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        TextView textView = (TextView)findViewById(R.id.my_text);
        List maintain_list = MainActivity.myDBMaster.myCarMaintainRecordDB.queryDataList();
        maintainRecordButton = (Button) findViewById(R.id.button_maintain_record);
        nextMaintinButton = (Button) findViewById(R.id.button_next_maintain);
        float text_size = MainActivity.getTextSize(this, this.getWindowManager().getDefaultDisplay().getWidth());
        maintainRecordButton.setTextSize(text_size);
        nextMaintinButton.setTextSize(text_size);
        if(null != maintain_list){
            textView.setText("已设置车辆信息");
            textView.setTextSize(text_size);
            maintainRecordButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MyActivity.this,MaintainRecordActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
            nextMaintinButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MyActivity.this,NextActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
        }
    }
}
