/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.math.*;

/**
 *
 * @author Chathika
 */
public class StrategyHW3 extends Strategy {
    StringBuilder decisionReference; //holds the decisions to be made 
    public StrategyHW3 () {
        //Randomly initialize strategy
        for(int i = 0; i < 4; i++) {
            double r = Math.random();
            if ( r < 0.5) {
                decisionReference.setCharAt(i, '0');
            } else {
                decisionReference.setCharAt(i, '1');
            }
        }
    }
    
}
