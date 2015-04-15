/******************************************************************************
*  A Teaching GA					  Developed by Hal Stringer & Annie Wu, UCF
*  Version 2, January 18, 2004
*******************************************************************************/

import java.io.*;
import java.util.*;
import java.text.*;
import java.awt.*;
public class SearchNN {

/*******************************************************************************
*                           INSTANCE VARIABLES                                 *
*******************************************************************************/

/*******************************************************************************
*                           STATIC VARIABLES                                   *
*******************************************************************************/

	public static PrisonerDilemma problem;
	public static Chromosome[] member;
	public static Chromosome[] child;

	public static Chromosome bestOfGenChromosome;
	public static int bestOfGenR;
	public static int bestOfGenG;
	public static Chromosome bestOfRunChromosome;
	public static int bestOfRunR;
	public static int bestOfRunG;
	public static Chromosome bestOverAllChromosome;
	public static int bestOverAllR;
	public static int bestOverAllG;

	public static double sumRawFitness;
	public static double sumRawFitness2;	// sum of squares of fitness
	public static double sumSclFitness;
	public static double sumProFitness;
	public static double defaultBest;
	public static double defaultWorst;

	public static double averageRawFitness;
	public static double stdevRawFitness;

	public static int G;
	public static int R;
	public static Random r = new Random();
	private static double randnum;

	private static int memberIndex[];
	private static double memberFitness[];
	private static int TmemberIndex;
	private static double TmemberFitness;

	private static double fitnessStats[][];  // 0=Avg, 1=Best

