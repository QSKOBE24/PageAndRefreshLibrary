package com.qskobe24.mylistview.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.qskobe24.mylistview.R;
import com.qskobe24.mylistview.adapter.CancelDepositAdapter;
import com.qskobe24.mylistview.bean.CancelDepositBean;

public class CancelDepositActivity extends Activity {
	private String[] dateString = { "2015-05-12", "2015-05-11", "2015-05-10",
			"2015-05-09" };
	private String[] typeString = { "存入", "快速取出", "普通存入", "快速取出" };
	private String[] dealMoneyStrings = { "450.00", "665.00", "545.45",
			"400.55" };
	private String[] dealTypeStrings = { "成功", "未报", "取消", "成功" };
    private String[] operationStrings = {"取消存入","存入","正常存入","取消存入"};
    
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_canceldeposit);

		listView = (ListView) findViewById(R.id.canceldeposit_listview);

		final List<CancelDepositBean> list = new ArrayList<CancelDepositBean>();
		list.add(new CancelDepositBean(dateString[0], typeString[0],
				dealMoneyStrings[0], dealTypeStrings[0],operationStrings[0]));
		list.add(new CancelDepositBean(dateString[1], typeString[1],
				dealMoneyStrings[1], dealTypeStrings[1],operationStrings[1]));
		list.add(new CancelDepositBean(dateString[2], typeString[2],
				dealMoneyStrings[2], dealTypeStrings[2],operationStrings[2]));
		list.add(new CancelDepositBean(dateString[3], typeString[3],
				dealMoneyStrings[3], dealTypeStrings[3],operationStrings[3]));
		list.add(new CancelDepositBean(dateString[0], typeString[0],
				dealMoneyStrings[0], dealTypeStrings[0],operationStrings[0]));
		list.add(new CancelDepositBean(dateString[1], typeString[1],
				dealMoneyStrings[1], dealTypeStrings[1],operationStrings[1]));
		list.add(new CancelDepositBean(dateString[2], typeString[2],
				dealMoneyStrings[2], dealTypeStrings[2],operationStrings[2]));
		list.add(new CancelDepositBean(dateString[3], typeString[3],
				dealMoneyStrings[3], dealTypeStrings[3],operationStrings[3]));
		list.add(new CancelDepositBean(dateString[0], typeString[0],
				dealMoneyStrings[0], dealTypeStrings[0],operationStrings[0]));
		list.add(new CancelDepositBean(dateString[1], typeString[1],
				dealMoneyStrings[1], dealTypeStrings[1],operationStrings[1]));
		list.add(new CancelDepositBean(dateString[2], typeString[2],
				dealMoneyStrings[2], dealTypeStrings[2],operationStrings[2]));
		list.add(new CancelDepositBean(dateString[3], typeString[3],
				dealMoneyStrings[3], dealTypeStrings[3],operationStrings[3]));
		list.add(new CancelDepositBean(dateString[0], typeString[0],
				dealMoneyStrings[0], dealTypeStrings[0],operationStrings[0]));
		list.add(new CancelDepositBean(dateString[1], typeString[1],
				dealMoneyStrings[1], dealTypeStrings[1],operationStrings[1]));
		list.add(new CancelDepositBean(dateString[2], typeString[2],
				dealMoneyStrings[2], dealTypeStrings[2],operationStrings[2]));
		list.add(new CancelDepositBean(dateString[3], typeString[3],
				dealMoneyStrings[3], dealTypeStrings[3],operationStrings[3]));
		list.add(new CancelDepositBean(dateString[0], typeString[0],
				dealMoneyStrings[0], dealTypeStrings[0],operationStrings[0]));
		list.add(new CancelDepositBean(dateString[1], typeString[1],
				dealMoneyStrings[1], dealTypeStrings[1],operationStrings[1]));
		list.add(new CancelDepositBean(dateString[2], typeString[2],
				dealMoneyStrings[2], dealTypeStrings[2],operationStrings[2]));
		list.add(new CancelDepositBean(dateString[3], typeString[3],
				dealMoneyStrings[3], dealTypeStrings[3],operationStrings[3]));
		list.add(new CancelDepositBean(dateString[0], typeString[0],
				dealMoneyStrings[0], dealTypeStrings[0],operationStrings[0]));
		list.add(new CancelDepositBean(dateString[1], typeString[1],
				dealMoneyStrings[1], dealTypeStrings[1],operationStrings[1]));
		list.add(new CancelDepositBean(dateString[2], typeString[2],
				dealMoneyStrings[2], dealTypeStrings[2],operationStrings[2]));
		list.add(new CancelDepositBean(dateString[3], typeString[3],
				dealMoneyStrings[3], dealTypeStrings[3],operationStrings[3]));
		list.add(new CancelDepositBean(dateString[0], typeString[0],
				dealMoneyStrings[0], dealTypeStrings[0],operationStrings[0]));
		list.add(new CancelDepositBean(dateString[1], typeString[1],
				dealMoneyStrings[1], dealTypeStrings[1],operationStrings[1]));
		list.add(new CancelDepositBean(dateString[2], typeString[2],
				dealMoneyStrings[2], dealTypeStrings[2],operationStrings[2]));
		list.add(new CancelDepositBean(dateString[3], typeString[3],
				dealMoneyStrings[3], dealTypeStrings[3],operationStrings[3]));
		list.add(new CancelDepositBean(dateString[0], typeString[0],
				dealMoneyStrings[0], dealTypeStrings[0],operationStrings[0]));
		list.add(new CancelDepositBean(dateString[1], typeString[1],
				dealMoneyStrings[1], dealTypeStrings[1],operationStrings[1]));
		list.add(new CancelDepositBean(dateString[2], typeString[2],
				dealMoneyStrings[2], dealTypeStrings[2],operationStrings[2]));
		list.add(new CancelDepositBean(dateString[3], typeString[3],
				dealMoneyStrings[3], dealTypeStrings[3],operationStrings[3]));
		list.add(new CancelDepositBean(dateString[0], typeString[0],
				dealMoneyStrings[0], dealTypeStrings[0],operationStrings[0]));
		list.add(new CancelDepositBean(dateString[1], typeString[1],
				dealMoneyStrings[1], dealTypeStrings[1],operationStrings[1]));
		list.add(new CancelDepositBean(dateString[2], typeString[2],
				dealMoneyStrings[2], dealTypeStrings[2],operationStrings[2]));
		list.add(new CancelDepositBean(dateString[3], typeString[3],
				dealMoneyStrings[3], dealTypeStrings[3],operationStrings[3]));
		list.add(new CancelDepositBean(dateString[0], typeString[0],
				dealMoneyStrings[0], dealTypeStrings[0],operationStrings[0]));
		list.add(new CancelDepositBean(dateString[1], typeString[1],
				dealMoneyStrings[1], dealTypeStrings[1],operationStrings[1]));
		list.add(new CancelDepositBean(dateString[2], typeString[2],
				dealMoneyStrings[2], dealTypeStrings[2],operationStrings[2]));
		list.add(new CancelDepositBean(dateString[3], typeString[3],
				dealMoneyStrings[3], dealTypeStrings[3],operationStrings[3]));
		list.add(new CancelDepositBean(dateString[0], typeString[0],
				dealMoneyStrings[0], dealTypeStrings[0],operationStrings[0]));
		list.add(new CancelDepositBean(dateString[1], typeString[1],
				dealMoneyStrings[1], dealTypeStrings[1],operationStrings[1]));
		list.add(new CancelDepositBean(dateString[2], typeString[2],
				dealMoneyStrings[2], dealTypeStrings[2],operationStrings[2]));
		list.add(new CancelDepositBean(dateString[3], typeString[3],
				dealMoneyStrings[3], dealTypeStrings[3],operationStrings[3]));
		list.add(new CancelDepositBean(dateString[0], typeString[0],
				dealMoneyStrings[0], dealTypeStrings[0],operationStrings[0]));
		list.add(new CancelDepositBean(dateString[1], typeString[1],
				dealMoneyStrings[1], dealTypeStrings[1],operationStrings[1]));
		list.add(new CancelDepositBean(dateString[2], typeString[2],
				dealMoneyStrings[2], dealTypeStrings[2],operationStrings[2]));
		list.add(new CancelDepositBean(dateString[3], typeString[3],
				dealMoneyStrings[3], dealTypeStrings[3],operationStrings[3]));
		list.add(new CancelDepositBean(dateString[0], typeString[0],
				dealMoneyStrings[0], dealTypeStrings[0],operationStrings[0]));
		list.add(new CancelDepositBean(dateString[1], typeString[1],
				dealMoneyStrings[1], dealTypeStrings[1],operationStrings[1]));
		list.add(new CancelDepositBean(dateString[2], typeString[2],
				dealMoneyStrings[2], dealTypeStrings[2],operationStrings[2]));
		list.add(new CancelDepositBean(dateString[3], typeString[3],
				dealMoneyStrings[3], dealTypeStrings[3],operationStrings[3]));
		list.add(new CancelDepositBean(dateString[0], typeString[0],
				dealMoneyStrings[0], dealTypeStrings[0],operationStrings[0]));
		list.add(new CancelDepositBean(dateString[1], typeString[1],
				dealMoneyStrings[1], dealTypeStrings[1],operationStrings[1]));
		list.add(new CancelDepositBean(dateString[2], typeString[2],
				dealMoneyStrings[2], dealTypeStrings[2],operationStrings[2]));
		list.add(new CancelDepositBean(dateString[3], typeString[3],
				dealMoneyStrings[3], dealTypeStrings[3],operationStrings[3]));
		list.add(new CancelDepositBean(dateString[0], typeString[0],
				dealMoneyStrings[0], dealTypeStrings[0],operationStrings[0]));
		list.add(new CancelDepositBean(dateString[1], typeString[1],
				dealMoneyStrings[1], dealTypeStrings[1],operationStrings[1]));
		list.add(new CancelDepositBean(dateString[2], typeString[2],
				dealMoneyStrings[2], dealTypeStrings[2],operationStrings[2]));
		list.add(new CancelDepositBean(dateString[3], typeString[3],
				dealMoneyStrings[3], dealTypeStrings[3],operationStrings[3]));
		list.add(new CancelDepositBean(dateString[0], typeString[0],
				dealMoneyStrings[0], dealTypeStrings[0],operationStrings[0]));
		list.add(new CancelDepositBean(dateString[1], typeString[1],
				dealMoneyStrings[1], dealTypeStrings[1],operationStrings[1]));
		list.add(new CancelDepositBean(dateString[2], typeString[2],
				dealMoneyStrings[2], dealTypeStrings[2],operationStrings[2]));
		list.add(new CancelDepositBean(dateString[3], typeString[3],
				dealMoneyStrings[3], dealTypeStrings[3],operationStrings[3]));
		list.add(new CancelDepositBean(dateString[0], typeString[0],
				dealMoneyStrings[0], dealTypeStrings[0],operationStrings[0]));
		list.add(new CancelDepositBean(dateString[1], typeString[1],
				dealMoneyStrings[1], dealTypeStrings[1],operationStrings[1]));
		list.add(new CancelDepositBean(dateString[2], typeString[2],
				dealMoneyStrings[2], dealTypeStrings[2],operationStrings[2]));
		list.add(new CancelDepositBean(dateString[3], typeString[3],
				dealMoneyStrings[3], dealTypeStrings[3],operationStrings[3]));
		list.add(new CancelDepositBean(dateString[0], typeString[0],
				dealMoneyStrings[0], dealTypeStrings[0],operationStrings[0]));
		list.add(new CancelDepositBean(dateString[1], typeString[1],
				dealMoneyStrings[1], dealTypeStrings[1],operationStrings[1]));
		list.add(new CancelDepositBean(dateString[2], typeString[2],
				dealMoneyStrings[2], dealTypeStrings[2],operationStrings[2]));
		list.add(new CancelDepositBean(dateString[3], typeString[3],
				dealMoneyStrings[3], dealTypeStrings[3],operationStrings[3]));
		list.add(new CancelDepositBean(dateString[0], typeString[0],
				dealMoneyStrings[0], dealTypeStrings[0],operationStrings[0]));
		list.add(new CancelDepositBean(dateString[1], typeString[1],
				dealMoneyStrings[1], dealTypeStrings[1],operationStrings[1]));
		list.add(new CancelDepositBean(dateString[2], typeString[2],
				dealMoneyStrings[2], dealTypeStrings[2],operationStrings[2]));
		list.add(new CancelDepositBean(dateString[3], typeString[3],
				dealMoneyStrings[3], dealTypeStrings[3],operationStrings[3]));
		list.add(new CancelDepositBean(dateString[0], typeString[0],
				dealMoneyStrings[0], dealTypeStrings[0],operationStrings[0]));
		list.add(new CancelDepositBean(dateString[1], typeString[1],
				dealMoneyStrings[1], dealTypeStrings[1],operationStrings[1]));
		list.add(new CancelDepositBean(dateString[2], typeString[2],
				dealMoneyStrings[2], dealTypeStrings[2],operationStrings[2]));
		list.add(new CancelDepositBean(dateString[3], typeString[3],
				dealMoneyStrings[3], dealTypeStrings[3],operationStrings[3]));
		
		CancelDepositAdapter.MyListener myListener  = new CancelDepositAdapter.MyListener() {
			@Override
			public void handle(int tag) {
				// TODO Auto-generated method stub
				list.get(tag).setOperationString("" + tag);
				((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
			}
		};
		
		CancelDepositAdapter dealFlowAdapter = new CancelDepositAdapter(
				getApplicationContext(),list, myListener);

		listView.setAdapter(dealFlowAdapter);
		
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(getApplicationContext(), "我是第"+position + "项,你按的我好疼", Toast.LENGTH_SHORT).show();
			}
		});
	}

}
