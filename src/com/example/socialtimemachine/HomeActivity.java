package com.example.socialtimemachine;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.parse.Parse;
import com.parse.ParseObject;

public class HomeActivity extends Activity {	
	protected void onCreate(Bundle savedInstanceState){
		Parse.initialize(this, "CblPQNXB5bztS0zjzox1vPPb8mRCiOorvNQMD3Jb", "fcqLiSWLa2JVHMW0esKZP3ewkAJm0jYPEjhlYVmg");
		
		final ActionBar actionBar = getActionBar();		
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		ParseObject testObject = new ParseObject("TestObject");
		testObject.put("foo", "bar");
		testObject.saveInBackground();
		ActionBar.TabListener tabListener = new ActionBar.TabListener(){			
			public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft){				
			}
			
			public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft){
				
			}
			
			public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft){
				
			}			
		};
		
		actionBar.addTab(actionBar.newTab().setText("OLD").setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("CURRENT").setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("CREATE NEW").setTabListener(tabListener));
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
	}
	
	public void newGame(View view){
		Intent intent = new Intent(this, NewGameActivity.class);
		startActivity(intent);
	}
	
}
