package com.example.goplay;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.goplay.Views.AddVenueFrag;
import com.example.goplay.Views.ChallengesFrag;
import com.example.goplay.Views.EditVenuesFrag;
import com.example.goplay.Views.FriendsFrag;
import com.example.goplay.Views.HomeFrag;
import com.example.goplay.Views.SettingsFrag;
import com.example.goplay.Views.UserMapFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    FloatingActionButton fab;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    private FirebaseAuth mAuth;
     private MenuItem adminItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        replaceFragment(new HomeFrag());

        Menu menu = navigationView.getMenu();
        MenuItem adminItem1 = menu.findItem(R.id.nav_addVenue); // Replace with the actual ID of your menu item
        MenuItem adminItem2 = menu.findItem(R.id.nav_EditVenues); // Replace with the actual ID of your menu item
        if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().getEmail().equals("admin@gmail.com")){
            adminItem1.setVisible(true); // Show the admin item
            adminItem2.setVisible(true);
        } else {
            adminItem1.setVisible(false); // Hide the admin item
            adminItem2.setVisible(false);
        }



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_friends) {
                    replaceFragment(new FriendsFrag());
                } else if (id == R.id.nav_home) {
                    replaceFragment(new HomeFrag());
                }else if (id == R.id.nav_search) {
                    replaceFragment(new UserMapFragment());
                } else if (id == R.id.nav_challenges) {
                    replaceFragment(new ChallengesFrag());
                } else if (id == R.id.nav_settings) {
                    replaceFragment(new SettingsFrag());
                } else if (id == R.id.nav_addVenue) {
                    replaceFragment(new AddVenueFrag());
                }else if (id == R.id.nav_EditVenues) {
                    replaceFragment(new EditVenuesFrag());
                } else if (id == R.id.nav_logout) {
                    FBAuthHelper.logout();
                    startActivity(new Intent(MainActivity.this, Login.class));
                    finish();
                }


                return true;
            }
        });

    }
    private void replaceFragment(Fragment toReplace) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.linearLayout, toReplace);
        transaction.commit();
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);

    }
}

