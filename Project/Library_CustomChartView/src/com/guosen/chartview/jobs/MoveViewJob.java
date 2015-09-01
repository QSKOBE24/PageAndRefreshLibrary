package com.guosen.chartview.jobs;




import android.view.View;

import com.guosen.chartview.util.Transformer;
import com.guosen.chartview.util.ViewPortHandler;

/**
 * Runnable that is used for viewport modifications since they cannot be
 * executed at any time. This can be used to delay the execution of viewport
 * modifications until the onSizeChanged(...) method of the chartview is called.
 * 一个任务耗时：用于窗口修改因为他们不能
 * 在任何时间执行。这可以用于延迟执行窗口修改直到图表视图的onSizeChanged()被调用
 * @author QSKOBE24
 */
public class MoveViewJob implements Runnable {

    protected ViewPortHandler mViewPortHandler;
    protected float xIndex = 0f;
    protected float yValue = 0f;
    protected Transformer mTrans;
    protected View view;

    public MoveViewJob(ViewPortHandler viewPortHandler, float xIndex, float yValue,
            Transformer trans, View v) {

        this.mViewPortHandler = viewPortHandler;
        this.xIndex = xIndex;
        this.yValue = yValue;
        this.mTrans = trans;
        this.view = v;
    }

    @Override
    public void run() {

        float[] pts = new float[] {
                xIndex, yValue
        };

        mTrans.pointValuesToPixel(pts);
        mViewPortHandler.centerViewPort(pts, view);
    }
}
