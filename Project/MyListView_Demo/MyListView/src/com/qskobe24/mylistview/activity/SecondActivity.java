package com.qskobe24.mylistview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.qskobe24.mylistview.R;
import com.qskobe24.mylistview.view.ToggleButton;
import com.qskobe24.mylistview.view.ToggleButton.OnToggleChanged;

public class SecondActivity extends Activity {
 
	private ToggleButton toggleButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deposit);
        toggleButton  = (ToggleButton)findViewById(R.id.activity_deposit_togglebutton);
        toggleButton.setOnToggleChanged(new OnToggleChanged() {
			@Override
			public void onToggle(boolean on) {
				// TODO Auto-generated method stub
				if(on){
					Toast.makeText(getApplicationContext(), "开关打开", Toast.LENGTH_SHORT).show();
				}else {
					Toast.makeText(getApplicationContext(), "开关关闭", Toast.LENGTH_SHORT).show();
				}
			}
		});
        
	}
}
