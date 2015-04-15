/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.TreeMap;

/**
 *
 * @author Chathika
 */
public class StrategyGoldbeck extends Strategy {
    
    TreeMap<String, Integer> lookupTable= new TreeMap<String, Integer>(); //holds the decisions to be made 
    TreeMap<String, Integer> successTable= new TreeMap<String, Integer>(); //holds the decisions to be made 
    String possibleHistories [] = new String[64];//All the possible moves over a history of 3 moves, length =2^6
    String possibleMoves []= {"00","01","10","11"}; //possible moves in a prisoner's dilemma
    int opponentLastLastMove = (int)Math.round(Math.random());
    int myLastLastMove = (int)Math.round(Math.random());
    int opponentLastLastLastMove = (int)Math.round(Math.random());
    int myLastLastLastMove = (int)Math.round(Math.random());
    int iteration = 0;
    String lastHistory = "000000";
    
    public StrategyGoldbeck () {
        name = "Goldbeck Strategy";
        //Randomly initialize strategy
        //Initialize possible moves 
        String possibleHistory = null;
        for(int a = 0; a < 4; a++) {
            for(int b = 0; b < 4; b++) {
                for(int c = 0; c < 4; c++) {
                    possibleHistories[a*16+b*4+c] = possibleMoves[a]+possibleMoves[b]+possibleMoves[c];
                }
            }
        }
//        //Randomly initialize values and add as key value pairs into lookup table
//        for(int i = 0; i < 64; i++) {
//            double r = Math.random();
//            lookupTable.put(possibleHistories[i], r<0.5?0:1);
//        }
        //Optimal Strategy
      //  String optimalStrategy = "0101100001001110011110000100011001011101010110000000111101011101";
      String optimalStrategy = "0100110101011111111011001001110000001101000110001100010110111010";
      //optimalStrategy = "1010010011101101010000000001000010010111100110101010011100000100";
      optimalStrategy = "0001101001001000011001001011101001000101000001000000110000000000";
        setLookUpTable(optimalStrategy);
        opponentLastMove = 0;
        myLastMove = 0;
    }
    public int nextMove() {
        if (iteration < 3){
            opponentLastLastLastMove = opponentLastLastMove;
                myLastLastLastMove = myLastLastMove;
                opponentLastLastMove = opponentLastMove;
                myLastLastMove = myLastMove;
            iteration++;
            return opponentLastMove;
        }
        //check if last move was a success
        if (!(opponentLastMove == 0 && myLastMove == 1)) {
            successTable.put(lastHistory, successTable.get(lastHistory)+1);
        }
        //refer lookup table for apporpriate move oldest move is on the left
        String history = Integer.toString(opponentLastLastLastMove) + Integer.toString(myLastLastLastMove) + Integer.toString(opponentLastLastMove) + Integer.toString(myLastLastMove)
                + Integer.toString(opponentLastMove) + Integer.toString(myLastMove);
        int move = lookupTable.get(history);
        
        //record history
        lastHistory =  history;
        opponentLastLastLastMove = opponentLastLastMove;
        myLastLastLastMove = myLastLastMove;
        opponentLastLastMove = opponentLastMove;
        myLastLastMove = myLastMove;
        return move;
    }
    public void setLookUpTable (String Chromo){
        for(int a = 0; a < 64; a++) {
            lookupTable.put(possibleHistories[a], Integer.valueOf(Chromo.substring(a,a+1)));
            successTable.put(possibleHistories[a], 0);
        }
    }
    public String generateChromo (){
        String chromo = "";
        for(int a = 0; a < 64; a++) {
            chromo.concat(Integer.toString(lookupTable.get(possibleHistories[a])));
        }
        return chromo;
    }
    public TreeMap<String, Integer> getSuccessTable () {
        return successTable;
    }
}
