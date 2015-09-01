package com.qskobe24.mylistview.activity;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.qskobe24.mylistview.R;
import com.qskobe24.mylistview.adapter.DealFlowAdapter;
import com.qskobe24.mylistview.bean.DealFlowBean;

public class DealFlowActivity extends Activity {
	
	private String[] dateString = {"2015-05-12","2015-05-11","2015-05-10","2015-05-09"}; 
	private String[] typeString = {"存入","快速取出","普通存入","快速取出"};
	private String[] dealMoneyStrings = {"450.00","665.00","545.45","400.55"};
	private String[] dealTypeStrings = {"成功","未报","取消","成功"};
	
	private ListView listView;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dealflow);
		
		
        listView = (ListView)findViewById(R.id.dealflow_listview);
		
		List<DealFlowBean> list = new ArrayList<DealFlowBean>();
		list.add(new DealFlowBean(dateString[0],typeString[0],dealMoneyStrings[0],dealTypeStrings[0]));
		list.add(new DealFlowBean(dateString[1],typeString[1],dealMoneyStrings[1],dealTypeStrings[1]));
		list.add(new DealFlowBean(dateString[2],typeString[2],dealMoneyStrings[2],dealTypeStrings[2]));
		
		DealFlowAdapter dealFlowAdapter = new DealFlowAdapter(getApplicationContext());
		
		dealFlowAdapter.setBeans(list);
		
		listView.setAdapter(dealFlowAdapter);
		
	}

}
