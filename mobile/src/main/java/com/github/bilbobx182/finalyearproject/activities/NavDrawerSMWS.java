package com.github.bilbobx182.finalyearproject.activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.github.bilbobx182.finalyearproject.DBManager;
import com.github.bilbobx182.finalyearproject.R;
import com.github.bilbobx182.finalyearproject.fragments.BluetoothSenderFragment;
import com.github.bilbobx182.finalyearproject.fragments.MenuDefaultFragment;
import com.github.bilbobx182.finalyearproject.fragments.MobileSettingsFragment;
import com.github.bilbobx182.finalyearproject.fragments.MobileWatchSettingsFragment;
import com.github.bilbobx182.finalyearproject.fragments.PreviousSentMessagesFragment;
import com.github.bilbobx182.finalyearproject.fragments.SendMessage;
import com.github.bilbobx182.finalyearproject.fragments.SetupMirrorFragment;
import com.github.bilbobx182.sharedcode.RequestPerformer;

import java.sql.SQLException;
import java.util.HashMap;


public class NavDrawerSMWS extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MenuDefaultFragment.OnFragmentInteractionListener,
        SendMessage.OnFragmentInteractionListener,
        MobileSettingsFragment.OnFragmentInteractionListener,
        SetupMirrorFragment.OnFragmentInteractionListener,
        MobileWatchSettingsFragment.OnFragmentInteractionListener,
        PreviousSentMessagesFragment.OnListFragmentInteractionListener,
        BluetoothSenderFragment.OnFragmentInteractionListener{

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

        if (dbManager.isFirstBoot()) {
            switchToFragment(new MobileSettingsFragment());
        }
    }

    private void setHeaderText(NavigationView navigationView) {
        View headerView = navigationView.getHeaderView(0);
        navFirstNameTextView = headerView.findViewById(R.id.navFirstName);
        navSurnameTextView = headerView.findViewById(R.id.navSurname);
        navProfileImageView = headerView.findViewById(R.id.navProfileImage);
        setClientInformationButton = headerView.findViewById(R.id.setClientInformation);

        if (getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstTime", true)){
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
            Toast.makeText(this, "Oh snap! You broke me! POPULATE HEADERS", Toast.LENGTH_SHORT).show();
        }
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
        getMenuInflater().inflate(R.menu.nav_drawer_smw, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Fragment fragment = new MobileSettingsFragment();
            switchToFragment(fragment);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
    }

    private void switchToFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @Override
    public void onListFragmentInteraction(int position) {
        int maxMessageCount = 0;
        try {
            dbManager.open();
            maxMessageCount = dbManager.getMessageCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        HashMap<String, String> previouslySentMessageHashMap = dbManager.getMessageByIndex((maxMessageCount - position));
        previouslySentMessageHashMap.put("queueurl", dbManager.getUserInformationByColumn("queue"));

        RequestPerformer requestPerformer = new RequestPerformer();
        requestPerformer.performSendMessage(previouslySentMessageHashMap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
