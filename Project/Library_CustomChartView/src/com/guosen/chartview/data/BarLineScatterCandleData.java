package com.guosen.chartview.data;



import java.util.List;

/**
 * 折线图和棒状图，散点图，烛台图的基类.
 * 
 * @author Philipp Jahoda
 */
public abstract class BarLineScatterCandleData<T extends BarLineScatterCandleDataSet<? extends Entry>>
        extends ChartData<T> {
    
    public BarLineScatterCandleData() {
        super();
    }
    
    public BarLineScatterCandleData(List<String> xVals) {
        super(xVals);
    }
    
    public BarLineScatterCandleData(String[] xVals) {
        super(xVals);
    }

    public BarLineScatterCandleData(List<String> xVals, List<T> sets) {
        super(xVals, sets);
    }

    public BarLineScatterCandleData(String[] xVals, List<T> sets) {
        super(xVals, sets);
    }
}
