package com.example.socialtimemachine;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;
import com.example.socialtimemachine.adapter.GameGalleryAdapter;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.ArrayList;
import java.util.Collections;
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
        List<String> listDataHeader;
        HashMap<String, List<String>> listDataChild;
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
            mProgressDialog.dismiss();
        }

        private void prepareListData() {
            listDataHeader = new ArrayList<String>();
            listDataChild = new HashMap<String, List<String>>();
            int location = 0;
            List<ParseObject> games = Collections.EMPTY_LIST;
            List<ParseObject> moves = Collections.EMPTY_LIST;
            List<String> currentGame = new ArrayList<String>();

             try {
                games = new ParseQuery("Game").find();
            } catch (Exception e) {
                Log.i("Get Games", e.toString());
            }

            for (ParseObject game : games) {
                listDataHeader.add(game.getObjectId());
                Log.i("GameId", game.getObjectId());

                try {
                    moves = new ParseQuery("Part")
                            .whereEqualTo("gameParent", game)
                            .find();
                } catch (Exception e) {
                    Log.i("Get Moves", e.toString());
                }

                currentGame = new ArrayList<String>();
                for (ParseObject move : moves) {
                     currentGame.add(move.getObjectId());
                     Log.i("Get Move", move.getObjectId());
                }

                listDataChild.put(listDataHeader.get(location), currentGame);
                location++;
            };
        }
    }
}
