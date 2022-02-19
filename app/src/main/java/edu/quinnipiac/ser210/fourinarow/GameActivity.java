package edu.quinnipiac.ser210.fourinarow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/*
	@author Aiden Rosen
	Date: 2/21/2022
	Class that contains the game loop, logic for the physical board, as well as saving states when rotating the phone.
 */

public class GameActivity extends AppCompatActivity
{

	private FourInARow board;
	private int currentState = FourInARow.PLAYING;
	private TextView winText;
	private GridLayout game;
	private ArrayList<ImageButton> buttons = new ArrayList<ImageButton>(36);  //Used to save the state of the game, since you can't save a 2D array
	private boolean[] clicked = new boolean[36]; //Used to check if a button has been clicked or not.

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		game = findViewById(R.id.board);

		//Set all of the buttons to not clicked
		for(int i = 0; i < clicked.length; i ++) clicked[i] = false;

		//Get the name from the previous activity
		String name = getIntent().getStringExtra("name");
		winText = (TextView) findViewById(R.id.winText);
		if(name != null) winText.setText("Your turn, " + name);

		for(int i = 0; i < game.getChildCount(); i++)
		{
			if(game.getChildAt(i) != null && ImageButton.class.isInstance(game.getChildAt(i)))
			{
				buttons.add((ImageButton) game.getChildAt(i));
			}
		}


		//Returns save state if there is one.  If not, create a new board
		if(savedInstanceState != null)
		{
			ArrayList<Integer> boardList = savedInstanceState.getIntegerArrayList("board");
			board = new FourInARow(FourInARow.toArray(boardList));
			clicked = savedInstanceState.getBooleanArray("clicked");
			setButton(boardList);
		}
		else board = new FourInARow();

	}

	//Used to redraw the chips onto the buttons after reloading the page/turning the screen
	public void setButton(ArrayList<Integer> board)
	{

		for(int i = 0; i < board.size(); i++)
		{
			for(int j = 0; j < 36; j++)
			{
				if (Integer.parseInt(buttons.get(j).getTag().toString()) == i)
				{
					switch (board.get(i))
					{
						case FourInARow.BLUE:
							buttons.get(j).setBackgroundResource(R.drawable.blue);
							Log.v("DEBUG", "BLUE SET");
							break;
						case FourInARow.RED:
							buttons.get(j).setBackgroundResource(R.drawable.red);
							break;
					}
				}
			}
		}
	}

	//Saves the state of the game
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState)
	{
		savedInstanceState.putIntegerArrayList("board", board.getBoardList());
		savedInstanceState.putBooleanArray("clicked", clicked);
		super.onSaveInstanceState(savedInstanceState);
	}


	//Processes button clicks on the grid.
	public void onClick(View view)
	{
		ImageButton thisButton = (ImageButton) view;
		int button = Integer.parseInt(view.getTag().toString());
		if(!clicked[button])
		{
			if(board.getSpace(button) == FourInARow.EMPTY && currentState == FourInARow.PLAYING)
			{
				thisButton.setBackgroundResource(R.drawable.blue);
				board.setMove(FourInARow.BLUE, button);
				clicked[button] = true;
			}

			if(board.checkForWinner() == FourInARow.BLUE)
			{
				winText.setText("You win!");
				currentState = FourInARow.BLUE_WON;
			} else
			{
				computerMove();
			}

			if(board.checkForTie())
			{
				winText.setText("You tied");
				currentState = FourInARow.TIE;
			}

		}
	}

	//Called when the quit button is pushed.  Does not save the game
	public void onQuit(View view)
	{
		if(currentState != FourInARow.PLAYING) super.finish();
	}


	//Process and draws the computer move
	public void computerMove()
	{
		int move = board.getComputerMove();

		if(currentState == FourInARow.PLAYING)
		{
			for(int i = 0; i < buttons.size(); i++)
			{
				if(Integer.parseInt((String) buttons.get(i).getTag()) == move)
				{
					buttons.get(i).setBackgroundResource(R.drawable.red);
					clicked[move] = true;
				}
			}
		}

		if(board.checkForWinner() == FourInARow.RED)
		{
			winText.setText("You lose.");
			currentState = FourInARow.RED_WON;
		}

		if(board.checkForTie())
		{
			winText.setText("You tied");
			currentState = FourInARow.TIE;
		}
	}

}