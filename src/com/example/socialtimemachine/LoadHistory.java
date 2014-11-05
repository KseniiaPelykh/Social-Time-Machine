package com.example.socialtimemachine;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class LoadHistory extends Activity {
	
	List<ParseObject> ob;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		Parse.initialize(this, "CblPQNXB5bztS0zjzox1vPPb8mRCiOorvNQMD3Jb", "fcqLiSWLa2JVHMW0esKZP3ewkAJm0jYPEjhlYVmg");	
		setContentView(R.layout.activity_history);
		ListView gamesTitle = (ListView)findViewById(R.id.listViewAnimals);	
				
		CustomAdapter mainAdapter = new CustomAdapter(this); 
		
		gamesTitle.setAdapter(mainAdapter);
		gamesTitle.setOnItemClickListener(new OnItemClickListener()
        {
			// argument position gives the index of item which is clicked
            public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3)
            {
            }
        });		
	}	

}

