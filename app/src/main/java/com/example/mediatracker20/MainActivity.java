package com.example.mediatracker20;

import android.content.Context;
import android.os.Bundle;
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
import model.jsonreaders.ItemManagerDocument;
import model.jsonreaders.ListManagerDocument;
import model.jsonreaders.TagManagerDocument;
import model.model.ItemManager;
import model.model.MediaItem;
import model.model.MediaList;
import model.model.Tag;
import model.model.TagManager;
import model.persistence.ReaderLoader;
import model.persistence.Saver;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import model.model.ListManager;

//Main activity to navigate from lists to items
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        if (Saver.getInstance().isChanged()) {
            db = FirebaseFirestore.getInstance();
            FirebaseAuth auth = FirebaseAuth.getInstance();
            Map<String, Object> data = new HashMap<>();
            data.put("itemManager", ItemManager.getInstance().getAllMediaItems());
            data.put("listManager", ListManager.getInstance().getAllLists());
            data.put("tagManager", TagManager.getInstance().getAllTags());
            db.collection("users").document(auth.getUid()).set(data);
        }
        super.onStop();
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
