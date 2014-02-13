package com.tepav.reader.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.tepav.reader.R;
import com.tepav.reader.adapters.*;
import com.tepav.reader.delegates.HaberServiceDelegate;
import com.tepav.reader.models.Haber;
import com.tepav.reader.services.BaseRequestService;
import com.tepav.reader.services.HaberService;
import com.tepav.reader.services.ReadingListService;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Kadir Anil Turgut
 * Date: 02.02.2014
 * Time: 23:03
 */
public class MainScreenActivity extends Activity{

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

        if (mDrawerToggle.onOptionsItemSelected(item)) {
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
        Fragment fragment = new MainContentFragment(position);
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
        private int position;

        //empty constructor required for fragment subclasses
        public MainContentFragment() {
        }

        public MainContentFragment(int position) {
            this.position = position;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_main_content, container, false);
            listOfMainContent = (ListView) rootView.findViewById(R.id.lvMainContent);

            switch (position) {
                case 0:
                    listOfMainContent.setAdapter(new HaberListAdapter(MainScreenActivity.this));
                    break;
                case 1:
                    listOfMainContent.setAdapter(new GunlukListAdapter(MainScreenActivity.this));
                    break;
                case 2:
                    listOfMainContent.setAdapter(new YayinListAdapter(MainScreenActivity.this));
                    break;
                case 3:
                    listOfMainContent.setAdapter(new RaporListAdapter(MainScreenActivity.this));
                    break;
                case 4:
                    listOfMainContent.setAdapter(new NotlarListAdapter(MainScreenActivity.this));
                    break;
                case 5:
                    listOfMainContent.setAdapter(new BasiliYayinListAdapter(MainScreenActivity.this));
                    break;
                case 6:
                    listOfMainContent.setAdapter(new ReadingListAdapter(MainScreenActivity.this, ReadingListService.PERSISTANCE_TYPE_READ_LIST));
                    break;
                case 7:
                    listOfMainContent.setAdapter(new ReadingListAdapter(MainScreenActivity.this, ReadingListService.PERSISTANCE_TYPE_FAVORITES));
                    break;
                case 8:
                    listOfMainContent.setAdapter(new HaberListAdapter(MainScreenActivity.this));
                    break;

            }


            setTitle(TITLES[getArguments().getInt(ARG_MAIN_CONTENT_NUMBER)]);

            return rootView;
        }
    }
}