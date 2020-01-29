package com.exanmple.carmaintain;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.exanmple.db.CarMaintainBean;
import com.exanmple.db.CarMaintainItemBean;
import com.exanmple.db.MyCarMaintainRecordBean;
import com.exanmple.myview.OuterAdaper;
import com.exanmple.myview.ThreeExpandListView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MaintainRecordActivity extends AppCompatActivity {
    private ExpandableListView expandableListView;
    List<ThreeExpandListView> threeExpandList=new ArrayList<>();

    private String buildRecordString(String car_name, String license_plate){
        return "型号："+car_name+" 车牌："+license_plate;
    }

    private boolean isMileageError(String carAndlicenseString, String mileage){
        List maintainList = MainActivity.myDBMaster.myCarMaintainRecordDB.queryDataList();
        String car_name = "";
        if(null != maintainList){
            for(int i=0; i < maintainList.size(); i++){
                MyCarMaintainRecordBean myCarMaintainRecordBean = (MyCarMaintainRecordBean)maintainList.get(i);
                if(carAndlicenseString.equals(buildRecordString(myCarMaintainRecordBean.name, myCarMaintainRecordBean.license_plate))){
                    if(mileage.equals(""+myCarMaintainRecordBean.item_mileage)) {
                        return true;
                    }else{
                        car_name = myCarMaintainRecordBean.name;
                    }
                }
            }
        }
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

    private void addMaintainItemRecord(String carAndlicenseString, final String mileage, final String time){
        List maintainList = MainActivity.myDBMaster.myCarMaintainRecordDB.queryDataList();
        String car_name = "";
        String license_plate = "";
        if(null != maintainList){
            for(int i = 0; i < maintainList.size(); i++) {
                MyCarMaintainRecordBean myCarMaintainRecordBean = (MyCarMaintainRecordBean) maintainList.get(i);
                if(carAndlicenseString.equals(buildRecordString(myCarMaintainRecordBean.name, myCarMaintainRecordBean.license_plate))) {
                    car_name = myCarMaintainRecordBean.name;
                    license_plate = myCarMaintainRecordBean.license_plate;
                    break;
                }
            }
        }
        if(true == isMileageError(carAndlicenseString, mileage)) {
            List carList = MainActivity.myDBMaster.carMaintainDB.queryDataList();
            if(null != carList){
                for(int i = 0; i < carList.size(); i++) {
                    CarMaintainBean carMaintainBean = (CarMaintainBean) carList.get(i);
                    if (car_name.equals(carMaintainBean.name)) {
                        Toast t = Toast.makeText(this, "输入公里数已设置或者小于汽车保养公里数 ("+carMaintainBean.maintain_mileage_cycle+")", Toast.LENGTH_LONG);
                        t.show();
                        break;
                    }
                }
            }
            return;
        }
        if(true == isTimeError(time)) {
            Toast t = Toast.makeText(this, "输入时间格式错误", Toast.LENGTH_LONG);
            t.show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(MaintainRecordActivity.this);
        builder.setTitle("选择保养项目");

        List carList = MainActivity.myDBMaster.carMaintainDB.queryDataList();
        List itemList = MainActivity.myDBMaster.carMaintainItemDB.queryDataList();
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
        final String finalCar_name = car_name;
        final String finalLicense_plate = license_plate;
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                for (int i = 0; i < checkedItems.length; i++) {
                    if (checkedItems[i]) {
                        MyCarMaintainRecordBean myCarMaintainRecordBean = new MyCarMaintainRecordBean();
                        myCarMaintainRecordBean.name = finalCar_name;
                        myCarMaintainRecordBean.license_plate = finalLicense_plate;
                        myCarMaintainRecordBean.item_name = items[i];
                        myCarMaintainRecordBean.item_mileage = Integer.parseInt(mileage);
                        myCarMaintainRecordBean.item_time = time;
                        Log.d("TEST_DEBUG", "["+mileage+"]添加保养项目: "+items[i]+"("+ finalCar_name +" "+ finalLicense_plate +")");
                        MainActivity.myDBMaster.myCarMaintainRecordDB.insertData(myCarMaintainRecordBean);
                    }
                }
                threeExpandList.clear();
                initData();
            }
        });
        builder.create().show();
    }

    private void addMaintainRecord(final String carAndlicenseString){
        AlertDialog.Builder builder = new AlertDialog.Builder(MaintainRecordActivity.this);
        builder.setTitle("添加保养记录");// 设置标题

        LinearLayout layout = new LinearLayout(MaintainRecordActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText item_mileage = new EditText(MaintainRecordActivity.this);
        item_mileage.setHint("5000");
        item_mileage.setInputType(InputType.TYPE_CLASS_NUMBER);

        final EditText item_time = new EditText(MaintainRecordActivity.this);
        item_time.setHint("2020/01/01");

        TextView mileageTextView = new TextView(MaintainRecordActivity.this);
        mileageTextView.setText("保养公里数");
        layout.addView(mileageTextView);
        layout.addView(item_mileage);
        TextView timeTextView = new TextView(MaintainRecordActivity.this);
        timeTextView.setText("保养时间");
        layout.addView(timeTextView);
        layout.addView(item_time);
        builder.setView(layout);

        builder.setPositiveButton("添加", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                addMaintainItemRecord(carAndlicenseString, item_mileage.getText().toString(), item_time.getText().toString());
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
            }
        });
        builder.create().show();// 使用show()方法显示对话框
    }

    private void deleteMaintainRecord(final String carAndlicenseString){
        final AlertDialog.Builder builder = new AlertDialog.Builder(MaintainRecordActivity.this);
        builder.setMessage("确定删除该设置车型保养记录");// 为对话框设置内容
        // 为对话框设置取消按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
            }
        });

        // 为对话框设置确定按钮
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                List maintainList = MainActivity.myDBMaster.myCarMaintainRecordDB.queryDataList();
                if(null != maintainList){
                    for(int i=0; i < maintainList.size(); i++){
                        MyCarMaintainRecordBean myCarMaintainRecordBean = (MyCarMaintainRecordBean)maintainList.get(i);
                        if(carAndlicenseString.equals(buildRecordString(myCarMaintainRecordBean.name, myCarMaintainRecordBean.license_plate))){
                            MainActivity.myDBMaster.myCarMaintainRecordDB.deleteData(myCarMaintainRecordBean.id);
                        }
                    }
                    threeExpandList.clear();
                    initData();
                }

            }
        });
        builder.create().show();// 使用show()方法显示对话框
    }

    private void doCarMaintainRecord(final String carAndlicenseString){
        final AlertDialog.Builder builder = new AlertDialog.Builder(MaintainRecordActivity.this);
        builder.setMessage("修改保养记录");// 为对话框设置内容
        // 为对话框设置取消按钮
        builder.setNegativeButton("添加", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                addMaintainRecord(carAndlicenseString);
            }
        });

        builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        // 为对话框设置确定按钮
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                deleteMaintainRecord(carAndlicenseString);
            }
        });
        builder.create().show();// 使用show()方法显示对话框
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintain_record);
        expandableListView = findViewById(R.id.maintain_record_expandable_listView);
        initData();
    }

    private void addMaintainTtem(final String carAndLicense, final String mileage){
        final List maintainList = MainActivity.myDBMaster.myCarMaintainRecordDB.queryDataList();
        String car_name = "";
        String license_plate = "";
        if(null != maintainList){
            for(int i = 0; i < maintainList.size(); i++) {
                MyCarMaintainRecordBean myCarMaintainRecordBean = (MyCarMaintainRecordBean) maintainList.get(i);
                if(carAndLicense.equals(buildRecordString(myCarMaintainRecordBean.name, myCarMaintainRecordBean.license_plate))) {
                    car_name = myCarMaintainRecordBean.name;
                    license_plate = myCarMaintainRecordBean.license_plate;
                    break;
                }
            }
        }else{
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(MaintainRecordActivity.this);
        builder.setTitle("修改保养项目");

        List itemList = MainActivity.myDBMaster.carMaintainItemDB.queryDataList();
        if(null == itemList){
            return;
        }

        List <String> itemListString = new ArrayList<>();
        for(int i = 0; i < itemList.size(); i++){
            CarMaintainItemBean carMaintainItemBean = (CarMaintainItemBean)itemList.get(i);
            if(car_name.equals(carMaintainItemBean.name)){
                itemListString.add(carMaintainItemBean.item_name);
            }
        }

        int itemNumber = itemListString.size();
        final String[] items = new String[itemNumber];// 存放选项的数组
        final boolean[] checkedItems = new boolean[itemNumber];

        for(int i=0; i < itemListString.size(); i++) {
            items[i] = itemListString.get(i);
            checkedItems[i] = false;
        }

        for(int j=0; j < checkedItems.length; j++){
            for(int i = 0; i < maintainList.size(); i++){
                MyCarMaintainRecordBean myCarMaintainRecordBean = (MyCarMaintainRecordBean)maintainList.get(i);
                if(carAndLicense.equals(buildRecordString(myCarMaintainRecordBean.name, myCarMaintainRecordBean.license_plate))) {
                    if(items[j].equals(myCarMaintainRecordBean.item_name)){
                        //checkedItems[j] = true;
                        break;
                    }
                }
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

        final String finalCar_name = car_name;
        final String finalLicense_plate = license_plate;
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                for (int i = 0; i < checkedItems.length; i++) {
                    boolean flag = false;
                    String time = "";
                    int deleteId = 0;
                    for(int j = 0; j < maintainList.size(); j++){
                        MyCarMaintainRecordBean myCarMaintainRecordBean = (MyCarMaintainRecordBean)maintainList.get(j);
                        if (finalCar_name.equals(myCarMaintainRecordBean.name) && finalLicense_plate.equals(myCarMaintainRecordBean.name) &&
                                mileage.equals(""+myCarMaintainRecordBean.item_mileage)) {
                            if(items[i].equals(myCarMaintainRecordBean.item_name)){
                                flag = true;
                                deleteId = myCarMaintainRecordBean.id;
                                break;
                            }
                            time = myCarMaintainRecordBean.item_time;
                        }
                    }
                    if (checkedItems[i]) {
                        if(flag == false) {
                            MyCarMaintainRecordBean myCarMaintainRecordBean = new MyCarMaintainRecordBean();
                            myCarMaintainRecordBean.name = finalCar_name;
                            myCarMaintainRecordBean.license_plate = finalLicense_plate;
                            myCarMaintainRecordBean.item_name = items[i];
                            myCarMaintainRecordBean.item_mileage = Integer.parseInt(mileage);
                            myCarMaintainRecordBean.item_time = time;
                            Log.d("TEST_DEBUG", "[" + mileage + "]添加保养项目: " + items[i] + "(" + finalCar_name + " " + finalLicense_plate + ")");
                            MainActivity.myDBMaster.myCarMaintainRecordDB.insertData(myCarMaintainRecordBean);
                        }

                    }else{
                        if(flag == true){
                            MainActivity.myDBMaster.myCarMaintainRecordDB.deleteData(deleteId);
                        }
                    }
                }
                threeExpandList.clear();
                initData();
            }
        });
        builder.create().show();
    }

    private void deleteMaintainItem(final String carAndLicense, final String mileage){
        final AlertDialog.Builder builder = new AlertDialog.Builder(MaintainRecordActivity.this);
        builder.setMessage("确定删除此记录");// 为对话框设置内容
        // 为对话框设置取消按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
            }
        });

        // 为对话框设置确定按钮
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                List maintainList = MainActivity.myDBMaster.myCarMaintainRecordDB.queryDataList();
                if(null != maintainList){
                    for(int i=0; i < maintainList.size(); i++){
                        MyCarMaintainRecordBean myCarMaintainRecordBean = (MyCarMaintainRecordBean)maintainList.get(i);
                        if(carAndLicense.equals(buildRecordString(myCarMaintainRecordBean.name, myCarMaintainRecordBean.license_plate)) &&
                                mileage.equals(""+myCarMaintainRecordBean.item_mileage)){
                            MainActivity.myDBMaster.myCarMaintainRecordDB.deleteData(myCarMaintainRecordBean.id);
                            Log.d("TEST DEBUG", "删除: "+carAndLicense+"("+mileage+" "+myCarMaintainRecordBean.item_name+")");
                        }
                    }
                    threeExpandList.clear();
                    initData();
                }

            }
        });
        builder.create().show();// 使用show()方法显示对话框
    }

    private void doMaintainItem(final String carAndLicense, final String mileage){
        final AlertDialog.Builder builder = new AlertDialog.Builder(MaintainRecordActivity.this);
        builder.setMessage("修改保养记录");// 为对话框设置内容
        // 为对话框设置取消按钮
        builder.setNegativeButton("添加", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                addMaintainTtem(carAndLicense, mileage);
            }
        });

        builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        // 为对话框设置确定按钮
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                deleteMaintainItem(carAndLicense, mileage);
            }
        });
        builder.create().show();// 使用show()方法显示对话框
    }

    private void deleteMaintainOnlyOneItem(final String carAndLicense, final String mileage, final String item){
        final AlertDialog.Builder builder = new AlertDialog.Builder(MaintainRecordActivity.this);
        builder.setMessage("确定删除此保养记录");// 为对话框设置内容
        // 为对话框设置取消按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
            }
        });

        // 为对话框设置确定按钮
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                List maintainList = MainActivity.myDBMaster.myCarMaintainRecordDB.queryDataList();
                if(null != maintainList){
                    for(int i=0; i < maintainList.size(); i++){
                        MyCarMaintainRecordBean myCarMaintainRecordBean = (MyCarMaintainRecordBean)maintainList.get(i);
                        if(carAndLicense.equals(buildRecordString(myCarMaintainRecordBean.name, myCarMaintainRecordBean.license_plate)) &&
                                mileage.equals(""+myCarMaintainRecordBean.item_mileage) && item.equals(myCarMaintainRecordBean.item_name)){
                            MainActivity.myDBMaster.myCarMaintainRecordDB.deleteData(myCarMaintainRecordBean.id);
                            Log.d("TEST DEBUG", "删除: "+carAndLicense+"("+mileage+" "+item+")");
                            break;
                        }
                    }
                    threeExpandList.clear();
                    initData();
                }
            }
        });
        builder.create().show();// 使用show()方法显示对话框
    }

    //创造数据
    private void initData() {
        List maintainList = MainActivity.myDBMaster.myCarMaintainRecordDB.queryDataList();
        if(null != maintainList){
            List<String> carStringList=new ArrayList<>();
            for(int i = 0; i < maintainList.size(); i++) {
                MyCarMaintainRecordBean myCarMaintainRecordBean = (MyCarMaintainRecordBean) maintainList.get(i);
                boolean flag = true;
                for (int j = 0; j < carStringList.size(); j++) {
                    if (buildRecordString(myCarMaintainRecordBean.name, myCarMaintainRecordBean.license_plate).equals(carStringList.get(j))) {
                        flag = false;
                        break;
                    }
                }
                if (flag == true) {
                    carStringList.add(buildRecordString(myCarMaintainRecordBean.name, myCarMaintainRecordBean.license_plate));
                }
            }
            for(int i = 0; i < carStringList.size(); i++){
                ThreeExpandListView threeExpandListView=new ThreeExpandListView();
                threeExpandListView.setName(carStringList.get(i));
                List<String> mileageIntList = new ArrayList<>();
                List<List<String>> itemList = new ArrayList<>();
                mileageIntList.clear();
                itemList.clear();
                mileageIntList.add(""+((MyCarMaintainRecordBean)maintainList.get(0)).item_mileage);
                itemList.add(new ArrayList<String>());
                for(int j = 0; j < maintainList.size(); j++){
                    MyCarMaintainRecordBean myCarMaintainRecordBean = (MyCarMaintainRecordBean) maintainList.get(j);
                    boolean flag = true;
                    if(buildRecordString(myCarMaintainRecordBean.name, myCarMaintainRecordBean.license_plate).equals(carStringList.get(i))) {
                        for(int k = 0; k < mileageIntList.size(); k++) {
                            if (mileageIntList.get(k).equals("" + myCarMaintainRecordBean.item_mileage)) {
                                itemList.get(k).add(myCarMaintainRecordBean.item_name);
                                flag = false;
                                break;
                            }
                        }
                        if(flag == true){
                            List<String> tmp = new ArrayList<>();
                            tmp.add(myCarMaintainRecordBean.item_name);
                            mileageIntList.add(""+myCarMaintainRecordBean.item_mileage);
                            itemList.add(tmp);
                        }
                    }
                }
                int mileagebuf[] = new int[mileageIntList.size()];
                for(int k=0; k<mileageIntList.size(); k++) {
                    int tmp = Integer.parseInt(mileageIntList.get(k));
                    mileagebuf[k] = tmp;
                    for (int g = 0; g < k; g++){
                        if(mileagebuf[g] > tmp){
                            for(int l = k; l > g; l--){
                                mileagebuf[l] = mileagebuf[l-1];
                            }
                            mileagebuf[g] = tmp;
                            break;
                        }
                    }
                }
                List<ThreeExpandListView.FirstListView> firstListViews=new ArrayList<>();
                for (int j = 0; j < mileagebuf.length; j++) {
                    if(mileagebuf[j] == 0)
                        continue;
                    for(int k = 0; k < mileageIntList.size(); k++){
                        if(mileageIntList.get(k).equals(""+mileagebuf[j])){
                            ThreeExpandListView.FirstListView firstListView=new ThreeExpandListView.FirstListView();
                            firstListView.setName(mileageIntList.get(k));
                            List<ThreeExpandListView.SecondListView> secondLists = new ArrayList<>();
                            for(int g = 0; g < itemList.get(k).size(); g++){
                                ThreeExpandListView.SecondListView secondListView = new ThreeExpandListView.SecondListView();
                                secondListView.setName(itemList.get(k).get(g));
                                secondLists.add(secondListView);
                            }
                            firstListView.setSecondList(secondLists);
                            firstListViews.add(firstListView);
                            break;
                        }
                    }
                }
                threeExpandListView.setFirstListViews(firstListViews);
                threeExpandList.add(threeExpandListView);
            }
            //创建适配器实例，设置适配器
            final OuterAdaper adapter=new OuterAdaper(threeExpandList,this);
            final int[] outAdapterPosition = new int[]{0};
            final boolean[] outAdapterItemLongClickFlag = {false};
            adapter.setItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    outAdapterPosition[0] = position;
                    outAdapterItemLongClickFlag[0] = true;
                    return false;
                }
            });
            expandableListView.setAdapter(adapter);

            expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    final long packedPosition = expandableListView.getExpandableListPosition(position);
                    final int groupPosition = expandableListView.getPackedPositionGroup(packedPosition);
                    final int childPosition = expandableListView.getPackedPositionChild(packedPosition);
                    if(groupPosition >= 0 && childPosition < 0){
                        Log.d("TEST DEBUG", "长按:"+threeExpandList.get(groupPosition).getName());
                        doCarMaintainRecord(threeExpandList.get(groupPosition).getName());
                    }else if(groupPosition >= 0 && outAdapterItemLongClickFlag[0] == true) {
                        outAdapterItemLongClickFlag[0] = false;
                        ExpandableListView listView = adapter.getIneerExpandListView(groupPosition);
                        final long secondPackedPosition = listView.getExpandableListPosition(outAdapterPosition[0]);
                        int secondGroupPosition = listView.getPackedPositionGroup(secondPackedPosition);
                        int secondChildPosition = listView.getPackedPositionChild(secondPackedPosition);
                        if(secondGroupPosition >= 0 && secondChildPosition < 0){
                            String carAndLicense = threeExpandList.get(groupPosition).getName();
                            String mileage = threeExpandList.get(groupPosition).getFirstListViews().get(secondGroupPosition).getName();
                            Log.d("==", "car: "+carAndLicense+" mileage:"+mileage);
                            doMaintainItem(carAndLicense, mileage);
                        }else if(secondChildPosition >= 0){
                            String carAndLicense = threeExpandList.get(groupPosition).getName();
                            String mileage = threeExpandList.get(groupPosition).getFirstListViews().get(secondGroupPosition).getName();
                            String item = threeExpandList.get(groupPosition).getFirstListViews().get(secondGroupPosition).getSecondList().get(secondChildPosition).getName();
                            Log.d("==", "car: "+carAndLicense+" mileage:"+mileage+" item:"+item);
                            deleteMaintainOnlyOneItem(carAndLicense, mileage, item);
                        }
                    }
                    return false;
                }
            });
        }else{
            final OuterAdaper adapter=new OuterAdaper(null,this);
            expandableListView.setAdapter(adapter);
            Log.d("++++++", "initData: no no no ");
        }
    }
}
