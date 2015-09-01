
package com.guosen.chartview.render;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;

import com.guosen.chartview.component.LimitLine;
import com.guosen.chartview.component.YAxis;
import com.guosen.chartview.component.LimitLine.LimitLabelPosition;
import com.guosen.chartview.component.YAxis.AxisDependency;
import com.guosen.chartview.component.YAxis.YAxisLabelPosition;
import com.guosen.chartview.util.PointD;
import com.guosen.chartview.util.Transformer;
import com.guosen.chartview.util.Utils;
import com.guosen.chartview.util.ViewPortHandler;

public class YAxisRenderer extends AxisRenderer {

	protected YAxis mYAxis;

    public YAxisRenderer(ViewPortHandler viewPortHandler, YAxis yAxis, Transformer trans) {
        super(viewPortHandler, trans);

        this.mYAxis = yAxis;

        mAxisLabelPaint.setColor(Color.BLACK);
        mAxisLabelPaint.setTextSize(Utils.convertDpToPixel(10f));
    }

    /**
     * Computes the axis values.
     * 
     * @param yMin - the minimum y-value in the data object for this axis
     * @param yMax - the maximum y-value in the data object for this axis
     */
    public void computeAxis(float yMin, float yMax) {

        // calculate the starting and entry point of the y-labels (depending on
        // zoom / contentrect bounds)
        if (mViewPortHandler.contentWidth() > 10 && !mViewPortHandler.isFullyZoomedOutY()) {

            PointD p1 = mTrans.getValuesByTouchPoint(mViewPortHandler.contentLeft(),
                    mViewPortHandler.contentTop()+10);
            PointD p2 = mTrans.getValuesByTouchPoint(mViewPortHandler.contentLeft(),
                    mViewPortHandler.contentBottom()-10);

            if (!mYAxis.isInverted()) {
                yMin = (float) p2.y;
                yMax = (float) p1.y;
            } else {

                yMin = (float) p1.y;
                yMax = (float) p2.y;
            }
        }

        computeAxisValues(yMin, yMax);
    }

    /**
     * Sets up the y-axis labels. Computes the desired number of labels between
     * the two given extremes. Unlike the papareXLabels() method, this method
     * needs to be called upon every refresh of the view.
     * 设定Y轴上的数据，在给定的最大和最小值之间计算给定数量的标签。不同于preparedXLabels()方法，这个方法
     * 每次更新视图时都要被调用。
     * @return
     */
    protected void computeAxisValues(float min, float max) {
        
        float yMin = min;
        float yMax = max;
        //需要显示的标签个数
        int labelCount = mYAxis.getLabelCount();
        //最大值与最小值之差的绝对值
        double range = Math.abs(yMax - yMin);

        if (labelCount == 0 || range <= 0) {
            mYAxis.mEntries = new float[] {};
            mYAxis.mEntryCount = 0;
            return;
        }
        //等分值
        double rawInterval = range / labelCount;
        
       /* double interval = Utils.roundToNextSignificant(rawInterval);
        
        double intervalMagnitude = Math.pow(10, (int) Math.log10(interval));
        int intervalSigDigit = (int) (interval / intervalMagnitude);
       
        if (intervalSigDigit > 5) {
            // Use one order of magnitude higher, to avoid intervals like 0.9 or
            // 90
            interval = Math.floor(10 * intervalMagnitude);
        }*/

        // if the labels should only show min and max
        if (mYAxis.isShowOnlyMinMaxEnabled()) {
            mYAxis.mEntryCount = 2;
            mYAxis.mEntries = new float[2];
            mYAxis.mEntries[0] = yMin;
            mYAxis.mEntries[1] = yMax;

        } else {
            double f;
            int i;
            mYAxis.mEntryCount = labelCount;
            if (mYAxis.mEntries.length < labelCount) {
                // Ensure stops contains at least numStops elements.
                mYAxis.mEntries = new float[labelCount];
            }

            for (f = yMin, i = 0; i < labelCount; f += rawInterval, ++i) {
                mYAxis.mEntries[i] = (float) f;
            }
        }

        if (rawInterval < 1) {
            mYAxis.mDecimals = (int) Math.ceil(-Math.log10(rawInterval));
        } else {
            mYAxis.mDecimals = 0;
        }
        
        
    }
        
