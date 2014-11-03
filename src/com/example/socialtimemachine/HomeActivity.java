package com.example.socialtimemachine;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class HomeActivity extends FragmentActivity {	
	
	ListView listView;
	List<ParseObject> ob;
	ProgressDialog mProgressDialog;
	ArrayAdapter<String> adapter;
	
	private SelectionFragment selectionFragment;
	private String userId;
		
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setUserId();
		
		Parse.initialize(this, "CblPQNXB5bztS0zjzox1vPPb8mRCiOorvNQMD3Jb", "fcqLiSWLa2JVHMW0esKZP3ewkAJm0jYPEjhlYVmg");
				
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
		
		//new RemoteDataTask().execute();
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
	
	private void setUserId(){
		Session session = Session.getActiveSession();		
		
		if (session != null){
			Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {				
				
				@Override 
				public void onCompleted(GraphUser user, Response response){
					if (user != null){
						userId = user.getId();						
					}
				}
			});
			Request.executeBatchAsync(request);
		}		
	}

	private class RemoteDataTask extends AsyncTask<Void, Void, Void>{
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			
			mProgressDialog = new ProgressDialog(HomeActivity.this);
			
			mProgressDialog.setTitle("History");
			mProgressDialog.setMessage("Loading...");
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.show();
		}
		
		@Override
		protected Void doInBackground(Void... params){
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Game");
			query.whereEqualTo("gameUser", userId);
			try {
				ob = query.find();
			}
			catch (ParseException e){
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override 
		protected void onPostExecute(Void result){
			/*listView = (ListView)findViewById(R.id.historyview);
			adapter = new ArrayAdapter<String>(HomeActivity.this,
					R.layout.listview_item);
			
			for (ParseObject item : ob){
				adapter.add((String) item.get("gameTitle"));
			}
			
			listView.setAdapter(adapter);
			mProgressDialog.dismiss();*/
		}
	}
}
