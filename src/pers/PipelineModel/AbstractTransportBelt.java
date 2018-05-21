package pers.PipelineModel;

import java.awt.Graphics;
import java.awt.Point;

public abstract class AbstractTransportBelt {

	private Path path;
	private int speed;
	
	public AbstractTransportBelt(Path path, int speed)
	{
		this.setPath(path);
		this.setSpeed(speed);
	}
	
	public AbstractTransportBelt(Point startPoint, Point endPoint, int speed)
	{	
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}
	
	abstract public void draw(Graphics g);
	
}
