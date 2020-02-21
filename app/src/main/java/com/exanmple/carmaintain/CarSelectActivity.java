package com.exanmple.carmaintain;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.exanmple.db.BaoJun560;
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

public class CarSelectActivity extends AppCompatActivity {
    // fruitList用于存储数据
    private List<Fruit> fruitList=new ArrayList<>();
    private String car_name = null;
    private static boolean longClickFlag = false;
    private FruitAdapter adapter;
    private Bitmap iconBitmap;
    private View convertViewDialog;
    private List carList;
    private List itemList;
    private List recordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_select);

        Button addButton = (Button)findViewById(R.id.add_car);
        TextView textView = (TextView)findViewById(R.id.car_select_text);
        float text_size = MainActivity.getTextSize(this, this.getWindowManager().getDefaultDisplay().getWidth());
        addButton.setTextSize(text_size);
        textView.setTextSize(text_size);

        addButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                TextView textView = findViewById(R.id.car_select_text);
                textView.setText("");

                AlertDialog.Builder builder = new AlertDialog.Builder(CarSelectActivity.this);
                builder.setTitle("添加新车型");// 设置标题
                convertViewDialog = View.inflate(getApplicationContext(), R.layout.add_dialog, null);

                EditText car_name = (EditText) convertViewDialog.findViewById(R.id.dialog_add_name);
                car_name.setHintTextColor(Color.rgb(200,200,200));
                EditText car_mileage = (EditText) convertViewDialog.findViewById(R.id.dialog_add_mileage);
                car_mileage.setHintTextColor(Color.rgb(200,200,200));
                EditText car_time = (EditText) convertViewDialog.findViewById(R.id.dialog_add_time);
                car_time.setHintTextColor(Color.rgb(200,200,200));
                Button button = (Button) convertViewDialog.findViewById(R.id.dialog_add_icon_button);
                final EditText carNameView = car_name;
                final EditText mileageView = car_mileage;
                final EditText timeView = car_time;
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
                        String carNameString = carNameView.getText().toString();
                        String carMileageString = mileageView.getText().toString();
                        String carTimeString = timeView.getText().toString();
                        if(!carNameString.equals("") && !carMileageString.equals("") && !carTimeString.equals("")) {
                            addNewCar(carNameString, carMileageString, carTimeString);
                        }else{
                            TextView textView = findViewById(R.id.car_select_text);
                            textView.setText("输入信息不完整");
                            textView.setTextColor(Color.rgb(255, 0, 0));
                        }
                    }
                });
                builder.setView(convertViewDialog);
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
                    setCarDialog(fruit.getName());
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
            Log.d("=====", "getBitmapFromUri: inSampleSize="+options.inSampleSize);
            options.inJustDecodeBounds =false;
            bitmap = BitmapFactory.decodeStream(getContentResolver()
                    .openInputStream(uri),null, options);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return bitmap;
    }

    private void addNewCar(String car_name, String car_mileage, String car_time){
        if(null != carList) carList.clear();
        carList = MainActivity.myDBMaster.carMaintainDB.queryDataList();
        if(null != carList){
            for(int i = 0; i < carList.size(); i++){
                CarMaintainBean carMaintainBean = (CarMaintainBean)carList.get(i);
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
            if(null != iconBitmap) {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                BaoJun560.compressBitmap(iconBitmap).compress(Bitmap.CompressFormat.PNG, 100, os);
                carMaintainBean.icon_byte = os.toByteArray();
                iconBitmap = null;
            }else{
                carMaintainBean.icon_byte = new byte[0];
            }
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
        if(null != carList) carList.clear();
        carList = MainActivity.myDBMaster.carMaintainDB.queryDataList();
        if(null != carList) {
            final String[] items = new String[carList.size()];// 创建一个存放选项的数组
            final boolean[] checkedItems = new boolean[carList.size()];// 存放选中状态，true为选中
            for(int i=0; i < carList.size(); i++){
                CarMaintainBean carMaintainBean = (CarMaintainBean)carList.get(i);
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
                    if(null != itemList) itemList.clear();
                    itemList = MainActivity.myDBMaster.carMaintainItemDB.queryDataList();
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
                if(null != carList) carList.clear();
                carList = MainActivity.myDBMaster.carMaintainDB.queryDataList();
                if(null != carList) {
                    for (int i = 0; i < carList.size(); i++) {
                        CarMaintainBean carMaintainBean = (CarMaintainBean) carList.get(i);
                        if (car_name.equals(carMaintainBean.name)) {
                            MainActivity.myDBMaster.carMaintainDB.deleteData(carMaintainBean.id);
                        }
                    }
                }
                if(null != itemList) itemList.clear();
                itemList = MainActivity.myDBMaster.carMaintainItemDB.queryDataList();
                if(null != itemList) {
                    for (int i = 0; i < itemList.size(); i++) {
                        CarMaintainItemBean carMaintainItemBean = (CarMaintainItemBean) itemList.get(i);
                        if (car_name.equals(carMaintainItemBean.name)) {
                            MainActivity.myDBMaster.carMaintainItemDB.deleteData(carMaintainItemBean.id);
                        }
                    }
                }
                if(null != recordList) recordList.clear();
                recordList = MainActivity.myDBMaster.myCarMaintainRecordDB.queryDataList();
                if(null != recordList) {
                    for (int i = 0; i < recordList.size(); i++) {
                        MyCarMaintainRecordBean myCarMaintainRecordBean = (MyCarMaintainRecordBean) recordList.get(i);
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
        if(null != carList) carList.clear();
        carList = MainActivity.myDBMaster.carMaintainDB.queryDataList();
        if (null != carList) {
            for(int i=0; i < carList.size(); i++){
                CarMaintainBean carMaintainBean = (CarMaintainBean) carList.get(i);
                Fruit a = new Fruit("" + carMaintainBean.name, carMaintainBean.icon_byte, "点击设置\n长按删除");
                float text_size = MainActivity.getTextSize(this, this.getWindowManager().getDefaultDisplay().getWidth());
                a.setTextSize(text_size);
                car_name = carMaintainBean.name;
                fruitList.add(a);
            }
        }
    }

    private void setCarDialog(final String car_name){
        final EditText inputServer = new EditText(CarSelectActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(CarSelectActivity.this);
        if(null != carList) carList.clear();
        carList = MainActivity.myDBMaster.carMaintainDB.queryDataList();
        if(null != carList){
            for(int i = 0; i < carList.size(); i++){
                CarMaintainBean carMaintainBean = (CarMaintainBean)carList.get(i);
                if(car_name.equals(carMaintainBean.name)){
                    Bitmap bitmap = BitmapFactory.decodeByteArray(carMaintainBean.icon_byte, 0, carMaintainBean.icon_byte.length);
                    builder.setIcon(new BitmapDrawable(bitmap));
                    break;
                }
            }
        }
        builder.setTitle("输入车牌号").setView(inputServer)
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
                    if(null != recordList) recordList.clear();
                    recordList = MainActivity.myDBMaster.myCarMaintainRecordDB.queryDataList();
                    if (null != recordList) {
                        for(int i = 0; i < recordList.size(); i++){
                            MyCarMaintainRecordBean myCarMaintainRecordBean = (MyCarMaintainRecordBean) recordList.get(i);
                            Log.d("TEST_DEBUG", "已有车型：" + myCarMaintainRecordBean.name+" 车牌号："+myCarMaintainRecordBean.license_plate);
                            if( car_name.equals(myCarMaintainRecordBean.name) && mMeetName.equals(myCarMaintainRecordBean.license_plate) ) {
                                TextView textView = findViewById(R.id.car_select_text);
                                textView.setText("已设置该车牌号车型");
                                textView.setTextColor(Color.rgb(255, 0, 0));
                                Log.d("TEST_DEBUG", "以设置该车牌号车型");
                                return;
                            }
                        }
                    }
                    createCarFirstRecord(car_name, mMeetName);
                }else{
                    TextView textView = findViewById(R.id.car_select_text);
                    textView.setText("未设置车牌");
                    textView.setTextColor(Color.rgb(255, 0, 0));
                }
            }
        });
        builder.show();
    }

    private void createCarFirstRecord(String car_name, String license_plate){
        if(null != carList) carList.clear();
        carList = MainActivity.myDBMaster.carMaintainItemDB.queryDataList();
        if (null != carList) {
            MyCarMaintainRecordBean myCarMaintainRecordBean = new MyCarMaintainRecordBean();
            myCarMaintainRecordBean.name = car_name;
            myCarMaintainRecordBean.license_plate = license_plate;
            myCarMaintainRecordBean.item_mileage = 0;
            myCarMaintainRecordBean.item_date = "";

            TextView textView = findViewById(R.id.car_select_text);
            textView.setText("设置车型成功："+car_name+"\n车牌号："+license_plate);
            textView.setTextColor(Color.rgb(0, 0, 255));
            Log.d("TEST_DEBUG", "\\\\\\\\"+carList.size());
            for(int i=0; i < carList.size(); i++){
                CarMaintainItemBean carMaintainItemBean = (CarMaintainItemBean) carList.get(i);
                if(carMaintainItemBean.name.equals(car_name)) {
                    myCarMaintainRecordBean.item_name = carMaintainItemBean.item_name;
                    MainActivity.myDBMaster.myCarMaintainRecordDB.insertData(myCarMaintainRecordBean);
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
