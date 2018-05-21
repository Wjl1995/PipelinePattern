package pers.Pipeline;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * ֧�ֲ��д����Pipeʵ���ࡣ
 * �����ÿ������Ԫ������һ�������񣬲��Բ��еķ�ʽȥִ����Щ������
 * �����������ִ�н���ᱻ�ϲ�Ϊ��Ӧ������Ԫ�ص���������
 * 
 * @author WJL
 *
 * @param <IN>
 * @param <OUT>
 * @param <V> ����������Ĵ���������
 */
public abstract class AbstractParallelPipe<IN, OUT, V> extends AbstractPipe<IN, OUT> {

	private final ExecutorService executorService;
	
	public AbstractParallelPipe(BlockingQueue<IN> queue, ExecutorService executorService)
	{
		super();
		this.executorService = executorService;
	}
	
	@Override
	protected OUT doProcess(IN input) throws PipeException {
		// TODO Auto-generated method stub
		OUT out = null;
		try{
			out = combineResults(invokeParallel(buildTasks(input)));
		} catch (Exception e){
			throw new PipeException(this, input, "Task failed", e);
		}
		return out;
	}
	
	/**
	 * ��������ʵ�֡����ڸ���ָ��������Ԫ��input����һ��������
	 * 
	 * @param input
	 * @return
	 * @throws Exception
	 */
	protected abstract List<Callable<V>> buildTasks(IN input) throws Exception;
	
	/**
	 * ��������ʵ�֡��Ը���������Ĵ��������кϲ����γ���Ӧ������Ԫ�ص���������
	 * 
	 * @param subTaskResults
	 * @return
	 * @throws Exception
	 */
	protected abstract OUT combineResults(List<Future<V>> subTaskResults) throws Exception;
	
	/**
	 * �Բ��еķ�ʽִ��һ��������
	 * 
	 * @param tasks
	 * @return
	 * @throws Exception
	 */
	protected List<Future<V>> invokeParallel(List<Callable<V>> tasks) throws Exception
	{
		return executorService.invokeAll(tasks);
	}

}
