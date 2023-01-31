import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.awt.*;
import java.io.*;
import java.io.File;

public class GenerateDisplay extends JFrame{
	static int numCount, xPos2, yPos2, xPos, yPos, numr, widthr, hLineY, DRlLineX, DRrLineX, rLineX, lLineX, lLineY, maxlen, maxwid, xLeft,yLeft,trans,psthlen, psthwid,num, width,totTime,maxAP,minSwep;
  	static int ind,trans1, trans2;
	static int startingX, startingY, xLeft3,xLeft2,yLeft2,yLeft3,psthlen2, width2,psthwid2,maxSwep;
	static double timeL, timeR, num2, psthbinXc, psthbinYc, psthbinX, psthbinY, psthbinX2, psthbinY2, psthbinXr, psthbinYr;
  	static Image dbImage;
  	static Graphics dbg;
  	static boolean [] keys;
  	static int mb,mx,my,mx0,my0, xLeft0, yLeft0,psthlen3, psthwid3;
  	static Color color;
	static int[] freqSelected, intenSelected;
	static ArrayList <Integer> numCountList,numIntensCountList,numIpsiIntensCountList,numILDCountList,maxIntensAPList,maxAPList, maxIpsiIntensAPList, maxILDAPList;
  	static ArrayList <Double> maxAPRateList, maxIntensAPRateList, maxIpsiIntensAPRateList, maxILDAPRateList, DS;
  	static ArrayList <Integer> psthNeu;
        static ArrayList <Double> psthRateNeu;
	static ArrayList <Integer> numFreqs;
	static int maxAPCount, numNeurons;
  	static int[] neuMaxSwep;
	static String[] countDotColorList;
  	static int dotSize, cDotSize;
	static double[][] DR;
	static double[] overview;
 	static double addedspont, spont, peakLatency,maxAPRate,maxDRTime,lowPSTHPerc,highDRPerc;
 	static int yWidth;
  	static float[] X;
  	static float[] Y;
	static int delta,count;
  	static Font small,f, heading;
  	static String xAxis,yAxis;
  	static File[] files;
	static Scanner infile, infile1, infile2, infile3, infile4;	
  	static ArrayList<psthClass> neurons;
  	static psthClass neuron;  
	static String[] basicColumnTitles, advColumnTitles;

  	public GenerateDisplay(int startingX, int startingY, int width) throws IOException{
     		keys = new boolean[2000];
     		maxlen = 1500;
     		maxwid = 800;
		this.startingX = startingX;
		this.startingY = startingY;
		dotSize=2;
		this.cDotSize=10;
    		f = new Font("Lucida Grande",Font.BOLD,9);//Font("Eurostile",Font.BOLD,9);
     		heading = new Font("Lucida Grande",Font.BOLD,11);
		small = new Font("Lucida Grande",Font.BOLD,8);
     		neurons = new ArrayList<psthClass>();

		basicColumnTitles = new String[4];
		advColumnTitles = new String[3];
		basicColumnTitles[0] = "time";
		basicColumnTitles[1] = "swep";
		basicColumnTitles[2] = "freq";
		basicColumnTitles[3] = "levl";
		advColumnTitles[0] = "oddb";
		advColumnTitles[1] = "ilvl";
		advColumnTitles[2] = "clvl";
		delta = 50;
     		psthlen = 500;
     		psthwid = 300;
     		psthlen2 = psthlen;
     		psthwid2 = psthwid;
		psthlen3 = psthlen;
		psthwid3 = psthwid;

     		xLeft0=startingX;
     		yLeft0=maxwid-startingY;
		xLeft = xLeft0;
		yLeft = yLeft0;

     		xLeft2=xLeft;//+psthlen+20;
     		yLeft2=yLeft;//maxwid-10;
		xLeft3=xLeft;
		yLeft3=yLeft;

     		trans2 = 100;
		trans1 = 200;

     		lLineX = xLeft;
     		rLineX = xLeft+ psthlen;
     		DRlLineX = xLeft;//+psthlen+20;
     		DRrLineX = DRlLineX + psthlen;
     		this.width = width;//width=5;
     		width2=5;
     		widthr=1;
     		yWidth=10;
     		totTime = 250;
     		maxAP = 200;
     		maxAPRate = 1000;
     		minSwep = 200;
		num = totTime/width;
		numCount = 2;
		spont = 0.0d;
     		Scanner kb = new Scanner(System.in);
    
		files = new File[0];
     		numNeurons = neurons.size();
     		maxDRTime = totTime/1000.0;//maxArray(DR1);
     		num2 = totTime/width2;
     		numr = totTime/widthr;
		
		timeL = 0.0;
		timeR = 250.0;

     		psthbinX = ((double)(psthlen+0.0)/(double)(num+0.0));
     		psthbinX2 = ((double)(psthlen2+0.0)/(double)(num2+0.0)); //psthlen2/num2;
     		psthbinY2 = ((double)(psthwid2+0.0)/(double)(maxSwep+0.0));
    						
		this.countDotColorList = new String[100];
	}

	public void setSize(int maxl, int maxw){
		this.maxlen = maxl;
		this.maxwid = maxw;
		//resetGraphPos();
	}
  
	public void resetBinWidth(int newWid){
		this.width = newWid;
		num = totTime/width;
		/*neurons.clear();
		for (int i=0; i<files.length;i++){
                        infile = new Scanner(new BufferedReader(new FileReader(files[i])));
                        neurons.add(new psthClass(psthlen,width,totTime,maxAP,minSwep,infile));
                }
		numNeurons=neurons.size();
		setParams();*/
		for (int i=0; i<numNeurons; i++){
			neurons.get(i).setBinWidth(width);
		}
		setParams();
		//psthbinX = ((double)(psthlen+0.0)/(double)(num+0.0));		
	}
	public int getStartingX(){
		return startingX;
	}
	public int getStartingY(){
		return startingY;
	}
	public void setStartingX(int x){
		this.startingX = x;
		xLeft0 = startingX;
		resetGraphPos();
	}
	public void setStartingY(int y){
		this.startingY = y;
		yLeft0 = startingY;
		resetGraphPos();
	}
	public void setStartingPos(int x, int y){
		this.startingX = x;
		this.startingY = y;
		xLeft0 = startingX;
		yLeft0 = startingY;
		resetGraphPos();
	}
	
