package com.amargodigits.weekdaycalendar;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
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
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Scroller;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import java.net.URL;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static String[] daySchedule, dayName;
    static TextView dayScheduleTextView;
    EditText textEdit;
    static String mJSONstr="";
    boolean isEdit; //True if now in Edit mode
    FloatingActionButton myFab;
   static int dayOnScreenID;
    static Toolbar toolbar;
    MenuItem saveMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Resources res=getResources();

        myFab = (FloatingActionButton)findViewById(R.id.fab);

        toolbar = (Toolbar) findViewById(R.id.toolbar);



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        dayScheduleTextView = (TextView) findViewById(R.id.dayScheduleText);
        textEdit = (EditText) findViewById(R.id.textEdit);


        dayScheduleTextView.setMovementMethod(new ScrollingMovementMethod());
        daySchedule = res.getStringArray(R.array.daySchedule);
        dayName = res.getStringArray(R.array.dayName);
        dayOnScreenID=curWeekdayNum()-1;
        showDaySchedule(dayOnScreenID);

        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar , R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

//        getActionBar().setTitle("Your title");


        myFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit)
                {
                    textEdit.setText(dayScheduleTextView.getText());
                    setEdit(false);
                }
                else
                {
                    String text2edit = String.valueOf(dayScheduleTextView.getText());
                    textEdit.setText(text2edit);
                    textEdit.setSelection(textEdit.getText().length());
                    setEdit(true);
                }
            }
        });


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
        dayScheduleTextView.scrollTo(0,0);
        switch (item.getItemId()) {
            case R.id.left:
                if (dayOnScreenID > 0) {
                    dayOnScreenID = dayOnScreenID - 1;
                } else {
                    dayOnScreenID = 6;
                }

                break;

            case R.id.right:
                if (dayOnScreenID < 6) {
                    dayOnScreenID = dayOnScreenID + 1;
                } else {
                    dayOnScreenID = 0;
                }

                break;

            case R.id.save:
            {
                Toast.makeText(this, "Saving...", Toast.LENGTH_SHORT).show();
            }

            break;
        }
        setEdit(false);

        showDaySchedule(dayOnScreenID);
        return true;
    }

public void setEdit(boolean ifEdit) { // Set Edit mode to TRUE or FALSE
    if (ifEdit) {  // Going to Edit Mode
        isEdit=true;
        dayScheduleTextView.setVisibility(View.INVISIBLE);
        textEdit.setVisibility(View.VISIBLE);
        myFab.setImageResource(android.R.drawable.ic_menu_revert);
        saveMenuItem.setVisible(true);

    } else {    // Going to View Mode
        isEdit=false;
        dayScheduleTextView.setVisibility(View.VISIBLE);
        textEdit.setVisibility(View.INVISIBLE);
        myFab.setImageResource(android.R.drawable.ic_menu_edit);
        saveMenuItem.setVisible(false);
    }

}

    public static void showDaySchedule(int dayID){
//        dayScheduleTextView.setText(Html.fromHtml("<font size=\"40\" face=\"arial\" color=\"red\">"+dayName[dayID]+"</font><br/>"));

        toolbar.setTitle(dayName[dayID]);

        Log.i("WD", dayID + " " + dayName[dayID] + " " + " " + toolbar.getTitle() + daySchedule[dayID]);

        dayScheduleTextView.setText(daySchedule[dayID]);
//        dayScheduleTextView.append(mJSONstr);

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

        Log.i("WD", "curWeekdayNum day=" + i);

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
                    Log.i("WD", "mJSONstr="+mJSONstr);

                    class FetchWeatherTask extends AsyncTask<String, Void, String[]> {

                        @Override
                        protected void onPostExecute(String[] result) {
                            super.onPostExecute(result);
                            showDaySchedule(dayOnScreenID);
                        }


                    @Override
                    protected String[] doInBackground(String... params) {

                        if (params.length == 0) {
                            return null;
                        }


// "https://jsonblob.com/api/jsonBlob/d2ed8f05-cb77-11e7-a404-4b06c927b36e"
                        try {
                            Log.i("WD", "in import");
                            URL scheduleRequestUrl = NetworkUtils.buildUrl(mJSONstr);
                            String jsonScheduleResponse = NetworkUtils
                                    .getResponseFromHttpUrl(scheduleRequestUrl);

                            //String[] simpleJsonData =
                                    JSONutils.getSimpleStringsFromJson(jsonScheduleResponse);

                            Log.i("WD", "showDaySchedule(dayOnScreenID)");
                        } catch (Exception e) {
                            Log.i("WD", "exception");
                            e.printStackTrace();
                        }

                    return null;}}
                new FetchWeatherTask().execute("");
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
}

// TODO Save notes to internal SQL
// TODO Swipe left-right
// Export one - Export All

