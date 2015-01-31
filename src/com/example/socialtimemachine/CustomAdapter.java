package com.example.socialtimemachine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.util.Date;

public class CustomAdapter extends ParseQueryAdapter {

	public CustomAdapter(Context context) {
      	 // Use the QueryFactory to construct a PQA that will only show
		 super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
		   public ParseQuery create() {
		     ParseQuery query = new ParseQuery("Part");
               query.include("gameParent");
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

       // Add the title of the game
       TextView titleTextView = (TextView) v.findViewById(R.id.game_title);
       titleTextView.setText(object.getParseObject("gameParent").getString("gameTitle"));

       TextView descriptionTextView = (TextView) v.findViewById(R.id.game_description);
       descriptionTextView.setText(object.getString("description"));

       // Add the user profile picture
       String userId = object.getString("user");
       if (userId != null) {
           StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                   .permitAll().build();
           StrictMode.setThreadPolicy(policy);

           Bitmap bitmap = getProfilePicture(userId);
           if (bitmap != null) {
               ImageView userProfilePicture = (ImageView) v.findViewById(R.id.game_user_profile_picture);
               userProfilePicture.setImageBitmap(bitmap);
           }

           //String userName = getUserName(userId);
           /*if (userName != null) {
               TextView userNameView = (TextView) v.findViewById(R.id.userName);
               userNameView.setText(userName);
           }*/
       }

	  // Add and download the image
	  ParseImageView image = (ParseImageView) v.findViewById(R.id.image);
	  ParseFile imageFile = object.getParseFile("image");
	  if (imageFile != null) {
	    image.setParseFile(imageFile);
	    image.loadInBackground();
	  }
	 
	  return v;
	}

    private Bitmap getProfilePicture(String userId) {
        Bitmap profilePicture = null;
        final String urlString = "https://graph.facebook.com/" + userId + "/picture?type=normal";
        URL imageURL = null;

        try {
            imageURL = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.i("GetUserProfilePicture:", e.toString());
        }

        try {
            HttpURLConnection connection = (HttpURLConnection) imageURL.openConnection();
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects( true );
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            profilePicture = BitmapFactory.decodeStream(inputStream);

        } catch (IOException e) {
            Log.i("GetUserProfilePicture:", e.toString());
        }

        return profilePicture;
    }

    private String getUserName(String userId){
        String userName = null;
        final String urlString = "https://graph.facebook.com/" + userId + "?fields=name";
        URL userNameUrl = null;

        try {
            userNameUrl = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.i("GetUserName:", e.toString());
        }

        try {
            HttpURLConnection connection = (HttpURLConnection) userNameUrl.openConnection();
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects( true );
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            userName = inputStream.toString();

        } catch (IOException e) {
            Log.i("GetUserName:", e.toString());
        }

        return userName;
    }
}
