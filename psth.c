#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <math.h>

void addAP(int totTime, int width, double time, int swep, int *psth, double **data, int *dataNum)
{
	int lastIndex = 0;
	int num = totTime/width;
	int index = (int)(floor((double)time/(width/1000.0)));
	if (index<num || time<(double)(totTime+0.0)){
		psth[index]+=1;
		lastIndex = dataNum[swep];
		data[swep][lastIndex]=time;
		dataNum[swep]=dataNum[swep]+1;
	}
}

int main(int argc, char *argv[])
{
	char line[1000];
	char *token;
	FILE *ptr;
	int i=1,j=1;
	int width=0;
	double time=0.0;
	int swep=0;
	int totTime = 250;
	int num=0;
	int minAP = 50, minSwep = 200;

	ptr = fopen("170414_180_Snip_TankSortChirag.csv","rt");
	
	if (ptr == NULL)
	{
		printf("ERROR: .csv file is missing.");
                return 0;
	}

	fgets(line,999,ptr);
	fgets(line,999,ptr);

	printf("Enter bin width: ");
	scanf("%d",&width);
	while (width<=0 || totTime%(width)!=0)
	{
		printf("Invalid entry: Enter a width that divides 250 and is greater than 0");
		scanf("%d",&width);
	}

	num = totTime/width;

	int *psth = (int *)malloc(num*sizeof(int));	
	double **data = malloc(minSwep*sizeof(* data));
	int *dataNum = (int *)malloc(minSwep*sizeof(int));

	int k,l;
	for (k=0;k<num;k++){
		psth[k] = 0;
	}
	
	if (data){
		for (k=0;k<minSwep;k++){
			data[k] = (double *)malloc(minAP*sizeof(double));
			dataNum[k] = 0;
			for (int l=0;l<minAP;l++){
				data[k][l] = 0.0;	
			}
		}
	}

	while (feof(ptr)==0)
	{
		token = strtok(line,",");
		while (token!=NULL)
		{
			if (j==4){
				time = atof(token);
				printf("\ntime=%f, ",time);
			}
			else if (j==8){
				swep = atoi(token);
				printf("swep=%d",swep);
				//token = NULL;
				break;
			}
			token = strtok(NULL,",");
			j++;
		}
		j=1;
		addAP(totTime,width,time,swep,psth,data,dataNum);
		fgets(line,999,ptr);
		i++;
	}
	
	printf("\n");
	printf("  TIME | COUNT | GRAPH\n");
	printf("---------------------------------------------------------------------------\n");
	for (int i=0;i<num;i++){
		printf("%3d-%3d| ",i*width,(i+1)*width);
		printf("%2d    |",psth[i]);
		for (int j=0;j<psth[i];j++){
			printf("=");
			//printf(" %0*d\n", psth[i], 0);
		}
		printf("\n");
	}

	fclose(ptr);
	return 0;
}
