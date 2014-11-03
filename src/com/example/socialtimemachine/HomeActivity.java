package com.example.socialtimemachine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.facebook.Session;
import com.parse.Parse;
import com.parse.ParseObject;

public class HomeActivity extends FragmentActivity {	
	
	private SelectionFragment selectionFragment;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		Parse.initialize(this, "CblPQNXB5bztS0zjzox1vPPb8mRCiOorvNQMD3Jb", "fcqLiSWLa2JVHMW0esKZP3ewkAJm0jYPEjhlYVmg");
		
		/*final ActionBar actionBar = getActionBar();		
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);*/
		
		ParseObject testObject = new ParseObject("TestObject");
		testObject.put("foo", "bar");
		testObject.saveInBackground();
		/*ActionBar.TabListener tabListener = new ActionBar.TabListener(){			
			public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft){				
			}
			
			public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft){
				
			}
			
			public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft){
				
			}			
		};
		
		actionBar.addTab(actionBar.newTab().setText("OLD").setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("CURRENT").setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("CREATE NEW").setTabListener(tabListener)); */
				
		setContentView(R.layout.activity_home);
		if (savedInstanceState == null) {
			// Add the fragment on initial activity setup
			
			selectionFragment = new SelectionFragment();
			getSupportFragmentManager()
			.beginTransaction()
			.add(R.id.selectionContainer, selectionFragment)
			.commit();
		}
		else {
			// Or set the fragment from restored state info
			selectionFragment = (SelectionFragment) getSupportFragmentManager()
					.findFragmentById(R.id.selectionContainer);
		}		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		//Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		//Handle presses on the action bar items
		switch (item.getItemId()){
			case R.id.logout:
				Session session = Session.getActiveSession();
				if (session != null){
					if (!session.isClosed()){
						session.closeAndClearTokenInformation();
						// clear preferences if saved
					}
				} 
				
				Intent intent = new Intent(this, MainActivity.class);
				startActivity(intent);
				
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	public void newGame(View view){
		Intent intent = new Intent(this, NewGameActivity.class);
		startActivity(intent);
	}	
}
