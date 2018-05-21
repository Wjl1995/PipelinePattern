package pers.PipelineModel;

import java.awt.Color;
import java.awt.Point;
import java.util.concurrent.TimeUnit;

import per.ThreadTermination.AbstractTerminatableThread;
import per.ThreadTermination.TerminationToken;
import pers.Pipeline.SimplePipeline;
import pers.PipelineCE.PipeModel;
import pers.PipelineCE.ProductStorage;

public class ProcessPipeline extends AbstractTerminatableThread {

	private SimplePipeline<Product, Product> simplePipeline;
	private ProductStorage storage;
	private ObjectInfo state;
	private boolean suspended = false;
	private PipeModel pipeModel;
	
	public ProcessPipeline(SimplePipeline<Product, Product> simplePipeline, ProductStorage storage,
			TerminationToken token, PipeModel pipeModel)
	{
		super(token);
		this.simplePipeline = simplePipeline;
		this.storage = storage;
		this.pipeModel = pipeModel;
		state = new ObjectInfo();
	}

	public void shutdown(long timeout, TimeUnit unit) throws InterruptedException {
		// TODO Auto-generated method stub
		simplePipeline.shutdown(timeout, unit);
		Thread.sleep(unit.toMillis(timeout));
		this.terminate();
	}
	
	@Override
	protected void doRun() throws Exception {
		// TODO Auto-generated method stub
		
		synchronized(this) {
			while(suspended) {
				wait();
		    }
		}
		Product product = storage.takeProduct();
		if (product == null)
		{
			state.setInfo("正在等待");
			state.setColor(Color.RED);
			return;
		}

		int endX = (int)pipeModel.getBounds().getCenterX();
		int endY = (int)pipeModel.getBounds().getCenterY();
		product.moveTo(new Point(endX, endY), 500);
		terminationToken.reservation.decrementAndGet();
		state.setInfo("正在运行");
		state.setColor(Color.GREEN);
		simplePipeline.process(product);
	}
	
	public ObjectInfo getPlatState()
	{
		return state;
	}
	
	public void suspendPipe()
	{
		this.suspended = true;
		state.setInfo("正在等待");
		state.setColor(Color.RED);
	}
	
	public synchronized  void resumePipe(){
	    suspended = false;
	    state.setInfo("正在运行");
		state.setColor(Color.GREEN);
	    notify();
	}
}
