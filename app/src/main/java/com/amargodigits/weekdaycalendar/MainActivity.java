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
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public String[] daySchedule, dayName;
    TextView dayScheduleTextView;
    EditText textEdit;
    boolean isEdit; //True if now in Edit mode
    FloatingActionButton myFab;

    int dayOnScreenID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Resources res=getResources();
        myFab = (FloatingActionButton)findViewById(R.id.fab);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar , R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        dayScheduleTextView = (TextView) findViewById(R.id.dayScheduleText);
        textEdit = (EditText) findViewById(R.id.textEdit);
        dayScheduleTextView.setMovementMethod(new ScrollingMovementMethod());



        daySchedule = res.getStringArray(R.array.daySchedule);
        dayName = res.getStringArray(R.array.dayName);
        dayOnScreenID=curWeekdayNum()-1;

        showDaySchedule(dayOnScreenID);


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
        }
        setEdit(false);

        showDaySchedule(dayOnScreenID);
        return true;
    }

public void setEdit(boolean ifEdit) { // Set Edit mode to TRUE or FALSE
    if (ifEdit) {
        isEdit=true;
        dayScheduleTextView.setVisibility(View.INVISIBLE);
        textEdit.setVisibility(View.VISIBLE);
        myFab.setImageResource(android.R.drawable.ic_menu_revert);
    } else {
        isEdit=false;
        dayScheduleTextView.setVisibility(View.VISIBLE);
        textEdit.setVisibility(View.INVISIBLE);
        myFab.setImageResource(android.R.drawable.ic_menu_edit);
    }

}

    public void showDaySchedule(int dayID){
//        dayScheduleTextView.setText(Html.fromHtml("<font size=\"40\" face=\"arial\" color=\"red\">"+dayName[dayID]+"</font><br/>"));
        getSupportActionBar().setTitle(dayName[dayID]);
        dayScheduleTextView.setText(daySchedule[dayID]);

    }

    public int curWeekdayNum(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int i=day-1;
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

                if (id == R.id.nav_help){  //Open dialog box
                    android.app.FragmentManager manager = getFragmentManager();
                    //getSupportFragmentManager();
                    MyDialogFragment myDialogFragment = new MyDialogFragment();
                    myDialogFragment.show(manager, "dialog");
                }

                else {

//                    String iurl = getString(R.string.action_insta_link);
//                    //      Toast.makeText(getApplicationContext(), id, Toast.LENGTH_LONG).show();
//                    if (id == R.id.nav_insta) {
//                        iurl = getString(R.string.action_insta_link);
//                    } else if (id == R.id.nav_vk) {
//                        iurl = getString(R.string.action_vk_link);
//                    } else if (id == R.id.nav_youtube) {
//                        iurl = getString(R.string.action_youtube_link);
//                    } else if (id == R.id.nav_shop) {
//                        iurl = getString(R.string.action_shop_link);
//                    }
//                    Intent i2 = new Intent(Intent.ACTION_VIEW, Uri.parse(iurl));
//                    i2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    i2.setPackage("com.android.chrome");
//                    try {
//                        startActivity(i2);
//                    } catch (android.content.ActivityNotFoundException e) {
//                        // Chrome is probably not installed
//                        // Try with the default browser
//                i2.setPackage(null);
//                startActivity(i2);
//            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }


        return true;
    }

    public static class MyDialogFragment extends DialogFragment {
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

}
