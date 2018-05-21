package pers.Pipeline;

import java.util.concurrent.TimeUnit;

/**
 * �Դ���׶εĳ���
 * �����������д������������Ϊ��һ������׶ε�����
 * 
 * @author WJL
 *
 * @param <IN>
 * @param <OUT>
 */
public interface Pipe<IN, OUT> {

	/**
	 * ���õ�ǰPipeʵ������һ��Pipeʵ��
	 * @param nextPipe
	 */
	void setNextPipe(Pipe<?, ?> nextPipe);
	
	/**
	 * ��ʼ����ǰPipeʵ�������ṩ�ķ���
	 * @param pipeCtx
	 */
	void init(PipeContext pipeCtx);
	
	/**
	 * ֹͣ��ǰPipeʵ�������ṩ�ķ���
	 * @param timeout
	 * @param unit
	 */
	void shutdown(long timeout, TimeUnit unit);
	
	/**
	 * ������Ԫ�ؽ��д���������������Ϊ��һ��Pipeʵ��������
	 * @param input
	 * @throws InterruptedException
	 */
	void process(IN input) throws InterruptedException;
	
}
