package com.example.socialtimemachine;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class HistoryFragment extends Fragment {

    ListView listview;
    ProgressDialog mProgressDialog;
    List<ParseObject> ob;
    CustomAdapter adapter;

    @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState){

       ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.tab_item, container, false);
        new RemoteDataTask().execute();
        return rootView;
   }

    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
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
        protected Void doInBackground(Void... params) {
            adapter = new CustomAdapter(getActivity());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            listview = (ListView)getActivity().findViewById(R.id.game_list);

            // Binds the Adapter to the ListView
            listview.setAdapter(adapter);

            // Close the progressdialog
            mProgressDialog.dismiss();

            // Capture button clicks on ListView items
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // Send single item click data to SingleItemView Class
                }
            });
        }
    }

}
