package com.testsmirk.game2048;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.widget.TextView;

public class MainActivity extends Activity {
	private TextView textViewScore;
	private int score = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		textViewScore = (TextView) findViewById(R.id.textview_score);

	}

	private static MainActivity mainActivity = null;

	public static MainActivity getMainActivity() {
		return mainActivity;
	}

	public MainActivity() {
		// TODO Auto-generated constructor stub
		mainActivity = this;
	}

	public void clearScore() {
		score = 0;
		showScore();
	}

	public void showScore() {
		textViewScore.setText(score + "");
	}

	public void addScore(int s) {
		score += s;
		showScore();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
