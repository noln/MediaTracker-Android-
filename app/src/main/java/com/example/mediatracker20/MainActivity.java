package com.example.mediatracker20;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import model.exceptions.KeyAlreadyExistsException;
import model.model.ItemManager;
import model.model.TagManager;
import model.persistence.ReaderLoader;
import model.persistence.Saver;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;


import model.model.ListManager;
import model.persistence.Writer;

//Main activity to navigate from lists to items
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        try {
            ReaderLoader.loadInfo(ListManager.getInstance(), TagManager.getInstance(), ItemManager.getInstance(), this);
        } catch (KeyAlreadyExistsException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Saver.getInstance().isChanged()) {
            try {
                ReaderLoader.saveProgram(ListManager.getInstance(), TagManager.getInstance(), ItemManager.getInstance(), this);
                Saver.getInstance().saved();
            } catch (JSONException e) {
                Log.d("fail save", "save failed");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        View f = findViewById(R.id.list_fragment_layout);
        if (f != null && f.getTag() == getString(R.string.home_fragment_tag)) {
            finishAffinity();
        } else {
            super.onBackPressed();
        }
    }
}
