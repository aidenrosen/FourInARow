package edu.quinnipiac.ser210.fourinarow;

/*
	@author Aiden Rosen
	Date: 2/21/2022
	Initial splash screen activity.
 */
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity
{
	private EditText playerName;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	//Method that runs once the user presses the start button
	public void startGame(View view)
	{
		playerName = (EditText) findViewById(R.id.name);
		Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra("name", playerName.getText().toString());

		startActivity(intent);

	}

}