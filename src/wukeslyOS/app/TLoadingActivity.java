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
 * һ��Activityģ��,��Activity�ṩ�첽���ؼ���������ʾ������Դ���������硢���ݿ⣬�ļ�ϵͳ�ȡ�
 * 
 * @author wuxiangli
 *
 */
public abstract class TLoadingActivity extends Activity {

	/**
	 * ��handler���������̣߳������ù�post�������handler�ύ�ȽϺ�ʱ��runnable��
	 */
	protected Handler mExpensiveOpHandler;
	private HandlerThread mHandlerThread;
	private LoadAsyncTask mLoadAsyncTask;
	private ProgressDialog mLoadingDialog;
	private boolean loadingDialogEnable = true;
	
	private LoadCallBack mLoadCallBack = new LoadCallBack() {
		
		@Override
		public void loadBefore() {
			// TODO �Զ����ɵķ������
			preLoad();
		}
		
		@Override
		public List doLoad(StatusCallBack statusCallBack) {
			// TODO �Զ����ɵķ������
			List results = loadData(statusCallBack);
			if(mLoadingDialog != null && mLoadingDialog.isShowing()){
				mLoadingDialog.dismiss();
			}
			return results;
		}
		
		@Override
		public void loadAfter(List results) {
			// TODO �Զ����ɵķ������
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
			// TODO �Զ����ɵķ������
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
	 * ���ݼ�����ɺ���½��档
	 * @param result ���
	 */
	public abstract void updateView(List results);
	
	/**
	 * �������ݵĺ��������������д��ʱ�����ݼ��ز���,
	 * ����ͨ��{@link #reportProgress(int, int)}֪ͨ���¼��ؽ���
	 * @param statusCallBack ��ȡ���ݼ����̵߳�״̬�������ݼ��ع�����ҪƵ��
	 * ʹ�øûص��������̵߳�״̬���ж��Ƿ���Ҫ��ǰ�˳���
	 * @return ���صĽ��
	 */
	public abstract List<Object> loadData(StatusCallBack statusCallBack);
	
	public abstract void preLoad();
	
	/**
	 * ���ڱ��浱ǰ���ݼ��ؽ���
	 * @param duration ���ȵ��ܳ�
	 * @param pos ��ǰ���µ���λ��
	 */
	public void reportProgress(int duration,int pos){
		Log.d("wxl", "������ȣ�d="+duration +"   ,pos = "+pos);
		Message msg = mHandler.obtainMessage(MSG_UPDATE_PROGRESS);
		msg.arg1 = duration;
		msg.arg2 = pos;
		mHandler.sendMessage(msg);
	}
	
	/**
	 * ���ڸ������ݼ��ؽ��ȣ����������߳�
	 * @param duration ���ȵ��ܳ�
	 * @param pos  ��ǰ���µ���λ��
	 */
	public void updateProgress(int duration,int pos){
		
		if(mLoadingDialog != null && !mLoadingDialog.isShowing()){
			mLoadingDialog.show();
		}
		if(loadingDialogEnable && mLoadingDialog != null){
			Log.d("wxl", "���½��ȣ�d="+duration +"   ,pos = "+pos);
			if(mLoadingDialog.getMax() != duration)
				mLoadingDialog.setMax(duration);
			mLoadingDialog.setProgress(pos);
		}
	}
	
	/**
	 * ������д�÷���ʱ�������ڵ���updateView().
	 * �÷�������Ҫ�漰ui���������UI����ֻ�������updateView�м���
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
		// TODO �Զ����ɵķ������
		super.onStart();
	}

	@Override
	protected void onDestroy() {
		// TODO �Զ����ɵķ������
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
