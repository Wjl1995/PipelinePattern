package pers.PipelineCE;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import pers.PipelineModel.*;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ProductStorage extends JLabel {

	private static final long serialVersionUID = 1L;
	private ArrayBlockingQueue<Product> queue;
	private ArrayList<Point> positions;
	private ObjectInfo stateInfo;
	
	public void init(ArrayBlockingQueue<Product> queue, int size, int rowNum, String resource) {
		// TODO Auto-generated constructor stub
		this.queue = queue;
		positions = new ArrayList<Point>();
		Rectangle posRectangle = this.getBounds();
		int minX = (int)posRectangle.getMinX();
		int minY = (int)posRectangle.getMinY();
		int maxX = (int)posRectangle.getMaxX();
		int maxY = (int)posRectangle.getMaxY();
		for (int i=0; i<size; i++)
		{
			int X = 0;
			int numX = i % rowNum;

			X = minX + (maxX-minX)*(numX+1)/(rowNum+1);

			int Y = 0;
			int numY = i / rowNum;
			Y = maxY - (maxY-minY)*(numY+1)/(rowNum+1);
			Point point = new Point(X, Y);
			positions.add(point);
		}
		stateInfo = new ObjectInfo();
		this.setIcon(new ImageIcon(resource));
	}
	
	public void addProduct(Product product) throws IllegalStateException 
	{
		queue.add(product);
		productMoveOn();
	}
	
	public Product takeProduct() throws InterruptedException
	{
		Product product = queue.poll();
		productMoveOn();
		return product;
	}
	
	private void productMoveOn()
	{
		Iterator<Product> it = queue.iterator();
		int i = 0;
		while (it.hasNext())
		{
			Product product = it.next();
			Point point = positions.get(i);
			product.moveTo(point);
			i++;
		}
	}
	
	public int getProductNum()
	{
		return queue.size();
	}
	
	public boolean isFull(){
		if (queue.size() == positions.size())
			return true;
		else
			return false;
	}
	
	public String toString()
	{
		String str = "";
		Iterator<Product> it = queue.iterator();
		while (it.hasNext())
		{
			Product product = it.next();
			String pointStr = "("+product.getPoint().x+","+product.getPoint().y+")";
			str += pointStr;
		}
		return str;
	}
	
	public ObjectInfo getStateInfo()
	{
		stateInfo.setInfo("ÊýÄ¿£º"+queue.size());
		stateInfo.setColor(Color.BLACK);
		
		return stateInfo;
	}
	
	public ArrayBlockingQueue<Product> getAllProduct()
	{
		return queue;
	}
	
	public void clear()
	{
		queue.clear();
	}
	
}
