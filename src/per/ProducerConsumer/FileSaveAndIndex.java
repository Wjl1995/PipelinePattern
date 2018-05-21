package per.ProducerConsumer;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import per.ThreadTermination.AbstractTerminatableThread;
import per.ThreadTermination.TerminationToken;

public class FileSaveAndIndex {

	private int consumerCount;
	private final WorkStealingEnabledChannel<String> channel;
	private final TerminationToken token = new TerminationToken();
	
	private final AttachmentProcess attachmentProcess = new AttachmentProcess();
	private final IndexingThread[] indexingThreads;
	
	public FileSaveAndIndex()
	{
		consumerCount = 3;
		@SuppressWarnings("unchecked")
		BlockingDeque<String>[] managedQueues = new LinkedBlockingDeque[consumerCount];
		channel = new WorkStealingChannel<String>(managedQueues);
		
		indexingThreads = new IndexingThread[consumerCount];
		for (int i=0; i<consumerCount; i++)
		{
			managedQueues[i] = new LinkedBlockingDeque<String>();
			indexingThreads[i] = new IndexingThread(token, managedQueues[i]);
		}
	}
	
	private class AttachmentProcess extends AbstractTerminatableThread{
		
		public void saveAttachment(String ii) throws Exception
		{
			try{
				channel.put(ii);
				token.reservation.incrementAndGet();
				Thread.sleep(50);
			}catch (InterruptedException e){;}
		}

		@Override
		protected void doRun() throws Exception {
			// TODO Auto-generated method stub
			try{
				saveAttachment(new String("ii"));
			}catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private class IndexingThread extends AbstractTerminatableThread{
		
		private final BlockingDeque<String> workQueue;
		
		public IndexingThread(TerminationToken token, BlockingDeque<String> workQueue)
		{
			super(token);
			this.workQueue = workQueue;
		}
		
		@Override
		protected void doRun() throws Exception {
			// TODO Auto-generated method stub
			String ii = channel.take(workQueue);
			try{
				indexFile(ii);
			}catch (Exception e){
				e.printStackTrace();
				System.out.println("take时抛出的异常");
			}
			finally{
				token.reservation.decrementAndGet();
			}
		}

		private void indexFile(String ii) {
			// TODO Auto-generated method stub
			try{
				Thread.sleep(200);
			}catch (InterruptedException e){;}
		}
	}
	
	public void doStart()
	{
		attachmentProcess.start();
		for (int i=0; i<consumerCount; i++)
			indexingThreads[i].start();
	}
	
	public void shutdown()
	{
		attachmentProcess.terminate();
		for (int i=0; i<consumerCount; i++)
			indexingThreads[i].terminate();
	}
	
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		FileSaveAndIndex fileSaveAndIndex = new FileSaveAndIndex();
		fileSaveAndIndex.doStart();
		Thread.sleep(500);
		fileSaveAndIndex.shutdown();
		System.out.println("发送停止指令时间："+System.currentTimeMillis());
		
		Thread.sleep(5000);
	}

}
