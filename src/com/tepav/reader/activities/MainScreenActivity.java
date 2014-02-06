package com.tepav.reader.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.tepav.reader.R;
import com.tepav.reader.adapters.NavigationDrawerAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: Kadir Anil Turgut
 * Date: 02.02.2014
 * Time: 23:03
 */
public class MainScreenActivity extends Activity {

    private DrawerLayout mDrawerLayout = null;
    private ActionBarDrawerToggle mDrawerToggle = null;
    private RelativeLayout myRelativeDrawerLayout = null;

    private ListView mDrawerList = null;
    private ListView listOfMainContent = null;

    String[] TITLES = {"Haberler", "Günlük", "Araştırma ve Yayınlar", "Raporlar", "Notlar", "Basılı Yayınlar", "Okuma Listem", "Favoriler", "Giriş Yap"};

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        getActionBar().setTitle("Haberler");
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#352354")));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        myRelativeDrawerLayout = (RelativeLayout) findViewById(R.id.relativeDrawerLayout);

        mDrawerList = (ListView) findViewById(R.id.listViewDrawer);
        mDrawerList.setAdapter(new NavigationDrawerAdapter(this, TITLES));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectItem(i);
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            @Override
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_screen, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void selectItem(int position) {

        //Update main content

        Fragment fragment = new MainContentFragment();
        Bundle args = new Bundle();
        args.putInt(MainContentFragment.ARG_MAIN_CONTENT_NUMBER, position);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        mDrawerList.setItemChecked(position, true);
        getActionBar().setTitle(TITLES[position]);
        mDrawerLayout.closeDrawer(myRelativeDrawerLayout);
    }

    public class MainContentFragment extends Fragment {
        public static final String ARG_MAIN_CONTENT_NUMBER = "main_content_number";

        //empty constructor required for fragment subclasses
        public MainContentFragment(){}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_main_content, container, false);

            listOfMainContent = (ListView) rootView.findViewById(R.id.lvMainContent);
            listOfMainContent.setAdapter(new ArrayAdapter<String>(MainScreenActivity.this, android.R.layout.simple_list_item_1, TITLES));

            setTitle(TITLES[getArguments().getInt(ARG_MAIN_CONTENT_NUMBER)]);

            return rootView;
        }
    }
}