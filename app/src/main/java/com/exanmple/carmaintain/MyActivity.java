package com.exanmple.carmaintain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MyActivity extends AppCompatActivity {
    private Button maintainRecordButton,nextMaintinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        maintainRecordButton = (Button) findViewById(R.id.button_maintain_record);
        nextMaintinButton = (Button) findViewById(R.id.button_next_maintain);
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
