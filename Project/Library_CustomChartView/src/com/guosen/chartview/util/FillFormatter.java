package com.guosen.chartview.util;

import com.guosen.chartview.data.LineData;
import com.guosen.chartview.data.LineDataSet;

/**
 * Interface for providing a custom logic to where the filling line of a DataSet
 * should end. If setFillEnabled(...) is set to true.
 * 接口提供了一个自定义的逻辑来实现数据集的填充线应该在哪里结束
 * @author Philipp Jahoda
 */
public interface FillFormatter {

    /**
     * Returns the vertical (y-axis) position where the filled-line of the
     * DataSet should end.
     * 数据集的填充线应该结束的Y轴方向位置
     * @param dataSet
     * @param data
     * @param chartMaxY
     * @param chartMinY
     * @return
     */
    public float getFillLinePosition(LineDataSet dataSet, LineData data, float chartMaxY,
            float chartMinY);
}
