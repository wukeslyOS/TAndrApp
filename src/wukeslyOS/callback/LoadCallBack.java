package wukeslyOS.callback;

import java.util.List;

public interface LoadCallBack {
	/**
	 * ����ǰԤ����,���������߳�
	 */
	public void loadBefore();
	/**
	 * ����ʵ�֣����������̣߳��ڸ÷����п��Ը��ݼ��ؽ��ȣ����½���
	 */
	public List doLoad(StatusCallBack statusCallBack);
	/**
	 * ������ɣ��������������߳�,
	 */
	public void loadAfter(List results);

}