	public boolean inPanel(int mx, int my){
		if (MouseCollide(mx,my,psthlen,psthwid)){
			System.out.println("inPanel");
			return true;
		}
		return false;
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
 
  	public void resetGraphPos(){
 		xLeft = xLeft0;//startingX;
 		yLeft = yLeft0;//maxwid - startingY;
 		xLeft2 = xLeft;// + 20 + psthlen;
 		xLeft3 = xLeft2;// + 20 + psthlen;
 		yLeft2 = yLeft;
 		yLeft3 = yLeft;
  	}

	public void updateCount(){
		for (int k=0; k<numNeurons; k++){
			neuron = neurons.get(k);
			//FREQ COUNT:
			numCount = numCountList.get(k);
			for (int l=0; l<numCount;l++){
                        	DR = neuron.getDR(l);
                        	count = 0;
                        	for (int i=0;i<maxSwep;i++){
                                	for (int j=0;j<DR[i].length;j++){
                                	        if (DR[i][j]!=0){
							if ( ((double)(psthlen)*DR[i][j]/(double)maxDRTime)>=(double)(neuron.getDRlLineX()) && ((double)(psthlen)*DR[i][j]/(double)maxDRTime)<=(double)(neuron.getDRrLineX())){
                                        	        //if ( ((double)psthlen*DR[i][j]/(double)maxDRTime)>=(double)(neuron.getDRlLineX()) && ((double)psthlen*DR[i][j]/(double)maxDRTime)<=(double)(neuron.getDRrLineX())){
								count ++;
                                                 	}
                                        	}
                                	}
                        	}
                        	neuron.setAPCount(l,count);
                	}
			//INTENSITY COUNT:
			numCount = numIntensCountList.get(k);
			for (int l=0; l<numCount;l++){
                                DR = neuron.getIntensDR(l);
                                count = 0;
                                for (int i=0;i<maxSwep;i++){
                                        for (int j=0;j<DR[i].length;j++){
                                                if (DR[i][j]!=0){
                                                        if ( (psthlen*DR[i][j]/maxDRTime)>=(double)(neuron.getDRlLineX()) && (psthlen*DR[i][j]/maxDRTime)<(double)(neuron.getDRrLineX())){
                                                                count ++;
                                                        }
                                                }
                                        }
                                }
                                neuron.setIntensAPCount(l,count);
                        }
							
			if (neuron.isClosedField()){
			//IPSI-INTENS COUNT:
			numCount = numIpsiIntensCountList.get(k);
			for (int l=0; l<numCount;l++){
                                DR = neuron.getIpsiIntensDR(l);
                                count = 0;
                                for (int i=0;i<maxSwep;i++){
                                        for (int j=0;j<DR[i].length;j++){
                                                if (DR[i][j]!=0){
                                                        if ( (psthlen*DR[i][j]/maxDRTime)>=(double)(neuron.getDRlLineX()) && (psthlen*DR[i][j]/maxDRTime)<(double)(neuron.getDRrLineX())){
                                                                count ++;
                                                        }
                                                }
                                        }
                                }
                                neuron.setIpsiIntensAPCount(l,count);
                        }
			//ILD COUNT:
			numCount = numILDCountList.get(k);
                        for (int l=0; l<numCount;l++){
                                DR = neuron.getILDDR(l);
                                count = 0;
                                for (int i=0;i<maxSwep;i++){
                                        for (int j=0;j<DR[i].length;j++){
                                                if (DR[i][j]!=0){
                                                        if ( (psthlen*DR[i][j]/maxDRTime)>=(double)(neuron.getDRlLineX()) && (psthlen*DR[i][j]/maxDRTime)<(double)(neuron.getDRrLineX())){
                                                                count ++;
                                                        }
                                                }
                                        }
                                }
                                neuron.setILDAPCount(l,count);
                        }
			}
			timeL = (double)(neuron.getDRlLineX())*(double)(width)/(double)psthbinX;
                        timeR = (double)(neuron.getDRrLineX())*(double)(width)/(double)psthbinX;
                        neuron.setlTime((int)(timeL));
               		neuron.setrTime((int)(timeR));
		}
	}
  	
  	public void movePSTH(int x, int y, int b, int index){
		mx = x;
 		my = y;
 		mb = b;
  			neuron = neurons.get(index);
  			lLineX = neuron.getlLineX();//(int)((double)neuron.getlTime()*(double)(psthbinX)/(double)(width));//neuron.getlLineX();
			rLineX = neuron.getrLineX();//(int)((double)neuron.getrTime()*(double)(psthbinX)/(double)(width));
  			//rLineX = neuron.getrLineX();
  			hLineY = neuron.gethLineY();
  			DRlLineX = neuron.getDRlLineX();
  			DRrLineX = neuron.getDRrLineX();
  			if (mb==1){
   				if (MouseCollide(lLineX+xLeft-5,yLeft-psthwid,10,psthwid) && !neuron.getDraggingRight() && !neuron.getDRDraggingRight() && !neuron.getDRDraggingLeft()){
    					neuron.setlLineX((int)(psthbinX)*((mx-xLeft)/(int)(psthbinX)));
    					neuron.setDRlLineX((int)(psthbinX)*((mx-xLeft)/(int)(psthbinX)));
					neuron.setDraggingLeft(true);
    					neuron.setDraggingRight(false);
    					neuron.setDRDraggingLeft(false);
    		                        neuron.setDRDraggingRight(false);
       				}
   				if (MouseCollide(rLineX+xLeft-5,yLeft-psthwid,10,psthwid) && !neuron.getDraggingLeft() && !neuron.getDRDraggingRight() && !neuron.getDRDraggingLeft()){
                                	neuron.setrLineX((int)(psthbinX)*((mx-xLeft)/(int)(psthbinX)));
					neuron.setDRrLineX((int)(psthbinX)*((mx-xLeft)/(int)(psthbinX)));
    					neuron.setDraggingLeft(false);
                                	neuron.setDraggingRight(true);
    					neuron.setDRDraggingLeft(false);
                                	neuron.setDRDraggingRight(false);
				}

  			}
			else if (mb==0){
                                neuron.setDraggingRight(false);
                                neuron.setDraggingLeft(false);
                                neuron.setDRDraggingLeft(false);
                                neuron.setDRDraggingRight(false);
                        }
  			if (neuron.getDraggingLeft()){
   				//neuron.setlLineX(psthbinX*((mx-xLeft)/psthbinX));
   				/*if (mx>(xLeft+psthlen)){
                              	neuron.setlLineX(psthlen);
                        	}*/
                        	if(mx<xLeft){
                                	neuron.setlLineX(0);
					neuron.setDRlLineX(0);
                        	}
   				else if(mx>(xLeft+rLineX-(int)psthbinX)){
    					neuron.setlLineX(rLineX-(int)psthbinX);
				}
   				else{
    					neuron.setlLineX((int)(psthbinX)*((mx-xLeft)/(int)(psthbinX)));
					neuron.setDRlLineX((int)(psthbinX)*((mx-xLeft)/(int)(psthbinX)));
					//neuron.setlLineX((int)(psthbinX*((mx-xLeft)/psthbinX)));
   				}
				neuron.setDraggingRight(false);
                                neuron.setDRDraggingLeft(false);
                                neuron.setDRDraggingRight(false);
  			}
  			else if (neuron.getDraggingRight()){
   				if (mx>(xLeft+psthlen)){
                                	neuron.setrLineX(psthlen);
					neuron.setDRrLineX(psthlen);
                        	}
   				else if(mx<(xLeft+lLineX+(int)psthbinX)){
    					neuron.setrLineX(lLineX+(int)psthbinX);
   				}
   				else{
    					neuron.setrLineX((int)(psthbinX)*((mx-xLeft)/(int)(psthbinX)));
					neuron.setDRrLineX((int)(psthbinX)*((mx-xLeft)/(int)(psthbinX)));
				}
				neuron.setDraggingLeft(false);
                                neuron.setDRDraggingLeft(false);
                                neuron.setDRDraggingRight(false);
  			}
  			/*if (mb==0){
   				neuron.setDraggingRight(false);
   				neuron.setDraggingLeft(false);
   				neuron.setDRDraggingLeft(false);
                        	neuron.setDRDraggingRight(false);
  			}*/
			lLineX = neuron.getlLineX();
                        rLineX = neuron.getrLineX();
                        hLineY = neuron.gethLineY();
                        DRlLineX = neuron.getDRlLineX();
                        DRrLineX = neuron.getDRrLineX();
			timeL = neuron.getlLineX()*width/psthbinX;
 			timeR = neuron.getrLineX()*width/psthbinX;
			neuron.setlTime((int)timeL);
			neuron.setrTime((int)timeR);
			numr = (int)((timeR-timeL)/widthr);
		//}

  	}

	public void moveDR(int x, int y, int b, int index){
		mx = x;
 		my = y;
 		mb = b;
 		//for (int i=0; i<neurons.size();i++){
  			neuron = neurons.get(index);
  			lLineX = neuron.getlLineX();
  			rLineX = neuron.getrLineX();
  			hLineY = neuron.gethLineY();
  			DRlLineX = neuron.getDRlLineX();
  			DRrLineX = neuron.getDRrLineX();

  			if (mb==1){
   				if (MouseCollide(DRrLineX+xLeft2-5,yLeft2-psthwid,10,psthwid) && !neuron.getDRDraggingLeft() && !neuron.getDraggingRight() && !neuron.getDraggingLeft()){
                                	neuron.setDRrLineX(mx-xLeft2);
                                	neuron.setrLineX((int)(psthbinX)*((mx-xLeft2)/(int)(psthbinX)));
					neuron.setDRDraggingLeft(false);
                                	neuron.setDRDraggingRight(true);
                                	neuron.setDraggingLeft(false);
                                	neuron.setDraggingRight(false);
                        	}
   				if (MouseCollide(DRlLineX+xLeft2-5,yLeft2-psthwid,10,psthwid) && !neuron.getDRDraggingRight() && !neuron.getDraggingRight() && !neuron.getDraggingLeft()){
                                	neuron.setDRlLineX(mx-xLeft2);
                                	neuron.setlLineX((int)(psthbinX)*((mx-xLeft2)/(int)(psthbinX)));
					neuron.setDRDraggingLeft(true);
                                	neuron.setDRDraggingRight(false);
                                	neuron.setDraggingLeft(false);
                                	neuron.setDraggingRight(false);
                        	}

  			}
			else if (mb==0){
                                neuron.setDraggingRight(false);
                                neuron.setDraggingLeft(false);
                                neuron.setDRDraggingLeft(false);
                                neuron.setDRDraggingRight(false);
                        }
  			if (neuron.getDRDraggingLeft()){
                        	if(mx<xLeft2){
                         	       	neuron.setDRlLineX(0);
				       	neuron.setlLineX(0);
                        	}
                        	else if(mx>(xLeft2+DRrLineX-1-(int)(psthbinX/width))){
                                	neuron.setDRlLineX(DRrLineX-(int)(psthbinX/width)-1);
					neuron.setlLineX((int)(psthbinX)*((DRrLineX-(int)(psthbinX/width)-1)/(int)(psthbinX)));
                        	}
                        	else{
                                	neuron.setDRlLineX(mx-xLeft2);
                        		neuron.setlLineX((int)(psthbinX)*((mx-xLeft2)/(int)(psthbinX)));
				}
				neuron.setDraggingRight(false);
                                neuron.setDraggingLeft(false);
                                neuron.setDRDraggingRight(false);
                	}
  			else if (neuron.getDRDraggingRight()){
                        	if (mx>(xLeft2+psthlen)){
                                	neuron.setDRrLineX(psthlen);
					neuron.setrLineX(psthlen);
                        	}
                        	else if(mx<(xLeft2+DRlLineX+1+(int)(psthbinX/width))){
                                	neuron.setDRrLineX(DRlLineX+(int)(psthbinX/width));
                        	}
                        	else{
                                	neuron.setDRrLineX(mx-xLeft2);
					neuron.setrLineX((int)(psthbinX)*((mx-xLeft2)/(int)(psthbinX)));
                        	}
                                neuron.setDraggingLeft(false);
                                neuron.setDraggingRight(false);
                                neuron.setDRDraggingLeft(false);
                	}
  			/*if (mb==0){
   				neuron.setDraggingRight(false);
   				neuron.setDraggingLeft(false);
   				neuron.setDRDraggingLeft(false);
                        	neuron.setDRDraggingRight(false);
  			}*/
			lLineX = neuron.getlLineX();
                        rLineX = neuron.getrLineX();
                        hLineY = neuron.gethLineY();
                        DRlLineX = neuron.getDRlLineX();
                        DRrLineX = neuron.getDRrLineX();
			timeL = (double)(neuron.getDRlLineX())*(double)(width)/(double)psthbinX;
 			timeR = (double)(neuron.getDRrLineX())*(double)(width)/(double)psthbinX;
			neuron.setlTime((int)(timeL));
			neuron.setrTime((int)(timeR));
			//System.out.println("moveDR; timeL="+neuron.getlTime()+", timeR="+neuron.getrTime());
			timeL = neuron.getlLineX()*width/psthbinX;
                        timeR = neuron.getrLineX()*width/psthbinX;
			numr = (int)((timeR-timeL)/widthr);
		//}

  	}
							
	public void moveCount(int x, int y, int b, int index, String curparam){
		mx = x;												
		my = y;
		mb = b;
		neuron = neurons.get(index);
		//if (mb == 0){
			for (int i=0; i<(numCount); i++){
				xPos = (int)(psthbinXc*i);
				if (curparam.equals("Frequency")){
					if (MouseCollide(xLeft+xPos-cDotSize/2,yLeft-(int)(neuron.getAPCount(i)*psthbinYc)-cDotSize/2,cDotSize,cDotSize)){
						if (mb==0){
							neuron.setCountDotSelected(i,"hover");
						}
						else if (mb == 1){
							neuron.setCountDotSelected(i,"click");
						}
					}
					else{
						//if (mb==1){
							neuron.setCountDotSelected(i,"none");
						//}
					}
				}
				else if (curparam.equals("Intensity")){
					if (MouseCollide(xLeft+xPos-cDotSize/2,yLeft-(int)(neuron.getIntensAPCount(i)*psthbinYc)-cDotSize/2,cDotSize,cDotSize)){
                                                if (mb == 0){
							neuron.setCountDotSelected(i,"hover");
                                        	}
						else if (mb == 1){
                                                        neuron.setCountDotSelected(i,"click");
                                                }	
					}
					else{
                                                neuron.setCountDotSelected(i,"none");
                                        }
				}
				else if (curparam.equals("Ipsi-Intensity")){
					if (MouseCollide(xLeft+xPos-cDotSize/2,yLeft-(int)(neuron.getIpsiIntensAPCount(i)*psthbinYc)-cDotSize/2,cDotSize,cDotSize)){
                                                if (mb == 0){
							neuron.setCountDotSelected(i,"hover");
                                        	}
						else if (mb == 1){
                                                        neuron.setCountDotSelected(i,"click");
                                                }
					}
					else{
                                                neuron.setCountDotSelected(i,"none");
                                        }
				}
				else if (curparam.equals("ILD")){
					if (MouseCollide(xLeft+xPos-cDotSize/2,yLeft-(int)(neuron.getILDAPCount(i)*psthbinYc)-cDotSize/2,cDotSize,cDotSize)){
                                      		if (mb == 0){
							neuron.setCountDotSelected(i,"hover");
                                        	}
						else if (mb == 1){
                                                        neuron.setCountDotSelected(i,"click");
                                                }
					}
					else{
                                                neuron.setCountDotSelected(i,"none");
                                        }
				}
				else{
					if (MouseCollide(xLeft+xPos-cDotSize/2,yLeft-(int)(neuron.getAPCount(i)*psthbinYc)-cDotSize/2,cDotSize,cDotSize)){
                                                if (mb==0){
                                                        neuron.setCountDotSelected(i,"hover");
                                                }
						else if (mb == 1){
                                                        neuron.setCountDotSelected(i,"click");
                                                }
                                        }
					else{
                                                neuron.setCountDotSelected(i,"none");
                                        }
				}		
			}
		//}
	}

	
	public void resetAllCountDots(int index){
		neurons.get(index).resetAllCountDots();
	}
	

	public void setParams(){
		//Freqs:
		maxAPList = new ArrayList<Integer>();
		numCountList = new ArrayList<Integer>();
		maxAPRateList = new ArrayList<Double>();
		//Intens:
		maxIntensAPList = new ArrayList<Integer>();
                numIntensCountList = new ArrayList<Integer>();
                maxIntensAPRateList = new ArrayList<Double>();
		//Ipsi-Intens:
		maxIpsiIntensAPList = new ArrayList<Integer>();
		numIpsiIntensCountList = new ArrayList<Integer>();
		maxIpsiIntensAPRateList = new ArrayList<Double>();
		//ILD:
		maxILDAPList = new ArrayList<Integer>();
                numILDCountList = new ArrayList<Integer>();
                maxILDAPRateList = new ArrayList<Double>();
		//freqSelectedList = new ArrayList<int[]>();
		//intenSelectedList = new ArrayList<int[]>();
		for (int i=0; i<numNeurons; i++){
			maxAPRateList.add(100.0*((calcMaxAPRate(neurons.get(i))+99.0)/100.0));
			maxIntensAPRateList.add(100.0*((calcIntensMaxAPRate(neurons.get(i))+99.0)/100.0));
			if (neurons.get(i).isClosedField()){
				maxIpsiIntensAPRateList.add(100.0*((calcIpsiIntensMaxAPRate(neurons.get(i))+99.0)/100.0));
				maxILDAPRateList.add(100.0*((calcILDMaxAPRate(neurons.get(i))+99.0)/100.0));
			}
			numCountList.add(neurons.get(i).getNumFreqs());
			numIntensCountList.add(neurons.get(i).getNumIntens());
			if (neurons.get(i).isClosedField()){
				numIpsiIntensCountList.add(neurons.get(i).getNumIpsiIntens());
				numILDCountList.add(neurons.get(i).getNumILD());
			}
			maxAPList.add(10*((calcMaxAP(neurons.get(i))+9)/10));
			maxIntensAPList.add(10*((calcIntensMaxAP(neurons.get(i))+9)/10));
			if (neurons.get(i).isClosedField()){
				maxIpsiIntensAPList.add(10*((calcIpsiIntensMaxAP(neurons.get(i))+9)/10));
				maxILDAPList.add(10*((calcILDMaxAP(neurons.get(i))+9)/10));
			}
			for (int j=0; j<neurons.get(i).getNumFreqs(); j++){	
				neurons.get(i).setPeakLatencyFreq(j,maxDoubleListPos(neurons.get(i).getRate(j)));
			}
			for (int j=0; j<neurons.get(i).getNumIntens(); j++){
				neurons.get(i).setPeakLatencyInten(j,maxDoubleListPos(neurons.get(i).getIntensRate(j)));
			}
			/*for (int j=0; j<neurons.get(i).getNumIpsiIntens(); j++){
                                neurons.get(i).setPeakLatencyIpsiInten(j,maxDoubleListPos(neurons.get(i).getIntensRate(j)));
                        }*/				
		}
	}
	public void modifyParams(int i){
                maxAPRateList.remove(i);
		maxIntensAPRateList.remove(i);
                maxIpsiIntensAPRateList.remove(i);
		maxILDAPRateList.remove(i);
		numCountList.remove(i);
		numIntensCountList.remove(i);
		numIpsiIntensCountList.remove(i);
		numILDCountList.remove(i);
                maxAPList.remove(i);		
		maxIntensAPList.remove(i);
		maxIpsiIntensAPList.remove(i);
		maxILDAPList.remove(i);
	}

	public Graphics drawOverview(Graphics gp, int k){
		neuron = neurons.get(k);
		
		//int maxFreq = 20000;
		//int maxIntens = 200;
		int minFreq = (int)(neuron.getOverFreqRange()[0]);
		int maxFreq = (int)(neuron.getOverFreqRange()[1]);
		int minIntens = (int)(neuron.getOverIntenRange()[0]);
                int maxIntens = (int)(neuron.getOverIntenRange()[1]);
		if (maxFreq==minFreq){
			maxFreq = 20000;
			minFreq = 0;
		}
		else{
			maxFreq = (int) (Math.ceil(maxFreq/1000.0d) * 1000.0d);
			minFreq = (int) (Math.floor(minFreq/1000.0d) * 1000.0d);
		}
		if (maxIntens==minIntens){
                        maxIntens = 200;
                        minIntens = -50;
                }
                else{
                        maxIntens = (int) (Math.ceil(maxIntens/10.0d) * 10.0d);
                        minIntens = (int) (Math.floor(minIntens/10.0d) * 10.0d);
                }
		

		psthbinX = ((double)(psthlen+0.0)/((double)((maxFreq-minFreq)+0.0)));
                psthbinY = ((double)(psthwid+0.0)/((double)((maxIntens-minIntens)+0.0)));		

		gp.setColor(Color.white);
                gp.fillRect(xLeft,yLeft-psthwid,psthlen,psthwid);

                gp.setColor(new Color(0,0,0));
                gp.setFont(heading);
                gp.drawLine(xLeft,yLeft,xLeft+psthlen,yLeft);
                gp.drawLine(xLeft,yLeft,xLeft,yLeft-psthwid);
                gp.setColor(Color.BLACK);
                gp.drawRect(xLeft,yLeft-psthwid,psthlen,psthwid);
                gp.setFont(f);


		for (int i=minIntens;i<=maxIntens;i+=10){
                        yPos = (int)(psthbinY*i);
                        //gp.setColor(new Color(100,100,100));
                        //gp.drawLine(xLeft,yLeft-yPos,psthlen+xLeft,yLeft-yPos);
                        if (i%50==0){
                                //gp.setFont(f);
                                gp.drawLine(xLeft,yLeft-yPos,psthlen+xLeft,yLeft-yPos);
                                gp.setColor(Color.black);
                                yAxis = ""+(i);
                                gp.drawLine(xLeft-3,yLeft-yPos,xLeft+3,yLeft-yPos);
                                gp.drawString(yAxis,xLeft-10,yLeft-yPos);
                                gp.setColor(new Color(100,100,100));
                        }
                }			
                for (int i=minFreq;i<=maxFreq;i+=1000){
                        xPos = (int)(psthbinX*i);
                        /*if (i!=0){
                                gp.drawLine(xLeft + xPos,yLeft,xLeft+xPos,yLeft-(int)(maxAP*psthbinY));
                        }*/
                        if (i%2000==0){
                                gp.drawLine(xLeft + xPos,yLeft,xLeft+xPos,yLeft-psthwid);
                                gp.setColor(Color.black);
                                xAxis = ""+(i);
                                gp.drawLine(xLeft+xPos,yLeft-3,xLeft+xPos,yLeft+3);
                                gp.drawString(xAxis,xLeft+xPos-5,yLeft+10);
                                gp.setColor(new Color(100,100,100));
                        }
                        /*if (i==num-1){
                                //gp.setFont(f);
                                gp.setColor(Color.black);
                                xAxis = ""+(num*width);
                                gp.drawLine(xLeft+(int)(psthbinX*num),yLeft-3,xLeft+(int)(psthbinX*num),yLeft+3);
                                gp.drawString(xAxis,xLeft+(int)(psthbinX*num)-5,yLeft+10);
                                gp.setColor(new Color(100,100,100));
                        }*/
                }

		for (int i=0; i<neuron.getNumOverview(); i++){
			//System.out.println(neuron.getNumOverview());
			overview = neuron.getOverview(i);
			if (overview[0] > 0){
				gp.setColor(Color.black);
				gp.drawString(""+(int)(overview[0])+","+(int)(overview[1]),xLeft+(int)((double)(psthlen)*overview[0]/(double)(maxFreq-minFreq))-10,yLeft-(int)((double)(psthwid)*overview[1]/(double)(maxIntens-minIntens)));
				gp.setColor(neuronColor((int)(overview[2]),true));
                		gp.fillRect(xLeft+(int)((double)(psthlen)*overview[0]/(double)(maxFreq-minFreq))-1,yLeft-(int)((double)(psthwid)*overview[1]/(double)(maxIntens-minIntens))-1,2*dotSize,2*dotSize);
			}
		}
		return gp;	
	}

	public Graphics drawPSTH(Graphics gp,int k,String type){
 		neuron = neurons.get(k);
		if (type.equals("Frequency")){
			numCount = numCountList.get(k);
			if (neuron.isCustomPSTHy()){
				maxAP = neuron.getCustomPSTHy();
			}
			else{
				maxAP = maxAPList.get(k);
			}
		}
		else if (type.equals("Intensity")){
			numCount = numIntensCountList.get(k);
			if (neuron.isCustomPSTHy()){
                                maxAP = neuron.getCustomPSTHy();
                        }
			else{
				maxAP = maxIntensAPList.get(k);
			}
		}
		else if (type.equals("Ipsi-Intensity") && neuron.isClosedField()){
                        numCount = numIpsiIntensCountList.get(k);
                        if (neuron.isCustomPSTHy()){
                                maxAP = neuron.getCustomPSTHy();
                        }
                        else{
                                maxAP = maxIpsiIntensAPList.get(k);
                        }
                }			
		else if (type.equals("ILD") && neuron.isClosedField()){
                        numCount = numILDCountList.get(k);
                        if (neuron.isCustomPSTHy()){
                                maxAP = neuron.getCustomPSTHy();
                        }
                        else{
                                maxAP = maxILDAPList.get(k);
                        }
                }
		else{
			numCount = numCountList.get(k);
                        maxAP = maxAPList.get(k);
		}
		psthbinX = ((double)(psthlen+0.0)/(double)(num+0.0));
 		psthbinY = ((double)(psthwid+0.0)/(double)(maxAP+0.0));
		timeL = neuron.getlLineX()*width/psthbinX;
 		timeR = neuron.getrLineX()*width/psthbinX;
 		numr = (int)((timeR-timeL)/widthr);//(int)max( (float)(finalListPos(lowpsthRateNeu)), (float)(finalListPos(highpsthRateNeu)) );

		gp.setColor(Color.white);
		gp.fillRect(xLeft,yLeft-psthwid,psthlen,psthwid);

        	gp.setColor(new Color(0,0,0));
		gp.setFont(heading);
		gp.drawLine(xLeft,yLeft,xLeft+psthlen,yLeft);
 		gp.drawLine(xLeft,yLeft,xLeft,yLeft-psthwid);
		gp.setColor(Color.BLACK);
                gp.drawRect(xLeft,yLeft-psthwid,psthlen,psthwid);
 		gp.setFont(f);

 		gp.setColor(new Color(100,100,100));
        	for (int i=0;i<=maxAP;i+=5){
     			yPos = (int)(psthbinY*i);
     			//gp.setColor(new Color(100,100,100));
            		//gp.drawLine(xLeft,yLeft-yPos,psthlen+xLeft,yLeft-yPos);
            		if (i%10==0){
      				//gp.setFont(f);
             			gp.drawLine(xLeft,yLeft-yPos,psthlen+xLeft,yLeft-yPos);
				gp.setColor(Color.black);
             			yAxis = ""+(i);
             			gp.drawLine(xLeft-3,yLeft-yPos,xLeft+3,yLeft-yPos);
             			gp.drawString(yAxis,xLeft-10,yLeft-yPos);
      				gp.setColor(new Color(100,100,100));
     			}
 		}
        	for (int i=0;i<=num;i++){
  			xPos = (int)(psthbinX*i);
  			/*if (i!=0){
   				gp.drawLine(xLeft + xPos,yLeft,xLeft+xPos,yLeft-(int)(maxAP*psthbinY));
         		}*/
  			if (i%10==0){
                 		gp.drawLine(xLeft + xPos,yLeft,xLeft+xPos,yLeft-(int)(maxAP*psthbinY));
   				gp.setColor(Color.black);
                 		xAxis = ""+(i*width);
                 		gp.drawLine(xLeft+xPos,yLeft-3,xLeft+xPos,yLeft+3);
                 		gp.drawString(xAxis,xLeft+xPos-5,yLeft+10);
                 		gp.setColor(new Color(100,100,100));
             		}
                	/*if (i==num-1){
                        	//gp.setFont(f);
                        	gp.setColor(Color.black);
                        	xAxis = ""+(num*width);
                        	gp.drawLine(xLeft+(int)(psthbinX*num),yLeft-3,xLeft+(int)(psthbinX*num),yLeft+3);
                        	gp.drawString(xAxis,xLeft+(int)(psthbinX*num)-5,yLeft+10);
   				gp.setColor(new Color(100,100,100));
                	}*/
		}
  		for (int l=0; l<numCount;l++){
			if (type.equals("Frequency")){
				if (neuron.getFreqSelected(l)<=0){
                                	continue;
                        	}
				psthNeu = neuron.getPSTH(l);
			}
			else if (type.equals("Intensity")){
				if (neuron.getIntenSelected(l)<=0){
                                        continue;
                                }
				psthNeu = neuron.getIntensPSTH(l);
			}
			else if (type.equals("Ipsi-Intensity") && neuron.isClosedField()){
                                if (neuron.getIpsiIntenSelected(l)<=0){
                                        continue;
                                }
                                psthNeu = neuron.getIpsiIntensPSTH(l);
                        }
			else if (type.equals("ILD") && neuron.isClosedField()){
                                if (neuron.getILDSelected(l)<=0){
                                        continue;
                                }
                                psthNeu = neuron.getILDPSTH(l);
                        }				
			else{
				psthNeu = neuron.getPSTH(l);
			}
			ind = neuron.getFreqIndex(l);
                	for (int i=0; i<num;i++){
				xPos = (int)(psthbinX*i);
				if (psthNeu.get(i)!=0){
                                        if ( (xLeft+xPos) >= (xLeft+neuron.getlLineX()) && (xLeft+xPos)< (xLeft+neuron.getrLineX())){
                                                gp.setColor(neuronColor(ind,false));
                                        }
                                        else{
                                                gp.setColor(neuronColor(ind,true));
                                        }
					if ((int)(psthNeu.get(i)*psthbinY) <= psthwid){
                                        	gp.fillRect(xLeft+xPos,yLeft-(int)(psthNeu.get(i)*psthbinY),(int)(psthbinX),(int)(psthNeu.get(i)*psthbinY));
                                        }
					else{
						gp.fillRect(xLeft+xPos,yLeft-psthwid,(int)(psthbinX),psthwid);
					}
					gp.setColor(new Color(100,100,100));
                                }
			}
        	}

 		//DRAGGING LINES:
 		if (neuron.getDraggingLeft()){
  			gp.setColor(new Color(0,200,255));
 		}
 		else{
      			gp.setColor(new Color(0,0,255));
     		}
 		gp.drawLine(neuron.getlLineX()+xLeft,yLeft,xLeft+neuron.getlLineX(),yLeft-psthwid);
 		if (neuron.getDraggingRight()){
                	gp.setColor(new Color(0,200,200));
       	 	}
        	else{
                	gp.setColor(new Color(0,0,200));
        	}
 		gp.drawLine(neuron.getrLineX()+xLeft,yLeft,xLeft+neuron.getrLineX(),yLeft-psthwid);

 		gp.setColor(Color.blue);
 		/*timeL = neuron.getlLineX()*width/psthbinX;
 		timeR = neuron.getrLineX()*width/psthbinX;
		neuron.setrTime((int)timeR);
		neuron.setlTime((int)timeL);
 		numr = (int)((timeR-timeL)/widthr);*/
 		gp.drawString(""+(int)timeL,xLeft+neuron.getlLineX(),yLeft-psthwid+10);
 		gp.drawString(""+(int)timeR,xLeft+neuron.getrLineX(),yLeft-psthwid+20);

 		return gp;
  	}

	public Graphics drawDR(Graphics gp,int k,String type){
 		neuron = neurons.get(k);
		maxSwep = neuron.getMaxSwep();
		if (type.equals("Frequency")){
			numCount = numCountList.get(k);
		}
		else if (type.equals("Intensity")){
			numCount = numIntensCountList.get(k);
		}
		else if (type.equals("Ipsi-Intensity") && neuron.isClosedField()){
                        numCount = numIpsiIntensCountList.get(k);
                }					
		else if (type.equals("ILD") && neuron.isClosedField()){
                        numCount = numILDCountList.get(k);
                }
		else{
			numCount = numCountList.get(k);
		}
		psthbinX = ((double)(psthlen+0.0)/(double)(num+0.0));
                psthbinX2 = ((double)(psthlen2+0.0)/(double)(num2+0.0)); //psthlen2/num2;
        	psthbinY2 = ((double)(psthwid2+0.0)/(double)(maxSwep+0.0));
		//timeL = neuron.getlLineX()*width/psthbinX;
 		//timeR = neuron.getrLineX()*width/psthbinX;
		numr = totTime;
		psthbinXr = ((double)(psthlen+0.0)/(double)(numr+0.0)); 		
		dotSize = neuron.getDotSize();

		gp.setColor(Color.white);
                gp.fillRect(xLeft2,yLeft2-psthwid2,psthlen2,psthwid2);

        	gp.setColor(new Color(0,0,0));
		gp.setFont(heading);
        	//gp.drawLine(xLeft2,yLeft2,xLeft2+psthlen2,yLeft2);
        	//gp.drawLine(xLeft2,yLeft2,xLeft2,yLeft2-psthwid2);
        	gp.setColor(Color.BLACK);
		gp.drawRect(xLeft2,yLeft2-psthwid2,psthlen2,psthwid2);
 		gp.setFont(f);

 		gp.setColor(new Color(100,100,100));
        
 		//DRAW DOT-RASTER
        	gp.setColor(new Color(100,100,100));
   		for (int i=0;i<=maxSwep;i+=20){
  			if (i!=0){
                		gp.drawLine(xLeft2,yLeft2-(int)(psthbinY2*i),psthlen2+xLeft2,yLeft2-(int)(psthbinY2*i));
         		}
  			if (i%40==0){
                        	//gp.setFont(f);
                        	gp.setColor(Color.black);
                        	yAxis = ""+(i);
                        	gp.drawLine(xLeft2-3,yLeft2-(int)(psthbinY2*i),xLeft2+3,yLeft2-(int)(psthbinY2*i));
                        	gp.drawString(yAxis,xLeft2-10,yLeft2-(int)(psthbinY2*i));
                        	gp.setColor(new Color(100,100,100));
                	}
                	if (i==(maxSwep-1)){
                        	//gp.setFont(f);
                        	gp.setColor(Color.black);
                        	yAxis = ""+(maxSwep);
                 		gp.drawLine(xLeft2-3,yLeft2-(int)(psthbinY2*maxSwep),xLeft2+3,yLeft2-(int)(psthbinY2*maxSwep));
                        	gp.drawString(yAxis,xLeft2-10,yLeft2-(int)(psthbinY2)*(maxSwep));
                        	gp.setColor(new Color(100,100,100));
  			}	
 		}
        	for (int i=0;i<=num2;i++){
  			xPos2 = (int)(psthbinX2*i);
  			if (i!=0){
                 		gp.drawLine(xLeft2 + xPos2,yLeft2,xLeft2+xPos2,yLeft2-(int)(maxSwep*psthbinY2));
         		}
  			if (i%10==0){
                        	//gp.setFont(f);
                        	gp.setColor(Color.black);
                        	xAxis = ""+(i*width2);
                        	gp.drawLine(xLeft2+xPos2,yLeft2-3,xLeft2+xPos2,yLeft2+3);
                        	gp.drawString(xAxis,xLeft2+xPos2-5,yLeft2+10);
            		     	gp.setColor(new Color(100,100,100));
  			}
 		}
		
		for (int l=0; l<numCount;l++){
			if (type.equals("Frequency")){
				if (neuron.getFreqSelected(l)<=0){
                                	continue;
                        	}
				DR = neuron.getDR(l);
			}
			else if (type.equals("Intensity")){
				if (neuron.getIntenSelected(l)<=0){
                                	continue;
                        	}
				DR = neuron.getIntensDR(l);
			}
			else if (type.equals("Ipsi-Intensity") && neuron.isClosedField()){
                                if (neuron.getIpsiIntenSelected(l)<=0){
                                        continue;
                                }
                                DR = neuron.getIpsiIntensDR(l);
                        }
			else if (type.equals("ILD") && neuron.isClosedField()){
                                if (neuron.getILDSelected(l)<=0){
                                        continue;
                                }
                                DR = neuron.getILDDR(l);
                        }			
			else{
				DR = neuron.getDR(l);
			}
			ind = neuron.getFreqIndex(l);
			gp.setColor(neuronColor(ind,false));
			if (neuron.getFSLSelected() && neuron.getFSLFreq(l)!=0.0){
                                if ((int)(psthbinXr*neuron.getFSLFreq(l)) <= psthlen){
                                        gp.drawLine(xLeft3 + (int)(psthbinXr*neuron.getFSLFreq(l)),yLeft3,xLeft3 + (int)(psthbinXr*neuron.getFSLFreq(l)),yLeft3-psthwid);
                                }
                        }
			for (int i=0;i<maxSwep;i++){
                        	for (int j=0;j<DR[i].length;j++){
                                	if (DR[i][j]!=0){
                                        	if ( (double)(psthlen)*DR[i][j]/(double)(maxDRTime)>=(double)(neuron.getDRlLineX()) && (double)(psthlen)*DR[i][j]/(double)(maxDRTime)< (double)(neuron.getDRrLineX())){
                                                	gp.setColor(neuronColor(ind,false));
                                        	}
                                		else{
							gp.setColor(neuronColor(ind,true));
                                		}
                                		gp.fillRect(xLeft2+(int)((double)(psthlen)*DR[i][j]/(double)(maxDRTime))-1,yLeft2-(int)((i+1)*(double)psthbinY2)-1,dotSize,dotSize);
                        		}
                		}
			}
		}

 //DRAGGING LINES:
 if (neuron.getDRDraggingRight()){
                gp.setColor(new Color(0,200,200));
        }
        else{
                gp.setColor(new Color(0,0,200));
        }
 gp.drawLine(neuron.getDRrLineX()+xLeft2,yLeft,xLeft2+neuron.getDRrLineX(),yLeft-psthwid);
 if (neuron.getDRDraggingLeft()){
                gp.setColor(new Color(0,200,255));
        }
        else{
                gp.setColor(new Color(0,0,255));
        }
 gp.drawLine(neuron.getDRlLineX()+xLeft2,yLeft,xLeft2+neuron.getDRlLineX(),yLeft-psthwid);

 gp.setColor(Color.blue);
 /*timeL = neuron.getlLineX()*width/psthbinX;
 timeR = neuron.getrLineX()*width/psthbinX;
 numr = (int)((timeR-timeL)/widthr);*/
 gp.drawString(""+(int)((double)(neuron.getDRlLineX())*width/(double)(psthbinX)),xLeft2+neuron.getDRlLineX(),yLeft-psthwid+10);
 gp.drawString(""+(int)((double)(neuron.getDRrLineX())*width/(double)(psthbinX)),xLeft2+neuron.getDRrLineX(),yLeft-psthwid+20);
	/*timeL = neuron.getlLineX()*width/psthbinX;
                timeR = neuron.getrLineX()*width/psthbinX;
                neuron.setrTime((int)timeR);
                neuron.setlTime((int)timeL); */

 //DISPLAY ACTION-POTENTIAL COUNTS
 /*lowPSTHTot = countList(lowpsthNeu,0,lowpsthNeu.size()-1);
 highPSTHTot = countList(highpsthNeu,0,highpsthNeu.size()-1);
 if (lowPSTHTot!=0){
                lowDRPerc = 100.0*(double)(lowDRCount)/(double)(countList(lowpsthNeu,0,lowpsthNeu.size()-1));
                lowDRPerc = Math.round(10*lowDRPerc)/10.0;
        }
        else{
                lowDRPerc = 0.0;
        }
 if (highPSTHTot!=0){
                highDRPerc = 100.0*(double)(highDRCount)/(double)(countList(highpsthNeu,0,highpsthNeu.size()-1));
                highDRPerc = Math.round(10*highDRPerc)/10.0;
        }
        else{
                highDRPerc = 0.0;
        }
 gp.setFont(heading);
        gp.setColor(Color.black);
 gp.drawString(lowDRCount+"/"+lowPSTHTot+" ("+lowDRPerc+"%)",xLeft2+200,yLeft-psthwid-1);
        gp.drawString(highDRCount+"/"+highPSTHTot+" ("+highDRPerc+"%)",xLeft2+335,yLeft2-psthwid-1); 

	neuron.setLowDRCount(lowDRCount);
        neuron.setHighDRCount(highDRCount);
	//}*/

 return gp;
  }

	public Graphics drawRate(Graphics gp,int k, String type){
 		neuron = neurons.get(k);
		//neuron.adjustSpontaneousRate(100);
		//neuron.calculateFSL(100,60);
		if (type.equals("Frequency")){
			numCount = numCountList.get(k);
                	maxAPRate = maxAPRateList.get(k);
		}
		else if (type.equals("Intensity")){
			numCount = numIntensCountList.get(k);
                	maxAPRate = maxIntensAPRateList.get(k);
		}		
		else if (type.equals("Ipsi-Intensity") && neuron.isClosedField()){
                        numCount = numIpsiIntensCountList.get(k);
                        maxAPRate = maxIpsiIntensAPRateList.get(k);
                }
		else if (type.equals("ILD") && neuron.isClosedField()){
                        numCount = numILDCountList.get(k);
                        maxAPRate = maxILDAPRateList.get(k);
                }					
		else{
			numCount = numCountList.get(k);
                	maxAPRate = maxAPRateList.get(k);
		}
 		if (maxAPRate >= 50){
			maxAPRate = 50*(((int)(maxAPRate)+49)/50);//100.0*((int)(maxAPRate)/100);
 		}
		else{
			maxAPRate = 10*(((int)(maxAPRate)+9)/10);
		}
 		psthbinYr = ((double)(psthwid3+0.0)/(double)(maxAPRate+0.0));
		timeL = neuron.getlLineX()*width/psthbinX;
 		timeR = neuron.getrLineX()*width/psthbinX;
 		numr = (int)((timeR-timeL)/widthr);//(int)max( (float)(finalListPos(lowpsthRateNeu)), (float)(finalListPos(highpsthRateNeu)) );
		psthbinXr = ((double)(psthlen+0.0)/(double)(numr+0.0));
 		
		gp.setColor(Color.white);
                gp.fillRect(xLeft3,yLeft3-psthwid3,psthlen3,psthwid3);

        	gp.setColor(new Color(0,0,0));
		gp.setFont(heading);

 		//gp.drawLine(xLeft3,yLeft3,xLeft3+psthlen3,yLeft3);
        	//gp.drawLine(xLeft3,yLeft3,xLeft3,yLeft3-psthwid3);
                gp.drawRect(xLeft3,yLeft3-psthwid3,psthlen3,psthwid3);
		gp.setFont(f);
        	
 		//DRAW RATE PSTHs
 		gp.setColor(new Color(100,100,100));
 		for (int i=0;i<=maxAPRate;i+=50){
  			if (i!=0){
                        	gp.drawLine(xLeft3,yLeft3-(int)(psthbinYr*i),psthlen3+xLeft3,yLeft3-(int)(psthbinYr*i));
                	}
                	if (i%100==0){
                        	gp.setColor(Color.black);
                        	yAxis = ""+(i);
                        	gp.drawLine(xLeft3-3,yLeft3-(int)(psthbinYr*i),xLeft3+3,yLeft3-(int)(psthbinYr*i));
                        	gp.drawString(yAxis,xLeft3-10,yLeft3-(int)(psthbinYr*i));
                        	gp.setColor(new Color(100,100,100));
                	}
                	if (i==(maxAPRate-1)){
                        	gp.setColor(Color.black);
                        	yAxis = ""+(maxSwep);
                        	gp.drawLine(xLeft3-3,yLeft3-(int)(psthbinYr*maxAPRate),xLeft3+3,yLeft3-(int)(psthbinYr*maxAPRate));
                        	gp.drawString(yAxis,xLeft3-10,yLeft3-(int)(psthbinYr*maxAPRate));
                        	gp.setColor(new Color(100,100,100));
                	} 
 		}
 		for (int i=0;i<=numr;i++){
                	if (i%10==0){
   				if (i!=0){
    					gp.drawLine(xLeft3 + (int)(psthbinXr*i),yLeft3,xLeft3+(int)(psthbinXr*i),yLeft3-psthwid3);
                        	}
                        	gp.setColor(Color.black);
                        	xAxis = ""+((int)timeL+i*widthr);
                        	gp.drawLine(xLeft3+(int)(psthbinXr*i),yLeft3-3,xLeft3+(int)(psthbinXr*i),yLeft3+3);
                        	gp.drawString(xAxis,xLeft3+(int)(psthbinXr*i)-5,yLeft3+10);
                        	gp.setColor(new Color(100,100,100));
                	}
        	}
		gp.setFont(small);
		for (int l=0; l<numCount; l++){
			/*if (neuron.getFreqSelected(l)<=0){
                                continue;
                        }*/
			if (type.equals("Frequency")){
				if (neuron.getFreqSelected(l)<=0){
                                	continue;
                        	}
				psthRateNeu = neuron.getRate(l);
				peakLatency = neuron.getPeakLatencyFreq(l);
			}
			else if (type.equals("Intensity")){
				if (neuron.getIntenSelected(l)<=0){
                                        continue;
                                }
				psthRateNeu = neuron.getIntensRate(l);
				peakLatency = neuron.getPeakLatencyInten(l);
                        }						
			else if (type.equals("Ipsi-Intensity") && neuron.isClosedField()){
                                if (neuron.getIpsiIntenSelected(l)<=0){
                                        continue;		
                                }
                                psthRateNeu = neuron.getIpsiIntensRate(l);
                                peakLatency = neuron.getPeakLatencyIpsiInten(l);
                        }
			else if (type.equals("ILD") && neuron.isClosedField()){
                                if (neuron.getILDSelected(l)<=0){
                                        continue;
                                }
                                psthRateNeu = neuron.getILDRate(l);
                                peakLatency = neuron.getPeakLatencyILD(l);
                        }						
			else{
				if (neuron.getFreqSelected(l)<=0){
                                        continue;
                                }
				psthRateNeu = neuron.getRate(l);
				peakLatency = neuron.getPeakLatencyFreq(l);
                        }
			ind = neuron.getFreqIndex(l);
			gp.setColor(neuronColor(ind,false));
			for (int i=0;i<(numr-1);i++){
                        	//gp.setColor(new Color(250,30*l,5*l));
                        	if ( !(psthRateNeu.get(i+(int)timeL)==0.0 && psthRateNeu.get(i+1+(int)timeL)==0.0) ){
                                	gp.drawLine(xLeft3+(int)(psthbinXr*(i)),yLeft3-(int)(psthRateNeu.get(i+(int)timeL)*psthbinYr),xLeft3+(int)(psthbinXr*(i+1)),yLeft3-(int)(psthRateNeu.get(i+1+(int)timeL)*psthbinYr));
                        	}
				if (neuron.getPeakSelected() && i==peakLatency){
					gp.setColor(Color.BLACK);
					gp.fillOval(xLeft3 -1 + (int)(psthbinXr*(i)),yLeft3-1-(int)(psthRateNeu.get(i+(int)timeL)*psthbinYr),2,2);
					gp.drawString("PL"+l+"="+(int)peakLatency*widthr,xLeft3 -10 + (int)(psthbinXr*(i)),yLeft3-4-(int)(psthRateNeu.get(i+(int)timeL)*psthbinYr));
					gp.setColor(neuronColor(ind,false));
				}
			}
			/*for (int i=0; i<psthlen;i++){
				if ( ((double)(i)/psthbinXr) < 250){
					gp.drawOval(xLeft3+i,yLeft3-(int)( neuron.rateSplineY(((double)(i)/psthbinXr) * psthbinYr)),1,1);
				}
			}*/
			if (neuron.getFSLFreq(l)!=0.0 && neuron.getFSLSelected()){
				if ((int)(psthbinXr*neuron.getFSLFreq(l)) <= psthlen){
					gp.drawLine(xLeft3 + (int)(psthbinXr*neuron.getFSLFreq(l)),yLeft3,xLeft3 + (int)(psthbinXr*neuron.getFSLFreq(l)),yLeft3-psthwid);
				}
			}
		}
 
 return gp;
  }
	public Graphics drawCount(Graphics gp, int k, String type){
		neuron = neurons.get(k);
		if (type.equals("Frequency")){
			maxAPCount = neuron.getMaxNumFreq();
			numCount = numCountList.get(k);
		}
		else if (type.equals("Intensity")){
			maxAPCount = neuron.getMaxNumIntens();
			numCount = numIntensCountList.get(k);
		}
		else if (type.equals("Ipsi-Intensity") && neuron.isClosedField()){
                        maxAPCount = neuron.getMaxNumIpsiIntens();
                        numCount = numIpsiIntensCountList.get(k);
		}				
		else if (type.equals("ILD") && neuron.isClosedField()){
                        maxAPCount = neuron.getMaxNumILD();
                        numCount = numILDCountList.get(k);
		}						
		else{
			maxAPCount = neuron.getMaxNumFreq();
			numCount = numCountList.get(k);
		}
		
		if (maxAPCount>=50){
			maxAPCount = 50*((maxAPCount+49)/50);
			delta = 50;
		}
		else{
			maxAPCount = 10*((maxAPCount+9)/10);
			delta = 10;
		}
		psthbinYc = ((double)(psthwid3+0.0)/(double)(maxAPCount+0.0));
		if (numCount > 1){
			psthbinXc = ((double)(psthlen3+0.0)/(double)(numCount-1.0+0.0));
		}
		else{
			psthbinXc = ((double)(psthlen3+0.0)/(2.0));
		}
		gp.setColor(Color.white);
                gp.fillRect(xLeft3,yLeft3-psthwid3,psthlen3,psthwid3);
                gp.setColor(new Color(0,0,0));
                gp.drawLine(xLeft3,yLeft3,xLeft3+psthlen3,yLeft3);
                gp.drawLine(xLeft3,yLeft3,xLeft3,yLeft3-psthwid3);
                gp.drawRect(xLeft3,yLeft3-psthwid3,psthlen3,psthwid3);
                gp.setFont(f);

                //DRAW COUNT LINE-GRAPH
                gp.setColor(new Color(100,100,100));
                
		for (int i=0;i<=maxAPCount;i+=delta){
                        if (i!=0){
                                gp.drawLine(xLeft3,yLeft3-(int)(psthbinYc*i),psthlen3+xLeft3,yLeft3-(int)(psthbinYc*i));
                        }
                        if (i%10==0){
                                gp.setColor(Color.black);
                                gp.drawLine(xLeft3-3,yLeft3-(int)(psthbinYc*i),xLeft3+3,yLeft3-(int)(psthbinYc*i));
				if (i%delta==0){
					yAxis = ""+(i);
					gp.drawString(yAxis,xLeft3-10,yLeft3-(int)(psthbinYc*i));
                                }
				gp.setColor(new Color(100,100,100));
                        }
                        if (i==(maxAPCount-1)){
                                gp.setColor(Color.black);
                                yAxis = ""+(maxAPCount);
                                gp.drawLine(xLeft3-3,yLeft3-(int)(psthbinYc*maxAPCount),xLeft3+3,yLeft3-(int)(psthbinYc*maxAPCount));
                                gp.drawString(yAxis,xLeft3-10,yLeft3-(int)(psthbinYc*maxAPCount));
                                gp.setColor(new Color(100,100,100));
                        }
                }

		if (numCount > 1){
			for (int i=0; i<numCount; i++){
				ind = neuron.getFreqIndex(i);
                        	xPos = (int)(psthbinXc*i);
                        	gp.drawLine(xLeft + xPos,yLeft,xLeft+xPos,yLeft-(int)(maxAPCount*psthbinYc));
                        	gp.setColor(Color.black);
				if (type.equals("Frequency")){
                        		xAxis = "f"+(ind);
                        	}
				else if (type.equals("Intensity")){
					xAxis = "I"+(ind);
				}			
				else if (type.equals("Ipsi-Intensity")){
					xAxis = "iI"+(ind);
				}
				else if (type.equals("ILD")){
					xAxis = "ILD"+(ind);
				}
				gp.drawLine(xLeft+xPos,yLeft-3,xLeft+xPos,yLeft+3);
                        	gp.drawString(xAxis,xLeft+xPos-5,yLeft+10); 
                        	gp.setColor(new Color(100,100,100));
                	}
		}
		else{
			ind = neuron.getFreqIndex(0);
			xPos = (int)(psthbinXc);
                        gp.drawLine(xLeft + xPos,yLeft,xLeft+xPos,yLeft-(int)(maxAPCount*psthbinYc));
                        gp.setColor(Color.black);
              		if (type.equals("Frequency")){
                                xAxis = "f"+ind;
                        }
                        else if (type.equals("Intensity")){
                                xAxis = "I"+ind;
                        }			
			else if (type.equals("Ipsi-Intensity")){
                                xAxis = "iI"+(ind);
                        }
                        else if (type.equals("ILD")){
                                xAxis = "ILD"+(ind);
                        }
                        gp.drawLine(xLeft+xPos,yLeft-3,xLeft+xPos,yLeft+3);
                        gp.drawString(xAxis,xLeft+xPos-5,yLeft+10);
                        gp.setColor(new Color(100,100,100));
		}
		gp.setColor(new Color(0,0,255));
		if (type.equals("Frequency")){
			if (numCount > 1){
				for (int i=0; i<(numCount-1); i++){
					xPos = (int)(psthbinXc*i);
					gp.drawLine(xLeft+xPos,yLeft-(int)(neuron.getAPCount(i)*psthbinYc),xLeft+(int)(psthbinXc*(i+1)),yLeft-(int)(neuron.getAPCount(i+1)*psthbinYc));
					gp.setColor(neuron.getCountDotSelected(i));
					gp.fillOval(xLeft+xPos-cDotSize/2, yLeft-(int)(neuron.getAPCount(i)*psthbinYc)-cDotSize/2,cDotSize,cDotSize);
					gp.setColor(neuron.getCountDotSelected(i+1));
					gp.fillOval(xLeft+xPos+(int)(psthbinXc)-cDotSize/2, yLeft-(int)(neuron.getAPCount(i+1)*psthbinYc)-cDotSize/2,cDotSize,cDotSize);
					
					//Tot Count Graphics
					gp.setColor(new Color(255,0,0));
					gp.drawLine(xLeft+xPos,yLeft-(int)(neuron.getTotAPCount(i)*psthbinYc),xLeft+(int)(psthbinXc*(i+1)),yLeft-(int)(neuron.getTotAPCount(i+1)*psthbinYc));
					gp.fillOval(xLeft+xPos-cDotSize/2, yLeft-(int)(neuron.getTotAPCount(i)*psthbinYc)-cDotSize/2,cDotSize,cDotSize);
                                        gp.fillOval(xLeft+xPos+(int)(psthbinXc)-cDotSize/2, yLeft-(int)(neuron.getTotAPCount(i+1)*psthbinYc)-cDotSize/2,cDotSize,cDotSize);

				}											
			}
			else{
				xPos = (int)psthbinXc;
				gp.fillOval(xLeft+xPos-cDotSize/2,yLeft-(int)(neuron.getAPCount(0)*psthbinYc)-cDotSize/2,cDotSize,cDotSize);
				gp.setColor(new Color(255,0,0));
				gp.fillOval(xLeft+xPos-cDotSize/2,yLeft-(int)(neuron.getTotAPCount(0)*psthbinYc)-cDotSize/2,cDotSize,cDotSize);
			}	
		}
		else if (type.equals("Intensity")){
			if (numCount > 1){
				for (int i=0; i<(numCount-1); i++){
                                	xPos = (int)(psthbinXc*i);
                                	gp.drawLine(xLeft+xPos,yLeft-(int)(neuron.getIntensAPCount(i)*psthbinYc),xLeft+(int)(psthbinXc*(i+1)),yLeft-(int)(neuron.getIntensAPCount(i+1)*psthbinYc));
                        		gp.fillOval(xLeft+xPos-cDotSize/2, yLeft-(int)(neuron.getIntensAPCount(i)*psthbinYc)-cDotSize/2,cDotSize,cDotSize);
					gp.fillOval(xLeft+xPos+(int)(psthbinXc)-cDotSize/2, yLeft-(int)(neuron.getIntensAPCount(i+1)*psthbinYc)-cDotSize/2,cDotSize,cDotSize);
				
					//Tot Count Graphics
                                        gp.setColor(new Color(255,0,0));
                                        gp.drawLine(xLeft+xPos,yLeft-(int)(neuron.getTotIntensAPCount(i)*psthbinYc),xLeft+(int)(psthbinXc*(i+1)),yLeft-(int)(neuron.getTotIntensAPCount(i+1)*psthbinYc));
                                        gp.fillOval(xLeft+xPos-cDotSize/2, yLeft-(int)(neuron.getTotIntensAPCount(i)*psthbinYc)-cDotSize/2,cDotSize,cDotSize);
                                        gp.fillOval(xLeft+xPos+(int)(psthbinXc)-cDotSize/2, yLeft-(int)(neuron.getTotIntensAPCount(i+1)*psthbinYc)-cDotSize/2,cDotSize,cDotSize);
				}		
			}
			else{
                                xPos = (int)psthbinXc;
                                gp.fillOval(xLeft+xPos-cDotSize/2,yLeft-cDotSize/2-(int)(neuron.getIntensAPCount(0)*psthbinYc),cDotSize,cDotSize);
                        	gp.setColor(new Color(255,0,0));
				gp.fillOval(xLeft+xPos-cDotSize/2,yLeft-cDotSize/2-(int)(neuron.getTotIntensAPCount(0)*psthbinYc),cDotSize,cDotSize);
			}
		}
		else if (type.equals("Ipsi-Intensity")){
                        //System.out.println("LINE 1263: ipsiCount = "+neuron.getIpsiIntensAPCount(0));
			if (numCount > 1){
                                for (int i=0; i<(numCount-1); i++){
                                        xPos = (int)(psthbinXc*i);
                                        gp.drawLine(xLeft+xPos,yLeft-(int)(neuron.getIpsiIntensAPCount(i)*psthbinYc),xLeft+(int)(psthbinXc*(i+1)),yLeft-(int)(neuron.getIpsiIntensAPCount(i+1)*psthbinYc));
                                	gp.fillOval(xLeft+xPos-cDotSize/2, yLeft-(int)(neuron.getIpsiIntensAPCount(i)*psthbinYc)-cDotSize/2,cDotSize,cDotSize);
					gp.fillOval(xLeft+xPos+(int)(psthbinXc)-cDotSize/2, yLeft-(int)(neuron.getIpsiIntensAPCount(i+1)*psthbinYc)-cDotSize/2,cDotSize,cDotSize);
				
					//Tot Count Graphics
                                        gp.setColor(new Color(255,0,0));
                                        gp.drawLine(xLeft+xPos,yLeft-(int)(neuron.getTotIpsiIntensAPCount(i)*psthbinYc),xLeft+(int)(psthbinXc*(i+1)),yLeft-(int)(neuron.getTotIpsiIntensAPCount(i+1)*psthbinYc));
                                        gp.fillOval(xLeft+xPos-cDotSize/2, yLeft-(int)(neuron.getTotIpsiIntensAPCount(i)*psthbinYc)-cDotSize/2,cDotSize,cDotSize);
                                        gp.fillOval(xLeft+xPos+(int)(psthbinXc)-cDotSize/2, yLeft-(int)(neuron.getTotIpsiIntensAPCount(i+1)*psthbinYc)-cDotSize/2,cDotSize,cDotSize);
				}
                        }
                        else{
                                xPos = (int)psthbinXc;
                                gp.fillOval(xLeft+xPos-cDotSize/2,yLeft-cDotSize/2-(int)(neuron.getIpsiIntensAPCount(0)*psthbinYc),cDotSize,cDotSize);
                        	gp.setColor(new Color(255,0,0));
				gp.fillOval(xLeft+xPos-cDotSize/2,yLeft-cDotSize/2-(int)(neuron.getTotIpsiIntensAPCount(0)*psthbinYc),cDotSize,cDotSize);
			}
                }				
		else if (type.equals("ILD")){
			//System.out.println("LINE 1277: ILDcount = "+ neuron.getILDAPCount(0));
                        if (numCount > 1){
                                for (int i=0; i<(numCount-1); i++){
                                        xPos = (int)(psthbinXc*i);
                                        gp.drawLine(xLeft+xPos,yLeft-(int)(neuron.getILDAPCount(i)*psthbinYc),xLeft+(int)(psthbinXc*(i+1)),yLeft-(int)(neuron.getILDAPCount(i+1)*psthbinYc));
                                	gp.fillOval(xLeft+xPos-cDotSize/2, yLeft-(int)(neuron.getILDAPCount(i)*psthbinYc)-cDotSize/2,cDotSize,cDotSize);
					gp.fillOval(xLeft+xPos+(int)(psthbinXc)-cDotSize/2, yLeft-(int)(neuron.getILDAPCount(i+1)*psthbinYc)-cDotSize/2,cDotSize,cDotSize);
					
					//Tot Count Graphics
                                        gp.setColor(new Color(255,0,0));
                                        gp.drawLine(xLeft+xPos,yLeft-(int)(neuron.getTotILDAPCount(i)*psthbinYc),xLeft+(int)(psthbinXc*(i+1)),yLeft-(int)(neuron.getTotILDAPCount(i+1)*psthbinYc));
                                        gp.fillOval(xLeft+xPos-cDotSize/2, yLeft-(int)(neuron.getTotILDAPCount(i)*psthbinYc)-cDotSize/2,cDotSize,cDotSize);                            
					gp.fillOval(xLeft+xPos+(int)(psthbinXc)-cDotSize/2, yLeft-(int)(neuron.getTotILDAPCount(i+1)*psthbinYc)-cDotSize/2,cDotSize,cDotSize);
				}
                        }
                        else{
                                xPos = (int)psthbinXc;
                                gp.fillOval(xLeft+xPos-cDotSize/2,yLeft-cDotSize/2-(int)(neuron.getILDAPCount(0)*psthbinYc),cDotSize,cDotSize);
				gp.setColor(new Color(255,0,0));
				gp.fillOval(xLeft+xPos-cDotSize/2,yLeft-cDotSize/2-(int)(neuron.getTotILDAPCount(0)*psthbinYc),cDotSize,cDotSize);
                        }
		}
		return gp;
	}

	public Graphics drawAngleCount(Graphics gp, int k){
		int onzero = 0;
		maxAPCount = 0;
		numCount = 5;
		int p1 = 0;
		int p2 = 0;
		if (k<1){
			onzero = 1;
			for (int i=0; i<5; i++){
                        	neuron = neurons.get(i);
                        	maxAPCount = (int)max((float)maxAPCount,(float)neuron.getMaxNumFreq());
                	}
		}
		else{
			onzero = 0;
			for (int i=5; i<10; i++){
                        	neuron = neurons.get(i);
                        	maxAPCount = (int)max((float)maxAPCount,(float)neuron.getMaxNumFreq());
                	}
		}

		if (maxAPCount>=50){
			maxAPCount = 50*((maxAPCount+49)/50);
			delta = 50;
		}
		else{
			maxAPCount = 10*((maxAPCount+9)/10);
			delta = 10;
		}
		psthbinYc = ((double)(psthwid3+0.0)/(double)(maxAPCount+0.0));
		psthbinXc = ((double)(psthlen3+0.0)/(double)(numCount-1.0+0.0));
		gp.setColor(Color.white);
                gp.fillRect(xLeft3,yLeft3-psthwid3,psthlen3,psthwid3);
                gp.setColor(new Color(0,0,0));
                gp.drawLine(xLeft3,yLeft3,xLeft3+psthlen3,yLeft3);
                gp.drawLine(xLeft3,yLeft3,xLeft3,yLeft3-psthwid3);
                gp.drawRect(xLeft3,yLeft3-psthwid3,psthlen3,psthwid3);
                gp.setFont(f);

                //DRAW COUNT LINE-GRAPH
                gp.setColor(new Color(100,100,100));
                
		for (int i=0;i<=maxAPCount;i+=delta){
                        if (i!=0){
                                gp.drawLine(xLeft3,yLeft3-(int)(psthbinYc*i),psthlen3+xLeft3,yLeft3-(int)(psthbinYc*i));
                        }
                        if (i%10==0){
                                gp.setColor(Color.black);
                                gp.drawLine(xLeft3-3,yLeft3-(int)(psthbinYc*i),xLeft3+3,yLeft3-(int)(psthbinYc*i));
				if (i%delta==0){
					yAxis = ""+(i);
					gp.drawString(yAxis,xLeft3-10,yLeft3-(int)(psthbinYc*i));
                                }
				gp.setColor(new Color(100,100,100));
                        }
                        if (i==(maxAPCount-1)){
                                gp.setColor(Color.black);
                                yAxis = ""+(maxAPCount);
                                gp.drawLine(xLeft3-3,yLeft3-(int)(psthbinYc*maxAPCount),xLeft3+3,yLeft3-(int)(psthbinYc*maxAPCount));
                                gp.drawString(yAxis,xLeft3-10,yLeft3-(int)(psthbinYc*maxAPCount));
                                gp.setColor(new Color(100,100,100));
                        }
                }

		for (int i=0; i<numCount; i++){
                       	xPos = (int)(psthbinXc*i);
                       	gp.drawLine(xLeft + xPos,yLeft,xLeft+xPos,yLeft-(int)(maxAPCount*psthbinYc));
                       	gp.setColor(Color.black);
			if (i==0){
                        	xAxis = "C90";
                	}
                	else if (i==1){
               	        	xAxis = "C45";
               	 	}
                	else if (i==2){
                	        xAxis = "MID (0)";
                	}
                	else if (i==3){
                	        xAxis = "I45";
                	}
                	else if (i==4){
                        	xAxis = "I90";
                	}
			gp.drawLine(xLeft+xPos,yLeft-3,xLeft+xPos,yLeft+3);
                       	gp.drawString(xAxis,xLeft+xPos-5,yLeft+10); 
                       	gp.setColor(new Color(100,100,100));
                }
		gp.setColor(new Color(0,0,255));
		for (int i=0; i<(numCount-1); i++){
			if (onzero == 1){
				xPos = (int)(psthbinXc*i);
				//ind = neuron.getFreqIndex(i);
				gp.drawLine(xLeft+xPos,yLeft-(int)(neurons.get(i).getAPCount(0)*psthbinYc),xLeft+(int)(psthbinXc*(i+1)),yLeft-(int)(neurons.get(i+1).getAPCount(0)*psthbinYc));
				gp.setColor(new Color(255,0,0));
				if (neurons.get(i).getNumFreqs()<=1){
					p1 = 0;
				}
				else{
					p1 = neurons.get(i).getAPCount(1);
				}
				if (neurons.get(i+1).getNumFreqs()<=1){
                                        p2 = 0;
                                }
                                else{
                                        p2 = neurons.get(i+1).getAPCount(1);
                                }
				gp.drawLine(xLeft+xPos,yLeft-(int)(p1*psthbinYc),xLeft+(int)(psthbinXc*(i+1)),yLeft-(int)(p2*psthbinYc));
				gp.setColor(new Color(0,0,255));
			}
			else if (onzero == 0){
                                xPos = (int)(psthbinXc*i);
                                gp.drawLine(xLeft+xPos,yLeft-(int)(neurons.get(i+5).getAPCount(0)*psthbinYc),xLeft+(int)(psthbinXc*(i+1)),yLeft-(int)(neurons.get(i+6).getAPCount(0)*psthbinYc));
				gp.setColor(new Color(255,0,0));
				if (neurons.get(i+5).getNumFreqs()<=1){
                                        p1 = 0;
                                }
                                else{
                                        p1 = neurons.get(i+5).getAPCount(1);
                                }
                                if (neurons.get(i+6).getNumFreqs()<=1){
                                        p2 = 0;
                                }
                                else{
                                        p2 = neurons.get(i+6).getAPCount(1);
                                }
				gp.drawLine(xLeft+xPos,yLeft-(int)(p1*psthbinYc),xLeft+(int)(psthbinXc*(i+1)),yLeft-(int)(p2*psthbinYc));
				gp.setColor(new Color(0,0,255));
			}	
	
		}
		return gp;
	}
  
  /*public void paint(Graphics g){
    if (dbImage == null){
      dbImage = createImage(maxlen,maxwid);
      dbg = dbImage.getGraphics();
    }
    //dbg.setFont(f);
    dbg.setColor(new Color(255,255,255));
    dbg.fillRect(0,0,maxlen,maxwid);
    resetGraphPos();
    for (int k=0; k<numNeurons;k++){
 dbg = drawPSTH(dbg,k);
    }
    g.drawImage(dbImage,0,0,this);
  }*/
	/*public Graphics displayGraphs(Graphics gp){
   		for (int k=0; k<numNeurons;k++){
 			gp = drawPSTH(gp,k);
			gp = drawDR(gp,k);
			gp = drawRate(gp,k);
   		}
   		return gp;
  	}*/

	private Color neuronColor(int l, boolean notInLines){
		Color col;
		int trans;
		if (notInLines){
			trans = trans2 + 20*l;
			if (l==0){
                                col = new Color(0,0,255,trans2);
                        }
                        else if (l==1){
                                col =  new Color(255,0,0,trans2);
                        }
                        else if (l==2){
                                col = new Color(0,255,0,trans2);
                        }
                        else if (l==3){
                                col = new Color(255,128,0,trans2);
                        }
                        else if (l==4){
                                col = new Color(255,255,0,trans2);
                        }
                        else if (l==5){
                                col = new Color(128,255,50,trans2);
                        }
                        else if (l==6){
                                col = new Color(0,150,150,trans2);
                        }
                        else if (l==7){
                                col = new Color(255,0,255,trans2);
                        }
                        else{
                                col = new Color(100,0,100,trans2);
                        }
		}
		else{
			trans = trans1 + 20*l;
			if (l==0){
				col = new Color(0,0,255,trans1);
			}
			else if (l==1){
				col =  new Color(255,0,0,trans1);
			}
			else if (l==2){
				col = new Color(0,255,0,trans1);
			}
			else if (l==3){
				col = new Color(255,128,0,trans1);
			}
			else if (l==4){
				col = new Color(255,255,0,trans1);
			}
			else if (l==5){
				col = new Color(128,255,50,trans1);
			}
			else if (l==6){
				col = new Color(0,150,150,trans1);
			}
			else if (l==7){
				col = new Color(255,0,255,trans1);
			}
			else{
				col = new Color(100,0,100,trans1);
			}
		}
		return col;
	}

	public int getlTime(int i){
		return neurons.get(i).getlTime();
	}
	public int getrTime(int i){
                return neurons.get(i).getrTime();
        }
	public void setTimeWindow(int lTime, int rTime){
		for (int i=0; i<numNeurons; i++){
			if (lTime < rTime){
				if (rTime <= totTime){
					neurons.get(i).setrLineX((int)((double)rTime*(double)(psthbinX)/(double)(width)));
					neurons.get(i).setrLineX((int)(psthbinX)*((neurons.get(i).getrLineX())/(int)(psthbinX)));
					if (rTime%5!=0){
						neurons.get(i).setDRrLineX(1+(int)((double)rTime*(double)(psthbinX)/(double)(width)));
					}
					else{
						neurons.get(i).setDRrLineX((int)((double)rTime*(double)(psthbinX)/(double)(width)));

					}
					neurons.get(i).setrTime(rTime);
				}
				else{
					neurons.get(i).setrLineX(psthlen);
					neurons.get(i).setDRrLineX(psthlen);
					neurons.get(i).setrTime(totTime);
				}
				if (lTime >= 0){
					neurons.get(i).setlLineX((int)((double)lTime*(double)(psthbinX)/(double)(width)));
					neurons.get(i).setlLineX((int)(psthbinX)*((neurons.get(i).getlLineX())/(int)(psthbinX)));
					if (lTime%5!=0){
						neurons.get(i).setDRlLineX(1+(int)((double)lTime*(double)(psthbinX)/(double)(width)));
                                	}
					else{
						neurons.get(i).setDRlLineX((int)((double)lTime*(double)(psthbinX)/(double)(width)));

					}
					neurons.get(i).setlTime(lTime);
				}
                                else{
					neurons.get(i).setlLineX(0);
					neurons.get(i).setDRlLineX(0);
					neurons.get(i).setlTime(0);
                                }
				//System.out.println("L-TIME: "+neurons.get(i).getlTime()+", R-TIME: "+neurons.get(i).getrTime());
			}
		}
	}

	public void setIndivWindow(int i,int lTime, int rTime){
		if (lTime < rTime){
                                if (rTime <= totTime){
                                        neurons.get(i).setrLineX((int)((double)rTime*(double)(psthbinX)/(double)(width)));
                                        neurons.get(i).setrLineX((int)(psthbinX)*((neurons.get(i).getrLineX())/(int)(psthbinX)));
                                        if (rTime%5!=0){
                                                neurons.get(i).setDRrLineX(1+(int)((double)rTime*(double)(psthbinX)/(double)(width)));
                                        }
                                        else{
                                                neurons.get(i).setDRrLineX((int)((double)rTime*(double)(psthbinX)/(double)(width)));

                                        }
                                        neurons.get(i).setrTime(rTime);
                                }
                                else{
                                        neurons.get(i).setrLineX(psthlen);
                                        neurons.get(i).setDRrLineX(psthlen);
                                        neurons.get(i).setrTime(totTime);
                                }
                                if (lTime >= 0){
                                        neurons.get(i).setlLineX((int)((double)lTime*(double)(psthbinX)/(double)(width)));
                                        neurons.get(i).setlLineX((int)(psthbinX)*((neurons.get(i).getlLineX())/(int)(psthbinX)));
                                        if (lTime%5!=0){
                                                neurons.get(i).setDRlLineX(1+(int)((double)lTime*(double)(psthbinX)/(double)(width)));
                                        }
                                        else{
                                                neurons.get(i).setDRlLineX((int)((double)lTime*(double)(psthbinX)/(double)(width)));

                                        }
                                        neurons.get(i).setlTime(lTime);
                                }
                                else{
                                        neurons.get(i).setlLineX(0);
                                        neurons.get(i).setDRlLineX(0);
                                        neurons.get(i).setlTime(0);
                                }
                        }
	}

	public int getNumFreqs(int l){
		return neurons.get(l).getNumFreqs();
	}
	public int getNumIntens(int l){
		return neurons.get(l).getNumIntens();
	}
	public int getNumIpsiIntens(int l){
		return neurons.get(l).getNumIpsiIntens();
	}
	public int getNumILD(int l){
                return neurons.get(l).getNumILD();
        }						
	public ArrayList<Integer> getPSTH(int l, int i){
		return neurons.get(l).getPSTH(i);
	}
	public int getCount(int l, int i){
		return neurons.get(l).getCount(i);
	}
	public ArrayList<Double> getRate(int l, int i){
		return neurons.get(l).getRate(i);
	}

	//CALC-MAX FUNCTIONS:
	private int calcMaxAP(psthClass neu){
		int x = maxList(neu.getPSTH(0));
		for (int i=0; i<neu.getNumFreqs();i++){
			x = (int)max((float)x,(float)maxList(neu.getPSTH(i)));
		
		}
		return x;
	}
	private int calcIntensMaxAP(psthClass neu){
		int x = maxList(neu.getIntensPSTH(0));
		for (int i=0; i<neu.getNumIntens();i++){
                        x = (int)max((float)x,(float)maxList(neu.getIntensPSTH(i)));
                }
                return x;
	}
	private int calcIpsiIntensMaxAP(psthClass neu){
                int x = maxList(neu.getIpsiIntensPSTH(0));
                for (int i=0; i<neu.getNumIpsiIntens();i++){
                        x = (int)max((float)x,(float)maxList(neu.getIpsiIntensPSTH(i)));
                }
                return x;
        }
	private int calcILDMaxAP(psthClass neu){
                int x = maxList(neu.getILDPSTH(0));
                for (int i=0; i<neu.getNumILD();i++){
                        x = (int)max((float)x,(float)maxList(neu.getILDPSTH(i)));
                }							
                return x;
        }	

	private double calcMaxAPRate(psthClass neu){
                double x = maxDoubleList(neu.getRate(0));
                for (int i=0; i<neu.getNumFreqs();i++){
                        x = (int)max((float)x,(float)maxDoubleList(neu.getRate(i)));
                }		
                return x;
        }
	private double calcIntensMaxAPRate(psthClass neu){
                double x = maxDoubleList(neu.getIntensRate(0));
                for (int i=0; i<neu.getNumIntens();i++){
                        x = (int)max((float)x,(float)maxDoubleList(neu.getIntensRate(i)));
                }
                return x;
        }
	private double calcIpsiIntensMaxAPRate(psthClass neu){
                double x = maxDoubleList(neu.getIpsiIntensRate(0));
                for (int i=0; i<neu.getNumIpsiIntens();i++){
                        x = (int)max((float)x,(float)maxDoubleList(neu.getIpsiIntensRate(i)));
                }
                return x;
        }
	private double calcILDMaxAPRate(psthClass neu){
                double x = maxDoubleList(neu.getILDRate(0));
                for (int i=0; i<neu.getNumILD();i++){
                        x = (int)max((float)x,(float)maxDoubleList(neu.getILDRate(i)));
                }	
                return x;
        }
	
	//------------------

	public File[] getFiles(){
		return files;
	}

	public void addNeuron(psthClass neu){
		neurons.add(neu);
	}

	public void saveFile(File f, String experiment) throws IOException{
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream((f+".nlab")));
		out.writeInt(numNeurons);
		out.writeObject(experiment);
		for (int i=0; i<numNeurons; i++){
			try{
				out.writeObject(files[i]);
				out.writeInt(neurons.get(i).getlTime());
				out.writeInt(neurons.get(i).getrTime());
				out.writeObject(neurons.get(i).getTitles("basic"));
				out.writeObject(neurons.get(i).getTitles("adv"));
			}
			catch (NotSerializableException e){
				System.out.println("Neuron "+i+"not serializable");
				continue;
			}
		}
		out.close();
		/*for (int i=0; i<numNeurons; i++){
			f.write(files[i].getName()+","+files[i].getPath().toString()+","+neurons.get(i).getlLineX()+","+neurons.get(i).getrLineX()+"\n");
		}*/
		//f.close();
        }
	public void getPSTHFile(FileWriter f, int ind) throws IOException{
		neuron = neurons.get(ind);
		numCount = neuron.getNumFreqs();
		psthNeu = neuron.getPSTH(0);
		f.write(files[ind].getName()+"\n");
		f.write("TIME(BIN-WIDTH="+width+"),");
		
		for (int i=0; i<numCount; i++){
			if (neuron.getNumFreqs()==1){
                                if (neuron.getFreqIndex(i)==0){
                                        f.write("f0 COUNT,");
                                        f.write("f1 COUNT,");
                                }
                                else if (neuron.getFreqIndex(i)==1){
                                        f.write("f0 COUNT,");
                 	                f.write("f1 COUNT,");
                                }
                         }
                         else{
                                f.write("f"+neuron.getFreqIndex(i)+" COUNT,");
                         }
		}
		f.write("\n");
		for (int i=-1;i<psthNeu.size();i++){
			f.write((i+1)*width+",");
			for (int j=0; j< numCount; j++){
				if (numCount == 1){
					if (neuron.getFreqIndex(j)==0){
                                                if (i==-1){
							f.write(0+","+0+",");
						}
						else{ 
							f.write(neuron.getPSTH(j).get(i)+",");
                       		        		f.write(0+",");
                                        	}
					}
                                        else if (neuron.getFreqIndex(j)==1){
						if (i==-1){
                                                        f.write(0+","+0+",");
                                                }
						else{
                                        		f.write(0+",");
                                            		f.write(neuron.getPSTH(j).get(i)+",");
                                		}
					}
				}
				else{
					if (i!=-1){
						f.write(neuron.getPSTH(j).get(i)+",");
					}
					else{
						f.write(0+",");
					}
				}
			}
			f.write("\n");
		}
		f.close();
	}	
	public void getAllPSTHFile(FileWriter f) throws IOException{
		f.write(",");
		for (int i=0; i<numNeurons; i++){
                        neuron = neurons.get(i);
			f.write(files[i].getName()+",");
			if (neuron.getNumFreqs() > 1){
                        	for (int j=0; j<(neuron.getNumFreqs()-1); j++){
					f.write(",");
				}
			}
			else{
				f.write(",");
			}
                        f.write(",");
                }
		f.write("\n");
		f.write("TIME(BIN-WIDTH="+width+"),");
		for (int i=0; i<numNeurons; i++){
			neuron = neurons.get(i);
			for (int j=0; j< neuron.getNumFreqs(); j++){
				if (neuron.getNumFreqs()==1){
					if (neuron.getFreqIndex(j)==0){
						f.write("f0 COUNT,");
						f.write("f1 COUNT,");
					}
					else if (neuron.getFreqIndex(j)==1){
						f.write("f0 COUNT,");
						f.write("f1 COUNT,");
					}
				}
				else{
					f.write("f"+neuron.getFreqIndex(j)+" COUNT,");
				}
			}
			f.write(",");
		}
		f.write("\n");

		psthNeu = neuron.getPSTH(0);
		for (int i=-1; i<psthNeu.size();i++){
			f.write((i+1)*width+",");
			for (int j=0; j<numNeurons;j++){
				neuron = neurons.get(j);
				for (int k=0; k<neuron.getNumFreqs();k++){
					if (neuron.getNumFreqs() == 1){
						if (i==-1){
                                                        f.write(0+","+0+",");
                                                }
						else{
							if (neuron.getFreqIndex(k)==0){
								f.write(neuron.getPSTH(k).get(i)+",");
								f.write(0+",");	
							}
							else if (neuron.getFreqIndex(k) == 1){
								f.write(0+",");
								f.write(neuron.getPSTH(k).get(i)+",");
							}
						}
					}
					else{
						if (i==-1){
                                                        f.write(0+",");
                                                }
						else{
							f.write(neuron.getPSTH(k).get(i)+",");
						}
					}
				}
				f.write(",");
			}
			f.write("\n");
		}
               	f.close();
        }

