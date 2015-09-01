package com.guosen.chartview.render;

import com.guosen.chartview.interfaces.BarLineScatterCandleDataProvider;
import com.guosen.chartview.util.ViewPortHandler;



/**
 * Abstract baseclass of all Renderers.
 * 
 * @author Philipp Jahoda
 */
public abstract class Renderer {

    /**
     * the component that handles the drawing area of the chart and it's offsets
     * 用来处理图表绘制区域和边界的组件
     */
    protected ViewPortHandler mViewPortHandler;

    /** the minimum value on the x-axis that should be plotted
     * X轴上应该被绘制的最小值*/
    protected int mMinX = 0;

    /** the maximum value on the x-axis that should be plotted
     * X轴上应该被绘制的最大值*/
    protected int mMaxX = 0;

    public Renderer(ViewPortHandler viewPortHandler) {
        this.mViewPortHandler = viewPortHandler;
    }

    /**
     * Returns true if the specified value fits in between the provided min
     * and max bounds, false if not.
     * 
     * @param val
     * @param min
     * @param max
     * @return
     */
    protected boolean fitsBounds(float val, float min, float max) {

        if (val < min || val > max)
            return false;
        else
            return true;
    }

    /**
     * Calculates the minimum and maximum x-value the chart can currently
     * display (with the given zoom level).
     * @param chart
     * @param modulus
     */
    public void calcXBounds(BarLineScatterCandleDataProvider chart, int xAxisModulus) {
        
        int low = chart.getLowestVisibleXIndex();
        int high = chart.getHighestVisibleXIndex();
        
        int subLow = (low % xAxisModulus == 0) ? xAxisModulus : 0;
        
        mMinX = Math.max((low / xAxisModulus) * (xAxisModulus) - subLow, 0);
        mMaxX = Math.min((high / xAxisModulus) * (xAxisModulus) + xAxisModulus, (int) chart.getXChartMax());
    }
}
