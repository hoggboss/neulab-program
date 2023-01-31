import java.util.*; 
import java.awt.*;
import java.io.*;

public class psth{
	public static void main(String[] args) throws IOException{
        	int width=0,index=0,lastIndex=0;
        	double time=0.0;
        	int swep=0;
        	int totTime = 250;
        	int num=0;
        	int minAP = 50, minSwep = 200;
		Scanner kb = new Scanner(System.in);
  		Scanner infile = new Scanner(new BufferedReader(new FileReader("170414_180_Snip_TankSortChirag.csv")));
  		String[] line = infile.nextLine().split(",");
		line = infile.nextLine().split(",");
				
		System.out.println("Enter bin width:");
		width = kb.nextInt();
		
		while (width<=0 || totTime%(width)!=0)
        	{
			System.out.println("Invalid entry: Enter a width that divides 250 and is greater than 0");
                	width = kb.nextInt();
        	}
		
		num = totTime/width;

		ArrayList <Integer> psth = new ArrayList<Integer>();
		ArrayList<ArrayList<Double>> data = new ArrayList<ArrayList<Double>>();
		ArrayList <Integer> dataNum = new ArrayList<Integer>();

		ArrayList <Double> nextSwep = new ArrayList<Double>();

        	for (int k=0;k<num;k++){
                	psth.add(0);
        	}

                for (int k=0;k<minSwep;k++){
			dataNum.add(0);
     		}

		while (infile.hasNextLine()){
			time = Double.parseDouble(line[3]);
			swep = Integer.parseInt(line[7]);
			//lastIndex = 0;
        		index = (int)(Math.floor((double)time/(width/1000.0)));
        		if (index<num || time<(double)(totTime+0.0)){
                		psth.set(index,psth.get(index)+1);
                		lastIndex = dataNum.get(swep);
                		//data.get(swep).set(lastIndex,time);
				dataNum.set(swep,dataNum.get(swep)+1);
				//data[swep][lastIndex]=time;
                		//dataNum[swep]=dataNum[swep]+1;
        		}
			line = infile.nextLine().split(",");
		}

        	System.out.println("\n  TIME | COUNT | GRAPH");
        	System.out.println("---------------------------------------------------------------------------");
		for (int i=0;i<num;i++){
        	        System.out.printf("%3d-%3d| %2d    |",i*width,(i+1)*width,psth.get(i));
        	        for (int j=0;j<psth.get(i);j++){
        	                System.out.print("=");
        	        }
        	        System.out.println("");
        	}
	
		
	}

}

