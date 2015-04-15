/******************************************************************************
*  A Teaching GA					  Developed by Wanwan Li, UCF
*  Version 2, January 18, 2004
*******************************************************************************/

import java.io.*;
import java.util.*;
import java.text.*;

public class PrisonerDilemma
{

/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/
	String name;
	int[] opponentDecisions;
	int[] myDecisions;

/*******************************************************************************
*                            STATIC VARIABLES                                  *
*******************************************************************************/


/*******************************************************************************
*                              CONSTRUCTORS                                    *
*******************************************************************************/

	public PrisonerDilemma (int[] opponentDecisions)
	{
		this.name = "Prisoner's Dilemma ";
		this.opponentDecisions=opponentDecisions;				// the opponents decisions [-1,+1] represents [D, C]
		this.myDecisions=new int[opponentDecisions.length];			// my decisions [-1,+1] represents [Defect, Coorperate]
	}

/*******************************************************************************
*                                MEMBER METHODS                                *
*******************************************************************************/

//  COMPUTE A CHROMOSOME'S RAW FITNESS *************************************

	public void doRawFitness(Chromosome X)
	{
		int historyTimes=Parameters.numGenes/2;			// how many times of the history of decisions are considered
		this.makeSimpleDecisions(historyTimes);			// at the the beginning, no history are recorded, so only use simple decision
		int totalTimes=this.opponentDecisions.length;
		for(int i=historyTimes; i<totalTimes; i++)
		{
			this.myDecisions[i]=this.makeDecision(X,i);		// use Neural Network to make decision according to the history of decisoions
		}
		X.rawFitness=0;
		for(int i=0; i<totalTimes; i++)					// accumulating the fitness value for chromosome according to the chioces are made
		{
			X.rawFitness+=this.fitness(i);
		}
		X.rawFitness-=150;
	}

	private double fitness(int time)
	{
		int a=this.myDecisions[time];
		int b=this.opponentDecisions[time];
		return -0.75*a+1.75*b+2.25;
	}

	private void makeSimpleDecisions(int times)
	{
		this.myDecisions[0]=1;
		for (int i=1; i<times; i++)
		{
			this.myDecisions[i]=this.opponentDecisions[i-1];
		}
	}

	private int makeDecision(Chromosome X,int time)
	{
		int historyTimes=Parameters.geneSize/2,c=0;
		double[] input= new double[Parameters.geneSize];				// the input values of Neural Networks are make up of the decision history
		for(int i=1;i<=historyTimes;i++)
		{
			input[c++]=this.opponentDecisions[time-i];
			input[c++]=this.myDecisions[time-i];
		}
		double[] output=new double[Parameters.numGenes];			// the output values are calculated from the weighted accumulation of inputs
		for (int i=0; i<Parameters.numGenes; i++)
		{ 
			output[i]=0.0;
			for (int j=0; j<Parameters.geneSize; j++)
			{
				output[i]+=X.weights[i][j]*input[j];
			}
		}
		double result=0.0;
		for (int i=0; i<Parameters.numGenes; i++)				// the result value are calculated from the biased accumulation of outputs
		{ 
			result+=output[i]*X.bias[i];
		}
		return result>0.0?1:-1;
	}

//  PRINT OUT AN INDIVIDUAL GENE TO THE SUMMARY FILE *********************************

	public void doPrintGenes(Chromosome X)
	{
		System.out.println("Chromosome. Solution ( Weights and Bias ) are:");int c=0;
		System.out.println("***************************************************");
		int rawFitness=0;
		System.out.println("Neural Network.Weights=");
		System.out.println("{");	
		for (int i=0; i<Parameters.numGenes; i++)		
		{ 
			System.out.print("\t{");
			for (int j=0; j<Parameters.geneSize; j++) 		
			{
				System.out.print(X.weights[i][j]);
				if(j==Parameters.geneSize-1)System.out.print("},\n");
				else System.out.print(",\t");
			}
		}
		System.out.println("};");
		System.out.println("Neural Network.Bias=");
		System.out.println("{");
		for (int j=0; j<Parameters.geneSize; j++) 		
		{
			System.out.print("\t");
			System.out.print(X.bias[j]);	
			if(j==Parameters.geneSize-1)System.out.print("\n");
			else System.out.print(",\t");
		}
		System.out.println("};");
		System.out.println();
		System.out.println("***************************************************");
		System.out.println("Chromosome. Raw Fitness is:"+X.rawFitness);
	}

/*******************************************************************************
*                             STATIC METHODS                                   *
*******************************************************************************/

}   // End of PrisonerDilemma.java ******************************************************

