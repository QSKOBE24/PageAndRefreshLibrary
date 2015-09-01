
package com.guosen.chartview.render;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View.MeasureSpec;

import java.util.List;

import com.guosen.chartview.animation.ChartAnimator;
import com.guosen.chartview.buffer.CircleBuffer;
import com.guosen.chartview.buffer.LineBuffer;
import com.guosen.chartview.charts.BarLineChartBase;
import com.guosen.chartview.component.MarkerView;
import com.guosen.chartview.data.Entry;
import com.guosen.chartview.data.LineData;
import com.guosen.chartview.data.LineDataSet;
import com.guosen.chartview.interfaces.LineDataProvider;
import com.guosen.chartview.util.Highlight;
import com.guosen.chartview.util.Transformer;
import com.guosen.chartview.util.ViewPortHandler;

/**
 * 折线图中具有用来绘制整个图表的每个部分的实现类，只不过这些实现过程用到的Canvas都是自己新建的
 * 并不是最终显示在屏幕上的画布，因此需要在BarLineChartBase这个组件类具体的onDraw()函数被调用时
 * 传递系统最终显示的Canvas给这些具体函数，实现将最后画出来的图显示到屏幕上，不然画出来的图是黑色的，无法显示。
 * 
 * */
public class LineChartRenderer extends DataRenderer {
	

    protected LineDataProvider mChart;

    /** paint for the inner circle of the value indicators
     * 用来画每个值的内部圆圈的画笔
     **/
    protected Paint mCirclePaintInner;

    /**
     * Bitmap object used for drawing the paths (otherwise they are too long if
     * rendered directly on the canvas)
     * 用来画路径的画笔
     */
    protected Bitmap mDrawBitmap;

    /**
     * on this canvas, the paths are rendered, it is initialized with the
     * pathBitmap
     * 路径渲染的画布。
     */
    protected Canvas mBitmapCanvas;

    protected Path cubicPath = new Path();
    protected Path cubicFillPath = new Path();

    protected LineBuffer[] mLineBuffers;

    protected CircleBuffer[] mCircleBuffers;
    
    protected boolean startDrawDefaultMarkerView = false;
    

    public LineChartRenderer(LineDataProvider chart, ChartAnimator animator,
            ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
        mChart = chart;

        mCirclePaintInner = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaintInner.setStyle(Paint.Style.FILL);
        mCirclePaintInner.setColor(Color.WHITE);
        
    }

	@Override
    public void initBuffers() {

        LineData lineData = mChart.getLineData();
        mLineBuffers = new LineBuffer[lineData.getDataSetCount()];
        mCircleBuffers = new CircleBuffer[lineData.getDataSetCount()];

        for (int i = 0; i < mLineBuffers.length; i++) {
            LineDataSet set = lineData.getDataSetByIndex(i);
            mLineBuffers[i] = new LineBuffer(set.getEntryCount() * 4 - 4);
            mCircleBuffers[i] = new CircleBuffer(set.getEntryCount() * 2);
        }
    }
    
	/**
     * 用来具体画图表上的数据值
     */
    @Override
    public void drawData(Canvas c) {

        int width = (int) mViewPortHandler.getChartWidth();
        int height = (int) mViewPortHandler.getChartHeight();

        if (mDrawBitmap == null
                || (mDrawBitmap.getWidth() != width)
                || (mDrawBitmap.getHeight() != height)) {

            if (width > 0 && height > 0) {
                //新建一个图标大小的bitmap，用来填充画布
                mDrawBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
                mBitmapCanvas = new Canvas(mDrawBitmap);
            } else
                return;
        }

        mDrawBitmap.eraseColor(Color.TRANSPARENT);

        LineData lineData = mChart.getLineData();

        for (LineDataSet set : lineData.getDataSets()) {
            if (set.isVisible())
                drawDataSet(c, set);
        }
        c.drawBitmap(mDrawBitmap, 0, 0, mRenderPaint);
                
    }
    /**
     *画图表上的折线上两两点之间的线段的具体绘制过程
     */
    protected void drawDataSet(Canvas c, LineDataSet dataSet) {
        //返回Y轴数组值
        List<Entry> entries = dataSet.getYVals();

        if (entries.size() < 1)
            return;
        //设置整个图表的画笔大小
        mRenderPaint.setStrokeWidth(dataSet.getLineWidth());
        mRenderPaint.setPathEffect(dataSet.getDashPathEffect());

        // if drawing cubic lines is enabled
        //画平滑的曲线
        if (dataSet.isDrawCubicEnabled()) {
            drawCubic(c, dataSet, entries);
            // draw normal (straight) lines
        } else {
        	//画正常的折线
            drawLinear(c, dataSet, entries);
        }
        mRenderPaint.setPathEffect(null);
    }

