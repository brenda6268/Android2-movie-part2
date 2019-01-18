package com.br.gridviewmovie.mode;

import android.view.View;
import android.widget.ListView;

/**
 * Created by nodeuser on 24/03/18.
 */

public class MyListView extends ListView {
    public MyListView(android.content.Context context,android.util.AttributeSet attrs){
        super(context, attrs);
    }
    /**
     * 设置不滚动
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }
}
