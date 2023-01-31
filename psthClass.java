import java.util.*; 
import java.awt.*;
import java.io.*;

public class psthClass{
	private Random generator;
	//private CubicSpline rateSpline;				
	private int TIME_POS, ODD_POS, FREQ_POS, INTEN_POS, SWEP_POS, ILVL_POS, CLVL_POS;
	private int numCount, ILD, width,widthr;
	private int index,indexr, count;
	private int dotSize,odd;//, N;
	private int lastIndex;
        private double spont, N,Time, fsl, lsl, thres, P, sponRate;
        private double time, frequency;
	private boolean isCustomPSTHy, closedField;
	private int customPSTHy,swep, sweps,inten, intensIndex, ilvl, clvl;
        private int totTime, numOverview;
        private int numIntens, numFreqs, num, numr, psthlen;
	private int numIpsiIntens, ipsiIntensIndex, maxNumIpsiIntens, maxIpsiIntens;
	private int numILD, ILDIndex, maxILD, maxNumILD;
       	private int minAP, maxNumIntens, maxNumFreq;
	private int minSwep, rTime, lTime;
	private int DRlLineX, DRrLineX,lLineX,rLineX,hLineY;
	private Scanner infile;					
	private boolean peakSelected, fslSelected, psthPassed, ratePassed;
	private int[] peakLatencyFreq, peakLatencyInten, peakLatencyIpsiInten, peakLatencyILD;
	private double[] lslFreq, lslInten, lslIpsiInten, lslILD, fslFreq, fslInten, fslIpsiInten, fslILD;
	private boolean DRdraggingRight,DRdraggingLeft,draggingRight,draggingLeft;
	private int[] freqSelected, intenSelected, ipsiIntenSelected, ILDSelected;
	private ArrayList <Integer> missingSweps,newPSTH, newIntensPSTH,emptyPSTH, newIpsiIntensPSTH, newILDPSTH;
	private ArrayList <Double> tFreqValues,freqValues, intensities, newRate, emptyRate;
	private ArrayList <Double> ST, DS;
	private double[][] DR,newDR, emptyDR;
	private ArrayList <Integer> psth, freqIndex, tFreq, freqs, intens, intensCount;
	private ArrayList <Integer> ipsiIntens, ipsiIntensCount, contIntens, contIntensCount, ILDs, ILDCount;
        private ArrayList <Integer> newDataNum, emptyDataNum;
	private ArrayList <ArrayList<Integer>> tAllPSTH, allPSTH, allDataNum;
	private ArrayList <ArrayList<Double>> tAllRate, allRate, tSpikeTrains, spikeTrains, intenspikeTrains;
	private ArrayList <ArrayList<Integer>> allIntensPSTH, allIntensDataNum;
	private ArrayList <ArrayList<Integer>> allILDPSTH, allILDDataNum, allIpsiIntensPSTH, allIpsiIntensDataNum;
        private ArrayList <ArrayList<Double>> allIntensRate, allILDRate, allIpsiIntensRate;
	private ArrayList <double[][]> allDR, tAllDR;
	private ArrayList <double[][]> allIntensDR, allIpsiIntensDR, allILDDR;
	private ArrayList <double[]> allOverview;
	private ArrayList <Integer> tSwepFreq,swepFreq;
	private int nLBH, nHBL;
        private int[] numLBH, numHBL, numLBL, numHBH;
        private double[] fslLBH, fslHBL, fslLBL, fslHBH;
        private double[] countLBH, countHBL, countLBL, countHBH;
	private int[] IntensAPCount, APCount, ipsiIntensAPCount, ILDAPCount;
	private int[] totIntensAPCount, totAPCount, totIpsiIntensAPCount, totILDAPCount;
	private String freqTitle, oddbTitle, levlTitle, timeTitle, swepTitle, ilvlTitle, clvlTitle;
	private String[] basicColumn, advColumn;
	private ArrayList<ArrayList<Double>> countLBLList, countHBHList, countHBLList, countLBHList;
	private Color[] countDotColorList;
	private int dotSelected;

	private double[] overFreqRange, overIntenRange, trial;

