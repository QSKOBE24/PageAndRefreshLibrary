package com.guosen.chartview.interfaces;



import com.guosen.chartview.util.ValueFormatter;

import android.graphics.PointF;
import android.graphics.RectF;


/**
 * Interface that provides everything there is to know about the dimensions,
 * bounds, and range of the chart.
 * 有关于尺寸，边界和图表范围的所有信息的接口
 * @author Philipp Jahoda
 */
public interface ChartInterface {

    public float getXChartMin();

    public float getXChartMax();

    public float getYChartMin();

    public float getYChartMax();
    
    public int getXValCount();

    public int getWidth();

    public int getHeight();

    public PointF getCenterOfView();

    public PointF getCenterOffsets();

    public RectF getContentRect();
    
    public ValueFormatter getDefaultValueFormatter();

}