    /**
     * Draws a cubic line.
     * 
     * @param c
     * @param dataSet
     * @param entries
     */
    protected void drawCubic(Canvas c, LineDataSet dataSet, List<Entry> entries) {

        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

        Entry entryFrom = dataSet.getEntryForXIndex(mMinX);
        Entry entryTo = dataSet.getEntryForXIndex(mMaxX);

        int minx = Math.max(dataSet.getEntryPosition(entryFrom), 0);
        int maxx = Math.min(dataSet.getEntryPosition(entryTo) + 1, entries.size());

        float phaseX = mAnimator.getPhaseX();
        float phaseY = mAnimator.getPhaseY();

        float intensity = dataSet.getCubicIntensity();

        cubicPath.reset();

        int size = (int) Math.ceil((maxx - minx) * phaseX + minx);

        if (size - minx >= 2) {

            float prevDx = 0f;
            float prevDy = 0f;
            float curDx = 0f;
            float curDy = 0f;

            Entry prevPrev = entries.get(minx);
            Entry prev = entries.get(minx);
            Entry cur = entries.get(minx);
            Entry next = entries.get(minx + 1);

            // let the spline start
            cubicPath.moveTo(cur.getXIndex(), cur.getVal() * phaseY);

            prevDx = (cur.getXIndex() - prev.getXIndex()) * intensity;
            prevDy = (cur.getVal() - prev.getVal()) * intensity;

            curDx = (next.getXIndex() - cur.getXIndex()) * intensity;
            curDy = (next.getVal() - cur.getVal()) * intensity;

            // the first cubic
            cubicPath.cubicTo(prev.getXIndex() + prevDx, (prev.getVal() + prevDy) * phaseY,
                    cur.getXIndex() - curDx,
                    (cur.getVal() - curDy) * phaseY, cur.getXIndex(), cur.getVal() * phaseY);

            for (int j = minx + 1, count = Math.min(size, entries.size() - 1); j < count; j++) {

                prevPrev = entries.get(j == 1 ? 0 : j - 2);
                prev = entries.get(j - 1);
                cur = entries.get(j);
                next = entries.get(j + 1);

                prevDx = (cur.getXIndex() - prevPrev.getXIndex()) * intensity;
                prevDy = (cur.getVal() - prevPrev.getVal()) * intensity;
                curDx = (next.getXIndex() - prev.getXIndex()) * intensity;
                curDy = (next.getVal() - prev.getVal()) * intensity;

                cubicPath.cubicTo(prev.getXIndex() + prevDx, (prev.getVal() + prevDy) * phaseY,
                        cur.getXIndex() - curDx,
                        (cur.getVal() - curDy) * phaseY, cur.getXIndex(), cur.getVal() * phaseY);
            }

            if (size > entries.size() - 1) {

                prevPrev = entries.get((entries.size() >= 3) ? entries.size() - 3
                        : entries.size() - 2);
                prev = entries.get(entries.size() - 2);
                cur = entries.get(entries.size() - 1);
                next = cur;

                prevDx = (cur.getXIndex() - prevPrev.getXIndex()) * intensity;
                prevDy = (cur.getVal() - prevPrev.getVal()) * intensity;
                curDx = (next.getXIndex() - prev.getXIndex()) * intensity;
                curDy = (next.getVal() - prev.getVal()) * intensity;

                // the last cubic
                cubicPath.cubicTo(prev.getXIndex() + prevDx, (prev.getVal() + prevDy) * phaseY,
                        cur.getXIndex() - curDx,
                        (cur.getVal() - curDy) * phaseY, cur.getXIndex(), cur.getVal() * phaseY);
            }
        }

        // if filled is enabled, close the path
        if (dataSet.isDrawFilledEnabled()) {

            cubicFillPath.reset();
            cubicFillPath.addPath(cubicPath);
            // create a new path, this is bad for performance
            drawCubicFill(dataSet, cubicFillPath, trans,
                    entryFrom.getXIndex(), entryFrom.getXIndex() + size);
        }

        mRenderPaint.setColor(dataSet.getColor());

        mRenderPaint.setStyle(Paint.Style.STROKE);

        trans.pathValueToPixel(cubicPath);

        mBitmapCanvas.drawPath(cubicPath, mRenderPaint);

        mRenderPaint.setPathEffect(null);
    }

