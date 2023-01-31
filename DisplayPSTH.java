import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.awt.*;
import java.io.*;

public class DisplayPSTH extends JFrame implements MouseListener, MouseMotionListener, KeyListener{
  static int lLineX,maxlen, maxwid, xLeft,yLeft,trans,psthlen, psthwid,psthbinX, psthbinY, num, width,totTime,maxAP,minSwep;
  static int xLeft2,yLeft2,psthlen2, width2,psthwid2,psthbinX2, psthbinY2,maxSwep;
  static double num2;
  static Image dbImage;
  static Graphics dbg;
  static boolean [] keys;
  static boolean dragging;
  static int mb,mx,my,mx0,my0;
  static Color color;
  static ArrayList <Integer> lowpsthNeu1;
  static ArrayList <Integer> highpsthNeu1;
  static ArrayList <Integer> lowpsthNeu;
  static ArrayList <Integer> highpsthNeu;
  //private ArrayList <Integer> psthNeu2;
  //private ArrayList <Integer> psthNeu3;

  //static ArrayList<Scanner> files;
  static int[] neuMaxSwep;
  static int dotSize;
  static double[][] lowDR1;
  static double[][] highDR1;
  static double maxDRTime;
  static int yWidth;
  static float[] X;
  static float[] Y;

  static ArrayList<psthClass> neurons;
  static psthClass neuron;  