    /**
     * Sets up the y-axis labels. Computes the desired number of labels between
     * the two given extremes. Unlike the papareXLabels() method, this method
     * needs to be called upon every refresh of the view.
     * 设定Y轴上的数据，在给定的最大和最小值之间计算给定数量的标签。不同于preparedXLabels()方法，这个方法
     * 每次更新视图时都要被调用。
     * @return
     
    protected void computeAxisValues(float min, float max) {
        
        float yMin = min;
        float yMax = max;
        //需要显示的标签个数
        int labelCount = mYAxis.getLabelCount();
        //最大值与最小值之差的绝对值
        double range = Math.abs(yMax - yMin);

        if (labelCount == 0 || range <= 0) {
            mYAxis.mEntries = new float[] {};
            mYAxis.mEntryCount = 0;
            return;
        }
        //每个等分区间的值
        double rawInterval = range / labelCount;
        
        double interval = Utils.roundToNextSignificant(rawInterval);
        
        double intervalMagnitude = Math.pow(10, (int) Math.log10(interval));
        int intervalSigDigit = (int) (interval / intervalMagnitude);
        if (intervalSigDigit > 5) {
            // Use one order of magnitude higher, to avoid intervals like 0.9 or
            // 90
            interval = Math.floor(10 * intervalMagnitude);
        }

        // if the labels should only show min and max
        if (mYAxis.isShowOnlyMinMaxEnabled()) {
            mYAxis.mEntryCount = 2;
            mYAxis.mEntries = new float[2];
            mYAxis.mEntries[0] = yMin;
            mYAxis.mEntries[1] = yMax;

        } else {

            double first = Math.ceil(yMin / interval) * interval;
            double last =  Utils.nextUp(Math.floor(yMax / interval) * interval);

            double f;
            int i;
            int n = 0;
            for (f = first; f <= last; f += interval) {
                ++n;
            }

            mYAxis.mEntryCount = n;

            if (mYAxis.mEntries.length < n) {
                // Ensure stops contains at least numStops elements.
                mYAxis.mEntries = new float[n];
            }

            for (f = first, i = 0; i < n; f += interval, ++i) {
                mYAxis.mEntries[i] = (float) f;
            }
        }

        if (interval < 1) {
            mYAxis.mDecimals = (int) Math.ceil(-Math.log10(interval));
        } else {
            mYAxis.mDecimals = 0;
        }
    }
     */

    /**
     * draws the y-axis labels to the screen
     */
    @Override
    public void renderAxisLabels(Canvas c) {

        if (!mYAxis.isEnabled() || !mYAxis.isDrawLabelsEnabled())
            return;
        float[] positions = new float[mYAxis.mEntryCount * 2];
        for (int i = 0; i < positions.length; i += 2) {
            // only fill y values, x values are not needed since the y-labels
            // are
            // static on the x-axis
            positions[i + 1] = mYAxis.mEntries[i / 2];
        }

        mTrans.pointValuesToPixel(positions);

        mAxisLabelPaint.setTypeface(mYAxis.getTypeface());
        mAxisLabelPaint.setTextSize(mYAxis.getTextSize());
        mAxisLabelPaint.setColor(mYAxis.getTextColor());

        float xoffset = mYAxis.getXOffset();
        float yoffset = Utils.calcTextHeight(mAxisLabelPaint, "A") / 2.5f + mYAxis.getYOffset();

        AxisDependency dependency = mYAxis.getAxisDependency();
        YAxisLabelPosition labelPosition = mYAxis.getLabelPosition();

        float xPos = 0f;

        if (dependency == AxisDependency.LEFT) {

            if (labelPosition == YAxisLabelPosition.OUTSIDE_CHART) {
                mAxisLabelPaint.setTextAlign(Align.RIGHT);
                xPos = mViewPortHandler.offsetLeft() - xoffset;
            } else {
                mAxisLabelPaint.setTextAlign(Align.LEFT);
                xPos = mViewPortHandler.offsetLeft() + xoffset;
            }

        } else {

            if (labelPosition == YAxisLabelPosition.OUTSIDE_CHART) {
                mAxisLabelPaint.setTextAlign(Align.LEFT);
                xPos = mViewPortHandler.contentRight() + xoffset;
            } else {
                mAxisLabelPaint.setTextAlign(Align.RIGHT);
                xPos = mViewPortHandler.contentRight() - xoffset;
            }
        }

        drawYLabels(c, xPos, positions, yoffset);
    }

    //绘制左Y轴与右Y轴
    @Override
    public void renderAxisLine(Canvas c) {
        if (!mYAxis.isEnabled() || !mYAxis.isDrawAxisLineEnabled())
            return;
        mAxisLinePaint.setColor(mYAxis.getAxisLineColor());
        mAxisLinePaint.setStrokeWidth(mYAxis.getAxisLineWidth());

        if (mYAxis.getAxisDependency() == AxisDependency.LEFT) {
            c.drawLine(mViewPortHandler.contentLeft(),
                    mViewPortHandler.contentTop(), mViewPortHandler.contentLeft(),
                    mViewPortHandler.contentBottom(), mAxisLinePaint);
            c.drawLine(mViewPortHandler.contentRight(),
                    mViewPortHandler.contentTop(), mViewPortHandler.contentRight(),
                    mViewPortHandler.contentBottom(), mAxisLinePaint);
        }
    }

