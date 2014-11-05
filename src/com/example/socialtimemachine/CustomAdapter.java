package com.example.socialtimemachine;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class CustomAdapter extends ParseQueryAdapter {
	
	public CustomAdapter(Context context) {
		 // Use the QueryFactory to construct a PQA that will only show	
		 super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
		   public ParseQuery create() {
		     ParseQuery query = new ParseQuery("Game");
		     return query;
		   }
		 });
	}
	
	@Override
	public View getItemView(ParseObject object, View v, ViewGroup parent) {
	  if (v == null) {
	    v = View.inflate(getContext(), R.layout.listview_item, null);
	  }
	 
	  super.getItemView(object, v, parent);
	 
	  // Add and download the image
	  ParseImageView todoImage = (ParseImageView) v.findViewById(R.id.icon);
	  ParseFile imageFile = object.getParseFile("gameImage");
	  if (imageFile != null) {
	    todoImage.setParseFile(imageFile);
	    todoImage.loadInBackground();
	  }
	 
	  // Add the title view
	  TextView titleTextView = (TextView) v.findViewById(R.id.gameTitle);
	  titleTextView.setText(object.getString("gameTitle"));
	 
	  return v;
	}
}
