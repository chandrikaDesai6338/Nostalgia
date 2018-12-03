package com.work.nostalgia.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import com.work.nostalgia.R;
import com.work.nostalgia.adapter.FeedAdapter;
import com.work.nostalgia.dbManager.AppDatabase;
import com.work.nostalgia.dbManager.FeedsDao;
import com.work.nostalgia.model.FeedModel;
import com.work.nostalgia.utility.utils;

import java.util.ArrayList;
import java.util.List;

public class ViewPost extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "ViewPost";

    RecyclerView.Adapter adapter;
    RecyclerView rvfeeds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rvfeeds = findViewById(R.id.rvfeeds);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FeedsDao feedsDao = AppDatabase.getAppDatabase(this).feedsDao();
        List<FeedModel> feedsList = feedsDao.getAll();
        displayUser(feedsList);


    }
    public static FeedModel[] populateData(Context context){
        Bitmap image1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.feed1);
        byte[] imageb = utils.getInstance().getBytes(image1);
        return new FeedModel[] {
                new FeedModel("John Carter", "Sarah", "text1","Mon Jan 2018 05:16", imageb),
                new FeedModel("William turner", "Elizabeth", "text2","Mon Jan 2018 05:16", imageb),
                new FeedModel("Mary Magdeline", "Christian", "text3","Mon Jan 2018 05:16", imageb),
                new FeedModel("Neville Longbottom", "Loona Lovegood", "text4","Mon Jan 2018 05:16", imageb),
                new FeedModel("Rupert Bellius Weasley", "Hermione Granger", "text5","Mon Jan 2018 05:16", imageb)
        };
    }

    private void displayUser(List<FeedModel> feeds) {
        if (feeds != null) {
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
