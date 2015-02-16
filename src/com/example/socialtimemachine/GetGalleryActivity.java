package com.example.socialtimemachine;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.socialtimemachine.adapter.GalleryAdapter;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

public class GetGalleryActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        new GalleryTask().execute();
    }

    public class GalleryTask extends AsyncTask<Integer, Void, Void> {
        GridView gridview;
        ProgressDialog mProgressDialog;
        GalleryAdapter adapter;
        private String userId;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(GetGalleryActivity.this);

            // Set progressdialog title
            mProgressDialog.setTitle("Gallery");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);

            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Integer... params) {
            Session session = Session.getActiveSession();
            if (session != null) {
                Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        if (user != null) {
                            userId = user.getId();
                        }
                    }
                });

                Request.executeAndWait(request);
            }

            adapter = new GalleryAdapter(GetGalleryActivity.this, userId);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            gridview = (GridView)GetGalleryActivity.this.findViewById(R.id.gallery_grid_view);

            // Binds the Adapter to the ListView
            gridview.setAdapter(adapter);

            // Capture button clicks on ListView items
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
