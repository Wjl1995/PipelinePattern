package pers.Pipeline;

import per.ThreadTermination.*;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * 基于工作者线程的Pipe实现类。
 * 提交到此Pipe的任务由指定个数的工作者线程共同处理。
 * 此类允许在Pipeline的每个Pipe(处理阶段)，同时处理批量任务。
 * 
 * @author WJL
 *
 * @param <IN>
 * @param <OUT>
 */
public class WorkerThreadPipeDecorator<IN, OUT> implements Pipe<IN, OUT> {

	/** 任务队列  */
	protected final BlockingQueue<IN> workQueue;
	/** 任务标志  */
	private final TerminationToken terminationToken = new TerminationToken();
	/** 工作者线程  */
	private final Set<AbstractTerminatableThread> workerThreads = new HashSet<AbstractTerminatableThread>();
	/** 委托Pipe实例  */
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