	public void getSSAFile(FileWriter f, int ind) throws IOException{
		neuron = neurons.get(ind);
		neuron.SSA_Analysis();
		ArrayList<Double> indiv;
		ArrayList<Integer> missingSweps = neuron.getMissingSweps_SSA();
		String[] types = {"LBH","LBL","HBL","HBH"};
		int sweps = neuron.numSweps();
		
		f.write(files[ind].getName()+"\n");
		f.write("Num,,Count LBH,FSL LBH,,Count LBL,FSL LBL,,Count HBL,FSL HBL,,Count HBH, FSL HBH\n");
		for (int i=0; i<sweps; i++){
			f.write((i)+",,"+neuron.getCount_SSA("LBH",i)+","+neuron.getFSL_SSA("LBH",i)+",,"+neuron.getCount_SSA("LBL",i)+","+neuron.getFSL_SSA("LBL",i)+",,"+neuron.getCount_SSA("HBL",i)+","+neuron.getFSL_SSA("HBL",i)+",,"+neuron.getCount_SSA("HBH",i)+","+neuron.getFSL_SSA("HBH",i)+"\n");
		}
		f.write("\n");
		f.write("\n");
		f.write("Missing Sweeps (Sweep not recorded),");
		for (int i=0; i<missingSweps.size(); i++){
			f.write(missingSweps.get((i))+",");
		}
		f.write("\n");
                f.write("\n");
		for (int i=0; i<types.length; i++){
			f.write(types[i]+" COUNT\n");
			for (int j=0; j<sweps; j++){
				indiv = neuron.getIndivCount_SSA(types[i],j);
				f.write((j)+",,");
				if (indiv!=null){
					for (int k=0; k<indiv.size(); k++){
						f.write(indiv.get(k)+",");
					}
				}
				else{
					f.write(",");
				}
				f.write("\n");
			}
			f.write("\n\n");
		}
		f.close();
	}

