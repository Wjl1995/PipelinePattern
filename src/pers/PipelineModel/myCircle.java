package pers.PipelineModel;

//Class that declares a circle object

import java.awt.*;

public abstract class myCircle extends myShape{

	public myCircle(int firstX, int firstY, int secondX, int secondY, Color shapeColor) {
		super(firstX, firstY, secondX, secondY, shapeColor);
		// TODO Auto-generated constructor stub
	}

	// draw a circle
	public void draw( Graphics g ){
		int upperLeftX = Math.min( getX1(), getX2() );
		int upperLeftY = Math.min( getY1(), getY2() );
		int width = Math.abs( getX1() - getX2() );
		int height = Math.abs( getY1() - getY2() );
		
		g.setColor( getColor() );
		g.fillOval(upperLeftX, upperLeftY, width, height);
	}

	// check include
	public Boolean isInclude( Point point ){
		
		Point center = new Point( Math.abs( (getX1() + getX2())/2 ),  Math.abs( (getY1() + getY2())/2) );
		int diameter = Math.abs( getX1() - getX2() ) / 2;
		
		int distance = (int) Math.pow((point.x - center.x), 2) + (int) Math.pow((point.y - center.y), 2);
		
		if ( distance < diameter )
			return true;
		else
			return false;
	}

	@Override
	public Boolean isOn(Point point) {
		// TODO Auto-generated method stub
		if ( point.x>Math.min(getX1(), getX2()) && point.x<Math.max(getX1(), getX2())
				&& point.y< (Math.min(getY1(), getY2())+5) )
			return true;
		return false;
	}
	
	abstract public void moveTo(Point point);
	
}
