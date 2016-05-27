package wukeslyOS.os;

import java.util.List;
import android.os.AsyncTask;
import wukeslyOS.callback.LoadCallBack;
import wukeslyOS.callback.StatusCallBack;

public class LoadAsyncTask extends AsyncTask<String, Integer, List<Object>> implements StatusCallBack{
	
	LoadCallBack mCallBack;
	
	public LoadAsyncTask(LoadCallBack loadCallBack) {
		// TODO �Զ����ɵĹ��캯�����
		mCallBack = loadCallBack;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO �Զ����ɵķ������
		super.onPreExecute();
		if(mCallBack != null){
			mCallBack.loadBefore();
		}
	}

	@Override
	protected void onPostExecute(List<Object> result) {
		// TODO �Զ����ɵķ������
		super.onPostExecute(result);
		if(mCallBack != null){
			mCallBack.loadAfter(result);
		}
	}

	@Override
	protected List<Object> doInBackground(String... params) {
		// TODO �Զ����ɵķ������
		if(mCallBack != null){
			return mCallBack.doLoad(this);
		}
		return null;
	}
	
	

	@Override
	protected void onCancelled(List<Object> result) {
		// TODO �Զ����ɵķ������
		super.onCancelled(result);
	}


	@Override
	public boolean isCancel() {
		// TODO �Զ����ɵķ������
		return isCancelled();
	}
	

}