	public void getCountFile(FileWriter f, int ind) throws IOException{
		neuron = neurons.get(ind);
		String fType = "";
		numCount = neuron.getNumFreqs();
		f.write(files[ind].getName()+"\n");
		f.write(",FREQUENCY#,COUNT,PEAK-LATENCY(ms)\n");
		timeL = (double)(neuron.getDRlLineX())*(double)(width)/(double)psthbinX;
                timeR = (double)(neuron.getDRrLineX())*(double)(width)/(double)psthbinX;
                neuron.setlTime((int)(timeL));
                neuron.setrTime((int)(timeR));
		for (int i=0; i<numCount;i++){
			ind = neuron.getFreqIndex(i);
			peakLatency = neuron.getPeakLatencyFreq(i);
			if (i==0){
			      if (ind==0 && numCount<2){
       	                               fType = "High=";
                              }
                              else if (ind==1 && numCount<=2){
				       fType = "Low=";
                              }
			      else{       
				       fType = "";
                              }
                              f.write("L-TIME="+(int)(neuron.getlTime())+","+fType+(ind)+","+neuron.getAPCount(i)+","+peakLatency+"\n");
                      	}
                        else if (i==1){
                              if (ind==1 && numCount<=2){
                                       fType = "Low=";
                              }
                              else{
                                       fType = "";
                              }
                              f.write("R-TIME="+(int)(neuron.getrTime())+","+fType+(ind)+","+neuron.getAPCount(i)+","+peakLatency+"\n");
                        }
      		        else{
                              f.write(","+(ind)+","+neuron.getAPCount(i)+","+peakLatency+"\n");
                	}

		}
		f.close();
	}
        public void getAllCountFile(FileWriter f) throws IOException{
                String fType = "";
		for (int j=0; j<numNeurons; j++){
			neuron = neurons.get(j);
                	timeL = (double)(neuron.getDRlLineX())*(double)(width)/(double)psthbinX;
                	timeR = (double)(neuron.getDRrLineX())*(double)(width)/(double)psthbinX;
                	neuron.setlTime((int)(timeL));
                	neuron.setrTime((int)(timeR));
			numCount = neuron.getNumFreqs();
                	f.write("FILE "+(j+1)+": "+ files[j].getName()+"\n");
                	f.write(",FREQUENCY#,COUNT,PEAK-LATENCY(ms)\n");
                	for (int i=0; i<numCount;i++){
				ind = neuron.getFreqIndex(i);
				if (ind==0 && numCount <= 2){
                                       fType = "High=";
                                }
				else if (ind==1 && numCount <= 2){
                                       fType = "Low=";
                                }
                                else{
                                       fType = "";
                                }
				peakLatency = neuron.getPeakLatencyFreq(i);
				if (i==0){
					//f.write("L-TIME="+(int)(neuron.getlTime())+","+fType+(ind)+","+neuron.getAPCount(i)+","+peakLatency+"\n");
                			if (numCount<=1){
						if (ind==0){
							f.write("L-TIME="+(int)(neuron.getlTime())+","+"High="+(0)+","+neuron.getAPCount(i)+","+peakLatency+"\n");

							f.write("R-TIME="+(int)(neuron.getrTime())+","+"Low="+(1)+",0,0\n");
						}
						else if (ind==1){
							f.write("L-TIME="+(int)(neuron.getlTime())+","+"High="+(0)+",0,0\n");
							f.write("R-TIME="+(int)(neuron.getrTime())+","+"Low="+(1)+","+neuron.getAPCount(i)+","+peakLatency+"\n");

						}
					}
					else{
						f.write("L-TIME="+(int)(neuron.getlTime())+","+fType+(ind)+","+neuron.getAPCount(i)+","+peakLatency+"\n");
					}
				}
				else if (i==1){
                                        f.write("R-TIME="+(int)(neuron.getrTime())+","+fType+(ind)+","+neuron.getAPCount(i)+","+peakLatency+"\n");
                                }
				else{
					f.write(","+(ind)+","+neuron.getAPCount(i)+","+peakLatency+"\n");
				}
			}
                	f.write("\n");
		}
		f.close();
        }