  public DisplayPSTH() throws IOException{
     super("DB Graphics"); 
     keys = new boolean[2000];
     maxlen = 1000;
     maxwid = 800;
     dotSize=1;
     dragging = false;

     neurons = new ArrayList<psthClass>();
     //files = new ArrayList<Scanner>();

     psthlen = 200;
     psthwid = 200;
     psthlen2 = psthlen;
     psthwid2 = 200;

     xLeft=10;
     yLeft=maxwid-10;

     xLeft2=xLeft+psthlen+20;
     yLeft2=maxwid-10;

     trans = 150;

     lLineX = xLeft;
     width=5;
     width2=10;
     yWidth=10;
     totTime = 250;
     maxAP = 200;
     minSwep = 200;
     Scanner kb = new Scanner(System.in);
     Scanner infile1 = new Scanner(new BufferedReader(new FileReader("260314_10_Snip_TankSortChirag.csv")));
     //Scanner infile1 = new Scanner(new BufferedReader(new FileReader("170414_185_Snip_TankSortChirag.csv")));
     Scanner infile2 = new Scanner(new BufferedReader(new FileReader("170414_185_Snip_TankSortChirag.csv")));
     Scanner infile3 = new Scanner(new BufferedReader(new FileReader("170414_190_Snip_TankSortChirag.csv")));    
     
     

     num = totTime/width;
     psthClass neu1 = new psthClass(width,totTime,maxAP,minSwep,infile1);
     lowpsthNeu1 = neu1.getLowPSTH();
     highpsthNeu1 = neu1.getHighPSTH();
     lowDR1 = neu1.getLowDotRaster();
     highDR1 = neu1.getHighDotRaster();

     maxSwep = neu1.getMaxSwep();
     //System.out.println("maxSwep="+maxSwep);
     maxDRTime = 0.250;//maxArray(DR1);
     num2 = psthlen2/width2;

     maxAP = (int)(max((float)(maxList(lowpsthNeu1)),(float)(maxList(highpsthNeu1))));     
     //System.out.println(maxAP);
	/*
     psthClass neu2 = new psthClass(width,totTime,maxAP,minSwep,infile2);
     psthNeu2 = neu2.getPSTH();  
     psthClass neu3 = new psthClass(width,totTime,maxAP,minSwep,infile3);
     psthNeu3 = neu3.getPSTH();
*/
     psthbinX = psthlen/num;
     psthbinY = (int)((float)(psthwid+0.0)/(float)(maxAP+0.0));     
	/*
     System.out.println(((float)(psthwid+0.0)/(float)(maxAP+0.0)));
     System.out.println(psthbinY);
*/
     psthbinX2 = (int)((float)(psthlen2+0.0)/(float)(num2+0.0)); //psthlen2/num2;
     psthbinY2 = (int)((float)(psthwid2+0.0)/(float)(maxSwep+0.0));

	//neu1.printDotRaster();

     addMouseListener(this);
     addKeyListener(this);
     addMouseMotionListener(this);
     setSize(maxlen,maxwid);
     setVisible(true);
     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
  public void mousePressed (MouseEvent e){
     //mx = e.getX();
     //my = e.getY();
     mb = e.getButton();
     //lLineX = mx;
     //dragging = true;
     //System.out.println("X: "+mx+", Y: "+my+", B: "+mb);
  }
  public void mouseMoved (MouseEvent e){
     mx = e.getX();
     my = e.getY();
  }
  public void mouseEntered(MouseEvent e){}
  public void mouseExited(MouseEvent e){}
  public void mouseClicked(MouseEvent e){}
  public void mouseReleased(MouseEvent e){
     	mb=0;
	dragging=false;
  }
  public void mouseDragged(MouseEvent e){
     mx = e.getX();
     my = e.getY();
     mb = e.getButton();
     //lLineX = mx;
     if (dragging){
	repaint();
     }
  }
  
  public void keyTyped(KeyEvent e){}
  public void keyPressed(KeyEvent e){
    keys[e.getKeyCode()] = true;
  }
  public void keyReleased(KeyEvent e){
    keys[e.getKeyCode()] = false;
  }
  
  public static void delay(long len){
    try{
      Thread.sleep(len);
     }
    catch(InterruptedException ex){
      System.out.println(ex);
     }
  }

  public static boolean MouseCollide(int x,int y,int l,int h){
     if (mx>x&&mx<(x+l)){
      if (my>y&&my<(y+h)){
       return true;
      }
     }
     return false;
  }
  
  
  public void move(){
	//for (int i=0; i<neurons.size();i++){
		//neuron = neurons.get(i);
	if (MouseCollide(lLineX-5,yLeft-psthwid,10,psthwid)){
        	if (mb==1){
                	lLineX=mx;
			dragging=true;
                	//mb=0;	
        	}
    	}
	if (dragging){
		lLineX=mx;
		if (mb==0){
			dragging=false;
		}
	}
	if (mb==0){
		dragging=false;
	}

  }

  
  public void paint(Graphics g){
    if (dbImage == null){
      dbImage = createImage(maxlen,maxwid);
      dbg = dbImage.getGraphics();
    }
    dbg.setColor(new Color(255,255,255));
    dbg.fillRect(0,0,maxlen,maxwid);
    dbg.setColor(new Color(0,0,0));
    dbg.drawLine(xLeft,yLeft,xLeft+psthlen,yLeft);
    dbg.drawLine(xLeft,yLeft,xLeft,yLeft-psthwid);
    dbg.drawRect(xLeft-10,yLeft-psthwid-10,psthlen+20,psthwid+20);
    dbg.setColor(new Color(100,100,100));
    for (int i=2;i<=maxAP;i+=2){
        dbg.drawLine(xLeft,yLeft-psthbinY*i,psthlen+xLeft,yLeft-psthbinY*i);
    }
    for (int i=1;i<=num;i++){
        dbg.drawLine(xLeft + psthbinX*i,yLeft,xLeft+psthbinX*i,yLeft-maxAP*psthbinY);
    }
    dbg.setColor(new Color(250,0,0,trans));
    for (int i=0;i<num;i++){
	dbg.setColor(new Color(250,0,0,trans));
        if (lowpsthNeu1.get(i)!=0){
                dbg.fillRect(xLeft+psthbinX*i,yLeft-lowpsthNeu1.get(i)*psthbinY,psthbinX,lowpsthNeu1.get(i)*psthbinY);
        }
    	dbg.setColor(new Color(0,250,0,trans));
	if (highpsthNeu1.get(i)!=0){
		dbg.fillRect(xLeft+psthbinX*i,yLeft-highpsthNeu1.get(i)*psthbinY,psthbinX,highpsthNeu1.get(i)*psthbinY);
	}
    }


    dbg.setColor(new Color(0,0,0));
    dbg.drawLine(xLeft2,yLeft2,xLeft2+psthlen2,yLeft2);
    dbg.drawLine(xLeft2,yLeft2,xLeft2,yLeft2-psthwid2);
    dbg.drawRect(xLeft2-10,yLeft2-psthwid2-10,psthlen2+20,psthwid2+20);
    dbg.setColor(new Color(100,100,100));
    for (int i=5;i<=maxSwep;i+=5){
        dbg.drawLine(xLeft2,yLeft2-psthbinY2*i,psthlen2+xLeft2,yLeft2-psthbinY2*i);
    }
    for (int i=1;i<=num2;i++){
        dbg.drawLine(xLeft2 + psthbinX2*i,yLeft2,xLeft2+psthbinX2*i,yLeft2-maxSwep*psthbinY2);
    }
    dbg.setColor(new Color(250,0,0));
    for (int i=0;i<maxSwep;i++){
        dbg.setColor(new Color(250,0,0));
	for (int j=0;j<lowDR1[i].length;j++){
                if (lowDR1[i][j]!=0){
                        dbg.fillRect(xLeft2+(int)(psthlen*lowDR1[i][j]/maxDRTime)-1,yLeft2-(i+1)*psthbinY2-1,dotSize,dotSize);
                }
        }
	dbg.setColor(new Color(0,200,0,trans));
	for (int j=0;j<highDR1[i].length;j++){
                if (highDR1[i][j]!=0){
                        dbg.fillRect(xLeft2+(int)(psthlen*highDR1[i][j]/maxDRTime)-1,yLeft2-(i+1)*psthbinY2-1,dotSize,dotSize);
                }
        }
    }

    dbg.setColor(new Color(0,0,255));
    dbg.drawLine(lLineX,yLeft,lLineX,yLeft-psthwid);
    g.drawImage(dbImage,0,0,this);
  }

  public static float max(float a, float b){
    if (a>b){
      return a;
    }
    return b;
  }

  public static int maxList(ArrayList<Integer> list){
    int max=list.get(0);
    for (int i=1; i<list.size(); i++){
      if (list.get(i)>max){
        max = list.get(i);
      }
    }
    return max;
  }
	
  public static double maxArray(double[][] list){
    double max=list[0][0];
    for (int i=0; i<list.length; i++){
      for (int j=0; j<list[i].length;j++){
        if (list[i][j]>max){
          max = list[i][j];
        }
      }
    }
    return max;
  }
  
  public static float min(float a, float b){
    if (a<b){
      return a;
    }
    return b;
  }
    
  public static void main(String [] args) throws IOException{
    DisplayPSTH frame = new DisplayPSTH();
    while (true){
    	frame.move();
    	frame.repaint();
    	delay(50);
    }
  }
}
