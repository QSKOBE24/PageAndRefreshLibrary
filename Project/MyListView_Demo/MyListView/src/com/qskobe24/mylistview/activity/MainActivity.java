package com.qskobe24.mylistview.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.qskobe24.mylistview.R;
import com.qskobe24.mylistview.adapter.MainAdapter;

public class MainActivity extends Activity {
 
	private ListView listView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
       
		listView = (ListView)findViewById(R.id.listView1);
		
		MainAdapter mainAdapter = new MainAdapter(this);
		
		listView.setAdapter(mainAdapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				switch (position) {
				case 0:
					intent.setClass(getApplicationContext(), SecondActivity.class);
					startActivity(intent);
					break;
				case 1:
					intent.setClass(getApplicationContext(), ThirdActivity.class);
					startActivity(intent);
					break;
				case 2:
					intent.setClass(getApplicationContext(), ForthActivity.class);
					startActivity(intent);
					break;
				case 3:
					intent.setClass(getApplicationContext(), FifthActivity.class);
					startActivity(intent);
					break;
				case 4:
					intent.setClass(getApplicationContext(), SixthActivity.class);
					startActivity(intent);
					break;
				case 5:
					intent.setClass(getApplicationContext(), SeventhActivity.class);
					startActivity(intent);
					break;
				}
			}
		});
	}
}
