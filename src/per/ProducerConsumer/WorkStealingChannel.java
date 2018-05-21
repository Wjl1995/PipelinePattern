package per.ProducerConsumer;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

/**
 * ������WorkStealingChannel<T>
 * ���ܣ�ʵ��ͨ���ĶԶ�����ʽ������ȡ������ȡ�㷨�����߳���������߳������ʡ�
 * 
 * @author WJL
 * @param <T>
 */
public class WorkStealingChannel<T> implements WorkStealingEnabledChannel<T> {

	private final BlockingDeque<T>[] managedQueues;
	
	public WorkStealingChannel(BlockingDeque<T>[] managedQueues)
	{
		this.managedQueues = managedQueues;
	}
	
	@Override
	public T take() throws InterruptedException {
		// TODO Auto-generated method stub
		return take(null);
	}

	/**
	 * ����hashֵ������Ӧ����
	 */
	@Override
	public void put(T product) throws InterruptedException {
		// TODO Auto-generated method stub
		int targetIndex = (product.hashCode() % managedQueues.length);
		BlockingQueue<T> targetQueue = managedQueues[targetIndex];
		targetQueue.put(product);
	}

	/**
	 * ������ȡ������
	 * ���ȴӶ�Ӧ����ȡֵ���������ǰ���������ѱ�������ϣ�
	 * 		�����ȴ��������ж�βȡֵ������ȡ������
	 * 		���������������ȡֵ����ʱ���ö���Ϊ�գ����߳�������ֱ���ö�����ֵ��ӡ�
	 */
	@Override
	public T take(BlockingDeque<T> preferredQueue) throws InterruptedException {
		// TODO Auto-generated method stub
		
		// ���ȴ�ָ�����ܹܶ�����ȡ��Ʒ
		BlockingDeque<T> targetQueue = preferredQueue;
		T product = null;
		
		System.out.println("����ȡֵ");
		
		// ��ͼ��ָ���Ķ��ж���ȡ��Ʒ
		if (null != targetQueue)
			product = targetQueue.poll();
		
		int queueIndex = -1;
		
		while (null == product)
		{
			queueIndex = (queueIndex + 1) % managedQueues.length;
			targetQueue = managedQueues[queueIndex];
			// ��ͼ�������ܹܶ��еĶ�β��ȡ��Ʒ
			product = targetQueue.pollLast();
			if (targetQueue == preferredQueue)
				break;
		}
		
		if (null == product)
		{
			// �����ȡ�����ܹܶ��еĲ�Ʒ
			queueIndex = (int)(System.currentTimeMillis() % managedQueues.length);
			targetQueue = managedQueues[queueIndex];
			
			product = targetQueue.takeLast();
		}
		System.out.println("ȡֵ���");
		
		return product;
	}

}
