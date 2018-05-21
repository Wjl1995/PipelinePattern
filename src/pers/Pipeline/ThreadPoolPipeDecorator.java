package pers.Pipeline;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 基于线程池的Pipe实现类。
 * 
 * @author WJL
 *
 * @param <IN>
 * @param <OUT>
 */
public class ThreadPoolPipeDecorator<IN, OUT> implements Pipe<IN, OUT> {

	private final Pipe<IN, OUT> delegate;
	private final ExecutorService executorService;
	
	private final TerminationToken terminationToken;
	private final CountDownLatch stageProcessDoneLatch = new CountDownLatch(1);
	
	public ThreadPoolPipeDecorator(Pipe<IN, OUT> delegate, ExecutorService executorService)
	{
		this.delegate = delegate;
		this.executorService = executorService;
		this.terminationToken = TerminationToken.newInstance(executorService);
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
	}

	@Override
	public void shutdown(long timeout, TimeUnit unit) {
		// TODO Auto-generated method stub
		terminationToken.setIsToShutdown();
		if (terminationToken.reservation.get() > 0){
			try {
				if (stageProcessDoneLatch.getCount() > 0){
					stageProcessDoneLatch.await(timeout, unit);
				}
			} catch (InterruptedException e){
				;
			}
		}
	}

	@Override
	public void process(IN input) throws InterruptedException {
		// TODO Auto-generated method stub
		Runnable task = new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				int remainingReservations = -1;
				try{
					delegate.process(input);
				} catch (InterruptedException e){
					;
				} finally {
					remainingReservations = terminationToken.reservation.decrementAndGet();
				}
				if (terminationToken.isToShutdown() && 0 == remainingReservations)
					stageProcessDoneLatch.countDown();
			}
		};
		
		executorService.submit(task);
		terminationToken.reservation.incrementAndGet();
	}
	
	/**
	 * 线程池停止标志。
	 * 每个ExecutorService实例对应唯一一个TerminationToken实例。
	 * 
	 * @author WJL
	 *
	 */
	private static class TerminationToken extends per.ThreadTermination.TerminationToken{
		private final static ConcurrentMap<ExecutorService, TerminationToken> INSTANCE_MAP
			= new ConcurrentHashMap<ExecutorService, TerminationToken>();
		
		private TerminationToken(){
			
		}
		
		void setIsToShutdown()
		{
			this.toShutdown = true;
		}
		
		static TerminationToken newInstance(ExecutorService executorService)
		{
			TerminationToken token = INSTANCE_MAP.get(executorService);
			if (null == token)
			{
				token = new TerminationToken();
				TerminationToken existingToken = INSTANCE_MAP.putIfAbsent(executorService, token);
				if (null != existingToken)
					token = existingToken;
			}
			return token;
		}
		
	}

}