	public void setNewFiles(File[] sf) throws IOException{
		this.files = sf;
		neurons.clear();
		for (int i=0; i<files.length;i++){
                        infile = new Scanner(new BufferedReader(new FileReader(files[i])));
                        neurons.add(new psthClass(basicColumnTitles,advColumnTitles,psthlen,width,totTime,maxAP,minSwep,infile));
                }
		numNeurons = neurons.size();
		setParams();
	}
	public void setNewFiles(File[]sf, String[][][] allTitles) throws IOException{
		this.files = sf;
		neurons.clear();
		for (int i=0; i<files.length;i++){
                        infile = new Scanner(new BufferedReader(new FileReader(files[i])));
                        neurons.add(new psthClass(allTitles[i][0],allTitles[i][1],psthlen,width,totTime,maxAP,minSwep,infile));
                }
                numNeurons = neurons.size();
                setParams();
	}

	public void addNewFiles(File[] newFiles) throws IOException, Exception{
		String errorFiles = "";
		Throwable cause = new Throwable();
		for (int i=0; i<newFiles.length; i++){
			try{
				infile = new Scanner(new BufferedReader(new FileReader(newFiles[i])));
                        	neurons.add(new psthClass(basicColumnTitles,advColumnTitles,psthlen,width,totTime,maxAP,minSwep,infile));
			}
			catch (IOException e){
				System.out.println("Error: "+newFiles[i].getName()+" could not be opened. Check file format.");
				//Error error = new Error("Error: '"+newFiles[i].getName()+"' could not be opened. Check file format.",e.getCause());
				errorFiles += "'"+newFiles[i].getName()+"' ";
				cause = e.getCause();
				//throw error;
			}
			catch (Exception e){
				System.out.println("Error: "+newFiles[i].getName()+" could not be opened. Check file format.");
				Error error = new Error("Error: '"+newFiles[i].getName()+"' could not be opened. Check file format.",e.getCause());
				errorFiles += "'"+newFiles[i].getName()+"' ";
				cause = e.getCause();
				//throw error;
				//throw e;
			}
		}
		if (!errorFiles.equals("")){
			Error error = new Error("File(s) "+errorFiles+"could not be opened. Check file format.",cause);
			throw error;
		}
		numNeurons = neurons.size();
		setParams();
		addFiles(newFiles);
	}
	public void addFiles(File[] newFiles){
    		ArrayList<File> allFiles =new ArrayList<File>(Arrays.asList(files));
    		for (int i=0;i<newFiles.length;i++){
      			allFiles.add(newFiles[i]);
    		}
    		files = allFiles.toArray(new File[allFiles.size()]);
   		allFiles.clear();
  	}

