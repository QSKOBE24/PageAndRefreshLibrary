package com.guosen.chartview.component;


import android.graphics.Paint;

import com.guosen.chartview.util.DefaultValueFormatter;
import com.guosen.chartview.util.Utils;
import com.guosen.chartview.util.ValueFormatter;


/**
 * Class representing the y-axis labels settings and its entries. Only use the
 * setter methods to modify it. Do not access public variables directly. Be
 * aware that not all features the YLabels class provides are suitable for the
 * RadarChart. Customizations that affect the value range of the axis need to be
 * applied before setting data for the chart.
 *
 * 该类代表y轴标签设置和它的条目。只使用setter方法来修改它。不要直接访问公共变量。
 * 请注意,并不是所有的YLabels提供的特性都适合RadarChart
 * 影响X轴的自定义采用必须在图表设置了数据之前。
 *
 * @author Philipp Jahoda
 */
public class YAxis extends AxisBase {

    /** custom formatter that is used instead of the auto-formatter if set
     * 如果设置了，自定义格式用来代替自动格式*/
    protected ValueFormatter mValueFormatter;

    /** the actual array of entries
     *  entry的真实数组*/
    public float[] mEntries = new float[] {};

    /** the number of entries the legend contains
     * legend包含的entry数量 */
    public int mEntryCount;

    /** the number of decimal digits to use
     * 使用的小数点的位数 */
    public int mDecimals;

    /** the number of y-label entries the y-labels should have, default 5 
     * 这个是显示在Y轴上数据个数*/
    private int mLabelCount = 4;

    /** indicates if the top y-label entry is drawn or not */
    private boolean mDrawTopYLabelEntry = true;

    /** if true, the y-labels show only the minimum and maximum value */
    protected boolean mShowOnlyMinMax = false;

    /** flag that indicates if the axis is inverted or not
     * 用来指示Y轴的值是从低到高依次递增，还是依次递减；如果是true，则为递减，false为递增 */
    protected boolean mInverted = false;

    /** if true, the y-label entries will always start at zero */
    protected boolean mStartAtZero = false;

	/** custom minimum value this axis represents */
    protected float mCustomAxisMin = Float.NaN;

    /** custom maximum value this axis represents */
    protected float mCustomAxisMax = Float.NaN;

    /**
     * axis space from the largest value to the top in percent of the total axis
     * range
     */
    protected float mSpacePercentTop = 10f;

    /**
     * axis space from the smallest value to the bottom in percent of the total
     * axis range
     */
    protected float mSpacePercentBottom = 20f;

    public float mAxisMaximum = 0f;
    public float mAxisMinimum = 0f;

    /** the total range of values this axis covers */
    public float mAxisRange = 0f;

    /** the position of the y-labels relative to the chart */
    private YAxisLabelPosition mPosition = YAxisLabelPosition.OUTSIDE_CHART;

    /** enum for the position of the y-labels relative to the chart */
    public enum YAxisLabelPosition {
        OUTSIDE_CHART, INSIDE_CHART
    }

    /** the side this axis object represents
     * Y轴对象表示的边 */
    private AxisDependency mAxisDependency;

    /**
     * Enum that specifies the axis a DataSet should be plotted against, either
     * LEFT or RIGHT.
     * 指示数据在Y轴左边还是右边
     * @author Philipp Jahoda
     */
    public enum AxisDependency {
        LEFT, RIGHT
    }

    public YAxis() {
        super();
        this.mAxisDependency = AxisDependency.LEFT;
        this.mYOffset = 0f;
	}

    public YAxis(AxisDependency position) {
        super();
        this.mAxisDependency = position;
        this.mYOffset = 0f;
	}

    public AxisDependency getAxisDependency() {
        return mAxisDependency;
    }

    /**
     * returns the position of the y-labels
     */
    public YAxisLabelPosition getLabelPosition() {
        return mPosition;
    }

    /**
     * sets the position of the y-labels
     * 
     * @param pos
     */
    public void setPosition(YAxisLabelPosition pos) {
        mPosition = pos;
    }

    /**
     * returns true if drawing the top y-axis label entry is enabled
     * 
     * @return
     */
    public boolean isDrawTopYLabelEntryEnabled() {
        return mDrawTopYLabelEntry;
    }

    /**
     * set this to true to enable drawing the top y-label entry. Disabling this
     * can be helpful when the top y-label and left x-label interfere with each
     * other. default: true
     * 
     * @param enabled
     */
    public void setDrawTopYLabelEntry(boolean enabled) {
        mDrawTopYLabelEntry = enabled;
    }

    /**
     * sets the number of label entries for the y-axis max = 25, min = 2,
     * default: 6, be aware that this number is not fixed and can only be
     * approximated
     * 
     * @param yCount
     */
    public void setLabelCount(int yCount) {

        if (yCount > 25)
            yCount = 25;
        if (yCount < 2)
            yCount = 2;

        mLabelCount = yCount;
    }

    /**
     * Returns the number of label entries the y-axis should have
     * 
     * @return
     */
    public int getLabelCount() {
        return mLabelCount;
    }

    /**
     * If enabled, the YLabels will only show the minimum and maximum value of
     * the chart. This will ignore/override the set label count.
     * 
     * @param enabled
     */
    public void setShowOnlyMinMax(boolean enabled) {
        mShowOnlyMinMax = enabled;
    }

