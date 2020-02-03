package com.exanmple.carmaintain;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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
import com.exanmple.myview.ExpandListViewAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NextActivity extends AppCompatActivity {
    private ExpandableListView expandableListView;
    private ExpandListViewAdapter myAdapter;
    private List<String> groupList = new ArrayList<>();
    private List<List<String>> childList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        initView();
    }

    private String buildRecordString(String car_name, String license_plate){
        return "汽车型号："+car_name+"  设置车牌号："+license_plate;
    }

    private String buildItemString(String itemName){
        return "保养项目："+itemName;
    }

    private String buildMileageString(String mileage){
        return "下次保养公里数："+mileage;
    }

    private String buildDateString(String date){
        return "下次保养时间："+date;
    }

    private String getbuildSubString(String buildString){
        int index = buildString.indexOf("：");
        return buildString.substring(index+1, buildString.length());
    }

    private void initView() {
        expandableListView = (ExpandableListView) findViewById(R.id.next_item_list);
        List maintainList = MainActivity.myDBMaster.myCarMaintainRecordDB.queryDataList();

        List <String> carAndLicenseStringList = new ArrayList<>();
        List <String> carStringList = new ArrayList<>();
        List <List <String>> mileageStringList = new ArrayList<>();
        List <List <String>> dateStringList = new ArrayList<>();
        List <List <List <String>>> itemStringList = new ArrayList<>();
        final List <List <String>> itemStringShowList = new ArrayList<>();

        if (null != maintainList) {
            for (int i = 0; i < maintainList.size(); i++) {
                MyCarMaintainRecordBean myCarMaintainRecordBean = (MyCarMaintainRecordBean) maintainList.get(i);
                Log.d("=====++", "initView: "+myCarMaintainRecordBean.name+" "+myCarMaintainRecordBean.license_plate+" "+myCarMaintainRecordBean.item_mileage+" "+myCarMaintainRecordBean.item_name);
                boolean flag = false;
                int carId = 0;
                for (int j = 0; j < carAndLicenseStringList.size(); j++) {
                    if (carAndLicenseStringList.get(j).equals(buildRecordString(myCarMaintainRecordBean.name, myCarMaintainRecordBean.license_plate))) {
                        flag = true;
                        break;
                    }
                    carId++;
                }
                if (flag == false) {
                    carAndLicenseStringList.add(buildRecordString(myCarMaintainRecordBean.name, myCarMaintainRecordBean.license_plate));
                    carStringList.add(myCarMaintainRecordBean.name);
                    List<String> tmp = new ArrayList<>();
                    tmp.add("" + myCarMaintainRecordBean.item_mileage);
                    mileageStringList.add(tmp);
                    List<String> tmp1 = new ArrayList<>();
                    tmp1.add("" + myCarMaintainRecordBean.item_date);
                    dateStringList.add(tmp1);
                    List<String> tmp2 = new ArrayList<>();
                    tmp2.add(myCarMaintainRecordBean.item_name);
                    List<List<String>> tmp3 = new ArrayList<>();
                    tmp3.add(tmp2);
                    itemStringList.add(tmp3);
                }
                flag = false;
                for (int j = 0; j < mileageStringList.get(carId).size(); j++) {
                    if (mileageStringList.get(carId).get(j).equals("" + myCarMaintainRecordBean.item_mileage)) {
                        flag = true;
                        itemStringList.get(carId).get(j).add(myCarMaintainRecordBean.item_name);
                        break;
                    }
                }
                if (flag == false) {
                    mileageStringList.get(carId).add("" + myCarMaintainRecordBean.item_mileage);
                    dateStringList.get(carId).add(""+myCarMaintainRecordBean.item_date);
                    List<String> tmp1 = new ArrayList<>();
                    tmp1.add(myCarMaintainRecordBean.item_name);
                    itemStringList.get(carId).add(tmp1);
                }
            }

            for(int i=0; i < carAndLicenseStringList.size(); i++){
                if(carAndLicenseStringList.size() == mileageStringList.size()){
                    int []mileageInt = new int[mileageStringList.get(i).size()];
                    for(int j = 0; j < mileageStringList.get(i).size(); j++){
                        int tmp = Integer.parseInt(mileageStringList.get(i).get(j));
                        mileageInt[j] = tmp;
                        for (int k = 0; k < j; k++){
                            if(mileageInt[k] > tmp){
                                for(int g = j; g > k; g--){
                                    mileageInt[g] = mileageInt[g-1];
                                }
                                mileageInt[k] = tmp;
                                break;
                            }
                        }
                    }
                    List <List <String>> itemStringTmpList = new ArrayList<>();
                    for(int j=0; j < mileageInt.length; j++){
                        for(int k = 0; k < mileageStringList.get(i).size(); k++){
                            if(mileageStringList.get(i).get(k).equals(""+mileageInt[j])){
                                itemStringTmpList.add(itemStringList.get(i).get(k));
                            }
                        }
                    }
                    itemStringList.get(i).clear();
                    mileageStringList.get(i).clear();
                    for(int j=0; j < mileageInt.length; j++){
                        mileageStringList.get(i).add(""+mileageInt[j]);
                        itemStringList.get(i).add(itemStringTmpList.get(j));
                    }
                }
            }

            for(int i=0; i < itemStringList.size(); i++){
                for(int j=0; j < itemStringList.get(i).size(); j++){
                    for(int k=0; k < itemStringList.get(i).get(j).size(); k++){
                        Log.d("====", "Car: "+carAndLicenseStringList.get(i)+" "+mileageStringList.get(i).get(j)+" "+itemStringList.get(i).get(j).get(k));
                    }
                }
            }

            List itemList = MainActivity.myDBMaster.carMaintainItemDB.queryDataList();
            List carList = MainActivity.myDBMaster.carMaintainDB.queryDataList();
            if(null != itemList && null != carList) {
                for (int i = 0; i < carAndLicenseStringList.size(); i++) {
                    List <String> itemStringTmp1List = new ArrayList<>();
                    itemStringShowList.add(itemStringTmp1List);
                    int mileage = 0;
                    for(int j=0; j < carList.size(); j++){
                        CarMaintainBean carMaintainBean = (CarMaintainBean)carList.get(j);
                        if(carMaintainBean.name.equals(carStringList.get(i))){
                            int index = mileageStringList.get(i).size() - 1;
                            mileage = carMaintainBean.maintain_mileage_cycle+Integer.parseInt(mileageStringList.get(i).get(index));
                            Log.d("TEST DEBUG", "下次保养公里数="+mileage);
                            itemStringShowList.get(i).add(buildMileageString(""+mileage));
                            String date = dateStringList.get(i).get(index);
                            if(!date.equals("")) {
                                String year = date.substring(0, 4);
                                String month = date.substring(5, 7);
                                Log.d("====", "initView: y="+year+" m="+month);
                                int monthSun = Integer.parseInt(month) + carMaintainBean.maintain_time_cycle;
                                int yearSun = Integer.parseInt(year);
                                if (monthSun > 12) {
                                    yearSun = yearSun + (int) (Math.floor(monthSun / 12));
                                    monthSun = monthSun % 12;
                                }
                                //2020/10/10
                                itemStringShowList.get(i).add(buildDateString(""+yearSun + "/" + ((monthSun < 10) ? ("0"+monthSun) : monthSun) + "/" + date.substring(8, 10)));
                            }else{
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                                Date nowDate = new Date(System.currentTimeMillis());
                                itemStringShowList.get(i).add(buildDateString(""+simpleDateFormat.format(nowDate)));
                            }
                            break;
                        }
                    }
                    for(int j=0; j < itemList.size(); j++){
                        CarMaintainItemBean carMaintainItemBean = (CarMaintainItemBean)itemList.get(j);
                        if(carMaintainItemBean.name.equals(carStringList.get(i))){
                            boolean flag = false;
                            for(int k=mileageStringList.get(i).size() - 1; k >= 0 ; k--){
                                for(int g=0; g < itemStringList.get(i).get(k).size(); g++){
                                    if(carMaintainItemBean.item_name.equals(itemStringList.get(i).get(k).get(g))){
                                        if((mileage - Integer.parseInt(mileageStringList.get(i).get(k))) >= carMaintainItemBean.item_mileage_cycle){
                                            itemStringShowList.get(i).add(buildItemString(carMaintainItemBean.item_name));
                                        }
                                        flag = true;
                                        break;
                                    }
                                }
                                if(true == flag)
                                    break;
                            }
                        }
                    }
                    String[] itemString = new String[itemStringShowList.get(i).size()];
                    for(int j=0; j < itemString.length; j++){
                        itemString[j] = itemStringShowList.get(i).get(j);
                    }
                    addData(carAndLicenseStringList.get(i), itemString);
                }
            }
        }

        myAdapter = new ExpandListViewAdapter(this,groupList,childList);
        myAdapter.setGroupRightText("长按完成\n保养");
        myAdapter.setChildRightText(" ");
        float text_size = MainActivity.getTextSize(this, this.getWindowManager().getDefaultDisplay().getWidth());
        myAdapter.setTextSize(text_size, text_size * 4 / 5);
        expandableListView.setAdapter(myAdapter);

        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final long packedPosition = expandableListView.getExpandableListPosition(position);
                final int groupPosition = expandableListView.getPackedPositionGroup(packedPosition);
                final int childPosition = expandableListView.getPackedPositionChild(packedPosition);
                //长按的是group的时候，childPosition = -1，这是子条目的长按点击
                if (childPosition == -1) {
                    Log.d("TEST_DEBUG", "onItemLongClick: 汽车名称："+groupList.get(groupPosition));
                    finishMaintain(groupList.get(groupPosition), itemStringShowList.get(groupPosition));
                }
                return true;
            }
        });
    }

    private boolean isMileageError(String carAndLicense, String mileage, String preMileage){
        if(mileage.equals("")){
            return true;
        }

        List maintainList = MainActivity.myDBMaster.myCarMaintainRecordDB.queryDataList();
        String carString = "";
        if(null != maintainList){
            for(int i=0; i < maintainList.size(); i++){
                MyCarMaintainRecordBean myCarMaintainRecordBean = (MyCarMaintainRecordBean)maintainList.get(i);
                if(carAndLicense.equals(buildRecordString(myCarMaintainRecordBean.name, myCarMaintainRecordBean.license_plate))){
                    carString = myCarMaintainRecordBean.name;
                    break;
                }
            }
        }else{
            return true;
        }
        List carList = MainActivity.myDBMaster.carMaintainDB.queryDataList();
        if(null != carList) {
            for (int i = 0; i < carList.size(); i++) {
                CarMaintainBean carMaintainBean = (CarMaintainBean) carList.get(i);
                if (carString.equals(carMaintainBean.name)) {
                    if (Integer.parseInt(mileage) > (Integer.parseInt(preMileage) - carMaintainBean.maintain_mileage_cycle))
                        return false;
                }
            }
        }
        return true;
    }

    private boolean isDataError(String date){
        Pattern p = Pattern.compile("[0-9][0-9][0-9][0-9]/[0-1][0-9]/[0-3][0-9]");
        Matcher m = p.matcher(date);
        if(m.matches() ){
            return false;
        }
        return true;
    }

    private void selectItem(final String carAndLicense, final String mileage, final String date, final List <String> itemStringList){
        if(isMileageError(carAndLicense, mileage, ""+getbuildSubString(itemStringList.get(0)))){
            Toast t = Toast.makeText(this, "输入公里数错误", Toast.LENGTH_LONG);
            t.show();
            return;
        }
        if(isDataError(date)){
            Toast t = Toast.makeText(this, "输入日期格式错误", Toast.LENGTH_LONG);
            t.show();
            return;
        }

        List maintainList = MainActivity.myDBMaster.myCarMaintainRecordDB.queryDataList();
        String carString = "";
        String licensePlateString = "";
        if(null != maintainList){
            for(int i=0; i < maintainList.size(); i++){
                MyCarMaintainRecordBean myCarMaintainRecordBean = (MyCarMaintainRecordBean)maintainList.get(i);
                if(carAndLicense.equals(buildRecordString(myCarMaintainRecordBean.name, myCarMaintainRecordBean.license_plate))){
                    carString = myCarMaintainRecordBean.name;
                    licensePlateString = myCarMaintainRecordBean.license_plate;
                    break;
                }
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(NextActivity.this);
        builder.setTitle("选择保养项目");

        List itemList = MainActivity.myDBMaster.carMaintainItemDB.queryDataList();
        List <String> carItemStringList = new ArrayList<>();
        if(null != itemList){
            for(int i = 0; i < itemList.size(); i++){
                CarMaintainItemBean carMaintainItemBean = (CarMaintainItemBean)itemList.get(i);
                if(carString.equals(carMaintainItemBean.name)){
                    carItemStringList.add(carMaintainItemBean.item_name);
                }
            }
        }

        final String[] items = new String[carItemStringList.size()];// 存放选项的数组
        final boolean[] checkedItems = new boolean[carItemStringList.size()];
        for(int i=0; i < carItemStringList.size(); i++){
            items[i] = carItemStringList.get(i);
            checkedItems[i] = false;
            for(int j = 2; j < itemStringList.size(); j++){
                if(items[i].equals(getbuildSubString(itemStringList.get(j)))){
                    checkedItems[i] = true;
                    break;
                }
            }
        }

        // 第一个参数选项，第二个参数选项的状态，第三个点击事件
        builder.setMultiChoiceItems(items, checkedItems,
                new DialogInterface.OnMultiChoiceClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1, boolean arg2) {
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
        final String finalCar_name = carString;
        final String finalLicense_plate = licensePlateString;
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
                        myCarMaintainRecordBean.item_date = date;
                        Log.d("TEST_DEBUG", "["+mileage+"]添加保养项目: "+items[i]+"("+ finalCar_name +" "+ finalLicense_plate +")");
                        MainActivity.myDBMaster.myCarMaintainRecordDB.insertData(myCarMaintainRecordBean);
                    }
                }
                groupList.clear();
                childList.clear();
                //expandableListView.clearFocus();
                initView();
            }
        });
        builder.create().show();
    }

    private void finishMaintain(final String carAndlicense, final List <String> itemStringList){
        AlertDialog.Builder builder = new AlertDialog.Builder(NextActivity.this);
        builder.setTitle("设置下次保养公里数和时间");// 设置标题

        LinearLayout layout = new LinearLayout(NextActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText item_mileage = new EditText(NextActivity.this);
        item_mileage.setText(getbuildSubString(itemStringList.get(0)));
        item_mileage.setInputType(InputType.TYPE_CLASS_NUMBER);

        final EditText item_date = new EditText(NextActivity.this);
        item_date.setText(getbuildSubString(itemStringList.get(1)));

        TextView mileageTextView = new TextView(NextActivity.this);
        mileageTextView.setText("保养公里数");
        layout.addView(mileageTextView);
        layout.addView(item_mileage);
        final TextView dataTextView = new TextView(NextActivity.this);
        dataTextView.setText("保养时间");
        layout.addView(dataTextView);
        layout.addView(item_date);
        builder.setView(layout);

        builder.setPositiveButton("完成设置", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                selectItem(carAndlicense, item_mileage.getText().toString(), item_date.getText().toString(), itemStringList);
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

    /**
     * 用来添加数据的方法
     */
    private void addData(String group, String[] friend) {
        groupList.add(group);
        //每一个item打开又是一个不同的list集合
        List<String> childitem = new ArrayList<>();
        for (int i = 0; i < friend.length; i++) {
            childitem.add(friend[i]);
        }
        childList.add(childitem);
    }
}
