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
		// TODO �Զ����ɵķ������
		Log.d("wxl", "updateView");
	}

	@Override
	public List<Object> loadData(StatusCallBack statusCallBack) {
		// TODO �Զ����ɵķ������
		for(int i = 0;i<100 && !statusCallBack.isCancel();i++){
			//Log.d("wxl", isCancel +"");
			reportProgress(100, i+1);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		}
		return null;
	}

	private Runnable mRunnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO �Զ����ɵķ������
			for(int i=0;i<60;i++){
				try {
					Log.d("wxl", "run");
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
				
			}
		}
	};
	
	@Override
	public void updateProgress(int duration, int pos) {
		// TODO �Զ����ɵķ������
		super.updateProgress(duration, pos);
		String msg = "�ܽ���="+duration +"  ����ǰ���� = "+pos;
		tv.setText(msg);
	}

	@Override
	public void preLoad() {
		// TODO �Զ����ɵķ������
		Log.d("wxl", "preLoad");
		mExpensiveOpHandler.post(mRunnable);
	}

	@Override
	public void loaded(List results) {
		// TODO �Զ����ɵķ������
		Log.d("wxl", "loaded");
	}
}
