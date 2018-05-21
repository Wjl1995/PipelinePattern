package pers.PipelineCE;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.concurrent.ArrayBlockingQueue;

import javax.swing.JPanel;

import per.ThreadTermination.TerminationToken;
import pers.Pipeline.*;
import pers.PipelineModel.*;

public class PipelinePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private ArrayBlockingQueue<Product> allProducts;
	private ProducerModel producerModel;
	private LineTransportBelt producerTransportBelt;
	private ProductStorage producerStorage;
	private PipeModel customerPlat1;
	private LineTransportBelt p1ToP2Belt;
	private PipeModel customerPlat2;
	private LineTransportBelt p2ToStorageBelt;
	private ProductStorage finProStorage;
	
	private TerminationToken productToken;
	
	public PipelinePanel()
	{
		allProducts = new ArrayBlockingQueue<Product>(200);
		productToken = new TerminationToken();
		creatPipeline();
	}
	
	private void creatPipeline()
	{
		producerModel = new ProducerModel();
		producerStorage = new ProductStorage();
		customerPlat1 = new PipeModel();
		customerPlat2 = new PipeModel();
		finProStorage = new ProductStorage();
		
		add(producerModel);
		add(producerStorage);
		add(customerPlat1);
		add(customerPlat2);
		add(finProStorage);
		setLayout(null);
		// setBackground(new Color(0, 112, 26));	// 卡其绿
		
		setBackground(new Color(255, 255, 255));	// 纯白色
		
		producerModel.setBounds(50, 600, 200, 200);
		producerStorage.setBounds(100, 300, 100, 100);
		customerPlat1.setBounds(100, 100, 100, 100);
		customerPlat2.setBounds(400, 100, 100, 100);
		finProStorage.setBounds(700, 100, 200, 200);
		
		Point startPoint1 = new Point(150, 600);
		Point endPoint1 = new Point(150, 400);
		producerTransportBelt = new LineTransportBelt(startPoint1, endPoint1, 2);
		ArrayBlockingQueue<Product> producerQueue = new ArrayBlockingQueue<Product>(20);
		producerStorage.init(producerQueue, 20, 5, "image/storage/storage1.png");
		Producer producer = new Producer(producerTransportBelt, producerStorage, productToken, allProducts, this, finProStorage);
		producerModel.initModel("image/producer/producer1.png", producer);
		
		Point startPoint2 = new Point(200, 150);
		Point endPoint2 = new Point(400, 150);
		p1ToP2Belt = new LineTransportBelt(startPoint2, endPoint2, 1);
		Pipe<Product, Product> pipe1 = new AbstractPipe<Product, Product>(){

			@Override
			protected Product doProcess(Product input) throws PipeException {
				// TODO Auto-generated method stub
				input.setImgIcon(ProductColor.RED);
				input.moveTo(startPoint2);
				input.moveBy(p1ToP2Belt);
				return input;
			}
			
		};
		Point startPoint3 = new Point(500, 150);
		Point endPoint3 = new Point(700, 150);
		p2ToStorageBelt = new LineTransportBelt(startPoint3, endPoint3, 1);
		ArrayBlockingQueue<Product> finQueue = new ArrayBlockingQueue<Product>(100);
		finProStorage.init(finQueue, 100, 10, "image/storage/storage2.png");
		Pipe<Product, Product> pipe2 = new AbstractPipe<Product, Product>(){

				@Override
				protected Product doProcess(Product input) throws PipeException {
					// TODO Auto-generated method stub
					input.moveTo(new Point((int)customerPlat2.getBounds().getCenterX(), 
							(int)customerPlat2.getBounds().getCenterY()));
					input.setImgIcon(ProductColor.GREEN);
					input.moveTo(startPoint3);
					input.moveBy(p2ToStorageBelt, finProStorage);
					return input;
				}
			
		};
		SimplePipeline<Product, Product> simplePipeline = new SimplePipeline<Product, Product>();
		pipe1.setNextPipe(pipe2);
		simplePipeline.addAsWorkerThreadBasedPipe(pipe1, 1);
		simplePipeline.addAsWorkerThreadBasedPipe(pipe2, 1);
		simplePipeline.init(simplePipeline.newDefaultPipeContext());
		ProcessPipeline processPipeline = new ProcessPipeline(simplePipeline, producerStorage, productToken, customerPlat1);
		customerPlat1.initModel("image/plat/plat1.png", processPipeline);
		customerPlat2.initModel("image/plat/plat2.png", processPipeline);
		
	}
	
	protected void paintComponent( Graphics g )
	{
		super.paintComponent(g);
		
		g.setFont(new Font("宋体",  Font.BOLD, 12));
		ObjectInfo info = customerPlat1.getStateInfo();
		g.setColor(info.getColor());
		g.drawString(info.getInfo(), customerPlat1.getX(), customerPlat1.getY()-10);
		info = producerModel.getStateInfo();
		g.setColor(info.getColor());
		g.drawString(info.getInfo(), producerModel.getX(), producerModel.getY()-10);
		info = producerStorage.getStateInfo();
		g.setColor(info.getColor());
		g.drawString(info.getInfo(), producerStorage.getX(), producerStorage.getY()-10);
		info = finProStorage.getStateInfo();
		g.setColor(info.getColor());
		g.drawString(info.getInfo(), finProStorage.getX(), finProStorage.getY()-10);
		
		g.drawString("生产总数："+allProducts.size(), 600, 600);
		
		g.setColor(Color.BLACK);
		producerTransportBelt.draw(g);
		p1ToP2Belt.draw(g);
		p2ToStorageBelt.draw(g);
	}

	public ArrayBlockingQueue<Product> getAllProducts() {
		return allProducts;
	}

	public void start()
	{
		producerModel.doProduce();
		customerPlat1.Process();
	}
	
	public void shutdown()
	{
		producerModel.shutdown();
		try{
			customerPlat1.shutdown();
		} catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