	public psthClass(String[] basColumns, String[] advColumns,int psthlen, int width, int totTime, int minAP, int minSwep, Scanner infile){
        	Scanner kb = new Scanner(System.in);			
		generator = new Random(System.currentTimeMillis());
		this.closedField = false;
		this.sweps = 0;
		this.psthlen = psthlen;
		this.width=width;
		this.widthr = 1;
		this.index=0;
		this.indexr=0;
		this.lastIndex=0;
        	this.time=0.0;
		this.rTime = totTime;
		this.lTime = 0;
        	this.swep=0;
        	this.totTime = totTime;
        	this.num=0;
		this.numr=0;
        	this.minAP = minAP;
		this.minSwep = minSwep;
		this.dotSize = 2;
		this.infile = infile;
		this.lLineX = 0;
		this.rLineX = psthlen;
		this.DRlLineX = 0;
		this.DRrLineX = psthlen;
		this.hLineY = 10;
		this.ratePassed = false;
		this.psthPassed = false;
		this.fslSelected = true;
		this.peakSelected = true;
  		this.spont = 0.0d;
		this.isCustomPSTHy = false;
		this.countDotColorList = new Color[100];		
		this.dotSelected = -1;

		this.basicColumn = basColumns;
		this.advColumn = advColumns;
		//BASIC TITLES:
		if (basicColumn.length >= 4){
			this.timeTitle = basicColumn[0].toUpperCase();
			this.swepTitle = basicColumn[1].toUpperCase();
			this.freqTitle = basicColumn[2].toUpperCase();
			this.levlTitle = basicColumn[3].toUpperCase();
		}
		else{
			this.timeTitle = "TIME";
                        this.swepTitle = "SWEP";
                        this.freqTitle = "FREQ";
                        this.levlTitle = "LEVL";
		}
		//ADV TITLES:
		if (advColumn.length >= 3){
			this.oddbTitle = advColumn[0].toUpperCase();
			this.ilvlTitle = advColumn[1].toUpperCase();
			this.clvlTitle = advColumn[2].toUpperCase();
		}
		else{
			this.oddbTitle = "ODDB";
                        this.ilvlTitle = "ILVL";
                        this.clvlTitle = "CLVL";
		}			
		
		this.TIME_POS = -1;
		this.INTEN_POS = -1;
		this.SWEP_POS = -1;
		this.FREQ_POS = -1;
		this.ODD_POS = -1;
		this.ILVL_POS = -1;
		this.CLVL_POS = -1;

		String[] line = infile.nextLine().split(",");
		for (int i=0; i< line.length; i++){
			if ( line[i].toUpperCase().equals(timeTitle)){
				TIME_POS = i;			
			}
			else if (line[i].toUpperCase().equals(oddbTitle)){
				ODD_POS = i;
			}
			else if (line[i].toUpperCase().equals(levlTitle)){
				INTEN_POS = i;
			}
			else if(line[i].toUpperCase().equals(freqTitle)){
				FREQ_POS = i;
			}
			else if(line[i].toUpperCase().equals(swepTitle)){
				SWEP_POS = i;
			}
			else if(line[i].toUpperCase().equals(ilvlTitle)){
                                ILVL_POS = i;
                        }
			else if(line[i].toUpperCase().equals(clvlTitle)){
                                CLVL_POS = i;
                        }
		}
		
		/*
		if (TIME_POS == -1){
			TIME_POS= 3;
		}
		if (SWEP_POS == -1){
			SWEP_POS = 7;
		}
		if (ODD_POS == -1){
			ODD_POS = 10;
		}
		if (FREQ_POS == -1){
			FREQ_POS = 8;
		}
		if (INTEN_POS == -1){
			INTEN_POS = 9;
		}*/
		
		//line = infile.nextLine().split(",");
		
		num = totTime/width;
		numr = totTime/widthr;
		numFreqs = 100;
		this.draggingRight=false;
		this.draggingLeft=false;
		this.DRdraggingRight=false;
                this.DRdraggingLeft=false;
		this.odd = 0;
		
		this.spikeTrains = new ArrayList<ArrayList<Double>>();
		
		this.swepFreq = new ArrayList<Integer>();
		this.freqIndex = new ArrayList<Integer>();
		this.freqs = new ArrayList<Integer>();
		this.intens = new ArrayList<Integer>();
		this.freqValues = new ArrayList<Double>();
		this.intensCount = new ArrayList<Integer>();
		
		this.allOverview = new ArrayList<double[]>();
		
		this.allPSTH = new ArrayList<ArrayList<Integer>>();
		this.allRate = new ArrayList<ArrayList<Double>>();
		this.allDR = new ArrayList<double[][]>();
		this.allDataNum = new ArrayList<ArrayList<Integer>>();

		this.allIntensPSTH = new ArrayList<ArrayList<Integer>>();
                this.allIntensRate = new ArrayList<ArrayList<Double>>();
                this.allIntensDR = new ArrayList<double[][]>();
                this.allIntensDataNum = new ArrayList<ArrayList<Integer>>();
		
		if (ILVL_POS != -1){
			this.closedField = true;
			this.ipsiIntens = new ArrayList<Integer>();
                	this.contIntens = new ArrayList<Integer>();
                	this.ILDs = new ArrayList<Integer>();
			this.ipsiIntensCount = new ArrayList<Integer>();
                	this.contIntensCount = new ArrayList<Integer>();
			this.ILDCount = new ArrayList<Integer>();
			
			this.allILDPSTH = new ArrayList<ArrayList<Integer>>();
                	this.allILDRate = new ArrayList<ArrayList<Double>>();
                	this.allILDDR = new ArrayList<double[][]>();
                	this.allILDDataNum = new ArrayList<ArrayList<Integer>>();
			
			this.allIpsiIntensPSTH = new ArrayList<ArrayList<Integer>>();
                	this.allIpsiIntensRate = new ArrayList<ArrayList<Double>>();
                	this.allIpsiIntensDR = new ArrayList<double[][]>();
                	this.allIpsiIntensDataNum = new ArrayList<ArrayList<Integer>>();
		}
		
		
		for (int i=0; i<minSwep; i++){
			swepFreq.add(0);
		}

		for (int k=0;k<numFreqs;k++){
			freqs.add(0);
			freqValues.add(0.0d);
			spikeTrains.add(null);
			allPSTH.add(null);
			allRate.add(null);
			allDR.add(null);
			allDataNum.add(null);
			
			allIntensPSTH.add(null);
                        allIntensRate.add(null);
                        allIntensDR.add(null);
                        allIntensDataNum.add(null);
		
			if (closedField){	
				allILDPSTH.add(null);
                        	allILDRate.add(null);
                        	allILDDR.add(null);
                       		allILDDataNum.add(null);
				
				allIpsiIntensPSTH.add(null);
                        	allIpsiIntensRate.add(null);
                        	allIpsiIntensDR.add(null);
                        	allIpsiIntensDataNum.add(null);
			}
		}

		while (infile.hasNextLine()){
			line = infile.nextLine().split(",");
			time = Double.parseDouble(line[TIME_POS]);//3
			swep = Integer.parseInt(line[SWEP_POS]);//7
			odd = Integer.parseInt(line[ODD_POS]);//10
			frequency = Double.parseDouble(line[FREQ_POS]);//8
        		inten = Integer.parseInt(line[INTEN_POS]);//9
			if (closedField){
				//clvl = Integer.parseInt(line[CLVL_POS]);
				ilvl = Integer.parseInt(line[ILVL_POS]);
				ILD = ilvl - inten;
			}
			index = (int)(Math.floor((double)time/(width/1000.0)));
        		indexr = (int)(Math.floor((double)time/(widthr/1000.0)));
			if (index<num && time>0.0 && time<(double)(totTime+0.0) && swep<=minSwep && swep>0 && odd>=0 && odd<numFreqs){	
				//if (!allOverview.contains(new double[]{odd,inten})){
                                if (!containsOverview(allOverview,frequency,inten,(double)(odd))){
				        trial = new double[]{frequency,inten,(double)(odd)};
					//System.out.println("LINE 220: odd="+trial[0]+", inten="+trial[1]);
					allOverview.add(trial);
				}
				
				if (freqs.get(odd)<=0){
					allPSTH.set(odd,newPSTHArrayList());
					allPSTH.get(odd).set(index,allPSTH.get(odd).get(index)+1);
				}
				else if (freqs.get(odd)>0 && allPSTH.get(odd)!=null){
					newPSTH = allPSTH.get(odd);
					newPSTH.set(index,newPSTH.get(index)+1);
					allPSTH.set(odd,newPSTH);
				}
				if (intens.contains(inten)){
					intensIndex = intens.indexOf(inten);
					newIntensPSTH = allIntensPSTH.get(intensIndex);
					newIntensPSTH.set(index,newIntensPSTH.get(index)+1);
					allIntensPSTH.set(intensIndex,newIntensPSTH);
				}
				else{
					intensIndex = intensCount.size();
					allIntensPSTH.set(intensIndex,newPSTHArrayList());
					allIntensPSTH.get(intensIndex).set(index,allIntensPSTH.get(intensIndex).get(index)+1);
				}
				
				if (closedField){
				//IPSI INTENSITY
				if (ipsiIntens.contains(ilvl)){
                                        ipsiIntensIndex = ipsiIntens.indexOf(ilvl);
                                        newIpsiIntensPSTH = allIpsiIntensPSTH.get(ipsiIntensIndex);
                                        newIpsiIntensPSTH.set(index,newIpsiIntensPSTH.get(index)+1);
                                        allIpsiIntensPSTH.set(ipsiIntensIndex,newIpsiIntensPSTH);
                                }
                                else{
                                        ipsiIntensIndex = ipsiIntensCount.size();
                                        allIpsiIntensPSTH.set(ipsiIntensIndex,newPSTHArrayList());
                                        allIpsiIntensPSTH.get(ipsiIntensIndex).set(index,allIpsiIntensPSTH.get(ipsiIntensIndex).get(index)+1);
                                }

				//ILD PSTH
				if (ILDs.contains(ILD)){
					ILDIndex = ILDs.indexOf(ILD);
					newILDPSTH = allILDPSTH.get(ILDIndex);
					newILDPSTH.set(index,newILDPSTH.get(index)+1);
					allILDPSTH.set(ILDIndex,newILDPSTH);
				}
				else{
					ILDIndex = ILDCount.size();
					allILDPSTH.set(ILDIndex,newPSTHArrayList());
					allILDPSTH.get(ILDIndex).set(index,allILDPSTH.get(ILDIndex).get(index)+1);
				}
				}
                		
				//DOT-RASTERS:
				//if (time!=0.0){
					if (freqs.get(odd)<=0){
						allDR.set(odd,newDRArray());
						allDataNum.set(odd,newDataNumArrayList());
						lastIndex = allDataNum.get(odd).get(swep-1);
						(allDR.get(odd))[swep-1][lastIndex]=time;
                                                allDataNum.get(odd).set(swep-1,allDataNum.get(odd).get(swep-1)+1);
						spikeTrains.set(odd,new ArrayList<Double>());
						spikeTrains.get(odd).add(time);
					}
					else if (freqs.get(odd)>0 && allDataNum.get(odd)!=null){//else{
						lastIndex = allDataNum.get(odd).get(swep-1);
						(allDR.get(odd))[swep-1][lastIndex]=time;
						allDataNum.get(odd).set(swep-1,allDataNum.get(odd).get(swep-1)+1);
						spikeTrains.get(odd).add(time);
					}

					if (intens.contains(inten)){
                                        	intensIndex = intens.indexOf(inten);
                                        	lastIndex = allIntensDataNum.get(intensIndex).get(swep-1);
                                		(allIntensDR.get(intensIndex))[swep-1][lastIndex]=time;
                                                allIntensDataNum.get(intensIndex).set(swep-1,allIntensDataNum.get(intensIndex).get(swep-1)+1);
					}
                                	else{
                                		intensIndex = intens.size();
						allIntensDR.set(intensIndex,newDRArray());
                                                allIntensDataNum.set(intensIndex,newDataNumArrayList());
                                                lastIndex = allIntensDataNum.get(intensIndex).get(swep-1);
                                                (allIntensDR.get(intensIndex))[swep-1][lastIndex]=time;
                                                allIntensDataNum.get(intensIndex).set(swep-1,allIntensDataNum.get(intensIndex).get(swep-1)+1);
					}
			
					if (closedField){
					//IPSI-INTENS DR:
					if (ipsiIntens.contains(ilvl)){
                                                ipsiIntensIndex = ipsiIntens.indexOf(ilvl);
                                                lastIndex = allIpsiIntensDataNum.get(ipsiIntensIndex).get(swep-1);
                                                (allIpsiIntensDR.get(ipsiIntensIndex))[swep-1][lastIndex]=time;
                                                allIpsiIntensDataNum.get(ipsiIntensIndex).set(swep-1,allIpsiIntensDataNum.get(ipsiIntensIndex).get(swep-1)+1);
                                        }
                                        else{
                                                ipsiIntensIndex = ipsiIntens.size();
                                                allIpsiIntensDR.set(ipsiIntensIndex,newDRArray());
                                                allIpsiIntensDataNum.set(ipsiIntensIndex,newDataNumArrayList());
                                                lastIndex = allIpsiIntensDataNum.get(ipsiIntensIndex).get(swep-1);
                                                (allIpsiIntensDR.get(ipsiIntensIndex))[swep-1][lastIndex]=time;
                                                allIpsiIntensDataNum.get(ipsiIntensIndex).set(swep-1,allIpsiIntensDataNum.get(ipsiIntensIndex).get(swep-1)+1);
                                        }
					//ILD DR:
					if (ILDs.contains(ILD)){
                                                ILDIndex = ILDs.indexOf(ILD);
                                                lastIndex = allILDDataNum.get(ILDIndex).get(swep-1);
                                                (allILDDR.get(ILDIndex))[swep-1][lastIndex]=time;
                                                allILDDataNum.get(ILDIndex).set(swep-1,allILDDataNum.get(ILDIndex).get(swep-1)+1);
                                        }
                                        else{
                                                ILDIndex = ILDs.size();
                                                allILDDR.set(ILDIndex,newDRArray());
                                                allILDDataNum.set(ILDIndex,newDataNumArrayList());
                                                lastIndex = allILDDataNum.get(ILDIndex).get(swep-1);
                                                (allILDDR.get(ILDIndex))[swep-1][lastIndex]=time;
                                                allILDDataNum.get(ILDIndex).set(swep-1,allILDDataNum.get(ILDIndex).get(swep-1)+1);
                                        }
					}
				//}
				psthPassed = true;
        		}
			if (indexr<numr && time<(double)(totTime+0.0) && swep<=minSwep && swep>0 && odd>=0 && odd<numFreqs){
				if (freqs.get(odd)<=0){
					allRate.set(odd,newRateArrayList());
                                        allRate.get(odd).set(indexr,allRate.get(odd).get(indexr)+1000.0/minSwep);	
				}
				else if (freqs.get(odd)>0 && allRate.get(odd)!=null){//else{
					allRate.get(odd).set(indexr,allRate.get(odd).get(indexr)+1000.0/minSwep);
				}
				if (intens.contains(inten)){
                                        intensIndex = intens.indexOf(inten);
                                	allIntensRate.get(intensIndex).set(indexr,allIntensRate.get(intensIndex).get(indexr)+1000.0/minSwep);
				}
                                else{
                                        intensIndex = intensCount.size();
					allIntensRate.set(intensIndex,newRateArrayList());
                                        allIntensRate.get(intensIndex).set(indexr,allIntensRate.get(intensIndex).get(indexr)+1000.0/minSwep);
                                }
				
				if (closedField){
				//IPSI-INTENSITY RATE:
				if (ipsiIntens.contains(ilvl)){
                                        ipsiIntensIndex = ipsiIntens.indexOf(ilvl);
                                        allIpsiIntensRate.get(ipsiIntensIndex).set(indexr,allIpsiIntensRate.get(ipsiIntensIndex).get(indexr)+1000.0/minSwep);
                                }
                                else{
                                        ipsiIntensIndex = ipsiIntensCount.size();
                                        allIpsiIntensRate.set(ipsiIntensIndex,newRateArrayList());
                                        allIpsiIntensRate.get(ipsiIntensIndex).set(indexr,allIpsiIntensRate.get(ipsiIntensIndex).get(indexr)+1000.0/minSwep);
                                }
				//ILD RATE:
				if (ILDs.contains(ILD)){
                                        ILDIndex = ILDs.indexOf(ILD);
                                        allILDRate.get(ILDIndex).set(indexr,allILDRate.get(ILDIndex).get(indexr)+1000.0/minSwep);
                                }
                                else{
                                        ILDIndex = intensCount.size();
                                        allILDRate.set(ILDIndex,newRateArrayList());
                                        allILDRate.get(ILDIndex).set(indexr,allILDRate.get(ILDIndex).get(indexr)+1000.0/minSwep);
                                }
				}
				
				ratePassed = true;
			}
		
			if (psthPassed || ratePassed){
				swepFreq.set((swep-1),odd);
				freqs.set(odd,freqs.get(odd)+1);
				if (frequency > 0.0d){
					freqValues.set(odd,frequency);
				}
				//INTENS
				if (intens.contains(inten)){
					intensIndex = intens.indexOf(inten);
					intensCount.set(intensIndex,intensCount.get(intensIndex)+1);
				}
				else{
					intensCount.add(1);
                                	intens.add(inten);
				}

				if (closedField){
					//IPSI-INTENS
					if (ipsiIntens.contains(ilvl)){
                                	        ipsiIntensIndex = ipsiIntens.indexOf(ilvl);
                                	        ipsiIntensCount.set(ipsiIntensIndex,ipsiIntensCount.get(ipsiIntensIndex)+1);
                                	}
                                	else{
                                	        ipsiIntensCount.add(1);
                                	        ipsiIntens.add(ilvl);
                                	}
					//ILD:
					if (ILDs.contains(ILD)){
                                        	ILDIndex = ILDs.indexOf(ILD);
                                        	ILDCount.set(ILDIndex,ILDCount.get(ILDIndex)+1);
                                	}
                                	else{
                                        	ILDCount.add(1);
                                        	ILDs.add(ILD);
                                	}
				}

				sweps = Math.max(sweps,swep);
				psthPassed = false;
				ratePassed = false;
			}
		}
		//minSwep = swep;
		
		tFreqValues = new ArrayList<Double>();
		tFreq = new ArrayList<Integer>();
		tAllPSTH = new ArrayList<ArrayList<Integer>>();
		tAllRate = new ArrayList<ArrayList<Double>>();
		tSpikeTrains = new ArrayList<ArrayList<Double>>();
		tAllDR = new ArrayList<double[][]>();
		for (int i=0; i<freqs.size();i++){
			if (freqs.get(i)>0){
				tFreqValues.add(freqValues.get(i));
				tFreq.add(freqs.get(i));
				tAllPSTH.add(allPSTH.get(i));
				tAllRate.add(allRate.get(i));
				tAllDR.add(allDR.get(i));
				tSpikeTrains.add(spikeTrains.get(i));
				freqIndex.add(i);
			}
		}
		allPSTH = new ArrayList<ArrayList<Integer>>(tAllPSTH);
		allRate = new ArrayList<ArrayList<Double>>(tAllRate);
		allDR = new ArrayList<double[][]>(tAllDR);
		spikeTrains = new ArrayList<ArrayList<Double>>(tSpikeTrains);
		freqs = new ArrayList<Integer>(tFreq);
		freqValues = new ArrayList<Double>(tFreqValues);
		maxNumFreq = Collections.max(freqs);
		numFreqs = freqs.size();
		numIntens = intensCount.size();
		maxNumIntens = Collections.max(intensCount);
		APCount = new int[numFreqs];
		IntensAPCount = new int[numIntens];
		totAPCount = new int[numFreqs];
		totIntensAPCount = new int[numIntens];		

		if (closedField){
			numIpsiIntens = ipsiIntensCount.size();
			numILD = ILDCount.size();
			//maxIpsiIntens = ipsiIntensCount.size();
			maxNumIpsiIntens = Collections.max(ipsiIntensCount);
			ipsiIntensAPCount = new int[numIpsiIntens];
			//maxILD = ILDCount.size();
			maxNumILD = Collections.max(ILDCount);
			ILDAPCount = new int[numILD];
			
			totIpsiIntensAPCount = new int[numIpsiIntens];
			totILDAPCount = new int[numILD];
		}

		//Calc Total Counts:
		//FREQ COUNT:
                numCount = numFreqs;
                for (int l=0; l<numCount;l++){
                       	DR = allDR.get(l);
                       		count = 0;
                                for (int i=0;i<minSwep;i++){
                                        for (int j=0;j<DR[i].length;j++){
                                                if (DR[i][j]!=0){
                                                        count ++;
                                                }
                                        }
                                }
                       totAPCount[l]=count;
                }
                //INTENSITY COUNT:
		numCount = numIntens;
                for (int l=0; l<numCount;l++){
                        DR = allIntensDR.get(l);
                                count = 0;
                                for (int i=0;i<minSwep;i++){
                                        for (int j=0;j<DR[i].length;j++){
                                                if (DR[i][j]!=0){
                                                        count ++;
                                                }
                                        }
                                }
                       totIntensAPCount[l] = count;
                }			
		//IPSI-INTENS COUNT:
		numCount = numIpsiIntens;
                for (int l=0; l<numCount;l++){
                        DR = allIpsiIntensDR.get(l);
                                count = 0;
                                for (int i=0;i<minSwep;i++){
                                        for (int j=0;j<DR[i].length;j++){
                                                if (DR[i][j]!=0){
                                                        count ++;
                                                }
                                        }
                                }
                       totIpsiIntensAPCount[l] = count;
                }				
		//ILD COUNT:
		numCount = numILD;
                for (int l=0; l<numCount;l++){
                        DR = allILDDR.get(l);
                                count = 0;
                                for (int i=0;i<minSwep;i++){
                                        for (int j=0;j<DR[i].length;j++){
                                                if (DR[i][j]!=0){
                                                        count ++;
                                                }
                                        }
                                }
                       totILDAPCount[l] = count;
                }	
		

		//Overview Panel
		numOverview = allOverview.size();
		overFreqRange = calcRange(allOverview,0);
		overIntenRange = calcRange(allOverview,1);
		
		lslFreq = new double[numFreqs];
                lslInten = new double[numIntens];
                fslFreq = new double[numFreqs];
                fslInten = new double[numIntens];
                peakLatencyFreq = new int[numFreqs];
                peakLatencyInten = new int[numIntens];
		
		if (closedField){
                        lslIpsiInten = new double[numIpsiIntens];
                        lslILD = new double[numILD];	
			fslIpsiInten = new double[numIpsiIntens];
                	fslILD = new double[numILD];
			peakLatencyIpsiInten = new int[numIpsiIntens];
                	peakLatencyILD = new int[numILD];
		}
		
                freqSelected = new int[numFreqs];
		for (int i=0; i<numFreqs; i++){
			Collections.sort(spikeTrains.get(i));
			freqSelected[i] = 1;
			fslFreq[i] = 0.0;
			lslFreq[i] = 0.0;
		}
          	intenSelected = new int[numIntens];
                for (int i=0; i<numIntens;i++){
                        intenSelected[i] = 1;
			fslInten[i] = 0.0;
                        lslInten[i] = 0.0;
		}
		
		if (closedField){
			ipsiIntenSelected = new int[numIpsiIntens];
                	for (int i=0; i<numIpsiIntens;i++){
                	        ipsiIntenSelected[i] = 1;
                	        fslIpsiInten[i] = 0.0;
                	        lslIpsiInten[i] = 0.0;
                	}
			ILDSelected = new int[numILD];
                	for (int i=0; i<numILD;i++){
                        	ILDSelected[i] = 1;
                        	fslILD[i] = 0.0;
                        	lslILD[i] = 0.0;
                	}
		}
		
		if (sweps>=60 && sweps<70){
                        sweps = 60;
                        for (int i=0; i<numFreqs; i++){
                                ArrayList<Double> rate = allRate.get(i);
                                for (int j=0; j<rate.size(); j++){
                                        rate.set(j,rate.get(j)*((double)minSwep)/sweps);
                                }
                        }
                }
                else if (sweps>200){
                        sweps = 200;
                }

		tSwepFreq = new ArrayList<Integer>();
		for (int i=0; i<sweps; i++){
                        tSwepFreq.add(swepFreq.get(i));
		}
		swepFreq = new ArrayList<Integer>(tSwepFreq);

		for (int i=0; i<countDotColorList.length; i++){
			countDotColorList[i] = new Color(0,0,255);
		}						

		//CUBIC SPLINE
                /*float[] ex = new float[totTime+1];
                for (int i=0; i<(totTime+1); i++){
                        ex[i] = (float)(i+0.0f);
                }
                float[] rate = new float[totTime+1];
                rate[0] = 0;
                for (int i=0; i<totTime; i++){
                        rate[i+1] = (float)((allRate.get(0)).get(i) + 0.0f);
                }*/
                //rateSpline = new CubicSpline(ex, rate);

                //FIRST-SPIKE LATENCY:
                calculateFSL(0.0d,sweps);
                /*System.out.println("\nNew spont. rate: ");
                double newRate = kb.nextDouble();
                adjustSpontaneousRate(newRate);
                calculateFSL(newRate,sweps);
                System.out.println("----------------------------------\n");
                */
	}

