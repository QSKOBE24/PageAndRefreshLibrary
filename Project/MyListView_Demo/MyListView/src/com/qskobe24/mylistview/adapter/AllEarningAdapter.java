package com.qskobe24.mylistview.adapter;

import java.util.ArrayList;
import java.util.List;

import com.qskobe24.mylistview.R;
import com.qskobe24.mylistview.bean.AllEarningBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AllEarningAdapter extends BaseAdapter {

	private List<AllEarningBean> beans = new ArrayList<AllEarningBean>();
	
	public List<AllEarningBean> getBeans() {
		return beans;
	}

	public void setBeans(List<AllEarningBean> beans) {
		this.beans = beans;
	}

	private LayoutInflater mInflater;

	public AllEarningAdapter(Context context) {
		this.mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return beans.size();
	}

	@Override
	public Object getItem(int position) {
		return beans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 ViewHolder viewHolder;
		 if(convertView == null){
			 convertView = mInflater.inflate(R.layout.list_item_allearning,parent,false);
			 
			 viewHolder = new ViewHolder();
			 viewHolder.lefTextView = (TextView)convertView.findViewById(R.id.allearning_item_left_textview);
			 viewHolder.centerTextView = (TextView)convertView.findViewById(R.id.allearning_item_center_textview);
			 viewHolder.rightTextView = (TextView)convertView.findViewById(R.id.allearning_item_right_textview);
			 viewHolder.container  = (LinearLayout)convertView.findViewById(R.id.allearning_item_rl);
			 
			 convertView.setTag(viewHolder);
			 
		 }else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		 if(position % 2 == 0){
			 viewHolder.container.setBackgroundResource(R.color.allearning_odditem_bg);
		 }else if(position % 2 == 1){
			 viewHolder.container.setBackgroundResource(R.color.allearning_evenitem_bg);
		}
		 
		 viewHolder.lefTextView.setText(getBeans().get(position).getDateString());
		 viewHolder.centerTextView.setText(getBeans().get(position).getAllMoneyString());
		 viewHolder.rightTextView.setText(getBeans().get(position).getEarningPerDayString());
		 
		 return convertView;
	}


	
	class ViewHolder{
		private LinearLayout container;
		private TextView lefTextView;
		private TextView centerTextView;
		private TextView rightTextView;
	}
}
