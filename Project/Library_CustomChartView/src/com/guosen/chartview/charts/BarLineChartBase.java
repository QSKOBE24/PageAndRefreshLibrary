
package com.guosen.chartview.charts;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.guosen.chartview.component.XAxis;
import com.guosen.chartview.component.YAxis;
import com.guosen.chartview.component.Legend.LegendPosition;
import com.guosen.chartview.component.XAxis.XAxisPosition;
import com.guosen.chartview.component.YAxis.AxisDependency;
import com.guosen.chartview.data.Approximator;
import com.guosen.chartview.data.BarLineScatterCandleData;
import com.guosen.chartview.data.BarLineScatterCandleDataSet;
import com.guosen.chartview.data.DataSet;
import com.guosen.chartview.data.Entry;
import com.guosen.chartview.data.LineData;
import com.guosen.chartview.data.LineDataSet;
import com.guosen.chartview.interfaces.BarLineScatterCandleDataProvider;
import com.guosen.chartview.jobs.MoveViewJob;
import com.guosen.chartview.listener.BarLineChartTouchListener;
import com.guosen.chartview.listener.OnDrawListener;
import com.guosen.chartview.render.XAxisRenderer;
import com.guosen.chartview.render.YAxisRenderer;
import com.guosen.chartview.util.FillFormatter;
import com.guosen.chartview.util.Highlight;
import com.guosen.chartview.util.PointD;
import com.guosen.chartview.util.SelectionDetail;
import com.guosen.chartview.util.Transformer;
import com.guosen.chartview.util.Utils;

/**
 * Base-class of LineChart, BarChart, ScatterChart and CandleStickChart.
 * 
 * @author QSKOBE24
 */
