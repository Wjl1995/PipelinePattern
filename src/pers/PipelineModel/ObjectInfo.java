package pers.PipelineModel;

import java.awt.Color;

public class ObjectInfo {

	private String info;
	private Color color;
	
	public ObjectInfo(){
		
	}
	
	public ObjectInfo(String info, Color color)
	{
		this.info = info;
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public String getInfo() {
		return info;
	}
	
	public void setInfo(String info) {
		this.info = info;
	}
}
