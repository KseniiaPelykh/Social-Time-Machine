package com.example.socialtimemachine;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.socialtimemachine.adapter.ActiveGamesAdapter;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.android.Util;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.util.Date;

public class CustomAdapter extends ParseQueryAdapter {

    public static final int GET_MY_GAMES = 0;
    public static final int GET_ALL_GAMES = 2;
    public static final int GET_FRIENDS_GAMES = 1;

	public CustomAdapter(Context context, final int type, final String userId) {
      	 // Use the QueryFactory to construct a PQA that will only show
		 super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
		   public ParseQuery create() {

               Log.i("Type", "type" + type);
               Log.i("Custom Adapter userID", userId);

               ParseQuery query = null;
               if (type == GET_ALL_GAMES) {
                   query = new ParseQuery("Part");
               }
               else if (type == GET_MY_GAMES){
                   query = new ParseQuery("Part")
                    .whereEqualTo("user", userId);
               } else {
                    query = new ParseQuery("Part")
                        .whereNotEqualTo("user", userId);
               }

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
          ImageView userProfilePicture = (ImageView) v.findViewById(R.id.game_user_profile_picture);
          BitmapWorkerTask task = new BitmapWorkerTask(userProfilePicture);
          task.execute(userId);

           TextView userNameView = (TextView) v.findViewById(R.id.userName);
           setUserName(userId, userNameView);
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

    private class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
        private  final WeakReference<ImageView> imageViewReference;
        ProgressDialog mProgressDialog;
        Bitmap bitmap;

        public BitmapWorkerTask(ImageView imageView){
            imageViewReference = new WeakReference<ImageView>(imageView);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String userId = params[0];
            return bitmap = getProfilePicture(userId);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
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
    }

    private void setUserName(String userId, final TextView textView){
        Session session = Session.getActiveSession();
         new Request(session, "/" + userId, null , HttpMethod.GET,
                new Request.Callback() {
                    public void onCompleted(Response response) {
                        GraphObject responseGraphObject = response
                                .getGraphObject();
                        JSONObject json = responseGraphObject
                                .getInnerJSONObject();
                        try {
                            String userName = json.getString("name");
                            if (userName != null) {
                                textView.setText(userName);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).executeAsync();
    }
}
