package com.example.socialtimemachine;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.PushService;
import com.parse.SaveCallback;

public class MainActivity extends FragmentActivity {

	private MainFragment mainFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        PushService.setDefaultPushCallback(this, MainActivity.class);

        setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			// Add the fragment on initial activity setup
			mainFragment = new MainFragment();
			getSupportFragmentManager()
			.beginTransaction()
			.add(R.id.container, mainFragment)
			.commit();
		}
		else {
			// Or set the fragment from restored state info
			mainFragment = (MainFragment) getSupportFragmentManager()
					.findFragmentById(R.id.container);
		}
	}
}