	public void setCountDotSelected(int i, String typ){
		if (typ.equals("none") && getCountDotClicked()!= i){
                        countDotColorList[i] = new Color(0,0,255);
                	//setCountDotClicked(-1);
		}						
                else if (typ.equals("hover") && getCountDotClicked()!= i){
                        countDotColorList[i] = new Color(0,200,200);
                }
		else if (typ.equals("click")){
			countDotColorList[i] = new Color(255,0,0);
                	setCountDotClicked(i);
		}
		else{
			if (getCountDotClicked() == i){
				countDotColorList[i] = new Color(255,0,0);
			}					
			else{
				countDotColorList[i] = new Color(0,0,255);
			}
		}
	}						
	public Color getCountDotSelected(int i){
		return countDotColorList[i];
	}
	public void setCountDotClicked(int i){
		this.dotSelected = i;
		if (i>=0){
			setAllSingleSelected(i,1);
		}
		else{
			setAllSelected(1);
		}
	}
	public int getCountDotClicked(){
		return dotSelected;
	}
	public void resetAllCountDots(){
		for (int i=0; i<countDotColorList.length;i++){
			countDotColorList[i] = new Color(0,200,200);
		}
		dotSelected = -1;
		setAllSelected(1);
	}

	public void SSA_Analysis(){
		double count = 0.0d;
		nLBH = 0;
		nHBL = 0;
		numLBH = new int[sweps];
		numHBL = new int[sweps];
		numLBL = new int[sweps];
		numHBH = new int[sweps];
		fslLBH = new double[sweps];
		fslHBL = new double[sweps];
		fslLBL = new double[sweps];
		fslHBH = new double[sweps];
		countLBH = new double[sweps];
		countHBL = new double[sweps];
		countHBH = new double[sweps];
		countLBL = new double[sweps];
		countLBLList = new ArrayList<ArrayList<Double>>();
                countHBHList = new ArrayList<ArrayList<Double>>();
                countLBHList = new ArrayList<ArrayList<Double>>();
                countHBLList = new ArrayList<ArrayList<Double>>();
		
		for (int i=0; i< sweps; i++){
			countLBLList.add(null);
			countHBHList.add(null);
			countLBHList.add(null);
			countHBLList.add(null);
			numLBH[i] = 0;
			numHBL[i] = 0;
			numLBL[i] = 0;
			numHBH[i] = 0;
			fslLBH[i] = 0.0d;
			fslHBL[i] = 0.0d;
			fslLBL[i] = 0.0d;
			fslHBH[i] = 0.0d;
			countLBH[i] = 0.0d;
			countHBL[i] = 0.0d;
			countHBH[i] = 0.0d;
			countLBL[i] = 0.0d;
		}

		nHBL = 0;
		nLBH = 0;
		int lastIndex = -1;
		int lvl = 0;
		missingSweps = new ArrayList<Integer>();
		if (swepFreq.get(0)==0){
                 	if (count(allDR.get(0)[0])>0){
				nHBL = 1;
               			lastIndex = 0;
				
				numHBH[nHBL-1] = numHBH[nHBL-1] + 1;
                                countHBH[nHBL-1] = countHBH[nHBL-1] + count;
                                if (numHBH[nHBL-1] > 1){
                                        countHBHList.get(nHBL-1).add(count);
                                }
                                else{
                                        countHBHList.set(nHBL-1,new ArrayList<Double>());
                	                countHBHList.get(nHBL-1).add(count);
                                }
                                fslHBH[nHBL-1] = fslHBH[nHBL-1] + (calculateSweepFSL(allDR.get(0)[0],lvl));
			}
			else{
				missingSweps.add(0);
			}
		}
                else if (swepFreq.get(0)==1){
                	if (count(allDR.get(1)[0])>0){
				nLBH = 1;
                		lastIndex = 0;
				
				numLBL[nLBH-1] = numLBL[nLBH-1] + 1;
                                countLBL[nLBH-1] = countLBL[nLBH-1] + count;//(allDR.get(1)[i]);
                                //countLBLList.get(nLBH).add(count);
                                fslLBL[nLBH-1] = fslLBL[nLBH-1] + (calculateSweepFSL(allDR.get(1)[0],lvl));
                        
                                if (numLBL[nLBH-1] > 1){
                                        countLBLList.get(nLBH-1).add(count);
                                }
                                else{   
                                        countLBLList.set(nLBH-1,new ArrayList<Double>());
                                        countLBLList.get(nLBH-1).add(count);
                                }
                                System.out.println("swepFreq.get("+0+")="+swepFreq.get(0)+"\n");
			}
			else{
				missingSweps.add(0);
			}
		}

                for (int i=1; i<sweps; i++){
			//System.out.println("swepFreq.get("+i+")="+swepFreq.get(i)+"\n");
			if (swepFreq.get(i)==0){
				count = count(allDR.get(0)[i]);
				if (lastIndex!=-1){
					if (swepFreq.get(lastIndex)==1){
						if (count > 0){
							System.out.println("swepFreq.get("+i+")="+swepFreq.get(i)+"\n");
							numLBH[nLBH] = numLBH[nLBH] + 1;
							fslLBH[nLBH] = fslLBH[nLBH]+(calculateSweepFSL(allDR.get(0)[i],lvl));
                                        		countLBH[nLBH] = countLBH[nLBH]+count;
							if (numLBH[nLBH] > 1){
								countLBHList.get(nLBH).add(count);
                                        		}
							else{
								countLBHList.set(nLBH,new ArrayList<Double>());
								countLBHList.get(nLBH).add(count);
							}
							nLBH = 0;
						
							nHBL = 1;
							numHBH[nHBL-1] = numHBH[nHBL-1] + 1;
                                        		fslHBH[nHBL-1] = fslHBH[nHBL-1]+(calculateSweepFSL(allDR.get(0)[i],lvl));
                                       			countHBH[nHBL-1] = countHBH[nHBL-1]+count;
							if (numHBH[nHBL-1] > 1){
                                        		        countHBHList.get(nHBL-1).add(count);
                                        		}
                                        		else{
                                        			countHBHList.set(nHBL-1,new ArrayList<Double>());
                                                		countHBHList.get(nHBL-1).add(count);
                                        		}
							lastIndex = i;
						}
						else{
							missingSweps.add(i+1);
						}				
					}
					else if (swepFreq.get(lastIndex)==0){
					//count = count(allDR.get(0)[i]);
						if (count > 0){
							System.out.println("swepFreq.get("+i+")="+swepFreq.get(i)+"\n");
							//nHBL ++;
							numHBH[nHBL] = numHBH[nHBL] + 1;
							countHBH[nHBL] = countHBH[nHBL] + count;
                                  	     		if (numHBH[nHBL] > 1){
								countHBHList.get(nHBL).add(count);
							}
							else{
								countHBHList.set(nHBL,new ArrayList<Double>());
								countHBHList.get(nHBL).add(count);
							}
							fslHBH[nHBL] = fslHBH[nHBL] + (calculateSweepFSL(allDR.get(0)[i],lvl));
							nHBL++;
						}
						else{
							missingSweps.add(i+1);
						}			
					}	
				}
				else{
					if (count > 0){
						nHBL = 1;
						lastIndex = i;
						System.out.println("swepFreq.get("+i+")="+swepFreq.get(i)+"\n");
						
						numHBH[nHBL-1] = numHBH[nHBL-1] + 1;
                                                countHBH[nHBL-1] = countHBH[nHBL-1] + count;
                                                if (numHBH[nHBL-1] > 1){
                                                        countHBHList.get(nHBL-1).add(count);
                                                }
                                                else{
                                                        countHBHList.set(nHBL-1,new ArrayList<Double>());
                                                        countHBHList.get(nHBL-1).add(count);
                                                }
                                                fslHBH[nHBL-1] = fslHBH[nHBL-1] + (calculateSweepFSL(allDR.get(0)[i],lvl));
						
					}
					else{
						missingSweps.add(i+1);
					}
				}
			}
			
			else if (swepFreq.get(i)==1){
				count = count(allDR.get(1)[i]);
				if (lastIndex != -1){
					if (swepFreq.get(lastIndex)==0){
						if (count > 0){
							System.out.println("swepFreq.get("+i+")="+swepFreq.get(i)+"\n");
							numHBL[nHBL] = numHBL[nHBL] + 1;
                        	                	fslHBL[nHBL] = fslHBL[nHBL]+(calculateSweepFSL(allDR.get(1)[i],lvl));
                        	                	countHBL[nHBL] = countHBL[nHBL]+count;
                        		               	if (numHBL[nHBL] > 1){
								countHBLList.get(nHBL).add(count);
							}
							else{
								countHBLList.set(nHBL,new ArrayList<Double>());
								countHBLList.get(nHBL).add(count);
							}
							nHBL = 0;
						
							nLBH = 1;
							numLBL[nLBH-1] = numLBL[nLBH-1] + 1;
                                        		fslLBL[nLBH-1] = fslLBL[nLBH-1]+(calculateSweepFSL(allDR.get(1)[i],lvl));
							countLBL[nLBH-1] = countLBL[nLBH-1]+count;//(allDR.get(1)[i]);
							
							if (numLBL[nLBH-1] > 1){
								countLBLList.get(nLBH-1).add(count);
							}
							else{
								countLBLList.set(nLBH-1,new ArrayList<Double>());
								countLBLList.get(nLBH-1).add(count);
							}
							lastIndex = i;
						}
						else{
							missingSweps.add(i+1);
						}
					}
					else if (swepFreq.get(lastIndex)==1){
						if (count > 0){
							System.out.println("swepFreq.get("+i+")="+swepFreq.get(i)+"\n");
							//nLBH ++;
							//System.out.println("nLBH="+nLBH+"\n");
							numLBL[nLBH] = numLBL[nLBH] + 1;
							countLBL[nLBH] = countLBL[nLBH] + count;//(allDR.get(1)[i]);
							//countLBLList.get(nLBH).add(count);
							fslLBL[nLBH] = fslLBL[nLBH] + (calculateSweepFSL(allDR.get(1)[i],lvl));

							if (numLBL[nLBH] > 1){
                                                	        countLBLList.get(nLBH).add(count);
                                                	}
                                                	else{
                                                	        countLBLList.set(nLBH,new ArrayList<Double>());
                                                	        countLBLList.get(nLBH).add(count);
                                                	}
							nLBH++;
							lastIndex = i;
						}
						else{
							missingSweps.add(i+1);
						}
					}
				}
				else{
					if (count >0){
						nLBH = 1;
						lastIndex = i;
						System.out.println("swepFreq.get("+i+")="+swepFreq.get(i)+"\n");
						
						numHBH[nHBL-1] = numHBH[nHBL-1] + 1;
                                                countHBH[nHBL-1] = countHBH[nHBL-1] + count;
                                                if (numHBH[nHBL-1] > 1){
                                                        countHBHList.get(nHBL-1).add(count);
                                                }
                                                else{
                                                        countHBHList.set(nHBL-1,new ArrayList<Double>());
                                                        countHBHList.get(nHBL-1).add(count);
                                                }
                                                fslHBH[nHBL-1] = fslHBH[nHBL-1] + (calculateSweepFSL(allDR.get(0)[i],lvl));
					}
					else{
						missingSweps.add(i+1);
					}
				}
			}
		}
		for (int i=0; i<sweps; i++){
			//System.out.println("numHBL["+i+"]="+numHBL[i]+"\n");
			if (numHBL[i] > 0){
				countHBL[i] = countHBL[i]/((double)numHBL[i]);
				fslHBL[i] = fslHBL[i]/((double)numHBL[i]);
				//System.out.println("countHBL["+i+"]="+countHBL[i]+"\n");
			}
			if (numHBH[i]>0){
				countHBH[i] = countHBH[i]/((double)numHBH[i]);
				fslHBH[i] = fslHBH[i]/((double)numHBH[i]);
			}
			if (numLBL[i]>0){
				countLBL[i] = countLBL[i]/((double)numLBL[i]);
				fslLBL[i] = fslLBL[i]/((double)numLBL[i]);
				//System.out.println("countLBL["+i+"]="+countLBL[i]+"\n");
			}
			if (numLBH[i]>0){
				countLBH[i] = countLBH[i]/((double)numLBH[i]);
				fslLBH[i] = fslLBH[i]/((double)numLBH[i]);
			}
		}
	}

