package com.qskobe24.mylistview.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.qskobe24.mylistview.R;
import com.qskobe24.mylistview.adapter.ProductEarningAdapter;
import com.qskobe24.mylistview.bean.ProductEarningBean;

public class ProductEarningActivity extends Activity {
	
	private String[] dataString = {"2015-05-12","2015-05-11","2015-05-10","2015-05-09"}; 
	private String[] earningStrings={"1.199","1.0555","1.5745","1.6455"};
	private String[] sevenDaysStrings ={"5.640%","5.643%","5.620%","5.670%"};
	
	private ListView listView;
	private ProductEarningAdapter productEarningAdapter;
	List<ProductEarningBean> list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_productearning);
		initListView();
	}

	private void initListView() {
		// TODO Auto-generated method stub
		
        listView = (ListView)findViewById(R.id.productearning_listview);
		
		list = new ArrayList<ProductEarningBean>();
		list.add(new ProductEarningBean(dataString[0],earningStrings[0],sevenDaysStrings[0]));
		list.add(new ProductEarningBean(dataString[1],earningStrings[1],sevenDaysStrings[1]));
		list.add(new ProductEarningBean(dataString[2],earningStrings[2],sevenDaysStrings[2]));
		
		productEarningAdapter = new ProductEarningAdapter(getApplicationContext());
		
		productEarningAdapter.setBeans(list);
		
		listView.setAdapter(productEarningAdapter);
		
	}


}
