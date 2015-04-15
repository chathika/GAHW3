
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.TreeMap;

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
    
    
    public GoldbeckIPDFitnessFunction() {
        name = "IPD Goldbeck";
    }
    public void doRawFitness(Chromo X, Chromo pop[]){
        

        //setup strategy
        StrategyGoldbeck myStrategy = new StrategyGoldbeck();
        myStrategy.setLookUpTable(X.chromo);
        
        //Run IPD fot this strategy with all other strategies
        int rawFitness = 0;
        //System.setOut(dummyStream);
        for(int i = 0; i < Parameters.popSize; i++ ){
            StrategyGoldbeck opponentStrategy = new StrategyGoldbeck();
            opponentStrategy.setLookUpTable(pop[i].chromo);
            IteratedPD ipd = new IteratedPD(myStrategy, opponentStrategy);

            ipd.runSteps(150);         
            
            //rawFitness += ipd.player1Score();
           // System.out.printf(" Player 1 score = %d\n", ipd.player1Score());
           // System.out.printf(" Player 2 score = %d\n", ipd.player2Score());
        }
        TreeMap<String,Integer> successTable = myStrategy.getSuccessTable();
        Integer successes[] = successTable.values().toArray(new Integer[0]);
        for (int i = 0; i < Parameters.geneSize * Parameters.numGenes; i++ )
        {
            rawFitness += successes[i];
        }
//        StrategyTitForTat opponentStrategy1 = new StrategyTitForTat();
//        IteratedPD ipd = new IteratedPD(myStrategy, opponentStrategy1);
//        ipd.runSteps(150);
//        rawFitness += ipd.player1Score();
//        StrategyTitForTwoTats opponentStrategy2 = new StrategyTitForTwoTats();
//        ipd = new IteratedPD(myStrategy, opponentStrategy2);
//        ipd.runSteps(150);
//        rawFitness += ipd.player1Score();
//        StrategyAlwaysCooperate opponentStrategy3 = new StrategyAlwaysCooperate();
//        ipd = new IteratedPD(myStrategy, opponentStrategy3);
//        ipd.runSteps(150);
//        rawFitness += ipd.player1Score();
//        StrategyAlwaysDefect opponentStrategy4 = new StrategyAlwaysDefect();
//        ipd = new IteratedPD(myStrategy, opponentStrategy4);
//        ipd.runSteps(150);
//        rawFitness += ipd.player1Score();
        //Final fitness is average score
        //int finalFitness = rawFitness/(Parameters.popSize);
        
        X.rawFitness = rawFitness;
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
