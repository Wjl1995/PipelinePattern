package pers.Pipeline;

/**
 * �Ը�������׶εļ��㻷�����г�����Ҫ�����쳣����
 * 
 * @author WJL
 *
 */
public interface PipeContext {

	/**
	 * ���ڶԴ���׶��׳����쳣���д���
	 * @param exp
	 */
	void handleError(PipeException exp);
}