	public void removeFile(int i){
		System.out.println("1723: files.length="+files.length+"\n");
		ArrayList<File> allFiles = new ArrayList<File>(Arrays.asList(files));
		allFiles.remove(i);
		files = allFiles.toArray(new File[allFiles.size()]);
		System.out.println("1726: new files.length="+files.length+"\n");		

		neurons.remove(i);
		numNeurons = neurons.size();
		System.out.println("1731: numNeurons="+numNeurons+"\n");
		modifyParams(i);
	}

	//SPONTANEOUS OPTIONS:
	public void setAddedSpont(double r, String type){
		this.addedspont = r;
		for (int i=0; i<numNeurons; i++){
			neuron = neurons.get(i);
			neuron.adjustSpontaneousRate(spont, type);
			neuron.calculateFSL(spont,neuron.numSweps());
		}
	}
	public double getAddedSpont(){
		return addedspont;
	}
	public void setSpont(int i, double r){
		neuron = neurons.get(i);
		neuron.setSpont(r);
		neuron.calculateFSL(r, neuron.numSweps());
	}
	public void setAllSpont(double r){
		for (int i=0; i<numNeurons; i++){
                        neuron = neurons.get(i);
                        neuron.setSpont(r);
			neuron.calculateFSL(r,neuron.numSweps());
                }
	}
	public double getSpont(int i){
		return neurons.get(i).getSpont();
	}

