package com.qskobe24.mylistview.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guosen.pageandrefreshlistview.PageAndRefreshBaseAdapter;
import com.guosen.pageandrefreshlistview.PageAndRefreshRequestService;
import com.qskobe24.mylistview.R;
import com.qskobe24.mylistview.bean.AllEarningBean;

/**
 * 适配器示例<br> 
 * 继承 {@link PageAndRefreshBaseAdapter} 基类<br>
 * 由于已经对数据源做了封装，使用者只需要关注视图的处理
 * 
 * @author King
 */
@SuppressLint("InflateParams")
public class PageListViewAdapter extends PageAndRefreshBaseAdapter {

	private LayoutInflater mInflater;
	
	public PageListViewAdapter(Context context, PageAndRefreshRequestService requestService) {
		super(requestService);
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder = null;
		if(convertView == null || convertView.getTag() == null){
			convertView = mInflater.inflate(R.layout.list_item_allearning, null);
			viewHolder = new ViewHolder();
			
			 viewHolder.lefTextView = (TextView)convertView.findViewById(R.id.allearning_item_left_textview);
			 viewHolder.centerTextView = (TextView)convertView.findViewById(R.id.allearning_item_center_textview);
			 viewHolder.rightTextView = (TextView)convertView.findViewById(R.id.allearning_item_right_textview);
			 viewHolder.container  = (LinearLayout)convertView.findViewById(R.id.allearning_item_rl);
			 
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		 AllEarningBean allEarningBean = (AllEarningBean)getItem(position);
		 
		 viewHolder.lefTextView.setText(allEarningBean.getDateString());
		 viewHolder.centerTextView.setText(allEarningBean.getAllMoneyString());
		 viewHolder.rightTextView.setText(allEarningBean.getEarningPerDayString());
		 
		 if(position % 2 == 0){
			 viewHolder.container.setBackgroundResource(R.color.allearning_odditem_bg);
		 }else if(position % 2 == 1){
			 viewHolder.container.setBackgroundResource(R.color.allearning_evenitem_bg);
		}
		 
		
		return convertView;
	}

	private static class ViewHolder{
		 LinearLayout container;
		 TextView lefTextView;
		 TextView centerTextView;
		 TextView rightTextView;
	}

}
