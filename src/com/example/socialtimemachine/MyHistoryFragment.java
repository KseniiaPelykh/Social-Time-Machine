package com.example.socialtimemachine;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Ksu on 2/7/2015.
 */
public class MyHistoryFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.tab_item, container, false);
        new HistoryTask().execute(0);
        return rootView;
    }

    public class HistoryTask extends AsyncTask<Integer, Void, Void> {
        ListView listview;
        ProgressDialog mProgressDialog;
        CustomAdapter adapter;
        private String userId;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(getActivity());

            // Set progressdialog title
            mProgressDialog.setTitle("History");
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

            adapter = new CustomAdapter(getActivity(), params[0], userId);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            listview = (ListView)getActivity().findViewById(R.id.game_list);

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