	public static void main(String[] args) throws java.io.IOException{

		Calendar dateAndTime = Calendar.getInstance(); 
		Date startTime = dateAndTime.getTime();

	//  Read Parameter File
		System.out.println("\nParameter File Name is: " + args[0] + "\n");
		Parameters parmValues = new Parameters(args[0]);

/*
Parameters.numRuns=2;
Parameters.popSize=10;
Parameters.generations=30;
*/

		int gameTimes=100;
		int[] randomDecisions=new int[gameTimes];
		for(int i=0;i<gameTimes;i++)
		{
			double rand=r.nextDouble();
			if(rand>0.5) randomDecisions[i]=1;
			else randomDecisions[i]=-1;
		}
	//  Store the Statistical Data To the Visualization Data Matrix
		double[] generations=new double[Parameters.generations];
		double[] bestBestFitness=new double[Parameters.generations];
		double[] avgBestFitness=new double[Parameters.generations];
		double[] avgAvgFitness=new double[Parameters.generations];
		double[] avgStdDeviation=new double[Parameters.generations];
		for(int i=0; i<Parameters.generations; i++)
		{
			generations[i]=i;
			bestBestFitness[i]=0;
			avgBestFitness[i]=0;
			avgAvgFitness[i]=0;
			avgStdDeviation[i]=0;
		}

	//  Hold the Global Statistical Data For Several Runs
		double globalBestFitnessSum=0.0;
		double globalBestFitnessSum2=0.0;
		double globalAvgBestFitness=0.0;
		double globalBestFitnessDeviation=0.0;

		int[] firstGenThatGetMaxFitness=new int[Parameters.numRuns];
		for(int i=0; i<Parameters.numRuns; i++)firstGenThatGetMaxFitness[i]=0;


	//  Write Parameters To Summary Output File
		String summaryFileName = Parameters.expID + "_summary.txt";
	//	FileWriter summaryOutput = new FileWriter(summaryFileName);
	//	parmValues.outputParameters(summaryOutput);

	//	Set up Fitness Statistics matrix
		fitnessStats = new double[2][Parameters.generations];
		for (int i=0; i<Parameters.generations; i++){
			fitnessStats[0][i] = 0;
			fitnessStats[1][i] = 0;
		}

	//	Problem Specific Setup - For new new fitness function problems, create
	//	the appropriate class file (extending FitnessFunction.java) and add
	//	an else_if block below to instantiate the problem.
 
		if (Parameters.problemType.equals("PD")){
				problem = new PrisonerDilemma(randomDecisions);
		}
		else System.out.println("Invalid Problem Type");

		System.out.println(problem.name);

	//	Initialize RNG, array sizes and other objects
		r.setSeed(Parameters.seed);
		memberIndex = new int[Parameters.popSize];
		memberFitness = new double[Parameters.popSize];
		member = new Chromosome[Parameters.popSize];
		child = new Chromosome[Parameters.popSize];
		bestOfGenChromosome = new Chromosome();
		bestOfRunChromosome = new Chromosome();
		bestOverAllChromosome = new Chromosome();

		if (Parameters.minORmax.equals("max")){
			defaultBest = 0;
			defaultWorst = 999999999999999999999.0;
		}
		else{
			defaultBest = 999999999999999999999.0;
			defaultWorst = 0;
		}

		bestOverAllChromosome.rawFitness = defaultBest;

		//  Start program for multiple runs
		for (R = 1; R <= Parameters.numRuns; R++){

			bestOfRunChromosome.rawFitness = defaultBest;
			System.out.println();

			//	Initialize First Generation
			for (int i=0; i<Parameters.popSize; i++){
				member[i] = new Chromosome();
				child[i] = new Chromosome();
			}

			//	Begin Each Run
			for (G=0; G<Parameters.generations; G++){

				sumProFitness = 0;
				sumSclFitness = 0;
				sumRawFitness = 0;
				sumRawFitness2 = 0;
				bestOfGenChromosome.rawFitness = defaultBest;

				//	Test Fitness of Each Member
				for (int i=0; i<Parameters.popSize; i++){

					member[i].rawFitness = 0;
					member[i].sclFitness = 0;
					member[i].proFitness = 0;

					problem.doRawFitness(member[i]);

					sumRawFitness = sumRawFitness + member[i].rawFitness;
					sumRawFitness2 = sumRawFitness2 +
						member[i].rawFitness * member[i].rawFitness;

					if (Parameters.minORmax.equals("max")){
						if (member[i].rawFitness > bestOfGenChromosome.rawFitness){
							Chromosome.copyB2A(bestOfGenChromosome, member[i]);
							bestOfGenR = R;
							bestOfGenG = G;
						}
						if (member[i].rawFitness > bestOfRunChromosome.rawFitness){
							Chromosome.copyB2A(bestOfRunChromosome, member[i]);
							bestOfRunR = R;
							bestOfRunG = G;
						}
						if (member[i].rawFitness > bestOverAllChromosome.rawFitness){
							Chromosome.copyB2A(bestOverAllChromosome, member[i]);
							bestOverAllR = R;
							bestOverAllG = G;
						}
					}
					else {
						if (member[i].rawFitness < bestOfGenChromosome.rawFitness){
							Chromosome.copyB2A(bestOfGenChromosome, member[i]);
							bestOfGenR = R;
							bestOfGenG = G;
						}
						if (member[i].rawFitness < bestOfRunChromosome.rawFitness){
							Chromosome.copyB2A(bestOfRunChromosome, member[i]);
							bestOfRunR = R;
							bestOfRunG = G;
						}
						if (member[i].rawFitness < bestOverAllChromosome.rawFitness){
							Chromosome.copyB2A(bestOverAllChromosome, member[i]);
							bestOverAllR = R;
							bestOverAllG = G;
						}
					}
				}

				// Accumulate fitness statistics
				fitnessStats[0][G] += sumRawFitness / Parameters.popSize;
				fitnessStats[1][G] += bestOfGenChromosome.rawFitness;

				averageRawFitness = sumRawFitness / Parameters.popSize;
				stdevRawFitness = Math.sqrt(
							Math.abs(sumRawFitness2 - 
							sumRawFitness*sumRawFitness/Parameters.popSize)
							/
							(Parameters.popSize-1)
							);

				// Output generation statistics to screen
		//		System.out.println(R + "\t" + G +  "\t" + (int)bestOfGenChromosome.rawFitness + "\t" + averageRawFitness + "\t" + stdevRawFitness);

				// Calculate the generation statistics 
				if(bestOfGenChromosome.rawFitness>bestBestFitness[G])
				bestBestFitness[G]=bestOfGenChromosome.rawFitness;
				avgBestFitness[G]+=bestOfGenChromosome.rawFitness;
				avgAvgFitness[G]+=averageRawFitness;
				avgStdDeviation[G]+=stdevRawFitness;

				/* Output generation statistics to summary file 
				summaryOutput.write(" R ");
				Hwrite.right(R, 3, summaryOutput);
				summaryOutput.write(" G ");
				Hwrite.right(G, 3, summaryOutput);
				Hwrite.right((int)bestOfGenChromosome.rawFitness, 7, summaryOutput);
				Hwrite.right(averageRawFitness, 11, 3, summaryOutput);

				Hwrite.right(stdevRawFitness, 11, 3, summaryOutput);
				summaryOutput.write("\n");*/


		// *********************************************************************
		// **************** SCALE FITNESS OF EACH MEMBER AND SUM ***************
		// *********************************************************************

				switch(Parameters.scaleType){

				case 0:     // No change to raw fitness
					for (int i=0; i<Parameters.popSize; i++){
						member[i].sclFitness = member[i].rawFitness + .000001;
						sumSclFitness += member[i].sclFitness;
					}
					break;

				case 1:     // Fitness not scaled.  Only inverted.
					for (int i=0; i<Parameters.popSize; i++){
						member[i].sclFitness = 1/(member[i].rawFitness + .000001);
						sumSclFitness += member[i].sclFitness;
					}
					break;

				case 2:     // Fitness scaled by Rank (Maximizing fitness)

					//  Copy genetic data to temp array
					for (int i=0; i<Parameters.popSize; i++){
						memberIndex[i] = i;
						memberFitness[i] = member[i].rawFitness;
					}
					//  Bubble Sort the array by floating point number
					for (int i=Parameters.popSize-1; i>0; i--){
						for (int j=0; j<i; j++){
							if (memberFitness[j] > memberFitness[j+1]){
								TmemberIndex = memberIndex[j];
								TmemberFitness = memberFitness[j];
								memberIndex[j] = memberIndex[j+1];
								memberFitness[j] = memberFitness[j+1];
								memberIndex[j+1] = TmemberIndex;
								memberFitness[j+1] = TmemberFitness;
							}
						}
					}
					//  Copy ordered array to scale fitness fields
					for (int i=0; i<Parameters.popSize; i++){
						member[memberIndex[i]].sclFitness = i;
						sumSclFitness += member[memberIndex[i]].sclFitness;
					}

					break;

				case 3:     // Fitness scaled by Rank (minimizing fitness)

					//  Copy genetic data to temp array
					for (int i=0; i<Parameters.popSize; i++){
						memberIndex[i] = i;
						memberFitness[i] = member[i].rawFitness;
					}
					//  Bubble Sort the array by floating point number
					for (int i=1; i<Parameters.popSize; i++){
						for (int j=(Parameters.popSize - 1); j>=i; j--){
							if (memberFitness[j-i] < memberFitness[j]){
								TmemberIndex = memberIndex[j-1];
								TmemberFitness = memberFitness[j-1];
								memberIndex[j-1] = memberIndex[j];
								memberFitness[j-1] = memberFitness[j];
								memberIndex[j] = TmemberIndex;
								memberFitness[j] = TmemberFitness;
							}
						}
					}
					//  Copy array order to scale fitness fields
					for (int i=0; i<Parameters.popSize; i++){
						member[memberIndex[i]].sclFitness = i;
						sumSclFitness += member[memberIndex[i]].sclFitness;
					}

					break;

				default:
					System.out.println("ERROR - No scaling method selected");
				}


		// *********************************************************************
		// ****** PROPORTIONALIZE SCALED FITNESS FOR EACH MEMBER AND SUM *******
		// *********************************************************************

				for (int i=0; i<Parameters.popSize; i++){
					member[i].proFitness = member[i].sclFitness/sumSclFitness;
					sumProFitness = sumProFitness + member[i].proFitness;
				}

		// *********************************************************************
		// ************ CROSSOVER AND CREATE NEXT GENERATION *******************
		// *********************************************************************

				int parent1 = -1;
				int parent2 = -1;

				//  Assumes always two offspring per mating
				for (int i=0; i<Parameters.popSize; i=i+2){

					//	Select Two Parents
					parent1 = Chromosome.selectParent();
					parent2 = parent1;
					while (parent2 == parent1){
						parent2 = Chromosome.selectParent();
					}

					//	Crossover Two Parents to Create Two Children
					randnum = r.nextDouble();
					if (randnum < Parameters.xoverRate){
						Chromosome.mateParents(parent1, parent2, member[parent1], member[parent2], child[i], child[i+1]);
					}
					else {
						Chromosome.mateParents(parent1, member[parent1], child[i]);
						Chromosome.mateParents(parent2, member[parent2], child[i+1]);
					}
				} // End Crossover

				//	Mutate Children
				for (int i=0; i<Parameters.popSize; i++){
					child[i].doMutation();
				}

				//	Swap Children with Last Generation
				for (int i=0; i<Parameters.popSize; i++){
					Chromosome.copyB2A(member[i], child[i]);
				}

			} //  Repeat the above loop for each generation



//			Hwrite.left(bestOfRunR, 4, summaryOutput);
//			Hwrite.right(bestOfRunG, 4, summaryOutput);

//			problem.doPrintGenes(bestOfRunChromosome, summaryOutput);


			// Print the Best Fitness in One Run
			System.out.println("The "+R+ "th Run's Best Fitness is: " +(int)bestOfRunChromosome.rawFitness);

			//Calculate Average Best Fitness And its Deviation Over Several Runs
			globalBestFitnessSum+=bestOfRunChromosome.rawFitness;
			globalBestFitnessSum2+=bestOfRunChromosome.rawFitness*bestOfRunChromosome.rawFitness;
			firstGenThatGetMaxFitness[R-1]=bestOfRunG;

		} //End of a Run

/*
		Hwrite.left("B", 8, summaryOutput);

		problem.doPrintGenes(bestOverAllChromosome, summaryOutput);

		//	Output Fitness Statistics matrix
		summaryOutput.write("Gen                 AvgFit              BestFit \n");
		for (int i=0; i<Parameters.generations; i++){
			Hwrite.left(i, 15, summaryOutput);
			Hwrite.left(fitnessStats[0][i]/Parameters.numRuns, 20, 2, summaryOutput);
			Hwrite.left(fitnessStats[1][i]/Parameters.numRuns, 20, 2, summaryOutput);
			summaryOutput.write("\n");
		}

		summaryOutput.write("\n");
		summaryOutput.close();
*/
		System.out.println();
		System.out.println("Start:  " + startTime);
		dateAndTime = Calendar.getInstance(); 
		Date endTime = dateAndTime.getTime();
		System.out.println("End  :  " + endTime);

		problem.doPrintGenes(bestOverAllChromosome);

		globalAvgBestFitness=globalBestFitnessSum/Parameters.numRuns;
		globalBestFitnessDeviation=Math.sqrt
		(
			Math.abs(globalBestFitnessSum2-globalBestFitnessSum*globalBestFitnessSum/Parameters.numRuns)
			/
			(Parameters.numRuns-1)
		);

		System.out.println("After the "+Parameters.numRuns+ "th Run, the Avg Best Fitness is: " +globalAvgBestFitness);
		System.out.println("After the "+Parameters.numRuns+ "th Run, the Std Deviation is: " +globalBestFitnessDeviation);

		double firstGenSum=0.0;
		System.out.println("The first generation in which an optimum indv is found in each run are shown below respectively:");
		for(int i=0; i<Parameters.numRuns; i++)
		{
			firstGenSum+=firstGenThatGetMaxFitness[i];
			System.out.print(firstGenThatGetMaxFitness[i]+" ");
		}
		System.out.println("\nThe average of the first generation in which an optimum indv is found in each run is:"+(firstGenSum/Parameters.numRuns));

		for(int i=0; i<Parameters.generations; i++)
		{
			avgBestFitness[i]/=Parameters.numRuns;
			avgAvgFitness[i]/=Parameters.numRuns;
			avgStdDeviation[i]/=Parameters.numRuns;
		}
		double[][] gen=new double[2][];
		int[] dataLengths=new int[2];
		boolean[] isColumns=new boolean[2];
		for(int i=0;i<2;i++)
		{
			gen[i]=generations;
			dataLengths[i]=gen[i].length;
			isColumns[i]=false;
		}
		double[][] ave=new double[][]
		{
			avgAvgFitness,
			avgStdDeviation
		};
		Color[] dataColors=new Color[]
		{
			Color.green,
			Color.yellow
		};
		String[] dataLabels=new String[]
		{
			"avgAvgFitness",
			"avgStdDeviation"
		};
		SearchFrame SearchFrame1=new SearchFrame(gen,ave,dataLengths,isColumns,dataColors,dataLabels,"Gen","Fitness");
		SearchFrame1.setVisible(true);

	} // End of Main Class

}   // End of Search.Java ******************************************************
