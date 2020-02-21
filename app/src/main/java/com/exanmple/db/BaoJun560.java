package com.exanmple.db;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.exanmple.carmaintain.MainActivity;
import com.exanmple.carmaintain.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class BaoJun560 {
    final static public Bitmap compressBitmap(Bitmap bitmap){
        int h = bitmap.getHeight();
        int w = bitmap.getWidth();
        int fix = 60;
        if(h > w){
            w = fix*w/h;
            h = fix;
        }else{
            h = fix*h/w;
            w = fix;
        }
        Log.d("TEST DEBUG", "compressBitmap: w="+w+" h="+h);
        return Bitmap.createScaledBitmap(bitmap, w, h, true);
    }

    public BaoJun560(Context context, MyDBMaster myDBMaster) {
        List list = MainActivity.myDBMaster.carMaintainDB.queryDataList();
        if(list != null){
            if(list.size() != 0)
                return;
        }
        CarMaintainBean baoJun560Car = new CarMaintainBean();
        baoJun560Car.name = "宝骏560 1.8L 手动挡";
        baoJun560Car.maintain_mileage_cycle = 5000;
        baoJun560Car.maintain_time_cycle = 6;
        Drawable drawable = context.getResources().getDrawable(R.drawable.baojun);
        Bitmap bitmap = compressBitmap(((BitmapDrawable)drawable).getBitmap());
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        baoJun560Car.icon_byte = os.toByteArray();

        myDBMaster.carMaintainDB.insertData(baoJun560Car);

        CarMaintainItemBean baoJunMaintainItem = new CarMaintainItemBean();
        baoJunMaintainItem.name = baoJun560Car.name;

        baoJunMaintainItem.item_name = "发动机机油 4L (5W-30)";
        baoJunMaintainItem.item_mileage_cycle = 5000;
        baoJunMaintainItem.item_time_cycle = 6;
        drawable = context.getResources().getDrawable(R.drawable.ji_you);
        bitmap = compressBitmap(((BitmapDrawable)drawable).getBitmap());
        os.reset();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        baoJunMaintainItem.icon_byte = os.toByteArray();
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "机油滤请器";
        baoJunMaintainItem.item_mileage_cycle = 5000;
        baoJunMaintainItem.item_time_cycle = 6;
        drawable = context.getResources().getDrawable(R.drawable.ji_you_lv_qing_qi);
        bitmap = compressBitmap(((BitmapDrawable)drawable).getBitmap());
        os.reset();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        baoJunMaintainItem.icon_byte = os.toByteArray();
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "滤芯-空滤总成";
        baoJunMaintainItem.item_mileage_cycle = 10000;
        baoJunMaintainItem.item_time_cycle = 12;
        drawable = context.getResources().getDrawable(R.drawable.kong_qi_lv_qing_qi);
        bitmap = compressBitmap(((BitmapDrawable)drawable).getBitmap());
        os.reset();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        baoJunMaintainItem.icon_byte = os.toByteArray();
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "空调滤清器滤芯";
        baoJunMaintainItem.item_mileage_cycle = 10000;
        baoJunMaintainItem.item_time_cycle = 12;
        drawable = context.getResources().getDrawable(R.drawable.kong_tiao_lv_xin);
        bitmap = compressBitmap(((BitmapDrawable)drawable).getBitmap());
        os.reset();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        baoJunMaintainItem.icon_byte = os.toByteArray();
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "燃油滤清器总成";
        baoJunMaintainItem.item_mileage_cycle = 10000;
        baoJunMaintainItem.item_time_cycle = 12;
        drawable = context.getResources().getDrawable(R.drawable.ran_you_lv_qing_qi);
        bitmap = compressBitmap(((BitmapDrawable)drawable).getBitmap());
        os.reset();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        baoJunMaintainItem.icon_byte = os.toByteArray();
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "变速箱油 75W-90";
        baoJunMaintainItem.item_mileage_cycle = 20000;
        baoJunMaintainItem.item_time_cycle = 24;
        drawable = context.getResources().getDrawable(R.drawable.bian_shu_xiang_you);
        bitmap = compressBitmap(((BitmapDrawable)drawable).getBitmap());
        os.reset();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        baoJunMaintainItem.icon_byte = os.toByteArray();
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "制动液 DOT4";
        baoJunMaintainItem.item_mileage_cycle = 30000;
        baoJunMaintainItem.item_time_cycle = 36;
        drawable = context.getResources().getDrawable(R.drawable.zhi_dong_ye);
        bitmap = compressBitmap(((BitmapDrawable)drawable).getBitmap());
        os.reset();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        baoJunMaintainItem.icon_byte = os.toByteArray();
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "防冻液 -40 摄氏度";
        baoJunMaintainItem.item_mileage_cycle = 20000;
        baoJunMaintainItem.item_time_cycle = 24;
        drawable = context.getResources().getDrawable(R.drawable.fang_dong_ye);
        bitmap = compressBitmap(((BitmapDrawable)drawable).getBitmap());
        os.reset();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        baoJunMaintainItem.icon_byte = os.toByteArray();
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "火花塞";
        baoJunMaintainItem.item_mileage_cycle = 20000;
        baoJunMaintainItem.item_time_cycle = 24;
        drawable = context.getResources().getDrawable(R.drawable.huo_hua_sai);
        bitmap = compressBitmap(((BitmapDrawable)drawable).getBitmap());
        os.reset();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        baoJunMaintainItem.icon_byte = os.toByteArray();
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "节气门清洗";
        baoJunMaintainItem.item_mileage_cycle = 10000;
        baoJunMaintainItem.item_time_cycle = 12;
        drawable = context.getResources().getDrawable(R.drawable.jie_qi_men);
        bitmap = compressBitmap(((BitmapDrawable)drawable).getBitmap());
        os.reset();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        baoJunMaintainItem.icon_byte = os.toByteArray();
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "发动机清洗";
        baoJunMaintainItem.item_mileage_cycle = 10000;
        baoJunMaintainItem.item_time_cycle = 12;
        drawable = context.getResources().getDrawable(R.drawable.fa_dong_ji_qing_xi);
        bitmap = compressBitmap(((BitmapDrawable)drawable).getBitmap());
        os.reset();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        baoJunMaintainItem.icon_byte = os.toByteArray();
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "三元催化清洗";
        baoJunMaintainItem.item_mileage_cycle = 20000;
        baoJunMaintainItem.item_time_cycle = 24;
        drawable = context.getResources().getDrawable(R.drawable.san_yuan_cui_hua_qi_qing_xi);
        bitmap = compressBitmap(((BitmapDrawable)drawable).getBitmap());
        os.reset();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        baoJunMaintainItem.icon_byte = os.toByteArray();
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "左前轮轮胎";
        baoJunMaintainItem.item_mileage_cycle = 100000;
        baoJunMaintainItem.item_time_cycle = 84;
        drawable = context.getResources().getDrawable(R.drawable.lun_tai);
        bitmap = compressBitmap(((BitmapDrawable)drawable).getBitmap());
        os.reset();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        baoJunMaintainItem.icon_byte = os.toByteArray();
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "右前轮轮胎";
        baoJunMaintainItem.item_mileage_cycle = 100000;
        baoJunMaintainItem.item_time_cycle = 84;
        drawable = context.getResources().getDrawable(R.drawable.lun_tai);
        bitmap = compressBitmap(((BitmapDrawable)drawable).getBitmap());
        os.reset();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        baoJunMaintainItem.icon_byte = os.toByteArray();
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "左后轮轮胎";
        baoJunMaintainItem.item_mileage_cycle = 100000;
        baoJunMaintainItem.item_time_cycle = 84;
        drawable = context.getResources().getDrawable(R.drawable.lun_tai);
        bitmap = compressBitmap(((BitmapDrawable)drawable).getBitmap());
        os.reset();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        baoJunMaintainItem.icon_byte = os.toByteArray();
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "右后轮轮胎";
        baoJunMaintainItem.item_mileage_cycle = 100000;
        baoJunMaintainItem.item_time_cycle = 84;
        drawable = context.getResources().getDrawable(R.drawable.lun_tai);
        bitmap = compressBitmap(((BitmapDrawable)drawable).getBitmap());
        os.reset();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        baoJunMaintainItem.icon_byte = os.toByteArray();
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "前刹车片";
        baoJunMaintainItem.item_mileage_cycle = 100000;
        baoJunMaintainItem.item_time_cycle = 84;
        drawable = context.getResources().getDrawable(R.drawable.qian_sha_che_pian);
        bitmap = compressBitmap(((BitmapDrawable)drawable).getBitmap());
        os.reset();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        baoJunMaintainItem.icon_byte = os.toByteArray();
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "后刹车片";
        baoJunMaintainItem.item_mileage_cycle = 100000;
        baoJunMaintainItem.item_time_cycle = 84;
        drawable = context.getResources().getDrawable(R.drawable.hou_sha_che_pian);
        bitmap = compressBitmap(((BitmapDrawable)drawable).getBitmap());
        os.reset();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        baoJunMaintainItem.icon_byte = os.toByteArray();
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "天窗轨道润滑油";
        baoJunMaintainItem.item_mileage_cycle = 60000;
        baoJunMaintainItem.item_time_cycle = 36;
        drawable = context.getResources().getDrawable(R.drawable.tian_chuang_gui_dao_run_hua_you);
        bitmap = compressBitmap(((BitmapDrawable)drawable).getBitmap());
        os.reset();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        baoJunMaintainItem.icon_byte = os.toByteArray();
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "4门润滑油";
        baoJunMaintainItem.item_mileage_cycle = 60000;
        baoJunMaintainItem.item_time_cycle = 36;
        drawable = context.getResources().getDrawable(R.drawable.che_men_ren_hua_you);
        bitmap = compressBitmap(((BitmapDrawable)drawable).getBitmap());
        os.reset();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        baoJunMaintainItem.icon_byte = os.toByteArray();
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "燃油添加剂";
        baoJunMaintainItem.item_mileage_cycle = 20000;
        baoJunMaintainItem.item_time_cycle = 12;
        drawable = context.getResources().getDrawable(R.drawable.ran_you_tian_jia_ji);
        bitmap = compressBitmap(((BitmapDrawable)drawable).getBitmap());
        os.reset();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        baoJunMaintainItem.icon_byte = os.toByteArray();
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "蓄电池";
        baoJunMaintainItem.item_mileage_cycle = 100000;
        baoJunMaintainItem.item_time_cycle = 96;
        drawable = context.getResources().getDrawable(R.drawable.xu_dian_chi);
        bitmap = compressBitmap(((BitmapDrawable)drawable).getBitmap());
        os.reset();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        baoJunMaintainItem.icon_byte = os.toByteArray();
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "发动机皮带";
        baoJunMaintainItem.item_mileage_cycle = 200000;
        baoJunMaintainItem.item_time_cycle = 120;
        drawable = context.getResources().getDrawable(R.drawable.zhen_shi_pi_dai);
        bitmap = compressBitmap(((BitmapDrawable)drawable).getBitmap());
        os.reset();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        baoJunMaintainItem.icon_byte = os.toByteArray();
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "前雨刷片";
        baoJunMaintainItem.item_mileage_cycle = 200000;
        baoJunMaintainItem.item_time_cycle = 120;
        drawable = context.getResources().getDrawable(R.drawable.qian_yu_shua_pian);
        bitmap = compressBitmap(((BitmapDrawable)drawable).getBitmap());
        os.reset();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        baoJunMaintainItem.icon_byte = os.toByteArray();
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);

        baoJunMaintainItem.item_name = "后雨刷片";
        baoJunMaintainItem.item_mileage_cycle = 200000;
        baoJunMaintainItem.item_time_cycle = 120;
        drawable = context.getResources().getDrawable(R.drawable.hou_yu_shua_pian);
        bitmap = compressBitmap(((BitmapDrawable)drawable).getBitmap());
        os.reset();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        baoJunMaintainItem.icon_byte = os.toByteArray();
        myDBMaster.carMaintainItemDB.insertData(baoJunMaintainItem);
    }
}
