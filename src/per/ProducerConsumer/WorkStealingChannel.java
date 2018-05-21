package per.ProducerConsumer;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

/**
 * 类名：WorkStealingChannel<T>
 * 功能：实现通道的对队列形式，并采取工作窃取算法减少线程闲置提高线程利用率。
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
	 * 根据hash值存入响应队列
	 */
	@Override
	public void put(T product) throws InterruptedException {
		// TODO Auto-generated method stub
		int targetIndex = (product.hashCode() % managedQueues.length);
		BlockingQueue<T> targetQueue = managedQueues[targetIndex];
		targetQueue.put(product);
	}

	/**
	 * 工作窃取方法。
	 * 优先从对应队列取值，但如果当前队列任务已被处理完毕，
	 * 		则：优先从其他队列队尾取值，若仍取不到；
	 * 		则随机从其他队列取值，此时若该队列为空，则线程阻塞，直至该队列有值入队。
	 */
	@Override
	public T take(BlockingDeque<T> preferredQueue) throws InterruptedException {
		// TODO Auto-generated method stub
		
		// 优先从指定的受管队列中取产品
		BlockingDeque<T> targetQueue = preferredQueue;
		T product = null;
		
		System.out.println("正在取值");
		
		// 试图从指定的队列队首取产品
		if (null != targetQueue)
			product = targetQueue.poll();
		
		int queueIndex = -1;
		
		while (null == product)
		{
			queueIndex = (queueIndex + 1) % managedQueues.length;
			targetQueue = managedQueues[queueIndex];
			// 试图从其他受管队列的队尾窃取产品
			product = targetQueue.pollLast();
			if (targetQueue == preferredQueue)
				break;
		}
		
		if (null == product)
		{
			// 随机窃取其他受管队列的产品
			queueIndex = (int)(System.currentTimeMillis() % managedQueues.length);
			targetQueue = managedQueues[queueIndex];
			
			product = targetQueue.takeLast();
		}
		System.out.println("取值完毕");
		
		return product;
	}

}
