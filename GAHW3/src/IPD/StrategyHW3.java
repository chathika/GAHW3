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
public class StrategyHW3 extends Strategy {
    StringBuilder decisionReference = new StringBuilder(64); //holds the decisions to be made 
    TreeMap<String, Integer> lookupTable= new TreeMap<String, Integer>();
    String possibleHistories [] = new String[64];// 
    String possibleMoves []= {"00","01","10","11"}; //possible moves in a prisoner's dilemma
    int opponentLastLastMove = (int)Math.round(Math.random());
    int myLastLastMove = (int)Math.round(Math.random());
    int opponentLastLastLastMove = (int)Math.round(Math.random());
    int myLastLastLastMove = (int)Math.round(Math.random());
    
    public StrategyHW3 () {
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
        decisionReference.setLength(64);
        for(int i = 0; i < 64; i++) {
            double r = Math.random();
            if ( r < 0.5) {
                decisionReference.setCharAt(i, '0');
                //lookupTable
            } else {
                decisionReference.setCharAt(i, '1');
            }
        }
    }
    public int nextMove() {
        int move=0;
        
        opponentLastLastLastMove = opponentLastLastMove;
        myLastLastLastMove = myLastLastMove;
        opponentLastLastMove = opponentLastMove;
        myLastLastMove = myLastMove;
        return move;
    }
    
}