    /**
     * Returns true if showing only min and max value is enabled.
     * 
     * @return
     */
    public boolean isShowOnlyMinMaxEnabled() {
        return mShowOnlyMinMax;
    }

    /**
     * If this is set to true, the y-axis is inverted which means that low
     * values are on top of the chart, high values on bottom.
     * 
     * @param enabled
     */
    public void setInverted(boolean enabled) {
        mInverted = enabled;
    }

    /**
     * If this returns true, the y-axis is inverted.
     * 
     * @return
     */
    public boolean isInverted() {
        return mInverted;
    }

    /**
     * enable this to force the y-axis labels to always start at zero
     * 
     * @param enabled
     */
    public void setStartAtZero(boolean enabled) {
        this.mStartAtZero = enabled;
    }

    /**
     * returns true if the chart is set to start at zero, false otherwise
     * 
     * @return
     */
    public boolean isStartAtZeroEnabled() {
        return mStartAtZero;
    }

	public float getAxisMinValue() {
        return mCustomAxisMin;
    }

    /**
     * Set a custom minimum value for this axis. If set, this value will not be
     * calculated automatically depending on the provided data. Use
     * resetAxisMinValue() to undo this. Do not forget to call
     * setStartAtZero(false) if you use this method. Otherwise, the axis-minimum
     * value will still be forced to 0.
     * 
     * @param min
     */
    public void setAxisMinValue(float min) {
        mCustomAxisMin = min;
    }

    /**
     * By calling this method, any custom minimum value that has been previously
     * set is reseted, and the calculation is done automatically.
     */
    public void resetAxisMinValue() {
        mCustomAxisMin = Float.NaN;
    }

    public float getAxisMaxValue() {
        return mCustomAxisMax;
    }

    /**
     * Set a custom maximum value for this axis. If set, this value will not be
     * calculated automatically depending on the provided data. Use
     * resetAxisMaxValue() to undo this.
     * 
     * @param max
     */
    public void setAxisMaxValue(float max) {
        mCustomAxisMax = max;
    }

    /**
     * By calling this method, any custom maximum value that has been previously
     * set is reseted, and the calculation is done automatically.
     */
    public void resetAxisMaxValue() {
        mCustomAxisMax = Float.NaN;
    }

    /**
     * Sets the top axis space in percent of the full range. Default 10f
     * 
     * @param percent
     */
    public void setSpaceTop(float percent) {
        mSpacePercentTop = percent;
    }

    /**
     * Returns the top axis space in percent of the full range. Default 10f
     * 
     * @return
     */
    public float getSpaceTop() {
        return mSpacePercentTop;
    }

    /**
     * Sets the bottom axis space in percent of the full range. Default 10f
     * 
     * @param percent
     */
    public void setSpaceBottom(float percent) {
        mSpacePercentBottom = percent;
    }

    /**
     * Returns the bottom axis space in percent of the full range. Default 10f
     * 
     * @return
     */
    public float getSpaceBottom() {
        return mSpacePercentBottom;
    }

    public float getRequiredWidthSpace(Paint p) {

        p.setTextSize(mTextSize);

        String label = getLongestLabel();
        return (float) Utils.calcTextWidth(p, label) + getXOffset() * 2f;
    }

    public float getRequiredHeightSpace(Paint p) {

        p.setTextSize(mTextSize);

        String label = getLongestLabel();
        return (float) Utils.calcTextHeight(p, label) + Utils.convertDpToPixel(2.5f) * 2f + getYOffset();
    }

    @Override
    public String getLongestLabel() {

        String longest = "";

        for (int i = 0; i < mEntries.length; i++) {
            String text = getFormattedLabel(i);

            if (longest.length() < text.length())
                longest = text;
        }

        return longest;
    }

    /**
     * Returns the formatted y-label at the specified index. This will either
     * use the auto-formatter or the custom formatter (if one is set).
     * 
     * @param index
     * @return
     */
    public String getFormattedLabel(int index) {
        if (index < 0 || index >= mEntries.length)
            return "";
        else
            return getValueFormatter().getFormattedValue(mEntries[index]);
    }

    /**
     * Sets the formatter to be used for drawing the values inside the chart. If
     * no formatter is set, the chart will automatically determine a reasonable
     * formatting (concerning decimals) for all the values that are drawn inside
     * the chart. Use chart.getDefaultValueFormatter() to use the formatter
     * calculated by the chart.
     *
     * @param f
     */
    public void setValueFormatter(ValueFormatter f) {

        if (f == null)
            return;
        else
            mValueFormatter = f;
    }

    /**
     * Returns the formatter used for drawing the values inside the chart.
     *
     * @return
     */
    public ValueFormatter getValueFormatter() {
        return mValueFormatter;
    }

    /**
     * If this component has no ValueFormatter or is only equipped with the
     * default one (no custom set), return true.
     * 
     * @return
     */
    public boolean needsDefaultFormatter() {
        if (mValueFormatter == null)
            return true;
        if (mValueFormatter instanceof DefaultValueFormatter)
            return true;

        return false;
    }

    /**
     * Returns true if this axis needs horizontal offset, false if no offset is
     * needed.
     * 
     * @return
     */
    public boolean needsOffset() {
        if (isEnabled() && isDrawLabelsEnabled()
                && getLabelPosition() == YAxisLabelPosition.OUTSIDE_CHART)
            return true;
        else
            return false;
    }
}
