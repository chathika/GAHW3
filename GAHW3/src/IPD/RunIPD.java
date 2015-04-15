/**
 * General class containing main program to run the
 * iterated Prisoner's Dilemma (IPD).
 * @author	081028AW
 */
public class RunIPD extends Object
   {
  /**
   * Main program to start IPD program.
   */

   public static void main(String args[])
      {
     
      int i;
      int maxSteps = 150;

      Strategy player1, player2;
      IteratedPD ipd;

      for (i=0; i<args.length; i++)
         {
        /* check parameters */
         if (args[i].equals("-l") || args[i].equals("-L"))
            {
            maxSteps = Integer.parseInt(args[i+1]);
            System.out.println(" Max steps = " + maxSteps);
            }  /* if */
         }  /* for i */
/////////////////////////
      player1 = new StrategyAlwaysCooperate();
      player2 = new StrategyWanwanLi();
      ipd = new IteratedPD(player1, player2);

      ipd.runSteps(maxSteps);

      System.out.printf(" Player 1 score = %d\n", ipd.player1Score());
      System.out.printf(" Player 2 score = %d\n", ipd.player2Score());

      player1 = new StrategyAlwaysCooperate();
      player2 = new StrategyGoldbeck();
      ipd = new IteratedPD(player1, player2);

      ipd.runSteps(maxSteps);

      System.out.printf(" Player 1 score = %d\n", ipd.player1Score());
      System.out.printf(" Player 2 score = %d\n", ipd.player2Score());
//////////////////////////// 
      player1 = new StrategyAlwaysDefect();
      player2 = new StrategyWanwanLi();
      ipd = new IteratedPD(player1, player2);

      ipd.runSteps(maxSteps);

      System.out.printf(" Player 1 score = %d\n", ipd.player1Score());
      System.out.printf(" Player 2 score = %d\n", ipd.player2Score());

      player1 = new StrategyAlwaysDefect();
      player2 = new StrategyGoldbeck();
      ipd = new IteratedPD(player1, player2);

      ipd.runSteps(maxSteps);

      System.out.printf(" Player 1 score = %d\n", ipd.player1Score());
      System.out.printf(" Player 2 score = %d\n", ipd.player2Score());
////////////////////////////
      player1 = new StrategyTitForTat();
      player2 = new StrategyWanwanLi();
      ipd = new IteratedPD(player1, player2);

      ipd.runSteps(maxSteps);

      System.out.printf(" Player 1 score = %d\n", ipd.player1Score());
      System.out.printf(" Player 2 score = %d\n", ipd.player2Score());

      player1 = new StrategyTitForTat();
      player2 = new StrategyGoldbeck();
      ipd = new IteratedPD(player1, player2);

      ipd.runSteps(maxSteps);

      System.out.printf(" Player 1 score = %d\n", ipd.player1Score());
      System.out.printf(" Player 2 score = %d\n", ipd.player2Score());
///////////////////////////
      player1 = new StrategyTitForTwoTats();
      player2 = new StrategyWanwanLi();
      ipd = new IteratedPD(player1, player2);

      ipd.runSteps(maxSteps);

      System.out.printf(" Player 1 score = %d\n", ipd.player1Score());
      System.out.printf(" Player 2 score = %d\n", ipd.player2Score());

      player1 = new StrategyTitForTwoTats();
      player2 = new StrategyGoldbeck();
      ipd = new IteratedPD(player1, player2);

      ipd.runSteps(maxSteps);

      System.out.printf(" Player 1 score = %d\n", ipd.player1Score());
      System.out.printf(" Player 2 score = %d\n", ipd.player2Score());
///////////////////////////
      player1 = new StrategyRandom();
      player2 = new StrategyWanwanLi();
      ipd = new IteratedPD(player1, player2);

      ipd.runSteps(maxSteps);

      System.out.printf(" Player 1 score = %d\n", ipd.player1Score());
      System.out.printf(" Player 2 score = %d\n", ipd.player2Score());

      player1 = new StrategyRandom();
      player2 = new StrategyGoldbeck();
      ipd = new IteratedPD(player1, player2);

      ipd.runSteps(maxSteps);

      System.out.printf(" Player 1 score = %d\n", ipd.player1Score());
      System.out.printf(" Player 2 score = %d\n", ipd.player2Score());
///////////////////////////
      player1 = new StrategyGoldbeck();
      player2 = new StrategyWanwanLi();
      ipd = new IteratedPD(player1, player2);

      ipd.runSteps(maxSteps);

      System.out.printf(" Player 1 score = %d\n", ipd.player1Score());
      System.out.printf(" Player 2 score = %d\n", ipd.player2Score());
      }  /* main */
   }  /* class RunIPD */

