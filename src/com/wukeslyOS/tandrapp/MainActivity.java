package com.wukeslyOS.tandrapp;

import java.util.List;

import com.wukeslyOS.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import wukeslyOS.app.TLoadingActivity;
import wukeslyOS.callback.StatusCallBack;

public class MainActivity extends TLoadingActivity {

	TextView tv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv = (TextView) findViewById(R.id.tv);
		startLoading(true, false,ProgressDialog.STYLE_HORIZONTAL);
	}

	@Override
	public void updateView(List result) {
		// TODO 自动生成的方法存根
		Log.d("wxl", "updateView");
	}

	@Override
	public List<Object> loadData(StatusCallBack statusCallBack) {
		// TODO 自动生成的方法存根
		for(int i = 0;i<100 && !statusCallBack.isCancel();i++){
			//Log.d("wxl", isCancel +"");
			reportProgress(100, i+1);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		return null;
	}

	private Runnable mRunnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO 自动生成的方法存根
			for(int i=0;i<60;i++){
				try {
					Log.d("wxl", "run");
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				
			}
		}
	};
	
	@Override
	public void updateProgress(int duration, int pos) {
		// TODO 自动生成的方法存根
		super.updateProgress(duration, pos);
		String msg = "总进度="+duration +"  ，当前进度 = "+pos;
		tv.setText(msg);
	}

	@Override
	public void preLoad() {
		// TODO 自动生成的方法存根
		Log.d("wxl", "preLoad");
		mExpensiveOpHandler.post(mRunnable);
	}

	@Override
	public void loaded(List results) {
		// TODO 自动生成的方法存根
		Log.d("wxl", "loaded");
	}
}
