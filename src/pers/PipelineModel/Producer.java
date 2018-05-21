package pers.PipelineModel;

import java.awt.Point;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

import javax.swing.JPanel;

import per.ThreadTermination.AbstractTerminatableThread;
import per.ThreadTermination.TerminationToken;
import pers.PipelineCE.*;

public class Producer extends AbstractTerminatableThread {
	
	private AbstractTransportBelt transportBelt;
	private ProductStorage storage;
	private TerminationToken productToken;
	private ArrayBlockingQueue<Product> allProduct;
	private JPanel playPanel;
	private ProductStorage finStorage;
	private int productCount = 0;
	private boolean suspended = false;
	
	public Producer(AbstractTransportBelt transportBelt, ProductStorage storage,
			TerminationToken token, ArrayBlockingQueue<Product> allProduct, JPanel playPanel, ProductStorage finStorage)
	{
		productToken = token;
		this.transportBelt = transportBelt;
		this.storage = storage;
		this.allProduct = allProduct;
		this.playPanel = playPanel;
		this.finStorage = finStorage;
	}
	
	@Override
	protected void doRun() throws Exception {
		// TODO Auto-generated method stub
		
		while (storage.isFull())
			;
		
		while (getProductCount() == 100)
		{
			if (finStorage.isFull())
			{
				// 清除
			
				Iterator<Product> it = finStorage.getAllProduct().iterator();
				while (it.hasNext())
				{
					Product pro = it.next();
					playPanel.remove(pro);
				}
				finStorage.clear();
				setProductCount(0);
				
				// 移到仓库
			}
		}
		
		Thread.sleep(200);
		synchronized(this) {
			while(suspended) {
				wait();
		    }
		}
		
		String randomString = RandomString.getInstance().createString(new Random(), 20);
		Point productStartPoint = transportBelt.getPath().getPoints().get(0);
		
		Product product = new Product(randomString, productStartPoint, ProductColor.BLACK);
		try {
			allProduct.add(product);
		} catch (IllegalStateException illE)
		{
			System.out.println("所有生产任务："+getProductCount()+"已完成，自动关闭");
			illE.printStackTrace();
			this.terminate();
		}
		playPanel.add(product);
		setProductCount(getProductCount() + 1);
		playPanel.setComponentZOrder(product, getProductCount());
		product.moveBy(transportBelt, storage);
		productToken.reservation.incrementAndGet();
	}

	public int getProductCount() {
		return productCount;
	}

	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}
	
	public void suspendProducer()
	{
		this.suspended = true;
	}
	
	public synchronized  void resumeProducer(){
	    suspended = false;
	    notify();
	}
}
