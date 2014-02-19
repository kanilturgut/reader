package com.tepav.reader.activities;

import android.app.*;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.tepav.reader.R;
import com.tepav.reader.adapters.*;
import com.tepav.reader.delegates.EmailListRegisterServiceDelegate;
import com.tepav.reader.services.EmailListRegisterService;
import com.tepav.reader.services.ReadingListService;
import com.tepav.reader.utils.Util;

/**
 * Created with IntelliJ IDEA.
 * User: Kadir Anil Turgut
 * Date: 02.02.2014
 * Time: 23:03
 */
public class MainScreenActivity extends Activity implements EmailListRegisterServiceDelegate{

    public static DrawerLayout mDrawerLayout = null;
    private ActionBarDrawerToggle mDrawerToggle = null;
    public static RelativeLayout myRelativeDrawerLayout = null;

    private ListView mDrawerList = null;
    private ListView listOfMainContent = null;
    public int position = 0;
    private String[] TITLES;

    private static final int HABER = 0;
    private static final int GUNLUK = 1;
    private static final int ARASTIRMA_VE_YAYINLAR = 2;
    private static final int RAPORLAR = 3;
    private static final int NOTLAR = 4;
    private static final int BASILI_YAYINLAR = 5;
    private static final int OKUMA_LISTESI = 6;
    private static final int FAVORILER = 7;
    private static final int MAIL_LISTESI = 8;

    private EmailListRegisterService emailListRegisterService = null;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        TITLES = getResources().getStringArray(R.array.menus);

        getActionBar().setTitle(TITLES[0]);
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#352354")));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        myRelativeDrawerLayout = (RelativeLayout) findViewById(R.id.relativeDrawerLayout);

        mDrawerList = (ListView) findViewById(R.id.listViewDrawer);
        mDrawerList.setAdapter(new NavigationDrawerAdapter(this, TITLES));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!Util.isNetworkAvailable(MainScreenActivity.this)) {
                    if (i == OKUMA_LISTESI || i == FAVORILER) {
                        selectItem(i);
                    } else {
                        createAlertDialog(getResources().getString(R.string.no_internet));
                    }
                } else {
                    selectItem(i);
                }
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
            if (!Util.isNetworkAvailable(this)) {
                createAlertDialog(getResources().getString(R.string.no_internet));
                selectItem(OKUMA_LISTESI);
            } else {
                selectItem(HABER);
            }
        }

        emailListRegisterService = new EmailListRegisterService(this);
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

        if (position != MAIL_LISTESI) {
            //Update main content
            Fragment fragment = new MainContentFragment(position);
            Bundle args = new Bundle();
            args.putInt(MainContentFragment.ARG_MAIN_CONTENT_NUMBER, position);
            fragment.setArguments(args);

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            mDrawerList.setItemChecked(position, true);
            getActionBar().setTitle(TITLES[position]);

        } else {
            final Dialog dialog = new Dialog(MainScreenActivity.this);
            dialog.setContentView(R.layout.custom_newsletter_dialog);
            dialog.setTitle(getResources().getString(R.string.mail_list));
            dialog.setCancelable(false);

            Button buttonDialogYes = (Button) dialog.findViewById(R.id.buttonDialogYes);
            Button buttonDialogNo = (Button) dialog.findViewById(R.id.buttonDialogNo);

            final EditText editTextEmail = (EditText) dialog.findViewById(R.id.editTextUserEMail);

            buttonDialogYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String email = editTextEmail.getText().toString().trim();

                    if(editTextEmail != null || !editTextEmail.equals("")) {
                        if (email.contains("@")) {
                            emailListRegisterService.registerForEmailList(email);
                            dialog.dismiss();
                        } else {
                            editTextEmail.setText("");
                            createAlertDialog(getResources().getString(R.string.email_wrong));
                        }

                    } else {
                        createAlertDialog(getResources().getString(R.string.email_wrong));
                    }
                }
            });

            buttonDialogNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }

        mDrawerLayout.closeDrawer(myRelativeDrawerLayout);
    }

    @Override
    public void registerForEmailListRequestDidFinish(int status) {
        if (status == EmailListRegisterService.STATUS_OK) {
            createAlertDialog(getResources().getString(R.string.email_status_ok));
        } else if (status == EmailListRegisterService.STATUS_FAIL) {
            createAlertDialog(getResources().getString(R.string.email_status_fail));
        } else if (status == EmailListRegisterService.STATUS_EXISTS) {
            createAlertDialog(getResources().getString(R.string.email_status_exist));
        }
    }

    public class MainContentFragment extends Fragment {
        public static final String ARG_MAIN_CONTENT_NUMBER = "main_content_number";

        //empty constructor required for fragment subclasses
        public MainContentFragment() {
        }

        public MainContentFragment(int position) {
            MainScreenActivity.this.position = position;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_main_content, container, false);
            listOfMainContent = (ListView) rootView.findViewById(R.id.lvMainContent);

            switch (position) {
                case HABER:
                    listOfMainContent.setAdapter(new HaberListAdapter(MainScreenActivity.this));
                    break;
                case GUNLUK:
                    listOfMainContent.setAdapter(new GunlukListAdapter(MainScreenActivity.this));
                    break;
                case ARASTIRMA_VE_YAYINLAR:
                    listOfMainContent.setAdapter(new YayinListAdapter(MainScreenActivity.this));
                    break;
                case RAPORLAR:
                    listOfMainContent.setAdapter(new RaporListAdapter(MainScreenActivity.this));
                    break;
                case NOTLAR:
                    listOfMainContent.setAdapter(new NotlarListAdapter(MainScreenActivity.this));
                    break;
                case BASILI_YAYINLAR:
                    listOfMainContent.setAdapter(new BasiliYayinListAdapter(MainScreenActivity.this));
                    break;
                case OKUMA_LISTESI:
                    listOfMainContent.setAdapter(new ReadingListAdapter(MainScreenActivity.this, ReadingListService.PERSISTANCE_TYPE_READ_LIST));
                    break;
                case FAVORILER:
                    listOfMainContent.setAdapter(new ReadingListAdapter(MainScreenActivity.this, ReadingListService.PERSISTANCE_TYPE_FAVORITES));
                    break;
            }

            setTitle(TITLES[getArguments().getInt(ARG_MAIN_CONTENT_NUMBER)]);
            return rootView;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.exit_question))
                .setTitle(getResources().getString(R.string.exit_title))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        System.exit(0);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("MainActivity", "onRestart");
        if (MainScreenActivity.this.position == 6 || MainScreenActivity.this.position == 7) {
            selectItem(position);
        }
    }

    private void createAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}