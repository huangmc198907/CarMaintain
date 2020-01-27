package com.exanmple.db;

import com.exanmple.carmaintain.MainActivity;

import java.util.List;

public class BaoJun560 {
    public BaoJun560(MyDBMaster myDBMaster) {
        List list = MainActivity.myDBMaster.carMaintainDB.queryDataList();
        if(list != null){
            if(list.size() != 0)
                return;
        }
        CarMaintainBean baoJun560Car = new CarMaintainBean();
        baoJun560Car.name = "宝骏560 1.8L 手动挡";
        baoJun560Car.maintain_mileage_cycle = 5000;
        baoJun560Car.maintain_time_cycle = 3;
        baoJun560Car.icon_path = "baojun.jpg";

        myDBMaster.carMaintainDB.insertData(baoJun560Car);

        CarMaintainItemBean baoJunMaintainItem = new CarMaintainItemBean();
        baoJunMaintainItem.name = baoJun560Car.name;

        baoJunMaintainItem.item_name = "机油机滤";
        baoJunMaintainItem.item_mileage_cycle = 5000;
        baoJunMaintainItem.item_time_cycle = 6;
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "空气滤清器";
        baoJunMaintainItem.item_mileage_cycle = 10000;
        baoJunMaintainItem.item_time_cycle = 6;
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "空调滤清器";
        baoJunMaintainItem.item_mileage_cycle = 10000;
        baoJunMaintainItem.item_time_cycle = 6;
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "汽油滤清器";
        baoJunMaintainItem.item_mileage_cycle = 10000;
        baoJunMaintainItem.item_time_cycle = 6;
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "变速箱油";
        baoJunMaintainItem.item_mileage_cycle = 20000;
        baoJunMaintainItem.item_time_cycle = 24;
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "刹车油";
        baoJunMaintainItem.item_mileage_cycle = 35000;
        baoJunMaintainItem.item_time_cycle = 36;
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "防冻液";
        baoJunMaintainItem.item_mileage_cycle = 20000;
        baoJunMaintainItem.item_time_cycle = 24;
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "火花塞";
        baoJunMaintainItem.item_mileage_cycle = 20000;
        baoJunMaintainItem.item_time_cycle = 24;
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "节气门清洗";
        baoJunMaintainItem.item_mileage_cycle = 10000;
        baoJunMaintainItem.item_time_cycle = 12;
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "发动机清洗";
        baoJunMaintainItem.item_mileage_cycle = 10000;
        baoJunMaintainItem.item_time_cycle = 12;
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "三元催化清洗";
        baoJunMaintainItem.item_mileage_cycle = 20000;
        baoJunMaintainItem.item_time_cycle = 24;
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "左前轮轮胎";
        baoJunMaintainItem.item_mileage_cycle = 100000;
        baoJunMaintainItem.item_time_cycle = 84;
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "右前轮轮胎";
        baoJunMaintainItem.item_mileage_cycle = 100000;
        baoJunMaintainItem.item_time_cycle = 84;
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "左后轮轮胎";
        baoJunMaintainItem.item_mileage_cycle = 100000;
        baoJunMaintainItem.item_time_cycle = 84;
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "右后轮轮胎";
        baoJunMaintainItem.item_mileage_cycle = 100000;
        baoJunMaintainItem.item_time_cycle = 84;
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "左前轮轮胎";
        baoJunMaintainItem.item_mileage_cycle = 100000;
        baoJunMaintainItem.item_time_cycle = 84;
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "前刹车片";
        baoJunMaintainItem.item_mileage_cycle = 100000;
        baoJunMaintainItem.item_time_cycle = 84;
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "后刹车片";
        baoJunMaintainItem.item_mileage_cycle = 100000;
        baoJunMaintainItem.item_time_cycle = 84;
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "天窗轨道润滑油";
        baoJunMaintainItem.item_mileage_cycle = 60000;
        baoJunMaintainItem.item_time_cycle = 36;
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "4门润滑油";
        baoJunMaintainItem.item_mileage_cycle = 60000;
        baoJunMaintainItem.item_time_cycle = 36;
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "燃油添加剂";
        baoJunMaintainItem.item_mileage_cycle = 20000;
        baoJunMaintainItem.item_time_cycle = 12;
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "蓄电池";
        baoJunMaintainItem.item_mileage_cycle = 100000;
        baoJunMaintainItem.item_time_cycle = 96;
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "发动机皮带";
        baoJunMaintainItem.item_mileage_cycle = 200000;
        baoJunMaintainItem.item_time_cycle = 120;
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "前雨刷片";
        baoJunMaintainItem.item_mileage_cycle = 200000;
        baoJunMaintainItem.item_time_cycle = 120;
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "后雨刷片";
        baoJunMaintainItem.item_mileage_cycle = 200000;
        baoJunMaintainItem.item_time_cycle = 120;
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);
    }
}