	public void setBinWidth(int w){
		this.width = w;
		for (int k=0; k<numFreqs; k++){
			DR = allDR.get(k);
			psth = new ArrayList<Integer>();
			for (int i=0; i<(totTime/width);i++){
				psth.add(0);
			}
			for (int i=0; i< DR.length; i++){
				for (int j=0; j< DR[0].length;j++){
					time = DR[i][j];
					index = (int)(Math.floor((double)time/(width/1000.0)));
					if (time>0 && index<psth.size()){
						psth.set(index,psth.get(index)+1);
					}
				}
			}
			allPSTH.set(k,psth);
		}
	}

	public void adjustSpontaneousRate(double x, String type){
		//generator = new Random(123456789L);
		ArrayList<Double> spikeTrain;
		ArrayList<Double> rate;
		int ind, numAdd;//, j;
		double variance = (0.5d);
		double t, prob;
		double sampRate = 2400.0d;
		/*tAllRate = new ArrayList<ArrayList<Double>>(allRate);
		tSpikeTrains = new ArrayList<ArrayList<Double>>(spikeTrains);
		*/
		spikeTrains = copy2DArrayList(tSpikeTrains);
                allRate = copy2DArrayList(tAllRate);//new ArrayList<ArrayList<Double>>(allRate);
	
		for (int i=0; i<numFreqs; i++){
			spikeTrain = spikeTrains.get(i);
			rate = 	allRate.get(i);
			/*for (int j=0; j<(int)sampRate; j++){
				t = totTime*(((double)j)/sampRate)/1000.0d;
				prob = generator.nextDouble();
				if (rate.get((int)(1000.0d*t))==0.0d && prob>(x*sweps/(1000.0d*sampRate))){
					spikeTrain.add(t);
					if ((int)(1000.0d*t) < rate.size()){
						rate.set((int)(1000.0d*t),rate.get((int)(1000.0d*t))+(1000.0/((double)sweps)));
					}
				}
				Collections.sort(spikeTrain);
			}*/
			
			for (int j=0; j<rate.size();j++){
				//if ((180.0d*rate.get(j)/((double)(swp))) < 0.5d*x){
				//if (rate.get(j) < 0.5d*(x*60.0/180.0)){
				if (rate.get(j) == 0.0d){
				//numAdd = (int)(generator.nextDouble()*(generator.nextDouble()*((0.2d)*(x-180.0d*rate.get(j)/((double)(swp))))));
					if (type.equals("poisson")){
						numAdd = (int)(getPoisson(((double)(sweps)/1000.0d)*(x-rate.get(j))));
					}
					else if (type.equals("gaussian")){
						numAdd = (int)(generator.nextGaussian()*Math.sqrt(variance) + (((double)(sweps)/1000.0d)*x));
					}
					else{
						numAdd = (int)((10.0d/8.0d)*(generator.nextDouble()*generator.nextDouble()*(((((double)sweps)/1000.0d)*(x-rate.get(j))))));
					}
					for (int k=0; k<numAdd; k++){
                                                t = ((double)(j))/1000.0d + (generator.nextDouble()*0.001d);
                                                spikeTrain.add(t);
                                                rate.set(j,rate.get(j)+(1000.0/((double)sweps)));
					}
				}
				Collections.sort(spikeTrain);
			}
			
			spikeTrains.set(i,spikeTrain);
			allRate.set(i,rate);
		}
		//System.out.println("\n");
	}
	private int getPoisson(double lambda) {
  		double L = Math.exp(-lambda);
  		double p = 1.0;
  		int k = 0;

  		do {
    			k++;
   			p *= Math.random();
  		} while (p > L);
  		return (k - 1);
	}

