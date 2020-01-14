package com.exanmple.carmaintain;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.exanmple.db.CarMaintainBean;
import com.exanmple.db.CarMaintainItemBean;
import com.exanmple.db.MyCarMaintainRecordBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CarSelectActivity extends AppCompatActivity {
    // fruitList用于存储数据
    private List<Fruit> fruitList=new ArrayList<>();
    private String car_name = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_select);

        // 先拿到数据并放在适配器上
        initFruits(); //初始化水果数据
        FruitAdapter adapter=new FruitAdapter(CarSelectActivity.this,R.layout.fruit_item,fruitList);

        // 将适配器上的数据传递给listView
        ListView listView=findViewById(R.id.car_list);
        listView.setAdapter(adapter);

        // 为ListView注册一个监听器，当用户点击了ListView中的任何一个子项时，就会回调onItemClick()方法
        // 在这个方法中可以通过position参数判断出用户点击的是那一个子项
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fruit fruit=fruitList.get(position);
                Log.d("TEST_DEBUG","车辆列表中，点击车辆名称="+fruit.getName()+" 列表id="+position);
                showInputDialog();
            }
        });
    }

    // 初始化数据
    private void initFruits(){
        List carlist = MainActivity.myDBMaster.carMaintainDB.queryDataList();
        if (null != carlist) {
            for(int i=0; i < carlist.size(); i++){
                CarMaintainBean carMaintainBean = (CarMaintainBean) carlist.get(i);
                Fruit a = new Fruit("" + carMaintainBean.name, R.drawable.baojun);
                car_name = carMaintainBean.name;
                fruitList.add(a);
            }
        }
    }

    private void showInputDialog(){
        final EditText inputServer = new EditText(CarSelectActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(CarSelectActivity.this);
        builder.setTitle("输入车牌号").setIcon(R.drawable.baojun).setView(inputServer)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String mMeetName = inputServer.getText().toString();
                //do something...
                Log.d("TEST_DEBUG", "输入车牌号为：" + mMeetName);
                if(mMeetName.length() > 0){
                    List recordList = MainActivity.myDBMaster.myCarMaintainRecordDB.queryDataList();
                    if (null != recordList) {
                        for(int i = 0; i < recordList.size(); i++){
                            MyCarMaintainRecordBean myCarMaintainRecordBean = (MyCarMaintainRecordBean) recordList.get(i);
                            Log.d("TEST_DEBUG", "已有车型：" + myCarMaintainRecordBean.name+" 车牌号："+myCarMaintainRecordBean.license_plate);
                            if( car_name.equals(myCarMaintainRecordBean.name) && mMeetName.equals(myCarMaintainRecordBean.license_plate) ) {
                                TextView textView = findViewById(R.id.car_text);
                                textView.setText("以设置该车牌号车型");
                                Log.d("TEST_DEBUG", "以设置该车牌号车型");
                                return;
                            }
                        }
                    }
                    createCarFirstRecord(mMeetName);
                }
            }
        });
        builder.show();
    }

    private void createCarFirstRecord(String license_plate){
        List carlist = MainActivity.myDBMaster.carMaintainItemDB.queryDataList();
        if (null != carlist) {
            MyCarMaintainRecordBean myCarMaintainRecordBean = new MyCarMaintainRecordBean();
            myCarMaintainRecordBean.name = car_name;
            myCarMaintainRecordBean.license_plate = license_plate;
            myCarMaintainRecordBean.item_mileage = 5000;
            myCarMaintainRecordBean.item_time = "";

            TextView textView = findViewById(R.id.car_text);
            textView.setText("设置车型成功："+car_name+"\n车牌号："+license_plate);

            for(int i=0; i < carlist.size(); i++){
                CarMaintainItemBean carMaintainItemBean = (CarMaintainItemBean) carlist.get(i);
                myCarMaintainRecordBean.item_name = carMaintainItemBean.item_name;
                MainActivity.myDBMaster.myCarMaintainRecordDB.insertData(myCarMaintainRecordBean);
            }
        }
    }
}
