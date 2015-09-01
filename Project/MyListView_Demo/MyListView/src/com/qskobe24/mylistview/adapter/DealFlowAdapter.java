package com.qskobe24.mylistview.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qskobe24.mylistview.R;
import com.qskobe24.mylistview.bean.DealFlowBean;

public class DealFlowAdapter extends BaseAdapter {

	private List<DealFlowBean> beans = new ArrayList<DealFlowBean>();
	
	
	public List<DealFlowBean> getBeans() {
		return beans;
	}

	public void setBeans(List<DealFlowBean> beans) {
		this.beans = beans;
	}

	private LayoutInflater mInflater;

	public DealFlowAdapter(Context context) {
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return beans.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return beans.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if(convertView == null){
			 convertView = mInflater.inflate(R.layout.list_item_deal_flow,parent,false);
			 
			 viewHolder = new ViewHolder();
			 viewHolder.lefTopTextView = (TextView)convertView.findViewById(R.id.dealflow_item_lefttop_textview);
			 viewHolder.lefBottomTextView = (TextView)convertView.findViewById(R.id.dealflow_item_leftbottom_textview);
			 viewHolder.centerTextView = (TextView)convertView.findViewById(R.id.dealflow_item_center_textview);
			 viewHolder.rightTextView = (TextView)convertView.findViewById(R.id.dealflow_item_right_textview);
			 viewHolder.container  = (LinearLayout)convertView.findViewById(R.id.dealflow_item_rl);
			 
			 convertView.setTag(viewHolder);
			 
		 }else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.lefTopTextView .setText(getBeans().get(position).getDealTypeString());
		viewHolder.lefBottomTextView .setText(getBeans().get(position).getDateString());
		viewHolder.centerTextView .setText(getBeans().get(position).getDealMoneyString());
		viewHolder.rightTextView .setText(getBeans().get(position).getDealStatusString());
		
		 if(position % 2 == 0){
			 viewHolder.container.setBackgroundResource(R.color.dealflow_odditem_bg);
		 }else if(position % 2 == 1){
			 viewHolder.container.setBackgroundResource(R.color.dealflow_evenitem_bg);
		}
		 
		 return convertView;
	}
	
	
	class ViewHolder{
		private LinearLayout container;
		private TextView lefTopTextView;
		private TextView lefBottomTextView;
		private TextView centerTextView;
		private TextView rightTextView;
	}

}
