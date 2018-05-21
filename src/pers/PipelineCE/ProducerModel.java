package pers.PipelineCE;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import pers.PipelineModel.*;

public class ProducerModel extends JLabel implements MouseListener{
	
	private static final long serialVersionUID = 1L;
	
	private Producer producer;
	private boolean state = false;
	private ObjectInfo stateInfo;

	public void initModel(String resource, Producer producer)
	{
		this.setIcon(new ImageIcon(resource));
		this.producer = producer;
		this.addMouseListener(this);
		stateInfo = new ObjectInfo();
	}

	public void setResource(String resource) {
		this.setIcon(new ImageIcon(resource));
	}
	
	public void doProduce()
	{
		producer.start();
		setState(true);

	}
	
	public void shutdown()
	{
		producer.terminate();
		setState(false);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		setCursor(Cursor.getDefaultCursor());
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if (getState()){
			producer.suspendProducer();
			setState(false);
		}
		else{
			producer.resumeProducer();
			setState(true);
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public boolean getState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}
	
	public ObjectInfo getStateInfo()
	{
		if (state)
		{
			stateInfo.setInfo("正在生产");
			stateInfo.setColor(Color.GREEN);
		}
		else
		{
			stateInfo.setInfo("正在等待");
			stateInfo.setColor(Color.RED);
		}
		return stateInfo;
	}
	
}
