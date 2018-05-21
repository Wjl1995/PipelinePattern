package pers.Pipeline;

/**
 * �Ը���Pipe�ĳ���һ��Pipelineʵ���ɰ������Pipeʵ����
 * 
 * @author WJL
 *
 * @param <IN>
 * @param <OUT>
 */
public interface Pipeline<IN, OUT> extends Pipe<IN, OUT> {

	/**
	 * ����Pipelineʵ�������һ��Pipeʵ����
	 * 
	 * @param pipe
	 */
	void addPipe(Pipe<?, ?> pipe);
}
