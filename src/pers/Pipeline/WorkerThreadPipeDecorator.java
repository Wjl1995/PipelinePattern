package pers.Pipeline;

import per.ThreadTermination.*;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * ���ڹ������̵߳�Pipeʵ���ࡣ
 * �ύ����Pipe��������ָ�������Ĺ������̹߳�ͬ����
 * ����������Pipeline��ÿ��Pipe(����׶�)��ͬʱ������������
 * 
 * @author WJL
 *
 * @param <IN>
 * @param <OUT>
 */
public class WorkerThreadPipeDecorator<IN, OUT> implements Pipe<IN, OUT> {

	/** �������  */
	protected final BlockingQueue<IN> workQueue;
	/** �����־  */
	private final TerminationToken terminationToken = new TerminationToken();
	/** �������߳�  */
	private final Set<AbstractTerminatableThread> workerThreads = new HashSet<AbstractTerminatableThread>();
	/** ί��Pipeʵ��  */
	private final Pipe<IN, OUT> delegate;
	
	public WorkerThreadPipeDecorator(Pipe<IN, OUT> delegate, int workerCount)
	{
		this(new SynchronousQueue<IN>(), delegate, workerCount);
	}
	
	public WorkerThreadPipeDecorator(BlockingQueue<IN> workQueue, Pipe<IN, OUT> delegate, int workerCount)
	{
		if (workerCount <= 0)
			throw new IllegalArgumentException("workerCount can not be positive!");
		
		this.workQueue = workQueue;
		this.delegate = delegate;
		for(int i=0; i<workerCount; i++)
		{
			workerThreads.add(new AbstractTerminatableThread(terminationToken){

				@Override
				protected void doRun() throws Exception {
					// TODO Auto-generated method stub
					try{
						dispatch();
					}finally{
						terminationToken.reservation.decrementAndGet();
					}
				}		
			});
		}
	}
	
	protected void dispatch() throws InterruptedException{
		// TODO Auto-generated method stub
		IN input = workQueue.take();
		delegate.process(input);
	}
	
	@Override
	public void setNextPipe(Pipe<?, ?> nextPipe) {
		// TODO Auto-generated method stub
		delegate.setNextPipe(nextPipe);
	}

	@Override
	public void init(PipeContext pipeCtx) {
		// TODO Auto-generated method stub
		delegate.init(pipeCtx);
		for (AbstractTerminatableThread thread : workerThreads)
			thread.start();
	}

	@Override
	public void shutdown(long timeout, TimeUnit unit) {
		// TODO Auto-generated method stub
		for (AbstractTerminatableThread thread : workerThreads)
		{
			thread.terminate();
			try{
				thread.join(TimeUnit.MILLISECONDS.convert(timeout, unit));
			} catch (InterruptedException e){
			}
		}
		delegate.shutdown(timeout, unit);
	}

	@Override
	public void process(IN input) throws InterruptedException {
		// TODO Auto-generated method stub
		workQueue.put(input);
		terminationToken.reservation.incrementAndGet();
	}

}
