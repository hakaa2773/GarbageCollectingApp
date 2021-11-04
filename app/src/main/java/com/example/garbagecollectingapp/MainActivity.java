package com.example.garbagecollectingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private NavigationView nav;
    private FragmentManager frag_man;
    private FragmentTransaction frag_tra;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nav = findViewById(R.id.navview);
        drawerLayout = findViewById(R.id.navdrawer);
        nav.bringToFront();

        //Initialize fragment
        Fragment fragment = new MapFragment();

        //open fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout,fragment)
                .commit();



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // nav = findViewById(R.id.navigation);
        loadFragment(new HomeFragment());

        nav.setNavigationItemSelectedListener(this);
    }

    private void loadFragment(Fragment fragment) {
        frag_man = getSupportFragmentManager();
        frag_tra = frag_man.beginTransaction();
        frag_tra.replace(R.id.frame_layout,fragment);
        frag_tra.commit();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                loadFragment(new HomeFragment());
                break;
            case R.id.nav_map:

                loadFragment(new MapFragment());
                break;
            case R.id.nav_search:
                loadFragment(new SearchFragment());
                break;
            case R.id.nav_account:
                if (FirebaseAuth.getInstance().getCurrentUser()==null){
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
                }
                else {
                    loadFragment(new ProfileFragment());
                }
                break;
        }
        return true;
    }
}