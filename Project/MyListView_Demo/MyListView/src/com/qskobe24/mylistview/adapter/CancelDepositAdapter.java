package com.qskobe24.mylistview.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qskobe24.mylistview.R;
import com.qskobe24.mylistview.bean.CancelDepositBean;

public class CancelDepositAdapter extends BaseAdapter  {
	private List<CancelDepositBean> beans;
	private LayoutInflater mInflater;
	private MyListener m_listener;
	private OnClickListener m_clickListener;
	public CancelDepositAdapter(Context context,List<CancelDepositBean> beans,MyListener listener ) {
		this.mInflater = LayoutInflater.from(context);
		this.beans = beans;
		m_listener = listener;
		m_clickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				Object obj = v.getTag();
				if (obj instanceof Integer) {
					int count = ((Integer) obj).intValue();
					if (m_listener != null) {
						m_listener.handle(count);
					}
				}
			}
		};
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
		final ViewHolder viewHolder;
		if(convertView == null){
			 convertView = mInflater.inflate(R.layout.list_item_cancel_deposit,parent,false);
			 
			 viewHolder = new ViewHolder();
			 viewHolder.lefTopTextView = (TextView)convertView.findViewById(R.id.canceldeposit_item_lefttop_textview);
			 viewHolder.lefBottomTextView = (TextView)convertView.findViewById(R.id.canceldeposit_item_leftbottom_textview);
			 viewHolder.centerTextView = (TextView)convertView.findViewById(R.id.canceldeposit_item_center_textview);
			 viewHolder.rightTextView = (TextView)convertView.findViewById(R.id.canceldeposit_item_right_textview);
			 viewHolder.container  = (LinearLayout)convertView.findViewById(R.id.canceldeposit_item_rl);
			 viewHolder.rightButton = (Button)convertView.findViewById(R.id.canceldeposit_item_right_button);
			 viewHolder.rightButton.setTag(position);
			 viewHolder.rightButton.setOnClickListener(m_clickListener);
			 convertView.setTag(viewHolder);
			 
		 }else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.lefTopTextView.setText(beans.get(position).getTypeString());
		viewHolder.lefBottomTextView.setText(beans.get(position).getDateString());
		viewHolder.centerTextView.setText(beans.get(position).getMoneyString());
		viewHolder.rightTextView.setText(beans.get(position).getStatusString());
		viewHolder.rightButton.setText(beans.get(position).getOperationString());
		
		 if(position % 2 == 0){
			 viewHolder.container.setBackgroundResource(R.color.canceldeposit_odditem_bg);
		 }else if(position % 2 == 1){
			 viewHolder.container.setBackgroundResource(R.color.canceldeposit_evenitem_bg);
		}
		 
		 return convertView;
	}

	
	
	class ViewHolder{
		private LinearLayout container;
		private TextView lefTopTextView;
		private TextView lefBottomTextView;
		private TextView centerTextView;
		private TextView rightTextView;
		private Button   rightButton;
	}

	public static interface MyListener {
		public void handle(int tag);
	}
	
}
