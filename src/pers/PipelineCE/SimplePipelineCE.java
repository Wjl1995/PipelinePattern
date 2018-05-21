package pers.PipelineCE;

import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

public class SimplePipelineCE extends JFrame{

	private static final long serialVersionUID = 1L;
	
	private PipelinePanel playPanel;
	
	public SimplePipelineCE()
	{
		createUI();
		playPanel.start();
	}
	
	private void createUI()
	{
		setTitle("Á÷Ë®Ïß");
		setSize(1040, 940);
		
		// get content pane for attaching GUI components
		Container contentPane = getContentPane();	
		// enable explicit position of GUI components
		contentPane.setLayout(null);
		
		playPanel = new PipelinePanel();
		playPanel.setBounds(0, 50, 1040, 840);
		contentPane.add(playPanel);
		setVisible(true);
		
		this.addWindowListener(new WindowAdapter(){  
			
		    public void windowClosing(WindowEvent e){ 
		    	playPanel.shutdown();
		        System.exit(0);  
		    }
		});
	}
	
	 public static void main(String[] args){
		 SimplePipelineCE pipeline = new SimplePipelineCE();
		 
		 TimerTask task = new TimerTask() {  
	            @Override  
	            public void run() {  
	                // task to run goes here  
	            	pipeline.repaint();  
	            }  
	     };
	     Timer timer = new Timer();  
	     long delay = 0;  
	     long intevalPeriod = 1 * 50;  
	     timer.scheduleAtFixedRate(task, delay, intevalPeriod);
	 }
}
