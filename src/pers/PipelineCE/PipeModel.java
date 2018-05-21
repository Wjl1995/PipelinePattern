package pers.PipelineCE;


import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.TimeUnit;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import pers.PipelineModel.*;

public class PipeModel extends JLabel implements MouseListener{

	private static final long serialVersionUID = 1L;
	
	private ProcessPipeline processPipeline;
	private boolean state = false;

	public void initModel(String resource, ProcessPipeline processPipeline)
	{
		this.setIcon(new ImageIcon(resource));
		this.processPipeline = processPipeline;
		this.addMouseListener(this);
	}
	
	public void setResource(String resource) {
		this.setIcon(new ImageIcon(resource));
	}
	
	public void Process()
	{
		processPipeline.start();
		setState(true);
	}

	public void shutdown() throws InterruptedException {
		// TODO Auto-generated method stub
		processPipeline.shutdown(0, TimeUnit.MILLISECONDS);
		setState(false);
	}

	public ObjectInfo getStateInfo()
	{
		return processPipeline.getPlatState();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		setCursor(Cursor.getDefaultCursor());
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if (getState()){
			processPipeline.suspendPipe();
			setState(false);
		}
		else{
			processPipeline.resumePipe();
			setState(true);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean getState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}
}
