package com.exanmple.myview;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

public class InnerExpandableListView extends ExpandableListView {
    public InnerExpandableListView(Context context) {
        super(context);
    }

    public InnerExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InnerExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public InnerExpandableListView(Activity activity) {
        super(activity);
    }

    //通过此方法重新计算expandableListView的高度，使得里边的expandableListView全部显示
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
