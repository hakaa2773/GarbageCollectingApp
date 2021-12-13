package com.example.garbagecollectingapp;

import androidx.annotation.NonNull;
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
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class AdminHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private NavigationView nav;
    private FragmentManager frag_man;
    private FragmentTransaction frag_tra;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        nav = findViewById(R.id.adminnavview);
        drawerLayout = findViewById(R.id.adminnavdrawer);
        nav.bringToFront();


        //Initialize fragment
        //Fragment fragment = new AdminMapFragment();

        //open fragment
        //getSupportFragmentManager()
        //        .beginTransaction()
        //       .replace(R.id.frame_layout,fragment)
        //       .commit();


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // nav = findViewById(R.id.navigation);
        loadFragment(new AdminHomeFragment());

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
            case R.id.admin_nav_home:
                loadFragment(new AdminHomeFragment());
                break;
            case R.id.admin_nav_driver:

                Intent int_reg = new Intent(AdminHomeActivity.this,DriverRegisterActivity.class);
                startActivity(int_reg);
                finish();
                break;

        }
        return true;
    }

    }

