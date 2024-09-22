package com.example.finalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.homeBottomNevigationMenuHome);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.homeBottomNevigationMenuSubHome);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.homeMylocation)
        {

        } else if (item.getItemId() == R.id.homeContacts) {

        } else if (item.getItemId() == R.id.homeMyProfile) {
            Intent intent = new Intent(HomeActivity.this, MyProfileActivity.class);
            startActivity(intent);
        }
        return true;
    }

    HomeFragment1 homeFragment1 = new HomeFragment1();
    PersonalDetailsFragment personalDetailsFragment = new PersonalDetailsFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.homeBottomNevigationMenuSubHome)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeFrameLayout,homeFragment1).commit();
        } else if (item.getItemId() == R.id.homeBottomNevigationMenuPersonalDetails) {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeFrameLayout,personalDetailsFragment).commit();
        }


        return true;
    }
}