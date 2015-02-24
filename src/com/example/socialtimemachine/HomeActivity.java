package com.example.socialtimemachine;

import android.content.Intent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.socialtimemachine.view.SlidingTabLayout;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.WebDialog;
import com.parse.ParseInstallation;

public class HomeActivity extends FragmentActivity {

    private String userId;
    private SlidingTabLayout mSlidingTabLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUserId();
        setContentView(R.layout.activity_home);
        mSlidingTabLayout=(SlidingTabLayout)findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.populateTabStrip();

        if(savedInstanceState==null)
        {
            HistoryFragment history = new HistoryFragment();
            getSupportFragmentManager()
            .beginTransaction()
            .add(R.id.history_container, history)
            .commit();

             View addButton = this.findViewById(R.id.add_new_game_button);
             addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    newGame(view);
                }
              });
        }
        else
        {
            HistoryFragment history = (HistoryFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.history_container);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Handle presses on the action bar items
        switch (item.getItemId()) {
           case R.id.logout:
                Session session = Session.getActiveSession();
                if (session != null) {
                    if (!session.isClosed()) {
                        session.closeAndClearTokenInformation();
                        // clear preferences if saved
                    }
                }

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.get_active_games:
                Intent getActiveGames = new Intent(this, GetActiveGamesActivity.class);
                getActiveGames.putExtra("UserId", userId);
                startActivity(getActiveGames);
                return true;

            case R.id.get_gallery:
                Intent getGallery = new Intent(this, GetGalleryActivity.class);
                getGallery.putExtra("UserId", userId);
                startActivity(getGallery);
                return  true;

            case R.id.invite_friends:
                sendRequestDialog();
                return  true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void newGame(View view) {
        Intent intent = new Intent(this, NewGameActivity.class);
        startActivity(intent);
    }

    private void setUserId() {
        Session session = Session.getActiveSession();
          if (session != null) {
            Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {

                @Override
                public void onCompleted(GraphUser user, Response response) {
                    if (user != null) {
                        userId = user.getId();
                        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                        installation.put("userId", userId);
                        installation.saveInBackground();
                    }
                }
            });
            Request.executeBatchAsync(request);
        }
    }

    private void sendRequestDialog() {
        Bundle params = new Bundle();
        params.putString("message", "Invite you to try the STM");

        WebDialog requestsDialog = (
                new WebDialog.RequestsDialogBuilder(HomeActivity.this,
                        Session.getActiveSession(),
                        params))
                .setOnCompleteListener(new WebDialog.OnCompleteListener() {

                    @Override
                    public void onComplete(Bundle values,
                                           FacebookException error) {
                        if (error != null) {
                            if (error instanceof FacebookOperationCanceledException) {
                                Toast.makeText(HomeActivity.this.getApplicationContext(),
                                        "Request cancelled",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(HomeActivity.this.getApplicationContext(),
                                        "Network Error",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            final String requestId = values.getString("request");
                            if (requestId != null) {
                                Toast.makeText(HomeActivity.this.getApplicationContext(),
                                        "Request sent",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(HomeActivity.this.getApplicationContext(),
                                        "Request cancelled",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                })
                .build();
        requestsDialog.show();
    }
}
