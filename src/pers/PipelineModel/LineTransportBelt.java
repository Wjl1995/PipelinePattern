package pers.PipelineModel;

import java.awt.Graphics;
import java.awt.Point;
import java.util.Vector;

public class LineTransportBelt extends AbstractTransportBelt {

	public LineTransportBelt(Path path, int speed) {
		super(path, speed);
		// TODO Auto-generated constructor stub
	}
	
	public LineTransportBelt(Point startPoint, Point endPoint, int speed)
	{
		super(startPoint, endPoint, speed);
		Path path = new Path();
		int pathLength = (int)Math.sqrt((endPoint.x-startPoint.x)*(endPoint.x-startPoint.x) + 
				(endPoint.y-startPoint.y)*(endPoint.y-startPoint.y));
		int times = pathLength / 50;

		Vector<Point> vecPoints = new Vector<Point>();
		for (int i=0; i<=times; i++)
		{
			Point point = new Point(i*(endPoint.x-startPoint.x)/times+startPoint.x, 
					i*(endPoint.y-startPoint.y)/times+startPoint.y);
			vecPoints.add(point);
		}
		path.setPoints(vecPoints);
		
		this.setPath(path);
		this.setSpeed(speed);
	}

	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		this.getPath().draw(g);
	}
}