	/*public double rateSplineY(double x){
		return (double)(rateSpline.getY((float)(x+0.0f)) + 0.0d);
	}*/

	public double calculateSweepFSL(double[] train,double spont){
		thres = 0.01d;
		fsl = 0.0d;
		for (int n=5; n<train.length;n++){
                        P = 0.0d;
                        Time = train[n];
			if (Time>0.0d){
                        	for (int m=0; m<n; m++){
                        	        P +=(double)((double)(Math.pow((N*sponRate*(Time)),m))*Math.exp(-1.0d*N*sponRate*(Time)))/((double)fact(m));
                        	}
                		if (100.0d*P > (100.0d-100.0d*thres)){
                        	        fsl = 1000.0d*Time;
                        	        break;
                		}
			}
		}
		return fsl;
	}

	public void calculateFSLIndiv(int i, double spont, int reps){
		thres = 0.000001d;
                fsl = 0.0d;
                lsl = 0.0d;
                sponRate = spont;
                double iTime = 0.0d;
                boolean fslCalculated = false;
                N = reps;
                        ST = tSpikeTrains.get(i);
                        if (i==0){
                                N = (double)reps;//180;
                        }
                        else{
                                N = (double)reps;//20;
                        }
                        //sponRate = sponRate/N;

                        for (int n=5; n<ST.size();n++){
                                P = 0.0d;
                                Time = ST.get(n);
                                for (int m=0; m<n; m++){
                                        P +=(double)((double)(Math.pow((N*sponRate*(Time)),m))*Math.exp(-1.0d*N*sponRate*(Time)))/((double)fact(m));
                                }
                                if (100.0d*P > (100.0d-100.0d*thres)){
                                        fsl = 1000.0d*Time;
                                        break;
                                        /*if (fslCalculated==false){
                                                fsl = 1000.0d*Time;
                                                fslCalculated = true;
                                                //break;
                                        }
                                        else{
                                                lsl = 1000.0d*Time;
                                        }*/
                                }
                                else{
                                        if (n>=(ST.size()-1) && fslCalculated==false){
                                                fsl = 0.0d;
                                                lsl = 0.0d;
                                        }
                                }
                        }
                        //System.out.println("f"+i+": fsl="+fsl+", lsl= "+lsl+"\n");
                        fslFreq[i] = fsl;
                        lslFreq[i] = lsl;
	}

