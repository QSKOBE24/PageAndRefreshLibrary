package com.qskobe24.chartview;

import java.util.ArrayList;

import com.guosen.chartview.animation.Easing;
import com.guosen.chartview.charts.PieChart;
import com.guosen.chartview.data.Entry;
import com.guosen.chartview.data.PieData;
import com.guosen.chartview.data.PieDataSet;
import com.guosen.chartview.util.ColorTemplate;
import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public class SecondActivity extends Activity {
	private PieChart pieChart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_second);

		pieChart = (PieChart) findViewById(R.id.piechart);
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
			xVals.add("手机" + i);

		PieDataSet dataSet = new PieDataSet(yVals1, "手机销量占比");

		ArrayList<Integer> colors = new ArrayList<Integer>();

		for (int c : ColorTemplate.VORDIPLOM_COLORS)
			colors.add(c);
		dataSet.setColors(colors);
		PieData data = new PieData(xVals, dataSet);
		pieChart.setData(data);
	}

}
