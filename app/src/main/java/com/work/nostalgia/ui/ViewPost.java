package com.work.nostalgia.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.work.nostalgia.R;
import com.work.nostalgia.adapter.FeedAdapter;
import com.work.nostalgia.dbManager.AppDatabase;
import com.work.nostalgia.dbManager.FeedsDao;
import com.work.nostalgia.model.FeedModel;
import com.work.nostalgia.utility.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.work.nostalgia.dbManager.AppDatabase.destroyInstance;

public class ViewPost extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "ViewPost";
    public final int PERMISSION_REQUEST_CODE = 1;

    RecyclerView.Adapter adapter;
    RecyclerView rvfeeds;

    ProgressDialog ringProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rvfeeds = findViewById(R.id.rvfeeds);
        if (!checkPermission()) {
            requestPermission();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FeedsDao feedsDao = AppDatabase.getAppDatabase(this).feedsDao();
        List<FeedModel> feedsList = feedsDao.getAll();
        //loading dialog init
        ringProgressDialog = new ProgressDialog(this);
        ringProgressDialog.setMessage(getString(R.string.loading));
        ringProgressDialog.show();
        displayUser(feedsList);
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }


    public static FeedModel[] populateData(Context context) {
        Bitmap image1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.feed1);
        Bitmap image2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.feed2);
        Bitmap image3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.feed3);
        Bitmap image4 = BitmapFactory.decodeResource(context.getResources(), R.drawable.feed4);
        Bitmap image5 = BitmapFactory.decodeResource(context.getResources(), R.drawable.feed5);
        byte[] imagea = utils.getInstance().getBytes(image1);
        byte[] imageb = utils.getInstance().getBytes(image2);
        byte[] imagec = utils.getInstance().getBytes(image3);
        byte[] imaged = utils.getInstance().getBytes(image4);
        byte[] imagee = utils.getInstance().getBytes(image5);
        return new FeedModel[]{
                new FeedModel("John Carter", "Sarah", "A quadcopter, also called a quadrotor helicopter or quadrotor, is a multirotor helicopter that is lifted and propelled by four rotors.", "Mon Jan 2018 05:16", imagea),
                new FeedModel("William turner", "Elizabeth", "Nintendo is making a bunch of weird DIY cardboardtoys for the Switch and they’re awesome", "Mon Jan 2018 05:16", imageb),
                new FeedModel("Mary Magdeline", "Christian", "Inferno is a 2016 American action mystery thriller film directed by Ron Howard and written by David Koepp", "Mon Jan 2018 05:16", imagec),
                new FeedModel("Neville Longbottom", "Loona Lovegood", "The movie adds Neville “having the hots” for Luna at random, just to make sure everyone apparently pairs off while in high school.", "Mon Jan 2018 05:16", imaged),
                new FeedModel("Rupert Bellius Weasley", "Hermione Granger", "Ron and Hermione become a great couple at the end of the series, but they have a great friendship before that. Despite their bickering and fights, they care about each other, and their loyalty reflects that.", "Mon Jan 2018 05:16", imagee)
        };
    }

    private void displayUser(List<FeedModel> feeds) {
        if (feeds != null && feeds.size() > 0) {
            ringProgressDialog.dismiss();
            Log.d(TAG, "");
            rvfeeds.setLayoutManager(new LinearLayoutManager(ViewPost.this));
            adapter = new FeedAdapter(feeds, ViewPost.this);
            rvfeeds.setAdapter(adapter);
        } else {
            Log.d(TAG, "User response null");
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_post, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.weather) {
            Intent intent1 = new Intent(ViewPost.this, WeatherActivity.class);
            startActivity(intent1);
        } else if (id == R.id.post) {
            Intent intent2 = new Intent(ViewPost.this, AddPostActivity.class);
            startActivity(intent2);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
