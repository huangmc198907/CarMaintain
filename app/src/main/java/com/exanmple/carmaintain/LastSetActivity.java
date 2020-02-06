package com.exanmple.carmaintain;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.exanmple.db.CarMaintainBean;
import com.exanmple.db.CarMaintainItemBean;
import com.exanmple.db.MyCarMaintainRecordBean;
import com.exanmple.myview.Fruit;
import com.exanmple.myview.FruitAdapter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LastSetActivity extends AppCompatActivity {
    // fruitList用于存储数据
    private List<Fruit> fruitList=new ArrayList<>();
    private FruitAdapter adapter;
    private List carList;
    private List itemList;
    private List recordList;

    private String buildRecordString(String car_name, String license_plate){
        return "汽车型号："+car_name+"  设置车牌号："+license_plate;
    }

    private boolean isMileageError(String car_name, String mileage, String license_plate){
        if(null != recordList) recordList.clear();
        recordList = MainActivity.myDBMaster.myCarMaintainRecordDB.queryDataList();
        if(null != recordList){
            for(int i=0; i < recordList.size(); i++){
                MyCarMaintainRecordBean myCarMaintainRecordBean = (MyCarMaintainRecordBean)recordList.get(i);
                if(car_name.equals(myCarMaintainRecordBean.name) &&
                        license_plate.equals(myCarMaintainRecordBean.license_plate) &&
                        mileage.equals(""+myCarMaintainRecordBean.item_mileage)){
                    return true;
                }
            }
        }
        if(Integer.parseInt(mileage) > 0) {
            return false;
        }
        /*
        List carList = MainActivity.myDBMaster.carMaintainDB.queryDataList();
        if(null != carList) {
            for (int i = 0; i < carList.size(); i++) {
                CarMaintainBean carMaintainBean = (CarMaintainBean) carList.get(i);
                if (car_name.equals(carMaintainBean.name)) {
                    if (carMaintainBean.maintain_mileage_cycle <= Integer.parseInt(mileage))
                        return false;
                }
            }
        }

         */
        return true;
    }

    private boolean isTimeError(String time){
        Pattern p = Pattern.compile("[0-9][0-9][0-9][0-9]/[0-1][0-9]/[0-3][0-9]");
        Matcher m = p.matcher(time);
        if(m.matches() ){
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_set);
        TextView textView = findViewById(R.id.last_set_text);

        float text_size = MainActivity.getTextSize(this, this.getWindowManager().getDefaultDisplay().getWidth());
        textView.setTextSize(text_size);

        if(null != recordList) recordList.clear();
        recordList = MainActivity.myDBMaster.myCarMaintainRecordDB.queryDataList();
        if (null != recordList && recordList.size() > 0) {
            textView.setText("已设置车辆信息");
        }

        // 先拿到数据并放在适配器上
        initFruits(); //初始化水果数据
        adapter=new FruitAdapter(LastSetActivity.this,R.layout.fruit_item,fruitList);

        // 将适配器上的数据传递给listView
        final ListView listView=findViewById(R.id.set_car_select_list);
        listView.setAdapter(adapter);

        // 为ListView注册一个监听器，当用户点击了ListView中的任何一个子项时，就会回调onItemClick()方法
        // 在这个方法中可以通过position参数判断出用户点击的是那一个子项
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = findViewById(R.id.last_set_text);
                textView.setText("");
                Fruit fruit=fruitList.get(position);
                Log.d("TEST_DEBUG","车辆列表中，点击车辆名称="+fruit.getName()+" 列表id="+position);
                TextView mileageView = (TextView)findViewById(R.id.last_set_maintain_mileage);
                TextView timeView = (TextView)findViewById(R.id.last_set_maintain_time);
                String mileageString = mileageView.getText().toString();
                String timeString = timeView.getText().toString();
                if(!mileageString.equals("") && !timeString.equals("")) {
                    if(null != recordList) recordList.clear();
                    recordList = MainActivity.myDBMaster.myCarMaintainRecordDB.queryDataList();
                    String car_name = "";
                    String license_plate = "";
                    if (null != recordList) {
                        for (int i = 0; i < recordList.size(); i++) {
                            MyCarMaintainRecordBean myCarMaintainRecordBean = (MyCarMaintainRecordBean) recordList.get(i);
                            if (fruit.getName().equals(buildRecordString(myCarMaintainRecordBean.name, myCarMaintainRecordBean.license_plate))) {
                                car_name = myCarMaintainRecordBean.name;
                                license_plate = myCarMaintainRecordBean.license_plate;
                                break;
                            }
                        }
                    }
                    if(true == isMileageError(car_name, mileageString, license_plate)) {
                        if(null != carList) carList.clear();
                        carList = MainActivity.myDBMaster.carMaintainDB.queryDataList();
                        if(null != carList){
                            for(int i = 0; i < carList.size(); i++) {
                                CarMaintainBean carMaintainBean = (CarMaintainBean)carList.get(i);
                                if(car_name.equals(carMaintainBean.name))
                                    textView.setText("输入公里数已设置或者小于汽车保养公里数（汽车保养公里数：" +carMaintainBean.maintain_mileage_cycle+ "）");
                            }
                        }else {
                            textView.setText("输入公里数错误");
                        }
                        return;
                    }
                    if(true == isTimeError(timeString)) {
                        textView.setText("输入时间格式错误");
                        return;
                    }
                    setLastRecordDialog(car_name, license_plate, mileageString, timeString);
                }else{
                    textView.setText("请输入公里和时间");
                }
            }
        });
    }

    private void setLastRecordDialog(final String car_name, final String license_plate, final String mileage, final String time){
        AlertDialog.Builder builder = new AlertDialog.Builder(LastSetActivity.this);
        builder.setTitle("选择保养项目");

        if(null != carList) carList.clear();
        carList = MainActivity.myDBMaster.carMaintainDB.queryDataList();
        if(null != itemList) itemList.clear();
        itemList = MainActivity.myDBMaster.carMaintainItemDB.queryDataList();
        if(null == carList || null == itemList){
            return;
        }

        int maintain_mileage = 0;
        int maintain_time = 0;
        for(int i = 0; i < carList.size(); i++){
            CarMaintainBean carMaintainBean = (CarMaintainBean)carList.get(i);
            if(car_name.equals(carMaintainBean.name)){
                maintain_mileage = carMaintainBean.maintain_mileage_cycle;
                maintain_time = carMaintainBean.maintain_time_cycle;
                break;
            }
        }

        int itemNumber = 0;
        for(int i=0; i < itemList.size(); i++){
            CarMaintainItemBean carMaintainItemBean = (CarMaintainItemBean)itemList.get(i);
            if(car_name.equals(carMaintainItemBean.name)){
                itemNumber++;
            }
        }

        if(itemNumber == 0)
            return;

        final String[] items = new String[itemNumber];// 存放选项的数组
        final boolean[] checkedItems = new boolean[itemNumber];

        int j = 0;
        for(int i=0; i < itemList.size(); i++){
            CarMaintainItemBean carMaintainItemBean = (CarMaintainItemBean)itemList.get(i);
            if(car_name.equals(carMaintainItemBean.name)){
                items[j] = carMaintainItemBean.item_name;
                int item_mileage = carMaintainItemBean.item_mileage_cycle;
                int car_mileage = Integer.parseInt(mileage) - maintain_mileage;
                int item_number = car_mileage / item_mileage;
                int car_number = car_mileage / maintain_mileage;
                car_mileage = maintain_mileage * car_number;
                //Log.d("TEST DEBUG", "保养项目:" + carMaintainItemBean.item_name + " 公里数:" + item_mileage + " 时间：" + carMaintainItemBean.item_time_cycle);
                //Log.d("TEST DEBUG", "setLastRecordDialog: item_number:" + item_number + " flaut:" + car_mileage / item_mileage);
                //Log.d("TEST DEBUG", "车型保养公里数:" + maintain_mileage + " 时间:" + maintain_time);
                //Log.d("TEST DEBUG", "setLastRecordDialog: car_number:" + car_number + " flaut:" + car_mileage / maintain_mileage);
                if (item_number > 0 && car_number > 0 &&
                        (car_mileage >= (item_mileage * item_number)) &&
                        (car_mileage < (item_mileage * item_number + maintain_mileage)) ){
                    checkedItems[j] = true;
                }else{
                    checkedItems[j] = false;
                }
                j++;
            }
        }

        // 第一个参数选项，第二个参数选项的状态，第三个点击事件
        builder.setMultiChoiceItems(items, checkedItems,
                new DialogInterface.OnMultiChoiceClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1,
                                        boolean arg2) {
                        // TODO Auto-generated method stub
                        if (arg2) {
                            checkedItems[arg1] = true;
                        } else {
                            checkedItems[arg1] = false;
                        }
                    }
                });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                arg0.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                for (int i = 0; i < checkedItems.length; i++) {
                    if (checkedItems[i]) {
                        MyCarMaintainRecordBean myCarMaintainRecordBean = new MyCarMaintainRecordBean();
                        myCarMaintainRecordBean.name = car_name;
                        myCarMaintainRecordBean.license_plate = license_plate;
                        myCarMaintainRecordBean.item_name = items[i];
                        myCarMaintainRecordBean.item_mileage = Integer.parseInt(mileage);
                        myCarMaintainRecordBean.item_date = time;
                        Log.d("TEST_DEBUG", "["+mileage+"]添加保养项目: "+items[i]+"("+car_name+" "+license_plate+")");
                        MainActivity.myDBMaster.myCarMaintainRecordDB.insertData(myCarMaintainRecordBean);
                    }
                }
            }
        });
        builder.create().show();
    }

    // 初始化数据
    private void initFruits(){
        if(null != recordList) recordList.clear();
        recordList = MainActivity.myDBMaster.myCarMaintainRecordDB.queryDataList();
        if(null != carList) carList.clear();
        carList = MainActivity.myDBMaster.carMaintainDB.queryDataList();
        List <String> recordString = new ArrayList<>();
        if (null != recordList && null != carList) {
            for(int i=0; i < recordList.size(); i++){
                boolean addFlag = true;
                MyCarMaintainRecordBean myCarMaintainRecordBean = (MyCarMaintainRecordBean) recordList.get(i);
                for(int j = 0; j < recordString.size(); j++){
                    if(recordString.get(j).equals(buildRecordString(myCarMaintainRecordBean.name, myCarMaintainRecordBean.license_plate))){
                        addFlag = false;
                        break;
                    }
                }
                if(addFlag == true){
                    String recordStrings = buildRecordString(myCarMaintainRecordBean.name, myCarMaintainRecordBean.license_plate);
                    byte []b = new byte[0];
                    for(int j = 0; j < carList.size(); j++){
                        CarMaintainBean carMaintainBean = (CarMaintainBean)carList.get(j);
                        if(myCarMaintainRecordBean.name.equals(carMaintainBean.name)){
                            b = carMaintainBean.icon_byte;
                            break;
                        }
                    }
                    Fruit a = new Fruit(recordStrings,b);
                    float text_size = MainActivity.getTextSize(this, this.getWindowManager().getDefaultDisplay().getWidth());
                    a.setTextSize(text_size);
                    recordString.add(recordStrings);
                    fruitList.add(a);
                }
            }
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if(level == TRIM_MEMORY_UI_HIDDEN){
            if(null != carList) carList.clear();
            if(null != itemList) itemList.clear();
            if(null != recordList) recordList.clear();
        }
    }
}
