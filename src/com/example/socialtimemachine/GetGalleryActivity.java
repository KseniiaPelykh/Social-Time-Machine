package com.example.socialtimemachine;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;
import com.example.socialtimemachine.adapter.GameGalleryAdapter;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class GetGalleryActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        new GalleryTask().execute();
    }

    public class GalleryTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog mProgressDialog;
        GameGalleryAdapter gameGalleryAdapter;
        List<ParseObject> listDataHeader;
        HashMap<ParseObject, List<ParseObject>> listDataChild;
        ExpandableListView expandableListView;

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
        protected Void doInBackground(Void... params) {
            prepareListData();
            gameGalleryAdapter = new GameGalleryAdapter(
                    GetGalleryActivity.this,
                    listDataHeader,
                    listDataChild);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            expandableListView = (ExpandableListView) GetGalleryActivity.this.findViewById(R.id.gallery_list);
            expandableListView.setAdapter(gameGalleryAdapter);
            expandableListView.setFooterDividersEnabled(true);
            mProgressDialog.dismiss();
        }

        private void prepareListData() {
            listDataHeader = new ArrayList<ParseObject>();
            listDataChild = new HashMap<ParseObject, List<ParseObject>>();
            int location = 0;
            List<ParseObject> games = Collections.EMPTY_LIST;
            List<ParseObject> moves = Collections.EMPTY_LIST;

            Calendar cal = Calendar.getInstance();
            Date date = cal.getTime();

             try {
                games = new ParseQuery("Game")
                        .whereLessThanOrEqualTo("endFinal", date)
                        .find();

            } catch (Exception e) {
                Log.i("Get Games", e.toString());
            }

            for (ParseObject game : games) {
                listDataHeader.add(game);

                try {
                    moves = new ParseQuery("Part")
                            .whereEqualTo("gameParent", game)
                            .find();
                } catch (Exception e) {
                    Log.i("Get Moves", e.toString());
                }

                listDataChild.put(listDataHeader.get(location), moves);
                location++;
            };
        }
    }
}
