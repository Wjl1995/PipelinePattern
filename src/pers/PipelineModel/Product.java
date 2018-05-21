package pers.PipelineModel;

import java.awt.Point;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import pers.PipelineCE.ProductStorage;

public class Product extends JLabel{

	private static final long serialVersionUID = 1L;
	
	private String Context;
	private Point point;
	private ProductColor productColor;
	private int R = 10;
	
	public Product(String Context, Point point, ProductColor productColor) {
		// TODO Auto-generated constructor stub
		this.setContext(Context);
		this.setPoint(point);
		this.setBounds(point.x-R, point.y-R, 2*R, 2*R);
		this.setProductColor(productColor);
		setImgIcon(productColor);
	}

	public String getContext() {
		return Context;
	}

	public void setContext(String context) {
		Context = context;
	}
	
	public void moveBy(AbstractTransportBelt transportBelt, ProductStorage storage) throws IllegalStateException 
	{
		int speed = transportBelt.getSpeed();
		Iterator<Point> it = transportBelt.getPath().getPoints().iterator();
		
			while (it.hasNext())
			{
				Point point = it.next();
				moveTo(point);
				try {
					Thread.sleep(200/speed);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			storage.addProduct(this);
	}
	
	public void moveBy(AbstractTransportBelt transportBelt)
	{
		int speed = transportBelt.getSpeed();
		Iterator<Point> it = transportBelt.getPath().getPoints().iterator();
		
			while (it.hasNext())
			{
				Point point = it.next();
				moveTo(point);
				try {
					Thread.sleep(200/speed);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}

	public void moveTo(Point point) {
		// TODO Auto-generated method stub
		this.point = new Point(point);
		this.setLocation(point.x-R, point.y-R);
	}
	
	public void moveTo(Point end, int time) throws InterruptedException
	{
		int times = time / 100;
		int stepX = (end.x-point.x) / times;
		int stepY = (end.y-point.y) / times;
		for (int i=0; i<times; i++)
		{
			Thread.sleep(100);
			point.x += stepX;
			point.y += stepY;
			this.setLocation(point.x-R, point.y-R);
		}
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}
	
	public void setImgIcon(ProductColor productColor)
	{
		switch (productColor)
		{	
		case BLACK:
			this.setIcon(new ImageIcon("image/product/black.png"));
			break;
		case BLUE:
			this.setIcon(new ImageIcon("image/product/blue.png"));
			break;
		case COLOR:
			this.setIcon(new ImageIcon("image/product/color.png"));
			break;
		case GREEN:
			this.setIcon(new ImageIcon("image/product/green.png"));
			break;
		case PINK:
			this.setIcon(new ImageIcon("image/product/pink.png"));
			break;
		case RED:
			this.setIcon(new ImageIcon("image/product/red.png"));
			break;
		case VIOLET:
			this.setIcon(new ImageIcon("image/product/zi.png"));
			break;
		default:
			break;
		}
	}

	public ProductColor getProductColor() {
		return productColor;
	}

	public void setProductColor(ProductColor productColor) {
		this.productColor = productColor;
	}
}
