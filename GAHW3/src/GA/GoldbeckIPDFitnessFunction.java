
import java.io.FileWriter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Chathika
 */
public class GoldbeckIPDFitnessFunction extends FitnessFunction{
    private Chromo population[];
    
    public GoldbeckIPDFitnessFunction(Chromo population[]) {
        this.population = population; 
    }
    public void doRawFitness(Chromo X){
        name = "IPD Goldbeck";

        //setup strategy
        StrategyGoldbeck myStrategy = new StrategyGoldbeck();
        myStrategy.setLookUpTable(X.chromo);
        //Run IPD fot this strategy with all other strategies
        int rawFitness = 0;
        for(int i = 0; i < Parameters.popSize; i++ ){
            StrategyGoldbeck opponentStrategy = new StrategyGoldbeck();
            opponentStrategy.setLookUpTable(X.chromo);
            IteratedPD ipd = new IteratedPD(myStrategy, opponentStrategy);

            ipd.runSteps(150);
            rawFitness += ipd.player1Score();
           // System.out.printf(" Player 1 score = %d\n", ipd.player1Score());
           // System.out.printf(" Player 2 score = %d\n", ipd.player2Score());
        }
        //Final fitness is average score
        int finalFitness = rawFitness/Parameters.popSize;
        
        X.rawFitness = finalFitness;
    }
    public void doPrintGenes(Chromo X, FileWriter output) throws java.io.IOException{

		for (int i=0; i<Parameters.numGenes; i++){
			Hwrite.right(X.getGeneAlpha(i),11,output);
		}
		output.write("   RawFitness");
		output.write("\n        ");
		for (int i=0; i<Parameters.numGenes; i++){
			Hwrite.right(X.getIntGeneValue(i),11,output);
		}
		Hwrite.right((int) X.rawFitness,13,output);
		output.write("\n\n");
		return;
	}
}
