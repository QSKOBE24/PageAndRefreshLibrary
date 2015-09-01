
package com.guosen.chartview.charts;

import android.content.Context;
import android.util.AttributeSet;
import com.guosen.chartview.animation.Easing;
import com.guosen.chartview.component.MarkerView;
import com.guosen.chartview.component.MyMarkerView;
import com.guosen.chartview.data.LineData;
import com.guosen.chartview.data.LineDataSet;
import com.guosen.chartview.interfaces.LineDataProvider;
import com.guosen.chartview.render.LineChartRenderer;
import com.guosen.chartview.util.FillFormatter;
import com.guosen.library_customchartview.R;

/**
 * Chart that draws lines, surfaces, circles, ...
 * 折线线性图表类
 * @author Philipp Jahoda
 */
public class LineChart extends BarLineChartBase<LineData> implements LineDataProvider {

    private FillFormatter mFillFormatter;

    private MarkerView    mMarkerView; 
    
    public  LineChart(Context context) {
        super(context);
    }

    public  LineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public  LineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();
        //自定义的最后一个数值上方的小图标
        getAxisLeft().enableGridDashedLine(6f, 3f, 0f);
        animateX(2500, Easing.EasingOption.EaseInCubic);
        setData(null);
        mRenderer = new LineChartRenderer(this, mAnimator, mViewPortHandler);
        mFillFormatter = new DefaultFillFormatter();
        
        mMarkerView = new  MyMarkerView(getContext(), R.layout.custom_marker_view);
        setMarkerView(mMarkerView);
    }

    @Override
    protected void calcMinMax() {
        super.calcMinMax();

        if (mDeltaX == 0 && mData.getYValCount() > 0)
            mDeltaX = 1;
    }

    @Override
    public void setFillFormatter(FillFormatter formatter) {
        if (formatter == null)
            formatter = new DefaultFillFormatter();
        else
            mFillFormatter = formatter;
    }

    @Override
    public FillFormatter getFillFormatter() {
        return mFillFormatter;
    }
    
    @Override
    public LineData getLineData() {
        return mData;
    }
    
    
    
}
