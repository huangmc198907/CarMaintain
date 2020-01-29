package com.exanmple.myview;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.exanmple.carmaintain.MainActivity;
import com.exanmple.carmaintain.R;

import java.util.ArrayList;
import java.util.List;

public class InnerAdapter extends BaseExpandableListAdapter {
    private List<ThreeExpandListView.FirstListView> secondListView;//省的集合
    private Activity activity;//创建布局使用
    //构造参数
    public InnerAdapter(List<ThreeExpandListView.FirstListView> secondListView, Activity activity) {
        if (secondListView!=null) {
            this.secondListView = secondListView;
        }else secondListView=new ArrayList<>();
        this.activity = activity;
    }
    //获取外层组个数
    @Override
    public int getGroupCount() {
        return secondListView.size();
    }
    //获取内层组个数
    @Override
    public int getChildrenCount(int i) {
        return secondListView.get(i).getSecondList().size();
    }
    //获取外层组
    @Override
    public Object getGroup(int i) {
        return secondListView.get(i);
    }
    //获取内层数据
    @Override
    public Object getChild(int i, int i1) {
        return secondListView.get(i).getSecondList().get(i1);
    }
    //组下标
    @Override
    public long getGroupId(int i) {
        return i;
    }
    //子View下标
    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }
    //不知道做什么用
    @Override
    public boolean hasStableIds() {
        return false;
    }
    //获取组布局
    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        view = View.inflate(activity, R.layout.expand_list_second, null);
        //分组名字
        TextView textView = (TextView) view.findViewById(R.id.second_name);
        //子元素的个数
        TextView number = (TextView) view.findViewById(R.id.second_number);
        TextView modeText = (TextView) view.findViewById(R.id.second_model);
        number.setText(secondListView.get(i).getSize()+"个");
        textView.setText(secondListView.get(i).getName());
        float text_size = MainActivity.getTextSize(activity, activity.getWindowManager().getDefaultDisplay().getWidth());
        textView.setTextSize(text_size);
        number.setTextSize(text_size * 4 / 5);
        modeText.setTextSize(text_size * 4 / 5);
        return view;

        /*
        RelativeLayout relativeLayout = new RelativeLayout(activity);
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        relativeLayout.setLayoutParams(params);

        TextView textView=new TextView(activity);
        textView.setLayoutParams(params);
        //textView.setPadding(90,15,10,15);
        WindowManager wm1 = activity.getWindowManager();
        int width1 = wm1.getDefaultDisplay().getWidth();
        textView.setPadding(width1/20,15,10,15);
        textView.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
        textView.setText(secondListView.get(i).getName());
        textView.setBackgroundColor(Color.rgb(222,184,135));
        textView.setTextSize(width1/ MainActivity.TEXT_MIDDLE_SIZE);

        relativeLayout.addView(textView);
        return relativeLayout;
         */
    }
    //获取子View布局
    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        view = View.inflate(activity, R.layout.expand_list_third, null);
        //分组名字
        TextView textView = (TextView) view.findViewById(R.id.third_name);
        //子元素的个数
        TextView modeText = (TextView) view.findViewById(R.id.third_model);
        textView.setText(secondListView.get(i).getSecondList().get(i1).getName());
        float text_size = MainActivity.getTextSize(activity, activity.getWindowManager().getDefaultDisplay().getWidth());
        textView.setTextSize(text_size);
        modeText.setTextSize(text_size * 4 / 5);
        return view;

        /*
        TextView textView=new TextView(activity);
        AbsListView.LayoutParams params=new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(params);
        //textView.setPadding(110,15,10,15);
        WindowManager wm1 = activity.getWindowManager();
        int width1 = wm1.getDefaultDisplay().getWidth();
        Log.d("=======", "getChildView: "+width1);
        textView.setPadding(width1/10,15,10,15);
        textView.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
        textView.setText(secondListView.get(i).getCities().get(i1).getName());
        textView.setBackgroundColor(Color.rgb(255,160,122));
        textView.setTextSize(width1/ MainActivity.TEXT_LITTLE_SIZE);
        return textView;

         */
    }
    //子View是否可选
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
