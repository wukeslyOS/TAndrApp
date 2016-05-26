package wukeslyOS.app;

import com.wukeslyOS.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class LoadingActivity extends Activity {
	
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO 自动生成的方法存根
			super.handleMessage(msg);
		}
		
	};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
	}
}
