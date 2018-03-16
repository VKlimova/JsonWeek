package com.amargodigits.weekdaycalendar;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Scroller;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.net.URL;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private SQLiteDatabase mDb;
    public static String[] dayName;         // Day names array
    public static String[] daySchedule;     // Day schedules array
//    static TextView dayScheduleTextView;
    EditText textEdit;
    static String mJSONstr="";
    boolean isEdit; //True if now in Edit mode
    FloatingActionButton myFab;
    static int dayOnScreenID;
    static Toolbar toolbar;
    MenuItem saveMenuItem;
    private final static String LOG_TAG = MainActivity.class.getSimpleName();

    private static SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    static private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Resources res=getResources();

        myFab = (FloatingActionButton)findViewById(R.id.fab);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

   //     dayScheduleTextView = (TextView) findViewById(R.id.dayScheduleText);
        textEdit = (EditText) findViewById(R.id.textEdit);


//        dayScheduleTextView.setMovementMethod(new ScrollingMovementMethod());
        daySchedule = res.getStringArray(R.array.daySchedule);
        dayName = res.getStringArray(R.array.dayName);
        Log.i(LOG_TAG, "onCreate curWeekdayNum()="+curWeekdayNum());
        dayOnScreenID=curWeekdayNum()-1;
        if (dayOnScreenID<0) {dayOnScreenID=6;}
        // showDaySchedule(dayOnScreenID);
        toolbar.setTitle(dayName[dayOnScreenID]);

        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar , R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

//        getActionBar().setTitle("Your title");

        // Create the adapter that will return a fragment for each of the
        // seven sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Create a DB helper (this will create the DB if run for the first time)
        WDScheduleDbHelper dbHelper = new WDScheduleDbHelper(this);

        // Keep a reference to the mDb until paused or killed. Get a writable database
        // because you will be adding restaurant customers
        mDb = dbHelper.getWritableDatabase();

        // Get all guest info from the database and save in a cursor
        Cursor cursor = getWeekSchedule();

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
             // Change the header to weekday name
                toolbar.setTitle(dayName[position]);
                dayOnScreenID=position;
                Log.i(LOG_TAG, "onPageSelected dayOnScreenID=" + dayOnScreenID );
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });




        myFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   // Edit - reset button clicks
                String todaySchedue;


                if (isEdit)  //
                {
                 //   todaySchedue=daySchedule[mViewPager.getCurrentItem()];
//                    textEdit.setText(dayScheduleTextView.getText());
                   // textEdit.setText(todaySchedue);
                    setEdit(false);

                 //   mViewPager.setVisibility(View.VISIBLE);
                }
                else        // open text in edit field
                {
                    // based on the current position you can then cast the page to the correct
                    // class and call the method:

                        View view = (TextView) findViewById(R.id.section_label);
                    Log.i(LOG_TAG, view.toString());
                    mViewPager.setVisibility(View.GONE);
                    Log.i(LOG_TAG, " mViewPager.setVisibility(View.GONE);");

                    String text2edit;
                    text2edit = (String) daySchedule[mViewPager.getCurrentItem()];

                    textEdit.setText(text2edit);
                    textEdit.setSelection(textEdit.getText().length());
                    setEdit(true);
                }
            }
        });

        mViewPager.setCurrentItem(dayOnScreenID);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        Drawable drawable = menu.findItem(R.id.left).getIcon();
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(Color.MAGENTA, PorterDuff.Mode.SRC_ATOP);
        }

        drawable = menu.findItem(R.id.right).getIcon();
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
        }

        saveMenuItem = menu.findItem(R.id.save);

        return true;

    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        dayScheduleTextView.scrollTo(0,0);
        mViewPager.findViewById(R.id.section_label).scrollTo(0,0);
        switch (item.getItemId()) {
            case R.id.left:
                if (dayOnScreenID > 0) {
                    dayOnScreenID = dayOnScreenID - 1;
                } else {
                    dayOnScreenID = 6;
                }
                mViewPager.setCurrentItem(dayOnScreenID);
                break;

            case R.id.right:
                if (dayOnScreenID < 6) {
                    dayOnScreenID = dayOnScreenID + 1;
                } else {
                    dayOnScreenID = 0;
                }
                mViewPager.setCurrentItem(dayOnScreenID);
                break;

            case R.id.save:
            {
                Toast.makeText(this, "Saving...", Toast.LENGTH_SHORT).show();
            }

            break;
        }
        setEdit(false);

