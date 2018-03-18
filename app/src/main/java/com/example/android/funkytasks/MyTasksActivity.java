/**
 * MyTasksActivity
 *
 * Version 1.0.0
 *
 * Create by Funky Tasks on March 8th
 *
 * Copyright information: https://github.com/CMPUT301W18T20/FunkyTasks/wiki/Reuse-Statement
 */

package com.example.android.funkytasks;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

/**
 * This activity displays all of one user's tasks
 */
public class MyTasksActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tasks);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        username = LoginActivity.username;

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


    }


    /**
     * Creates the options menu.
     *
     * @param menu a menu object speficying the menu to be instantiated
     * @return returns true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_tasks, menu);
        return true;
    }

    /**
     * States what happens when an item in the menu is selected
     *
     * @param item a menu item specifying which item was selected
     * @return returns a boolean stating whether or not it was successful
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        /**
         * States what happens when the view is created and also how the view should be created
         *
         * @param inflater a layout inflater that helps the view display
         * @param container a view group object that dictates where the view is displayed
         * @param savedInstanceState a bundle representing the state of the app when it was
         *                           last open
         * @return returns the view
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_my_tasks, container, false);
            TextView textView = rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * A fragment that tells the app which page the user wants to view
         *
         * @param position an integer stating the position of the view
         * @return returns the tab that should be displayed or null
         */
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    MyRequestedTasksFragment tab1 = new MyRequestedTasksFragment();
                    return tab1;
                case 1:
                    ToSolveTasksFragment tab2 = new ToSolveTasksFragment();
                    return tab2;
                default:
                    return null;
            }
        }

        /**
         * Returns the count
         * @return returns 2
         */
        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        /**
         * Returns the page title
         *
         * @param position an integer that states which tab of the app that is currently in view
         * @return returns the page title or null
         */
        @Override
        public CharSequence getPageTitle(int position){
            switch (position){
                case 0:
                    return "Posted";
                case 1:
                    return "To Solve";
            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainMenuActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }


}
