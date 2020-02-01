package com.exanmple.myview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.exanmple.carmaintain.R;

import java.util.List;

public class ExpandListViewAdapter extends BaseExpandableListAdapter {
    private List<String> groupList;//外层的数据源
    private List<List<String>> childList;//里层的数据源
    private Context context;
    private float groupTextSize = 20;
    private float childTextSize = 20;
    private String groupRightText = "";
    private String childRightText = "";

    public ExpandListViewAdapter(Context context, List<String> groupList,List<List<String>> childList ){
        this.context = context;
        this.groupList = groupList;
        this.childList = childList;
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    public void setTextSize(float groupTSize, float childTSize){
        this.groupTextSize = groupTSize;
        this.childTextSize = childTSize;
    }

    public void setGroupRightText(String text){
        this.groupRightText = text;
    }

    public void setChildRightText(String text){
        this.childRightText = text;
    }

    public float getTextSize(){
        return this.groupTextSize;
    }

    /**
     * 这个返回的一定要是对应外层的item里面的List集合的size
     * @param groupPosition
     * @return
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return childList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupPosition;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = View.inflate(context, R.layout.item_group, null);
        //分组名字
        TextView textView = (TextView) convertView.findViewById(R.id.group_textview);
        //子元素的个数
        TextView number = (TextView) convertView.findViewById(R.id.group_number);
        TextView addText = (TextView) convertView.findViewById(R.id.add_text);
        number.setText(childList.get(groupPosition).size()+"个");
        textView.setText(groupList.get(groupPosition));
        if(!this.groupRightText.equals(""))
            addText.setText(this.groupRightText);
        if(groupTextSize > 0) {
            textView.setTextSize(groupTextSize);
            number.setTextSize(groupTextSize * 2 / 3);
            addText.setTextSize(groupTextSize * 2 / 3);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup viewGroup) {
        view = View.inflate(context, R.layout.item_child, null);
        TextView textView = (TextView) view.findViewById(R.id.child_name);
        TextView modelView = (TextView) view.findViewById(R.id.child_model);
        //外层的分组名字
        textView.setText(childList.get(groupPosition).get(childPosition));
        if(!this.childRightText.equals(""))
            modelView.setText(this.childRightText);
        if(childTextSize > 0) {
            textView.setTextSize(childTextSize);
            modelView.setTextSize(childTextSize * 2 / 3);
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
