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

import com.example.socialtimemachine.view.SlidingTabLayout;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.Parse;
import com.parse.ParseInstallation;

public class HomeActivity extends FragmentActivity {

    private SelectionFragment selectionFragment;
    private String userId;
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUserId();

        setContentView(R.layout.activity_home);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        // mViewPager = (ViewPager) findViewById(R.id.viewpager);
        // mViewPager.setAdapter(new TabPagerAdapter(this.getSupportFragmentManager()));

        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        //mSlidingTabLayout=(SlidingTabLayout)findViewById(R.id.sliding_tabs);
       // findViewById(R.id.sliding_tabs);
       // mSlidingTabLayout.setViewPager(mViewPager);

        if(savedInstanceState==null)
        {
            // Add fragments to activity setup
            /*selectionFragment = new SelectionFragment();
            getSupportFragmentManager()
            .beginTransaction()
            .add(R.id.selection_container, selectionFragment)
            .commit();*/

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
            // Or set the fragment from restored state info
            //selectionFragment = (SelectionFragment) getSupportFragmentManager()
                 //.findFragmentById(R.id.selection_container);

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

    public class TabPagerAdapter extends FragmentStatePagerAdapter{
        public TabPagerAdapter(FragmentManager fragmentManager)
        {
            super(fragmentManager);
        }

       @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.i("v", "Position" + position);
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new MyHistoryFragment();
                case 1:
                    return new FriendsHistoryFragment();
                default:
                    return new HistoryFragment();
            }
         }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "My History";
                case 1:
                    return "Friend's History";
                case 2:
                    return "All History";
                default:
                    return "No Title";
            }
        }
    }
}
