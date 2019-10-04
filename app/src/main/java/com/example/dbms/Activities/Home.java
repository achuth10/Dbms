package com.example.dbms.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.dbms.Fragments.Dashboard;
import com.example.dbms.Fragments.HomePage;
import com.example.dbms.Fragments.Profile;
import com.example.dbms.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wooplr.spotlight.SpotlightView;

public class Home extends AppCompatActivity {
private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        loadFragment(new Dashboard());
        bottomNavigationView.setSelectedItemId(R.id.navigation_dashboard);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;

                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        fragment = new HomePage();
                        break;

                    case R.id.navigation_dashboard:
                        fragment = new Dashboard();
                        break;

                    case R.id.navigation_profile:
                        fragment = new Profile();
                        break;
                }

                return loadFragment(fragment);
            }
        });
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                //Do nothing
            }
        });
    }




    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.FragmentContainer, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
