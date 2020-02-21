package com.exanmple.myview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.exanmple.carmaintain.R;
import com.exanmple.myview.Fruit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class FruitAdapter extends ArrayAdapter<Fruit> {
    private int resourceId;

    // 适配器的构造函数，把要适配的数据传入这里
    public FruitAdapter(Context context, int textViewResourceId, List<Fruit> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }

    // convertView 参数用于将之前加载好的布局进行缓存
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Fruit fruit=getItem(position); //获取当前项的Fruit实例

        // 加个判断，以免ListView每次滚动时都要重新加载布局，以提高运行效率
        View view;
        ViewHolder viewHolder;
        if (convertView==null){

            // 避免ListView每次滚动时都要重新加载布局，以提高运行效率
            view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

            // 避免每次调用getView()时都要重新获取控件实例
            viewHolder=new ViewHolder();
            viewHolder.fruitImage=view.findViewById(R.id.fruit_image);
            viewHolder.fruitName=view.findViewById(R.id.fruit_name);
            TextView textView = (TextView) view.findViewById(R.id.fruit_text);
            textView.setText(fruit.getAdditionalText());

            // 将ViewHolder存储在View中（即将控件的实例存储在其中）
            view.setTag(viewHolder);
        } else{
            view=convertView;
            viewHolder=(ViewHolder) view.getTag();
        }

        // 获取控件实例，并调用set...方法使其显示出来
        byte[] b = fruit.getImageIcon();
        Log.d("====", "getView: b.length="+b.length);
        Bitmap bitmap1 = BitmapFactory.decodeByteArray(b, 0, b.length, null);
        viewHolder.fruitImage.setImageBitmap(bitmap1);
        viewHolder.fruitName.setText(fruit.getName());
        if(fruit.getTextSize() > 0)
            viewHolder.fruitName.setTextSize(fruit.getTextSize());
        return view;
    }

    // 定义一个内部类，用于对控件的实例进行缓存
    class ViewHolder{
        ImageView fruitImage;
        TextView fruitName;
    }
}
