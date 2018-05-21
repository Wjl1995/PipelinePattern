package pers.PipelineModel;

import java.awt.*;
import java.util.Iterator;
import java.util.Vector;


public class Path {

	private Vector<Point> points;
	
	public Path()
	{	
	}

	public Path(Vector<Point> points)
	{
		this.points = points;
	}
	
	public Vector<Point> getPoints() {
		return points;
	}

	public void setPoints(Vector<Point> points) {
		this.points = points;
	}

	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		Iterator<Point> it = points.iterator();
		while (it.hasNext())
		{
			Point point = it.next();
			int R = 2;
			g.fillOval(point.x-R, point.y-R, 2*R, 2*R);
		}
	}
	
}
