package com.exanmple.carmaintain;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.exanmple.db.CarMaintainBean;
import com.exanmple.db.CarMaintainItemBean;
import com.exanmple.db.MyCarMaintainRecordBean;

import java.util.ArrayList;
import java.util.List;

public class ItemSetActivity extends AppCompatActivity {
    private ExpandableListView expandableListView;
    private ExpandListViewAdapter myAdapter;
    private List<String> groupList;
    private List<List<String>> childList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_set);

        initView();
    }

    private void initView() {
        expandableListView = (ExpandableListView) findViewById(R.id.maintain_list);
        groupList = new ArrayList<>();
        childList = new ArrayList<>();

        TextView textView = (TextView)findViewById(R.id.car_text);

        List carlist = MainActivity.myDBMaster.myCarMaintainRecordDB.queryDataList();
        List itemlist = MainActivity.myDBMaster.carMaintainItemDB.queryDataList();

        String car_name = null;

        if (null != carlist && null != itemlist) {
            textView.setText("以设置车辆信息");
            List <String> car = new ArrayList<>();;
            List <String> license_plate =  new ArrayList<>();
            for(int i=0; i < carlist.size(); i++) {
                boolean flag = false;
                MyCarMaintainRecordBean myCarMaintainRecordBean = (MyCarMaintainRecordBean) carlist.get(i);
                for(int j=0; j < car.size(); j++){
                    if(car.get(j).equals(myCarMaintainRecordBean.name)){
                        flag = true;
                        break;
                    }
                }

                if(flag == true)
                    continue;

                car.add(myCarMaintainRecordBean.name);
                license_plate.add(myCarMaintainRecordBean.license_plate);
                Log.d("TEST_DEBUG","车辆:"+myCarMaintainRecordBean.name+" 车牌："+myCarMaintainRecordBean.license_plate);
            }
            Log.d("TEST_DEBUG","===="+car.size());
            for(int i=0; i < car.size(); i++){
                Log.d("TEST_DEBUG","车辆:"+car.get(i)+" 车牌："+license_plate.get(i));
                List <String> item = new ArrayList<>();
                for (int j = 0; j < itemlist.size(); j++) {
                    CarMaintainItemBean carMaintainItemBean = (CarMaintainItemBean) itemlist.get(j);
                    if (car.get(i).equals(carMaintainItemBean.name)) {
                        item.add(carMaintainItemBean.item_name);
                    }
                }

                String []itemString = new String[item.size()];
                for(int j=0; j < item.size(); j++)
                    itemString[j] = item.get(j);

                addData(car.get(i)+"  车牌号："+license_plate.get(i),itemString);
            }
        }

        myAdapter = new ExpandListViewAdapter(this,groupList,childList);
        expandableListView.setAdapter(myAdapter);

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