//        showDaySchedule(dayOnScreenID);
        return true;
    }

public void setEdit(boolean ifEdit) { // Set Edit mode to TRUE or FALSE
    if (ifEdit) {  // Going to Edit Mode
        isEdit=true;
       // dayScheduleTextView.setVisibility(View.GONE);
        mViewPager.setVisibility(View.GONE);
        textEdit.setVisibility(View.VISIBLE);
        myFab.setImageResource(android.R.drawable.ic_menu_revert);
        saveMenuItem.setVisible(true);

    } else {    // Going to View Mode
        isEdit=false;
        // dayScheduleTextView.setVisibility(View.VISIBLE);
        mViewPager.setVisibility(View.VISIBLE);
        textEdit.setVisibility(View.INVISIBLE);
        myFab.setImageResource(android.R.drawable.ic_menu_edit);
        saveMenuItem.setVisible(false);
    }

}

    public static void showDaySchedule(int dayID){
//        dayScheduleTextView.setText(Html.fromHtml("<font size=\"40\" face=\"arial\" color=\"red\">"+dayName[dayID]+"</font><br/>"));
        Log.i(LOG_TAG, "dayID="+dayID);
        toolbar.setTitle(dayName[dayID]);
       mViewPager.setCurrentItem(dayID);
        // Log.i("WD", dayID + " " + dayName[dayID] + " " + " " + toolbar.getTitle() + daySchedule[dayID]);
    //    dayScheduleTextView.setText(daySchedule[dayID]);
    }

    public int curWeekdayNum(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int i;
        if (day > 0) {
            i = day - 1;
        } else {
            i = 7;
        }

        Log.i(LOG_TAG, "curWeekdayNum day=" + i);

        return i;
    }

    public void onBackPressed(){

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (isEdit) {
                    setEdit(false);
                }
                else{
                    super.onBackPressed();
                }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        android.app.FragmentManager manager = getFragmentManager();
        switch (id) {
            case R.id.nav_help:
                HelpDialogFragment helpDialogFragment = new HelpDialogFragment();
                helpDialogFragment.show(manager, "dialog");
                break;

            case R.id.nav_shar:
                Share(dayName[dayOnScreenID], dayName[dayOnScreenID] + "\n\n" + daySchedule[dayOnScreenID]);
                break;

            case R.id.nav_edit:
                myFab.performClick();
                break; //nav_edit

            case R.id.nav_import:
                ImportDialogFragment importDialogFragment = new ImportDialogFragment();
                importDialogFragment.show(manager, "dialog");

                break; //nav_import
            case R.id.nav_reset:
                Resources res=getResources();
                daySchedule = res.getStringArray(R.array.daySchedule);
                dayName = res.getStringArray(R.array.dayName);
                mViewPager.setAdapter(mSectionsPagerAdapter);
                Log.i(LOG_TAG, "RESET dayOnScreenID="+dayOnScreenID);

//                dayOnScreenID=curWeekdayNum()-1;
//                if (dayOnScreenID<0) {dayOnScreenID=6;}

                showDaySchedule(dayOnScreenID);
                break;
        }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    //    H E L P      d i a l o g
    public static class HelpDialogFragment extends DialogFragment {
        //  @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            String title = getString(R.string.help);
            String button1String = "Ok";
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.help_fragment, null);
            builder.setView(view);
            //           builder.setTitle(title);  // заголовок
            builder.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            builder.setCancelable(true);
            //           builder.setIcon(R.drawable.chu_splin);
            return builder.create();
        }
    }