	//PARAMETER VIEWING OPTIONS:
	public int getFreqIndex(int k, int i){
		return neurons.get(k).getFreqIndex(i);
	}
	public double getFreqValue(int k, int i){
		return neurons.get(k).getFreqValue(i);
	}
	public int getIntenValue(int k, int i){
		return neurons.get(k).getIntenValue(i);
	}
	public int getIpsiIntenValue(int k, int i){
                return neurons.get(k).getIpsiIntenValue(i);
        }
	public int getILDValue(int k, int i){
                return neurons.get(k).getILDValue(i);
        }
		

	//Format PSTH:
	public int getPSTHWid(){
		return width;
	}
	public int getCustomPSTHy(int i){
		return neurons.get(i).getCustomPSTHy();
	}
	public void setCustomPSTHy(int i, int size){
		neurons.get(i).setCustomPSTHy(size);
	}
	public void setCustomPSTHyAll(int size){
		for (int i=0; i<numNeurons; i++){
			neurons.get(i).setCustomPSTHy(size);
		}
	}
	public void setIsCustomPSTHy(int i,boolean a){
		neurons.get(i).setIsCustomPSTHy(a);
	}
	public void setIsCustomPSTHyAll(boolean a){
		for (int i=0; i<numNeurons; i++){
			neurons.get(i).setIsCustomPSTHy(a);
		}					
	}
	public boolean getIsCustomPSTHy(int i){
		return neurons.get(i).isCustomPSTHy();
	}		
	
