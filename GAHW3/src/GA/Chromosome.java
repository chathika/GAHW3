/******************************************************************************
*  A Teaching GA					  Developed by Wanwan Li, UCF
*  Version 2, January 18, 2004
*******************************************************************************/

import java.io.*;
import java.util.*;
import java.text.*;

public class Chromosome
{
/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/

	public double[] bias;
	public double[][] weights;
	public double rawFitness;
	public double sclFitness;
	public double proFitness;

/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/

	private static double randnum;

/*******************************************************************************
*                              CONSTRUCTORS                                    *
*******************************************************************************/

	public Chromosome()
	{
		this.weights = new double[Parameters.numGenes][Parameters.geneSize];
		for (int i=0; i<Parameters.numGenes; i++)		
		{ 
			for (int j=0; j<Parameters.geneSize; j++) 		
			{
				// find weights for each neural network unit
				// each weight is belong to [-0.5, 0.5]
				double rand=SearchNN.r.nextDouble();
				this.weights[i][j]=rand-0.5;
			}
		}
		this.bias = new double[Parameters.geneSize];
		for (int j=0; j<Parameters.geneSize; j++)
		{
			// find bias for each neural network unit
			// each bias is belong to [0.5, 1]
			double rand=SearchNN.r.nextDouble();
			this.bias[j]=0.5+rand*0.5;
		}
		this.rawFitness = -1;   //  Fitness not yet evaluated
		this.sclFitness = -1;   //  Fitness not yet scaled
		this.proFitness = -1;   //  Fitness not yet proportionalized
	}


/*******************************************************************************
*                                MEMBER METHODS                                *
*******************************************************************************/

	//  Get Alpha Represenation of a Gene **************************************

	public String getGeneAlpha(int geneID){
		return "01";
	}

	//  Get Integer Value of a Gene (Positive or Negative, 2's Compliment) ****

	public int getIntGeneValue(int geneID){
		return 0;
	}

	//  Get Integer Value of a Gene (Positive only) ****************************

	public int getPosIntGeneValue(int geneID){
		return 0;
	}

	//  Mutate a Chromosome Based on Mutation Type *****************************

	public void doMutation()
	{
		return;
	}

/*******************************************************************************
*                             STATIC METHODS                                   *
*******************************************************************************/

	//  Select a parent for crossover ******************************************

	public static int selectParent(){

		double rWheel = 0;
		int j = 0;
		int k = 0;

		switch (Parameters.selectType){

		case 1:     // Proportional Selection
			randnum = SearchNN.r.nextDouble();
			for (j=0; j<Parameters.popSize; j++){
				rWheel = rWheel + SearchNN.member[j].proFitness;
				if (randnum < rWheel) return(j);
			}
			break;

		case 3:     // Random Selection
			randnum = SearchNN.r.nextDouble();
			j = (int) (randnum * Parameters.popSize);
			return(j);

		case 2:     //  Tournament Selection

		default:
			System.out.println("ERROR - No selection method selected");
		}
	return(-1);
	}

	//  Produce a new child from two parents  **********************************
	public static void mateParents(int pnum1, int pnum2, Chromosome parent1, Chromosome parent2, Chromosome child1, Chromosome child2)
	{
		double[][] childWeights1 = new double[Parameters.numGenes][Parameters.geneSize];
		double[][] childWeights2 = new double[Parameters.numGenes][Parameters.geneSize];
		for (int i=0; i<Parameters.numGenes; i++)
		{ 
			for (int j=0; j<Parameters.geneSize; j++)
			{
				double w1=parent1.weights[i][j];
				double w2=parent2.weights[i][j];
				double rand=SearchNN.r.nextDouble();
				if(rand>0.5)					//try to do the uniform xover
				{
					childWeights1[i][j]=w2;
					childWeights2[i][j]=w1;
				}
				else						//do not execute the crossover 
				{
					childWeights1[i][j]=w1;
					childWeights2[i][j]=w2;
				}
			}
		}
		child1.weights=childWeights1;
		child2.weights=childWeights2;

		double[] childBias1 = new double[Parameters.geneSize];
		double[] childBias2 = new double[Parameters.geneSize];
		for (int j=0; j<Parameters.geneSize; j++)
		{
			double b1=parent1.bias[j];
			double b2=parent2.bias[j];
			double rand=SearchNN.r.nextDouble();
			if(rand>0.5)					//try to do the uniform xover
			{
				childBias1[j]=b2;
				childBias2[j]=b1;
			}
			else						//do not execute the crossover 
			{
				childBias1[j]=b1;
				childBias2[j]=b2;
			}
		}
		child1.bias=childBias1;
		child2.bias=childBias2;
		
		//  Set fitness values back to zero
		child1.rawFitness = -1;   //  Fitness not yet evaluated
		child1.sclFitness = -1;   //  Fitness not yet scaled
		child1.proFitness = -1;   //  Fitness not yet proportionalized
		child2.rawFitness = -1;   //  Fitness not yet evaluated
		child2.sclFitness = -1;   //  Fitness not yet scaled
		child2.proFitness = -1;   //  Fitness not yet proportionalized
	}

	//  Produce a new child from a single parent  ******************************

	public static void mateParents(int pnum, Chromosome parent, Chromosome child)
	{

		//  Create child chromosome from parental material
		child.weights = parent.weights;
		child.bias = parent.bias;

		//  Set fitness values back to zero
		child.rawFitness = -1;   //  Fitness not yet evaluated
		child.sclFitness = -1;   //  Fitness not yet scaled
		child.proFitness = -1;   //  Fitness not yet proportionalized
	}

	//  Copy one chromosome to another  ***************************************

	public static void copyB2A (Chromosome targetA, Chromosome sourceB)
	{

		//  Copy targetA chromosome from sourceB material

		targetA.weights = sourceB.weights;
		targetA.bias = sourceB.bias;

		targetA.rawFitness = sourceB.rawFitness;
		targetA.sclFitness = sourceB.sclFitness;
		targetA.proFitness = sourceB.proFitness;
		return;
	}

}   // End of Chromosome.java ******************************************************
