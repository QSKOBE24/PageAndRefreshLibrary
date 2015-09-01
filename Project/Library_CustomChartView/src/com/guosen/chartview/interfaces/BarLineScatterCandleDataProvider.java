package com.guosen.chartview.interfaces;

import com.guosen.chartview.component.YAxis.AxisDependency;
import com.guosen.chartview.util.Transformer;

public interface BarLineScatterCandleDataProvider extends ChartInterface {
    public Transformer getTransformer(AxisDependency axis);
    public int getMaxVisibleCount();
    public boolean isInverted(AxisDependency axis);    
    public int getLowestVisibleXIndex();
    public int getHighestVisibleXIndex();
}