    /**
     * draws the y-labels on the specified x-position
     * 在X轴指定位置绘制出Y坐标轴上的文字
     * @param fixedPosition
     * @param positions
     */
    protected void drawYLabels(Canvas c, float fixedPosition, float[] positions, float offset) {
    	float disSpacing = 0.0f;
    	float valueSpacing = 0.0f;
    	float top = 0.0f;
    	float secondTop = 0.0f;
    	float highest = mYAxis.getAxisMaxValue();
    	float smallest = mYAxis.getAxisMinValue();
    	 //需要显示的标签个数
        int labelCount = mYAxis.getLabelCount();
        //最大值与最小值之差的绝对值
        double range = Math.abs(highest - smallest);
        //等分值
        double rawInterval = range / labelCount;
        
    //	valueSpacing = (highest- smallest)/ mYAxis.getLabelCount();;
    //	System.out.print(highest);
        for (int i = 0; i < mYAxis.mEntryCount; i++) {
        	String text = mYAxis.getFormattedLabel(i);
        	if(i == 0 ) {
        		//String tempString = mYAxis.getFormattedLabel(i + 1);
        		disSpacing = positions[i*2+3] - positions[i*2+1];
        		if(disSpacing < 0) disSpacing = -disSpacing;
        	}else if( i == mYAxis.mEntryCount - 1){
        		top = (float) (Float.parseFloat(text) + rawInterval);
        		secondTop = positions[i * 2 + 1];
        		c.drawText(text, fixedPosition, secondTop + offset, mAxisLabelPaint);
			}else {
        		c.drawText(text, fixedPosition, positions[i * 2 + 1] + offset, mAxisLabelPaint);
			}
        }
        c.drawText(mYAxis.getValueFormatter().getFormattedValue(top), fixedPosition,secondTop - disSpacing + 2*offset, mAxisLabelPaint);  
    }

    @Override
    public void renderGridLines(Canvas c) {

        if (!mYAxis.isDrawGridLinesEnabled() || !mYAxis.isEnabled())
            return;

        // pre alloc
        float[] position = new float[2];

        mGridPaint.setColor(mYAxis.getGridColor());
        mGridPaint.setStrokeWidth(mYAxis.getGridLineWidth());
        mGridPaint.setPathEffect(mYAxis.getGridDashPathEffect());

        Path gridLinePath = new Path();

        // draw the horizontal grid
        //绘制平行于X坐标轴的网格虚线
        for (int i = 0; i < mYAxis.mEntryCount; i++) {
        //	if(i == 0) continue;
        	
            position[1] = mYAxis.mEntries[i];
            mTrans.pointValuesToPixel(position);

            gridLinePath.moveTo(mViewPortHandler.contentLeft()-10f, position[1]);
            gridLinePath.lineTo(mViewPortHandler.contentRight()+10f,
                    position[1]);

            // draw a path because lines don't support dashing on lower android versions
            c.drawPath(gridLinePath, mGridPaint);

            gridLinePath.reset();
        }
    }

    /**
     * Draws the LimitLines associated with this axis to the screen.
     * 
     * @param c
     */
    @Override
	public void renderLimitLines(Canvas c) {

        List<LimitLine> limitLines = mYAxis.getLimitLines();

        if (limitLines == null || limitLines.size() <= 0)
            return;

        float[] pts = new float[2];
        Path limitLinePath = new Path();

        for (int i = 0; i < limitLines.size(); i++) {

            LimitLine l = limitLines.get(i);
            
            mLimitLinePaint.setStyle(Paint.Style.STROKE);
            mLimitLinePaint.setColor(l.getLineColor());
            mLimitLinePaint.setStrokeWidth(l.getLineWidth());
            mLimitLinePaint.setPathEffect(l.getDashPathEffect());

            pts[1] = l.getLimit();

            mTrans.pointValuesToPixel(pts);

            limitLinePath.moveTo(mViewPortHandler.contentLeft(), pts[1]);
            limitLinePath.lineTo(mViewPortHandler.contentRight(), pts[1]);
            
            c.drawPath(limitLinePath, mLimitLinePaint);
            limitLinePath.reset();
            // c.drawLines(pts, mLimitLinePaint);

            String label = l.getLabel();

            // if drawing the limit-value label is enabled
            if (label != null && !label.equals("")) {

                float xOffset = Utils.convertDpToPixel(4f);
                float yOffset = l.getLineWidth() + Utils.calcTextHeight(mLimitLinePaint, label)
                        / 2f;

                mLimitLinePaint.setStyle(l.getTextStyle());
                mLimitLinePaint.setPathEffect(null);
                mLimitLinePaint.setColor(l.getTextColor());
                mLimitLinePaint.setStrokeWidth(0.5f);
                mLimitLinePaint.setTextSize(l.getTextSize());

                if (l.getLabelPosition() == LimitLabelPosition.POS_RIGHT) {

                    mLimitLinePaint.setTextAlign(Align.RIGHT);
                    c.drawText(label, mViewPortHandler.contentRight()
                            - xOffset,
                            pts[1] - yOffset, mLimitLinePaint);

                } else {
                    mLimitLinePaint.setTextAlign(Align.LEFT);
                    c.drawText(label, mViewPortHandler.offsetLeft()
                            + xOffset,
                            pts[1] - yOffset, mLimitLinePaint);
                }
            }
        }
    }
}
