package com.example.mediatracker20;

import android.content.Intent;
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

    private ListManager listManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listManager = ListManager.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
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
                File listFile = new File(this.getFilesDir(), "listFile.txt");
                File tagFile = new File(this.getFilesDir(), "tagFile.txt");
                File itemFile = new File(this.getFilesDir(), "itemFile.txt");
                Writer writer = new Writer(listFile, tagFile, itemFile);
                writer.write(ListManager.getInstance());
                writer.close();
                Saver.getInstance().saved();
            } catch (IOException | JSONException e) {
                Log.d("fail save", "save failed");
                e.printStackTrace();
            }
            ;
        }
    }

    @Override
    public void onBackPressed() {
        View f = findViewById(R.id.home_fragment_content);
        if (f != null && f.getTag() == getString(R.string.home_fragment_tag)) {
            finishAffinity();
        } else {
            super.onBackPressed();
        }
    }
}