	public void calculateFSL(double spont, int reps){
		thres = 0.000001d;
		fsl = 0.0d;
		lsl = 0.0d;
		sponRate = spont;
		double iTime = 0.0d;
		boolean fslCalculated = false;
		N = reps;
		
		for (int i=0; i<numFreqs; i++){
			fslCalculated = false;
			ST = tSpikeTrains.get(i);
			if (i==0){
				N = (double)reps;//180;
			}
			else{
				N = (double)reps;//20;
			}
			//sponRate = sponRate/N;

			for (int n=5; n<ST.size();n++){
				P = 0.0d;
				Time = ST.get(n);
				for (int m=0; m<n; m++){
					P +=(double)((double)(Math.pow((N*sponRate*(Time)),m))*Math.exp(-1.0d*N*sponRate*(Time)))/((double)fact(m));
				}
				if (100.0d*P > (100.0d-100.0d*thres)){
					fsl = 1000.0d*Time;
					break;
					/*if (fslCalculated==false){
						fsl = 1000.0d*Time;
						fslCalculated = true;
						//break;
					}
					else{
						lsl = 1000.0d*Time;
					}*/
				}
				else{
					if (n>=(ST.size()-1) && fslCalculated==false){
						fsl = 0.0d;
						lsl = 0.0d;
					}
				}
			}
			//System.out.println("f"+i+": fsl="+fsl+", lsl= "+lsl+"\n");
			fslFreq[i] = fsl;
			lslFreq[i] = lsl;
		}
	}

	public String[] getTitles(String type){
		if (type.equals("basic")){
			return basicColumn;
		}
		else if (type.equals("adv")){
			return advColumn;
		}
		return basicColumn;
	}

