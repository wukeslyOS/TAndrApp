package wukeslyOS.os;

import java.util.List;
import android.os.AsyncTask;
import wukeslyOS.callback.LoadCallBack;
import wukeslyOS.callback.StatusCallBack;

public class LoadAsyncTask extends AsyncTask<String, Integer, List<Object>> implements StatusCallBack{
	
	LoadCallBack mCallBack;
	
	public LoadAsyncTask(LoadCallBack loadCallBack) {
		// TODO 自动生成的构造函数存根
		mCallBack = loadCallBack;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO 自动生成的方法存根
		super.onPreExecute();
		if(mCallBack != null){
			mCallBack.loadBefore();
		}
	}

	@Override
	protected void onPostExecute(List<Object> result) {
		// TODO 自动生成的方法存根
		super.onPostExecute(result);
		if(mCallBack != null){
			mCallBack.loadAfter(result);
		}
	}

	@Override
	protected List<Object> doInBackground(String... params) {
		// TODO 自动生成的方法存根
		if(mCallBack != null){
			return mCallBack.doLoad(this);
		}
		return null;
	}
	
	

	@Override
	protected void onCancelled(List<Object> result) {
		// TODO 自动生成的方法存根
		super.onCancelled(result);
	}


	@Override
	public boolean isCancel() {
		// TODO 自动生成的方法存根
		return isCancelled();
	}
	

}
