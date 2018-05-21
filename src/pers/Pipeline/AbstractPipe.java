package pers.Pipeline;

import java.util.concurrent.TimeUnit;

/**
 * Pipe�ĳ���ʵ����
 * �������������doProcess����������Ԫ�ؽ��д���������Ӧ�������Ϊ��һ��Pipeʵ�������롣
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
	 * ��������ʵ�֡���������ʵ�����������߼���
	 * 
	 * @param input ��������Ԫ��
	 * @return ��������
	 * @throws PipeException
	 */
	protected abstract OUT doProcess(IN input) throws PipeException;

}