	public String getTitle(String param){
		if (param.equals("time")){
			return timeTitle;
		}
		else if (param.equals("swep")){
			return swepTitle;
		}
		else if (param.equals("oddb")){
			return oddbTitle;
		}
		else if (param.equals("levl")){
			return levlTitle;
		}
		else if (param.equals("freq")){
			return freqTitle;
		}
		else if (param.equals("ilvl")){
			return ilvlTitle;
		}
		else if (param.equals("clvl")){
			return clvlTitle;
		}
		else{
			return timeTitle;
		}
	}
	public void setTitle(String param, String title){
		if (param.equals("time")){
                        timeTitle = title;
                }
                else if (param.equals("swep")){
                        swepTitle = title;
                }
                else if (param.equals("oddb")){
                        oddbTitle = title;
                }
                else if (param.equals("levl")){
                        levlTitle = title;
                }
                else if (param.equals("freq")){
                        freqTitle = title;
                }
		else if (param.equals("ilvl")){
			ilvlTitle = title;
		}
		else if (param.equals("clvl")){
			clvlTitle = title;
		}
                else{
                        timeTitle = title;
                }
	}

	private boolean containsOverview(ArrayList<double[]> tot,double freq, double inten, double odd){
		if (tot.size() <= 0){
			return false;
		}
		for (int i=0; i> tot.size(); i++){
			if (tot.get(i)[0] == freq && tot.get(i)[1] == inten && tot.get(i)[2] == odd){
				return true;
			}
		}
		return false;
	}

	private double[] calcRange(ArrayList<double[]> tot, int ind){
		double max = tot.get(0)[ind];
		double min = tot.get(0)[ind];
		double[] ans = new double[2];
		ans[0] = min;
		ans[1] = max;
		for (int i=0; i<tot.size(); i++){
			if (tot.get(i)[ind] > max){
				max = tot.get(i)[ind];
			}
			if (tot.get(i)[ind] < min){
				min = tot.get(i)[ind];
			}
		}
		ans[0] = min;
		ans[1] = max;
		return ans;
	}

	private ArrayList<Double> copyArrayList(ArrayList<Double> list){
		ArrayList<Double> newList = new ArrayList<Double>();
		double x;
		for (int i=0; i< list.size(); i++){
			x = list.get(i);
			newList.add(x);
		}
		return newList;
	}
	private ArrayList<ArrayList<Double>> copy2DArrayList(ArrayList<ArrayList<Double>> list){
		ArrayList<ArrayList<Double>> newList = new ArrayList<ArrayList<Double>>();
		for (int i=0; i<list.size(); i++){
			newList.add(copyArrayList(list.get(i)));
		}
		return newList;
	}

	private int count(double[] a){
		int ans = 0;
		for (int i=0; i<a.length; i++){
			if (a[i]!=0.0d && a[i]<=(((double)rTime)/1000.0d) && a[i]>=(((double)lTime)/1000.0d)){
				ans += 1;
				//System.out.println("648: ans="+ans+"\n");
			}
		}
		return ans;
	}

	private int fact(int n){
		if (n <= 1){
			return 1;
		}
		else{
			return n*fact(n-1);
		}
	}

	public int getNumOverview(){
		return numOverview;
	}
	public double[] getOverFreqRange(){
		return overFreqRange;
	}
	public double[] getOverIntenRange(){
                return overIntenRange;
        }

	public int getMaxNumFreq(){
		return maxNumFreq;
	}
	public int getMaxNumIntens(){
		return maxNumIntens;
	}				
	public int getMaxNumIpsiIntens(){
		return maxNumIpsiIntens;
	}
	public int getMaxNumILD(){
                return maxNumILD;
        }			
	
	public int getCount(int i){
		return freqs.get(i);
	}

	//TOTAL AP COUNTS:
	public int getTotAPCount(int l){
                return totAPCount[l];
        }
	public int getTotIntensAPCount(int l){
                return totIntensAPCount[l];
        }
	public int getTotIpsiIntensAPCount(int l){
                return totIpsiIntensAPCount[l];
        }
	public int getTotILDAPCount(int l){
                return totILDAPCount[l];
        }
	
	//REGULAR AP COUNTS
	public void setAPCount(int l, int count){
		APCount[l] = count;
	}
	public int getAPCount(int l){
		return APCount[l];
	}
	public void setIntensAPCount(int l, int count){
		IntensAPCount[l] = count;
	}
	public int getIntensAPCount(int l){
		return IntensAPCount[l];
	}
	public void setIpsiIntensAPCount(int l, int count){
		ipsiIntensAPCount[l] = count;
	}
	public int getIpsiIntensAPCount(int l){
		return ipsiIntensAPCount[l];
	}
	public void setILDAPCount(int l, int count){
                ILDAPCount[l] = count;
        }
	public int getILDAPCount(int l){
		return ILDAPCount[l];
	}	

