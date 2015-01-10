package com.example.socialtimemachine;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.socialtimemachine.view.SlidingTabLayout;

/**
 * A basic sample which shows how to use SlidingTabLayout
 * to display a custom ViewPager title strip which gives continuous feedback
 * to the user when scrolling
 */

public class SlidingTabsBasicFragment extends Fragment {

    static final String LOG_TAG = "SlidingTabsBasicFragment";

    /**
     * A custom ViewPager title strip which looks much like Tabs present in Android v4.0 and
     * above, but is designed to give continuous feedback to the user when scrolling
     */
    private SlidingTabLayout mSlidingTabLayout;

    /**
     * A ViewPager which will be used in conjunction with the link SlidingTabLayout above
     */
    private ViewPager mViewPager;

    /**
     * Inflates the View which will be displayed by this Fragment
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState){
        return  inflater.inflate(R.layout.tab_basic_fragment, container, false);
    }

    /**
     * This is called after the onCreateView has finished.
     * Here we can pick out the View we need to configure from the content view.
     *
     * We set the ViewPager's adapter to be an instance of SamplePagerAdapter. The
     * SlidingTabLayout} is then given the ViewPager so that it can populate itself.
     *
     * View created in onCreateView(LayoutInflater, ViewGroup, Bundle)
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SamplePagerAdapter());

        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    /**
     * The android.support.v4.view.PagerAdapter used to display pages in this sample.
     * The individual pages are simple and just display two lines of text. The important section of
     * this class is the getPageTitle method which controls what is displayed in the
     * SlidingTabLayout.
     */
    class SamplePagerAdapter extends PagerAdapter {
        /**
         * @return the number of pages to display
         */
        @Override
        public int getCount() {
            return 3;
        }

        /**
         * @return true if the value returned from {@link #instantiateItem(ViewGroup, int)} is the
         * same object as the {@link View} added to the {@link ViewPager}.
         */
        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        /**
         * Return the title of the item at {@code position}. This is important as what this method
         * returns is what is displayed in the {@link SlidingTabLayout}.
         * <p>
         * Here we construct one using the position value, but for real application the title should
         * refer to the item's contents.
         */
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

        /**
         * Instantiate the {@link View} which should be displayed at {@code position}. Here we
         * inflate a layout from the apps resources and then change the text view to signify the position.
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // Inflate a new layout from our resources
            View view = getActivity().getLayoutInflater().inflate(R.layout.tab_item,
                    container, false);
            // Add the newly created View to the ViewPager
            container.addView(view);

            // Retrieve a TextView from the inflated View, and update it's text
            TextView title = (TextView) view.findViewById(R.id.item_title);
            title.setText(String.valueOf(position + 1));

            Log.i(LOG_TAG, "instantiateItem() [position: " + position + "]");

            // Return the View
            return view;
        }

        /**
         * Destroy the item from the {@link ViewPager}. In our case this is simply removing the
         * {@link View}.
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            Log.i(LOG_TAG, "destroyItem() [position: " + position + "]");
        }
    }
}
