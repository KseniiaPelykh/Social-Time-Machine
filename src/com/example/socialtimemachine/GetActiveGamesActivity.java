package com.example.socialtimemachine;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.socialtimemachine.adapter.ActiveGamesAdapter;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

import java.util.List;

public class GetActiveGamesActivity extends Activity {

    private String userId;
    ListView listview;
    ProgressDialog mProgressDialog;
    List<ParseObject> ob;
    ActiveGamesAdapter adapter;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            userId = extras.getString("UserId");
        }

        setContentView(R.layout.activity_get_active_games);
        new LoadActiveGames().execute();
    }

    private class LoadActiveGames extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(GetActiveGamesActivity.this);

            // Set progressdialog title
            mProgressDialog.setTitle("Active Games");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);

            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            adapter = new ActiveGamesAdapter(GetActiveGamesActivity.this, userId);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            listview = (ListView) findViewById(R.id.active_games_list);

            // Binds the Adapter to the ListView
            listview.setAdapter(adapter);

            // Capture button clicks on ListView items
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // Send single item click data to SingleItemView Class
                }
            });

            // Close the progressdialog
            mProgressDialog.dismiss();
        }
    }
}