	private ArrayList<Integer> newDataNumArrayList(){
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int k=0;k<minSwep;k++){
                	list.add(0);
                }
		return list;
	}
	private double[][] newDRArray(){
		double[][] list = new double[minSwep][minAP];
		return list;
		//for (int i=0; i<minSwep;
	}
	private ArrayList<Integer> newPSTHArrayList(){
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int k=0;k<num;k++){
                        list.add(0);
                }
		return list;
	}
	private ArrayList<Double> newRateArrayList(){
                ArrayList<Double> list = new ArrayList<Double>();
                for (int k=0;k<numr;k++){
                        list.add(0.0);
                }
		return list;
        }

	//SSA ANALYSIS ARRAYS:
	public double getFSL_SSA(String s, int i){
		if (s.equals("HBL")){
			return fslHBL[i];
		}
		else if (s.equals("LBL")){
			return fslLBL[i];
		}
		else if (s.equals("LBH")){
                        return fslLBH[i];
                }
		else if (s.equals("HBH")){
                        return fslHBH[i];
                }
		return fslHBL[i];
	}
	public double getCount_SSA(String s, int i){
		if (s.equals("HBL")){
                        return countHBL[i];
                }
                else if (s.equals("LBL")){
                        return countLBL[i];
                }
                else if (s.equals("LBH")){
                        return countLBH[i];
                }
                else if (s.equals("HBH")){
                        return countHBH[i];
                }
                return countHBL[i];
	}
	public double getNum_SSA(String s, int i){
		if (s.equals("HBL")){
                        return numHBL[i];
                }
                else if (s.equals("LBL")){
                        return numLBL[i];
                }
                else if (s.equals("LBH")){
                        return numLBH[i];
                }
                else if (s.equals("HBH")){
                        return numHBH[i];
                }
                return numHBL[i];
	}
	public ArrayList<Double> getIndivCount_SSA(String s, int i){
		if (s.equals("HBL")){
                        return countHBLList.get(i);
                }
                else if (s.equals("LBL")){
                        return countLBLList.get(i);
                }
                else if (s.equals("LBH")){
                        return countLBHList.get(i);
                }
                else if (s.equals("HBH")){
                        return countHBHList.get(i);
                }
                return countHBLList.get(i);
	}
	public ArrayList<Integer> getMissingSweps_SSA(){
		return missingSweps;
	}

	//CUSTOM PSTH:
	public boolean isCustomPSTHy(){
		return isCustomPSTHy;
	}
	public void setIsCustomPSTHy(boolean a){
		isCustomPSTHy = a;
	}
	public int getCustomPSTHy(){
		return customPSTHy;
	}
	public void setCustomPSTHy(int a){
		this.customPSTHy = a;
	}

	//CUSTOM DR:
	public void setDotSize(int a){
		this.dotSize = a;
	}
	public int getDotSize(){
		return this.dotSize;
	}

	public int getFreqSelected(int i){
		return freqSelected[i];
	}
	public int getIntenSelected(int i){
		return intenSelected[i];
	}
	public int getIpsiIntenSelected(int i){
                return ipsiIntenSelected[i];
        }
	public int getILDSelected(int i){
                return ILDSelected[i];
        }		
	public void setFreqSelected(int i, int k){
                freqSelected[i] = k;
        }
        public void setIntenSelected(int i, int k){
                intenSelected[i] = k;
        }
	public void setIpsiIntenSelected(int i, int k){
                ipsiIntenSelected[i] = k;
        }
	public void setILDSelected(int i, int k){
                ILDSelected[i] = k;
        }

	public void setSingleFreqSelected(int i, int k){
		for (int j=0; j<freqSelected.length; j++){
                        if (j==i){
				freqSelected[j] = k;
                	}
			else{
				freqSelected[j] = 0;
			}
		}
	}
	public void setSingleIntenSelected(int i, int k){
                for (int j=0; j<intenSelected.length; j++){
                        if (j==i){
                                intenSelected[j] = k;
                        }
                        else{
                                intenSelected[j] = 0;
                        }
                }
        }
	public void setSingleIpsiIntenSelected(int i, int k){
                if (closedField){
			for (int j=0; j<ipsiIntenSelected.length; j++){
                        	if (j==i){		
                        	        ipsiIntenSelected[j] = k;
                        	}
                        	else{
                        	        ipsiIntenSelected[j] = 0;
                        	}
			}
                }
        }
	public void setSingleILDSelected(int i, int k){
                if (closedField){
			for (int j=0; j<ILDSelected.length; j++){
                        	if (j==i){
                        	        ILDSelected[j] = k;
                        	}
                        	else{
                        	        ILDSelected[j] = 0;
                        	}
                	}
		}
        }
	
	public void setAllSingleSelected(int i, int k){
		setSingleFreqSelected(i,k);
		setSingleIntenSelected(i,k);
		setSingleIpsiIntenSelected(i,k);
		setSingleILDSelected(i,k);
	}

	public void setAllFreqSelected(int k){
		for (int i=0; i<freqSelected.length; i++){
			freqSelected[i] = k;
		}
	}
	public void setAllIntenSelected(int k){
                for (int i=0; i<intenSelected.length; i++){
                        intenSelected[i] = k;
                }
        }
	public void setAllIpsiIntenSelected(int k){
                if (closedField){
			for (int i=0; i<ipsiIntenSelected.length; i++){
                	        ipsiIntenSelected[i] = k;
                	}
		}	
        }
	public void setAllILDSelected(int k){
                if (closedField){
			for (int i=0; i<ILDSelected.length; i++){
                	        ILDSelected[i] = k;
                	}
		}
        }
	public void setAllSelected(int k){
		setAllFreqSelected(k);
		setAllIntenSelected(k);
		setAllIpsiIntenSelected(k);
		setAllILDSelected(k);
	}
	
	public boolean getFSLSelected(){
		return fslSelected;
	}
	public void setFSLSelected(boolean a){
		fslSelected = a;
	}
	public boolean getPeakSelected(){
		return peakSelected;
	}
	public void setPeakSelected(boolean a){
		peakSelected = a;
	}
	
	//RETURN PSTHs:
	public ArrayList<Integer> getPSTH(int i){
		return allPSTH.get(i);
	}
	public ArrayList<Integer> getIntensPSTH(int i){
		return allIntensPSTH.get(i);
	}
	public ArrayList<Integer> getIpsiIntensPSTH(int i){
                return allIpsiIntensPSTH.get(i);
        }
	public ArrayList<Integer> getILDPSTH(int i){
		return allILDPSTH.get(i);
	}

	//RETURN RATES:
	public ArrayList<Double> getRate(int i){
		return allRate.get(i);
	}
	public ArrayList<Double> getIntensRate(int i){
		return allIntensRate.get(i);
	}
	public ArrayList<Double> getIpsiIntensRate(int i){
		return allIpsiIntensRate.get(i);
	}					
	public ArrayList<Double> getILDRate(int i){
		return allILDRate.get(i);
	}

	//RETURN DRs:
	public double[][] getDR(int i){
		return allDR.get(i);
	}
	public double[][] getIntensDR(int i){
		return allIntensDR.get(i);
	}
	public double[][] getIpsiIntensDR(int i){
		return allIpsiIntensDR.get(i);
	}
	public double[][] getILDDR(int i){
		return allILDDR.get(i);
	}

	public double[] getOverview(int i){
		return allOverview.get(i);
	}

	public int getlTime(){
		return lTime;
	}
	public int getrTime(){
		return rTime;
	}
	public void setlTime(int t){
		lTime = t;
	}
	public void setrTime(int t){
		rTime = t;
	}

	public int getlLineX(){
		return lLineX;
	}
	public int getrLineX(){
		return rLineX;
	}
	public int gethLineY(){
		return hLineY;
	}
	public void setlLineX(int a){
                lLineX=a;
        }
        public void setrLineX(int a){
                rLineX=a;
        }
        public void sethLineY(int a){
                hLineY=a;
        }

	public int getDRlLineX(){
                return DRlLineX;
        }
        public int getDRrLineX(){
                return DRrLineX;
        }
        public void setDRlLineX(int a){
                DRlLineX=a;
        }
        public void setDRrLineX(int a){
                DRrLineX=a;
        }

	public void setDraggingRight(boolean a){
		draggingRight=a;
	}
	public void setDraggingLeft(boolean a){
                draggingLeft=a;
        }
	public boolean getDraggingRight(){
                return draggingRight;
        }
        public boolean getDraggingLeft(){
        	return draggingLeft;
        }

	public void setDRDraggingRight(boolean a){
                DRdraggingRight=a;
        }
        public void setDRDraggingLeft(boolean a){
                DRdraggingLeft=a;
        }
        public boolean getDRDraggingRight(){
                return DRdraggingRight;
        }
        public boolean getDRDraggingLeft(){
                return DRdraggingLeft;
        }
	
	public ArrayList<Double> getDS(){
		return this.DS;
	}

	public ArrayList<Double> getST(int l){
		return this.spikeTrains.get(l);
	}

	public int getFreqIndex(int i){
		return freqIndex.get(i);
	}
	public double getFreqValue(int i){
		return freqValues.get(i);
	}
	public Integer getIntenValue(int i){
		return intens.get(i);
	}
	public Integer getIpsiIntenValue(int i){
		return ipsiIntens.get(i);
	}
	public Integer getILDValue(int i){
		return ILDs.get(i);
	}
	public ArrayList<Integer> getFreqs(){
		return freqs;
	}
	public ArrayList<Integer> getIntens(){
		return intens;
	}
	public ArrayList<Integer> getIpsiIntens(){
		return ipsiIntens;
	}
	public ArrayList<Integer> getILDs(){
		return ILDs;		
	}		
	public int getNumFreqs(){
		return numFreqs;
	}
	public int getNumIntens(){
		return numIntens;
	}
	public int getNumIpsiIntens(){
		return numIpsiIntens;
	}
	public int getNumILD(){
		return numILD;
	}		
	public void setNumFreqs(int n){
		numFreqs = n;
	}
	public void setNumIntens(int n){
		numIntens = n;
	}
	public void setNumIpsiIntens(int n){
		numIpsiIntens = n;
	}
	public void setNumILD(int n){
		numILD = n;
	}
	public void setNumContraIntens(int n){
		System.out.println("1393: set contraIntens");
	}

	public int getMaxSwep(){
		return minSwep;
	}

	public void setSpont(double r){
		this.spont = r;
	}
	public double getSpont(){
		return spont;
	}
	
	//PEAK LATENCY FUNCTIONS:
	public void setPeakLatencyFreq(int i, int x){
		this.peakLatencyFreq[i] = x;
	}
	public int getPeakLatencyFreq(int i){
		if (i<numFreqs){
			return this.peakLatencyFreq[i];
		}
		else{
			return peakLatencyFreq[numFreqs-1];
		}
	}
	public void setPeakLatencyInten(int i, int x){
                this.peakLatencyInten[i] = x;
        }
        public int getPeakLatencyInten(int i){
                if (i<numIntens){
                        return this.peakLatencyInten[i];
                }
                else{
                        return peakLatencyInten[numIntens-1];
                }
        }
	public void setPeakLatencyIpsiInten(int i, int x){
                this.peakLatencyIpsiInten[i] = x;
        }
        public int getPeakLatencyIpsiInten(int i){
                if (i<numIpsiIntens){
                        return this.peakLatencyIpsiInten[i];
                }
                else{
                        return peakLatencyIpsiInten[numIpsiIntens-1];
                }					
        }
	public void setPeakLatencyILD(int i, int x){
                this.peakLatencyILD[i] = x;
        }
        public int getPeakLatencyILD(int i){
                if (i<numILD){
                        return this.peakLatencyILD[i];
                }
                else{
                        return peakLatencyILD[numILD-1];
                }
        }		

	public double[] getFSLFreqList(){
		return fslFreq;
	}
	public double getFSLFreq(int i){
		return fslFreq[i];
	}
	public void setFSLFreq(int i,double f){
		fslFreq[i] = f;
	}
	public int numSweps(){
		return sweps;
	}

	public boolean isClosedField(){
		return closedField;
	}


}

