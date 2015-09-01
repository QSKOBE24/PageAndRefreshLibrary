package com.qskobe24.mylistview.activity;

import android.app.Activity;
import android.os.Bundle;

import com.guosen.pageandrefreshlistview.PageAndRefreshListView;
import com.qskobe24.mylistview.R;
import com.qskobe24.mylistview.adapter.PageListViewAdapter;
import com.qskobe24.mylistview.request.DemoRequest;

public class PageListViewActivity extends Activity {

	private PageAndRefreshListView mListView;
	private PageListViewAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_page_and_refresh);
		mListView = (PageAndRefreshListView) findViewById(R.id.demo_page_list);
		mAdapter = new PageListViewAdapter(this, new DemoRequest());
		mListView.setAdapter(mAdapter);
	}

	
}
