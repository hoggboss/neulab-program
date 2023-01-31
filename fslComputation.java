import java.util.*;
import java.awt.*;
import java.io.*;

public class fslComputation{
	//static Scanner infile;
	static psthClass neuron;
	static double ave;
	static int numFreqs;
	static int reps = 1000;
	static int lim = 0;
	static int sweps;
	static double delta = 0.0;
	static double[] rates = {0.0d,1.0d,5.0d,10.0d,15.0d,20.0d,25.0d,30.0d,35.0d,40.0d,45.0d,50.0d,55.0d,60.0d,65.0d,70.0d,75.0d,80.0d,85.0d,90.0d,95.0d,100.0d,105.0d,110.0d,115.0d,120.0d,125.0,130.0d,135.0d,140.0d,145.0d,150.0d};
	static int numRates = rates.length;
	static double[][][] fslCalculations;
	static double[][] fsl, fslVar;
	
	public fslComputation(int swep,int psthlen, int width, int totTime, int minAP, int minSwep, Scanner infile){
		this.sweps = swep;
		neuron = new psthClass(psthlen,width,totTime,minAP,minSwep,infile);
		numFreqs = neuron.getNumFreqs();
		if (numFreqs<=1){
			lim = 0;
		}
		else{
			lim = 1;
		}
		fslCalculations = new double[numFreqs][numRates][reps];
		fsl = new double[numFreqs][numRates];
		fslVar = new double[numFreqs][numRates];
		calculateFSL();
	}
	
	private void calculateFSL(){
		for (int i=lim; i<numFreqs; i++){
			for (int j=0; j<numRates; j++){
				//neuron.adjustSpontaneousRate(rates[j]);
				ave = 0.0;
				for (int k=0; k<reps; k++){
					/*if (j!=0){
						//neuron.adjustSpontaneousRate(0.0);
						neuron.adjustSpontaneousRate(rates[j]+0.0d);
					}*/
					neuron.adjustSpontaneousRate(rates[j]+0.0d);
					neuron.calculateFSLIndiv(i,rates[j]+0.0d,sweps);
					fslCalculations[i][j][k] = neuron.getFSLFreq(i);
					//System.out.println("f"+i+", rates["+j+"]="+rates[j]+", fsl= "+fslCalculations[i][j][k]+"\n");
					ave += neuron.getFSLFreq(i);
				}
				ave = ave/((double)reps);
				//System.out.println("f"+i+"; fsl="+ave);
				//System.out.println("\n-----------");
				fsl[i][j] = ave;
				calcVar(i,j,ave);
			}
		}
	}
	private void calcVar(int i, int j, double aver){
		double var = 0.0d;
		for (int k=0; k<reps; k++){
			var += (fslCalculations[i][j][k] - aver)*(fslCalculations[i][j][k] - aver);
		}
		var = var/((double)(reps-1.0));
		fslVar[i][j] = Math.pow(var,0.5);
	}

	public void export(String type,String name) throws IOException{
		FileWriter f = new FileWriter("FSL_"+type+".csv");
		f.write(name+"\n");
		for (int i=lim; i<numFreqs; i++){
			f.write(",f"+i+",N="+reps+",,,,");
		}
		f.write("\n");
		for (int i=lim; i<numFreqs; i++){
			f.write(",FSL,FSL-low,FSL-high,StdDev,,");
		}
		f.write("\n");
		for (int j=0; j<numRates; j++){
			for (int i=lim; i<numFreqs;i++){
		   	   f.write(rates[j]+","+fsl[i][j]+","+(fsl[i][j]-fslVar[i][j])+","+(fsl[i][j]+fslVar[i][j])+","+fslVar[i][j]+",,");
			}
			f.write("\n");
		}
		f.close();
	}
	
	public static void main(String[] args) throws IOException{
		fslComputation neuFSL;
		Scanner file;
		int swe=60;
		//String name = "050214_70_Snip_TankSortChirag.csv";
		//String name = "260314_10_Snip_TankSortChirag.csv";
		//String name = "070314_230_Snip_TankSortChirag.csv";
		//String name = "160414_29_Snip_TankSortChirag.csv";
		//String name = "170414_185_Snip_TankSortChirag.csv";
		//String name = "160414_29_Snip_TankSortChirag.csv";
		String[] type = {"Onset","On-Adapting","Chopper","Buildup"};
			
		String[] files = {"170414_180_Snip_TankSortChirag.csv","170414_185_Snip_TankSortChirag.csv","070314_230_Snip_TankSortChirag.csv","070314_236_Snip_TankSortChirag.csv","070314_23_Snip_TankSortChirag.csv","070314_73_Snip_TankSortChirag.csv","160414_28_Snip_TankSortChirag.csv","160414_29_Snip_TankSortChirag.csv"};	
		
		try{
			for (int i=0; i<files.length;i++){
				file = new Scanner(new BufferedReader(new FileReader(files[i])));
				neuFSL = new fslComputation(swe,100,5,250,200,200,file);
				neuFSL.export((type[(i/2)]+"_"+(i%2+1)),files[i]);
			}
			/*file = new Scanner(new BufferedReader(new FileReader(name)));
                	neuFSL = new fslComputation(swe,100,5,250,200,200,file);
                	neuFSL.export("");*/
		}
		catch (IOException e){
			System.out.println(e.getMessage());
		}
		catch (Exception e){
			System.out.println(e.getMessage());
		}
		System.out.println("COMPLETE");
	}
}