	//Format DR:
	public int getDotSize(int i){
		return neurons.get(i).getDotSize();
	}
	public void setDotSize(int i, int size){
		neurons.get(i).setDotSize(size);
	}
	public void setDotSizeAll(int size){
		for (int i=0; i< numNeurons; i++){
			neurons.get(i).setDotSize(size);
		}
	}

	//File Properties:
	public String getColumnTitle(int i, String type){
		if (type.equals("basic")){
			if (i<basicColumnTitles.length){
				return basicColumnTitles[i];
			}
		}
		else if (type.equals("adv")){
			if (i<advColumnTitles.length){
				return this.advColumnTitles[i];
			}
		}
		return this.basicColumnTitles[i];
	}
	public void setColumnTitle(int i, String name, String type){
		if (type.equals("basic")){
			if (i<basicColumnTitles.length){
				this.basicColumnTitles[i] = name;
			}
		}
		else if (type.equals("adv")){
			if (i<advColumnTitles.length){
                                this.advColumnTitles[i] = name;
                        }
		}
		else{
			this.basicColumnTitles[i] = name;
		}
	}

	public boolean isClosedField(int i){
		return neurons.get(i).isClosedField();
	}

	public int getNumNeurons(){
		return numNeurons;
	}
	public int getFreqSelected(int l, int i){
		return neurons.get(l).getFreqSelected(i);
	}
	public int getIntenSelected(int l, int i){
                return neurons.get(l).getIntenSelected(i);
        }
	public int getIpsiIntenSelected(int l, int i){
                return neurons.get(l).getIpsiIntenSelected(i);
        }
	public int getILDSelected(int l, int i){
                return neurons.get(l).getILDSelected(i);
        }					
	public void setFreqSelected(int l, int i, int j){
                neurons.get(l).setFreqSelected(i,j);
        }
        public void setIntenSelected(int l, int i, int j){
                neurons.get(l).setIntenSelected(i,j);
        }
	public void setIpsiIntenSelected(int l, int i, int j){
                neurons.get(l).setIpsiIntenSelected(i,j);
        }
	public void setILDSelected(int l, int i, int j){
		neurons.get(l).setILDSelected(i,j);
	}
	public boolean getFSLSelected(int l){
		return neurons.get(l).getFSLSelected();
	}
	public void setFSLSelected(int l, boolean a){
		neurons.get(l).setFSLSelected(a);
	}
	public boolean getPeakSelected(int l){
                return neurons.get(l).getPeakSelected();
        }
        public void setPeakSelected(int l, boolean a){
                neurons.get(l).setPeakSelected(a);
        }
	
	public int getAPCount(int l, int i){
		return neurons.get(l).getAPCount(i);
	}
	

        public void setLen(int len){
		numNeurons = neurons.size();
		for (int i=0;i<numNeurons;i++){
			neuron = neurons.get(i);
			neuron.setlLineX((int)(neuron.getlLineX()*(((double)(len)/(double)psthlen))));
                        neuron.setrLineX((int)(neuron.getrLineX()*(((double)(len)/(double)psthlen))));
                        neuron.setDRlLineX((int)(neuron.getDRlLineX()*(((double)(len)/(double)psthlen))));
                        neuron.setDRrLineX((int)(neuron.getDRrLineX()*((((double)len)/(double)psthlen))));
		}
		this.psthlen = len;
                psthlen2 = psthlen;
                psthlen3 = psthlen;
		//update();
	}
	public void setWid(int wid){
		this.psthwid = wid;
		psthwid2 = psthwid;
		psthwid3 = psthwid;
		//update();
	}
	public void update(){
		xLeft = xLeft0;
                yLeft = yLeft0;

                xLeft2=xLeft;//+psthlen+20;
                yLeft2=yLeft;//maxwid-10;

                lLineX = 0;
                rLineX = psthlen;
                DRlLineX = 0;//+psthlen+20;
                DRrLineX = psthlen;
		
		for (int i=0;i<numNeurons;i++){
                        neuron = neurons.get(i);
                        neuron.setlLineX(0);
                        neuron.setrLineX(psthlen);
                        neuron.setDRlLineX(0);
                        neuron.setDRrLineX(psthlen);
                }
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

  public static double maxDoubleList(ArrayList<Double> list){
    double max=list.get(0);
    for (int i=1; i<list.size(); i++){
      if (list.get(i)>max){
        max = list.get(i);
      }
    }
    return max;
  }

  public static int maxListPos(ArrayList<Integer> list){
    int max=list.get(0);
    int pos=0;
    for (int i=1; i<list.size(); i++){
      if (list.get(i)>max){
        max = list.get(i);
 pos = i;
      }
    }
    return pos;
  }

  public static int maxDoubleListPos(ArrayList<Double> list){
    double max=list.get(0);
    int pos=0;
    for (int i=1; i<list.size(); i++){
      if (list.get(i)>max){
        max = list.get(i);
        pos = i;
      }
    }
    return pos;
  }

  public static int finalListPos(ArrayList<Double> list){
    int pos = 0;
    for (int i=0;i<list.size();i++){
      if (list.get(i)!=0.0){
 pos = i;
      }
    }
    return pos;
  }

  public static int finalIntListPos(ArrayList<Integer> list){
    int pos = 0;
    for (int i=0;i<list.size();i++){
      if (list.get(i)!=0){
 pos = i;
      }
    }
    return pos;
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

  public static int countList(ArrayList<Integer> list,int a, int b){
 int count=0;
 for (int i=a;i<=b;i++){
  count += list.get(i);
 }
 return count;
  }

  /*public static int countArray(double[][] list,int a, int b){
 int count=0;
 for (int  j=0;j<list.length;j++){
  for (int i=0;i<list[0].length;i++){
   if (
 */  

  public static float min(float a, float b){
    if (a<b){
      return a;
    }
    return b;
  }


}
