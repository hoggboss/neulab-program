import java.util.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

public class NeuLab {
	static ArrayList<NeuronAnalysis> frames;
	static NeuronAnalysis frame;
	public static void main(String[] args) throws IOException{
	    /*SwingUtilities.invokeLater(new Runnable(){
	      @Override
	      public void run(){
	        NeuronAnalysis frame = new NeuronAnalysis();
		 //frame.move();
		 //frame.repaint();
	      }
	    });*/
		frames = new ArrayList<NeuronAnalysis>();
		createNewFrame();
		/*(NeuronAnalysis frame = new NeuronAnalysis();
	    	frame.addWindowListener(new WindowAdapter() {
	      	public void windowClosing(WindowEvent e) {
			System.exit(0);
      		}
   	 	});*/
    		//frame.repaint();
    		//frame.move();
    		//frame.initLinePos();
    		//frame.repaint();
		ArrayList <Integer> closedFrames = new ArrayList<Integer>();
    		while (true){
			//frame.move();
			//if (frame.getUpdate()){
			//	System.out.println("YES");
				
			//	frame.repaint();
			//}
			/*else{
				System.out.println("NO");
			}*/
			//System.out.println("UPDATING");
			//}	
			//delay(30);
			for (int i=0; i<frames.size(); i++){
    	  			frames.get(i).move();
    	  			frames.get(i).repaint();
				if (frames.get(i).getNewFrame()){
					createNewFrame();
					frames.get(i).setNewFrame(false);
				}
				if (frames.get(i).getCloseFrame()){
					if (frames.size() <= 1){
						System.exit(0);
					}
					frames.get(i).setCloseFrame(false);
					frames.get(i).setVisible(false);
					//frames.get(i).dispose();
					
					closedFrames.add(i);
					//frames.remove(frames.get(i));
					//break;
				}
				/*if (frames.get(i).hasFocus()){
					System.out.println("Focus: "+i+"\n");
				}*/
    	  			//delay(30);
			}
			if (closedFrames.size() > 0){
				for (int i=0; i<closedFrames.size(); i++){
					frames.remove(i);
				}
				closedFrames = new ArrayList<Integer>();
			}
			//System.out.println("frames.size()="+frames.size()+"\n");
			delay(30);
    		}
  	}

	public static void createNewFrame() throws IOException{
		final NeuronAnalysis newFrame = new NeuronAnalysis();
                /*newFrame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                	if ( frames.size() > 1){
				newFrame.setVisible(false);
				//newFrame.setCloseWindow(true);
			}
			else{
				System.exit(0);
			}
                }
                });*/
		newFrame.setVisible(true);
		newFrame.initLinePos();
                newFrame.repaint();
		newFrame.requestFocus();
		newFrame.toFront();
		//newFrame.setState(Frame.NORMAL);
                
		MouseListener m = new MouseAdapter()
        	{
            		@Override
            		public void mousePressed(MouseEvent e)
            		{
				newFrame.requestFocus();
				newFrame.toFront();
        		}
		};
		newFrame.addMouseListener(m);
		frames.add(newFrame);
	}

	//Delay
	public static void delay(long len){
    	try{
    		Thread.sleep(len);
    	}
    	catch(InterruptedException ex){
    		System.out.println(ex);
    	}

    }
}
