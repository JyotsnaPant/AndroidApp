package com.example.searchApplication;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TabbedActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    public String urlTwitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);



        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));

        Bundle b = getIntent().getExtras();
        String Array = b.getString("Response");
        try {
            JSONObject newArray = (JSONObject) new JSONObject(Array);

            JSONObject embedded = (JSONObject) new JSONObject(Array).get("_embedded");
            JSONArray attractions = (JSONArray) embedded.get("attractions");
            String name = attractions.getJSONObject(0).get("name").toString();
//            artistName.setText(name);
//            artistName = jsonArrayDetails.getJSONObject(0).get("name").toString();
            JSONArray venue = (JSONArray) embedded.get("venues");
            String vname = venue.getJSONObject(0).get("name").toString();
            String url="";
            if(newArray.has("url"))
            {
                url = (String) new JSONObject(Array).get("url");

            }

            urlTwitter = "https://twitter.com/intent/tweet?text=Check out " + name + "located at " + vname + " Website: " + url + "&hashtags=CSCI571EventSearch";
//            https://twitter.com/intent/tweet?text=Check out {{ details.name }} located at {{ details._embedded.venues[0].name }}. Website: {{ details.url }}&hashtags=CSCI571EventSearch


        } catch (Exception e) {
            e.printStackTrace();
        }
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

    }



    public void OnFragmentInteractionListenerArtist()
    {

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tabbed, menu);
        MenuItem item= menu.findItem(R.id.action_settings);
        item.setVisible(false);
//        MenuItem item2 = menu.findItem(R.id.action_custom_button);
//        item2.getActionView().setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(urlTwitter)));
//                //https://twitter.com/intent/tweet?text=Check out {{ details.name }} located at {{ details._embedded.venues[0].name }}. Website: {{ details.url }}&hashtags=CSCI571EventSearch
//                //mMenu.performIdentifierAction(item2.getItemId(), 0);
//            }
//        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_custom_button) {
            startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(urlTwitter)));
        }
        if(item.getItemId() == android.R.id.home)
        {
            finish();
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

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tabbed, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
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

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below)
            switch (position) {
                case 0:
                    EventDetailsFragment detailsFragment = new EventDetailsFragment();
                    return detailsFragment;
                case 1:
                    ArtistFragment artistFragment = new ArtistFragment();
                    return artistFragment;
                case 2:
                    VenueFragment venueFragment = new VenueFragment();
                    return venueFragment;
                case 3:
                    UpcomingEventsFragment UEFragment = new UpcomingEventsFragment();
                    return UEFragment;
                default:
                    return null;
            }
//            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }
    }

}
