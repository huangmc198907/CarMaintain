package com.exanmple.carmaintain;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.exanmple.db.BaoJun560;
import com.exanmple.db.CarMaintainBean;
import com.exanmple.db.CarMaintainItemBean;
import com.exanmple.myview.ExpandListViewAdapter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ItemSetActivity extends AppCompatActivity {
    private ExpandableListView expandableListView;
    private ExpandListViewAdapter myAdapter;
    private List<String> groupList = new ArrayList<>();
    private List<List<String>> childList = new ArrayList<>();
    private List<byte[]> groupIconList = new ArrayList<>();
    private List<List<byte[]>> childIconList = new ArrayList<>();
    private Bitmap iconBitmap;
    private View convertViewDialog;
    private List carList;
    private List itemList;
    private List recordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_set);
        TextView textView = findViewById(R.id.item_set_text);
        float text_size = MainActivity.getTextSize(this, this.getWindowManager().getDefaultDisplay().getWidth());
        textView.setTextSize(text_size);
        initView();
    }

    private String buildItemString(String item_name, int item_mileage, int item_time){
        return item_name+"  （"+item_mileage + "公里，"+item_time+"个月）";
    }

    private void upgradeView(){
        groupList.clear();
        childList.clear();
        groupIconList.clear();
        childIconList.clear();
        //expandableListView.clearFocus();
        initView();
    }

    private void initView() {
        expandableListView = (ExpandableListView) findViewById(R.id.item_set_maintain_list);

        TextView textView = (TextView)findViewById(R.id.item_set_text);

        if(null != carList) carList.clear();
        carList = MainActivity.myDBMaster.carMaintainDB.queryDataList();
        if(null != itemList) itemList.clear();
        itemList = MainActivity.myDBMaster.carMaintainItemDB.queryDataList();

        if (null != carList && null != itemList) {
            textView.setText("");
            for(int i=0; i < carList.size(); i++) {
                CarMaintainBean carMaintainBean = (CarMaintainBean) carList.get(i);
                Log.d("TEST_DEBUG","车辆:"+carMaintainBean.name);
                List <String> item = new ArrayList<>();
                List <byte[]> itemIcon = new ArrayList<>();
                for (int j = 0; j < itemList.size(); j++) {
                    CarMaintainItemBean carMaintainItemBean = (CarMaintainItemBean) itemList.get(j);
                    if (carMaintainBean.name.equals(carMaintainItemBean.name)) {
                        item.add(buildItemString(carMaintainItemBean.item_name, carMaintainItemBean.item_mileage_cycle,carMaintainItemBean.item_time_cycle));
                        itemIcon.add(carMaintainItemBean.icon_byte);
                    }
                }
                String []itemString = new String[item.size()];
                for(int j=0; j < item.size(); j++)
                    itemString[j] = item.get(j);

                addData(carMaintainBean.name,itemString, carMaintainBean.icon_byte, itemIcon);
            }
        }

        myAdapter = new ExpandListViewAdapter(this,groupList,childList, groupIconList, childIconList);
        float text_size = MainActivity.getTextSize(this, this.getWindowManager().getDefaultDisplay().getWidth());
        myAdapter.setTextSize(text_size, text_size * 4 / 5);
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
        if(null != itemList) itemList.clear();
        itemList = MainActivity.myDBMaster.carMaintainItemDB.queryDataList();
        if(null != itemList){
            for(int i=0; i < itemList.size(); i++){
                CarMaintainItemBean carMaintainItemBean = (CarMaintainItemBean)itemList.get(i);
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
                upgradeView();
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
                    iconBitmap = BitmapFactory.decodeByteArray(carMaintainItemBean.icon_byte, 0, carMaintainItemBean.icon_byte.length);
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

        convertViewDialog = View.inflate(getApplicationContext(), R.layout.add_dialog, null);
        builder.setView(convertViewDialog);

        final EditText item_name = (EditText) convertViewDialog.findViewById(R.id.dialog_add_name);
        item_name.setHint("项目名称");
        item_name.setHintTextColor(Color.rgb(200,200,200));

        final EditText item_mileage = (EditText) convertViewDialog.findViewById(R.id.dialog_add_mileage);
        item_mileage.setHint("更换或维修公里数周期\n(如：5000 表示5000公里)");
        item_mileage.setInputType(InputType.TYPE_CLASS_NUMBER);
        item_mileage.setHintTextColor(Color.rgb(200,200,200));


        final EditText item_time = (EditText) convertViewDialog.findViewById(R.id.dialog_add_time);
        item_time.setHint("更换或维修时间周期\n(如：3 表示3个月)");
        item_time.setInputType(InputType.TYPE_CLASS_NUMBER);
        item_time.setHintTextColor(Color.rgb(200,200,200));

        Button button = (Button) convertViewDialog.findViewById(R.id.dialog_add_icon_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");//选择图片
                //intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
            }
        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode ==RESULT_OK) {
            if (requestCode ==1) {
                Uri uri = data.getData();
                iconBitmap = getBitmapFromUri(uri);//将得到的uri传给转换方法，并返回一个bitmap对象
                ImageView imageView = (ImageView) convertViewDialog.findViewById(R.id.dialog_add_icon_view);
                imageView.setImageBitmap(iconBitmap);
            }
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) {
        Bitmap bitmap =null;
        try {
            BitmapFactory.Options options =new BitmapFactory.Options();
            int picWidth = options.outWidth;
            int picHeight = options.outHeight;
            WindowManager windowManager = getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            int screenWidth = display.getWidth();
            int screenHeight = display.getHeight();
            options.inSampleSize =1;
            if (picWidth > picHeight) {
                if (picWidth > screenWidth)
                    options.inSampleSize = picWidth / screenWidth;
            }else {
                if (picHeight > screenHeight)
                    options.inSampleSize = picHeight / screenHeight;
            }

            options.inJustDecodeBounds =false;
            bitmap = BitmapFactory.decodeStream(getContentResolver()
                    .openInputStream(uri),null, options);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return bitmap;
    }

    private void addNewItem(String car_name, String item_name, String item_mileage, String item_time){
        if(null != itemList) itemList.clear();
        itemList = MainActivity.myDBMaster.carMaintainItemDB.queryDataList();
        if(null != itemList){
            for(int i = 0; i < itemList.size(); i++){
                CarMaintainItemBean carMaintainItemBean = (CarMaintainItemBean)itemList.get(i);
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
            if(null != iconBitmap) {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                BaoJun560.compressBitmap(iconBitmap).compress(Bitmap.CompressFormat.PNG, 100, os);
                carMaintainItemBean.icon_byte = os.toByteArray();
                iconBitmap = null;
            }else{
                carMaintainItemBean.icon_byte = new byte[0];
            }
            MainActivity.myDBMaster.carMaintainItemDB.insertData(carMaintainItemBean);
            Log.d("TEST_DEBUG", "addNewCar: 汽车名称："+carMaintainItemBean.name+" 保养项目名称："+carMaintainItemBean.item_name+" 保养公里数："+carMaintainItemBean.item_mileage_cycle+" 保养时间间隔："+carMaintainItemBean.item_time_cycle);
            upgradeView();
        }
    }

    /**
     * 用来添加数据的方法
     */
    private void addData(String group, String[] friend, byte[] groupIcon, List<byte[]> friendIcon) {
        groupList.add(group);
        groupIconList.add(groupIcon);
        //每一个item打开又是一个不同的list集合
        List<String> childitem = new ArrayList<>();
        for (int i = 0; i < friend.length; i++) {
            childitem.add(friend[i]);
        }
        childList.add(childitem);
        childIconList.add(friendIcon);
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
