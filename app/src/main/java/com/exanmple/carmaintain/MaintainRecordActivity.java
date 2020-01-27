package com.exanmple.carmaintain;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import com.exanmple.db.MyCarMaintainRecordBean;
import com.exanmple.myview.OuterAdaper;
import com.exanmple.myview.ThreeExpandListView;

import java.util.ArrayList;
import java.util.List;

public class MaintainRecordActivity extends AppCompatActivity {
    private ExpandableListView expandableListView;
    List<ThreeExpandListView> threeExpandList=new ArrayList<>();

    private String buildRecordString(String car_name, String license_plate){
        return "型号："+car_name+" 车牌："+license_plate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintain_record);
        expandableListView = findViewById(R.id.maintain_record_expandable_listView);
        initData();
        //创建适配器实例，设置适配器
        final OuterAdaper adapter=new OuterAdaper(threeExpandList,this);
        final int[] outAdapterPosition = {0};
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
                }else if(groupPosition >= 0 && outAdapterItemLongClickFlag[0] == true) {
                    outAdapterItemLongClickFlag[0] = false;
                    ExpandableListView listView = adapter.getIneerExpandListView(groupPosition);
                    final long secondPackedPosition = listView.getExpandableListPosition(outAdapterPosition[0]);
                    int secondGroupPosition = listView.getPackedPositionGroup(secondPackedPosition);
                    int secondChildPosition = listView.getPackedPositionChild(secondPackedPosition);
                    if(secondGroupPosition >= 0 && secondChildPosition < 0){
                        String car = threeExpandList.get(groupPosition).getName();
                        String mileage = threeExpandList.get(groupPosition).getFirstListViews().get(secondGroupPosition).getName();
                        Log.d("==", "car: "+car+" mileage:"+mileage);
                    }else if(secondChildPosition >= 0){
                        String car = threeExpandList.get(groupPosition).getName();
                        String mileage = threeExpandList.get(groupPosition).getFirstListViews().get(secondGroupPosition).getName();
                        String item = threeExpandList.get(groupPosition).getFirstListViews().get(secondGroupPosition).getSecondList().get(secondChildPosition).getName();
                        Log.d("==", "car: "+car+" mileage:"+mileage+" item:"+item);
                    }
                }
                return false;
            }
        });
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
        }else{
            Log.d("++++++", "initData: no no no ");
        }
    }
}
