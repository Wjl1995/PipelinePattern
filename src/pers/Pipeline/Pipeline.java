package pers.Pipeline;

/**
 * 对复合Pipe的抽象。一个Pipeline实例可包含多个Pipe实例。
 * 
 * @author WJL
 *
 * @param <IN>
 * @param <OUT>
 */
public interface Pipeline<IN, OUT> extends Pipe<IN, OUT> {

	/**
	 * 往该Pipeline实例中添加一个Pipe实例。
	 * 
	 * @param pipe
	 */
	void addPipe(Pipe<?, ?> pipe);
}
