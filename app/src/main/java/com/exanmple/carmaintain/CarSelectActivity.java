package com.exanmple.carmaintain;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.exanmple.db.CarMaintainBean;
import com.exanmple.db.CarMaintainItemBean;
import com.exanmple.db.MyCarMaintainRecordBean;

import java.util.ArrayList;
import java.util.List;

public class CarSelectActivity extends AppCompatActivity {
    // fruitList用于存储数据
    private List<Fruit> fruitList=new ArrayList<>();
    private String car_name = null;
    private static boolean longClickFlag = false;
    private FruitAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_select);

        Button addButton = (Button)findViewById(R.id.add_car);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = findViewById(R.id.car_select_text);
                textView.setText("");

                AlertDialog.Builder builder = new AlertDialog.Builder(CarSelectActivity.this);
                builder.setTitle("添加新车型");// 设置标题

                LinearLayout layout = new LinearLayout(CarSelectActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText car_name = new EditText(CarSelectActivity.this);
                car_name.setHint("车辆名称和型号");

                final EditText car_mileage = new EditText(CarSelectActivity.this);
                car_mileage.setHint("车辆保养公里数周期");
                car_mileage.setInputType(InputType.TYPE_CLASS_NUMBER);

                final EditText car_time = new EditText(CarSelectActivity.this);
                car_time.setHint("车辆保养时间周期");
                car_time.setInputType(InputType.TYPE_CLASS_NUMBER);

                layout.addView(car_name);
                layout.addView(car_mileage);
                layout.addView(car_time);
                builder.setView(layout);

                // 为对话框设置取消按钮
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                    }
                });

                // 为对话框设置确定按钮
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        String carNameString = car_name.getText().toString();
                        String carMileageString = car_mileage.getText().toString();
                        String carTimeString = car_time.getText().toString();
                        if(!carNameString.equals("") && !carMileageString.equals("") && !carTimeString.equals("")) {
                            addNewCar(carNameString, carMileageString, carTimeString);
                        }else{
                            TextView textView = findViewById(R.id.car_select_text);
                            textView.setText("输入信息不完整");
                            textView.setTextColor(Color.rgb(255, 0, 0));
                        }
                    }
                });
                builder.create().show();// 使用show()方法显示对话框
            }
        });

        // 先拿到数据并放在适配器上
        initFruits(); //初始化水果数据
        adapter=new FruitAdapter(CarSelectActivity.this,R.layout.fruit_item,fruitList);

        // 将适配器上的数据传递给listView
        final ListView listView=findViewById(R.id.car_select_list);
        listView.setAdapter(adapter);

        // 为ListView注册一个监听器，当用户点击了ListView中的任何一个子项时，就会回调onItemClick()方法
        // 在这个方法中可以通过position参数判断出用户点击的是那一个子项
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = findViewById(R.id.car_select_text);
                textView.setText("");
                Fruit fruit=fruitList.get(position);
                Log.d("TEST_DEBUG","车辆列表中，点击车辆名称="+fruit.getName()+" 列表id="+position);
                if(longClickFlag != true) {
                    setCarDialog();
                }else{
                    longClickFlag = false;
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = findViewById(R.id.car_select_text);
                textView.setText("");
                longClickFlag = true;
                Fruit fruit=fruitList.get(position);
                Log.d("TEST_DEBUG","车辆列表中，点击车辆名称="+fruit.getName()+" 列表id="+position);
                Log.d("TEST_DEBUG", "条目总数为："+parent.getCount());
                if(parent.getCount() > 1) {
                    deletDialog(fruit.getName());
                }else{
                    textView.setText("只有一款车型不能删除");
                    textView.setTextColor(Color.rgb(255, 0, 0));
                }
                return false;
            }
        });
    }

    private void addNewCar(String car_name, String car_mileage, String car_time){
        List car_list = MainActivity.myDBMaster.carMaintainDB.queryDataList();
        if(null != car_list){
            for(int i = 0; i < car_list.size(); i++){
                CarMaintainBean carMaintainBean = (CarMaintainBean)car_list.get(i);
                if(car_name.equals(carMaintainBean.name)){
                    TextView textView = findViewById(R.id.car_select_text);
                    textView.setText("已有该车型");
                    textView.setTextColor(Color.rgb(255, 0, 0));
                    return;
                }
            }

            selectItemCopyFromCar(car_name, car_mileage, car_time);
            CarMaintainBean carMaintainBean = new CarMaintainBean();
            carMaintainBean.name = car_name;
            carMaintainBean.maintain_mileage_cycle = Integer.parseInt(car_mileage);
            carMaintainBean.maintain_time_cycle = Integer.parseInt(car_time);
            carMaintainBean.icon_path = "";
            MainActivity.myDBMaster.carMaintainDB.insertData(carMaintainBean);
            Log.d("TEST_DEBUG", "addNewCar: 汽车名称："+carMaintainBean.name+" 保养公里数："+carMaintainBean.maintain_mileage_cycle+" 保养时间间隔："+carMaintainBean.maintain_time_cycle);
            fruitList.clear();
            initFruits();
            adapter.notifyDataSetChanged();
        }
    }

    private void selectItemCopyFromCar(final String car_name, String car_mileage, String car_time){
        AlertDialog.Builder builder = new AlertDialog.Builder(CarSelectActivity.this);
        builder.setTitle("选择需要复制的保养项目车型");
        List list = MainActivity.myDBMaster.carMaintainDB.queryDataList();
        if(null != list) {
            final String[] items = new String[list.size()];// 创建一个存放选项的数组
            final boolean[] checkedItems = new boolean[list.size()];// 存放选中状态，true为选中
            for(int i=0; i < list.size(); i++){
                CarMaintainBean carMaintainBean = (CarMaintainBean)list.get(i);
                items[i] = carMaintainBean.name;
                checkedItems[i] = false;
            }
            checkedItems[0] = true;

            // ，false为未选中，和setSingleChoiceItems中第二个参数对应
            // 为对话框添加单选列表项
            // 第一个参数存放选项的数组，第二个参数存放默认被选中的项，第三个参数点击事件
            builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    for (int i = 0; i < checkedItems.length; i++) {
                        checkedItems[i] = false;
                    }
                    checkedItems[arg1] = true;
                }
            });

            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    String str = "";
                    for (int i = 0; i < checkedItems.length; i++) {
                        if (checkedItems[i]) {
                            str = items[i];
                        }
                    }
                    Log.d("TEST_DEBUG", "选择了" + str);
                    List itemList = MainActivity.myDBMaster.carMaintainItemDB.queryDataList();
                    if(null != itemList){
                        for(int i = 0; i < itemList.size(); i++){
                            CarMaintainItemBean carMaintainItemBean = (CarMaintainItemBean)itemList.get(i);
                            if(str.equals(carMaintainItemBean.name)){
                                carMaintainItemBean.name = car_name;
                                MainActivity.myDBMaster.carMaintainItemDB.insertData(carMaintainItemBean);
                            }
                        }
                    }
                }
            });
            builder.create().show();
        }
    }

    private void deletDialog(final String car_name) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(CarSelectActivity.this);
        builder.setMessage("是否删除车型");// 为对话框设置内容
        // 为对话框设置取消按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
            }
        });

        // 为对话框设置确定按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                List list = MainActivity.myDBMaster.carMaintainDB.queryDataList();
                if(null != list) {
                    for (int i = 0; i < list.size(); i++) {
                        CarMaintainBean carMaintainBean = (CarMaintainBean) list.get(i);
                        if (car_name.equals(carMaintainBean.name)) {
                            MainActivity.myDBMaster.carMaintainDB.deleteData(carMaintainBean.id);
                        }
                    }
                }
                list = MainActivity.myDBMaster.carMaintainItemDB.queryDataList();
                if(null != list) {
                    for (int i = 0; i < list.size(); i++) {
                        CarMaintainItemBean carMaintainItemBean = (CarMaintainItemBean) list.get(i);
                        if (car_name.equals(carMaintainItemBean.name)) {
                            MainActivity.myDBMaster.carMaintainItemDB.deleteData(carMaintainItemBean.id);
                        }
                    }
                }
                list = MainActivity.myDBMaster.myCarMaintainRecordDB.queryDataList();
                if(null != list) {
                    for (int i = 0; i < list.size(); i++) {
                        MyCarMaintainRecordBean myCarMaintainRecordBean = (MyCarMaintainRecordBean) list.get(i);
                        if (car_name.equals(myCarMaintainRecordBean.name)) {
                            MainActivity.myDBMaster.myCarMaintainRecordDB.deleteData(myCarMaintainRecordBean.id);
                        }
                    }
                }
                fruitList.clear();
                initFruits();
                adapter.notifyDataSetChanged();
            }
        });
        builder.create().show();// 使用show()方法显示对话框
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

    private void setCarDialog(){
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
                                TextView textView = findViewById(R.id.car_select_text);
                                textView.setText("以设置该车牌号车型");
                                textView.setTextColor(Color.rgb(255, 0, 0));
                                Log.d("TEST_DEBUG", "以设置该车牌号车型");
                                return;
                            }
                        }
                    }
                    //createCarFirstRecord(mMeetName);
                }else{
                    TextView textView = findViewById(R.id.car_select_text);
                    textView.setText("未设置车牌");
                    textView.setTextColor(Color.rgb(255, 0, 0));
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

            TextView textView = findViewById(R.id.car_select_text);
            textView.setText("设置车型成功："+car_name+"\n车牌号："+license_plate);
            textView.setTextColor(Color.rgb(0, 0, 255));

            for(int i=0; i < carlist.size(); i++){
                CarMaintainItemBean carMaintainItemBean = (CarMaintainItemBean) carlist.get(i);
                myCarMaintainRecordBean.item_name = carMaintainItemBean.item_name;
                MainActivity.myDBMaster.myCarMaintainRecordDB.insertData(myCarMaintainRecordBean);
            }
        }
    }
}
