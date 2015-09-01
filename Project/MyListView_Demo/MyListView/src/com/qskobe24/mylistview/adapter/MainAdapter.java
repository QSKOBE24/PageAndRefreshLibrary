package com.qskobe24.mylistview.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import com.qskobe24.mylistview.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MainAdapter extends BaseAdapter {

	private final LayoutInflater inflater;
	
	private List<String> items = new ArrayList<String>(Arrays.asList("存入薪金宝","存入成功","存入失败","薪金宝取出","取出成功","取出失败"));

	public MainAdapter(Context context) {
		inflater = LayoutInflater.from(context);
	}

	@Override public int getCount() {
		return items.size();
	}

	@Override public Object getItem(int position) {
		return items.get(position);
	}

	@Override public long getItemId(int position) {
		return position;
	}

	@Override public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView != null) {
		      holder = (ViewHolder) convertView.getTag();
		    } else {
		    	convertView = inflater.inflate(R.layout.list_item_main, parent, false);
		        holder = new ViewHolder();
		        holder.textView = (TextView)convertView.findViewById(R.id.item);
		        System.out.println(""+position);
		        convertView.setTag(holder);
		    }
		
		holder.textView.setText(items.get(position));
		
		return convertView;
	}
	
	static class ViewHolder {
		private  TextView textView;
	}

	
	
}