@SuppressLint({ "RtlHardcoded", "DrawAllocation", "HandlerLeak" })
public abstract class BarLineChartBase<T extends BarLineScatterCandleData<? extends BarLineScatterCandleDataSet<? extends Entry>>>
        extends Chart<T> implements BarLineScatterCandleDataProvider {

    /** the maximum number of entried to which values will be drawn
     * */
    protected int mMaxVisibleCount = 100;

    /** flag that indicates if auto scaling on the y axis is enabled
     *  */
    private boolean mAutoScaleMinMaxEnabled = false;
    private Integer mAutoScaleLastLowestVisibleXIndex = null;
    private Integer mAutoScaleLastHighestVisibleXIndex = null;

    /**
     * flag that indicates if pinch-zoom is enabled. if true, both x and y axis
     * can be scaled with 2 fingers, if false, x and y axis can be scaled
     * separately
     */
    protected boolean mPinchZoomEnabled = false;

    /** flag that indicates if double tap zoom is enabled or not
     * */
    protected boolean mDoubleTapToZoomEnabled = false;

    /**
     * flag that indicates if highlighting per dragging over a fully zoomed out
     * chart is enabled
     */
    protected boolean mHighlightPerDragEnabled = false;

    /** if true, dragging is enabled for the chart
     * */
    private boolean mDragEnabled = false;
    /** if true, ScaleX is enabled for the chart
     * */
    private boolean mScaleXEnabled = false;
    /** if true, ScaleY is enabled for the chart
     * */
    private boolean mScaleYEnabled = false;

    /** if true, data filtering is enabled
     **/
    protected boolean mFilterData = false;

    /** paint object for the (by default) lightgrey background of the grid
     *  */
    protected Paint mGridBackgroundPaint;

    /** */
    protected Paint mBorderPaint;

    /** flag indicating if the grid background should be drawn or not
     *  */
    protected boolean mDrawGridBackground = false;

    /* */
    protected boolean mDrawBorders = false;

    /**
     * * the listener for user drawing on the chart
     * */
    protected OnDrawListener mDrawListener;

    /**
     * the object representing the labels on the y-axis, this object is prepared
     * in the pepareYLabels() method
     */
    protected YAxis mAxisLeft;
 //   protected YAxis mAxisRight;

    /** the object representing the labels on the x-axis
     * */
    protected XAxis mXAxis;
    /**
      */
    protected YAxisRenderer mAxisRendererLeft;
 //   protected YAxisRenderer mAxisRendererRight;

    protected Transformer mLeftAxisTransformer;
 //   protected Transformer mRightAxisTransformer;
    protected Transformer mHorizontalTransformer;
    /**
     */
    protected XAxisRenderer mXAxisRenderer;

    /**
     */
    protected XAxisRenderer mXAxisBottomRenderer;
   
    /**
     * 设置是否绘制默认的高亮显示自定义MarkerView
     */
    private boolean  NeedDrawMarkerView = true;
    
    private boolean  LinesFinishedDrawing   = false;
    
    private   Handler  drawHandler;
    
    private   static  final  int  HandlerDrawDefaultMarkerView = 1;
 
    
	public boolean isLinesFinishedDrawing() {
		return LinesFinishedDrawing;
	}

	public void setLinesFinishedDrawing(boolean linesFinishedDrawing) {
		LinesFinishedDrawing = linesFinishedDrawing;
	}

	public boolean isNeedDrawMarkerView() {
		return NeedDrawMarkerView;
	}

	public void setNeedDrawMarkerView(boolean NeedDrawMarkerView) {
		this.NeedDrawMarkerView = NeedDrawMarkerView;
	}

	public BarLineChartBase(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public BarLineChartBase(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BarLineChartBase(Context context) {
        super(context);
    }

    @Override
    protected void init() {
        super.init();

        mAxisLeft = new YAxis(AxisDependency.LEFT);
 //     mAxisRight = new YAxis(AxisDependency.RIGHT);
 //     mAxisRight.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        mXAxis = new XAxis();
        mLeftAxisTransformer = new Transformer(mViewPortHandler);
 //     mRightAxisTransformer = new Transformer(mViewPortHandler);
        mHorizontalTransformer = new Transformer(mViewPortHandler);
        mAxisRendererLeft = new YAxisRenderer(mViewPortHandler, mAxisLeft, mLeftAxisTransformer);
 //     mAxisRendererRight = new YAxisRenderer(mViewPortHandler, mAxisRight, mRightAxisTransformer);

        mXAxisRenderer = new XAxisRenderer(mViewPortHandler, mXAxis, mLeftAxisTransformer);

        mChartTouchListener = new BarLineChartTouchListener(this, mViewPortHandler.getMatrixTouch());

        mGridBackgroundPaint = new Paint();
        mGridBackgroundPaint.setStyle(Style.FILL);
        mGridBackgroundPaint.setColor(Color.TRANSPARENT);

        mBorderPaint = new Paint();
        mBorderPaint.setStyle(Style.STROKE);
        mBorderPaint.setColor(Color.BLACK);
        mBorderPaint.setStrokeWidth(Utils.convertDpToPixel(1f));
        
        drawHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case HandlerDrawDefaultMarkerView:
					 setLinesFinishedDrawing(true);
					 invalidate();
					break;
				default:
					break;
				}
			}
        	
        };
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (showDefaultData){
        	 calcModulus();
             mXAxisRenderer.calcXBounds(this, mXAxis.mAxisLabelModulus);
             mRenderer.calcXBounds(this, mXAxis.mAxisLabelModulus);
             drawGridBackground(canvas);
             //绘制X轴
             mXAxisRenderer.renderAxisLine(canvas);
             //绘制Y轴
             mAxisRendererLeft.renderAxisLine(canvas);
             //绘制右Y轴
             //mAxisRendererRight.renderAxisLine(canvas);
             //int clipRestoreCount = canvas.save();
             canvas.clipRect(mViewPortHandler.getContentRect());
             //绘制平行坐标轴的虚线的过程
             mXAxisRenderer.renderGridLines(canvas);
             mAxisRendererLeft.renderGridLines(canvas);
           //mAxisRendererRight.renderGridLines(canvas);
             canvas.save();
             return;
        }
        
        if(isLinesFinishedDrawing()){
        	//图表其它绘制完成之后，判断是否需要绘制自定义的年化利率
        	mRenderer.drawDefaultMarkerView(canvas, mMarkerView, this);
            canvas.save(); 
        }
        
        
        calcModulus();
        mXAxisRenderer.calcXBounds(this, mXAxis.mAxisLabelModulus);
        mRenderer.calcXBounds(this, mXAxis.mAxisLabelModulus);

        //execute all drawing commands
        //绘制背景,调用一次canvas.draw()函数，并将其最为当前屏幕最上方的canvas显示在屏幕上方
        drawGridBackground(canvas);

        if (mAxisLeft.isEnabled())
            mAxisRendererLeft.computeAxis(mAxisLeft.mAxisMinimum, mAxisLeft.mAxisMaximum);
     //   if (mAxisRight.isEnabled())
     //       mAxisRendererRight.computeAxis(mAxisRight.mAxisMinimum, mAxisRight.mAxisMaximum);
        //调用了三次canvas.draw()函数进行canvas绘制以后作为屏幕最上方的canvas显示
        //绘制X轴
        mXAxisRenderer.renderAxisLine(canvas);
        //绘制左Y轴
        mAxisRendererLeft.renderAxisLine(canvas);
        //绘制右Y轴
        //mAxisRendererRight.renderAxisLine(canvas);

        if (mAutoScaleMinMaxEnabled) {
            final int lowestVisibleXIndex = getLowestVisibleXIndex();
            final int highestVisibleXIndex = getHighestVisibleXIndex();

            if (mAutoScaleLastLowestVisibleXIndex == null ||
                    mAutoScaleLastLowestVisibleXIndex != lowestVisibleXIndex ||
                    mAutoScaleLastHighestVisibleXIndex == null ||
                    mAutoScaleLastHighestVisibleXIndex != highestVisibleXIndex) {
                calcMinMax();
                calculateOffsets();
                mAutoScaleLastLowestVisibleXIndex = lowestVisibleXIndex;
                mAutoScaleLastHighestVisibleXIndex = highestVisibleXIndex;
            }
        }


        //绘制坐标轴的过程
        mXAxisRenderer.renderGridLines(canvas);
        mAxisRendererLeft.renderGridLines(canvas);

        //绘制每条折线上两两点之间的过程
        mRenderer.drawData(canvas);
        // Removes clipping rectangle
        //绘制折线上圆圈的过程
        mRenderer.drawExtras(canvas);
        //坐标轴上的值的绘制
        mXAxisRenderer.renderAxisLabels(canvas);
        mAxisRendererLeft.renderAxisLabels(canvas);
      //  mAxisRendererRight.renderAxisLabels(canvas);
        //绘制折线上的值得过程
        mRenderer.drawValues(canvas);
        //绘制左下角每条折线的描述性文字和图标的过程
        mLegendRenderer.renderLegend(canvas);

        //真正的绘制每个数值点击之后显示的MarkerView的过程，而且还不是绘制十字架的过程
        //drawMarkers(canvas);
        //绘制右下角整个图标说明的过程
        drawDescription(canvas);
        
        canvas.save();
        
        if(!isLinesFinishedDrawing()){
        	Message message = new Message();
        	message.what = HandlerDrawDefaultMarkerView;
        	message.obj = canvas;
        	drawHandler.sendMessageDelayed(message, 1000);
        }
    }


    protected void prepareValuePxMatrix() {

        if (mLogEnabled)
            Log.i(LOG_TAG, "Preparing Value-Px Matrix, xmin: " + mXChartMin + ", xmax: "
                    + mXChartMax + ", xdelta: " + mDeltaX);
    //    mRightAxisTransformer.prepareMatrixValuePx(mXChartMin, mDeltaX, mAxisRight.mAxisRange,
    //            mAxisRight.mAxisMinimum);
        mLeftAxisTransformer.prepareMatrixValuePx(mXChartMin, mDeltaX, mAxisLeft.mAxisRange,
                mAxisLeft.mAxisMinimum);
    }

    protected void prepareOffsetMatrix() {
      //  mRightAxisTransformer.prepareMatrixOffset(mAxisRight.isInverted());
        mLeftAxisTransformer.prepareMatrixOffset(mAxisLeft.isInverted());
    }

    @Override
    public void notifyDataSetChanged() {
        if (mDataNotSet) {
            if (mLogEnabled)
                Log.i(LOG_TAG, "Preparing... DATA NOT SET.");
            return;
        } else {
            if (mLogEnabled)
                Log.i(LOG_TAG, "Preparing...");
        }

        if (mRenderer != null)
            mRenderer.initBuffers();

        calcMinMax();

         if (mAxisLeft.needsDefaultFormatter())
            mAxisLeft.setValueFormatter(mDefaultFormatter);
       
         mAxisRendererLeft.computeAxis(mAxisLeft.mAxisMinimum, mAxisLeft.mAxisMaximum);
         mXAxisRenderer.computeAxis(mData.getXValAverageLength(), mData.getXVals());

        if (mLegend != null)
            mLegendRenderer.computeLegend(mData);

        calculateOffsets();
    }

    @Override
    protected void calcMinMax() {
        if (mAutoScaleMinMaxEnabled)
            mData.calcMinMax(getLowestVisibleXIndex(), getHighestVisibleXIndex());
        //获得左Y轴和右Y轴上的最小值和最大值
        float minLeft = mData.getYMin(AxisDependency.LEFT);
        float maxLeft = mData.getYMax(AxisDependency.LEFT);
     //   float minRight = mData.getYMin(AxisDependency.RIGHT);
     //   float maxRight = mData.getYMax(AxisDependency.RIGHT);
        //通过判定Y轴是否设定了从0开始记数，来求出左Y轴和右Y轴的最大最小值差
        float leftRange = Math.abs(maxLeft - (mAxisLeft.isStartAtZeroEnabled() ? 0 : minLeft));
     //   float rightRange = Math.abs(maxRight - (mAxisRight.isStartAtZeroEnabled() ? 0 : minRight));

        // in case all values are equal
        //特殊情况，就是Y轴的所有数据值都相等
        if (leftRange == 0f) {
            maxLeft = maxLeft + 1f;
            if (!mAxisLeft.isStartAtZeroEnabled())
                minLeft = minLeft - 1f;
        }

       /* if (rightRange == 0f) {
            maxRight = maxRight + 1f;
            if (!mAxisRight.isStartAtZeroEnabled())
                minRight = minRight - 1f;
        }*/

        float topSpaceLeft = leftRange / 100f * mAxisLeft.getSpaceTop();
     //   float topSpaceRight = rightRange / 100f * mAxisRight.getSpaceTop();
        float bottomSpaceLeft = leftRange / 100f * mAxisLeft.getSpaceBottom();
     //   float bottomSpaceRight = rightRange / 100f * mAxisRight.getSpaceBottom();

        mXChartMax = mData.getXVals().size() - 1;
        mDeltaX = Math.abs(mXChartMax - mXChartMin);

        mAxisLeft.mAxisMaximum = !Float.isNaN(mAxisLeft.getAxisMaxValue()) ? mAxisLeft
                .getAxisMaxValue() : maxLeft + topSpaceLeft;
     //   mAxisRight.mAxisMaximum = !Float.isNaN(mAxisRight.getAxisMaxValue()) ? mAxisRight
     //           .getAxisMaxValue() : maxRight + topSpaceRight;
        mAxisLeft.mAxisMinimum = !Float.isNaN(mAxisLeft.getAxisMinValue()) ? mAxisLeft
                .getAxisMinValue() : minLeft - bottomSpaceLeft;
     //   mAxisRight.mAxisMinimum = !Float.isNaN(mAxisRight.getAxisMinValue()) ? mAxisRight
     //           .getAxisMinValue() : minRight - bottomSpaceRight;

        // consider starting at zero (0)
        if (mAxisLeft.isStartAtZeroEnabled())
            mAxisLeft.mAxisMinimum = 0f;

      //  if (mAxisRight.isStartAtZeroEnabled())
      //      mAxisRight.mAxisMinimum = 0f;

        mAxisLeft.mAxisRange = Math.abs(mAxisLeft.mAxisMaximum - mAxisLeft.mAxisMinimum);
     //   mAxisRight.mAxisRange = Math.abs(mAxisRight.mAxisMaximum - mAxisRight.mAxisMinimum);
    }

    @Override
    protected void calculateOffsets() {

        if (!mCustomViewPortEnabled) {

            float offsetLeft = 0f, offsetRight = 0f, offsetTop = 0f, offsetBottom = 0f;

            // setup offsets for legend
            if (mLegend != null && mLegend.isEnabled()) {
                if (mLegend.getPosition() == LegendPosition.RIGHT_OF_CHART
                        || mLegend.getPosition() == LegendPosition.RIGHT_OF_CHART_CENTER) {

                    offsetRight += Math.min(mLegend.mNeededWidth, mViewPortHandler.getChartWidth()
                            * mLegend.getMaxSizePercent())
                            + mLegend.getXOffset() * 2f;

                } else if (mLegend.getPosition() == LegendPosition.LEFT_OF_CHART
                        || mLegend.getPosition() == LegendPosition.LEFT_OF_CHART_CENTER) {

                    offsetLeft += Math.min(mLegend.mNeededWidth, mViewPortHandler.getChartWidth()
                            * mLegend.getMaxSizePercent())
                            + mLegend.getXOffset() * 2f;

                } else if (mLegend.getPosition() == LegendPosition.BELOW_CHART_LEFT
                        || mLegend.getPosition() == LegendPosition.BELOW_CHART_RIGHT
                        || mLegend.getPosition() == LegendPosition.BELOW_CHART_CENTER) {

                    float yOffset = mLegend.mTextHeightMax; 
                    offsetBottom += Math.min(mLegend.mNeededHeight + yOffset,
                            mViewPortHandler.getChartHeight() * mLegend.getMaxSizePercent());

                }
            }

            // offsets for y-labels
            if (mAxisLeft.needsOffset()) {
                offsetLeft += mAxisLeft.getRequiredWidthSpace(mAxisRendererLeft
                        .getPaintAxisLabels());
            }

          /*  if (mAxisRight.needsOffset()) {
                offsetRight += mAxisRight.getRequiredWidthSpace(mAxisRendererRight
                        .getPaintAxisLabels());
            }*/

            if (mXAxis.isEnabled() && mXAxis.isDrawLabelsEnabled()) {

                float xlabelheight = mXAxis.mLabelHeight * 2f;

                // offsets for x-labels
                if (mXAxis.getPosition() == XAxisPosition.BOTTOM) {

                    offsetBottom += xlabelheight;

                } else if (mXAxis.getPosition() == XAxisPosition.TOP) {

                    offsetTop += xlabelheight;

                } else if (mXAxis.getPosition() == XAxisPosition.BOTH_SIDED) {

                    offsetBottom += xlabelheight;
                    offsetTop += xlabelheight;
                }
            }

            offsetTop += getExtraTopOffset();
          //  offsetRight += getExtraRightOffset();
            offsetBottom += getExtraBottomOffset();
            offsetLeft += getExtraLeftOffset();

            float min = Utils.convertDpToPixel(10f);

            mViewPortHandler.restrainViewPort(Math.max(min, offsetLeft), Math.max(min, offsetTop),
                    Math.max(min, offsetRight), Math.max(min, offsetBottom));

            if (mLogEnabled) {
                Log.i(LOG_TAG, "offsetLeft: " + offsetLeft + ", offsetTop: " + offsetTop
                        + ", offsetRight: " + offsetRight + ", offsetBottom: " + offsetBottom);
                Log.i(LOG_TAG, "Content: " + mViewPortHandler.getContentRect().toString());
            }
        }

        prepareOffsetMatrix();
        prepareValuePxMatrix();
    }

    /**
     * calculates the modulus for x-labels and grid
     */
    protected void calcModulus() {
        if (mXAxis == null || !mXAxis.isEnabled())
            return;
        if (!mXAxis.isAxisModulusCustom()) {
            float[] values = new float[9];
            mViewPortHandler.getMatrixTouch().getValues(values);
            mXAxis.mAxisLabelModulus = (int) Math
                    .ceil((mData.getXValCount() * mXAxis.mLabelWidth)
                            / (mViewPortHandler.contentWidth() * values[Matrix.MSCALE_X]));

        }

        if (mLogEnabled)
            Log.i(LOG_TAG, "X-Axis modulus: " + mXAxis.mAxisLabelModulus + ", x-axis label width: "
                    + mXAxis.mLabelWidth + ", content width: " + mViewPortHandler.contentWidth());

        if (mXAxis.mAxisLabelModulus < 1)
            mXAxis.mAxisLabelModulus = 1;
    }

    @Override
    public float[] getMarkerPosition(Entry e, int dataSetIndex) {

        float xPos = e.getXIndex();
     
        // position of the marker depends on selected value index and value
        //选中的值的指数和值依据于选中的点的位置，在这里是图表上的点。
        float[] pts = new float[] {
                xPos, e.getVal() * mAnimator.getPhaseY()
        };
        //通过转换，将相对于坐标轴的位置转换为屏幕像素的位置
        getTransformer(mData.getDataSetByIndex(dataSetIndex).getAxisDependency())
                .pointValuesToPixel(pts);

        return pts;
    }

    /**
     * draws the grid background
     * 绘制图标显示的背景颜色
     */
    protected void drawGridBackground(Canvas c) {
        if (mDrawGridBackground) {
            // draw the grid background
            c.drawRect(mViewPortHandler.getContentRect(), mGridBackgroundPaint);
        }

        if (mDrawBorders) {
            c.drawRect(mViewPortHandler.getContentRect(), mBorderPaint);
           // c.save();
        }
    }

    
    /**
     * draws all MarkerViews on the highlighted positions
    */
    @Override
    protected void drawMarkers(Canvas canvas) {
        // if there is no marker view or drawing marker is disabled
        if (mMarkerView == null || !mDrawMarkerViews || !valuesToHighlight())
            return;

        for (int i = 0; i < mIndicesToHightlight.length; i++) {

            int xIndex = mIndicesToHightlight[i].getXIndex();
            int dataSetIndex = mIndicesToHightlight[i].getDataSetIndex();

            if (xIndex <= mDeltaX && xIndex <= mDeltaX * mAnimator.getPhaseX()) {

                Entry e = mData.getEntryForHighlight(mIndicesToHightlight[i]);

                // make sure entry not null
                if (e == null || e.getXIndex() != mIndicesToHightlight[i].getXIndex())
                    continue;

                float[] pos = getMarkerPosition(e, dataSetIndex);

                // check bounds
                if (!mViewPortHandler.isInBounds(pos[0], pos[1]))
                    continue;

                // callbacks to update the content
                mMarkerView.refreshContent(e, dataSetIndex);

                mMarkerView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                mMarkerView.layout(0, 0, mMarkerView.getMeasuredWidth(),
                        mMarkerView.getMeasuredHeight());

                if (pos[1] - mMarkerView.getHeight() <= 0) {
                    float y = mMarkerView.getHeight() - pos[1];
                    mMarkerView.draw(canvas, pos[0], pos[1] + y);
                } else {
                    mMarkerView.draw(canvas, pos[0], pos[1]);
                }
            }
        }
    }
    

    /**
     * Returns the Transformer class that contains all matrices and is
     * responsible for transforming values into pixels on the screen and
     * backwards.
     *
     * @return
     */
    public Transformer getTransformer(AxisDependency which) {
       // if (which == AxisDependency.LEFT)
           return mLeftAxisTransformer;
        //else
        //    return mRightAxisTransformer;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (mChartTouchListener == null || mDataNotSet)
            return false;
        // check if touch gestures are enabled
        if (!mTouchEnabled)
            return false;
        else
            return mChartTouchListener.onTouch(this, event);
    }

    @Override
    public void computeScroll() {
        if (mChartTouchListener instanceof BarLineChartTouchListener)
            ((BarLineChartTouchListener) mChartTouchListener).computeScroll();
    }

    /**
     * ################ ################ ################ ################
     */
    /**
     * CODE BELOW THIS RELATED TO SCALING AND GESTURES AND MODIFICATION OF THE
     * VIEWPORT
     */

    /**
     * Zooms in by 1.4f, into the charts center. center.
     */
    public void zoomIn() {
        Matrix save = mViewPortHandler.zoomIn(getWidth() / 2f, -(getHeight() / 2f));
        mViewPortHandler.refresh(save, this, true);
    }

    /**
     * Zooms out by 0.7f, from the charts center. center.
     */
    public void zoomOut() {
        Matrix save = mViewPortHandler.zoomOut(getWidth() / 2f, -(getHeight() / 2f));
        mViewPortHandler.refresh(save, this, true);
    }

    /**
     * Zooms in or out by the given scale factor. x and y are the coordinates
     * (in pixels) of the zoom center.
     * 
     * @param scaleX if < 1f --> zoom out, if > 1f --> zoom in
     * @param scaleY if < 1f --> zoom out, if > 1f --> zoom in
     * @param x
     * @param y
     */
    public void zoom(float scaleX, float scaleY, float x, float y) {
        Matrix save = mViewPortHandler.zoom(scaleX, scaleY, x, -y);
        mViewPortHandler.refresh(save, this, true);
    }

    /**
     * Resets all zooming and dragging and makes the chart fit exactly it's
     * bounds.
     */
    public void fitScreen() {
        Matrix save = mViewPortHandler.fitScreen();
        mViewPortHandler.refresh(save, this, true);
    }

    /**
     * Sets the minimum scale factor value to which can be zoomed out. 1f =
     * fitScreen
     * 
     * @param scaleX
     * @param scaleY
     */
    public void setScaleMinima(float scaleX, float scaleY) {
        mViewPortHandler.setMinimumScaleX(scaleX);
        mViewPortHandler.setMinimumScaleY(scaleY);
    }

    /**
     * Sets the size of the area (range on the x-axis) that should be maximum
     * visible at once (no further zooming out allowed). If this is e.g. set to
     * 10, no more than 10 values on the x-axis can be viewed at once without
     * scrolling.
     * 
     * @param maxXRange The maximum visible range of x-values.
     */
    public void setVisibleXRangeMaximum(float maxXRange) {
        float xScale = mDeltaX / (maxXRange);
        mViewPortHandler.setMinimumScaleX(xScale);
    }

    /**
     * Sets the size of the area (range on the x-axis) that should be minimum
     * visible at once (no further zooming in allowed). If this is e.g. set to
     * 10, no more than 10 values on the x-axis can be viewed at once without
     * scrolling.
     * 
     * @param minXRange The minimum visible range of x-values.
     */
    public void setVisibleXRangeMinimum(float minXRange) {
        float xScale = mDeltaX / (minXRange);
        mViewPortHandler.setMaximumScaleX(xScale);
    }

    /**
     * Limits the maximum and minimum value count that can be visible by
     * pinching and zooming. e.g. minRange=10, maxRange=100 no less than 10
     * values and no more that 100 values can be viewed at once without
     * scrolling
     * 
     * @param minXRange
     * @param maxXRange
     */
    public void setVisibleXRange(float minXRange, float maxXRange) {
        float maxScale = mDeltaX / minXRange;
        float minScale = mDeltaX / maxXRange;
        mViewPortHandler.setMinMaxScaleX(minScale, maxScale);
    }

    /**
     * Sets the size of the area (range on the y-axis) that should be maximum
     * visible at once.
     * 
     * @param maxYRange the maximum visible range on the y-axis
     * @param axis - the axis for which this limit should apply
     */
    public void setVisibleYRangeMaximum(float maxYRange, AxisDependency axis) {
        float yScale = getDeltaY(axis) / maxYRange;
        mViewPortHandler.setMinimumScaleY(yScale);
    }

    /**
     * Moves the left side of the current viewport to the specified x-index.
     * 
     * @param xIndex
     */
    public void moveViewToX(float xIndex) {

        Runnable job = new MoveViewJob(mViewPortHandler, xIndex, 0f,
                getTransformer(AxisDependency.LEFT), this);

        if (mViewPortHandler.hasChartDimens()) {
            post(job);
        } else {
            mJobs.add(job);
        }

    }

    /**
     * Centers the viewport to the specified y-value on the y-axis.
     * 
     * @param yValue
     * @param axis - which axis should be used as a reference for the y-axis
     */
    public void moveViewToY(float yValue, AxisDependency axis) {

        float valsInView = getDeltaY(axis) / mViewPortHandler.getScaleY();

        Runnable job = new MoveViewJob(mViewPortHandler, 0f, yValue + valsInView / 2f,
                getTransformer(axis), this);

        if (mViewPortHandler.hasChartDimens()) {
            post(job);
        } else {
            mJobs.add(job);
        }
    }

    /**
     * This will move the left side of the current viewport to the specified
     * x-index on the x-axis, and center the viewport to the specified y-value
     * on the y-axis.
     * 
     * @param xIndex
     * @param yValue
     * @param axis - which axis should be used as a reference for the y-axis
     */
    public void moveViewTo(float xIndex, float yValue, AxisDependency axis) {

        float valsInView = getDeltaY(axis) / mViewPortHandler.getScaleY();

        Runnable job = new MoveViewJob(mViewPortHandler, xIndex, yValue + valsInView / 2f,
                getTransformer(axis), this);

        if (mViewPortHandler.hasChartDimens()) {
            post(job);
        } else {
            mJobs.add(job);
        }
    }

    /**
     * This will move the center of the current viewport to the specified
     * x-index and y-value.
     *
     * @param xIndex
     * @param yValue
     * @param axis - which axis should be used as a reference for the y-axis
     */
    public void centerViewTo(int xIndex, float yValue, AxisDependency axis) {

        float valsInView = getDeltaY(axis) / mViewPortHandler.getScaleY();
        float xsInView = getXAxis().getValues().size() / mViewPortHandler.getScaleX();

        Runnable job = new MoveViewJob(mViewPortHandler,
                xIndex - xsInView / 2f, yValue + valsInView / 2f,
                getTransformer(axis), this);

        if (mViewPortHandler.hasChartDimens()) {
            post(job);
        } else {
            mJobs.add(job);
        }
    }

    /** flag that indicates if a custom viewport offset has been set */
    private boolean mCustomViewPortEnabled = false;

    /**
     * Sets custom offsets for the current ViewPort (the offsets on the sides of
     * the actual chart window). Setting this will prevent the chart from
     * automatically calculating it's offsets. Use resetViewPortOffsets() to
     * undo this. ONLY USE THIS WHEN YOU KNOW WHAT YOU ARE DOING, else use
     * setExtraOffsets(...).
     * 
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setViewPortOffsets(final float left, final float top,
            final float right, final float bottom) {

        mCustomViewPortEnabled = true;
        post(new Runnable() {

            @Override
            public void run() {

                mViewPortHandler.restrainViewPort(left, top, right, bottom);
                prepareOffsetMatrix();
                prepareValuePxMatrix();
            }
        });
    }

    /**
     * Resets all custom offsets set via setViewPortOffsets(...) method. Allows
     * the chart to again calculate all offsets automatically.
     */
    public void resetViewPortOffsets() {
        mCustomViewPortEnabled = false;
        calculateOffsets();
    }

    /**
     * ################ ################ ################ ################
     */
    /** CODE BELOW IS GETTERS AND SETTERS */

    /**
     * Returns the delta-y value (y-value range) of the specified axis.
     * 
     * @param axis
     * @return
     */
    public float getDeltaY(AxisDependency axis) {
       // if (axis == AxisDependency.LEFT)
            return mAxisLeft.mAxisRange;
       // else
       //     return mAxisRight.mAxisRange;
    }

    /**
     * Sets the OnDrawListener
     * 
     * @param drawListener
     */
    public void setOnDrawListener(OnDrawListener drawListener) {
        this.mDrawListener = drawListener;
    }

    /**
     * Gets the OnDrawListener. May be null.
     * 
     * @return
     */
    public OnDrawListener getDrawListener() {
        return mDrawListener;
    }

    /**
     * Returns the position (in pixels) the provided Entry has inside the chart
     * view or null, if the provided Entry is null.
     * 
     * @param e
     * @return
     */
    public PointF getPosition(Entry e, AxisDependency axis) {

        if (e == null)
            return null;

        float[] vals = new float[] {
                e.getXIndex(), e.getVal()
        };

        getTransformer(axis).pointValuesToPixel(vals);

        return new PointF(vals[0], vals[1]);
    }

    /**
     * sets the number of maximum visible drawn values on the chart only active
     * when setDrawValues() is enabled
     * 
     * @param count
     */
    public void setMaxVisibleValueCount(int count) {
        this.mMaxVisibleCount = count;
    }

    public int getMaxVisibleCount() {
        return mMaxVisibleCount;
    }

    /**
     * Set this to true to allow highlighting per dragging over the chart
     * surface when it is fully zoomed out. Default: true
     * 
     * @param enabled
     */
    public void setHighlightPerDragEnabled(boolean enabled) {
        mHighlightPerDragEnabled = enabled;
    }

    public boolean isHighlightPerDragEnabled() {
        return mHighlightPerDragEnabled;
    }

    /**
     * Sets the color for the background of the chart-drawing area (everything
     * behind the grid lines).
     * 
     * @param color
     */
    public void setGridBackgroundColor(int color) {
        mGridBackgroundPaint.setColor(color);
    }

    /**
     * Set this to true to enable dragging (moving the chart with the finger)
     * for the chart (this does not effect scaling).
     * 
     * @param enabled
     */
    public void setDragEnabled(boolean enabled) {
        this.mDragEnabled = enabled;
    }

    /**
     * Returns true if dragging is enabled for the chart, false if not.
     * 
     * @return
     */
    public boolean isDragEnabled() {
        return mDragEnabled;
    }

    /**
     * Set this to true to enable scaling (zooming in and out by gesture) for
     * the chart (this does not effect dragging) on both X- and Y-Axis.
     * 
     * @param enabled
     */
    public void setScaleEnabled(boolean enabled) {
        this.mScaleXEnabled = enabled;
        this.mScaleYEnabled = enabled;
    }

    public void setScaleXEnabled(boolean enabled) {
        mScaleXEnabled = enabled;
    }

    public void setScaleYEnabled(boolean enabled) {
        mScaleYEnabled = enabled;
    }

    public boolean isScaleXEnabled() {
        return mScaleXEnabled;
    }

    public boolean isScaleYEnabled() {
        return mScaleYEnabled;
    }

    /**
     * Set this to true to enable zooming in by double-tap on the chart.
     * Default: enabled
     * 
     * @param enabled
     */
    public void setDoubleTapToZoomEnabled(boolean enabled) {
        mDoubleTapToZoomEnabled = enabled;
    }

    /**
     * Returns true if zooming via double-tap is enabled false if not.
     * 
     * @return
     */
    public boolean isDoubleTapToZoomEnabled() {
        return mDoubleTapToZoomEnabled;
    }

    /**
     * set this to true to draw the grid background, false if not
     * 
     * @param enabled
     */
    public void setDrawGridBackground(boolean enabled) {
        mDrawGridBackground = enabled;
    }

    /**
     * Sets drawing the borders rectangle to true. If this is enabled, there is
     * no point drawing the axis-lines of x- and y-axis.
     * 
     * @param enabled
     */
    public void setDrawBorders(boolean enabled) {
        mDrawBorders = enabled;
    }

    /**
     * Sets the width of the border lines in dp.
     * 
     * @param width
     */
    public void setBorderWidth(float width) {
        mBorderPaint.setStrokeWidth(Utils.convertDpToPixel(width));
    }

    /**
     * Sets the color of the chart border lines.
     * 
     * @param color
     */
    public void setBorderColor(int color) {
        mBorderPaint.setColor(color);
    }

    /**
     * Returns the Highlight object (contains x-index and DataSet index) of the
     * selected value at the given touch point inside the Line-, Scatter-, or
     * CandleStick-Chart.
     * 
     * @param x
     * @param y
     * @return
     */
    public Highlight getHighlightByTouchPoint(float x, float y) {
        if (mDataNotSet || mData == null) {
            Log.e(LOG_TAG, "Can't select by touch. No data set.");
            return null;
        }
        // create an array of the touch-point
        float[] pts = new float[2];
        pts[0] = x;

        // take any transformer to determine the x-axis value
        mLeftAxisTransformer.pixelsToValue(pts);

        double xTouchVal = pts[0];
        double base = Math.floor(xTouchVal);

        double touchOffset = mDeltaX * 0.025;

        // touch out of chart
        if (xTouchVal < -touchOffset || xTouchVal > mDeltaX + touchOffset)
            return null;

        if (base < 0)
            base = 0;
        if (base >= mDeltaX)
            base = mDeltaX - 1;

        int xIndex = (int) base;

        // check if we are more than half of a x-value or not
        if (xTouchVal - base > 0.5) {
            xIndex = (int) base + 1;
        }

        List<SelectionDetail> valsAtIndex = getSelectionDetailsAtIndex(xIndex);

        float leftdist = Utils.getMinimumDistance(valsAtIndex, y, AxisDependency.LEFT);
        float rightdist = Utils.getMinimumDistance(valsAtIndex, y, AxisDependency.RIGHT);

        if (mData.getFirstRight() == null)
            rightdist = Float.MAX_VALUE;
        if (mData.getFirstLeft() == null)
            leftdist = Float.MAX_VALUE;

        AxisDependency axis = leftdist < rightdist ? AxisDependency.LEFT : AxisDependency.RIGHT;

        int dataSetIndex = Utils.getClosestDataSetIndex(valsAtIndex, y, axis);

        if (dataSetIndex == -1)
            return null;

        return new Highlight(xIndex, dataSetIndex);
    }

    /**
     * Returns an array of SelectionDetail objects for the given x-index. The SelectionDetail
     * objects give information about the value at the selected index and the
     * DataSet it belongs to. INFORMATION: This method does calculations at
     * runtime. Do not over-use in performance critical situations.
     *
     * @return
     */
    protected List<SelectionDetail> getSelectionDetailsAtIndex(int xIndex) {

        List<SelectionDetail> vals = new ArrayList<SelectionDetail>();

        float[] pts = new float[2];

        for (int i = 0; i < mData.getDataSetCount(); i++) {

            DataSet<?> dataSet = mData.getDataSetByIndex(i);

            // dont include datasets that cannot be highlighted
            if(!dataSet.isHighlightEnabled())
                continue;

            // extract all y-values from all DataSets at the given x-index
            final float yVal = dataSet.getYValForXIndex(xIndex);
            if (yVal == Float.NaN)
                continue;

            pts[1] = yVal;

            getTransformer(dataSet.getAxisDependency()).pointValuesToPixel(pts);

            if (!Float.isNaN(pts[1])) {
                vals.add(new SelectionDetail(pts[1], i, dataSet));
            }
        }

        return vals;
    }

    /**
     * Returns the x and y values in the chart at the given touch point
     * (encapsulated in a PointD). This method transforms pixel coordinates to
     * coordinates / values in the chart. This is the opposite method to
     * getPixelsForValues(...).
     * 
     * @param x
     * @param y
     * @return
     */
    public PointD getValuesByTouchPoint(float x, float y, AxisDependency axis) {

        // create an array of the touch-point
        float[] pts = new float[2];
        pts[0] = x;
        pts[1] = y;

        getTransformer(axis).pixelsToValue(pts);

        double xTouchVal = pts[0];
        double yTouchVal = pts[1];

        return new PointD(xTouchVal, yTouchVal);
    }

    /**
     * Transforms the given chart values into pixels. This is the opposite
     * method to getValuesByTouchPoint(...).
     * 
     * @param x
     * @param y
     * @return
     */
    public PointD getPixelsForValues(float x, float y, AxisDependency axis) {

        float[] pts = new float[] {
                x, y
        };

        getTransformer(axis).pointValuesToPixel(pts);

        return new PointD(pts[0], pts[1]);
    }

    /**
     * returns the y-value at the given touch position (must not necessarily be
     * a value contained in one of the datasets)
     * 
     * @param x
     * @param y
     * @return
     */
    public float getYValueByTouchPoint(float x, float y, AxisDependency axis) {
        return (float) getValuesByTouchPoint(x, y, axis).y;
    }

    /**
     * returns the Entry object displayed at the touched position of the chart
     * 
     * @param x
     * @param y
     * @return
     */
    public Entry getEntryByTouchPoint(float x, float y) {
        Highlight h = getHighlightByTouchPoint(x, y);
        if (h != null) {
            return mData.getEntryForHighlight(h);
        }
        return null;
    }

    /**
     * returns the DataSet object displayed at the touched position of the chart
     * 
     * @param x
     * @param y
     * @return
     */
    public BarLineScatterCandleDataSet<? extends Entry> getDataSetByTouchPoint(float x, float y) {
        Highlight h = getHighlightByTouchPoint(x, y);
        if (h != null) {
            return mData.getDataSetByIndex(h.getDataSetIndex());
        }
        return null;
    }

    /**
     * Returns the lowest x-index (value on the x-axis) that is still visible on
     * the chart.
     * 
     * @return
     */
    @Override
    public int getLowestVisibleXIndex() {
        float[] pts = new float[] {
                mViewPortHandler.contentLeft(), mViewPortHandler.contentBottom()
        };
        getTransformer(AxisDependency.LEFT).pixelsToValue(pts);
        return (pts[0] <= 0) ? 0 : (int) (pts[0] + 1);
    }

    /**
     * Returns the highest x-index (value on the x-axis) that is still visible
     * on the chart.
     * 
     * @return
     */
    @Override
    public int getHighestVisibleXIndex() {
        float[] pts = new float[] {
                mViewPortHandler.contentRight(), mViewPortHandler.contentBottom()
        };
        getTransformer(AxisDependency.LEFT).pixelsToValue(pts);
        if(mData == null) 
        	return 6;
        return (pts[0] >= mData.getXValCount()) ? mData.getXValCount() - 1 : (int) pts[0];
    }

    /**
     * returns the current x-scale factor
     */
    public float getScaleX() {
        if (mViewPortHandler == null)
            return 1f;
        else
            return mViewPortHandler.getScaleX();
    }

    /**
     * returns the current y-scale factor
     */
    public float getScaleY() {
        if (mViewPortHandler == null)
            return 1f;
        else
            return mViewPortHandler.getScaleY();
    }

    /**
     * if the chart is fully zoomed out, return true
     * 
     * @return
     */
    public boolean isFullyZoomedOut() {
        return mViewPortHandler.isFullyZoomedOut();
    }

    /**
     * Returns the left y-axis object. In the horizontal bar-chart, this is the
     * top axis.
     * 
     * @return
     */
    public YAxis getAxisLeft() {
        return mAxisLeft;
    }

    /**
     * Returns the right y-axis object. In the horizontal bar-chart, this is the
     * bottom axis.
     * 
     * @return
     */
   /* public YAxis getAxisRight() {
        return mAxisRight;
    }*/

    /**
     * Returns the y-axis object to the corresponding AxisDependency. In the
     * horizontal bar-chart, LEFT == top, RIGHT == BOTTOM
     * 
     * @param axis
     * @return
     */
    public YAxis getAxis(AxisDependency axis) {
      //  if (axis == AxisDependency.LEFT)
            return mAxisLeft;
       // else
       //     return mAxisRight;
    }

    @Override
    public boolean isInverted(AxisDependency axis) {
        return getAxis(axis).isInverted();
    }

    /**
     * Returns the object representing all x-labels, this method can be used to
     * acquire the XAxis object and modify it (e.g. change the position of the
     * labels)
     * 
     * @return
     */
    public XAxis getXAxis() {
        return mXAxis;
    }

    /**
     * Enables data filtering for the chart data, filtering will use the user
     * customized Approximator handed over to this method.
     * 
     * @param a
     */
    public void enableFiltering(Approximator a) {
        mFilterData = true;
        // mApproximator = a;
    }

    /**
     * Disables data filtering for the chart.
     */
    public void disableFiltering() {
        mFilterData = false;
    }

    /**
     * returns true if data filtering is enabled, false if not
     * 
     * @return
     */
    public boolean isFilteringEnabled() {
        return mFilterData;
    }

    /**
     * if set to true, both x and y axis can be scaled with 2 fingers, if false,
     * x and y axis can be scaled separately. default: false
     * 
     * @param enabled
     */
    public void setPinchZoom(boolean enabled) {
        mPinchZoomEnabled = enabled;
    }

    /**
     * returns true if pinch-zoom is enabled, false if not
     * 
     * @return
     */
    public boolean isPinchZoomEnabled() {
        return mPinchZoomEnabled;
    }

    /**
     * Set an offset in dp that allows the user to drag the chart over it's
     * bounds on the x-axis.
     * 
     * @param offset
     */
    public void setDragOffsetX(float offset) {
        mViewPortHandler.setDragOffsetX(offset);
    }

    /**
     * Set an offset in dp that allows the user to drag the chart over it's
     * bounds on the y-axis.
     * 
     * @param offset
     */
    public void setDragOffsetY(float offset) {
        mViewPortHandler.setDragOffsetY(offset);
    }

    /**
     * Returns true if both drag offsets (x and y) are zero or smaller.
     * 
     * @return
     */
    public boolean hasNoDragOffset() {
        return mViewPortHandler.hasNoDragOffset();
    }

    public XAxisRenderer getRendererXAxis() {
        return mXAxisRenderer;
    }

    /**
     * Sets a custom XAxisRenderer and overrides the existing (default) one.
     * @param xAxisRenderer
     */
    public void setXAxisRenderer(XAxisRenderer xAxisRenderer) {
        mXAxisRenderer = xAxisRenderer;
    }

    public YAxisRenderer getRendererLeftYAxis() {
        return mAxisRendererLeft;
    }

    /**
     * Sets a custom axis renderer for the left axis and overwrites the existing one.
     * @param rendererLeftYAxis
     */
    public void setRendererLeftYAxis(YAxisRenderer rendererLeftYAxis) {
        mAxisRendererLeft = rendererLeftYAxis;
    }

   /* public YAxisRenderer getRendererRightYAxis() {
        return mAxisRendererRight;
    }

    *//**
     * Sets a custom axis renderer for the right acis and overwrites the existing one.
     * @param rendererRightYAxis
     *//*
    public void setRendererRightYAxis(YAxisRenderer rendererRightYAxis) {
        mAxisRendererRight = rendererRightYAxis;
    }*/
    public float getYChartMax() {
        return mAxisLeft.mAxisMaximum;
    }

    public float getYChartMin() {
        return mAxisLeft.mAxisMinimum;
    }

    
    
  /*  public float getYChartMax() {
        return Math.max(mAxisLeft.mAxisMaximum, mAxisRight.mAxisMaximum);
    }

    public float getYChartMin() {
        return Math.min(mAxisLeft.mAxisMinimum, mAxisRight.mAxisMinimum);
    }
*/
    /**
     * Returns true if either the left or the right or both axes are inverted.
     * 
     * @return
     */
    public boolean isAnyAxisInverted() {
        if (mAxisLeft.isInverted())
            return true;
    //    if (mAxisRight.isInverted())
    //       return true;
        return false;
    }

    /**
     * Flag that indicates if auto scaling on the y axis is enabled. This is
     * especially interesting for charts displaying financial data.
     * 
     * @param enabled the y axis automatically adjusts to the min and max y
     *            values of the current x axis range whenever the viewport
     *            changes
     */
    public void setAutoScaleMinMaxEnabled(boolean enabled) {
        mAutoScaleMinMaxEnabled = enabled;
    }

    /**
     * @default false
     * @return true if auto scaling on the y axis is enabled.
     */
    public boolean isAutoScaleMinMaxEnabled() {
        return mAutoScaleMinMaxEnabled;
    }

    @Override
    public void setPaint(Paint p, int which) {
        super.setPaint(p, which);

        switch (which) {
            case PAINT_GRID_BACKGROUND:
                mGridBackgroundPaint = p;
                break;
        }
    }

    @Override
    public Paint getPaint(int which) {
        Paint p = super.getPaint(which);
        if (p != null)
            return p;

        switch (which) {
            case PAINT_GRID_BACKGROUND:
                return mGridBackgroundPaint;
        }

        return null;
    }

    /**
     * Default formatter that calculates the position of the filled line.
     * @author Philipp Jahoda
     */
    protected class DefaultFillFormatter implements FillFormatter {

        @Override
        public float getFillLinePosition(LineDataSet dataSet, LineData data,
                float chartMaxY, float chartMinY) {

            float fillMin = 0f;

            if (dataSet.getYMax() > 0 && dataSet.getYMin() < 0) {
                fillMin = 0f;
            } else {
                   //数据在Y轴是否从0开始记数
                if (!getAxis(dataSet.getAxisDependency()).isStartAtZeroEnabled()) {
                	//Y轴记数值不从0开始，那么填充线就从计算出的最小值开始进行填充
                    float max, min;

                    if (data.getYMax() > 0)
                        max = 0f;
                    else
                        max = chartMaxY;
                    if (data.getYMin() < 0)
                        min = 0f;
                    else
                        min = chartMinY;

                    fillMin = dataSet.getYMin() >= 0 ? min : max;
                } else {
                	//Y轴记数值从0开始，那么填充线就从0开始进行填充
                    fillMin = 0f;
                }

            }

            return fillMin;
        }
    }
   
   

    
}