    protected void drawCubicFill(LineDataSet dataSet, Path spline, Transformer trans,
            int from, int to) {

        float fillMin = mChart.getFillFormatter()
                .getFillLinePosition(dataSet, mChart.getLineData(), mChart.getYChartMax(),
                        mChart.getYChartMin());

        spline.lineTo(to - 1, fillMin);
        spline.lineTo(from, fillMin);
        spline.close();

        mRenderPaint.setStyle(Paint.Style.FILL);

        mRenderPaint.setColor(dataSet.getFillColor());
        // filled is drawn with less alpha
        mRenderPaint.setAlpha(dataSet.getFillAlpha());

        trans.pathValueToPixel(spline);
        mBitmapCanvas.drawPath(spline, mRenderPaint);

        mRenderPaint.setAlpha(255);
    }

    /**
     * Draws a normal line.
     * 
     * @param c
     * @param dataSet
     * @param entries
     */
    protected void drawLinear(Canvas c, LineDataSet dataSet, List<Entry> entries) {
    	// float []savedPoint = new float[2];
        //获取指定数据集在X轴上的位置
        int dataSetIndex = mChart.getLineData().getIndexOfDataSet(dataSet);
        //转换样式
        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());
        //X轴的相
        float phaseX = mAnimator.getPhaseX();
        //Y轴的相
        float phaseY = mAnimator.getPhaseY();

        mRenderPaint.setStyle(Paint.Style.STROKE);

        Canvas canvas = null;

        // if the data-set is dashed, draw on bitmap-canvas
        if (dataSet.isDashedLineEnabled()) {
            canvas = mBitmapCanvas;
        } else {
            canvas = c;
        }
        //开始绘制的Entry
        Entry entryFrom = dataSet.getEntryForXIndex(mMinX);
        //结束绘制的Entry
        Entry entryTo = dataSet.getEntryForXIndex(mMaxX);

        int minx = Math.max(dataSet.getEntryPosition(entryFrom), 0);
        int maxx = Math.min(dataSet.getEntryPosition(entryTo) + 1, entries.size());

        int range = (maxx - minx) * 4 - 4;

        LineBuffer buffer = mLineBuffers[dataSetIndex];
        buffer.setPhases(phaseX, phaseY);
        buffer.limitFrom(minx);
        buffer.limitTo(maxx);
        buffer.feed(entries);

        trans.pointValuesToPixel(buffer.buffer);

