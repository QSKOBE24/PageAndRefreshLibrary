package com.qskobe24.chartview;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import com.guosen.chartview.animation.Easing;
import com.guosen.chartview.charts.LineChart;
import com.guosen.chartview.charts.PieChart;
import com.guosen.chartview.data.Approximator;
import com.guosen.chartview.data.Approximator.ApproximatorType;
import com.guosen.chartview.data.DataSet;
import com.guosen.chartview.data.Entry;
import com.guosen.chartview.data.LineData;
import com.guosen.chartview.data.LineDataSet;
import com.guosen.chartview.data.PieData;
import com.guosen.chartview.data.PieDataSet;
import com.guosen.chartview.util.ColorTemplate;

@SuppressLint("SimpleDateFormat")
public class MainActivity extends DemoBase  {
    private LineChart mChart;
    private Button   getDataButton,skipButton;
    private PieChart pieChart;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        
        getDataButton = (Button)findViewById(R.id.setdata);
        getDataButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				GetDataFromNetOrDisk(7);
			}
		});
        
        skipButton = (Button)findViewById(R.id.skip);
        skipButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), SecondActivity.class);
				startActivity(intent);
			}
		});
        
        mChart = (LineChart) findViewById(R.id.chart1);
        pieChart = (PieChart)findViewById(R.id.piechart);
        pieChart.setTouchEnabled(true);
        pieChart.animateY(1500, Easing.EasingOption.EaseInOutQuad);
        setPieData(3);
        
        
    }
    
    private void setPieData(int count) {
    	float mult = 10;
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        for (int i = 0; i < count + 1; i++) {
            yVals1.add(new Entry((float) (Math.random() * mult) + mult / 5, i));
        }

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < count + 1; i++)
            xVals.add(Companies[i % Companies.length]);

        PieDataSet dataSet = new PieDataSet(yVals1, "手机销量占比");

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        dataSet.setColors(colors);
        PieData data = new PieData(xVals, dataSet);
        pieChart.setData(data);
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.line, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.actionToggleValues: { 
                for (DataSet<?> set : mChart.getData().getDataSets())
                    set.setDrawValues(!set.isDrawValuesEnabled());
                mChart.invalidate();
                break;
            }
            case R.id.actionToggleHighlight: {
                if (mChart.isHighlightEnabled())
                    mChart.setHighlightEnabled(false);
                else
                    mChart.setHighlightEnabled(true);
                mChart.invalidate();
                break;
            }
            case R.id.actionToggleFilled: {

                ArrayList<LineDataSet> sets = (ArrayList<LineDataSet>) mChart.getData()
                        .getDataSets();

                for (LineDataSet set : sets) {
                    if (set.isDrawFilledEnabled())
                        set.setDrawFilled(false);
                    else
                        set.setDrawFilled(true);
                }
                mChart.invalidate();
                break;
            }
            case R.id.actionToggleCircles: {
                ArrayList<LineDataSet> sets = (ArrayList<LineDataSet>) mChart.getData()
                        .getDataSets();

                for (LineDataSet set : sets) {
                    if (set.isDrawCirclesEnabled())
                        set.setDrawCircles(false);
                    else
                        set.setDrawCircles(true);
                }
                mChart.invalidate();
                break;
            }
            case R.id.actionToggleCubic: {
                ArrayList<LineDataSet> sets = (ArrayList<LineDataSet>) mChart.getData()
                        .getDataSets();

                for (LineDataSet set : sets) {
                    if (set.isDrawCubicEnabled())
                        set.setDrawCubic(false);
                    else
                        set.setDrawCubic(true);
                }
                mChart.invalidate();
                break;
            }
            case R.id.actionToggleStartzero: {
                mChart.getAxisLeft().setStartAtZero(!mChart.getAxisLeft().isStartAtZeroEnabled());
              //  mChart.getAxisRight().setStartAtZero(!mChart.getAxisRight().isStartAtZeroEnabled());
                mChart.invalidate();
                break;
            }
            case R.id.actionTogglePinch: {
                if (mChart.isPinchZoomEnabled())
                    mChart.setPinchZoom(false);
                else
                    mChart.setPinchZoom(true);

                mChart.invalidate();
                break;
            }
            case R.id.actionToggleAutoScaleMinMax: {
                mChart.setAutoScaleMinMaxEnabled(!mChart.isAutoScaleMinMaxEnabled());
                mChart.notifyDataSetChanged();
                break;
            }
            case R.id.animateX: {
                mChart.animateX(3000);
                break;
            }
            case R.id.animateY: {
                mChart.animateY(3000, Easing.EasingOption.EaseInCubic);
                break;
            }
            case R.id.animateXY: {
                mChart.animateXY(3000, 3000);
                break;
            }
            case R.id.actionToggleFilter: {

                // the angle of filtering is 35°
                Approximator a = new Approximator(ApproximatorType.DOUGLAS_PEUCKER, 35);

                if (!mChart.isFilteringEnabled()) {
                    mChart.enableFiltering(a);
                } else {
                    mChart.disableFiltering();
                }
                mChart.invalidate();

                break;
            }
            case R.id.actionSave: {
                if (mChart.saveToPath("title" + System.currentTimeMillis(), "")) {
                    Toast.makeText(getApplicationContext(), "Saving SUCCESSFUL!",
                            Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "Saving FAILED!", Toast.LENGTH_SHORT)
                            .show();

                // mChart.saveToGallery("title"+System.currentTimeMillis())
                break;
            }
            
            case R.id.pieactionToggleValues: {
                for (DataSet<?> set : mChart.getData().getDataSets())
                    set.setDrawValues(!set.isDrawValuesEnabled());

                mChart.invalidate();
                break;
            }
            case R.id.pieactionToggleHole: {
                if (pieChart.isDrawHoleEnabled())
                	pieChart.setDrawHoleEnabled(false);
                else
                	pieChart.setDrawHoleEnabled(true);
                mChart.invalidate();
                break;
            }
            case R.id.pieactionDrawCenter: {
                if (pieChart.isDrawCenterTextEnabled())
                	pieChart.setDrawCenterText(false);
                else
                	pieChart.setDrawCenterText(true);
                pieChart.invalidate();
                break;
            }
            case R.id.pieactionToggleXVals: {

            	pieChart.setDrawSliceText(!pieChart.isDrawSliceTextEnabled());
            	pieChart.invalidate();
                break;
            }
            case R.id.pieactionSave: {
                // mChart.saveToGallery("title"+System.currentTimeMillis());
            	pieChart.saveToPath("title" + System.currentTimeMillis(), "");
                break;
            }
            case R.id.pieactionTogglePercent:
            	pieChart.setUsePercentValues(!pieChart.isUsePercentValuesEnabled());
            	pieChart.invalidate();
                break;
            case R.id.pieanimateX: {
            	pieChart.animateX(1800);
                break;
            }
            case R.id.pieanimateY: {
            	pieChart.animateY(1800);
                break;
            }
            case R.id.pieanimateXY: {
            	pieChart.animateXY(1800, 1800);
                break;
            }
        }
        return true;
    }

    public void GetDataFromNetOrDisk(int count) {
        ArrayList<String> xVals = new ArrayList<String>();
        Calendar now = Calendar.getInstance();
        String today = new SimpleDateFormat("MM-dd").format(now.getTime());
        String []result = new String[count];
        result [count-1] = today;
        //result[count-2] = today;
        for (int i = 0; i< count-1; i++) {
			now.add(Calendar.DATE, -1);
			String yesterday = new SimpleDateFormat( "MM-dd").format(now.getTime());
			result[count-2-i] = yesterday;
		}
       
        for (int i = 0; i < result.length; i++) {
			xVals.add(result[i]);
		}
    
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        yVals.add(new Entry((float) 0.001, 0));
        yVals.add(new Entry((float) 0.001, 1));
        yVals.add(new Entry((float) 0.001, 2));
        yVals.add(new Entry((float) 0.001, 3));
        yVals.add(new Entry((float) 0.001, 4));
        yVals.add(new Entry((float) 0.001, 5));
        yVals.add(new Entry((float) 0.001, 6));
      
        //通过计算求得七日年化利率，然后再将其设置好
        yVals.get(yVals.size()-1).setExtra("2.00");
        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "");
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);
        //准备好了数据以后，一定要设置这句话
        mChart.setShowDefaultData(false);
        //以下2句话一定要在setData()之前调用，不然绘制图会出错
        //其中的最大值和最小值是得到数据之后进行计算以后得出的,其中MaxValue为最大值4/3*（数据集中最大Y值）-1/3*（数据集中的最小值）
        //MinValue为4/3*（数据集中最小Y值）-1/3*（数据集中的最大值）
       // mChart.getAxisLeft().setAxisMaxValue((float) (6.028f));
	   // mChart.getAxisLeft().setAxisMinValue((float) (-1.10f));
        setMaxMin(data);
		mChart.setData(data);
		mChart.invalidate();
    }
    

    public  void setMaxMin(LineData data){
    	float max = 0.0f;
    	float min = 0.0f;
    	int   num = 0;
    	double range  = 0.0f;
    	double rawInterval= 0.0f;
    	for (int j = 0; j < data.getDataSetCount(); j++) {
    		LineDataSet  lineDataSet = data.getDataSets().get(j);
            num  =  lineDataSet.getYVals().size();
    		max  =  lineDataSet.getYMax();
            min  =  lineDataSet.getYMin();
            range = Math.abs(max - min);
            rawInterval = range / num;
		}
    	
    	if(max == min){
    		mChart.getAxisLeft().setAxisMaxValue((float) (max + 1.0f));
    	    mChart.getAxisLeft().setAxisMinValue((float) (min - 1.0f));
    	}else {
    		mChart.getAxisLeft().setAxisMaxValue((float) (max + rawInterval));
    	    mChart.getAxisLeft().setAxisMinValue((float) (min-rawInterval));
		}
    	
    }
}