//       I M P O R T     d i a l o g
    public static class ImportDialogFragment extends DialogFragment {
        //  @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            String title = getString(R.string.impor);
            String button1String = "Ok";
            String button2String = "Cancel";

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.import_fragment, null);
            builder.setView(view);
            //           builder.setTitle(title);  // заголовок
            builder.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Dialog f = (Dialog) dialog;
                    EditText JSONtxt;
                    JSONtxt = (EditText) f.findViewById(R.id.import_str);
                    mJSONstr=JSONtxt.getText().toString();
                    Log.i(LOG_TAG, "mJSONstr="+mJSONstr);

                    class FetchWeatherTask extends AsyncTask<String, Void, String[]> {

                        @Override
                        protected void onPostExecute(String[] result) {
                            super.onPostExecute(result);
                            mViewPager.setAdapter(mSectionsPagerAdapter);
                            showDaySchedule(dayOnScreenID);
                        }

                    @Override
                    protected String[] doInBackground(String... params) {

                        if (params.length == 0) {
                            return null;
                        }
                        try {
                            Log.i(LOG_TAG, "in import");
                            URL scheduleRequestUrl = NetworkUtils.buildUrl(mJSONstr);
                            String jsonScheduleResponse = NetworkUtils
                                    .getResponseFromHttpUrl(scheduleRequestUrl);

                                    JSONutils.getSimpleStringsFromJson(jsonScheduleResponse);

                            Log.i(LOG_TAG, "showDaySchedule(dayOnScreenID)");
                        } catch (Exception e) {
                            Log.i(LOG_TAG, "exception");
                            e.printStackTrace();
                        }

                    return null;}}
                new FetchWeatherTask().execute("");
                    //mViewPager.setAdapter(mSectionsPagerAdapter);
                }
            });
            builder.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            builder.setCancelable(true);
            //           builder.setIcon(R.drawable.chu_splin);

            return builder.create();
        }
    }

    public void Share(String text1, String text2){

        // S H A R E intent

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
//        shareIntent.putExtra(Intent.EXTRA_STREAM, imgUri);

        shareIntent.putExtra(Intent.EXTRA_SUBJECT, text1);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text2);

        shareIntent.setType("image/jpeg");
        Intent chooser=Intent.createChooser(shareIntent, getString(R.string.shareTxt));
        startActivity(chooser);
    };

/////////////////    TABs
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
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            rootView.setTag(getArguments().getInt(ARG_SECTION_NUMBER)-1);
            rootView.setId(getArguments().getInt(ARG_SECTION_NUMBER)-1);

            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(
//                    getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER))+
                    dayName[getArguments().getInt(ARG_SECTION_NUMBER)-1]+
                    daySchedule[getArguments().getInt(ARG_SECTION_NUMBER)-1]);

            Log.v(LOG_TAG, "onCreateView rootView getTag=" + rootView.getTag() +  dayName[getArguments().getInt(ARG_SECTION_NUMBER)-1]);
         //   Log.v(LOG_TAG, "onCreateView rootView getId=" + rootView.getId() +  dayName[getArguments().getInt(ARG_SECTION_NUMBER)-1]);

            textView.setMovementMethod(new ScrollingMovementMethod());
            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 7 total pages.
            return 7;
        }

//        @Override
//        public CharSequence getPageTitle(int position) {
//            switch (position) {
//                case 0:
//                    return "SECTION 1";
//                case 1:
//                    return "SECTION 2";
//                case 2:
//                    return "SECTION 3";
//            }
//            return null;
//        }
    }


    /**
     * Query the mDb and get all guests from the waitlist table
     *
     * @return Cursor containing the list of guests
     */
    private Cursor getWeekSchedule() {
        return mDb.query(
                WDScheduleContract.ScheduleEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                WDScheduleContract.ScheduleEntry.COLUMN_DAYID
        );
    }


    public long insertSchedule(String daySchedule, int dayId) {
        ContentValues cv = new ContentValues();
        cv.put(WDScheduleContract.ScheduleEntry.COLUMN_DAYSCHEDULE, daySchedule);
        cv.put(WDScheduleContract.ScheduleEntry.COLUMN_DAYID, dayId);
      //  return mDb.insert(WDScheduleContract.ScheduleEntry.TABLE_NAME, null, cv);
        long insCount = mDb.insert(WDScheduleContract.ScheduleEntry.TABLE_NAME, "", cv);
        return insCount;
    }

    public int updateSchedule(String daySchedule, int dayId) {
        ContentValues cv = new ContentValues();
        cv.put(WDScheduleContract.ScheduleEntry.COLUMN_DAYSCHEDULE, daySchedule);
        //  return mDb.insert(WDScheduleContract.ScheduleEntry.TABLE_NAME, null, cv);
        int updCount = mDb.update(WDScheduleContract.ScheduleEntry.TABLE_NAME, cv, "id = " + dayId, null );
        return updCount;
    }

    //   (1) Create a new function called removeGuest that takes long id as input and returns a boolean
    public boolean removeGuest (long id){
        return (mDb.delete(WDScheduleContract.ScheduleEntry.TABLE_NAME, WDScheduleContract.ScheduleEntry._ID+ "="+id, null)>0);
    }
}

// TODO Save notes to device-internal SQLite
// TODO Swipe left-right
// TODO Export one - Export All
// TODO import inc vs. import replace
// TODO Calendar content provider cooperation

