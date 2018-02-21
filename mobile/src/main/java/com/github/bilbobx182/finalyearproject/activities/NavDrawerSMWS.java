package com.github.bilbobx182.finalyearproject.activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.bilbobx182.finalyearproject.DBManager;
import com.github.bilbobx182.finalyearproject.R;
import com.github.bilbobx182.finalyearproject.fragments.MenuDefaultFragment;
import com.github.bilbobx182.finalyearproject.fragments.MobileSettingsFragment;
import com.github.bilbobx182.finalyearproject.fragments.MobileWatchSettingsFragment;
import com.github.bilbobx182.finalyearproject.fragments.PreviousSentMessagesFragment;
import com.github.bilbobx182.finalyearproject.fragments.SendMessage;
import com.github.bilbobx182.finalyearproject.fragments.SetupMirrorFragment;
import com.github.bilbobx182.sharedcode.RequestPerformer;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.CapabilityClient;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.Wearable;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;


public class NavDrawerSMWS extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MenuDefaultFragment.OnFragmentInteractionListener,
        SendMessage.OnFragmentInteractionListener,
        MobileSettingsFragment.OnFragmentInteractionListener,
        SetupMirrorFragment.OnFragmentInteractionListener,
        MobileWatchSettingsFragment.OnFragmentInteractionListener,
        PreviousSentMessagesFragment.OnListFragmentInteractionListener {

    TextView navFirstNameTextView;
    TextView navSurnameTextView;
    ImageView navProfileImageView;
    Button setClientInformationButton;
    DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.dbManager = new DBManager(this);

        setContentView(R.layout.activity_nav_drawer_smws);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setHeaderText(navigationView);
        headerButtonPressed();

    }

    private void setHeaderText(NavigationView navigationView) {


        View headerView = navigationView.getHeaderView(0);
        navFirstNameTextView = headerView.findViewById(R.id.navFirstName);
        navSurnameTextView = headerView.findViewById(R.id.navSurname);
        navProfileImageView = headerView.findViewById(R.id.navProfileImage);
        setClientInformationButton = headerView.findViewById(R.id.setClientInformation);

        if (!isFirstTime()) {
            populateHeaderFromDatabase();
        }
    }

    private void populateHeaderFromDatabase() {
        try {
            dbManager.open();
            navFirstNameTextView.setText(dbManager.getUserInformationByColumn("firstname"));
            navSurnameTextView.setText(dbManager.getUserInformationByColumn("surname"));
            String imagePath = dbManager.getUserInformationByColumn("profilePath");


            setClientInformationButton.setVisibility(View.GONE);
            navProfileImageView.setMaxWidth(DrawerLayout.LayoutParams.MATCH_PARENT / 2);
            navProfileImageView.setMaxHeight(DrawerLayout.LayoutParams.MATCH_PARENT / 2);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 5;
            navProfileImageView.setImageBitmap(BitmapFactory.decodeFile(imagePath, options));

        } catch (Exception e) {

        }
    }

    private boolean isFirstTime() {
        boolean result = false;
        try {
            dbManager.open();
            String firstNameValue = dbManager.getUserInformationByColumn("firstname");
            if (firstNameValue.contains("Enter")) {
                result = true;
            }
        } catch (Exception e) {

        }
        return result;
    }

    private void headerButtonPressed() {

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerview = navigationView.getHeaderView(0);

        Button setDetails = headerview.findViewById(R.id.setClientInformation);

        setDetails.setOnClickListener(v -> {
            Fragment fragment = new MobileSettingsFragment();
            switchToFragment(fragment);
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_drawer_smw, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Fragment fragment = new MobileSettingsFragment();
            switchToFragment(fragment);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        Fragment fragment = null;

        // Menu Items
        if (id == R.id.navSendMessage) {
            fragment = new SendMessage();
        } else if (id == R.id.navPreviousSent) {
            fragment = new PreviousSentMessagesFragment();
        } else if (id == R.id.navWatchView) {
            fragment = new MobileWatchSettingsFragment();
        } else if (id == R.id.navSetupMirror) {
            fragment = new SetupMirrorFragment();
        } else if (id == R.id.navSettings) {
            fragment = new MobileWatchSettingsFragment();
        }

        if (fragment != null) {
            switchToFragment(fragment);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //you can leave it empty
    }

    private void switchToFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @Override
    public void onListFragmentInteraction(int position) {
        /*
         Recycler View shows them in reverse order of insert
         10 messages sent. That means latest = 1 in recyclerView but 10 in list.
         0 based index so 0-9 Then we can determine where it's placed by doing Max - (index - 1[because 0 based])
         Index 1 = Latest item = 10 - (1-1) = 10
         Index 2 = 2nd last send message = 10 - (2-1) = 9
         ...... etc
         */
//        GetAllMessageInfo(position);
        RequestPerformer requestPerformer = new RequestPerformer();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
