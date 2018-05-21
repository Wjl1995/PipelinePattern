package pers.PipelineModel;

// SuperClass for all shape objects

import java.awt.*;

public abstract class myShape extends Object
{
	private int x1;
	private int y1;
	private int x2;
	private int y2;
	
	private Color color;
	
	//constructor
	public myShape( int firstX, int firstY, int secondX, int secondY, Color shapeColor )
	{
		setX1( firstX );
		setY1( firstY );
		setX2( secondX );
		setY2( secondY );
		setColor( shapeColor );
	}

	public void setX1(int firstX) {
		// TODO Auto-generated method stub
		x1 = firstX;
	}
	
	public int getX1(){
		return x1;
	}
	
	public void setY1(int firstY) {
		// TODO Auto-generated method stub
		y1 = firstY;
	}
	
	public int getY1(){
		return y1;
	}
	
	public void setX2(int secondX) {
		// TODO Auto-generated method stub
		x2 = secondX;
	}
	
	public int getX2(){
		return x2;
	}
	
	public void setY2(int secondY) {
		// TODO Auto-generated method stub
		y2 = secondY;
	}
	
	public int getY2(){
		return y2;
	}

	public void setColor(Color shapeColor) {
		// TODO Auto-generated method stub
		color = shapeColor;
	}	
	
	public Color getColor(){
		return color;
	}

	// abstract draw method
	public abstract void draw( Graphics g );
	
	public abstract Boolean isInclude( Point point );
	
	public abstract Boolean isOn( Point point );
	
}
