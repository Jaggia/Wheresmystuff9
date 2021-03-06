package com.example.jaggia.wheresmystuff9.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.jaggia.wheresmystuff9.R;

public class MainUserScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.make_post) {
            Intent makePostIntent =
                    new Intent(MainUserScreen.this, MakeAPost.class);
            MainUserScreen.this.startActivity(makePostIntent);
        } else if (id == R.id.view_posts) {
            Intent viewPostIntent =
                    new Intent(MainUserScreen.this, ViewPosts.class);
            MainUserScreen.this.startActivity(viewPostIntent);
        } else if (id == R.id.logout) {
            Intent logoutIntent =
                    new Intent(MainUserScreen.this, LoginScreen.class);
            MainUserScreen.this.startActivity(logoutIntent);
        } else if (id == R.id.map_posts) {
            Intent logoutIntent =
                    new Intent(MainUserScreen.this, ViewMapPosts.class);
            MainUserScreen.this.startActivity(logoutIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