        // more than 1 color
        if (dataSet.getColors().size() > 1) {

            for (int j = 0; j < range; j += 4) {

                if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j]))
                    break;

                // make sure the lines don't do shitty things outside
                // bounds
                if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2])
                        || (!mViewPortHandler.isInBoundsTop(buffer.buffer[j + 1]) && !mViewPortHandler
                                .isInBoundsBottom(buffer.buffer[j + 3]))
                        || (!mViewPortHandler.isInBoundsTop(buffer.buffer[j + 1]) && !mViewPortHandler
                                .isInBoundsBottom(buffer.buffer[j + 3])))
                    continue;

                // get the color that is set for this line-segment
                mRenderPaint.setColor(dataSet.getColor(j / 4 + minx));
                canvas.drawLine(buffer.buffer[j], buffer.buffer[j + 1],
                        buffer.buffer[j + 2], buffer.buffer[j + 3], mRenderPaint);
            }

        } else { // only one color per dataset
            dataSet.setColor(Color.rgb(215, 65,49));
            mRenderPaint.setColor(dataSet.getColor());
            canvas.drawLines(buffer.buffer, 0, range,
                    mRenderPaint);
        }
        mRenderPaint.setPathEffect(null);

        // if drawing filled is enabled
        if (dataSet.isDrawFilledEnabled() && entries.size() > 0) {
            drawLinearFill(c, dataSet, entries, minx, maxx, trans);
        }
        
    }
    

    protected void drawLinearFill(Canvas c, LineDataSet dataSet, List<Entry> entries, int minx,
            int maxx,
            Transformer trans) {

        mRenderPaint.setStyle(Paint.Style.FILL);

        mRenderPaint.setColor(dataSet.getFillColor());
        // filled is drawn with less alpha
        mRenderPaint.setAlpha(dataSet.getFillAlpha());

        Path filled = generateFilledPath(
                entries,
                mChart.getFillFormatter().getFillLinePosition(dataSet, mChart.getLineData(),
                        mChart.getYChartMax(), mChart.getYChartMin()), minx, maxx);

        trans.pathValueToPixel(filled);

        c.drawPath(filled, mRenderPaint);

        // restore alpha
        mRenderPaint.setAlpha(255);
    }

    /**
     * Generates the path that is used for filled drawing.
     * 
     * @param entries
     * @return
     */
    private Path generateFilledPath(List<Entry> entries, float fillMin, int from, int to) {

        float phaseX = mAnimator.getPhaseX();
        float phaseY = mAnimator.getPhaseY();

        Path filled = new Path();
        filled.moveTo(entries.get(from).getXIndex(), fillMin);
        filled.lineTo(entries.get(from).getXIndex(), entries.get(from).getVal() * phaseY);

        // create a new path
        for (int x = from + 1, count = (int) Math.ceil((to - from) * phaseX + from); x < count; x++) {

            Entry e = entries.get(x);
            filled.lineTo(e.getXIndex(), e.getVal() * phaseY);
        }

        // close up
        filled.lineTo(
                entries.get(
                        Math.max(
                                Math.min((int) Math.ceil((to - from) * phaseX + from) - 1,
                                        entries.size() - 1), 0)).getXIndex(), fillMin);

        filled.close();

        return filled;
    }

    //绘制折线上的值得过程
    @Override
    public void drawValues(Canvas c) {

        if (mChart.getLineData().getYValCount() < mChart.getMaxVisibleCount()
                * mViewPortHandler.getScaleX()) {

            List<LineDataSet> dataSets = mChart.getLineData().getDataSets();

            for (int i = 0; i < dataSets.size(); i++) {

                LineDataSet dataSet = dataSets.get(i);

                if (!dataSet.isDrawValuesEnabled())
                    continue;

                // apply the text-styling defined by the DataSet
                applyValueTextStyle(dataSet);

                Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

                // make sure the values do not interfear with the circles
                int valOffset = (int) (dataSet.getCircleSize() * 1.75f);

                if (!dataSet.isDrawCirclesEnabled())
                    valOffset = valOffset / 2;

                List<Entry> entries = dataSet.getYVals();

                Entry entryFrom = dataSet.getEntryForXIndex(mMinX);
                Entry entryTo = dataSet.getEntryForXIndex(mMaxX);

                int minx = Math.max(dataSet.getEntryPosition(entryFrom), 0);
                int maxx = Math.min(dataSet.getEntryPosition(entryTo) + 1, entries.size());

                float[] positions = trans.generateTransformedValuesLine(
                        entries, mAnimator.getPhaseX(), mAnimator.getPhaseY(), minx, maxx);

                for (int j = 0; j < positions.length; j += 2) {

                    float x = positions[j];
                    float y = positions[j + 1];

                    if (!mViewPortHandler.isInBoundsRight(x))
                        break;

                    if (!mViewPortHandler.isInBoundsLeft(x) || !mViewPortHandler.isInBoundsY(y))
                        continue;

                    float val = entries.get(j / 2 + minx).getVal();

                    c.drawText(dataSet.getValueFormatter().getFormattedValue(val), x,
                            y - valOffset,
                            mValuePaint);
                }
            }
        }
    }


    //绘制折线上圆圈的过程
    @Override
    public void drawExtras(Canvas c) {
        drawCircles(c);
    }

    protected void drawCircles(Canvas c) {

        mRenderPaint.setStyle(Paint.Style.FILL);

        float phaseX = mAnimator.getPhaseX();
        float phaseY = mAnimator.getPhaseY();

        List<LineDataSet> dataSets = mChart.getLineData().getDataSets();

        for (int i = 0; i < dataSets.size(); i++) {

            LineDataSet dataSet = dataSets.get(i);

            if (!dataSet.isVisible() || !dataSet.isDrawCirclesEnabled())
                continue;

            mCirclePaintInner.setColor(dataSet.getCircleHoleColor());

            Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());
            List<Entry> entries = dataSet.getYVals();

            Entry entryFrom = dataSet.getEntryForXIndex((mMinX < 0) ? 0 : mMinX);
            Entry entryTo = dataSet.getEntryForXIndex(mMaxX);

            int minx = Math.max(dataSet.getEntryPosition(entryFrom), 0);
            int maxx = Math.min(dataSet.getEntryPosition(entryTo) + 1, entries.size());

            CircleBuffer buffer = mCircleBuffers[i];
            buffer.setPhases(phaseX, phaseY);
            buffer.limitFrom(minx);
            buffer.limitTo(maxx);
            buffer.feed(entries);

            trans.pointValuesToPixel(buffer.buffer);

            float halfsize = dataSet.getCircleSize() / 2f;

            for (int j = 0, count = (int) Math.ceil((maxx - minx) * phaseX + minx) * 2; j < count; j += 2) {

                float x = buffer.buffer[j];
                float y = buffer.buffer[j + 1];

                if (!mViewPortHandler.isInBoundsRight(x))
                    break;

                // make sure the circles don't do shitty things outside
                // bounds
                if (!mViewPortHandler.isInBoundsLeft(x) || !mViewPortHandler.isInBoundsY(y))
                    continue;

                int circleColor = dataSet.getCircleColor(j / 2 + minx);

                mRenderPaint.setColor(circleColor);

                c.drawCircle(x, y, dataSet.getCircleSize(),
                        mRenderPaint);

                if (dataSet.isDrawCircleHoleEnabled()
                        && circleColor != mCirclePaintInner.getColor())
                    c.drawCircle(x, y,
                            halfsize,
                            mCirclePaintInner);
            }
        }
        
        
    }

    //绘制点击后出现的十字架的过程
    @Override
    public void drawHighlighted(Canvas c, Highlight[] indices) {

        for (int i = 0; i < indices.length; i++) {

            LineDataSet set = mChart.getLineData().getDataSetByIndex(indices[i]
                    .getDataSetIndex());

            if (set == null || !set.isHighlightEnabled())
                continue;

            mHighlightPaint.setColor(set.getHighLightColor());

            //获得突出值的X轴坐标系数，相当于在X坐标轴上的数据所在数组中的位数
            int xIndex = indices[i].getXIndex(); // get the
                                                 // x-position

            if (xIndex > mChart.getXChartMax() * mAnimator.getPhaseX())
                continue;
             //获得此时X轴坐标对应的Y值
            final float yVal = set.getYValForXIndex(xIndex);
            if (yVal == Float.NaN)
                continue;

            float y = yVal * mAnimator.getPhaseY(); // get the y-position 获得Y轴位置
            //突出线X轴和Y轴的轴线，格式如[2.0, 3.784, 2.0, 0.0, 0.0, 3.3783803, 6.0, 3.3783803]所示
            //结果就是一个十字架
            float[] pts = new float[] {
                    xIndex, mChart.getYChartMax(), xIndex, mChart.getYChartMin(), mChart.getXChartMin(), y,
                    mChart.getXChartMax(), y
            };

            mChart.getTransformer(set.getAxisDependency()).pointValuesToPixel(pts);
            // draw the highlight lines
            //绘制出突出线
            c.drawLines(pts, mHighlightPaint);
        }
    }

	
    public boolean isStartDrawDefaultMarkerView() {
		return startDrawDefaultMarkerView;
	}

	public void setStartDrawDefaultMarkerView(boolean startDrawDefaultMarkerView) {
		this.startDrawDefaultMarkerView = startDrawDefaultMarkerView;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void drawDefaultMarkerView(Canvas canvas,
			MarkerView mMarkerView,BarLineChartBase barLineChartBase) {
		// 获取最后的两个数值的X轴坐标值
		int highestVisibleXIndex      = mChart.getLineData().getXVals().size()-1;
		int secondHigestVisibleXIndex = highestVisibleXIndex - 1;
		
		if(mMarkerView == null || barLineChartBase == null ) return;
		
		// 获取最后的两个点的Y值Entry
		List<Entry> entries           = barLineChartBase.getEntriesAtIndex(highestVisibleXIndex);
		List<Entry> secondentries     = barLineChartBase.getEntriesAtIndex(secondHigestVisibleXIndex);

		Entry entry                   = entries.get(0);
		Entry secondEntry             = secondentries.get(0);
        //最后一个点的(x,y)值
		float[] defaultMarkerPosition = barLineChartBase.getMarkerPosition(entry, 0);
		//倒数第二个点的(x,y)值
		float[] secondMarkerPosition  = barLineChartBase.getMarkerPosition(secondEntry, 0);
		
		barLineChartBase.mIndicesToHightlight = new Highlight[] { 
				barLineChartBase.getHighlightByTouchPoint(
				defaultMarkerPosition[0], defaultMarkerPosition[1]) };
		
		 //倒数第二个点与最后一个点的差值
		float y = secondMarkerPosition[1] - defaultMarkerPosition[1];
		
		if(y<0) {
			//下降趋势
			entry.setData(true);
		}else {
			//上升趋势
			entry.setData(false);
		}

		// callbacks to update the content
		mMarkerView.refreshContent(entry, 0);

		mMarkerView.measure(
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		mMarkerView.layout(0, 0, mMarkerView.getMeasuredWidth(),
				mMarkerView.getMeasuredHeight());
		if (y < 0 )//下降趋势，此时MarkerView放最后一个点下方
		{
			mMarkerView.draw(canvas,
					defaultMarkerPosition[0] - mMarkerView.getWidth() / 2,
					defaultMarkerPosition[1] + mMarkerView.getHeight() + 10 );
		}else 
		{
			//相等或者上升趋势，此时MarkerView放最后一个点上方
				mMarkerView.draw(canvas,
						defaultMarkerPosition[0] - mMarkerView.getWidth() / 2,
						defaultMarkerPosition[1] -10 );
			
		}


	}
   
}
