package com.guosen.chartview.component;
import android.content.Context;
import android.graphics.Color;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guosen.chartview.component.MarkerView;
import com.guosen.chartview.data.Entry;
import com.guosen.chartview.util.Utils;
import com.guosen.library_customchartview.R;

/**
 * Custom implementation of the MarkerView.
 * 
 * @author QSKOBE24
 */
public class MyMarkerView extends MarkerView {
    private RelativeLayout relativeLayout;
    private TextView tvContent;

    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        relativeLayout = (RelativeLayout)findViewById(R.id.relativelayout);
        tvContent = (TextView) findViewById(R.id.tvContent);
    }

	@Override
    public void refreshContent(Entry e, int dataSetIndex) {
        	if(e.getData().equals(true)){
        		tvContent.setText("" + Utils.formatNumber(Float.parseFloat(e.getExtra()), 5, true));
                tvContent.setTextSize(13);
                tvContent.setTextColor(Color.WHITE);
        		relativeLayout.setBackgroundResource(R.drawable.markerview_down);
        	}else {
        		tvContent.setText("" + Utils.formatNumber(Float.parseFloat(e.getExtra()), 5, true));
                tvContent.setTextSize(13);
                tvContent.setTextColor(Color.WHITE);
                relativeLayout.setBackgroundResource(R.drawable.markerview_up);
			}
            
    }

    @Override
    public int getXOffset() {
        // this will center the marker-view horizontally
        return -(getWidth() / 2);
    }

    @Override
    public int getYOffset() {
        // this will cause the marker-view to be above the selected value
        return -getHeight();
    }
}
