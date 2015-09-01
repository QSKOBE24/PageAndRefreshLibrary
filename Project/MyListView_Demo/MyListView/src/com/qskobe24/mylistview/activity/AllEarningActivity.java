package com.qskobe24.mylistview.activity;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.qskobe24.mylistview.R;
import com.qskobe24.mylistview.adapter.AllEarningAdapter;
import com.qskobe24.mylistview.bean.AllEarningBean;

public class AllEarningActivity extends Activity {
	
	private String[] dataString = {"2015-05-12","2015-05-11","2015-05-10","2015-05-09"}; 
	private String[] earningStrings={"3600.00","6511.00","6845.00","9876.00"};
	private String[] sevenDaysStrings ={"23.00","23.00","23.00","23.00"};
	
	private ListView listView;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_allearning);
		listView = (ListView)findViewById(R.id.allearning_listview);
	    
		List<AllEarningBean> list = new ArrayList<AllEarningBean>();
		list.add(new AllEarningBean(dataString[0],earningStrings[0],sevenDaysStrings[0]));
		list.add(new AllEarningBean(dataString[1],earningStrings[1],sevenDaysStrings[1]));
		list.add(new AllEarningBean(dataString[2],earningStrings[2],sevenDaysStrings[2]));
		
		AllEarningAdapter allEarningAdapter = new AllEarningAdapter(getApplicationContext());
		
		allEarningAdapter.setBeans(list);
		
		listView.setAdapter(allEarningAdapter);
	
	
	}

}
