package com.exanmple.carmaintain;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.exanmple.db.CarMaintainBean;
import com.exanmple.db.CarMaintainItemBean;
import com.exanmple.db.MyCarMaintainRecordBean;

import java.util.ArrayList;
import java.util.List;

public class ItemSetActivity extends AppCompatActivity {
    private ExpandableListView expandableListView;
    private ExpandListViewAdapter myAdapter;
    private List<String> groupList = new ArrayList<>();
    private List<List<String>> childList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_set);

        initView();
    }

    private String buildItemString(String item_name, int item_mileage, int item_time){
        return item_name+"  （"+item_mileage + "公里，"+item_time+"个月）";
    }

    private void initView() {
        expandableListView = (ExpandableListView) findViewById(R.id.item_set_maintain_list);

        TextView textView = (TextView)findViewById(R.id.item_set_text);

        List carlist = MainActivity.myDBMaster.carMaintainDB.queryDataList();
        List itemlist = MainActivity.myDBMaster.carMaintainItemDB.queryDataList();

        if (null != carlist && null != itemlist) {
            textView.setText("以设置车辆信息");
            for(int i=0; i < carlist.size(); i++) {
                CarMaintainBean carMaintainBean = (CarMaintainBean) carlist.get(i);
                Log.d("TEST_DEBUG","车辆:"+carMaintainBean.name);
                List <String> item = new ArrayList<>();
                for (int j = 0; j < itemlist.size(); j++) {
                    CarMaintainItemBean carMaintainItemBean = (CarMaintainItemBean) itemlist.get(j);
                    if (carMaintainBean.name.equals(carMaintainItemBean.name)) {
                        item.add(buildItemString(carMaintainItemBean.item_name, carMaintainItemBean.item_mileage_cycle,carMaintainItemBean.item_time_cycle));
                    }
                }
                String []itemString = new String[item.size()];
                for(int j=0; j < item.size(); j++)
                    itemString[j] = item.get(j);

                addData(carMaintainBean.name,itemString);
            }
        }

        myAdapter = new ExpandListViewAdapter(this,groupList,childList);
        expandableListView.setAdapter(myAdapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Log.d("TEST_DEBUG", "setOnChildClickListener: "+childPosition+"  "+childList.get(groupPosition).get(childPosition));
                changeItem(groupList.get(groupPosition), ""+childList.get(groupPosition).get(childPosition));
                return false;
            }
        });

        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final long packedPosition = expandableListView.getExpandableListPosition(position);
                final int groupPosition = expandableListView.getPackedPositionGroup(packedPosition);
                final int childPosition = expandableListView.getPackedPositionChild(packedPosition);
                //长按的是group的时候，childPosition = -1，这是子条目的长按点击
                if (childPosition == -1) {
                    Log.d("TEST_DEBUG", "onItemLongClick: 汽车名称："+groupList.get(groupPosition));
                    additem(groupList.get(groupPosition));
                }
                return true;
            }
        });
    }

    private void changeItem(String car_name, String item_string){
        List item_list = MainActivity.myDBMaster.carMaintainItemDB.queryDataList();
        if(null != item_list){
            for(int i=0; i < item_list.size(); i++){
                CarMaintainItemBean carMaintainItemBean = (CarMaintainItemBean)item_list.get(i);
                String item_build_string = buildItemString(carMaintainItemBean.item_name, carMaintainItemBean.item_mileage_cycle, carMaintainItemBean.item_time_cycle);
                if(car_name.equals(carMaintainItemBean.name) && item_string.equals(item_build_string)){
                    changeItemDialog(carMaintainItemBean);
                    break;
                }
            }
        }
    }

    private void changeItemDialog(final CarMaintainItemBean carMaintainItemBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ItemSetActivity.this);
        builder.setTitle("修改保养项目");// 设置标题

        LinearLayout layout = new LinearLayout(ItemSetActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText item_name = new EditText(ItemSetActivity.this);
        item_name.setText(""+carMaintainItemBean.item_name);

        final EditText item_mileage = new EditText(ItemSetActivity.this);
        item_mileage.setText(""+carMaintainItemBean.item_mileage_cycle);
        item_mileage.setInputType(InputType.TYPE_CLASS_NUMBER);

        final EditText item_time = new EditText(ItemSetActivity.this);
        item_time.setText(""+carMaintainItemBean.item_time_cycle);
        item_time.setInputType(InputType.TYPE_CLASS_NUMBER);

        TextView nameTextView = new TextView(ItemSetActivity.this);
        nameTextView.setText("保养项目名称");
        layout.addView(nameTextView);
        layout.addView(item_name);
        TextView mileageTextView = new TextView(ItemSetActivity.this);
        mileageTextView.setText("更换或维修公里数");
        layout.addView(mileageTextView);
        layout.addView(item_mileage);
        TextView timeTextView = new TextView(ItemSetActivity.this);
        timeTextView.setText("更换或维修时间");
        layout.addView(timeTextView);
        layout.addView(item_time);
        builder.setView(layout);

        // 为对话框设置取消按钮
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                MainActivity.myDBMaster.carMaintainItemDB.deleteData(carMaintainItemBean.id);
                groupList.clear();
                childList.clear();
                //expandableListView.clearFocus();
                initView();
            }
        });

        // 为对话框设置取消按钮
        builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
            }
        });

        // 为对话框设置确定按钮
        builder.setNegativeButton("修改", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                String itemNameString = item_name.getText().toString();
                String itemMileageString = item_mileage.getText().toString();
                String itemTimeString = item_time.getText().toString();
                if(!itemNameString.equals("") && !itemMileageString.equals("") && !itemTimeString.equals("")) {
                    MainActivity.myDBMaster.carMaintainItemDB.deleteData(carMaintainItemBean.id);
                    addNewItem(carMaintainItemBean.name, itemNameString, itemMileageString, itemTimeString);
                }else{
                    TextView textView = findViewById(R.id.item_set_text);
                    textView.setText("输入信息不完整");
                    textView.setTextColor(Color.rgb(255, 0, 0));
                }
            }
        });
        builder.create().show();// 使用show()方法显示对话框
    }

    private void additem(final String car_name){
        AlertDialog.Builder builder = new AlertDialog.Builder(ItemSetActivity.this);
        builder.setTitle("添加保养项目");// 设置标题

        LinearLayout layout = new LinearLayout(ItemSetActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText item_name = new EditText(ItemSetActivity.this);
        item_name.setHint("项目名称");

        final EditText item_mileage = new EditText(ItemSetActivity.this);
        item_mileage.setHint("更换或维修公里数周期");
        item_mileage.setInputType(InputType.TYPE_CLASS_NUMBER);

        final EditText item_time = new EditText(ItemSetActivity.this);
        item_time.setHint("更换或维修时间周期");
        item_time.setInputType(InputType.TYPE_CLASS_NUMBER);

        layout.addView(item_name);
        layout.addView(item_mileage);
        layout.addView(item_time);
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
                String itemNameString = item_name.getText().toString();
                String itemMileageString = item_mileage.getText().toString();
                String itemTimeString = item_time.getText().toString();
                if(!itemNameString.equals("") && !itemMileageString.equals("") && !itemTimeString.equals("")) {
                    addNewItem(car_name, itemNameString, itemMileageString, itemTimeString);
                }else{
                    TextView textView = findViewById(R.id.item_set_text);
                    textView.setText("输入信息不完整");
                    textView.setTextColor(Color.rgb(255, 0, 0));
                }
            }
        });
        builder.create().show();// 使用show()方法显示对话框
    }

    private void addNewItem(String car_name, String item_name, String item_mileage, String item_time){
        List item_list = MainActivity.myDBMaster.carMaintainItemDB.queryDataList();
        if(null != item_list){
            for(int i = 0; i < item_list.size(); i++){
                CarMaintainItemBean carMaintainItemBean = (CarMaintainItemBean)item_list.get(i);
                if(car_name.equals(carMaintainItemBean.name) && item_name.equals((carMaintainItemBean.item_name))){
                    TextView textView = findViewById(R.id.item_set_text);
                    textView.setText("已有该保养项目");
                    textView.setTextColor(Color.rgb(255, 0, 0));
                    return;
                }
            }

            CarMaintainItemBean carMaintainItemBean = new CarMaintainItemBean();
            carMaintainItemBean.name = car_name;
            carMaintainItemBean.item_name = item_name;
            carMaintainItemBean.item_mileage_cycle = Integer.parseInt(item_mileage);
            carMaintainItemBean.item_time_cycle = Integer.parseInt(item_time);
            MainActivity.myDBMaster.carMaintainItemDB.insertData(carMaintainItemBean);
            Log.d("TEST_DEBUG", "addNewCar: 汽车名称："+carMaintainItemBean.name+" 保养项目名称："+carMaintainItemBean.item_name+" 保养公里数："+carMaintainItemBean.item_mileage_cycle+" 保养时间间隔："+carMaintainItemBean.item_time_cycle);
            groupList.clear();
            childList.clear();
            //expandableListView.clearFocus();
            initView();
        }
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
