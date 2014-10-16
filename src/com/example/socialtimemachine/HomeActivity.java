package com.example.socialtimemachine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends Activity {
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
	}
	
	public void newGame(View view){
		Intent intent = new Intent(this, NewGameActivity.class);
		startActivity(intent);
	}
	
}
