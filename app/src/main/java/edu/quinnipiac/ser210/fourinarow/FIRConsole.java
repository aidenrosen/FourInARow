package edu.quinnipiac.ser210.fourinarow;

import java.util.Scanner;

/**
 * Four in a row: Two-player console, non-graphics
 * @author relkharboutly
 * @date 1/22/2020
 */
public class FIRConsole  {
                                                     
   public static Scanner in = new Scanner(System.in); // the input Scanner
 
   public static FourInARow FIRboard = new FourInARow();
  
   
   /** The entry main method (the program starts here) */
   public static void main(String[] args) {
      
	   int currentState = FourInARow.PLAYING;
	   String userInput ="";
	   //game loop
	   do {
		   FIRboard.printBoard();
		   System.out.println("Your turn!  Pick a column to drop a piece in.");
		   int col = in.nextInt();
		   if(col > 35)
		   {
			   System.out.println("Invalid space.  Ending game");
			   break;
		   }
		   FIRboard.setMove(IGame.BLUE, col);
		   FIRboard.getComputerMove();
		 
		  switch( FIRboard.checkForWinner())
		  {
		  case 1:
			  System.out.println("You win!");
			  currentState = IGame.BLUE_WON;
			  FIRboard.printBoard();
			  break;
		  case 2:
			  System.out.println("You lose");  
			  currentState = IGame.RED_WON;
			  FIRboard.printBoard();
			  break;
		  }
      } while ((currentState == IGame.PLAYING) && (!userInput.equals("q"))); // repeat if not game-over
	   
	   System.out.println("Type q to quit, or r to play again!");
	   if(in.next().equals("r"))
	   {
		   FIRboard.clearBoard();
		   main(args);
	   }
   }
 
     
}