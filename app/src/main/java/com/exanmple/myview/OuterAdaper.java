package com.exanmple.myview;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.exanmple.carmaintain.MainActivity;
import com.exanmple.carmaintain.R;

import java.util.ArrayList;
import java.util.List;

public class OuterAdaper extends BaseExpandableListAdapter {
    private List<ThreeExpandListView> firstList;//国家的集合
    private Activity activity;//创建布局使用
    public static OnItemLongClickListener itemLongClickListener;
    public static ExpandableListView.OnChildClickListener childClickListener;
    private List<InnerExpandableListView> innerExpandableListViews = new ArrayList<>();

    public void setItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        OuterAdaper.itemLongClickListener = itemLongClickListener;
    }

    public void setChildClickListener(ExpandableListView.OnChildClickListener childClickListener) {
        OuterAdaper.childClickListener = childClickListener;
    }

    //构造方法
    public OuterAdaper(List<ThreeExpandListView> firstList, Activity activity) {
        if (firstList != null) {
            this.firstList = firstList;
        } else firstList = new ArrayList<>();
        this.activity = activity;
    }

    public ExpandableListView getIneerExpandListView(int id) {
        return innerExpandableListViews.get(id);
    }

    // 组数量
    @Override
    public int getGroupCount() {
        return firstList.size();
    }
    //子View数量
    @Override
    public int getChildrenCount(int i) {
        return 1;
    }
    //获取组对象
    @Override
    public Object getGroup(int i) {
        return firstList.get(i);
    }
    //获取子View对象
    @Override
    public Object getChild(int i, int i1) {
        return firstList.get(i).getFirstListViews().get(i1);
    }
    // 组View下标
    @Override
    public long getGroupId(int i) {
        return i;
    }
    //子View下标
    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    //获取组布局
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = View.inflate(activity, R.layout.expand_list_first, null);
        //分组名字
        TextView textView = (TextView) convertView.findViewById(R.id.first_textview);
        //子元素的个数
        TextView number = (TextView) convertView.findViewById(R.id.first_number);
        TextView addText = (TextView) convertView.findViewById(R.id.first_text);
        number.setText(firstList.get(groupPosition).getSize()+"个");
        textView.setText(firstList.get(groupPosition).getName());
        float text_size = MainActivity.getTextSize(activity, activity.getWindowManager().getDefaultDisplay().getWidth());
        textView.setTextSize(text_size);
        number.setTextSize(text_size * 2 / 3);
        addText.setTextSize(text_size * 2 / 3);
        return convertView;
        /*
        TextView textView = new TextView(activity);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(params);
        //textView.setLeft(20);
        //textView.setPadding(90, 15, 10, 15);
        WindowManager wm1 = activity.getWindowManager();
        int width1 = wm1.getDefaultDisplay().getWidth();
        textView.setLeft(width1/20);
        textView.setPadding(width1/20, 15, 10, 15);
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        Log.d("=======", "getGroupView: "+width1);
        textView.setText(firstList.get(groupPosition).getName());
        textView.setBackgroundColor(Color.rgb(255,218,185));
        textView.setTextSize(width1/ MainActivity.TEXT_BIG_SIZE);
        return textView;
         */
    }
    //获取子View
    @Override
    public View getChildView(int i, int i1, boolean b, final View view, ViewGroup viewGroup) {
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        InnerExpandableListView expandableListView = new InnerExpandableListView(activity);
        expandableListView.setLayoutParams(params);
        InnerAdapter adapter = new InnerAdapter(firstList.get(i).getFirstListViews(), activity);
        expandableListView.setAdapter(adapter);
        WindowManager wm1 = activity.getWindowManager();
        int width1 = wm1.getDefaultDisplay().getWidth();
        //expandableListView.setPadding(90, 15, 5, 15);
        expandableListView.setPadding(width1/20, 15, 5, 15);
        expandableListView.setOnItemLongClickListener(OuterAdaper.itemLongClickListener);
        expandableListView.setOnChildClickListener(OuterAdaper.childClickListener);
        expandableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        innerExpandableListViews.add(expandableListView);
        return expandableListView;
    }
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
