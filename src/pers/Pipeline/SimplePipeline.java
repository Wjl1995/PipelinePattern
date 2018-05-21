package pers.Pipeline;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class SimplePipeline<IN, OUT> extends AbstractPipe<IN, OUT> implements Pipeline<IN, OUT> {

	private final Queue<Pipe<?, ?>> pipes = new LinkedList<Pipe<?, ?>>();
	private final ExecutorService helperExecutor;
	
	public SimplePipeline()
	{
		this(Executors.newSingleThreadExecutor(new ThreadFactory(){

			@Override
			public Thread newThread(Runnable r) {
				// TODO Auto-generated method stub
				Thread t = new Thread(r, "SimplePipeline-Helper");
				t.setDaemon(true);
				return t;
			}
			
		}));
	}
	
	public SimplePipeline(final ExecutorService newSingleThreadExecutor) {
		// TODO Auto-generated constructor stub
		super();
		this.helperExecutor = newSingleThreadExecutor;
	}

	@Override
	public void addPipe(Pipe<?, ?> pipe) {
		// TODO Auto-generated method stub
		pipes.add(pipe);
	}
	
	public <INPUT, OUTPUT> void addAsWorkerThreadBasedPipe(Pipe<INPUT, OUTPUT> delegate, int workerCount)
	{
		addPipe(new WorkerThreadPipeDecorator<INPUT, OUTPUT>(delegate, workerCount));
	}
	
	public <INPUT, OUTPUT> void addAsThreadPoolBasedPipe(Pipe<INPUT, OUTPUT> delegate, ExecutorService executorService)
	{
		addPipe(new ThreadPoolPipeDecorator<INPUT, OUTPUT>(delegate, executorService));
	}

	@Override
	protected OUT doProcess(IN input) throws PipeException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void shutdown(long timeout, TimeUnit unit){
		Pipe<?, ?> pipe;
		
		while (null != (pipe = pipes.poll()))
			pipe.shutdown(timeout, unit);
		
		helperExecutor.shutdown();
	}
	
	public void process(IN input) throws InterruptedException{
		@SuppressWarnings("unchecked")
		Pipe<IN, ?> firstPipe = (Pipe<IN, ?>) pipes.peek();
		
		firstPipe.process(input);
	}
	
	public void init(final PipeContext ctx){
		LinkedList<Pipe<?, ?>> pipesList = (LinkedList<Pipe<?, ?>>) pipes;
		Pipe<?, ?> prevPipe = this;
		for (Pipe<?, ?> pipe : pipesList){
			prevPipe.setNextPipe(pipe);
			prevPipe = pipe;
		}
		
		Runnable task = new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				for (Pipe<?, ?> pipe : pipes)
					pipe.init(ctx);
			}
			
		};
		helperExecutor.submit(task);
	}
	
	public PipeContext newDefaultPipeContext(){
		return new PipeContext(){

			@Override
			public void handleError(final PipeException exp) {
				// TODO Auto-generated method stub
				helperExecutor.submit(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						exp.printStackTrace();
					}
					
				});
			}
			
		};
	}

}
