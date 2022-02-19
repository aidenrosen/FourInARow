package edu.quinnipiac.ser210.fourinarow;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * TicTacToe class implements the interface
 * @author relkharboutly
 * @date 2/12/2022
 */
public class FourInARow implements IGame
{
		 
	   // The game board and the game status
	   private static final int ROWS = 6, COLS = 6; // number of rows and columns
	   private int[][] board = new int[ROWS][COLS];// game board in 2D array
	   private ArrayList<Integer> boardList = new ArrayList<Integer>(); //game board in an ArrayList, used to save state
	  

	public FourInARow(){
		
	}

	//Used to recreate an already existing board
	public FourInARow(int[][] board)
	{
		this.board = board;
	}


	//Convets the 2D array of the board to an ArrayList.  Used for save states
	public ArrayList<Integer> getBoardList()
	{
		for(int i =0; i < ROWS; i++)
		{
			for(int j = 0; j < COLS; j++)
			{
				boardList.add(board[i][j]);
			}
		}
		return boardList;
	}

	//Converts the ArrayList back into a 2D array
	public static int[][] toArray(ArrayList<Integer> boardList)
	{
		int[][] tempBoard = new int[ROWS][COLS];
		for(int i = 0; i < ROWS; i++)
		{
			for(int j = 0; j < COLS; j++)
			{
				tempBoard[i][j] = boardList.get(toCoords(i,j));
			}
		}
		return tempBoard;
	}
	@Override
	public void clearBoard() {
		for(int i = 0; i < ROWS; i++)
		{
			for(int j = 0; j < COLS; j++)
			{
				board[i][j] = EMPTY;
			}
		}
	}

	//Set a move from the player or computer on the actual grid
	@Override
	public void setMove(int player, int location) 
	{
		int col = location%6;
		int row = location / 6;
		if(board[row][col] == EMPTY) board[row][col] = player;
	}

	//Returns the value of a given space
	public int getSpace(int space)
	{
		int col = space %6;
		int row = space / 6;
		return board[row][col];
	}

	//Returns true if the given point is inbounds
	public boolean isInBounds(int row, int col)
	{
		return row < ROWS && col < COLS && row > 0 && col > 0;
	}

	//Converts an (x,y) coordinate into a single number.
	public static int toCoords(int row, int col)
	{
		int move = row * 6;
		move = move + col;
		return move;
	}

	//AI Logic for determining computer moves.
	@Override
	public int getComputerMove() 
	{
		int move = (int) (Math.random() * 35);
		
		//Blocking Logic
		
		//Checking vertically
		for(int row = 0; row <ROWS - 2; row++)
		{
			for(int col = 0; col <COLS; col++)
			{
				if((board[row][col] == BLUE
						&& board[row+1][col] ==BLUE 
						&& board[row+2][col] == BLUE))
						{
							if(isInBounds(row+3, col) && board[row+3][col] == EMPTY) move = toCoords(row+3, col);
							else if(isInBounds(row-1, col) && board[row-1][col] == EMPTY) move= toCoords(row - 1, col);
						}
			}
		}
		
		
		for(int row2 = 0; row2 < ROWS; row2++)
		{
			for(int col = 0; col < COLS - 2; col++)
			{
				if((board[row2][col] == BLUE
					&& board[row2][col+1] == BLUE
					&& board[row2][col+2] == BLUE))
				{
					if(isInBounds(row2, col + 3) && board[row2][col + 3] == EMPTY) move = toCoords(row2, col + 3);
					else if(isInBounds(row2, col - 1) && board[row2][col - 1] == EMPTY) move= toCoords(row2, col - 1);
				}
			}
		}
		
		//Check positive slopes
		for(int row3 = 3; row3 < ROWS ; row3++)
		{
			for(int col = 0; col < COLS - 2; col++)
			{
				if((board[row3][col] == BLUE
						&& board[row3-1][col + 1] == BLUE
						&& board[row3-2][col + 2] == BLUE))
				{
					if(isInBounds(row3-3, col + 3) && board[row3 - 3][col + 3] == EMPTY) move = toCoords(row3 - 3, col + 3);

					else if(isInBounds(row3 +1, col - 1) && board[row3 + 1][col - 1] == EMPTY) move= toCoords(row3 + 1, col - 1);
				}
			}
		}
		
		//Check negative slopes
		for(int row4 = 2; row4 < ROWS ; row4++)
		{
			for(int col = 2; col < COLS; col++)
			{
				if((board[row4][col] == BLUE
						&& board[row4-1][col - 1] == BLUE
						&& board[row4-2][col - 2] == BLUE))
				{
					if(isInBounds(row4 - 3, col - 3) && board[row4 - 3][col - 3] == EMPTY) move = toCoords(row4 - 3, col - 3);
					else if(isInBounds(row4 + 1, col + 1) && board[row4 + 1][col + 1] == EMPTY) move= toCoords(row4 + 1, col + 1);
				}
			}
		}

		if(getSpace(move) == EMPTY) setMove(RED, move);
		else return getComputerMove();
		return move;
	}

