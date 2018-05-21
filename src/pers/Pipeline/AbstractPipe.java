package pers.Pipeline;

import java.util.concurrent.TimeUnit;

/**
 * Pipe的抽象实现类
 * 此类会调用子类的doProcess方法对输入元素进行处理，并向相应的输出作为下一个Pipe实例的输入。
 * 
 * @author WJL
 *
 * @param <IN>
 * @param <OUT>
 */
public abstract class AbstractPipe<IN, OUT> implements Pipe<IN, OUT> {

	protected volatile Pipe<?, ?> nextPipe = null;
	protected volatile PipeContext pipeCtx;
	
	@Override
	public void setNextPipe(Pipe<?, ?> nextPipe) {
		// TODO Auto-generated method stub
		this.nextPipe = nextPipe;
	}

	@Override
	public void init(PipeContext pipeCtx) {
		// TODO Auto-generated method stub
		this.pipeCtx = pipeCtx;
	}

	@Override
	public void shutdown(long timeout, TimeUnit unit) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	@Override
	public void process(IN input) throws InterruptedException {
		// TODO Auto-generated method stub
		try{
			OUT out = doProcess(input);
			if (null != nextPipe)
				if (null != out)
				{
					((Pipe<OUT, ?>)nextPipe).process(out);
				}
		}catch (InterruptedException e)
		{
			Thread.currentThread().interrupt();
		}catch (PipeException e)
		{
			pipeCtx.handleError(e);
		}
	}

	/**
	 * 留给子类实现。用于子类实现其任务处理逻辑。
	 * 
	 * @param input 任务输入元素
	 * @return 任务处理结果
	 * @throws PipeException
	 */
	protected abstract OUT doProcess(IN input) throws PipeException;

}
