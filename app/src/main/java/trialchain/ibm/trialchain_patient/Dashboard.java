package trialchain.ibm.trialchain_patient;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import trialchain.ibm.trialchain_patient.adapters.RecordListAdapter;
import trialchain.ibm.trialchain_patient.database.DBHelper;
import trialchain.ibm.trialchain_patient.messaging.IntentAction;
import trialchain.ibm.trialchain_patient.messaging.MessageBroadcastRecevier;
import trialchain.ibm.trialchain_patient.messaging.MessageListenService;

public class Dashboard extends AppCompatActivity implements RecordListFragment.OnFragmentInteractionListener {
    IntentFilter intentFilter;

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

    private MessageBroadcastRecevier myRecevier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Log.d("DASHBOARD","Activity create hui");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //Initialise the broadcast recevier that listens for broadcasts from MessageListenService.
        //As a message is recevied, the onReceive method is called in the broadcast recevier
        myRecevier = new MessageBroadcastRecevier();

        intentFilter = new IntentFilter(IntentAction.GET_MESSAGES);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);



    }

    @Override
    public void onStart()
    {
        super.onStart();
        Log.d("Dashboard.ONSTART","Now starting service");
        registerReceiver(myRecevier,intentFilter);

        //Initiate the background service that will listen for messages
        Intent intent = new Intent(this, MessageListenService.class);
        //startService(intent);

        Log.d("Dashboard.ONSTART","Now mocking server");


    }

    @Override
    public void onStop()
    {
        super.onStop();
        //if(myRecevier.)
        unregisterReceiver(myRecevier);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

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

    @Override
    public void onFragmentInteraction(ListView recordList, RecordListAdapter adapter, List<Record> records)
    {
      RecordListAdapter recordListAdapter = (RecordListAdapter) recordList.getAdapter();

        List<Record> newList = new DBHelper(this).getAllKeyPairs();

        records = newList;
        recordListAdapter.notifyDataSetChanged();

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
            View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
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
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch(position)
            {
                case 0:
                    return RecordListFragment.newInstance("Arg1","Arg2");
                case 1:
                    return MessagesFragment.newInstance();

            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Record List";
                case 1:
                    return "Messages";
                default:
                    return null;
            }

        }

    }
}