	//Returns true if there is a tie
	public boolean checkForTie()
	{
		for(int i = 0; i < 36; i++)
		{
			if (getSpace(i) == EMPTY) return false;
		}
		return true;
	}

	//Returns the winner
	@Override
	public int checkForWinner() {
		//Vertical check
		for(int row = 0; row < ROWS - 3; row++)
		{	
			for(int col = 0; col < COLS; col++)
			{
				if((board[row][col] == BLUE
					&& board[row+1][col] ==BLUE 
					&& board[row+2][col] == BLUE
					&& board[row+3][col] == BLUE) || 
					(board[row][col] == RED
					&& board[row+1][col] == RED 
					&& board[row+2][col] == RED
					&& board[row+3][col] == RED))
				{
					return board[row][col];
				}	
			}
			
			//Check horizontally
			
			for(int row2 = 0; row2 < ROWS; row2++)
			{
				for(int col = 0; col < COLS - 3; col++)
				{
					if((board[row2][col] == BLUE
						&& board[row2][col+1] == BLUE
						&& board[row2][col+2] == BLUE
						&& board[row2][col+3] == BLUE) ||
						(board[row2][col] == RED
						&& board[row2][col+1] == RED
						&& board[row2][col+2] == RED
						&& board[row2][col+3] == RED))
					{
						return board[row2][col];
					}
				}
			}
			
			//Check positive slopes
			for(int row3 = 3; row3 < ROWS ; row3++)
			{
				for(int col = 0; col < COLS - 3; col++)
				{
					if((board[row3][col] == BLUE
							&& board[row3-1][col + 1] == BLUE
							&& board[row3-2][col + 2] == BLUE
							&& board[row3-3][col + 3] == BLUE ||
							board[row3][col] == RED
							&& board[row3-1][col + 1] == RED
							&& board[row3-2][col + 2] == RED
							&& board[row3-3][col + 3] == RED))
					{
						return board[row3][col];
					}
				}
			}
			
			//Check negative slopes
			for(int row4 = 3; row4 < ROWS ; row4++)
			{
				for(int col = 3; col < COLS; col++)
				{
					if((board[row4][col] == BLUE
							&& board[row4-1][col - 1] == BLUE
							&& board[row4-2][col - 2] == BLUE
							&& board[row4-3][col - 3] == BLUE ||
							board[row4][col] == RED
							&& board[row4-1][col - 1] == RED
							&& board[row4-2][col - 2] == RED
							&& board[row4-3][col - 3] == RED))
					{
						return board[row4][col];
					}
				}
			}
		}
		
		return 0;
	}
	
	  /**
	   *  Print the game board 
	   */
	   public  void printBoard() {

	      for (int row = 0; row < ROWS; ++row) {
	         for (int col = 0; col < COLS; ++col) {
	            printCell(board[row][col]); // print each of the cells
	            if (col != COLS - 1) {
	               System.out.print("|");   // print vertical partition
	            }
	         }
	         System.out.println();
	         if (row != ROWS - 1) {
	            System.out.println("---------------------"); // print horizontal partition
	         }
	      }
	      System.out.println();
	      System.out.println("END");
	   }
	 
	   /**
	    * Print a cell with the specified "content" 
	    * @param content either BLUE, RED or EMPTY
	    */
	   public  void printCell(int content) {
	      switch (content) {
	         case EMPTY:  System.out.print("   "); break;
	         case BLUE: System.out.print(" B "); break;
	         case RED:  System.out.print(" R "); break;
	      }
	   }

}
