package wukeslyOS.callback;

import java.util.List;

public interface LoadCallBack {
	/**
	 * 加载前预处理,运行在主线程
	 */
	public void loadBefore();
	/**
	 * 加载实现，运行在子线程，在该方法中可以根据加载进度，跟新进程
	 */
	public List doLoad(StatusCallBack statusCallBack);
	/**
	 * 加载完成，后续处理在主线程,
	 */
	public void loadAfter(List results);

}
