package com.qskobe24.mylistview.request;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import com.guosen.pageandrefreshlistview.PageAndRefreshRequestCallBack;
import com.guosen.pageandrefreshlistview.PageAndRefreshRequestService;
import com.qskobe24.mylistview.bean.AllEarningBean;

@SuppressLint("HandlerLeak")
public class DemoRequest implements PageAndRefreshRequestService {
	
	private PageAndRefreshRequestCallBack mCallBack;
	@SuppressWarnings("unused")
	private int page = 0;
	
	@Override
	public void sendRequest(int page, PageAndRefreshRequestCallBack listener) {
		// TODO Auto-generated method stub
		if(mCallBack == null){
			mCallBack = listener;
		}
		this.page = page;
		
		new Thread(r).start();
	}
	
	private Handler handler = new Handler(){
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 模拟请求结果，一次成功，一次失败，循环
			case 1:
				  mCallBack.onRequestComplete((List<AllEarningBean>) msg.obj, 20);
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	
	private Runnable r = new Runnable() {
		@Override
		public void run() {
			try {
				List<AllEarningBean> data = new ArrayList<AllEarningBean>();
				// 由于请求是异步的，延时2s，表示请求过程有2s延时
				Thread.sleep(2000);
				AllEarningBean allEarningBean = null;
				for (int i = 0; i < 20; i++) {
					int money = (int)(Math.random()*5000);
					int m = (int)(Math.random()*30);
					allEarningBean = new AllEarningBean("2015-07-16", String.valueOf(money), String.valueOf(m));
					data.add(allEarningBean);
				}
				// 将请求的结果数据抛给Handler消息队列，在UI线程中渲染ListView视图
				handler.sendMessage(handler.obtainMessage(1, data));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	};


}
