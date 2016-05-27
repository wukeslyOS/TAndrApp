package wukeslyOS.app;

import java.util.List;
import com.wukeslyOS.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import wukeslyOS.callback.LoadCallBack;
import wukeslyOS.callback.StatusCallBack;
import wukeslyOS.os.AsyncContanst;
import wukeslyOS.os.LoadAsyncTask;

/**
 * 一个Activity模板,该Activity提供异步加载加载数据显示。数据源包括：网络、数据库，文件系统等。
 * 
 * @author wuxiangli
 *
 */
public abstract class TLoadingActivity extends Activity {

	/**
	 * 该handler运行欲子线程，可以用过post方法向该handler提交比较耗时的runnable。
	 */
	protected Handler mExpensiveOpHandler;
	private HandlerThread mHandlerThread;
	private LoadAsyncTask mLoadAsyncTask;
	private ProgressDialog mLoadingDialog;
	private boolean loadingDialogEnable = true;
	
	private LoadCallBack mLoadCallBack = new LoadCallBack() {
		
		@Override
		public void loadBefore() {
			// TODO 自动生成的方法存根
			preLoad();
		}
		
		@Override
		public List doLoad(StatusCallBack statusCallBack) {
			// TODO 自动生成的方法存根
			List results = loadData(statusCallBack);
			if(mLoadingDialog != null && mLoadingDialog.isShowing()){
				mLoadingDialog.dismiss();
			}
			return results;
		}
		
		@Override
		public void loadAfter(List results) {
			// TODO 自动生成的方法存根
			loaded(results);
			mHandler.removeMessages(MSG_UPDATE_VIEW);
			Message msg = mHandler.obtainMessage(MSG_UPDATE_VIEW);
			msg.obj = results;
			mHandler.sendMessage(msg);
		}
	};
	
	private final int MSG_UPDATE_VIEW = 0;
	private final int MSG_LOAD_DATA = 1;
	private final int MSG_UPDATE_PROGRESS = 2;
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO 自动生成的方法存根
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_UPDATE_VIEW:
				updateView((List<Object>)msg.obj);
				break;
			case MSG_LOAD_DATA:
				if (Build.VERSION.SDK_INT < 14) {
		            mLoadAsyncTask.execute(new String[1]);
		        } else {
		            mLoadAsyncTask.executeOnExecutor(AsyncContanst.THREAD_POOL_EXECUTOR, new String[1]);
		        }
				break;
			case MSG_UPDATE_PROGRESS:
				updateProgress(msg.arg1, msg.arg2);
				break;
			default:
				break;
			}
		}

	};
	
	/**
	 * 数据加载完成后更新界面。
	 * @param result 结果
	 */
	public abstract void updateView(List results);
	
	/**
	 * 加载数据的函数，里面可以填写耗时的数据加载操作,
	 * 可以通过{@link #reportProgress(int, int)}通知跟新加载进度
	 * @param statusCallBack 获取数据加载线程的状态，在数据加载过程需要频繁
	 * 使用该回调检查加载线程的状态，判断是否需要提前退出。
	 * @return 加载的结果
	 */
	public abstract List<Object> loadData(StatusCallBack statusCallBack);
	
	public abstract void preLoad();
	
	/**
	 * 用于报告当前数据加载进度
	 * @param duration 进度的总长
	 * @param pos 当前更新到的位置
	 */
	public void reportProgress(int duration,int pos){
		Log.d("wxl", "报告进度：d="+duration +"   ,pos = "+pos);
		Message msg = mHandler.obtainMessage(MSG_UPDATE_PROGRESS);
		msg.arg1 = duration;
		msg.arg2 = pos;
		mHandler.sendMessage(msg);
	}
	
	/**
	 * 用于更新数据加载进度，运行于主线程
	 * @param duration 进度的总长
	 * @param pos  当前更新到的位置
	 */
	public void updateProgress(int duration,int pos){
		
		if(mLoadingDialog != null && !mLoadingDialog.isShowing()){
			mLoadingDialog.show();
		}
		if(loadingDialogEnable && mLoadingDialog != null){
			Log.d("wxl", "更新进度：d="+duration +"   ,pos = "+pos);
			if(mLoadingDialog.getMax() != duration)
				mLoadingDialog.setMax(duration);
			mLoadingDialog.setProgress(pos);
		}
	}
	
	/**
	 * 子类重写该方法时，无需在调用updateView().
	 * 该方法不需要涉及ui操作，相关UI操作只需添加在updateView中即可
	 */
	public abstract void loaded(List results);

	private void init() {
		mHandlerThread = new HandlerThread("TLoadingActivity_expentiveOp_handlerThread");
		//handlerThread.quitSafely();
		mHandlerThread.start();
		mExpensiveOpHandler = new Handler(mHandlerThread.getLooper());
		mLoadAsyncTask = new LoadAsyncTask(mLoadCallBack);
		
	}
	
	private void initDialog(){
		mLoadingDialog = new ProgressDialog(TLoadingActivity.this);
		mLoadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);   
		mLoadingDialog.setMessage("Loading...");  
		mLoadingDialog.setIndeterminate(true);
		mLoadingDialog.setCancelable(true);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_loading);
		init();
	}
	
	public void startLoading(boolean showDialog,boolean isIndeterminate,int ProgressStyle) {
		if(mLoadingDialog == null){
			initDialog();
		}
		if(mLoadingDialog != null && mLoadingDialog.isShowing()){
			mLoadingDialog.dismiss();
		}
		loadingDialogEnable = showDialog;
		if(loadingDialogEnable){
			mLoadingDialog.setProgressStyle(ProgressStyle);
			mLoadingDialog.setIndeterminate(isIndeterminate);
		}
		mHandler.sendEmptyMessage(MSG_LOAD_DATA);
	}

	@Override
	protected void onStart() {
		// TODO 自动生成的方法存根
		super.onStart();
	}

	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
		Log.d("wxl", "onDestroy");
		if(mExpensiveOpHandler != null){
			mExpensiveOpHandler.removeCallbacksAndMessages(null);
			mExpensiveOpHandler = null;
		}
		if(mHandlerThread != null && mHandlerThread.getLooper() != null){
			mHandlerThread.getLooper().quit();
			mHandlerThread = null;
		}
		if(mHandler != null){
			mHandler.removeCallbacksAndMessages(null);
			mHandler = null;
		}
		if(mLoadAsyncTask != null){
			mLoadAsyncTask.cancel(true);
			mLoadAsyncTask = null;
		}
		super.onDestroy();
	}
	
	
	
}
