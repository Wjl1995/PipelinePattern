package per.ProducerConsumer;

public interface Channel<P> {

	/**
	 * ��ͨ����ȡ��һ����Ʒ
	 * @return "��Ʒ"
	 * @throws InterruptedException
	 */
	P take() throws InterruptedException;
	
	/**
	 * ��ͨ���д洢һ����Ʒ
	 * @param product
	 * @throws InterruptedException
	 */
	void put (P product) throws InterruptedException;
}
